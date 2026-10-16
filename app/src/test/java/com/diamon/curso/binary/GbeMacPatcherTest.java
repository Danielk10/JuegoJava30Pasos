package com.diamon.curso.binary;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Tests unitarios para GbeMacPatcher.
 * Validan el algoritmo del checksum NVM 0xBABA, la inyección de direcciones MAC,
 * el parseo de formatos y la detección de regiones GbE en imágenes completas.
 */
public class GbeMacPatcherTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testParseAndFormatMac() {
        String original = "00:1B:21:36:48:9A";
        byte[] bytes = GbeMacPatcher.parseMac(original);
        assertEquals(6, bytes.length);
        assertEquals((byte) 0x00, bytes[0]);
        assertEquals((byte) 0x1B, bytes[1]);
        assertEquals((byte) 0x21, bytes[2]);
        assertEquals((byte) 0x36, bytes[3]);
        assertEquals((byte) 0x48, bytes[4]);
        assertEquals((byte) 0x9A, bytes[5]);

        // Formatos con guiones y sin separadores
        assertArrayEquals(bytes, GbeMacPatcher.parseMac("00-1B-21-36-48-9A"));
        assertArrayEquals(bytes, GbeMacPatcher.parseMac("001B2136489A"));

        // Reconvertir a String
        assertEquals(original, GbeMacPatcher.formatMac(bytes));
    }

    @Test
    public void testIsPlausibleMac() {
        assertTrue(GbeMacPatcher.isPlausibleMac(GbeMacPatcher.parseMac("00:1B:21:36:48:9A")));
        assertTrue(GbeMacPatcher.isPlausibleMac(GbeMacPatcher.parseMac("A4:BB:6D:11:22:33")));

        // Multicast (bit 0 del primer byte es 1)
        assertFalse(GbeMacPatcher.isPlausibleMac(GbeMacPatcher.parseMac("01:00:5E:00:00:01")));

        // Broadcast (todo FF)
        assertFalse(GbeMacPatcher.isPlausibleMac(GbeMacPatcher.parseMac("FF:FF:FF:FF:FF:FF")));

        // Todo ceros
        assertFalse(GbeMacPatcher.isPlausibleMac(GbeMacPatcher.parseMac("00:00:00:00:00:00")));
    }

    @Test
    public void testChecksumCalculation0xBABA() {
        // Crear un bloque NVM sintético de 128 bytes (64 palabras)
        byte[] nvmBlock = new byte[GbeMacPatcher.GBE_NVM_SIZE];

        // Llenar con datos sintéticos deterministas
        for (int i = 0; i < nvmBlock.length; i++) {
            nvmBlock[i] = (byte) ((i * 37 + 13) & 0xFF);
        }

        // Configurar una MAC válida en los primeros 6 bytes
        byte[] mac = GbeMacPatcher.parseMac("70:85:C2:55:66:77");
        System.arraycopy(mac, 0, nvmBlock, 0, 6);

        // Inicialmente el checksum en Word 0x3F no suma 0xBABA
        int initialSum = 0;
        for (int i = 0; i < 64; i++) {
            initialSum += GbeMacPatcher.readWordLe(nvmBlock, i * 2);
        }

        // Calcular el checksum complementario
        int chk = GbeMacPatcher.calculateChecksumWord(nvmBlock, 0, GbeMacPatcher.DEFAULT_CHECKSUM_WORD_INDEX);
        GbeMacPatcher.writeWordLe(nvmBlock, GbeMacPatcher.DEFAULT_CHECKSUM_WORD_INDEX * 2, chk);

        // Validar que la suma total de las 64 palabras de 16 bits sea estrictamente 0xBABA
        int totalSum = 0;
        for (int i = 0; i < 64; i++) {
            totalSum += GbeMacPatcher.readWordLe(nvmBlock, i * 2);
        }
        assertEquals("La suma total mod 65536 debe ser 0xBABA",
                GbeMacPatcher.INTEL_GBE_TARGET_CHECKSUM, totalSum & 0xFFFF);

        assertTrue("GbeMacPatcher.validateChecksum debe retornar true",
                GbeMacPatcher.validateChecksum(nvmBlock, 0));
    }

    @Test
    public void testPatchMacAndVerify() {
        byte[] nvmBlock = new byte[GbeMacPatcher.GBE_NVM_SIZE];
        Arrays.fill(nvmBlock, (byte) 0x11);

        String newMac = "00:24:D7:99:88:77";
        GbeMacPatcher.patchMac(nvmBlock, 0, newMac, false);

        // Verificar que los bytes de la MAC se escribieron exactamente en offset 0..5
        byte[] extractedMac = new byte[6];
        System.arraycopy(nvmBlock, 0, extractedMac, 0, 6);
        assertEquals(newMac, GbeMacPatcher.formatMac(extractedMac));

        // Verificar que el checksum 0xBABA es válido
        assertTrue(GbeMacPatcher.validateChecksum(nvmBlock, 0));
    }

    @Test
    public void testPatchMacInFileWithBackup() throws IOException {
        // Archivo que simula una ROM con región GbE principal en 0x1000 y backup en 0x2000
        byte[] fullRom = new byte[0x4000]; // 16 KB
        Arrays.fill(fullRom, (byte) 0xFF);

        File romFile = tempFolder.newFile("bios_with_gbe.bin");
        try (FileOutputStream fos = new FileOutputStream(romFile)) {
            fos.write(fullRom);
        }

        String targetMac = "54:EE:75:12:34:56";
        GbeMacPatcher.patchMacInFile(romFile, 0x1000, targetMac, true);

        // Leer de nuevo para comprobar
        byte[] readBack = new byte[fullRom.length];
        try (java.io.FileInputStream fis = new java.io.FileInputStream(romFile)) {
            fis.read(readBack);
        }

        // Inspeccionar región principal (0x1000)
        GbeMacPatcher.GbeRegionInfo infoMain = GbeMacPatcher.inspectGbeBlock(readBack, 0x1000);
        assertNotNull(infoMain);
        assertEquals(targetMac, infoMain.getMacAddress());
        assertTrue(infoMain.isChecksumValid());
        assertTrue(infoMain.hasBackupRegion());

        // Inspeccionar región de respaldo (0x2000)
        GbeMacPatcher.GbeRegionInfo infoBackup = GbeMacPatcher.inspectGbeBlock(readBack, 0x2000);
        assertNotNull(infoBackup);
        assertEquals(targetMac, infoBackup.getMacAddress());
        assertTrue(infoBackup.isChecksumValid());
    }

    @Test
    public void testDetectGbeRegionInBiosDump() {
        byte[] dump = new byte[0x8000]; // 32 KB
        Arrays.fill(dump, (byte) 0xFF);

        // Inyectar GbE válida en offset 0x2000
        byte[] gbePart = new byte[GbeMacPatcher.GBE_NVM_SIZE];
        Arrays.fill(gbePart, (byte) 0x22);
        System.arraycopy(gbePart, 0, dump, 0x2000, gbePart.length);

        String testMac = "3C:D9:2B:44:55:66";
        GbeMacPatcher.patchMac(dump, 0x2000, testMac, false);

        GbeMacPatcher.GbeRegionInfo detected = GbeMacPatcher.detectGbeRegion(dump);
        assertNotNull("Debe detectar la región GbE", detected);
        assertEquals(0x2000L, detected.getOffset());
        assertEquals(testMac, detected.getMacAddress());
        assertTrue(detected.isChecksumValid());
    }
}
