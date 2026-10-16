package com.diamon.curso.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extractor de Licencia Digital de Producto Windows OEM desde tablas ACPI MSDM.
 *
 * Implementa el Pilar 2 de Flash SPI Tool PRO:
 * - Escaneo de la firma ACPI "MSDM" (0x4D, 0x53, 0x44, 0x4D).
 * - Decodificación de la cabecera estándar ACPI (longitud, OEMID, OEM Table ID, Checksum mod 256).
 * - Extracción precisa de la clave de producto de 25 caracteres (XXXXX-XXXXX-XXXXX-XXXXX-XXXXX).
 * - Soporte para volcados de gran tamaño (16MB a 64MB) mediante escaneo con ventana deslizante sin riesgo de OOM.
 */
public final class MsdmExtractor {

    /** Firma ASCII de la tabla ACPI Microsoft Data Management. */
    public static final byte[] MSDM_SIGNATURE = new byte[]{'M', 'S', 'D', 'M'};

    /** Tamaño mínimo de una tabla MSDM válida (cabecera ACPI 36B + SLS 20B + clave 29B = 85 bytes). */
    public static final int MIN_MSDM_TABLE_SIZE = 85;

    /** Tamaño máximo razonable de una tabla MSDM. */
    public static final int MAX_MSDM_TABLE_SIZE = 4096;

    /** Expresión regular estándar para la clave de 25 caracteres en 5 bloques (formato XXXXX-XXXXX-XXXXX-XXXXX-XXXXX). */
    private static final Pattern PRODUCT_KEY_PATTERN = Pattern.compile(
            "\\b([BCDFGHJKMPQRTVWXY2346789]{5}-[BCDFGHJKMPQRTVWXY2346789]{5}-[BCDFGHJKMPQRTVWXY2346789]{5}-[BCDFGHJKMPQRTVWXY2346789]{5}-[BCDFGHJKMPQRTVWXY2346789]{5})\\b",
            Pattern.CASE_INSENSITIVE
    );

    /** Patrón alfanumérico amplio de respaldo (en caso de claves personalizadas). */
    private static final Pattern GENERIC_KEY_PATTERN = Pattern.compile(
            "\\b([A-Z0-9]{5}-[A-Z0-9]{5}-[A-Z0-9]{5}-[A-Z0-9]{5}-[A-Z0-9]{5})\\b"
    );

    private static volatile boolean sNativeAvailable = false;
    static {
        try {
            System.loadLibrary("curso");
            sNativeAvailable = true;
        } catch (Throwable ignored) {
            sNativeAvailable = false;
        }
    }

    private static native int findSignatureNative(byte[] data, int startOffset, byte[] pattern);

    private MsdmExtractor() {
        // Clase de utilidades estáticas
    }

    /**
     * Resultado estructurado de la extracción de una clave MSDM.
     */
    public static final class MsdmResult {
        private final boolean found;
        private final String productKey;
        private final String oemId;
        private final String oemTableId;
        private final long tableOffset;
        private final int tableLength;
        private final boolean checksumValid;

        public MsdmResult(boolean found, String productKey, String oemId, String oemTableId,
                          long tableOffset, int tableLength, boolean checksumValid) {
            this.found = found;
            this.productKey = productKey != null ? productKey.toUpperCase(Locale.US) : "";
            this.oemId = oemId != null ? oemId.trim() : "";
            this.oemTableId = oemTableId != null ? oemTableId.trim() : "";
            this.tableOffset = tableOffset;
            this.tableLength = tableLength;
            this.checksumValid = checksumValid;
        }

        public static MsdmResult notFound() {
            return new MsdmResult(false, "", "", "", -1, 0, false);
        }

        public boolean isFound() {
            return found;
        }

        public String getProductKey() {
            return productKey;
        }

        public String getOemId() {
            return oemId;
        }

        public String getOemTableId() {
            return oemTableId;
        }

        public long getTableOffset() {
            return tableOffset;
        }

        public int getTableLength() {
            return tableLength;
        }

        public boolean isChecksumValid() {
            return checksumValid;
        }

