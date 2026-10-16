package com.diamon.curso.diff;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Fuente de datos de comparación diferencial de alta eficiencia (Zero-OOM)
 * para volcados binarios de 16MB, 32MB y 64MB mediante mapeo en memoria (mmap).
 */
public class MappedDiffDataSource implements Closeable {

    public static final int SECTOR_SIZE = 4096; // 4KB por sector SPI estándar

    public interface ScanProgressListener {
        void onProgress(int scannedSectors, int totalSectors);
        void onScanComplete(long totalDiffBytes, int modifiedSectorsCount);
    }

    public static class SectorInfo {
        public final int sectorIndex;
        public final long startOffset;
        public final long endOffset;
        public final int diffCount;
        public final float diffRatio;

        public SectorInfo(int sectorIndex, long startOffset, long endOffset, int diffCount) {
            this.sectorIndex = sectorIndex;
            this.startOffset = startOffset;
            this.endOffset = endOffset;
            this.diffCount = diffCount;
            long len = Math.max(1, endOffset - startOffset);
            this.diffRatio = (float) diffCount / (float) len;
        }

        public boolean hasDifferences() {
            return diffCount > 0;
        }
    }

    private final SourceReader sourceA;
    private final SourceReader sourceB;
    private final long sizeA;
    private final long sizeB;
    private final long maxLen;
    private final int totalSectors;

    private final int[] sectorDiffs;
    private long totalDiffBytes = 0;
    private int modifiedSectorsCount = 0;
    private boolean scanned = false;

    private interface SourceReader extends Closeable {
        long size();
        byte getByte(long offset);
        int read(long offset, byte[] dest, int destOffset, int length);
    }

    private static class ByteArrayReader implements SourceReader {
        private final byte[] data;

        ByteArrayReader(byte[] data) {
            this.data = data != null ? data : new byte[0];
        }

        @Override
        public long size() {
            return data.length;
        }

        @Override
        public byte getByte(long offset) {
            if (offset < 0 || offset >= data.length) return (byte) 0xFF;
            return data[(int) offset];
        }

        @Override
        public int read(long offset, byte[] dest, int destOffset, int length) {
            if (offset >= data.length) return 0;
            int toRead = (int) Math.min((long) length, data.length - offset);
            System.arraycopy(data, (int) offset, dest, destOffset, toRead);
            return toRead;
        }

        @Override
        public void close() {}
    }

    private static class MappedFileReader implements SourceReader {
        private final RandomAccessFile raf;
        private final FileChannel channel;
        private final MappedByteBuffer buffer;
        private final long size;

        MappedFileReader(File file) throws IOException {
            this.raf = new RandomAccessFile(file, "r");
            this.channel = raf.getChannel();
            this.size = channel.size();
            this.buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
        }

        @Override
        public long size() {
            return size;
        }

        @Override
        public byte getByte(long offset) {
            if (offset < 0 || offset >= size) return (byte) 0xFF;
            synchronized (buffer) {
                return buffer.get((int) offset);
            }
        }

        @Override
        public int read(long offset, byte[] dest, int destOffset, int length) {
            if (offset >= size) return 0;
            int toRead = (int) Math.min((long) length, size - offset);
            synchronized (buffer) {
                buffer.position((int) offset);
                buffer.get(dest, destOffset, toRead);
            }
            return toRead;
        }

        @Override
        public void close() {
            try { channel.close(); } catch (Exception ignored) {}
            try { raf.close(); } catch (Exception ignored) {}
        }
    }

    public MappedDiffDataSource(byte[] dataA, byte[] dataB) {
        this.sourceA = new ByteArrayReader(dataA);
        this.sourceB = new ByteArrayReader(dataB);
        this.sizeA = sourceA.size();
        this.sizeB = sourceB.size();
        this.maxLen = Math.max(sizeA, sizeB);
        this.totalSectors = (int) Math.ceil((double) maxLen / SECTOR_SIZE);
        this.sectorDiffs = new int[totalSectors];
    }

    public MappedDiffDataSource(File fileA, File fileB) throws IOException {
        this.sourceA = new MappedFileReader(fileA);
        this.sourceB = new MappedFileReader(fileB);
        this.sizeA = sourceA.size();
        this.sizeB = sourceB.size();
        this.maxLen = Math.max(sizeA, sizeB);
        this.totalSectors = (int) Math.ceil((double) maxLen / SECTOR_SIZE);
        this.sectorDiffs = new int[totalSectors];
    }

