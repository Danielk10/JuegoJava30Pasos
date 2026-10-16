package com.diamon.curso.binary;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

/**
 * Motor multialgoritmo de verificación de integridad y checksums criptográficos.
 *
 * Implementa el Pilar 1 de Flash SPI Tool PRO:
 * - Hash streaming concurrente en 1 solo paso sin riesgo de OutOfMemory (16MB, 32MB, 64MB).
 * - Algoritmos soportados: MD5 (128b), SHA-1 (160b), SHA-256 (256b) y CRC32 (32b).
 * - Detección y cotejo inteligente contra el portapapeles (Clipboard Match).
 * - Generación y verificación de archivos sidecar estándar GNU (.sha256 y .md5) compatibles con sha256sum -c.
 */
public final class IntegrityManager {

    /** Tamaño del búfer de lectura en streaming (256 KB). */
    public static final int BUFFER_SIZE = 256 * 1024;

    private static final Pattern HEX_64 = Pattern.compile("\\b([a-fA-F0-9]{64})\\b");
    private static final Pattern HEX_40 = Pattern.compile("\\b([a-fA-F0-9]{40})\\b");
    private static final Pattern HEX_32 = Pattern.compile("\\b([a-fA-F0-9]{32})\\b");
    private static final Pattern HEX_8  = Pattern.compile("\\b([a-fA-F0-9]{8})\\b");

    private IntegrityManager() {
        // Clase de utilidades estáticas
    }

    /**
     * Interfaz para reportar el progreso del cálculo en streaming.
     */
    public interface HashProgressListener {
        void onProgress(long bytesProcessed, long totalBytes);
    }

    /**
     * Contenedor inmutable de los resultados de hash de un archivo o flujo binario.
     */
    public static final class HashResult {
        private final String md5;
        private final String sha1;
        private final String sha256;
        private final String crc32;
        private final long byteCount;

        public HashResult(String md5, String sha1, String sha256, String crc32, long byteCount) {
            this.md5 = md5 != null ? md5.toLowerCase(Locale.US) : "";
            this.sha1 = sha1 != null ? sha1.toLowerCase(Locale.US) : "";
            this.sha256 = sha256 != null ? sha256.toLowerCase(Locale.US) : "";
            this.crc32 = crc32 != null ? crc32.toLowerCase(Locale.US) : "";
            this.byteCount = byteCount;
        }

        public String getMd5() {
            return md5;
        }

        public String getSha1() {
            return sha1;
        }

        public String getSha256() {
            return sha256;
        }

        public String getCrc32() {
            return crc32;
        }

        public long getByteCount() {
            return byteCount;
        }

        public Map<String, String> asMap() {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("MD5", md5);
            map.put("SHA-1", sha1);
            map.put("SHA-256", sha256);
            map.put("CRC32", crc32);
            return Collections.unmodifiableMap(map);
        }

        public String formatSummary() {
            StringBuilder sb = new StringBuilder();
            sb.append("Tamaño: ").append(byteCount).append(" bytes\n");
            sb.append("MD5:     ").append(md5).append("\n");
            sb.append("SHA-1:   ").append(sha1).append("\n");
            sb.append("SHA-256: ").append(sha256).append("\n");
            sb.append("CRC32:   ").append(crc32);
            return sb.toString();
        }

        @Override
        public String toString() {
            return "HashResult{" +
                    "bytes=" + byteCount +
                    ", md5='" + md5 + '\'' +
                    ", sha1='" + sha1 + '\'' +
                    ", sha256='" + sha256 + '\'' +
                    ", crc32='" + crc32 + '\'' +
                    '}';
        }
    }

    /**
     * Resultado del cotejo contra el portapapeles.
     */
    public static final class ClipboardMatchResult {
        private final boolean match;
        private final String algorithm;
        private final String matchedHash;
        private final String clipboardText;
        private final String message;

        public ClipboardMatchResult(boolean match, String algorithm, String matchedHash,
                                    String clipboardText, String message) {
            this.match = match;
            this.algorithm = algorithm;
            this.matchedHash = matchedHash;
            this.clipboardText = clipboardText;
            this.message = message;
        }

        public boolean isMatch() {
            return match;
        }

