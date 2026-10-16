package com.diamon.curso.binary;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Tests unitarios para MsdmExtractor.
 * Validan el escaneo de firmas ACPI MSDM, la extracción de claves digitales OEM Windows de 25 caracteres,
 * el cálculo de checksums ACPI y la compatibilidad con streaming sobre archivos.
 */
public class MsdmExtractorTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private static final String SAMPLE_KEY = "VK7JG-NPHTM-C97JM-9MPGT-3V66T";
    private static final String OEM_ID = "LENOVO";
    private static final String OEM_TABLE_ID = "TC-M900 ";

    /**
     * Helper para construir una tabla ACPI MSDM sintética válida de 85 bytes.
     */
    public static byte[] createSyntheticMsdmTable(String key, String oemId, String oemTableId) {
        byte[] table = new byte[85];

        // 1. Cabecera ACPI estándar (36 bytes)
        // [0..3] "MSDM"
        byte[] sig = "MSDM".getBytes(StandardCharsets.US_ASCII);
        System.arraycopy(sig, 0, table, 0, 4);

        // [4..7] Longitud (85 = 0x00000055)
        table[4] = 85;
        table[5] = 0;
        table[6] = 0;
        table[7] = 0;

        // [8] Revisión (1)
        table[8] = 1;

        // [9] Checksum: se calcula al final

        // [10..15] OEMID (6 bytes)
        byte[] oemBytes = Arrays.copyOf(oemId.getBytes(StandardCharsets.US_ASCII), 6);
        System.arraycopy(oemBytes, 0, table, 10, 6);

        // [16..23] OEM Table ID (8 bytes)
        byte[] tableIdBytes = Arrays.copyOf(oemTableId.getBytes(StandardCharsets.US_ASCII), 8);
        System.arraycopy(tableIdBytes, 0, table, 16, 8);

        // 2. Estructura SLS de Microsoft (después de offset 36)
        // [36..39] SLS Version (1)
        table[36] = 1;
        // [44..47] Data Type (1 = Product Key)
        table[44] = 1;
        // [52..55] Data Length (29)
        table[52] = 29;

        // [56..84] Clave de producto de 29 bytes
        byte[] keyBytes = key.getBytes(StandardCharsets.US_ASCII);
        System.arraycopy(keyBytes, 0, table, 56, Math.min(keyBytes.length, 29));

        // Calcular Checksum ACPI (la suma de todos los 85 bytes mod 256 debe ser 0)
        int sum = 0;
        for (int i = 0; i < table.length; i++) {
            if (i != 9) { // ignorar el byte de checksum
                sum += (table[i] & 0xFF);
            }
        }
        table[9] = (byte) ((-sum) & 0xFF);

        return table;
    }

    @Test
    public void testExtractKeySyntheticAcpiTable() {
        byte[] table = createSyntheticMsdmTable(SAMPLE_KEY, OEM_ID, OEM_TABLE_ID);

        // Incrustar la tabla en un volcado de 64KB en offset 0x4000
        byte[] dump = new byte[64 * 1024];
        Arrays.fill(dump, (byte) 0xFF);
        System.arraycopy(table, 0, dump, 0x4000, table.length);

        MsdmExtractor.MsdmResult result = MsdmExtractor.extractKey(dump);

        assertTrue("La clave debe ser detectada", result.isFound());
        assertEquals(SAMPLE_KEY, result.getProductKey());
        assertEquals(OEM_ID, result.getOemId());
        assertEquals(OEM_TABLE_ID.trim(), result.getOemTableId());
        assertEquals(0x4000L, result.getTableOffset());
        assertEquals(85, result.getTableLength());
        assertTrue("El checksum ACPI debe ser válido", result.isChecksumValid());
        assertTrue(result.getFormattedSummary().contains(SAMPLE_KEY));
    }

    @Test
    public void testExtractKeyFromFile() throws IOException {
        byte[] table = createSyntheticMsdmTable(SAMPLE_KEY, "DELL  ", "DELL_M48");
        byte[] dump = new byte[128 * 1024];
        System.arraycopy(table, 0, dump, 0x8000, table.length);

        File dumpFile = tempFolder.newFile("bios_with_msdm.bin");
        try (FileOutputStream fos = new FileOutputStream(dumpFile)) {
            fos.write(dump);
        }

        MsdmExtractor.MsdmResult result = MsdmExtractor.extractFromFile(dumpFile);

        assertTrue("Debe extraer la clave desde el archivo", result.isFound());
        assertEquals(SAMPLE_KEY, result.getProductKey());
        assertEquals("DELL", result.getOemId());
        assertEquals(0x8000L, result.getTableOffset());
        assertTrue(result.isChecksumValid());
    }

    @Test
    public void testIsValidProductKey() {
        // Claves válidas
        assertTrue(MsdmExtractor.isValidProductKey(SAMPLE_KEY));
        assertTrue(MsdmExtractor.isValidProductKey("W269N-WFGWX-YVC9B-4J6C9-T83GX"));
        assertTrue(MsdmExtractor.isValidProductKey("TX9XD-98N7V-6WMQ6-BX7FG-H8Q99"));

        // Claves inválidas
        assertFalse(MsdmExtractor.isValidProductKey(""));
        assertFalse(MsdmExtractor.isValidProductKey(null));
        assertFalse(MsdmExtractor.isValidProductKey("12345-12345-12345")); // muy corta
        assertFalse(MsdmExtractor.isValidProductKey("VK7JG-NPHTM-C97JM-9MPGT-3V66T-EXTRA")); // muy larga
        assertFalse(MsdmExtractor.isValidProductKey("VK7JGNPHTMC97JM9MPGT3V66T")); // sin guiones
    }

    @Test
    public void testExtractKeyNotFound() {
        byte[] emptyDump = new byte[32 * 1024];
        Arrays.fill(emptyDump, (byte) 0xAA);

        MsdmExtractor.MsdmResult result = MsdmExtractor.extractKey(emptyDump);
        assertFalse(result.isFound());
        assertEquals(-1L, result.getTableOffset());
        assertEquals("", result.getProductKey());
    }
}
