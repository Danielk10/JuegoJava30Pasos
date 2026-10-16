package com.diamon.curso.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Localizador, extractor y editor quirúrgico de estructuras DMI / SMBIOS.
 *
 * Implementa el Pilar 2 de Flash SPI Tool PRO:
 * - Localización de anclas SMBIOS ("_SM_", "_DMI_", "_SM3_").
 * - Extracción de metadatos de Type 1 (System Information): Fabricante, Modelo, Número de Serie y UUID.
 * - Soporte para orden de bytes SMBIOS 2.6+ (Mixed-Endian) y RFC 4122 (Big-Endian).
 * - Inyección y parcheo in-place de UUID de 16 bytes y Número de Serie sin desalinear la estructura.
 */
public final class DmiPatcher {

    public static final byte[] ANCHOR_SM  = new byte[]{'_', 'S', 'M', '_'};
    public static final byte[] ANCHOR_DMI = new byte[]{'_', 'D', 'M', 'I', '_'};
    public static final byte[] ANCHOR_SM3 = new byte[]{'_', 'S', 'M', '3', '_'};

    private static final Pattern UUID_REGEX = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    );

    private DmiPatcher() {
        // Clase de utilidades estáticas
    }

    /**
     * Contenedor con la información SMBIOS y offsets exactos para parchear.
     */
    public static final class DmiInfo {
        private final boolean found;
        private final long entryPointOffset;
        private final long type1Offset;
        private final long uuidOffset;
        private final long serialOffset;
        private final String manufacturer;
        private final String productName;
        private final String serialNumber;
        private final UUID uuid;
        private final String uuidFormattedSmbios;
        private final String uuidFormattedRfc4122;

        public DmiInfo(boolean found, long entryPointOffset, long type1Offset,
                       long uuidOffset, long serialOffset,
                       String manufacturer, String productName, String serialNumber,
                       UUID uuid, String uuidSmbios, String uuidRfc4122) {
            this.found = found;
            this.entryPointOffset = entryPointOffset;
            this.type1Offset = type1Offset;
            this.uuidOffset = uuidOffset;
            this.serialOffset = serialOffset;
            this.manufacturer = manufacturer != null ? manufacturer.trim() : "";
            this.productName = productName != null ? productName.trim() : "";
            this.serialNumber = serialNumber != null ? serialNumber.trim() : "";
            this.uuid = uuid;
            this.uuidFormattedSmbios = uuidSmbios != null ? uuidSmbios : "";
            this.uuidFormattedRfc4122 = uuidRfc4122 != null ? uuidRfc4122 : "";
        }

        public static DmiInfo notFound() {
            return new DmiInfo(false, -1, -1, -1, -1, "", "", "", null, "", "");
        }

        public boolean isFound() {
            return found;
        }

        public long getEntryPointOffset() {
            return entryPointOffset;
        }

        public long getType1Offset() {
            return type1Offset;
        }

        public long getUuidOffset() {
            return uuidOffset;
        }

        public long getSerialOffset() {
            return serialOffset;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public String getProductName() {
            return productName;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public UUID getUuid() {
            return uuid;
        }

        public String getUuidFormattedSmbios() {
            return uuidFormattedSmbios;
        }

        public String getUuidFormattedRfc4122() {
            return uuidFormattedRfc4122;
        }

        public String getSummary() {
            if (!found) {
                return "No se encontraron estructuras SMBIOS / DMI en el volcado.";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Fabricante:      ").append(manufacturer.isEmpty() ? "No especificado" : manufacturer).append("\n");
            sb.append("Modelo / Placa:  ").append(productName.isEmpty() ? "No especificado" : productName).append("\n");
            sb.append("Número de Serie: ").append(serialNumber.isEmpty() ? "No disponible" : serialNumber).append("\n");
            sb.append("UUID (SMBIOS):   ").append(uuidFormattedSmbios.isEmpty() ? "No disponible" : uuidFormattedSmbios).append("\n");
            sb.append("UUID (RFC 4122): ").append(uuidFormattedRfc4122.isEmpty() ? "No disponible" : uuidFormattedRfc4122).append("\n");
            sb.append(String.format(Locale.US, "Offset Estructura Type 1: 0x%08X\n", type1Offset));
            if (uuidOffset >= 0) {
                sb.append(String.format(Locale.US, "Offset UUID en ROM:       0x%08X\n", uuidOffset));
            }
            if (serialOffset >= 0) {
                sb.append(String.format(Locale.US, "Offset Serial en ROM:     0x%08X", serialOffset));
            }
            return sb.toString();
        }

        @Override
        public String toString() {
            return "DmiInfo{" +
                    "found=" + found +
                    ", manufacturer='" + manufacturer + '\'' +
                    ", productName='" + productName + '\'' +
                    ", serialNumber='" + serialNumber + '\'' +
                    ", uuidSmbios='" + uuidFormattedSmbios + '\'' +
                    ", type1Offset=0x" + Long.toHexString(type1Offset) +
                    '}';
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // 1. ESCANEO Y EXTRACCIÓN SMBIOS / DMI
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Escanea un volcado binario en memoria y extrae la información DMI principal.
     */
    public static DmiInfo extractDmiInfo(byte[] dump) {
        if (dump == null || dump.length < 32) {
            return DmiInfo.notFound();
        }

        // 1. Buscar anclas estándar _SM_
        int smPos = 0;
        while ((smPos = indexOfPattern(dump, ANCHOR_SM, smPos)) != -1) {
            // Verificar si tiene el ancla intermedia _DMI_ en smPos + 0x10
            if (smPos + 0x1F <= dump.length) {
                boolean hasDmi = matchPattern(dump, smPos + 0x10, ANCHOR_DMI);
                if (hasDmi) {
                    DmiInfo info = parseSmbiosEntryPoint(dump, smPos);
                    if (info.isFound()) {
                        return info;
                    }
                }
            }
            smPos += 4;
        }

        // 2. Buscar anclas intermedias directas _DMI_
        int dmiPos = 0;
        while ((dmiPos = indexOfPattern(dump, ANCHOR_DMI, dmiPos)) != -1) {
            DmiInfo info = scanStructuresNearby(dump, dmiPos);
            if (info != null && info.isFound()) {
                return info;
            }
            dmiPos += 5;
        }

        // 3. Búsqueda directa de estructuras Type 1 (0x01, length >= 0x19)
        return scanForType1Direct(dump);
    }

    /**
     * Escanea un archivo de volcado SPI completo (streaming sin OOM).
     */
    public static DmiInfo extractFromFile(File file) throws IOException {
        if (file == null || !file.exists() || !file.isFile()) {
            return DmiInfo.notFound();
        }
        // Para localizar DMI en archivos grandes, leemos en bloques con solapamiento
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
                int smPos = 0;
                while ((smPos = indexOfPattern(buffer, ANCHOR_SM, smPos, validBytes)) != -1) {
                    if (smPos + 0x1F <= validBytes) {
                        if (matchPattern(buffer, smPos + 0x10, ANCHOR_DMI)) {
                            DmiInfo local = parseSmbiosEntryPoint(buffer, smPos);
                            if (local.isFound()) {
                                long absoluteType1 = globalOffset + local.getType1Offset();
                                long absoluteUuid = local.getUuidOffset() >= 0 ? globalOffset + local.getUuidOffset() : -1;
                                long absoluteSerial = local.getSerialOffset() >= 0 ? globalOffset + local.getSerialOffset() : -1;
                                return new DmiInfo(true, globalOffset + smPos, absoluteType1,
                                        absoluteUuid, absoluteSerial,
                                        local.getManufacturer(), local.getProductName(),
                                        local.getSerialNumber(), local.getUuid(),
                                        local.getUuidFormattedSmbios(), local.getUuidFormattedRfc4122());
                            }
                        }
                    }
                    smPos += 4;
                }

                int retain = Math.min(OVERLAP, validBytes);
                System.arraycopy(buffer, validBytes - retain, buffer, 0, retain);
                bufferPos = retain;
                globalOffset += (validBytes - retain);
            }
        }

        return DmiInfo.notFound();
    }

    private static DmiInfo parseSmbiosEntryPoint(byte[] data, int entryPointPos) {
        // En el Entry Point SMBIOS de 32 bits:
        // offset 0x16: Longitud de la tabla de estructuras (uint16)
        // offset 0x18: Dirección base de la tabla de estructuras (uint32)
        // offset 0x1C: Número de estructuras (uint16)
        int tableLength = (data[entryPointPos + 0x16] & 0xFF) | ((data[entryPointPos + 0x17] & 0xFF) << 8);
        int tableAddr = readIntLe(data, entryPointPos + 0x18);
        int structCount = (data[entryPointPos + 0x1C] & 0xFF) | ((data[entryPointPos + 0x1D] & 0xFF) << 8);

        // Si tableAddr apunta a un offset viable dentro del búfer
        if (tableAddr > 0 && tableAddr + 32 <= data.length) {
            DmiInfo info = parseStructureTable(data, tableAddr, tableLength, structCount, entryPointPos);
            if (info != null && info.isFound()) {
                return info;
            }
        }

        // Si la dirección física de RAM no mapea directamente, buscar en las inmediaciones relativas
        return scanStructuresNearby(data, entryPointPos);
    }

    private static DmiInfo scanStructuresNearby(byte[] data, int anchorPos) {
        // Buscar estructura Type 1 dentro de un radio de +/- 64KB alrededor del ancla
        int searchStart = Math.max(0, anchorPos - 65536);
        int searchEnd = Math.min(data.length - 32, anchorPos + 65536);

        for (int i = searchStart; i < searchEnd; i++) {
            if ((data[i] & 0xFF) == 1) { // Type 1
                int len = data[i + 1] & 0xFF;
                if (len >= 0x19 && len <= 0x30 && i + len + 2 <= data.length) {
                    DmiInfo info = parseType1Structure(data, i, anchorPos);
                    if (info != null && info.isFound()) {
                        return info;
                    }
                }
            }
        }
        return DmiInfo.notFound();
    }

    private static DmiInfo scanForType1Direct(byte[] data) {
        for (int i = 0; i < data.length - 32; i++) {
            if ((data[i] & 0xFF) == 1) { // Type 1: System Information
                int len = data[i + 1] & 0xFF;
                if (len >= 0x19 && len <= 0x30 && i + len + 2 <= data.length) {
                    // Verificar si el bloque termina con doble byte nulo de tabla de strings
                    DmiInfo info = parseType1Structure(data, i, -1);
                    if (info != null && info.isFound() && (!info.getSerialNumber().isEmpty() || info.getUuid() != null)) {
                        return info;
                    }
                }
            }
        }
        return DmiInfo.notFound();
    }

    private static DmiInfo parseStructureTable(byte[] data, int tableOffset, int tableLength, int structCount, int entryPointPos) {
        int pos = tableOffset;
        int max = Math.min(data.length, tableOffset + (tableLength > 0 ? tableLength : 65536));
        int count = 0;

        while (pos + 4 < max) {
            int type = data[pos] & 0xFF;
            int length = data[pos + 1] & 0xFF;
            if (length < 4 || pos + length > max) break;

            if (type == 1) { // Type 1: System Information
                return parseType1Structure(data, pos, entryPointPos);
            }

            if (type == 127) { // End of Table marker
                break;
            }

            // Saltar formatted area + unformatted strings
            pos = skipStructureAndStrings(data, pos + length, max);
            count++;
            if (structCount > 0 && count >= structCount) break;
        }

        return null;
    }

    /**
     * Parsea una estructura Type 1 (System Information) en SMBIOS.
     *
     * Offsets en Formatted Area:
     *   [0x00] Type (0x01)
     *   [0x01] Length (>= 0x19)
     *   [0x02..0x03] Handle
     *   [0x04] Fabricante (Índice de string, 1-based)
     *   [0x05] Nombre de Producto (Índice de string, 1-based)
     *   [0x06] Versión (Índice de string, 1-based)
     *   [0x07] Número de Serie (Índice de string, 1-based)
     *   [0x08..0x17] UUID (16 bytes)
     */
    private static DmiInfo parseType1Structure(byte[] data, int structPos, int entryPointPos) {
        int length = data[structPos + 1] & 0xFF;
        if (length < 0x19 || structPos + length > data.length) {
            return null;
        }

        int mfgIdx = data[structPos + 0x04] & 0xFF;
        int prodIdx = data[structPos + 0x05] & 0xFF;
        int serialIdx = data[structPos + 0x07] & 0xFF;

        // Extraer los 16 bytes de UUID
        byte[] uuidBytes = new byte[16];
        System.arraycopy(data, structPos + 0x08, uuidBytes, 0, 16);
        long uuidOffset = structPos + 0x08;

        // Comprobar si el UUID está inicializado (no todo 0x00 ni todo 0xFF)
        boolean hasUuid = false;
        for (byte b : uuidBytes) {
            if (b != 0 && (b & 0xFF) != 0xFF) {
                hasUuid = true;
                break;
            }
        }

        UUID uuid = hasUuid ? parseUuidBytes(uuidBytes, true) : null;
        String uuidSmbios = hasUuid ? formatUuid(uuidBytes, true) : "";
        String uuidRfc4122 = hasUuid ? formatUuid(uuidBytes, false) : "";

        // Parsear la tabla de strings que sigue inmediatamente a la formatted area
        int stringsPos = structPos + length;
        List<String> strings = new ArrayList<>();
        List<Integer> stringOffsets = new ArrayList<>();
        int curPos = stringsPos;

        while (curPos < data.length) {
            if (data[curPos] == 0) {
                // Doble null marca el final de la estructura
                break;
            }
            int start = curPos;
            while (curPos < data.length && data[curPos] != 0) {
                curPos++;
            }
            String s = new String(data, start, curPos - start, StandardCharsets.US_ASCII);
            strings.add(s);
            stringOffsets.add(start);
            curPos++; // Saltar el byte null
        }

        String manufacturer = getStringByIndex(strings, mfgIdx);
        String productName = getStringByIndex(strings, prodIdx);
        String serialNumber = getStringByIndex(strings, serialIdx);
        long serialOffset = (serialIdx > 0 && serialIdx <= stringOffsets.size()) ?
                stringOffsets.get(serialIdx - 1) : -1;

        if (!hasUuid && serialNumber.isEmpty() && manufacturer.isEmpty()) {
            return null;
        }

        return new DmiInfo(true, entryPointPos, structPos, uuidOffset, serialOffset,
                manufacturer, productName, serialNumber, uuid, uuidSmbios, uuidRfc4122);
    }

    private static String getStringByIndex(List<String> strings, int index) {
        if (index > 0 && index <= strings.size()) {
            return strings.get(index - 1);
        }
        return "";
    }

    private static int skipStructureAndStrings(byte[] data, int pos, int max) {
        while (pos < max - 1) {
            if (data[pos] == 0 && data[pos + 1] == 0) {
                return pos + 2;
            }
            pos++;
        }
        return max;
    }

    // ════════════════════════════════════════════════════════════════════════
    // 2. CONVERSIÓN Y FORMATEO DE UUID (SMBIOS vs RFC 4122)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Convierte 16 bytes a objeto java.util.UUID.
     *
     * @param bytes              Array de 16 bytes.
     * @param smbiosLittleEndian Si es true, decodifica los primeros 3 campos en Little Endian (SMBIOS 2.6+).
     */
    public static UUID parseUuidBytes(byte[] bytes, boolean smbiosLittleEndian) {
        if (bytes == null || bytes.length != 16) {
            throw new IllegalArgumentException("El UUID requiere exactamente 16 bytes");
        }
        byte[] copy = bytes.clone();
        if (smbiosLittleEndian) {
            // Invertir time_low (4 bytes)
            swap(copy, 0, 3);
            swap(copy, 1, 2);
            // Invertir time_mid (2 bytes)
            swap(copy, 4, 5);
            // Invertir time_hi_and_version (2 bytes)
            swap(copy, 6, 7);
        }
        ByteBuffer bb = ByteBuffer.wrap(copy);
        long mostSig = bb.getLong();
        long leastSig = bb.getLong();
        return new UUID(mostSig, leastSig);
    }

    /**
     * Convierte un UUID a un array de 16 bytes listo para grabarse en memoria.
     *
     * @param uuid               Objeto UUID.
     * @param smbiosLittleEndian Si es true, codifica los primeros 3 campos en Little Endian para SMBIOS 2.6+.
     */
    public static byte[] uuidToBytes(UUID uuid, boolean smbiosLittleEndian) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID no puede ser nulo");
        }
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        byte[] bytes = bb.array();

        if (smbiosLittleEndian) {
            swap(bytes, 0, 3);
            swap(bytes, 1, 2);
            swap(bytes, 4, 5);
            swap(bytes, 6, 7);
        }
        return bytes;
    }

    /**
     * Formatea 16 bytes crudos en cadena canónica "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX".
     */
    public static String formatUuid(byte[] bytes, boolean smbiosLittleEndian) {
        UUID u = parseUuidBytes(bytes, smbiosLittleEndian);
        return u.toString().toUpperCase(Locale.US);
    }

    public static boolean isValidUuidString(String str) {
        if (str == null) return false;
        return UUID_REGEX.matcher(str.trim()).matches();
    }

    private static void swap(byte[] arr, int i, int j) {
        byte t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    // ════════════════════════════════════════════════════════════════════════
    // 3. PARCHEO QUIRÚRGICO DE UUID Y SERIAL NUMBER
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Parchea el UUID en el búfer de volcado en el offset especificado.
     *
     * @param dump               Volcado de firmware.
     * @param uuidOffset         Offset donde comienzan los 16 bytes del UUID.
     * @param newUuid            Nuevo UUID a inyectar.
     * @param smbiosLittleEndian true para codificación estándar SMBIOS 2.6+, false para RFC 4122.
     */
    public static void patchUuid(byte[] dump, int uuidOffset, UUID newUuid, boolean smbiosLittleEndian) {
        if (dump == null || uuidOffset < 0 || uuidOffset + 16 > dump.length) {
            throw new IllegalArgumentException("Offset o búfer inválido para parchear UUID");
        }
        byte[] bytes = uuidToBytes(newUuid, smbiosLittleEndian);
        System.arraycopy(bytes, 0, dump, uuidOffset, 16);
    }

    /**
     * Parchea el UUID directamente en un archivo binario en disco.
     */
    public static void patchUuidInFile(File binFile, long uuidOffset, UUID newUuid, boolean smbiosLittleEndian) throws IOException {
        if (binFile == null || !binFile.exists() || !binFile.isFile()) {
            throw new IOException("Archivo no existe: " + binFile);
        }
        byte[] bytes = uuidToBytes(newUuid, smbiosLittleEndian);
        try (RandomAccessFile raf = new RandomAccessFile(binFile, "rw")) {
            if (uuidOffset + 16 > raf.length()) {
                throw new IOException("Offset de UUID fuera de los límites del archivo");
            }
            raf.seek(uuidOffset);
            raf.write(bytes);
        }
    }

    /**
     * Parchea el Número de Serie in-place si el nuevo cabe en el espacio asignado.
     * Si el nuevo serial es más corto, se rellena con byte nulo (0x00) o espacios para preservar la estructura.
     */
    public static boolean patchSerialNumber(byte[] dump, int serialOffset, String oldSerial, String newSerial) {
        if (dump == null || serialOffset < 0 || oldSerial == null || newSerial == null) {
            return false;
        }
        int maxCapacity = oldSerial.length();
        if (newSerial.length() > maxCapacity) {
            // El nuevo serial excede la capacidad original in-place
            return false;
        }

        byte[] newBytes = newSerial.getBytes(StandardCharsets.US_ASCII);
        System.arraycopy(newBytes, 0, dump, serialOffset, newBytes.length);

        // Si es más corto, rellenar el remanente con byte nulo
        for (int i = newBytes.length; i < maxCapacity; i++) {
            dump[serialOffset + i] = 0;
        }
        return true;
    }

    /**
     * Parchea el número de serie en un archivo en disco.
     */
    public static boolean patchSerialNumberInFile(File binFile, long serialOffset, String oldSerial, String newSerial) throws IOException {
        if (binFile == null || !binFile.exists() || !binFile.isFile()) {
            return false;
        }
        if (newSerial.length() > oldSerial.length()) {
            return false;
        }
        try (RandomAccessFile raf = new RandomAccessFile(binFile, "rw")) {
            if (serialOffset + oldSerial.length() > raf.length()) {
                return false;
            }
            byte[] newBytes = newSerial.getBytes(StandardCharsets.US_ASCII);
            raf.seek(serialOffset);
            raf.write(newBytes);
            for (int i = newBytes.length; i < oldSerial.length(); i++) {
                raf.write(0);
            }
        }
        return true;
    }

    // ════════════════════════════════════════════════════════════════════════
    // UTILIDADES DE BÚSQUEDA BINARIA
    // ════════════════════════════════════════════════════════════════════════

    private static int indexOfPattern(byte[] data, byte[] pattern, int start) {
        return indexOfPattern(data, pattern, start, data.length);
    }

    private static int indexOfPattern(byte[] data, byte[] pattern, int start, int limit) {
        if (data == null || pattern == null || start < 0) return -1;
        int max = limit - pattern.length;
        for (int i = start; i <= max; i++) {
            if (matchPattern(data, i, pattern)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean matchPattern(byte[] data, int offset, byte[] pattern) {
        if (offset + pattern.length > data.length) return false;
        for (int j = 0; j < pattern.length; j++) {
            if (data[offset + j] != pattern[j]) return false;
        }
        return true;
    }

    private static int readIntLe(byte[] data, int offset) {
        return (data[offset] & 0xFF) |
                ((data[offset + 1] & 0xFF) << 8) |
                ((data[offset + 2] & 0xFF) << 16) |
                ((data[offset + 3] & 0xFF) << 24);
    }
}