    public MappedDiffDataSource(File fileA, byte[] dataB) throws IOException {
        this.sourceA = new MappedFileReader(fileA);
        this.sourceB = new ByteArrayReader(dataB);
        this.sizeA = sourceA.size();
        this.sizeB = sourceB.size();
        this.maxLen = Math.max(sizeA, sizeB);
        this.totalSectors = (int) Math.ceil((double) maxLen / SECTOR_SIZE);
        this.sectorDiffs = new int[totalSectors];
    }

    public long getSizeA() {
        return sizeA;
    }

    public long getSizeB() {
        return sizeB;
    }

    public long getMaxLen() {
        return maxLen;
    }

    public int getSectorCount() {
        return totalSectors;
    }

    public byte getByteA(long offset) {
        return sourceA.getByte(offset);
    }

    public byte getByteB(long offset) {
        return sourceB.getByte(offset);
    }

    public int readRow(long rowStartOffset, byte[] bufferA, byte[] bufferB, int length) {
        int readA = sourceA.read(rowStartOffset, bufferA, 0, length);
        int readB = sourceB.read(rowStartOffset, bufferB, 0, length);
        return Math.max(readA, readB);
    }

    /**
     * Escanea sincrónicamente todos los sectores para calcular el mapa de diferencias.
     */
    public synchronized void scan(ScanProgressListener listener) {
        byte[] bufA = new byte[SECTOR_SIZE];
        byte[] bufB = new byte[SECTOR_SIZE];

        totalDiffBytes = 0;
        modifiedSectorsCount = 0;

        for (int s = 0; s < totalSectors; s++) {
            long off = (long) s * SECTOR_SIZE;
            int len = (int) Math.min((long) SECTOR_SIZE, maxLen - off);

            int readA = sourceA.read(off, bufA, 0, len);
            int readB = sourceB.read(off, bufB, 0, len);

            int sDiffs = 0;
            for (int i = 0; i < len; i++) {
                long currentOffset = off + i;
                boolean inA = currentOffset < sizeA;
                boolean inB = currentOffset < sizeB;

                if (inA != inB) {
                    // Un archivo llegó a EOF mientras el otro continúa con datos: diferencia estricta
                    sDiffs++;
                } else if (inA && inB) {
                    byte a = bufA[i];
                    byte b = bufB[i];
                    if (a != b) {
                        sDiffs++;
                    }
                }
            }

            sectorDiffs[s] = sDiffs;
            totalDiffBytes += sDiffs;
            if (sDiffs > 0) {
                modifiedSectorsCount++;
            }

            if (listener != null && (s % 64 == 0 || s == totalSectors - 1)) {
                listener.onProgress(s + 1, totalSectors);
            }
        }

        scanned = true;
        if (listener != null) {
            listener.onScanComplete(totalDiffBytes, modifiedSectorsCount);
        }
    }

    public boolean isScanned() {
        return scanned;
    }

    public int getDiffCountInSector(int sectorIndex) {
        if (sectorIndex < 0 || sectorIndex >= sectorDiffs.length) return 0;
        return sectorDiffs[sectorIndex];
    }

    public boolean isSectorModified(int sectorIndex) {
        return getDiffCountInSector(sectorIndex) > 0;
    }

    public float getSectorDiffRatio(int sectorIndex) {
        if (sectorIndex < 0 || sectorIndex >= sectorDiffs.length) return 0f;
        long off = (long) sectorIndex * SECTOR_SIZE;
        long len = Math.max(1, Math.min((long) SECTOR_SIZE, maxLen - off));
        return (float) sectorDiffs[sectorIndex] / (float) len;
    }

    public SectorInfo getSectorInfo(int sectorIndex) {
        if (sectorIndex < 0 || sectorIndex >= totalSectors) return null;
        long start = (long) sectorIndex * SECTOR_SIZE;
        long end = Math.min(start + SECTOR_SIZE, maxLen);
        int diffs = getDiffCountInSector(sectorIndex);
        return new SectorInfo(sectorIndex, start, end, diffs);
    }

    public List<Integer> getModifiedSectors() {
        List<Integer> list = new ArrayList<>(modifiedSectorsCount);
        for (int s = 0; s < totalSectors; s++) {
            if (sectorDiffs[s] > 0) {
                list.add(s);
            }
        }
        return list;
    }

    public long getTotalDiffBytes() {
        return totalDiffBytes;
    }

    public int getModifiedSectorsCount() {
        return modifiedSectorsCount;
    }

    public boolean isIdentical() {
        return scanned && sizeA == sizeB && totalDiffBytes == 0;
    }

    @Override
    public void close() {
        try {
            sourceA.close();
        } catch (IOException ignored) {}
        try {
            sourceB.close();
        } catch (IOException ignored) {}
    }
}
