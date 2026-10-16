package com.diamon.curso.binary;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Tests unitarios para DmiPatcher.
 * Validan la codificación de UUID (SMBIOS 2.6+ vs RFC 4122),
 * el parcheo de número de serie y el parseo de estructuras Type 1.
 */
public class DmiPatcherTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private static final String TEST_UUID_STR = "4c4c4544-004b-4e10-8032-b8c04f473332";

    @Test
    public void testUuidFormattingAndParsing() {
        UUID originalUuid = UUID.fromString(TEST_UUID_STR);

        // 1. Probar codificación SMBIOS (Little-Endian en los 3 primeros campos)
        byte[] smbiosBytes = DmiPatcher.uuidToBytes(originalUuid, true);
        assertEquals(16, smbiosBytes.length);
        UUID decodedSmbios = DmiPatcher.parseUuidBytes(smbiosBytes, true);
        assertEquals(originalUuid, decodedSmbios);

        // 2. Probar codificación RFC 4122 (Big-Endian puro)
        byte[] rfcBytes = DmiPatcher.uuidToBytes(originalUuid, false);
        assertEquals(16, rfcBytes.length);
        UUID decodedRfc = DmiPatcher.parseUuidBytes(rfcBytes, false);
        assertEquals(originalUuid, decodedRfc);

        assertTrue(DmiPatcher.isValidUuidString(TEST_UUID_STR));
        assertFalse(DmiPatcher.isValidUuidString("invalid-uuid-string"));
    }

    @Test
    public void testPatchUuid() {
        byte[] buffer = new byte[256];
        Arrays.fill(buffer, (byte) 0);

        UUID newUuid = UUID.fromString(TEST_UUID_STR);
        int uuidOffset = 64;

        DmiPatcher.patchUuid(buffer, uuidOffset, newUuid, true);

        byte[] extracted = new byte[16];
        System.arraycopy(buffer, uuidOffset, extracted, 0, 16);
        UUID roundtrip = DmiPatcher.parseUuidBytes(extracted, true);

        assertEquals(newUuid, roundtrip);
    }

    @Test
    public void testPatchSerialNumber() {
        byte[] buffer = new byte[128];
        String oldSerial = "PF123456";
        int offset = 32;

        byte[] oldBytes = oldSerial.getBytes(StandardCharsets.US_ASCII);
        System.arraycopy(oldBytes, 0, buffer, offset, oldBytes.length);

        String newSerial = "PF999999";
        boolean success = DmiPatcher.patchSerialNumber(buffer, offset, oldSerial, newSerial);
        assertTrue(success);

        String readBack = new String(buffer, offset, newSerial.length(), StandardCharsets.US_ASCII);
        assertEquals(newSerial, readBack);

        // Probar con serial más corto (debe rellenar con nulos)
        String shortSerial = "PF00";
        boolean shortSuccess = DmiPatcher.patchSerialNumber(buffer, offset, newSerial, shortSerial);
        assertTrue(shortSuccess);
        assertEquals((byte) 0, buffer[offset + shortSerial.length()]);

        // Probar con serial más largo que la capacidad (debe retornar false)
        String tooLong = "PF1234567890EXTRA";
        assertFalse(DmiPatcher.patchSerialNumber(buffer, offset, oldSerial, tooLong));
    }

    @Test
    public void testExtractDmiInfoSynthetic() {
        // Construir estructura SMBIOS Type 1 sintética
        byte[] buffer = new byte[1024];
        int type1Pos = 128;

        // [0x00] Type = 1
        buffer[type1Pos] = 1;
        // [0x01] Length = 0x19 (25 bytes)
        buffer[type1Pos + 1] = 0x19;
        // [0x04] Fabricante string index = 1
        buffer[type1Pos + 4] = 1;
        // [0x05] Producto string index = 2
        buffer[type1Pos + 5] = 2;
        // [0x07] Serial string index = 3
        buffer[type1Pos + 7] = 3;

        // [0x08..0x17] UUID
        UUID expectedUuid = UUID.fromString(TEST_UUID_STR);
        byte[] uuidBytes = DmiPatcher.uuidToBytes(expectedUuid, true);
        System.arraycopy(uuidBytes, 0, buffer, type1Pos + 8, 16);

        // Tabla de strings inmediatamente después de formatted area (type1Pos + 25)
        int strPos = type1Pos + 0x19;
        String mfg = "DELL Inc.";
        String prod = "OptiPlex 7050";
        String ser = "8B12XYZ";

        System.arraycopy(mfg.getBytes(StandardCharsets.US_ASCII), 0, buffer, strPos, mfg.length());
        strPos += mfg.length();
        buffer[strPos++] = 0; // null separator

        System.arraycopy(prod.getBytes(StandardCharsets.US_ASCII), 0, buffer, strPos, prod.length());
        strPos += prod.length();
        buffer[strPos++] = 0; // null separator

        System.arraycopy(ser.getBytes(StandardCharsets.US_ASCII), 0, buffer, strPos, ser.length());
        strPos += ser.length();
        buffer[strPos++] = 0; // null separator

        buffer[strPos] = 0; // double null finalizer

        DmiPatcher.DmiInfo info = DmiPatcher.extractDmiInfo(buffer);

        assertTrue("Debe detectar la información DMI", info.isFound());
        assertEquals(mfg, info.getManufacturer());
        assertEquals(prod, info.getProductName());
        assertEquals(ser, info.getSerialNumber());
        assertEquals(expectedUuid, info.getUuid());
        assertTrue(info.getSummary().contains(prod));
    }
}
