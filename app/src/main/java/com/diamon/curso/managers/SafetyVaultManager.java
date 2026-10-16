package com.diamon.curso.managers;

import android.content.Context;

import com.diamon.curso.binary.IntegrityManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Bóveda de Seguridad Antibrick (Safety Vault) y gestor de snapshots pre-flash.
 *
 * Implementa el Pilar 4 de Flash SPI Tool PRO:
 * - Creación atómica de snapshots automáticos pre-flash antes de cualquier comando destructivo (-w / --erase).
 * - Nombrado canónico: vault/backup_<yyyyMMdd_HHmmss>_<chip>.bin con metadatos asociados (.meta).
 * - Registro de hash SHA-256 criptográfico para certificar la integridad de cada respaldo.
 * - Soporte para "🚨 Rollback de Emergencia en 1 Clic" devolviendo el snapshot más reciente.
 * - Poda automática (pruning) por cantidad o antigüedad para evitar saturar el almacenamiento del dispositivo.
 */
public class SafetyVaultManager {

    public static final String VAULT_DIR_NAME = "vault";
    public static final int DEFAULT_MAX_BACKUPS_TO_KEEP = 15;

    private static SafetyVaultManager sInstance;

    private final File vaultDir;

    /**
     * Información descriptiva de un snapshot almacenado en la bóveda.
     */
    public static final class VaultBackup implements Comparable<VaultBackup> {
        private final File file;
        private final File metaFile;
        private final String chipName;
        private final long timestamp;
        private final long sizeBytes;
        private final String sha256;

        public VaultBackup(File file, File metaFile, String chipName, long timestamp, long sizeBytes, String sha256) {
            this.file = file;
            this.metaFile = metaFile;
            this.chipName = chipName != null ? chipName : "SPI_CHIP";
            this.timestamp = timestamp;
            this.sizeBytes = sizeBytes;
            this.sha256 = sha256 != null ? sha256 : "";
        }

        public File getFile() {
            return file;
        }

        public File getMetaFile() {
            return metaFile;
        }

        public String getChipName() {
            return chipName;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public long getSizeBytes() {
            return sizeBytes;
        }

        public String getSha256() {
            return sha256;
        }

        public String getFormattedDate() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            return sdf.format(new Date(timestamp));
        }

        public String getSummary() {
            return String.format(Locale.US,
                    "Chip: %s | Fecha: %s | Tamaño: %s | SHA256: %s",
                    chipName, getFormattedDate(), formatFileSize(sizeBytes),
                    sha256.isEmpty() ? "N/A" : sha256.substring(0, Math.min(8, sha256.length())) + "...");
        }

        @Override
        public int compareTo(VaultBackup other) {
            // Orden descendente por fecha (el más reciente primero)
            return Long.compare(other.timestamp, this.timestamp);
        }

        @Override
        public String toString() {
            return "VaultBackup{" +
                    "file=" + file.getName() +
                    ", chip='" + chipName + '\'' +
                    ", date=" + getFormattedDate() +
                    ", size=" + sizeBytes +
                    '}';
        }
    }

    /**
     * Constructor para Android que usa el almacenamiento interno privado de la aplicación.
     */
    public SafetyVaultManager(Context context) {
        this(new File(context.getFilesDir(), VAULT_DIR_NAME));
    }

    /**
     * Constructor con directorio directo (para inyección de dependencias y pruebas unitarias en JVM).
     */
    public SafetyVaultManager(File vaultDir) {
        this.vaultDir = vaultDir;
        if (!this.vaultDir.exists()) {
            this.vaultDir.mkdirs();
        }
    }

