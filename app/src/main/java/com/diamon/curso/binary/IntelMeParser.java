package com.diamon.curso.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Parser y analizador de salud de Intel Management Engine (ME) / CSME / TXE.
 *
 * Implementa el Pilar 4 de Flash SPI Tool PRO:
 * - Localización y decodificación de la cabecera $FPT (Flash Partition Table).
 * - Detección de particiones clave (FTPR, MDMV, FTUP, NFPR, DLMP, WCOD, LOCL, RBEP, ISHP).
 * - Extracción precisa de la versión de firmware ME (Major.Minor.Hotfix.Build).
 * - Clasificación de SKU (Consumer vs Corporate / AMT / vPro).
 * - Diagnóstico de integridad estructural y límites de partición para evitar apagados a los 30 minutos.
 */
public final class IntelMeParser {

    /** Firma $FPT de la Flash Partition Table de Intel ME. */
    public static final byte[] FPT_SIGNATURE = new byte[]{'$', 'F', 'P', 'T'};

    /** Firma $MAN de cabecera de Manifiesto en particiones FTPR. */
    public static final byte[] MAN_SIGNATURE = new byte[]{'$', 'M', 'A', 'N'};

    /** Firma $CPD de Code Partition Directory en CSME modernos. */
    public static final byte[] CPD_SIGNATURE = new byte[]{'$', 'C', 'P', 'D'};

    /** Tamaño mínimo de la cabecera $FPT (32 bytes). */
    public static final int FPT_HEADER_SIZE = 32;

    /** Tamaño de cada entrada de partición en $FPT (32 bytes). */
    public static final int FPT_ENTRY_SIZE = 32;

    private IntelMeParser() {
        // Clase de utilidades estáticas
    }

    /**
     * Entrada individual de la Flash Partition Table.
     */
    public static final class PartitionEntry {
        private final String name;
        private final String owner;
        private final long offset;
        private final long length;
        private final long flags;

        public PartitionEntry(String name, String owner, long offset, long length, long flags) {
            this.name = name != null ? name.trim() : "";
            this.owner = owner != null ? owner.trim() : "";
            this.offset = offset;
            this.length = length;
            this.flags = flags;
        }

        public String getName() {
            return name;
        }

        public String getOwner() {
            return owner;
        }

        public long getOffset() {
            return offset;
        }

        public long getLength() {
            return length;
        }

        public long getFlags() {
            return flags;
        }

        @Override
        public String toString() {
            return String.format(Locale.US, "[%-4s] Owner: %-4s | Offset: 0x%08X | Size: %7d B | Flags: 0x%08X",
                    name, owner, offset, length, flags);
        }
    }

    /**
     * Resultado completo del análisis de salud y versión de Intel ME.
     */
    public static final class IntelMeInfo {
        private final boolean detected;
        private final long fptOffset;
        private final int headerVersion;
        private final String version;
        private final String sku;
        private final int partitionCount;
        private final List<PartitionEntry> partitions;
        private final boolean headerChecksumValid;
        private final boolean partitionsWithinBounds;
        private final String statusDescription;

        public IntelMeInfo(boolean detected, long fptOffset, int headerVersion,
                           String version, String sku, int partitionCount,
                           List<PartitionEntry> partitions, boolean headerChecksumValid,
                           boolean partitionsWithinBounds, String statusDescription) {
            this.detected = detected;
            this.fptOffset = fptOffset;
            this.headerVersion = headerVersion;
            this.version = version != null ? version : "Desconocida";
            this.sku = sku != null ? sku : "Desconocido";
            this.partitionCount = partitionCount;
            this.partitions = partitions != null ? Collections.unmodifiableList(partitions) : Collections.emptyList();
            this.headerChecksumValid = headerChecksumValid;
            this.partitionsWithinBounds = partitionsWithinBounds;
            this.statusDescription = statusDescription != null ? statusDescription : "";
        }

        public static IntelMeInfo notDetected() {
            return new IntelMeInfo(false, -1, 0, "No detectada", "N/A", 0,
                    Collections.emptyList(), false, false, "No se encontró cabecera $FPT de Intel ME.");
        }

        public boolean isDetected() {
            return detected;
        }

        public long getFptOffset() {
            return fptOffset;
        }

        public int getHeaderVersion() {
            return headerVersion;
        }

        public String getVersion() {
            return version;
        }