        public boolean isMatched() {
            return match;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        public String getMatchedAlgorithm() {
            return algorithm != null ? algorithm : "";
        }

        public boolean hasClipboardHash() {
            return algorithm != null && !algorithm.isEmpty();
        }

        public String getMatchedHash() {
            return matchedHash;
        }

        public String getClipboardText() {
            return clipboardText;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "ClipboardMatchResult{" +
                    "match=" + match +
                    ", algorithm='" + algorithm + '\'' +
                    ", matchedHash='" + matchedHash + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // 1. CÁLCULO DE HASH MULTIALGORITMO EN STREAMING (ZERO OOM)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Calcula simultáneamente MD5, SHA-1, SHA-256 y CRC32 sobre un archivo.
     *
     * @param file     Archivo binario a procesar (ej. dump BIOS).
     * @param listener Listener opcional de progreso (puede ser null).
     * @return Contenedor con todos los hashes calculados.
     * @throws IOException Si ocurre un error de lectura.
     */
    public static HashResult computeHashes(File file, HashProgressListener listener) throws IOException {
        if (file == null || !file.exists() || !file.isFile()) {
            throw new IOException("Archivo no existe o es inválido: " + file);
        }
        long totalBytes = file.length();
        try (InputStream in = new FileInputStream(file)) {
            return computeHashes(in, totalBytes, listener);
        }
    }

    /**
     * Sobrecarga conveniente sin listener de progreso.
     */
    public static HashResult computeHashes(File file) throws IOException {
        return computeHashes(file, null);
    }

    /**
     * Calcula los 4 hashes desde un InputStream genérico.
     */
    public static HashResult computeHashes(InputStream in, long totalBytes, HashProgressListener listener) throws IOException {
        MessageDigest md5;
        MessageDigest sha1;
        MessageDigest sha256;
        try {
            md5 = MessageDigest.getInstance("MD5");
            sha1 = MessageDigest.getInstance("SHA-1");
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo de hash no soportado por la plataforma", e);
        }
        CRC32 crc32 = new CRC32();

        byte[] buffer = new byte[BUFFER_SIZE];
        long bytesProcessed = 0;
        int read;

        while ((read = in.read(buffer)) != -1) {
            if (read > 0) {
                md5.update(buffer, 0, read);
                sha1.update(buffer, 0, read);
                sha256.update(buffer, 0, read);
                crc32.update(buffer, 0, read);
                bytesProcessed += read;
                if (listener != null) {
                    listener.onProgress(bytesProcessed, totalBytes);
                }
            }
        }

        String md5Hex = bytesToHex(md5.digest());
        String sha1Hex = bytesToHex(sha1.digest());
        String sha256Hex = bytesToHex(sha256.digest());
        String crc32Hex = String.format(Locale.US, "%08x", crc32.getValue());

        return new HashResult(md5Hex, sha1Hex, sha256Hex, crc32Hex, bytesProcessed);
    }

    /**
     * Calcula hashes sobre un búfer de bytes en memoria (útil para pruebas y bloques pequeños).
     */
    public static HashResult computeHashes(byte[] data) {
        if (data == null) {
            data = new byte[0];
        }
        MessageDigest md5;
        MessageDigest sha1;
        MessageDigest sha256;
        try {
            md5 = MessageDigest.getInstance("MD5");
            sha1 = MessageDigest.getInstance("SHA-1");
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo de hash no soportado", e);
        }
        CRC32 crc32 = new CRC32();

        md5.update(data);
        sha1.update(data);
        sha256.update(data);
        crc32.update(data);

        return new HashResult(
                bytesToHex(md5.digest()),
                bytesToHex(sha1.digest()),
                bytesToHex(sha256.digest()),
                String.format(Locale.US, "%08x", crc32.getValue()),
                data.length
        );
    }

    // ════════════════════════════════════════════════════════════════════════
    // 2. COTEJO CON EL PORTAPAPELES (CLIPBOARD MATCH)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Lee el portapapeles de Android y lo coteja contra el archivo especificado.
     */
    public static ClipboardMatchResult checkClipboardMatch(Context context, File file) throws IOException {
        String clipText = getClipboardText(context);
        HashResult hashes = computeHashes(file);
        return checkClipboardMatch(clipText, hashes);
    }

    /**
     * Coteja un texto dado (proveniente del portapapeles o de una entrada de texto) contra un HashResult.
     */
    public static ClipboardMatchResult checkClipboardMatch(String clipText, HashResult hashes) {
        if (hashes == null) {
            return new ClipboardMatchResult(false, null, null, clipText, "Hashes de archivo no disponibles.");
        }
        return checkClipboardMatch(clipText, hashes.asMap());
    }

    /**
     * Coteja texto arbitrario contra un mapa de algoritmos y hashes.
     */
    public static ClipboardMatchResult checkClipboardMatch(String clipText, Map<String, String> hashes) {
        if (clipText == null || clipText.trim().isEmpty()) {
            return new ClipboardMatchResult(false, null, null, "", "Portapapeles vacío o sin texto.");
        }

        String raw = clipText.trim();
        Map<String, String> normalized = new LinkedHashMap<>();
        if (hashes != null) {
            for (Map.Entry<String, String> entry : hashes.entrySet()) {
                if (entry.getValue() != null) {
                    normalized.put(entry.getKey().toUpperCase(Locale.US),
                            entry.getValue().trim().toLowerCase(Locale.US));
                }
            }
        }

        // 1. Búsqueda de tokens hex en el texto (prioridad de mayor a menor longitud criptográfica)
        // Intentar SHA-256 (64 hex)
        Matcher m64 = HEX_64.matcher(raw);
        while (m64.find()) {
            String token = m64.group(1).toLowerCase(Locale.US);
            String expected = normalized.get("SHA-256");
            if (expected != null && expected.equalsIgnoreCase(token)) {
                return new ClipboardMatchResult(true, "SHA-256", token, raw,
                        "MATCH CERTIFIED: El archivo coincide exactamente con el hash oficial del fabricante (SHA-256). Es 100% seguro para flashear.");
            } else if (expected != null) {
                return new ClipboardMatchResult(false, "SHA-256", token, raw,
                        "INTEGRITY WARNING: El hash SHA-256 del portapapeles (" + token + ") NO coincide con el archivo (" + expected + "). Riesgo de archivo corrupto o modelo incompatible.");
            }
        }

        // Intentar SHA-1 (40 hex)
        Matcher m40 = HEX_40.matcher(raw);
        while (m40.find()) {
            String token = m40.group(1).toLowerCase(Locale.US);
            String expected = normalized.get("SHA-1");
            if (expected != null && expected.equalsIgnoreCase(token)) {
                return new ClipboardMatchResult(true, "SHA-1", token, raw,
                        "MATCH CERTIFIED: El archivo coincide con el hash SHA-1 del portapapeles.");
            } else if (expected != null) {
                return new ClipboardMatchResult(false, "SHA-1", token, raw,
                        "INTEGRITY WARNING: El hash SHA-1 del portapapeles NO coincide con el archivo.");
            }
        }

        // Intentar MD5 (32 hex)
        Matcher m32 = HEX_32.matcher(raw);
        while (m32.find()) {
            String token = m32.group(1).toLowerCase(Locale.US);
            String expected = normalized.get("MD5");
            if (expected != null && expected.equalsIgnoreCase(token)) {
                return new ClipboardMatchResult(true, "MD5", token, raw,
                        "MATCH CERTIFIED: El archivo coincide con el hash MD5 oficial del fabricante.");
            } else if (expected != null) {
                return new ClipboardMatchResult(false, "MD5", token, raw,
                        "INTEGRITY WARNING: El hash MD5 del portapapeles NO coincide con el archivo.");
            }
        }

        // Intentar CRC32 (8 hex)
        Matcher m8 = HEX_8.matcher(raw);
        while (m8.find()) {
            String token = m8.group(1).toLowerCase(Locale.US);
            String expected = normalized.get("CRC32");
            if (expected != null && expected.equalsIgnoreCase(token)) {
                return new ClipboardMatchResult(true, "CRC32", token, raw,
                        "MATCH CERTIFIED: El archivo coincide con el CRC32 indicado.");
            }
        }

        return new ClipboardMatchResult(false, null, null, raw,
                "No se detectó ninguna firma criptográfica conocida (SHA-256, SHA-1, MD5, CRC32) en el portapapeles.");
    }

    /**
     * Obtiene el texto primario del portapapeles de Android de forma segura.
     */
    public static String getClipboardText(Context context) {
        if (context == null) return null;
        try {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null && clipboard.hasPrimaryClip()) {
                ClipData clip = clipboard.getPrimaryClip();
                if (clip != null && clip.getItemCount() > 0) {
                    CharSequence cs = clip.getItemAt(0).getText();
                    return cs != null ? cs.toString() : null;
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    // ════════════════════════════════════════════════════════════════════════
    // 3. GENERACIÓN Y VERIFICACIÓN DE ARCHIVOS SIDECAR GNU (.sha256 / .md5)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Genera el archivo sidecar estándar GNU .sha256 (compatible con `sha256sum -c`).
     * Formato: "<hash>  <filename>\n"
     *
     * @param binFile    Archivo binario original.
     * @param sha256Hex  Hash SHA-256 en formato hex (si es null, se computará).
     * @return El archivo sidecar generado.
     */
    public static File generateSha256Sidecar(File binFile, String sha256Hex) throws IOException {
        if (binFile == null || !binFile.exists()) {
            throw new IOException("Archivo binario no existe: " + binFile);
        }
        if (sha256Hex == null || sha256Hex.isEmpty()) {
            sha256Hex = computeHashes(binFile).getSha256();
        }
        File sidecar = new File(binFile.getParentFile(), binFile.getName() + ".sha256");
        String content = sha256Hex.toLowerCase(Locale.US) + "  " + binFile.getName() + "\n";
        writeFileString(sidecar, content);
        return sidecar;
    }

    /**
     * Genera el archivo sidecar estándar GNU .md5 (compatible con `md5sum -c`).
     */
    public static File generateMd5Sidecar(File binFile, String md5Hex) throws IOException {
        if (binFile == null || !binFile.exists()) {
            throw new IOException("Archivo binario no existe: " + binFile);
        }
        if (md5Hex == null || md5Hex.isEmpty()) {
            md5Hex = computeHashes(binFile).getMd5();
        }
        File sidecar = new File(binFile.getParentFile(), binFile.getName() + ".md5");
        String content = md5Hex.toLowerCase(Locale.US) + "  " + binFile.getName() + "\n";
        writeFileString(sidecar, content);
        return sidecar;
    }

    /**
     * Genera ambos archivos sidecar (.sha256 y .md5) a partir de un resultado de hashes previo.
     */
    public static void generateSidecars(File binFile, HashResult result) throws IOException {
        if (result == null) {
            result = computeHashes(binFile);
        }
        generateSha256Sidecar(binFile, result.getSha256());
        generateMd5Sidecar(binFile, result.getMd5());
    }

    /**
     * Verifica la integridad de un archivo a través de su archivo sidecar GNU (.sha256 o .md5).
     *
     * @param sidecarFile Archivo .sha256 o .md5.
     * @return true si el archivo apuntado por el sidecar existe y su hash coincide exactamente.
     */
    public static boolean verifySidecar(File sidecarFile) throws IOException {
        if (sidecarFile == null || !sidecarFile.exists() || !sidecarFile.isFile()) {
            throw new IOException("Archivo sidecar inexistente: " + sidecarFile);
        }

        String name = sidecarFile.getName().toLowerCase(Locale.US);
        boolean isSha256 = name.endsWith(".sha256");
        boolean isMd5 = name.endsWith(".md5");

        if (!isSha256 && !isMd5) {
            throw new IllegalArgumentException("Extensión de sidecar no soportada (esperado .sha256 o .md5): " + sidecarFile);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(sidecarFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                // Formato estándar GNU: "<hash> [* ]<filename>"
                int splitIdx = line.indexOf(' ');
                if (splitIdx <= 0) continue;

                String expectedHash = line.substring(0, splitIdx).trim().toLowerCase(Locale.US);
                String targetFileName = line.substring(splitIdx).trim();
                if (targetFileName.startsWith("*")) {
                    targetFileName = targetFileName.substring(1).trim();
                }

                File targetFile = new File(sidecarFile.getParentFile(), targetFileName);
                if (!targetFile.exists() || !targetFile.isFile()) {
                    // Intento de fallback: nombre del sidecar sin la extensión .sha256/.md5
                    String fallbackName = sidecarFile.getName().substring(0, sidecarFile.getName().lastIndexOf('.'));
                    targetFile = new File(sidecarFile.getParentFile(), fallbackName);
                    if (!targetFile.exists()) {
                        throw new IOException("El archivo referenciado en el sidecar no existe: " + targetFileName);
                    }
                }

                HashResult actualHashes = computeHashes(targetFile);
                String actualHash = isSha256 ? actualHashes.getSha256() : actualHashes.getMd5();

                return expectedHash.equalsIgnoreCase(actualHash);
            }
        }
        return false;
    }

    // ════════════════════════════════════════════════════════════════════════
    // UTILIDADES PRIVADAS
    // ════════════════════════════════════════════════════════════════════════

    private static String bytesToHex(byte[] bytes) {
        if (bytes == null) return "";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            int v = b & 0xFF;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }

    private static void writeFileString(File file, String content) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            fos.flush();
        }
    }
}