    public static synchronized SafetyVaultManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SafetyVaultManager(context.getApplicationContext());
        }
        return sInstance;
    }

    public File getVaultDir() {
        return vaultDir;
    }

    // ════════════════════════════════════════════════════════════════════════
    // 1. CREACIÓN DE SNAPSHOTS PRE-FLASH (ANTIBRICK)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Crea un snapshot de seguridad a partir de un archivo binario existente leído del chip.
     * La operación es atómica: se escribe en un archivo temporal .tmp y luego se renombra a .bin.
     *
     * @param sourceDump Archivo que contiene el volcado previo del chip.
     * @param chipName   Nombre o identificador del chip SPI (ej. "W25Q128FV").
     * @return El objeto VaultBackup registrado.
     * @throws IOException Si ocurre un error de E/S.
     */
    public VaultBackup createPreFlashSnapshot(File sourceDump, String chipName) throws IOException {
        if (sourceDump == null || !sourceDump.exists() || !sourceDump.isFile()) {
            throw new IOException("Archivo de origen inválido para crear snapshot: " + sourceDump);
        }

        String safeChip = sanitizeChipName(chipName);
        long now = System.currentTimeMillis();
        String dateStr = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date(now));

        String baseName = String.format(Locale.US, "backup_%s_%s", dateStr, safeChip);
        File targetBin = new File(vaultDir, baseName + ".bin");
        File tempBin = new File(vaultDir, baseName + ".tmp");
        File metaFile = new File(vaultDir, baseName + ".meta");

        // Copiar atómicamente al archivo temporal
        copyFile(sourceDump, tempBin);

        if (!tempBin.renameTo(targetBin)) {
            // Fallback si renameTo falla entre particiones
            copyFile(tempBin, targetBin);
            tempBin.delete();
        }

        // Calcular hash SHA-256 para el metadato
        String sha256 = IntegrityManager.computeHashes(targetBin).getSha256();

        // Escribir archivo de metadatos (.meta)
        writeMetadata(metaFile, safeChip, now, targetBin.length(), sha256);

        // Pruning automático preventivo
        pruneOldBackups(DEFAULT_MAX_BACKUPS_TO_KEEP);

        return new VaultBackup(targetBin, metaFile, safeChip, now, targetBin.length(), sha256);
    }

    /**
     * Crea un snapshot pre-flash a partir de un array de bytes en memoria.
     */
    public VaultBackup createPreFlashSnapshot(byte[] data, String chipName) throws IOException {
        if (data == null) {
            throw new IOException("Datos de volcado nulos");
        }

        String safeChip = sanitizeChipName(chipName);
        long now = System.currentTimeMillis();
        String dateStr = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date(now));

        String baseName = String.format(Locale.US, "backup_%s_%s", dateStr, safeChip);
        File targetBin = new File(vaultDir, baseName + ".bin");
        File tempBin = new File(vaultDir, baseName + ".tmp");
        File metaFile = new File(vaultDir, baseName + ".meta");

        try (FileOutputStream fos = new FileOutputStream(tempBin)) {
            fos.write(data);
            fos.flush();
            fos.getFD().sync();
        }

        if (!tempBin.renameTo(targetBin)) {
            copyFile(tempBin, targetBin);
            tempBin.delete();
        }

        String sha256 = IntegrityManager.computeHashes(targetBin).getSha256();
        writeMetadata(metaFile, safeChip, now, targetBin.length(), sha256);

        pruneOldBackups(DEFAULT_MAX_BACKUPS_TO_KEEP);

        return new VaultBackup(targetBin, metaFile, safeChip, now, targetBin.length(), sha256);
    }

    // ════════════════════════════════════════════════════════════════════════
    // 2. GESTIÓN Y LISTADO DE BACKUPS (ROLLBACK EN 1 CLIC)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Lista todos los backups existentes en la bóveda, ordenados del más reciente al más antiguo.
     */
    public List<VaultBackup> listBackups() {
        List<VaultBackup> list = new ArrayList<>();
        if (!vaultDir.exists() || !vaultDir.isDirectory()) {
            return list;
        }

        File[] files = vaultDir.listFiles((dir, name) -> name.startsWith("backup_") && name.endsWith(".bin"));
        if (files == null) return list;

        for (File f : files) {
            String base = f.getName().substring(0, f.getName().length() - 4);
            File meta = new File(vaultDir, base + ".meta");
            VaultBackup vb = loadBackup(f, meta);
            if (vb != null) {
                list.add(vb);
            }
        }

        Collections.sort(list);
        return list;
    }

    /**
     * Obtiene el snapshot más reciente almacenado en la bóveda para Rollback de Emergencia en 1 Clic.
     *
     * @return El backup más reciente, o null si la bóveda está vacía.
     */
    public VaultBackup getLatestBackup() {
        List<VaultBackup> backups = listBackups();
        if (!backups.isEmpty()) {
            return backups.get(0);
        }
        return null;
    }

    /**
     * Obtiene el último snapshot grabado específicamente para un chip dado.
     */
    public VaultBackup getLatestBackupForChip(String chipName) {
        String safeChip = sanitizeChipName(chipName);
        for (VaultBackup vb : listBackups()) {
            if (vb.getChipName().equalsIgnoreCase(safeChip)) {
                return vb;
            }
        }
        return null;
    }

    /**
     * Restaura un snapshot de la bóveda copiándolo a la ruta destino para su posterior flasheo.
     *
     * @param backup          Snapshot de la bóveda.
     * @param destinationFile Archivo de destino donde se restaurará la imagen binaria.
     * @return true si la restauración fue exitosa.
     */
    public boolean restoreBackup(VaultBackup backup, File destinationFile) {
        if (backup == null || backup.getFile() == null || !backup.getFile().exists()) {
            return false;
        }
        try {
            copyFile(backup.getFile(), destinationFile);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Elimina un snapshot de la bóveda junto a su archivo de metadatos.
     */
    public boolean deleteBackup(VaultBackup backup) {
        if (backup == null) return false;
        boolean binDeleted = false;
        if (backup.getFile() != null && backup.getFile().exists()) {
            binDeleted = backup.getFile().delete();
        }
        if (backup.getMetaFile() != null && backup.getMetaFile().exists()) {
            backup.getMetaFile().delete();
        }
        return binDeleted;
    }

    /**
     * Limpia snapshots antiguos manteniendo únicamente los N más recientes.
     */
    public int pruneOldBackups(int maxKeepCount) {
        if (maxKeepCount <= 0) return 0;
        List<VaultBackup> backups = listBackups();
        int deleted = 0;
        if (backups.size() > maxKeepCount) {
            for (int i = maxKeepCount; i < backups.size(); i++) {
                if (deleteBackup(backups.get(i))) {
                    deleted++;
                }
            }
        }
        return deleted;
    }

    /**
     * Calcula el espacio total en bytes consumido por los archivos binarios de la bóveda.
     */
    public long getTotalVaultSizeBytes() {
        long total = 0;
        for (VaultBackup vb : listBackups()) {
            total += vb.getSizeBytes();
        }
        return total;
    }

    // ════════════════════════════════════════════════════════════════════════
    // UTILIDADES PRIVADAS
    // ════════════════════════════════════════════════════════════════════════

    private VaultBackup loadBackup(File binFile, File metaFile) {
        String chip = "SPI_CHIP";
        long timestamp = binFile.lastModified();
        long size = binFile.length();
        String sha256 = "";

        if (metaFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(metaFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    int eq = line.indexOf('=');
                    if (eq > 0) {
                        String k = line.substring(0, eq).trim();
                        String v = line.substring(eq + 1).trim();
                        if ("chip".equalsIgnoreCase(k)) chip = v;
                        else if ("timestamp".equalsIgnoreCase(k)) timestamp = Long.parseLong(v);
                        else if ("sha256".equalsIgnoreCase(k)) sha256 = v;
                    }
                }
            } catch (Exception ignored) {
            }
        } else {
            // Intentar inferir chip del nombre de archivo: backup_YYYYMMdd_HHmmss_<chip>.bin
            String name = binFile.getName();
            String[] parts = name.split("_");
            if (parts.length >= 4) {
                chip = parts[3].replace(".bin", "");
            }
        }

        return new VaultBackup(binFile, metaFile, chip, timestamp, size, sha256);
    }

    private void writeMetadata(File metaFile, String chip, long timestamp, long size, String sha256) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("chip=").append(chip).append("\n");
        sb.append("timestamp=").append(timestamp).append("\n");
        sb.append("size=").append(size).append("\n");
        sb.append("sha256=").append(sha256).append("\n");
        try (FileOutputStream fos = new FileOutputStream(metaFile)) {
            fos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
            fos.flush();
        }
    }

    private static String sanitizeChipName(String chipName) {
        if (chipName == null || chipName.trim().isEmpty()) {
            return "UNKNOWN_CHIP";
        }
        return chipName.replaceAll("[^a-zA-Z0-9_.-]", "_");
    }

    private static void copyFile(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[IntegrityManager.BUFFER_SIZE];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.flush();
        }
    }

    public static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format(Locale.US, "%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}