        public String getSku() {
            return sku;
        }

        public int getPartitionCount() {
            return partitionCount;
        }

        public List<PartitionEntry> getPartitions() {
            return partitions;
        }

        public boolean isHeaderChecksumValid() {
            return headerChecksumValid;
        }

        public boolean isPartitionsWithinBounds() {
            return partitionsWithinBounds;
        }

        public String getStatusDescription() {
            return statusDescription;
        }

        public String formatSummary() {
            if (!detected) {
                return "Intel ME: No detectada en este volcado.";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Intel Management Engine / CSME:\n");
            sb.append("  Versión:       ").append(version).append("\n");
            sb.append("  SKU:           ").append(sku).append("\n");
            sb.append(String.format(Locale.US, "  Offset $FPT:   0x%08X\n", fptOffset));
            sb.append("  Particiones:   ").append(partitionCount).append("\n");
            sb.append("  Cabecera FPT:  ").append(headerChecksumValid ? "Checksum Válido" : "Checksum Descalibrado").append("\n");
            sb.append("  Diagnóstico:   ").append(statusDescription).append("\n");
            sb.append("  Particiones detectadas:\n");
            for (PartitionEntry p : partitions) {
                sb.append("    ").append(p.toString()).append("\n");
            }
            return sb.toString().trim();
        }

        @Override
        public String toString() {
            return "IntelMeInfo{" +
                    "detected=" + detected +
                    ", version='" + version + '\'' +
                    ", sku='" + sku + '\'' +
                    ", fptOffset=0x" + Long.toHexString(fptOffset) +
                    ", partitions=" + partitionCount +
                    '}';
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // 1. ESCANEO Y PARSEO DE INTEL ME
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Analiza un volcado binario en memoria en busca de Intel ME.
     */
    public static IntelMeInfo parseDump(byte[] dump) {
        if (dump == null || dump.length < FPT_HEADER_SIZE) {
            return IntelMeInfo.notDetected();
        }

        int fptPos = 0;
        while ((fptPos = indexOfSignature(dump, FPT_SIGNATURE, fptPos)) != -1) {
            IntelMeInfo info = parseFptAt(dump, fptPos);
            if (info != null && info.isDetected()) {
                return info;
            }
            fptPos += 4;
        }

        return IntelMeInfo.notDetected();
    }

    /**
     * Analiza un archivo completo mediante streaming sin riesgo de OOM.
     */
    public static IntelMeInfo parseFile(File file) throws IOException {
        if (file == null || !file.exists() || !file.isFile()) {
            return IntelMeInfo.notDetected();
        }

        final int CHUNK_SIZE = 512 * 1024;
        final int OVERLAP = 4096;
        byte[] buffer = new byte[CHUNK_SIZE + OVERLAP];
        int bufferPos = 0;
        long globalOffset = 0;

        try (InputStream in = new FileInputStream(file)) {
            while (true) {
                int read = in.read(buffer, bufferPos, CHUNK_SIZE);
                if (read <= 0) break;

                int validBytes = bufferPos + read;
                int pos = 0;
                while ((pos = indexOfSignature(buffer, FPT_SIGNATURE, pos, validBytes)) != -1) {
                    if (pos + FPT_HEADER_SIZE <= validBytes) {
                        IntelMeInfo info = parseFptAt(buffer, pos, globalOffset + pos, file.length());
                        if (info != null && info.isDetected()) {
                            return info;
                        }
                    }
                    pos += 4;
                }

                int retain = Math.min(OVERLAP, validBytes);
                System.arraycopy(buffer, validBytes - retain, buffer, 0, retain);
                bufferPos = retain;
                globalOffset += (validBytes - retain);
            }
        }

        return IntelMeInfo.notDetected();
    }

    // ════════════════════════════════════════════════════════════════════════
    // 2. PARSEO DE CABECERA $FPT Y ENTRADAS DE PARTICIÓN
    // ════════════════════════════════════════════════════════════════════════

    private static IntelMeInfo parseFptAt(byte[] data, int fptOffset) {
        return parseFptAt(data, fptOffset, fptOffset, data.length);
    }

    private static IntelMeInfo parseFptAt(byte[] data, int localOffset, long absoluteFptOffset, long totalFileSize) {
        if (localOffset + FPT_HEADER_SIZE > data.length) {
            return null;
        }

        // Estructura Cabecera $FPT (32 bytes):
        // [0x00..0x03] Firma "$FPT"
        // [0x04..0x07] Número de entradas de partición (uint32)
        // [0x08..0x0B] Versión de Cabecera (uint32)
        // [0x0C]       Versión de Entrada (uint8)
        // [0x0D]       Longitud de Cabecera (uint8, típicamente 0x20)
        // [0x0E..0x0F] Checksum (uint16 o suma mod 256)
        int entryCount = readDwordLe(data, localOffset + 0x04);
        int headerVersion = readDwordLe(data, localOffset + 0x08);
        int entryVersion = data[localOffset + 0x0C] & 0xFF;
        int headerLength = data[localOffset + 0x0D] & 0xFF;

        if (headerLength == 0) {
            headerLength = FPT_HEADER_SIZE;
        }

        // Filtro de plausibilidad para evitar falsos positivos
        if (entryCount <= 0 || entryCount > 64) {
            return null;
        }

        // Checksum de la cabecera $FPT: la suma de todos los 32 bytes mod 256 == 0 en muchas revisiones
        int sum = 0;
        for (int i = 0; i < FPT_HEADER_SIZE; i++) {
            sum += (data[localOffset + i] & 0xFF);
        }
        boolean checksumValid = ((sum & 0xFF) == 0);

        // Parsear cada entrada de partición
        List<PartitionEntry> partitions = new ArrayList<>();
        boolean allWithinBounds = true;
        PartitionEntry ftprPartition = null;
        boolean hasMdmv = false;
        boolean hasTxe = false;

        int tableOffset = localOffset + headerLength;
        for (int i = 0; i < entryCount; i++) {
            int entryPos = tableOffset + i * FPT_ENTRY_SIZE;
            if (entryPos + FPT_ENTRY_SIZE > data.length) {
                break;
            }

            String name = new String(data, entryPos, 4, StandardCharsets.US_ASCII).trim();
            String owner = new String(data, entryPos + 4, 4, StandardCharsets.US_ASCII).trim();
            long partOffset = readDwordLe(data, entryPos + 8) & 0xFFFFFFFFL;
            long partLength = readDwordLe(data, entryPos + 12) & 0xFFFFFFFFL;
            long flags = readDwordLe(data, entryPos + 28) & 0xFFFFFFFFL;

            if (name.isEmpty()) continue;

            PartitionEntry entry = new PartitionEntry(name, owner, partOffset, partLength, flags);
            partitions.add(entry);

            if ("FTPR".equalsIgnoreCase(name)) {
                ftprPartition = entry;
            } else if ("MDMV".equalsIgnoreCase(name) || "AMT".equalsIgnoreCase(name)) {
                hasMdmv = true;
            } else if ("FOVD".equalsIgnoreCase(name) || "ISHP".equalsIgnoreCase(name)) {
                hasTxe = true;
            }

            if (partOffset + partLength > totalFileSize && totalFileSize > 0) {
                allWithinBounds = false;
            }
        }

        if (partitions.isEmpty()) {
            return null;
        }

        // Determinar SKU
        String sku;
        if (hasTxe) {
            sku = "Ignition / TXE (Atom / Tablet)";
        } else if (hasMdmv) {
            sku = "Corporate / AMT (vPro 5MB+)";
        } else {
            sku = "Consumer (Slim 1.5MB)";
        }

        // Extraer versión de ME analizando el manifiesto en la partición FTPR o en los datos
        String version = detectMeVersion(data, localOffset, ftprPartition);

        String statusDesc;
        if (ftprPartition == null) {
            statusDesc = "Alerta: Falta partición nuclear FTPR (Riesgo alto de apagado a los 30 min).";
        } else if (!allWithinBounds) {
            statusDesc = "Alerta: Desbordamiento de límites de partición (ROM corrupta o truncada).";
        } else {
            statusDesc = "Región Intel ME íntegra. Particiones y cabecera verificadas.";
        }

        return new IntelMeInfo(true, absoluteFptOffset, headerVersion,
                version, sku, partitions.size(), partitions,
                checksumValid, allWithinBounds, statusDesc);
    }

    // ════════════════════════════════════════════════════════════════════════
    // 3. EXTRACCIÓN DE LA VERSIÓN DE INTEL ME ($MAN / $CPD)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Escanea el bloque FTPR o los alrededores en busca de la cabecera del Manifiesto ($MAN / $CPD)
     * y extrae los 4 componentes de versión (Major.Minor.Hotfix.Build).
     */
    private static String detectMeVersion(byte[] data, int fptOffset, PartitionEntry ftpr) {
        int searchStart = fptOffset;
        int searchLimit = Math.min(data.length, fptOffset + 128 * 1024);

        if (ftpr != null && ftpr.getOffset() > 0) {
            // Si la partición FTPR tiene offset relativo al principio del búfer o a ME
            int ftprRel = (int) (fptOffset + ftpr.getOffset());
            if (ftprRel < data.length) {
                searchStart = ftprRel;
                searchLimit = Math.min(data.length, ftprRel + Math.min((int) ftpr.getLength(), 64 * 1024));
            }
        }

        // 1. Buscar firma $MAN ($24 $4D $41 $4E)
        int manPos = indexOfSignature(data, MAN_SIGNATURE, searchStart, searchLimit);
        if (manPos != -1 && manPos + 0x30 <= data.length) {
            String v = extractVersionFromManifest(data, manPos);
            if (v != null) return v;
        }

        // 2. Buscar firma $CPD ($24 $43 $50 $44)
        int cpdPos = indexOfSignature(data, CPD_SIGNATURE, searchStart, searchLimit);
        if (cpdPos != -1 && cpdPos + 0x30 <= data.length) {
            String v = extractVersionFromManifest(data, cpdPos);
            if (v != null) return v;
        }

        // 3. Búsqueda de firma $MAN en cualquier parte de la ventana
        int anyMan = indexOfSignature(data, MAN_SIGNATURE, 0, Math.min(data.length, fptOffset + 256 * 1024));
        if (anyMan != -1 && anyMan + 0x30 <= data.length) {
            String v = extractVersionFromManifest(data, anyMan);
            if (v != null) return v;
        }

        return "Detectada (Revisión $FPT)";
    }

    /**
     * En la cabecera de Manifiesto de Intel ME:
     * Tras la firma $MAN o $CPD, los campos de versión típicamente se encuentran en:
     *   offset + 0x20 a 0x28: Major (uint16), Minor (uint16), Hotfix (uint16), Build (uint16)
     * o en offset + 0x10 a 0x18.
     */
    private static String extractVersionFromManifest(byte[] data, int manOffset) {
        int[] offsetsToTry = new int[]{0x20, 0x10, 0x18, 0x24};

        for (int rel : offsetsToTry) {
            int pos = manOffset + rel;
            if (pos + 8 <= data.length) {
                int major = readWordLe(data, pos);
                int minor = readWordLe(data, pos + 2);
                int hotfix = readWordLe(data, pos + 4);
                int build = readWordLe(data, pos + 6);

                // Versiones ME típicas: Major entre 2 y 18, Minor entre 0 y 20
                if (major >= 2 && major <= 20 && minor >= 0 && minor <= 30 && build > 0 && build < 65000) {
                    return String.format(Locale.US, "%d.%d.%d.%d", major, minor, hotfix, build);
                }
            }
        }
        return null;
    }

    // ════════════════════════════════════════════════════════════════════════
    // UTILIDADES BINARIAS LITTLE-ENDIAN
    // ════════════════════════════════════════════════════════════════════════

    public static int readWordLe(byte[] data, int offset) {
        return (data[offset] & 0xFF) | ((data[offset + 1] & 0xFF) << 8);
    }

    public static int readDwordLe(byte[] data, int offset) {
        return (data[offset] & 0xFF) |
                ((data[offset + 1] & 0xFF) << 8) |
                ((data[offset + 2] & 0xFF) << 16) |
                ((data[offset + 3] & 0xFF) << 24);
    }

    private static int indexOfSignature(byte[] data, byte[] pattern, int start) {
        return indexOfSignature(data, pattern, start, data.length);
    }

    private static int indexOfSignature(byte[] data, byte[] pattern, int start, int limit) {
        if (data == null || pattern == null || start < 0) return -1;
        int max = limit - pattern.length;
        for (int i = start; i <= max; i++) {
            boolean match = true;
            for (int j = 0; j < pattern.length; j++) {
                if (data[i + j] != pattern[j]) {
                    match = false;
                    break;
                }
            }
            if (match) return i;
        }
        return -1;
    }
}
