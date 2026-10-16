package com.diamon.curso.binary;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Locale;

/**
 * Parser, inyector de MAC y recalculador de checksum NVM para regiones Intel GbE (Gigabit Ethernet).
 *
 * Implementa el Pilar 2 de Flash SPI Tool PRO:
 * - Detección automática de la región GbE en volcados SPI/BIOS (offsets 0x1000, 0x2000, descriptor o escaneo).
 * - Extracción y formateo de la dirección MAC actual (6 bytes).
 * - Validación e inyección de nueva dirección MAC con soporte para copia primaria y copia de respaldo (backup).
 * - Algoritmo de recálculo exacto del Checksum NVM de 16 bits para que la suma total sea 0xBABA,
 *   evitando el error "Código 10: El dispositivo no puede iniciar" en Windows y el error -5 en Linux e1000e.
 */
public final class GbeMacPatcher {

    /** Magic target checksum NVM de Intel GbE (suma total de palabras = 0xBABA). */
    public static final int INTEL_GBE_TARGET_CHECKSUM = 0xBABA;

    /** Tamaño del bloque NVM de configuración (64 palabras de 16 bits = 128 bytes). */
    public static final int GBE_NVM_SIZE = 128;

    /** Número de palabras de 16 bits en la estructura NVM de Intel GbE. */
    public static final int GBE_NVM_WORD_COUNT = 64;

    /** Índice estándar de la palabra de checksum en Intel PCIe GbE (Word 0x3F = bytes 0x7E-0x7F). */
    public static final int DEFAULT_CHECKSUM_WORD_INDEX = 0x3F;

    /** Offsets típicos de la región GbE en imágenes SPI completas de Intel. */
    public static final long[] STANDARD_GBE_OFFSETS = new long[]{0x1000L, 0x2000L, 0x3000L, 0x0000L};

    /** Firma del Descriptor Flash de Intel en offset 0x10. */
    public static final int INTEL_FD_SIGNATURE = 0x0FF0A55A;

    private static volatile boolean sNativeAvailable = false;
    static {
        try {
            System.loadLibrary("curso");
            sNativeAvailable = true;
        } catch (Throwable ignored) {
            sNativeAvailable = false;
        }
    }

    private static native int calculateNvmChecksumNative(byte[] data, int offset, int checksumWordIndex);

    private GbeMacPatcher() {
        // Clase de utilidades estáticas
    }

    /**
     * Información descriptiva de una región Intel GbE detectada.
     */
    public static final class GbeRegionInfo {
        private final long offset;
        private final String macAddress;
        private final byte[] rawMac;
        private final int checksumWordIndex;
        private final int currentChecksum;
        private final int calculatedChecksum;
        private final boolean checksumValid;
        private final boolean hasBackupRegion;
        private final long backupOffset;

        public GbeRegionInfo(long offset, String macAddress, byte[] rawMac,
                             int checksumWordIndex, int currentChecksum, int calculatedChecksum,
                             boolean checksumValid, boolean hasBackupRegion, long backupOffset) {
            this.offset = offset;
            this.macAddress = macAddress;
            this.rawMac = rawMac != null ? rawMac.clone() : new byte[0];
            this.checksumWordIndex = checksumWordIndex;
            this.currentChecksum = currentChecksum;
            this.calculatedChecksum = calculatedChecksum;
            this.checksumValid = checksumValid;
            this.hasBackupRegion = hasBackupRegion;
            this.backupOffset = backupOffset;
        }

