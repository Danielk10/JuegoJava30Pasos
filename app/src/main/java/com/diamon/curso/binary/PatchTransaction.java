package com.diamon.curso.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Gestor transaccional de modificaciones de bytes en volcados binarios (dumps)
 * con soporte para historial Deshacer/Rehacer (Undo/Redo) y mapeo reactivo para UI.
 */
public class PatchTransaction {

    public interface OnTransactionChangeListener {
        void onTransactionChanged(PatchTransaction transaction);
    }

    public static class BytePatch {
        private final long offset;
        private final byte originalValue;
        private final byte newValue;

        public BytePatch(long offset, byte originalValue, byte newValue) {
            this.offset = offset;
            this.originalValue = originalValue;
            this.newValue = newValue;
        }

        public long getOffset() {
            return offset;
        }

        public byte getOriginalValue() {
            return originalValue;
        }

        public byte getNewValue() {
            return newValue;
        }

        @Override
        public String toString() {
            return String.format("BytePatch{offset=0x%08X, orig=0x%02X, new=0x%02X}",
                    offset, originalValue & 0xFF, newValue & 0xFF);
        }
    }

    private static class PatchStep {
        final long offset;
        final byte prevVal;
        final byte nextVal;
        final byte initialOriginalVal;

        PatchStep(long offset, byte prevVal, byte nextVal, byte initialOriginalVal) {
            this.offset = offset;
            this.prevVal = prevVal;
            this.nextVal = nextVal;
            this.initialOriginalVal = initialOriginalVal;
        }
    }

    private static class PatchAction {
        final List<PatchStep> steps = new ArrayList<>();
        final String description;

        PatchAction(String description) {
            this.description = description != null ? description : "Byte modification";
        }
    }

    private final Map<Long, BytePatch> activePatches = new LinkedHashMap<>();
    private final Deque<PatchAction> undoStack = new ArrayDeque<>();
    private final Deque<PatchAction> redoStack = new ArrayDeque<>();
    private final List<OnTransactionChangeListener> listeners = new CopyOnWriteArrayList<>();

    public PatchTransaction() {
    }

