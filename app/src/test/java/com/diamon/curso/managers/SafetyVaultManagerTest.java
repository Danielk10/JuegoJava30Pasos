package com.diamon.curso.managers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests unitarios para SafetyVaultManager.
 * Validan la creación atómica de snapshots pre-flash, el rollback de emergencia en 1 clic,
 * el almacenamiento de metadatos SHA-256 y la poda automática (pruning).
 */
public class SafetyVaultManagerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File vaultDir;
    private SafetyVaultManager vaultManager;

    @Before
    public void setUp() throws IOException {
        vaultDir = tempFolder.newFolder("vault_test");
        vaultManager = new SafetyVaultManager(vaultDir);
    }

    @Test
    public void testCreatePreFlashSnapshotFromFile() throws IOException {
        byte[] payload = "CONTENIDO_DUMP_BIOS_ORIGINAL_W25Q128".getBytes(StandardCharsets.US_ASCII);
        File sourceFile = tempFolder.newFile("chip_read.bin");
        try (FileOutputStream fos = new FileOutputStream(sourceFile)) {
            fos.write(payload);
        }

        SafetyVaultManager.VaultBackup backup = vaultManager.createPreFlashSnapshot(sourceFile, "W25Q128FV");

        assertNotNull(backup);
        assertTrue(backup.getFile().exists());
        assertTrue(backup.getMetaFile().exists());
        assertEquals("W25Q128FV", backup.getChipName());
        assertEquals(payload.length, backup.getSizeBytes());
        assertFalse(backup.getSha256().isEmpty());
        assertTrue(backup.getFile().getName().contains("W25Q128FV"));
    }

    @Test
    public void testCreatePreFlashSnapshotFromBytes() throws IOException {
        byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        SafetyVaultManager.VaultBackup backup = vaultManager.createPreFlashSnapshot(data, "MX25L6405");

        assertNotNull(backup);
        assertEquals(5, backup.getSizeBytes());
        assertEquals("MX25L6405", backup.getChipName());
        assertTrue(backup.getSummary().contains("MX25L6405"));
    }

    @Test
    public void testGetLatestBackupForRollback() throws Exception {
        byte[] data1 = "BACKUP_1".getBytes(StandardCharsets.US_ASCII);
        byte[] data2 = "BACKUP_2".getBytes(StandardCharsets.US_ASCII);

        vaultManager.createPreFlashSnapshot(data1, "CHIP_A");
        Thread.sleep(1050); // Garantizar segundo diferente en el timestamp
        vaultManager.createPreFlashSnapshot(data2, "CHIP_A");

        List<SafetyVaultManager.VaultBackup> all = vaultManager.listBackups();
        assertEquals(2, all.size());

        SafetyVaultManager.VaultBackup latest = vaultManager.getLatestBackup();
        assertNotNull(latest);
        assertEquals("CHIP_A", latest.getChipName());
        assertEquals(data2.length, latest.getSizeBytes());
    }

    @Test
    public void testRestoreBackup() throws IOException {
        byte[] originalData = "DATOS_DE_RECUPERACION_ANTIBRICK".getBytes(StandardCharsets.US_ASCII);
        SafetyVaultManager.VaultBackup backup = vaultManager.createPreFlashSnapshot(originalData, "EN25Q64");

        File destination = tempFolder.newFile("restored_firmware.bin");
        boolean ok = vaultManager.restoreBackup(backup, destination);
        assertTrue("La restauración debe ser exitosa", ok);
        assertEquals(originalData.length, destination.length());
    }

    @Test
    public void testPruneOldBackups() throws Exception {
        for (int i = 0; i < 5; i++) {
            vaultManager.createPreFlashSnapshot(new byte[]{(byte) i}, "CHIP_" + i);
            Thread.sleep(100);
        }

        assertEquals(5, vaultManager.listBackups().size());

        int deleted = vaultManager.pruneOldBackups(2);
        assertEquals(3, deleted);
        assertEquals(2, vaultManager.listBackups().size());
    }

    @Test
    public void testDeleteBackup() throws IOException {
        SafetyVaultManager.VaultBackup backup = vaultManager.createPreFlashSnapshot(new byte[]{0x77}, "TEST_CHIP");
        assertTrue(backup.getFile().exists());
        assertTrue(backup.getMetaFile().exists());

        boolean deleted = vaultManager.deleteBackup(backup);
        assertTrue(deleted);
        assertFalse(backup.getFile().exists());
        assertFalse(backup.getMetaFile().exists());
    }

    @Test
    public void testGetTotalVaultSizeBytes() throws IOException {
        vaultManager.createPreFlashSnapshot(new byte[100], "CHIP_1");
        vaultManager.createPreFlashSnapshot(new byte[250], "CHIP_2");

        assertEquals(350, vaultManager.getTotalVaultSizeBytes());
    }
}
