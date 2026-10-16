package com.diamon.curso.diff;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class MappedDiffDataSourceTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testIdenticalBuffers() {
        byte[] a = new byte[8192];
        byte[] b = new byte[8192];

        try (MappedDiffDataSource ds = new MappedDiffDataSource(a, b)) {
            assertEquals(2, ds.getSectorCount());
            ds.scan(null);
            assertTrue(ds.isIdentical());
            assertEquals(0, ds.getTotalDiffBytes());
            assertEquals(0, ds.getModifiedSectorsCount());
        }
    }

    @Test
    public void testModifiedSectorsCalculation() {
        byte[] a = new byte[8192];
        byte[] b = new byte[8192];

        // Modificar 10 bytes en el sector 0 (offset 100 a 109)
        for (int i = 100; i < 110; i++) {
            b[i] = (byte) 0xAA;
        }

        // Modificar 5 bytes en el sector 1 (offset 4096 + 20 a 4096 + 24)
        for (int i = 4096 + 20; i < 4096 + 25; i++) {
            b[i] = (byte) 0xBB;
        }

        try (MappedDiffDataSource ds = new MappedDiffDataSource(a, b)) {
            assertEquals(2, ds.getSectorCount());
            ds.scan(null);

            assertFalse(ds.isIdentical());
            assertEquals(15, ds.getTotalDiffBytes());
            assertEquals(2, ds.getModifiedSectorsCount());

            assertEquals(10, ds.getDiffCountInSector(0));
            assertEquals(5, ds.getDiffCountInSector(1));

            assertTrue(ds.isSectorModified(0));
            assertTrue(ds.isSectorModified(1));

            List<Integer> modified = ds.getModifiedSectors();
            assertEquals(2, modified.size());
            assertEquals(Integer.valueOf(0), modified.get(0));
            assertEquals(Integer.valueOf(1), modified.get(1));

            MappedDiffDataSource.SectorInfo s0 = ds.getSectorInfo(0);
            assertNotNull(s0);
            assertEquals(0, s0.startOffset);
            assertEquals(4096, s0.endOffset);
            assertEquals(10, s0.diffCount);
            assertTrue(s0.hasDifferences());
        }
    }

    @Test
    public void testMappedFilesComparison() throws IOException {
        File fileA = folder.newFile("test_a.bin");
        File fileB = folder.newFile("test_b.bin");

        byte[] a = new byte[4096];
        byte[] b = new byte[4096];
        b[0] = 0x55;
        b[4095] = 0x77;

        try (FileOutputStream fosA = new FileOutputStream(fileA);
             FileOutputStream fosB = new FileOutputStream(fileB)) {
            fosA.write(a);
            fosB.write(b);
        }

        try (MappedDiffDataSource ds = new MappedDiffDataSource(fileA, fileB)) {
            assertEquals(1, ds.getSectorCount());
            ds.scan(null);

            assertEquals(2, ds.getTotalDiffBytes());
            assertEquals(1, ds.getModifiedSectorsCount());
            assertEquals((byte) 0x55, ds.getByteB(0));
            assertEquals((byte) 0x77, ds.getByteB(4095));
            assertEquals((byte) 0x00, ds.getByteA(0));
        }
    }
}