    public synchronized void addListener(OnTransactionChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public synchronized void removeListener(OnTransactionChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (OnTransactionChangeListener listener : listeners) {
            try {
                listener.onTransactionChanged(this);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Modifica un byte individual en un offset especificado.
     */
    public synchronized boolean modifyByte(long offset, byte originalByteInSource, byte newByte) {
        return modifyByte(offset, originalByteInSource, newByte, "Modificar byte");
    }

    public synchronized boolean modifyByte(long offset, byte originalByteInSource, byte newByte, String description) {
        byte initialOriginal;
        byte prevVal;

        BytePatch existing = activePatches.get(offset);
        if (existing != null) {
            initialOriginal = existing.getOriginalValue();
            prevVal = existing.getNewValue();
        } else {
            initialOriginal = originalByteInSource;
            prevVal = originalByteInSource;
        }

        if (prevVal == newByte) {
            return false; // No hay cambio
        }

        PatchAction action = new PatchAction(description);
        action.steps.add(new PatchStep(offset, prevVal, newByte, initialOriginal));
        undoStack.push(action);
        redoStack.clear();

        if (newByte == initialOriginal) {
            activePatches.remove(offset);
        } else {
            activePatches.put(offset, new BytePatch(offset, initialOriginal, newByte));
        }

        notifyListeners();
        return true;
    }

    /**
     * Modifica un bloque de bytes de forma atómica.
     */
    public synchronized boolean modifyBytes(long startOffset, byte[] originalBytes, byte[] newBytes, String description) {
        if (newBytes == null || newBytes.length == 0) {
            return false;
        }

        PatchAction action = new PatchAction(description);
        boolean anyChanged = false;

        for (int i = 0; i < newBytes.length; i++) {
            long off = startOffset + i;
            byte orig = (originalBytes != null && i < originalBytes.length) ? originalBytes[i] : (byte) 0xFF;
            byte nByte = newBytes[i];

            byte initialOriginal;
            byte prevVal;

            BytePatch existing = activePatches.get(off);
            if (existing != null) {
                initialOriginal = existing.getOriginalValue();
                prevVal = existing.getNewValue();
            } else {
                initialOriginal = orig;
                prevVal = orig;
            }

            if (prevVal != nByte) {
                anyChanged = true;
                action.steps.add(new PatchStep(off, prevVal, nByte, initialOriginal));

                if (nByte == initialOriginal) {
                    activePatches.remove(off);
                } else {
                    activePatches.put(off, new BytePatch(off, initialOriginal, nByte));
                }
            }
        }

        if (!anyChanged) {
            return false;
        }

        undoStack.push(action);
        redoStack.clear();
        notifyListeners();
        return true;
    }

    /**
     * Deshace la última acción.
     */
    public synchronized boolean undo() {
        if (undoStack.isEmpty()) {
            return false;
        }

        PatchAction action = undoStack.pop();
        for (int i = action.steps.size() - 1; i >= 0; i--) {
            PatchStep step = action.steps.get(i);
            if (step.prevVal == step.initialOriginalVal) {
                activePatches.remove(step.offset);
            } else {
                activePatches.put(step.offset, new BytePatch(step.offset, step.initialOriginalVal, step.prevVal));
            }
        }

        redoStack.push(action);
        notifyListeners();
        return true;
    }

    /**
     * Rehace la última acción deshecha.
     */
    public synchronized boolean redo() {
        if (redoStack.isEmpty()) {
            return false;
        }

        PatchAction action = redoStack.pop();
        for (PatchStep step : action.steps) {
            if (step.nextVal == step.initialOriginalVal) {
                activePatches.remove(step.offset);
            } else {
                activePatches.put(step.offset, new BytePatch(step.offset, step.initialOriginalVal, step.nextVal));
            }
        }

        undoStack.push(action);
        notifyListeners();
        return true;
    }

    public synchronized boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public synchronized boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public synchronized boolean isModified(long offset) {
        return activePatches.containsKey(offset);
    }

    public synchronized byte getByte(long offset, byte fallback) {
        BytePatch patch = activePatches.get(offset);
        return patch != null ? patch.getNewValue() : fallback;
    }

    public synchronized int getModifiedCount() {
        return activePatches.size();
    }

    public synchronized Map<Long, BytePatch> getModifiedBytes() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(activePatches));
    }

    public synchronized Set<Long> getModifiedOffsets() {
        return Collections.unmodifiableSet(activePatches.keySet());
    }

    public synchronized void clear() {
        activePatches.clear();
        undoStack.clear();
        redoStack.clear();
        notifyListeners();
    }

    /**
     * Aplica los parches acumulados sobre un arreglo de bytes en memoria.
     */
    public synchronized byte[] applyTo(byte[] source) {
        if (source == null) return null;
        byte[] copy = source.clone();
        for (BytePatch patch : activePatches.values()) {
            long off = patch.getOffset();
            if (off >= 0 && off < copy.length) {
                copy[(int) off] = patch.getNewValue();
            }
        }
        return copy;
    }

    /**
     * Aplica los parches modificados sobre un archivo fuente y guarda en archivo destino.
     */
    public synchronized void applyToFile(File sourceFile, File targetFile) throws IOException {
        if (!sourceFile.exists()) {
            throw new IOException("Archivo fuente no existe: " + sourceFile.getAbsolutePath());
        }

        if (sourceFile.equals(targetFile)) {
            // Modificación in-place
            try (RandomAccessFile raf = new RandomAccessFile(targetFile, "rw")) {
                for (BytePatch patch : activePatches.values()) {
                    long off = patch.getOffset();
                    if (off < raf.length()) {
                        raf.seek(off);
                        raf.writeByte(patch.getNewValue() & 0xFF);
                    }
                }
            }
            return;
        }

        // Copiar y parchear
        try (FileInputStream in = new FileInputStream(sourceFile);
             FileOutputStream out = new FileOutputStream(targetFile)) {
            byte[] buf = new byte[65536];
            int n;
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n);
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(targetFile, "rw")) {
            for (BytePatch patch : activePatches.values()) {
                long off = patch.getOffset();
                if (off < raf.length()) {
                    raf.seek(off);
                    raf.writeByte(patch.getNewValue() & 0xFF);
                }
            }
        }
    }
}
