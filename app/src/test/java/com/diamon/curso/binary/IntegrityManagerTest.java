package com.diamon.curso.binary;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Tests unitarios para IntegrityManager.
 * Validan el motor de hash multialgoritmo (MD5, SHA-1, SHA-256, CRC32),
 * el cotejo con el portapapeles y la generación/verificación de sidecars GNU.
 */
public class IntegrityManagerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    // Vector de prueba conocido: "The quick brown fox jumps over the lazy dog"
    private static final byte[] KNOWN_INPUT = "The quick brown fox jumps over the lazy dog".getBytes(StandardCharsets.US_ASCII);
    private static final String EXPECTED_MD5 = "9e107d9d372bb6826bd81d3542a419d6";
    private static final String EXPECTED_SHA1 = "2fd4e1c67a2d28fced849ee1bb76e7391b93eb12";
    private static final String EXPECTED_SHA256 = "d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592";
    private static final String EXPECTED_CRC32 = "414fa339";

    @Test
    public void testComputeHashesKnownData() {
        IntegrityManager.HashResult res = IntegrityManager.computeHashes(KNOWN_INPUT);

        assertEquals(EXPECTED_MD5, res.getMd5());
        assertEquals(EXPECTED_SHA1, res.getSha1());
        assertEquals(EXPECTED_SHA256, res.getSha256());
        assertEquals(EXPECTED_CRC32, res.getCrc32());
        assertEquals(KNOWN_INPUT.length, res.getByteCount());
        assertEquals(4, res.asMap().size());
        assertTrue(res.formatSummary().contains("SHA-256"));
    }

    @Test
    public void testComputeHashesEmptyData() {
        IntegrityManager.HashResult res = IntegrityManager.computeHashes(new byte[0]);

        assertEquals("d41d8cd98f00b204e9800998ecf8427e", res.getMd5());
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", res.getSha1());
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", res.getSha256());
        assertEquals("00000000", res.getCrc32());
        assertEquals(0, res.getByteCount());
    }

    @Test
    public void testComputeHashesStreamingFile() throws IOException {
        File tempFile = tempFolder.newFile("test_dump.bin");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(KNOWN_INPUT);
        }

        final boolean[] progressCalled = new boolean[]{false};
        IntegrityManager.HashResult res = IntegrityManager.computeHashes(tempFile, (read, total) -> {
            progressCalled[0] = true;
            assertEquals(KNOWN_INPUT.length, total);
        });

        assertEquals(EXPECTED_SHA256, res.getSha256());
        assertTrue("El listener de progreso debió invocarse", progressCalled[0]);
    }

    @Test
    public void testClipboardMatchExactSha256() {
        IntegrityManager.HashResult hashes = IntegrityManager.computeHashes(KNOWN_INPUT);
        IntegrityManager.ClipboardMatchResult match = IntegrityManager.checkClipboardMatch(EXPECTED_SHA256, hashes);

        assertTrue(match.isMatched());
        assertTrue(match.isMatch());
        assertTrue(match.hasClipboardHash());
        assertEquals("SHA-256", match.getMatchedAlgorithm());
        assertTrue(match.getMessage().contains("MATCH CERTIFIED"));
    }

    @Test
    public void testClipboardMatchWithPrefixAndFilename() {
        IntegrityManager.HashResult hashes = IntegrityManager.computeHashes(KNOWN_INPUT);
        String clipboardText = "SHA256: " + EXPECTED_SHA256.toUpperCase(Locale.US) + "  bios_backup.bin";
        IntegrityManager.ClipboardMatchResult match = IntegrityManager.checkClipboardMatch(clipboardText, hashes);

        assertTrue(match.isMatched());
        assertEquals("SHA-256", match.getMatchedAlgorithm());
    }

    @Test
    public void testClipboardMatchMismatchedSha256() {
        IntegrityManager.HashResult hashes = IntegrityManager.computeHashes(KNOWN_INPUT);
        String wrongSha256 = "0000000000000000000000000000000000000000000000000000000000000000";
        IntegrityManager.ClipboardMatchResult match = IntegrityManager.checkClipboardMatch(wrongSha256, hashes);

        assertFalse(match.isMatched());
        assertTrue(match.hasClipboardHash());
        assertEquals("SHA-256", match.getMatchedAlgorithm());
        assertTrue(match.getMessage().contains("INTEGRITY WARNING"));
    }

    @Test
    public void testClipboardMatchMd5() {
        IntegrityManager.HashResult hashes = IntegrityManager.computeHashes(KNOWN_INPUT);
        IntegrityManager.ClipboardMatchResult match = IntegrityManager.checkClipboardMatch(EXPECTED_MD5, hashes);

        assertTrue(match.isMatched());
        assertEquals("MD5", match.getMatchedAlgorithm());
    }

    @Test
    public void testClipboardMatchEmptyOrNoHash() {
        IntegrityManager.HashResult hashes = IntegrityManager.computeHashes(KNOWN_INPUT);
        IntegrityManager.ClipboardMatchResult match = IntegrityManager.checkClipboardMatch("Texto arbitrario sin ningún hash", hashes);

        assertFalse(match.isMatched());
        assertFalse(match.hasClipboardHash());
    }

    @Test
    public void testGenerateAndVerifySidecars() throws IOException {
        File binFile = tempFolder.newFile("bios_dump.bin");
        try (FileOutputStream fos = new FileOutputStream(binFile)) {
            fos.write(KNOWN_INPUT);
        }

        IntegrityManager.HashResult hashes = IntegrityManager.computeHashes(binFile);
        IntegrityManager.generateSidecars(binFile, hashes);

        File sha256Sidecar = new File(tempFolder.getRoot(), "bios_dump.bin.sha256");
        File md5Sidecar = new File(tempFolder.getRoot(), "bios_dump.bin.md5");

        assertTrue("El archivo .sha256 debe existir", sha256Sidecar.exists());
        assertTrue("El archivo .md5 debe existir", md5Sidecar.exists());

        // Verificación con el archivo intacto
        assertTrue("La verificación de sidecar .sha256 debe ser exitosa", IntegrityManager.verifySidecar(sha256Sidecar));
        assertTrue("La verificación de sidecar .md5 debe ser exitosa", IntegrityManager.verifySidecar(md5Sidecar));

        // Corromper el archivo binario
        try (FileOutputStream fos = new FileOutputStream(binFile)) {
            fos.write("Corrupted dump content".getBytes(StandardCharsets.US_ASCII));
        }

        assertFalse("La verificación debe fallar tras corromper el binario", IntegrityManager.verifySidecar(sha256Sidecar));
        assertFalse("La verificación debe fallar tras corromper el binario", IntegrityManager.verifySidecar(md5Sidecar));
    }
}
