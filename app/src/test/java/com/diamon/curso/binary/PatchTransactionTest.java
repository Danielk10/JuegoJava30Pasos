package com.diamon.curso.binary;

import org.junit.Test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

public class PatchTransactionTest {

    @Test
    public void testSingleByteModificationAndUndoRedo() {
        PatchTransaction tx = new PatchTransaction();
        assertFalse(tx.canUndo());
        assertFalse(tx.canRedo());
        assertEquals(0, tx.getModifiedCount());

        // Modificar offset 0x10 de 0x00 a 0x55
        boolean changed = tx.modifyByte(0x10, (byte) 0x00, (byte) 0x55);
        assertTrue(changed);
        assertTrue(tx.isModified(0x10));
        assertEquals((byte) 0x55, tx.getByte(0x10, (byte) 0x00));
        assertEquals(1, tx.getModifiedCount());
        assertTrue(tx.canUndo());
        assertFalse(tx.canRedo());

        // Undo
        assertTrue(tx.undo());
        assertFalse(tx.isModified(0x10));
        assertEquals((byte) 0x00, tx.getByte(0x10, (byte) 0x00));
        assertEquals(0, tx.getModifiedCount());
        assertFalse(tx.canUndo());
        assertTrue(tx.canRedo());

        // Redo
        assertTrue(tx.redo());
        assertTrue(tx.isModified(0x10));
        assertEquals((byte) 0x55, tx.getByte(0x10, (byte) 0x00));
        assertEquals(1, tx.getModifiedCount());
    }

    @Test
    public void testRevertingToOriginalClearsModification() {
        PatchTransaction tx = new PatchTransaction();
        tx.modifyByte(0x20, (byte) 0xAA, (byte) 0xBB);
        assertTrue(tx.isModified(0x20));

        // Revertir a original
        tx.modifyByte(0x20, (byte) 0xAA, (byte) 0xAA);
        assertFalse(tx.isModified(0x20));
        assertEquals(0, tx.getModifiedCount());
    }

    @Test
    public void testBlockModificationAndApplyToMemory() {
        PatchTransaction tx = new PatchTransaction();
        byte[] buffer = new byte[]{0x10, 0x20, 0x30, 0x40, 0x50};
        byte[] newBytes = new byte[]{(byte) 0xFF, (byte) 0xEE};

        boolean changed = tx.modifyBytes(1, new byte[]{0x20, 0x30}, newBytes, "Test block");
        assertTrue(changed);
        assertEquals(2, tx.getModifiedCount());
        assertTrue(tx.isModified(1));
        assertTrue(tx.isModified(2));

        byte[] patched = tx.applyTo(buffer);
        assertArrayEquals(new byte[]{0x10, (byte) 0xFF, (byte) 0xEE, 0x40, 0x50}, patched);

        // Undo atomically reverts both
        tx.undo();
        assertEquals(0, tx.getModifiedCount());
        byte[] reverted = tx.applyTo(buffer);
        assertArrayEquals(buffer, reverted);
    }

    @Test
    public void testApplyToFile() throws IOException {
        File temp = File.createTempFile("test_patch", ".bin");
        temp.deleteOnExit();

        try (FileOutputStream fos = new FileOutputStream(temp)) {
            fos.write(new byte[]{0x01, 0x02, 0x03, 0x04});
        }

        PatchTransaction tx = new PatchTransaction();
        tx.modifyByte(1, (byte) 0x02, (byte) 0x99);
        tx.modifyByte(3, (byte) 0x04, (byte) 0x88);

        File target = File.createTempFile("test_patch_out", ".bin");
        target.deleteOnExit();

        tx.applyToFile(temp, target);

        byte[] result = java.nio.file.Files.readAllBytes(target.toPath());
        assertArrayEquals(new byte[]{0x01, (byte) 0x99, 0x03, (byte) 0x88}, result);
    }
}