        public long getOffset() {
            return offset;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public byte[] getRawMac() {
            return rawMac.clone();
        }

        public int getChecksumWordIndex() {
            return checksumWordIndex;
        }

        public int getCurrentChecksum() {
            return currentChecksum;
        }

        public int getCalculatedChecksum() {
            return calculatedChecksum;
        }

        public boolean isChecksumValid() {
            return checksumValid;
        }

        public boolean hasBackupRegion() {
            return hasBackupRegion;
        }

        public long getBackupOffset() {
            return backupOffset;
        }

        @Override
        public String toString() {
            return String.format(Locale.US,
                    "GbeRegionInfo{offset=0x%X, MAC='%s', valid=%s, chkWord=0x%02X, chk=0x%04X, backup=0x%X}",
                    offset, macAddress, checksumValid, checksumWordIndex, currentChecksum, backupOffset);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // 1. DETECCIÓN DE LA REGIÓN INTEL GbE
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Detecta la región Intel GbE en un volcado SPI completo.
     * Busca primero en los offsets estándar (0x1000, 0x2000), luego en el Descriptor Flash
     * y finalmente realiza un escaneo de firma y checksum 0xBABA.
     *
     * @param dump Volcado completo de la BIOS o firmware SPI.
     * @return Objeto GbeRegionInfo si se encuentra, o null si no se detecta.
     */
    public static GbeRegionInfo detectGbeRegion(byte[] dump) {
        if (dump == null || dump.length < GBE_NVM_SIZE) {
            return null;
        }

        // 1. Probar offsets estándar (0x1000, 0x2000, etc.)
        for (long off : STANDARD_GBE_OFFSETS) {
            if (off + GBE_NVM_SIZE <= dump.length) {
                GbeRegionInfo info = inspectGbeBlock(dump, (int) off);
                if (info != null && info.isChecksumValid()) {
                    return info;
                }
            }
        }

        // 2. Intentar leer la tabla de regiones del Intel Flash Descriptor (si está presente en 0x00)
        long fdGbeOffset = getGbeOffsetFromFlashDescriptor(dump);
        if (fdGbeOffset >= 0 && fdGbeOffset + GBE_NVM_SIZE <= dump.length) {
            GbeRegionInfo info = inspectGbeBlock(dump, (int) fdGbeOffset);
            if (info != null) {
                return info;
            }
        }

        // 3. Probar de nuevo offsets estándar aunque el checksum sea inválido (dump descalibrado)
        for (long off : STANDARD_GBE_OFFSETS) {
            if (off + GBE_NVM_SIZE <= dump.length) {
                GbeRegionInfo info = inspectGbeBlock(dump, (int) off);
                if (info != null && isPlausibleMac(info.getRawMac())) {
                    return info;
                }
            }
        }

        // 4. Escaneo exhaustivo por bloques de 4KB (0x1000)
        for (int off = 0; off + GBE_NVM_SIZE <= dump.length; off += 0x1000) {
            GbeRegionInfo info = inspectGbeBlock(dump, off);
            if (info != null && info.isChecksumValid()) {
                return info;
            }
        }

        return null;
    }

    /**
     * Inspecciona un bloque de 128 bytes en el offset especificado para determinar si corresponde a una NVM GbE.
     */
    public static GbeRegionInfo inspectGbeBlock(byte[] data, int offset) {
        if (data == null || offset < 0 || offset + GBE_NVM_SIZE > data.length) {
            return null;
        }

        byte[] rawMac = new byte[6];
        System.arraycopy(data, offset, rawMac, 0, 6);
        String macStr = formatMac(rawMac);

        // Detectar cuál palabra de checksum está en uso (0x3F estándar, o 0x1F, 0x06, 0x03)
        int[] candidateWords = new int[]{0x3F, 0x1F, 0x06, 0x03};
        int selectedWord = DEFAULT_CHECKSUM_WORD_INDEX;
        boolean isValid = false;
        int currentChecksum = 0;
        int calculatedChecksum = 0;

        for (int wordIdx : candidateWords) {
            int current = readWordLe(data, offset + wordIdx * 2);
            int calculated = calculateChecksumWord(data, offset, wordIdx);
            if (current == calculated) {
                selectedWord = wordIdx;
                isValid = true;
                currentChecksum = current;
                calculatedChecksum = calculated;
                break;
            }
        }

        if (!isValid) {
            // Si ninguno coincide, asumimos el estándar de la industria (0x3F)
            selectedWord = DEFAULT_CHECKSUM_WORD_INDEX;
            currentChecksum = readWordLe(data, offset + selectedWord * 2);
            calculatedChecksum = calculateChecksumWord(data, offset, selectedWord);
        }

        // Comprobar si existe una copia de respaldo a 0x1000 bytes (4KB) de distancia
        boolean hasBackup = false;
        long backupOffset = -1;
        if (offset + 0x1000 + GBE_NVM_SIZE <= data.length) {
            byte[] backupMac = new byte[6];
            System.arraycopy(data, offset + 0x1000, backupMac, 0, 6);
            if (Arrays.equals(rawMac, backupMac)) {
                hasBackup = true;
                backupOffset = offset + 0x1000;
            }
        }

        return new GbeRegionInfo(offset, macStr, rawMac, selectedWord,
                currentChecksum, calculatedChecksum, isValid, hasBackup, backupOffset);
    }

    // ════════════════════════════════════════════════════════════════════════
    // 2. PARSEO Y FORMATEO DE DIRECCIONES MAC
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Parsea un string de MAC ("AA:BB:CC:DD:EE:FF", "AA-BB-CC-DD-EE-FF" o "AABBCCDDEEFF") en un array de 6 bytes.
     */
    public static byte[] parseMac(String macStr) {
        if (macStr == null) {
            throw new IllegalArgumentException("Dirección MAC nula");
        }
        String clean = macStr.replaceAll("[:\\-\\s]", "").trim();
        if (clean.length() != 12) {
            throw new IllegalArgumentException("Longitud de MAC inválida: " + macStr);
        }
        byte[] mac = new byte[6];
        for (int i = 0; i < 6; i++) {
            mac[i] = (byte) Integer.parseInt(clean.substring(i * 2, i * 2 + 2), 16);
        }
        return mac;
    }

    /**
     * Formatea 6 bytes en formato estándar "AA:BB:CC:DD:EE:FF".
     */
    public static String formatMac(byte[] mac) {
        if (mac == null || mac.length < 6) return "00:00:00:00:00:00";
        return String.format(Locale.US, "%02X:%02X:%02X:%02X:%02X:%02X",
                mac[0] & 0xFF, mac[1] & 0xFF, mac[2] & 0xFF,
                mac[3] & 0xFF, mac[4] & 0xFF, mac[5] & 0xFF);
    }

    /**
     * Valida que la dirección MAC sea admisible para Ethernet (unicast, no broadcast, no todos ceros).
     */
    public static boolean isPlausibleMac(byte[] mac) {
        if (mac == null || mac.length != 6) return false;
        // Todos ceros
        boolean allZeros = true;
        boolean allOnes = true;
        for (byte b : mac) {
            if (b != 0) allZeros = false;
            if ((b & 0xFF) != 0xFF) allOnes = false;
        }
        if (allZeros || allOnes) return false;

        // Bit 0 del primer byte debe ser 0 para dirección Unicast (si es 1, es Multicast)
        return (mac[0] & 0x01) == 0;
    }

    public static boolean isValidMacString(String macStr) {
        if (macStr == null) return false;
        try {
            byte[] parsed = parseMac(macStr);
            return isPlausibleMac(parsed);
        } catch (Exception e) {
            return false;
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // 3. CÁLCULO Y RECÁLCULO DEL CHECKSUM NVM (0xBABA)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Calcula el valor complementario que debe grabarse en la palabra de checksum especificada
     * para que la suma total de las 64 palabras de 16 bits sea exactamente 0xBABA.
     *
     * Fórmula:
     *   ChecksumWord = (0xBABA - Suma_de_las_otras_63_palabras) & 0xFFFF
     *
     * @param data              Búfer que contiene los datos.
     * @param offset            Offset base de la región GbE (0 a GBE_NVM_SIZE bytes).
     * @param checksumWordIndex Índice de la palabra que almacena el checksum (0 a 63, ej. 0x3F).
     * @return El valor entero de 16 bits sin signo (0x0000 a 0xFFFF) del checksum correcto.
     */
    public static int calculateChecksumWord(byte[] data, int offset, int checksumWordIndex) {
        if (data == null || offset < 0 || offset + GBE_NVM_SIZE > data.length) {
            throw new IllegalArgumentException("Búfer o offset inválido para calcular checksum GbE");
        }
        if (checksumWordIndex < 0 || checksumWordIndex >= GBE_NVM_WORD_COUNT) {
            throw new IllegalArgumentException("Índice de palabra de checksum fuera de rango: " + checksumWordIndex);
        }

        if (sNativeAvailable) {
            try {
                int nativeRes = calculateNvmChecksumNative(data, offset, checksumWordIndex);
                if (nativeRes >= 0) {
                    return nativeRes;
                }
            } catch (Throwable ignored) {
            }
        }

        int sum = 0;
        for (int i = 0; i < GBE_NVM_WORD_COUNT; i++) {
            if (i == checksumWordIndex) {
                continue; // Omitir la palabra de checksum
            }
            int word = readWordLe(data, offset + i * 2);
            sum += word;
        }

        // Checksum necesario para que (sum + checksum) & 0xFFFF == 0xBABA
        return (INTEL_GBE_TARGET_CHECKSUM - (sum & 0xFFFF)) & 0xFFFF;
    }

    /**
     * Sobrecarga conveniente que asume checksumWordIndex = 0x3F y offset = 0.
     */
    public static int calculateChecksumWord(byte[] gbeBlock) {
        return calculateChecksumWord(gbeBlock, 0, DEFAULT_CHECKSUM_WORD_INDEX);
    }

    /**
     * Valida si un bloque de GbE NVM suma exactamente 0xBABA.
     */
    public static boolean validateChecksum(byte[] data, int offset) {
        return validateChecksum(data, offset, DEFAULT_CHECKSUM_WORD_INDEX);
    }

    /**
     * Valida si un bloque GbE suma 0xBABA usando el índice de palabra dado.
     */
    public static boolean validateChecksum(byte[] data, int offset, int checksumWordIndex) {
        if (data == null || offset < 0 || offset + GBE_NVM_SIZE > data.length) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < GBE_NVM_WORD_COUNT; i++) {
            sum += readWordLe(data, offset + i * 2);
        }
        return (sum & 0xFFFF) == INTEL_GBE_TARGET_CHECKSUM;
    }

    // ════════════════════════════════════════════════════════════════════════
    // 4. INYECCIÓN DE MAC Y PARCHEO QUIRÚRGICO
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Inyecta una nueva dirección MAC y recalcula automáticamente el checksum 0xBABA en el búfer.
     *
     * @param data              Búfer binario del volcado o bloque GbE.
     * @param offset            Offset base donde comienza la región GbE.
     * @param newMac            Los 6 bytes de la nueva MAC.
     * @param checksumWordIndex Índice de palabra para el checksum (ej. 0x3F).
     * @param updateBackup      Si es true y existe copia a offset + 0x1000, la actualiza también.
     */
    public static void patchMac(byte[] data, int offset, byte[] newMac, int checksumWordIndex, boolean updateBackup) {
        if (data == null || offset < 0 || offset + GBE_NVM_SIZE > data.length) {
            throw new IllegalArgumentException("Datos o offset inválido para parchear GbE");
        }
        if (newMac == null || newMac.length != 6) {
            throw new IllegalArgumentException("La dirección MAC debe contener exactamente 6 bytes");
        }

        // 1. Escribir MAC en la región principal (offset + 0x00..0x05)
        System.arraycopy(newMac, 0, data, offset, 6);

        // 2. Recalcular y escribir el checksum en la palabra designada
        int chk = calculateChecksumWord(data, offset, checksumWordIndex);
        writeWordLe(data, offset + checksumWordIndex * 2, chk);

        // 3. Parchear copia de respaldo si se solicita
        if (updateBackup && offset + 0x1000 + GBE_NVM_SIZE <= data.length) {
            System.arraycopy(newMac, 0, data, offset + 0x1000, 6);
            int backupChk = calculateChecksumWord(data, offset + 0x1000, checksumWordIndex);
            writeWordLe(data, offset + 0x1000 + checksumWordIndex * 2, backupChk);
        }
    }

    /**
     * Parchea la MAC dada como String en el volcado en memoria.
     */
    public static void patchMac(byte[] data, int offset, String newMacStr, boolean updateBackup) {
        byte[] mac = parseMac(newMacStr);
        patchMac(data, offset, mac, DEFAULT_CHECKSUM_WORD_INDEX, updateBackup);
    }

    /**
     * Parchea directamente un archivo binario en disco de forma segura in-place.
     */
    public static void patchMacInFile(File binFile, long gbeOffset, String newMacStr, boolean updateBackup) throws IOException {
        if (binFile == null || !binFile.exists() || !binFile.isFile()) {
            throw new IOException("Archivo no existe: " + binFile);
        }
        byte[] mac = parseMac(newMacStr);

        try (RandomAccessFile raf = new RandomAccessFile(binFile, "rw")) {
            if (gbeOffset + GBE_NVM_SIZE > raf.length()) {
                throw new IOException("Offset GbE fuera de los límites del archivo: " + gbeOffset);
            }

            // Leer bloque GbE actual
            byte[] block = new byte[GBE_NVM_SIZE];
            raf.seek(gbeOffset);
            raf.readFully(block);

            // Parchear en memoria el bloque principal
            patchMac(block, 0, mac, DEFAULT_CHECKSUM_WORD_INDEX, false);

            // Escribir bloque principal de vuelta
            raf.seek(gbeOffset);
            raf.write(block);

            // Parchear copia de respaldo si corresponde
            if (updateBackup && gbeOffset + 0x1000 + GBE_NVM_SIZE <= raf.length()) {
                byte[] backupBlock = new byte[GBE_NVM_SIZE];
                raf.seek(gbeOffset + 0x1000);
                raf.readFully(backupBlock);
                patchMac(backupBlock, 0, mac, DEFAULT_CHECKSUM_WORD_INDEX, false);
                raf.seek(gbeOffset + 0x1000);
                raf.write(backupBlock);
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // 5. DECODIFICACIÓN DEL INTEL FLASH DESCRIPTOR (FRBA)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Lee la tabla de regiones del Flash Descriptor en offset 0x00 para obtener el offset de la región GbE (Region 3).
     * Retorna -1 si no hay Flash Descriptor válido o si la región GbE no está asignada.
     */
    public static long getGbeOffsetFromFlashDescriptor(byte[] dump) {
        if (dump == null || dump.length < 0x1000) {
            return -1;
        }

        // Comprobar firma en offset 0x10: 0x0FF0A55A (Little Endian)
        int sig = readDwordLe(dump, 0x10);
        if (sig != INTEL_FD_SIGNATURE) {
            return -1;
        }

        // Offset 0x14: Flash Map 0
        // Bits [15:8] = FRBA (Flash Region Base Address) >> 4
        int flashMap0 = readDwordLe(dump, 0x14);
        int frba = ((flashMap0 >> 12) & 0xFF) << 4;
        if (frba == 0 || frba + 16 > dump.length) {
            // Convención habitual si FRBA está en 0x40
            frba = 0x40;
        }

        // Region 3 = GbE: FRBA + 12 (4 bytes por entrada de región)
        if (frba + 16 <= dump.length) {
            int gbeEntry = readDwordLe(dump, frba + 12);
            // Formato de entrada de región: Bits [14:0] = Base >> 12, Bits [30:16] = Limit >> 12
            int base = (gbeEntry & 0x7FFF) << 12;
            int limit = ((gbeEntry >> 16) & 0x7FFF) << 12;
            if (base > 0 && limit >= base) {
                return (long) base;
            }
        }

        return -1;
    }

    // ════════════════════════════════════════════════════════════════════════
    // UTILIDADES BINARIAS LITTLE-ENDIAN
    // ════════════════════════════════════════════════════════════════════════

    public static int readWordLe(byte[] data, int offset) {
        return (data[offset] & 0xFF) | ((data[offset + 1] & 0xFF) << 8);
    }

    public static void writeWordLe(byte[] data, int offset, int word) {
        data[offset] = (byte) (word & 0xFF);
        data[offset + 1] = (byte) ((word >> 8) & 0xFF);
    }

    public static int readDwordLe(byte[] data, int offset) {
        return (data[offset] & 0xFF) |
                ((data[offset + 1] & 0xFF) << 8) |
                ((data[offset + 2] & 0xFF) << 16) |
                ((data[offset + 3] & 0xFF) << 24);
    }
}
