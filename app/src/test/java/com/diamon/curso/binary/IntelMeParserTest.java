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
 * Tests unitarios para IntelMeParser.
 * Validan la detección de cabecera $FPT, enumeración de particiones,
 * extracción de versión ME (Major.Minor.Hotfix.Build) y clasificación de SKU.
 */
public class IntelMeParserTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Helper para construir un bloque sintético de Intel ME con cabecera $FPT y manifiesto $MAN.
     */
    public static byte[] createSyntheticIntelMeImage(int major, int minor, int hotfix, int build, boolean corporate) {
        byte[] image = new byte[64 * 1024]; // 64 KB
        Arrays.fill(image, (byte) 0xFF);

        // 1. Cabecera $FPT (32 bytes)
        // [0x00..0x03] "$FPT"
        System.arraycopy("$FPT".getBytes(StandardCharsets.US_ASCII), 0, image, 0, 4);
        // [0x04..0x07] Número de entradas (2 para consumer, 3 para corporate)
        int entryCount = corporate ? 3 : 2;
        image[4] = (byte) entryCount;
        image[5] = 0;
        image[6] = 0;
        image[7] = 0;
        // [0x08..0x0B] Versión de Cabecera (0x20)
        image[8] = 0x20;
        // [0x0D] Longitud de Cabecera (32 = 0x20)
        image[0x0D] = 0x20;

        // Checksum de la cabecera $FPT (la suma de todos los 32 bytes mod 256 debe ser 0)
        int headerSum = 0;
        for (int i = 0; i < 32; i++) {
            if (i != 0x0E) headerSum += (image[i] & 0xFF);
        }
        image[0x0E] = (byte) ((-headerSum) & 0xFF);

        // 2. Entradas de Partición (32 bytes cada una, comienzan en offset 0x20)
        // Entrada 1: FTPR (offset 0x20)
        System.arraycopy("FTPR".getBytes(StandardCharsets.US_ASCII), 0, image, 0x20, 4);
        System.arraycopy("ME  ".getBytes(StandardCharsets.US_ASCII), 0, image, 0x24, 4);
        // Offset relativo de la partición FTPR: 0x1000
        image[0x28] = 0x00;
        image[0x29] = 0x10;
        // Longitud: 0x8000
        image[0x2C] = 0x00;
        image[0x2D] = (byte) 0x80;

        // Entrada 2: NFPR (offset 0x40)
        System.arraycopy("NFPR".getBytes(StandardCharsets.US_ASCII), 0, image, 0x40, 4);
        System.arraycopy("ME  ".getBytes(StandardCharsets.US_ASCII), 0, image, 0x44, 4);
        image[0x48] = 0x00;
        image[0x49] = (byte) 0x90;
        image[0x4C] = 0x00;
        image[0x4D] = 0x20;

        if (corporate) {
            // Entrada 3: MDMV (offset 0x60)
            System.arraycopy("MDMV".getBytes(StandardCharsets.US_ASCII), 0, image, 0x60, 4);
            System.arraycopy("AMT ".getBytes(StandardCharsets.US_ASCII), 0, image, 0x64, 4);
        }

        // 3. Manifiesto $MAN dentro de la partición FTPR (en offset 0x1000)
        int ftprBase = 0x1000;
        System.arraycopy("$MAN".getBytes(StandardCharsets.US_ASCII), 0, image, ftprBase, 4);

        // En $MAN, los campos de versión se encuentran en offset + 0x20:
        // Major (uint16), Minor (uint16), Hotfix (uint16), Build (uint16)
        int verOffset = ftprBase + 0x20;
        image[verOffset]     = (byte) (major & 0xFF);
        image[verOffset + 1] = (byte) ((major >> 8) & 0xFF);
        image[verOffset + 2] = (byte) (minor & 0xFF);
        image[verOffset + 3] = (byte) ((minor >> 8) & 0xFF);
        image[verOffset + 4] = (byte) (hotfix & 0xFF);
        image[verOffset + 5] = (byte) ((hotfix >> 8) & 0xFF);
        image[verOffset + 6] = (byte) (build & 0xFF);
        image[verOffset + 7] = (byte) ((build >> 8) & 0xFF);

        return image;
    }

    @Test
    public void testDetectFptSignatureAndPartitions() {
        byte[] meData = createSyntheticIntelMeImage(11, 8, 92, 4222, false);

        IntelMeParser.IntelMeInfo info = IntelMeParser.parseDump(meData);

        assertTrue("Intel ME debe ser detectado", info.isDetected());
        assertEquals("11.8.92.4222", info.getVersion());
        assertEquals(2, info.getPartitionCount());
        assertTrue("SKU debe ser Consumer", info.getSku().contains("Consumer"));
        assertTrue("Checksum FPT debe ser válido", info.isHeaderChecksumValid());
        assertTrue(info.formatSummary().contains("FTPR"));
    }

    @Test
    public void testCorporateSkuDetection() {
        byte[] meData = createSyntheticIntelMeImage(12, 0, 70, 1652, true);

        IntelMeParser.IntelMeInfo info = IntelMeParser.parseDump(meData);

        assertTrue(info.isDetected());
        assertEquals("12.0.70.1652", info.getVersion());
        assertEquals(3, info.getPartitionCount());
        assertTrue("SKU debe ser Corporate / AMT", info.getSku().contains("Corporate"));
    }

    @Test
    public void testParseFromFile() throws IOException {
        byte[] meData = createSyntheticIntelMeImage(14, 1, 50, 1400, false);

        File meFile = tempFolder.newFile("intel_me_region.bin");
        try (FileOutputStream fos = new FileOutputStream(meFile)) {
            fos.write(meData);
        }

        IntelMeParser.IntelMeInfo info = IntelMeParser.parseFile(meFile);

        assertTrue("Debe parsear correctamente desde archivo", info.isDetected());
        assertEquals("14.1.50.1400", info.getVersion());
        assertEquals(2, info.getPartitionCount());
    }

    @Test
    public void testNoMeDetected() {
        byte[] randomData = new byte[8192];
        Arrays.fill(randomData, (byte) 0x00);

        IntelMeParser.IntelMeInfo info = IntelMeParser.parseDump(randomData);
        assertFalse("No debe detectar ME en datos vacíos", info.isDetected());
        assertEquals("No detectada", info.getVersion());
    }
}