        public String getFormattedSummary() {
            if (!found) {
                return "No se encontró tabla ACPI MSDM ni clave de producto en el volcado.";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Clave de Producto: ").append(productKey).append("\n");
            sb.append("OEM ID:            ").append(oemId.isEmpty() ? "Desconocido" : oemId).append("\n");
            sb.append("OEM Table ID:      ").append(oemTableId.isEmpty() ? "Desconocido" : oemTableId).append("\n");
            sb.append(String.format(Locale.US, "Offset en ROM:     0x%08X\n", tableOffset));
            sb.append("Tamaño de Tabla:   ").append(tableLength).append(" bytes\n");
            sb.append("Integridad ACPI:   ").append(checksumValid ? "Válida (Checksum OK)" : "Aviso: Checksum descalibrado");
            return sb.toString();
        }

        @Override
        public String toString() {
            return "MsdmResult{" +
                    "found=" + found +
                    ", productKey='" + productKey + '\'' +
                    ", oemId='" + oemId + '\'' +
                    ", oemTableId='" + oemTableId + '\'' +
                    ", offset=0x" + Long.toHexString(tableOffset) +
                    ", validChecksum=" + checksumValid +
                    '}';
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // 1. EXTRACCIÓN DESDE MEMORIA (BYTE ARRAY)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Extrae la primera clave de producto Windows OEM válida en un volcado en memoria.
     *
     * @param dump Volcado binario de BIOS.
     * @return MsdmResult con los datos extraídos o MsdmResult.notFound().
     */
    public static MsdmResult extractKey(byte[] dump) {
        List<MsdmResult> results = extractAllKeys(dump);
        if (!results.isEmpty()) {
            return results.get(0);
        }
        return MsdmResult.notFound();
    }

    /**
     * Extrae todas las claves MSDM encontradas en el volcado en memoria.
     */
    public static List<MsdmResult> extractAllKeys(byte[] dump) {
        List<MsdmResult> list = new ArrayList<>();
        if (dump == null || dump.length < MIN_MSDM_TABLE_SIZE) {
            return list;
        }

        int pos = 0;
        while ((pos = indexOfSignature(dump, MSDM_SIGNATURE, pos)) != -1) {
            MsdmResult res = parseMsdmTable(dump, pos, pos);
            if (res != null && res.isFound()) {
                list.add(res);
            }
            pos += 4;
        }

        // Si no se encontró mediante la estructura ACPI formal, buscar directamente el patrón de clave
        if (list.isEmpty()) {
            MsdmResult fallback = searchKeyRegex(dump, 0);
            if (fallback != null && fallback.isFound()) {
                list.add(fallback);
            }
        }

        return list;
    }

    // ════════════════════════════════════════════════════════════════════════
    // 2. EXTRACCIÓN DESDE ARCHIVO CON VENTANA DESLIZANTE (STREAMING ZERO OOM)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Extrae la clave OEM desde un archivo de volcado SPI (16MB a 64MB) de manera eficiente.
     */
    public static MsdmResult extractFromFile(File file) throws IOException {
        if (file == null || !file.exists() || !file.isFile()) {
            return MsdmResult.notFound();
        }

        final int CHUNK_SIZE = 256 * 1024; // 256 KB
        final int OVERLAP = MAX_MSDM_TABLE_SIZE;
        byte[] buffer = new byte[CHUNK_SIZE + OVERLAP];
        int bufferPos = 0;
        long globalOffset = 0;

        try (InputStream in = new FileInputStream(file)) {
            while (true) {
                int read = in.read(buffer, bufferPos, CHUNK_SIZE);
                if (read <= 0) break;

                int validBytes = bufferPos + read;

                // Buscar MSDM en este búfer
                int pos = 0;
                while ((pos = indexOfSignature(buffer, MSDM_SIGNATURE, pos, validBytes)) != -1) {
                    if (pos + MIN_MSDM_TABLE_SIZE <= validBytes) {
                        MsdmResult res = parseMsdmTable(buffer, pos, globalOffset + pos);
                        if (res != null && res.isFound()) {
                            return res;
                        }
                    }
                    pos += 4;
                }

                // Desplazar el remanente (overlap) al principio para la siguiente iteración
                int retain = Math.min(OVERLAP, validBytes);
                System.arraycopy(buffer, validBytes - retain, buffer, 0, retain);
                bufferPos = retain;
                globalOffset += (validBytes - retain);
            }
        }

        return MsdmResult.notFound();
    }

    // ════════════════════════════════════════════════════════════════════════
    // 3. PARSEO DE CABECERA Y ESTRUCTURA ACPI MSDM
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Parsea la tabla ACPI MSDM en la posición indicada.
     *
     * Estructura ACPI estándar (36 bytes):
     *   [0..3]   Firma "MSDM"
     *   [4..7]   Longitud de la tabla (uint32 Little Endian, típicamente ~85 bytes)
     *   [8]      Revisión (uint8, ej. 1)
     *   [9]      Checksum (uint8, suma mod 256 == 0)
     *   [10..15] OEMID (6 bytes ASCII, ej. "LENOVO")
     *   [16..23] OEM Table ID (8 bytes ASCII)
     *   [24..27] OEM Revision (uint32)
     *   [28..31] Creator ID (4 bytes)
     *   [29..35] Creator Revision (uint32)
     *
     * Estructura SLS de Microsoft (después de offset 36):
     *   [36..39] SLS Data Version (uint32)
     *   [40..43] SLS Data Reserved (uint32)
     *   [44..47] SLS Data Data Type (uint32, 1 = Product Key)
     *   [48..51] SLS Data Reserved (uint32)
     *   [52..55] SLS Data Data Length (uint32, típicamente 29)
     *   [56..84] Clave de 29 bytes (XXXXX-XXXXX-XXXXX-XXXXX-XXXXX)
     */
    private static MsdmResult parseMsdmTable(byte[] data, int localOffset, long absoluteOffset) {
        if (localOffset + 36 > data.length) {
            return null;
        }

        // Leer longitud de la tabla ACPI
        int tableLength = (data[localOffset + 4] & 0xFF) |
                ((data[localOffset + 5] & 0xFF) << 8) |
                ((data[localOffset + 6] & 0xFF) << 16) |
                ((data[localOffset + 7] & 0xFF) << 24);

        if (tableLength < MIN_MSDM_TABLE_SIZE || tableLength > MAX_MSDM_TABLE_SIZE) {
            // Longitud fuera de rango razonable para MSDM
            return null;
        }

        if (localOffset + tableLength > data.length) {
            // No hay suficientes bytes en el búfer actual
            return null;
        }

        // Comprobar checksum ACPI (la suma de todos los bytes de la tabla mod 256 debe ser 0)
        int sum = 0;
        for (int i = 0; i < tableLength; i++) {
            sum += (data[localOffset + i] & 0xFF);
        }
        boolean checksumValid = (sum & 0xFF) == 0;

        // Extraer OEM ID (6 bytes) y OEM Table ID (8 bytes)
        String oemId = new String(data, localOffset + 10, 6, StandardCharsets.US_ASCII).trim();
        String oemTableId = new String(data, localOffset + 16, 8, StandardCharsets.US_ASCII).trim();

        // 1. Intentar extracción directa en el offset estándar SLS (56)
        if (localOffset + 56 + 29 <= data.length) {
            String candidate = new String(data, localOffset + 56, 29, StandardCharsets.US_ASCII);
            if (isValidProductKey(candidate)) {
                return new MsdmResult(true, candidate, oemId, oemTableId, absoluteOffset, tableLength, checksumValid);
            }
        }

        // 2. Búsqueda con expresión regular en todo el cuerpo de la tabla
        String tableText = new String(data, localOffset, tableLength, StandardCharsets.US_ASCII);
        Matcher matcher = PRODUCT_KEY_PATTERN.matcher(tableText);
        if (matcher.find()) {
            return new MsdmResult(true, matcher.group(1), oemId, oemTableId, absoluteOffset, tableLength, checksumValid);
        }

        Matcher genericMatcher = GENERIC_KEY_PATTERN.matcher(tableText);
        if (genericMatcher.find()) {
            return new MsdmResult(true, genericMatcher.group(1), oemId, oemTableId, absoluteOffset, tableLength, checksumValid);
        }

        return null;
    }

    /**
     * Búsqueda heurística directa por expresión regular cuando no hay cabecera ACPI estándar intacta.
     */
    private static MsdmResult searchKeyRegex(byte[] data, long baseOffset) {
        String text = new String(data, StandardCharsets.US_ASCII);
        Matcher m = PRODUCT_KEY_PATTERN.matcher(text);
        if (m.find()) {
            int start = m.start();
            return new MsdmResult(true, m.group(1), "GENERIC", "OEM_DUMP", baseOffset + start, 29, false);
        }
        return null;
    }

    // ════════════════════════════════════════════════════════════════════════
    // 4. VALIDACIÓN DE CLAVES WINDOWS OEM
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Comprueba si una cadena coincide con la especificación de clave digital OEM de Windows de 25 caracteres.
     */
    public static boolean isValidProductKey(String key) {
        if (key == null || key.length() != 29) {
            return false;
        }
        return PRODUCT_KEY_PATTERN.matcher(key).matches() || GENERIC_KEY_PATTERN.matcher(key).matches();
    }

    // ════════════════════════════════════════════════════════════════════════
    // UTILIDADES DE BÚSQUEDA BINARIA
    // ════════════════════════════════════════════════════════════════════════

    private static int indexOfSignature(byte[] data, byte[] pattern, int start) {
        return indexOfSignature(data, pattern, start, data.length);
    }

    private static int indexOfSignature(byte[] data, byte[] pattern, int start, int limit) {
        if (data == null || pattern == null || start < 0) return -1;
        if (limit == data.length && sNativeAvailable) {
            try {
                int nativeIdx = findSignatureNative(data, start, pattern);
                if (nativeIdx >= -1) return nativeIdx;
            } catch (Throwable ignored) {
            }
        }
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
