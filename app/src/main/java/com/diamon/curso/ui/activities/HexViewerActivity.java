package com.diamon.curso.ui.activities;

import com.diamon.curso.R;
import com.diamon.curso.billing.BillingManager;
import com.diamon.curso.binary.GbeMacPatcher;
import com.diamon.curso.binary.IntegrityManager;
import com.diamon.curso.binary.MsdmExtractor;
import com.diamon.curso.binary.PatchTransaction;
import com.diamon.curso.managers.ProAccessDialog;
import com.diamon.curso.managers.ProPassManager;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamon.curso.utils.FileManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Locale;

public class HexViewerActivity extends AppCompatActivity {

    private TextView tvHexSummary;
    private RecyclerView recyclerHex;
    private HexAdapter hexAdapter;
    private HexDataSource currentDataSource;
    private File currentLoadedFile;

    private final PatchTransaction patchTransaction = new PatchTransaction();
    private ProPassManager proPassManager;
    private BillingManager billingManager;

    private final ActivityResultLauncher<Intent> exportFileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        exportDumpToUri(uri);
                    }
                }
            });

    public interface HexDataSource extends java.io.Closeable {
        long size();
        int read(long offset, byte[] dest, int destOffset, int length);
    }

    public static class MappedFileSource implements HexDataSource {
        private final RandomAccessFile raf;
        private final FileChannel channel;
        private final MappedByteBuffer mappedBuffer;
        private final long size;

        public MappedFileSource(File file) throws Exception {
            this.raf = new RandomAccessFile(file, "r");
            this.channel = raf.getChannel();
            this.size = channel.size();
            this.mappedBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
        }

        @Override
        public long size() {
            return size;
        }

        @Override
        public int read(long offset, byte[] dest, int destOffset, int length) {
            if (offset >= size) return 0;
            int toRead = (int) Math.min((long) length, size - offset);
            synchronized (mappedBuffer) {
                mappedBuffer.position((int) offset);
                mappedBuffer.get(dest, destOffset, toRead);
            }
            return toRead;
        }

        @Override
        public void close() {
            try {
                channel.close();
            } catch (Exception ignored) {}
            try {
                raf.close();
            } catch (Exception ignored) {}
        }
    }

    public static class ByteArraySource implements HexDataSource {
        private final byte[] data;

        public ByteArraySource(byte[] data) {
            this.data = data != null ? data : new byte[0];
        }

        @Override
        public long size() {
            return data.length;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hex_viewer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.str_visor_hexadecim);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        proPassManager = new ProPassManager(this);
        proPassManager.setOnPassRestoredListener(expireTime -> runOnUiThread(this::invalidateOptionsMenu));
        billingManager = new BillingManager(this, null);

        tvHexSummary = findViewById(R.id.tvHexSummary);
        recyclerHex = findViewById(R.id.recyclerHex);
        recyclerHex.setLayoutManager(new LinearLayoutManager(this));

        patchTransaction.addListener(tx -> runOnUiThread(this::invalidateOptionsMenu));

        loadDataFromIntent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentDataSource != null) {
            try {
                currentDataSource.close();
            } catch (Exception ignored) {}
            currentDataSource = null;
        }
        if (billingManager != null) {
            billingManager.onDestroy();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hex_viewer, menu);
        MenuItem undoItem = menu.findItem(R.id.action_undo);
        if (undoItem != null) {
            undoItem.setEnabled(patchTransaction.canUndo());
        }
        MenuItem saveItem = menu.findItem(R.id.action_save_and_flash);
        if (saveItem != null) {
            int mods = patchTransaction.getModifiedCount();
            saveItem.setTitle(mods > 0 ? getString(R.string.str_menu_save_mods, mods) : getString(R.string.menu_hex_save_and_flash));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        } else if (id == R.id.action_undo) {
            if (patchTransaction.undo()) {
                if (hexAdapter != null) hexAdapter.notifyDataSetChanged();
                invalidateOptionsMenu();
                Toast.makeText(this, getString(R.string.str_toast_mod_undone, patchTransaction.getModifiedCount()), Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.action_patch_gbe) {
            handleProTool(this::showGbePatcherDialog);
            return true;
        } else if (id == R.id.action_extract_msdm) {
            handleProTool(this::showMsdmDialog);
            return true;
        } else if (id == R.id.action_verify_integrity) {
            handleProTool(this::showIntegrityDialog);
            return true;
        } else if (id == R.id.action_export_storage) {
            promptExportToStorage();
            return true;
        } else if (id == R.id.action_save_and_flash) {
            saveAndFlash();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleProTool(Runnable onGranted) {
        if (!proPassManager.isProActive(billingManager)) {
            ProAccessDialog.show(this, null, billingManager, proPassManager, onGranted);
        } else {
            onGranted.run();
        }
    }

    private void loadDataFromIntent() {
        Uri fileUri = getIntent().getData();
        String fileName = "bios.bin";

        String biosSource = getSharedPreferences("flashrom_prefs", MODE_PRIVATE)
                .getString("bios_source", null);

        try {
            if (fileUri != null) {
                fileName = fileUri.getLastPathSegment();
                biosSource = getString(R.string.str_external_file, fileName);
                byte[] data = readUriToBytes(fileUri);

                File cachedFile = new File(getCacheDir(), "hex_edit_" + (fileName != null ? fileName : "bios.bin"));
                try (FileOutputStream fos = new FileOutputStream(cachedFile)) {
                    fos.write(data);
                }
                currentLoadedFile = cachedFile;

                if (fileName != null && fileName.toLowerCase().endsWith(".hex")) {
                    parseIntelHex(data, biosSource);
                } else {
                    displayBinary(new ByteArraySource(data), fileName, biosSource);
                }
            } else {
                String trackedFile = getSharedPreferences("flashrom_prefs", MODE_PRIVATE)
                        .getString("last_read_file", "bios.bin");
                File dataFile = new File(getFilesDir(), trackedFile);
                if (!dataFile.exists()) {
                    dataFile = new File(getFilesDir(), "bios.bin");
                }
                if (!dataFile.exists()) {
                    tvHexSummary.setText(R.string.str_err_no_data_visualize);
                    return;
                }
                currentLoadedFile = dataFile;
                fileName = dataFile.getName();

                if (fileName.toLowerCase().endsWith(".hex")) {
                    byte[] data = java.nio.file.Files.readAllBytes(dataFile.toPath());
                    parseIntelHex(data, biosSource);
                } else {
                    MappedFileSource mmapSource = new MappedFileSource(dataFile);
                    displayBinary(mmapSource, fileName, biosSource);
                }
            }
        } catch (Exception e) {
            tvHexSummary.setText(getString(R.string.str_err_load_data, e.getMessage()));
        }
    }

    private byte[] readUriToBytes(Uri uri) throws Exception {
        try (InputStream is = getContentResolver().openInputStream(uri)) {
            if (is == null)
                throw new IllegalStateException(getString(R.string.str_err_open_file));
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        }
    }

    private void displayBinary(HexDataSource source, String name, String biosSource) {
        if (currentDataSource != null && currentDataSource != source) {
            try {
                currentDataSource.close();
            } catch (Exception ignored) {}
        }
        currentDataSource = source;

        String summary = getString(R.string.str_binary_summary, name, source.size());
        if (biosSource != null) {
            summary += getString(R.string.str_source_label, biosSource);
        }
        tvHexSummary.setText(summary);
        hexAdapter = new HexAdapter(source, 0, patchTransaction, this::onByteClicked);
        recyclerHex.setAdapter(hexAdapter);
    }

    private void parseIntelHex(byte[] hexData, String biosSource) {
        try {
            String content = new String(hexData);
            String[] lines = content.split("\\r?\\n");

            long minAddr = Long.MAX_VALUE;
            long maxAddr = 0;
            int upperAddress = 0;

            for (String line : lines) {
                line = line.trim();
                if (!line.startsWith(":") || line.length() < 11)
                    continue;
                int byteCount = Integer.parseInt(line.substring(1, 3), 16);
                int address = Integer.parseInt(line.substring(3, 7), 16);
                int type = Integer.parseInt(line.substring(7, 9), 16);
                if (type == 0x00) {
                    long absolute = (long) upperAddress + address;
                    minAddr = Math.min(minAddr, absolute);
                    maxAddr = Math.max(maxAddr, absolute + byteCount);
                } else if (type == 0x04 && line.length() >= 15) {
                    upperAddress = Integer.parseInt(line.substring(9, 13), 16) << 16;
                } else if (type == 0x02 && line.length() >= 15) {
                    upperAddress = Integer.parseInt(line.substring(9, 13), 16) << 4;
                } else if (type == 0x01) {
                    break;
                }
            }

            if (minAddr == Long.MAX_VALUE) {
                tvHexSummary.setText(R.string.str_err_hex_no_records);
                return;
            }

            long bufferSize = maxAddr - minAddr;
            if (bufferSize > 32L * 1024 * 1024) {
                tvHexSummary.setText(getString(R.string.str_err_hex_too_large, String.valueOf(bufferSize / 1024 / 1024)));
                return;
            }

            byte[] binBuffer = new byte[(int) bufferSize];
            java.util.Arrays.fill(binBuffer, (byte) 0xFF);

            upperAddress = 0;
            for (String line : lines) {
                line = line.trim();
                if (!line.startsWith(":") || line.length() < 11)
                    continue;
                int byteCount = Integer.parseInt(line.substring(1, 3), 16);
                int address = Integer.parseInt(line.substring(3, 7), 16);
                int type = Integer.parseInt(line.substring(7, 9), 16);
                if (type == 0x00) {
                    long absolute = (long) upperAddress + address;
                    int offset = (int) (absolute - minAddr);
                    for (int i = 0; i < byteCount; i++) {
                        if (offset + i < binBuffer.length) {
                            binBuffer[offset + i] = (byte) Integer.parseInt(
                                     line.substring(9 + i * 2, 11 + i * 2), 16);
                        }
                    }
                } else if (type == 0x04 && line.length() >= 15) {
                    upperAddress = Integer.parseInt(line.substring(9, 13), 16) << 16;
                } else if (type == 0x02 && line.length() >= 15) {
                    upperAddress = Integer.parseInt(line.substring(9, 13), 16) << 4;
                } else if (type == 0x01) {
                    break;
                }
            }

            String summary = getString(R.string.str_hex_detected, (int) minAddr, (int) (maxAddr - 1));
            if (biosSource != null) {
                summary += getString(R.string.str_source_label, biosSource);
            }
            tvHexSummary.setText(summary);
            ByteArraySource hexSource = new ByteArraySource(binBuffer);
            if (currentDataSource != null) {
                try { currentDataSource.close(); } catch (Exception ignored) {}
            }
            currentDataSource = hexSource;
            hexAdapter = new HexAdapter(hexSource, (int) minAddr, patchTransaction, this::onByteClicked);
            recyclerHex.setAdapter(hexAdapter);

        } catch (Exception e) {
            tvHexSummary.setText(getString(R.string.str_err_parse_hex, e.getMessage()));
        }
    }

    private byte[] getAllDumpBytes() {
        if (currentDataSource == null) return new byte[0];
        int size = (int) Math.min(currentDataSource.size(), 64 * 1024 * 1024);
        byte[] dump = new byte[size];
        currentDataSource.read(0, dump, 0, size);
        return patchTransaction.applyTo(dump);
    }

    // =========================================================================
    // PILAR 2: DIÁLOGO EDITOR HEXADECIMAL IN-PLACE CON DESGLOSE Y UNDO/REDO
    // =========================================================================
    private void onByteClicked(final long offset) {
        if (!proPassManager.isProActive(billingManager)) {
            ProAccessDialog.show(this, null, billingManager, proPassManager, () -> onByteClicked(offset));
            return;
        }

        showByteEditorDialog(offset);
    }

    private void showByteEditorDialog(final long offset) {
        if (currentDataSource == null || offset < 0 || offset >= currentDataSource.size()) return;

        final byte[] quad = new byte[4];
        currentDataSource.read(offset, quad, 0, 4);
        final byte origByte = quad[0];

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(36, 24, 36, 24);
        layout.setBackgroundColor(0xFF1E1E2C);

        // 1. TÍTULO CON OFFSET HEX Y DECIMAL
        TextView tvTitle = new TextView(this);
        tvTitle.setText(getString(R.string.str_edit_byte_title, offset, offset));
        tvTitle.setTextSize(18);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setTextColor(0xFF00B0FF);
        layout.addView(tvTitle);

        // 2. DETALLE DE VALOR ACTUAL, BINARIO Y ASCII
        final TextView tvValues = new TextView(this);
        tvValues.setTextSize(14);
        tvValues.setTypeface(Typeface.MONOSPACE);
        tvValues.setTextColor(0xFFE0E0E0);
        tvValues.setPadding(0, 16, 0, 16);
        layout.addView(tvValues);

        // 3. DESGLOSE LITTLE-ENDIAN / BIG-ENDIAN (16 y 32 bits)
        final TextView tvEndian = new TextView(this);
        tvEndian.setTextSize(13);
        tvEndian.setTypeface(Typeface.MONOSPACE);
        tvEndian.setTextColor(0xFFB0BEC5);
        tvEndian.setPadding(0, 0, 0, 16);
        layout.addView(tvEndian);

        // Función de actualización reactiva de textos
        final Runnable updateDisplays = () -> {
            byte curByte = patchTransaction.getByte(offset, origByte);
            String binStr = String.format("%8s", Integer.toBinaryString(curByte & 0xFF)).replace(' ', '0');
            char asciiChar = (curByte >= 32 && curByte <= 126) ? (char) curByte : '.';

            StringBuilder sbVal = new StringBuilder();
            sbVal.append(getString(R.string.str_byte_original, origByte & 0xFF, origByte & 0xFF));
            sbVal.append(getString(R.string.str_byte_current, curByte & 0xFF, curByte & 0xFF, binStr, asciiChar));
            tvValues.setText(sbVal.toString());

            // Lectura de 4 bytes con parches aplicados para desglose de endianness
            byte b0 = curByte;
            byte b1 = (offset + 1 < currentDataSource.size()) ? patchTransaction.getByte(offset + 1, quad[1]) : 0;
            byte b2 = (offset + 2 < currentDataSource.size()) ? patchTransaction.getByte(offset + 2, quad[2]) : 0;
            byte b3 = (offset + 3 < currentDataSource.size()) ? patchTransaction.getByte(offset + 3, quad[3]) : 0;

            int le16 = (b0 & 0xFF) | ((b1 & 0xFF) << 8);
            int be16 = ((b0 & 0xFF) << 8) | (b1 & 0xFF);
            int le32 = (b0 & 0xFF) | ((b1 & 0xFF) << 8) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 24);
            int be32 = ((b0 & 0xFF) << 24) | ((b1 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | (b3 & 0xFF);

            StringBuilder sbEnd = new StringBuilder();
            sbEnd.append(getString(R.string.str_byte_word16_header));
            sbEnd.append(getString(R.string.str_byte_endian_val, le16, le16, be16, be16));
            sbEnd.append(getString(R.string.str_byte_dword32_header));
            sbEnd.append(getString(R.string.str_byte_dword_endian_val, le32, le32, be32, be32));
            tvEndian.setText(sbEnd.toString());
        };

        updateDisplays.run();

        // 4. CAMPO EDITTEXT PARA NUEVO VALOR EN HEXADECIMAL
        TextView tvInputLabel = new TextView(this);
        tvInputLabel.setText(R.string.str_new_hex_value_label);
        tvInputLabel.setTextColor(0xFFFFB300);
        tvInputLabel.setTextSize(14);
        layout.addView(tvInputLabel);

        final EditText etNewHex = new EditText(this);
        etNewHex.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        etNewHex.setTypeface(Typeface.MONOSPACE);
        etNewHex.setTextSize(18);
        etNewHex.setTextColor(0xFFFFFFFF);
        etNewHex.setBackgroundColor(0xFF2B2B3C);
        etNewHex.setPadding(24, 16, 24, 16);
        etNewHex.setText(String.format(Locale.US, "%02X", patchTransaction.getByte(offset, origByte) & 0xFF));
        layout.addView(etNewHex);

        // 5. FILA DE BOTONES: APLICAR, DESHACER, REHACER
        LinearLayout btnRow = new LinearLayout(this);
        btnRow.setOrientation(LinearLayout.HORIZONTAL);
        btnRow.setPadding(0, 24, 0, 8);

        Button btnApply = new Button(this);
        btnApply.setText(R.string.str_btn_apply);
        btnApply.setBackgroundColor(0xFF00C853);
        btnApply.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        p1.setMargins(0, 0, 8, 0);
        btnRow.addView(btnApply, p1);

        final Button btnUndo = new Button(this);
        btnUndo.setText(R.string.str_btn_undo);
        btnUndo.setBackgroundColor(0xFF455A64);
        btnUndo.setTextColor(Color.WHITE);
        btnUndo.setEnabled(patchTransaction.canUndo());
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        p2.setMargins(4, 0, 4, 0);
        btnRow.addView(btnUndo, p2);

        final Button btnRedo = new Button(this);
        btnRedo.setText(R.string.str_btn_redo);
        btnRedo.setBackgroundColor(0xFF455A64);
        btnRedo.setTextColor(Color.WHITE);
        btnRedo.setEnabled(patchTransaction.canRedo());
        LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        p3.setMargins(8, 0, 0, 0);
        btnRow.addView(btnRedo, p3);

        layout.addView(btnRow);
        scrollView.addView(layout);
        builder.setView(scrollView);
        builder.setNegativeButton(R.string.str_close, null);

        final AlertDialog dialog = builder.create();

        btnApply.setOnClickListener(v -> {
            String hexStr = etNewHex.getText().toString().trim();
            if (hexStr.isEmpty()) return;
            try {
                int val = Integer.parseInt(hexStr, 16);
                byte newByte = (byte) (val & 0xFF);
                patchTransaction.modifyByte(offset, origByte, newByte);
                updateDisplays.run();
                btnUndo.setEnabled(patchTransaction.canUndo());
                btnRedo.setEnabled(patchTransaction.canRedo());
                if (hexAdapter != null) hexAdapter.notifyItemChanged((int) (offset / 16));
                invalidateOptionsMenu();
                Toast.makeText(this, R.string.str_byte_modified_success, Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(this, getString(R.string.str_invalid_hex, hexStr), Toast.LENGTH_SHORT).show();
            }
        });

        btnUndo.setOnClickListener(v -> {
            if (patchTransaction.undo()) {
                updateDisplays.run();
                etNewHex.setText(String.format(Locale.US, "%02X", patchTransaction.getByte(offset, origByte) & 0xFF));
                btnUndo.setEnabled(patchTransaction.canUndo());
                btnRedo.setEnabled(patchTransaction.canRedo());
                if (hexAdapter != null) hexAdapter.notifyDataSetChanged();
                invalidateOptionsMenu();
            }
        });

        btnRedo.setOnClickListener(v -> {
            if (patchTransaction.redo()) {
                updateDisplays.run();
                etNewHex.setText(String.format(Locale.US, "%02X", patchTransaction.getByte(offset, origByte) & 0xFF));
                btnUndo.setEnabled(patchTransaction.canUndo());
                btnRedo.setEnabled(patchTransaction.canRedo());
                if (hexAdapter != null) hexAdapter.notifyDataSetChanged();
                invalidateOptionsMenu();
            }
        });

        dialog.show();
    }

    // =========================================================================
    // HERRAMIENTAS PRO: INYECTAR MAC INTEL GbE CON CHECKSUM 0xBABA
    // =========================================================================
    private void showGbePatcherDialog() {
        final byte[] dump = getAllDumpBytes();
        if (dump.length == 0) {
            Toast.makeText(this, R.string.str_no_dump_loaded, Toast.LENGTH_SHORT).show();
            return;
        }

        final GbeMacPatcher.GbeRegionInfo region = GbeMacPatcher.detectGbeRegion(dump);
        final long gbeOffset = (region != null) ? region.getOffset() : 0x1000L;
        final String currentMac = (region != null) ? region.getMacAddress() : "00:1B:21:88:99:AA";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.str_dialog_gbe_title);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(36, 20, 36, 16);
        layout.setBackgroundColor(0xFF1E1E2C);

        TextView tvInfo = new TextView(this);
        tvInfo.setTextColor(0xFFE0E0E0);
        tvInfo.setTextSize(13);
        tvInfo.setTypeface(Typeface.MONOSPACE);
        String checksumStatus = (region != null && region.isChecksumValid())
                ? getString(R.string.str_gbe_checksum_valid)
                : getString(R.string.str_gbe_checksum_invalid);
        String backupStatus = (region != null && region.hasBackupRegion())
                ? getString(R.string.str_gbe_backup_present)
                : getString(R.string.str_gbe_backup_none);
        String info = getString(R.string.str_gbe_info_format, gbeOffset, currentMac, checksumStatus, backupStatus);
        tvInfo.setText(info);
        layout.addView(tvInfo);

        final EditText etMac = new EditText(this);
        etMac.setText(currentMac);
        etMac.setTextColor(0xFF00E5FF);
        etMac.setTypeface(Typeface.MONOSPACE);
        etMac.setBackgroundColor(0xFF2B2B3C);
        etMac.setPadding(24, 16, 24, 16);
        layout.addView(etMac);

        builder.setView(layout);
        builder.setPositiveButton(R.string.str_gbe_btn_inject, (dialog, which) -> {
            String newMacStr = etMac.getText().toString().trim();
            if (!GbeMacPatcher.isValidMacString(newMacStr)) {
                Toast.makeText(this, R.string.str_invalid_mac_format, Toast.LENGTH_LONG).show();
                return;
            }

            int nvmLen = Math.min(GbeMacPatcher.GBE_NVM_SIZE * 2, dump.length - (int) gbeOffset);
            byte[] originalBlock = new byte[nvmLen];
            System.arraycopy(dump, (int) gbeOffset, originalBlock, 0, nvmLen);

            byte[] patchedDump = dump.clone();
            try {
                GbeMacPatcher.patchMac(patchedDump, (int) gbeOffset, newMacStr, true);
                byte[] newBlock = new byte[nvmLen];
                System.arraycopy(patchedDump, (int) gbeOffset, newBlock, 0, nvmLen);

                patchTransaction.modifyBytes(gbeOffset, originalBlock, newBlock, getString(R.string.str_gbe_patch_desc));
                if (hexAdapter != null) hexAdapter.notifyDataSetChanged();
                recyclerHex.scrollToPosition((int) (gbeOffset / 16));
                invalidateOptionsMenu();

                Toast.makeText(this, R.string.str_gbe_inject_success, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.str_gbe_inject_error, e.getMessage()), Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.str_cancelar, null);
        builder.show();
    }

    // =========================================================================
    // HERRAMIENTAS PRO: EXTRACTOR DE CLAVE WINDOWS OEM MSDM
    // =========================================================================
    private void showMsdmDialog() {
        byte[] dump = getAllDumpBytes();
        MsdmExtractor.MsdmResult result = MsdmExtractor.extractKey(dump);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.str_dialog_msdm_title);

        if (!result.isFound()) {
            builder.setMessage(R.string.str_msdm_not_found);
            builder.setPositiveButton(R.string.str_ok, null);
            builder.show();
            return;
        }

        final String productKey = result.getProductKey();
        final long tableOffset = result.getTableOffset();

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(36, 20, 36, 16);
        layout.setBackgroundColor(0xFF1E1E2C);

        TextView tvKey = new TextView(this);
        tvKey.setText(productKey);
        tvKey.setTextColor(0xFF00E676);
        tvKey.setTextSize(20);
        tvKey.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        tvKey.setGravity(Gravity.CENTER);
        tvKey.setPadding(0, 16, 0, 16);
        layout.addView(tvKey);

        TextView tvDetails = new TextView(this);
        tvDetails.setTextColor(0xFFB0BEC5);
        tvDetails.setTextSize(13);
        tvDetails.setTypeface(Typeface.MONOSPACE);
        String checksumValidStr = result.isChecksumValid() ? getString(R.string.str_status_valid) : getString(R.string.str_status_invalid);
        String details = getString(R.string.str_msdm_details_format,
                result.getOemId(), result.getOemTableId(),
                tableOffset,
                checksumValidStr);
        tvDetails.setText(details);
        layout.addView(tvDetails);

        builder.setView(layout);
        builder.setPositiveButton(R.string.str_btn_copy_key, (dialog, which) -> {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                cm.setPrimaryClip(ClipData.newPlainText("Windows OEM Key", productKey));
                Toast.makeText(this, getString(R.string.str_msdm_copied_toast, productKey), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton(R.string.str_btn_goto_offset, (dialog, which) -> {
            if (recyclerHex != null) {
                recyclerHex.scrollToPosition((int) (tableOffset / 16));
            }
        });
        builder.setNegativeButton(R.string.str_close, null);
        builder.show();
    }

    // =========================================================================
    // HERRAMIENTAS PRO: VERIFICACIÓN CRIPTOGRÁFICA DE INTEGRIDAD Y SIDECARS
    // =========================================================================
    private void showIntegrityDialog() {
        final byte[] dump = getAllDumpBytes();
        if (dump.length == 0) {
            Toast.makeText(this, R.string.str_integrity_no_data, Toast.LENGTH_SHORT).show();
            return;
        }

        final IntegrityManager.HashResult hashes = IntegrityManager.computeHashes(dump);
        final String clipText = IntegrityManager.getClipboardText(this);
        final IntegrityManager.ClipboardMatchResult matchResult = IntegrityManager.checkClipboardMatch(clipText, hashes);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.str_dialog_integrity_title);

        ScrollView sv = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(36, 20, 36, 16);
        layout.setBackgroundColor(0xFF1E1E2C);

        // Insignia de coincidencia con portapapeles
        TextView tvBadge = new TextView(this);
        tvBadge.setTextSize(14);
        tvBadge.setTypeface(null, Typeface.BOLD);
        tvBadge.setPadding(16, 12, 16, 12);
        if (matchResult.isMatched()) {
            tvBadge.setBackgroundColor(0xFF1B5E20);
            tvBadge.setTextColor(0xFF00E676);
            tvBadge.setText(getString(R.string.str_integrity_match_success, matchResult.getMatchedAlgorithm()));
        } else if (matchResult.hasClipboardHash()) {
            tvBadge.setBackgroundColor(0xFFB71C1C);
            tvBadge.setTextColor(0xFFFF5252);
            tvBadge.setText(R.string.str_integrity_mismatch_warning);
        } else {
            tvBadge.setBackgroundColor(0xFF263238);
            tvBadge.setTextColor(0xFF90A4AE);
            tvBadge.setText(R.string.str_integrity_no_clipboard_hash);
        }
        layout.addView(tvBadge);

        TextView tvHashes = new TextView(this);
        tvHashes.setTextSize(12);
        tvHashes.setTypeface(Typeface.MONOSPACE);
        tvHashes.setTextColor(0xFFECEFF1);
        tvHashes.setPadding(0, 16, 0, 16);
        tvHashes.setText(hashes.formatSummary());
        layout.addView(tvHashes);

        sv.addView(layout);
        builder.setView(sv);

        builder.setPositiveButton(R.string.str_btn_copy_sha256, (d, w) -> {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                cm.setPrimaryClip(ClipData.newPlainText("SHA256 Hash", hashes.getSha256()));
                Toast.makeText(this, R.string.str_toast_sha256_copied, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton(R.string.str_btn_generate_sidecars, (d, w) -> {
            try {
                File targetDir = getFilesDir();
                File binFile = (currentLoadedFile != null) ? currentLoadedFile : new File(targetDir, "bios.bin");
                IntegrityManager.generateSidecars(binFile, hashes);
                FileManager.exportToDownloads(this, binFile.getName() + ".sha256");
                FileManager.exportToDownloads(this, binFile.getName() + ".md5");
                Toast.makeText(this, R.string.str_toast_sidecars_success, Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Toast.makeText(this, getString(R.string.str_toast_sidecars_error, ex.getMessage()), Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton(R.string.str_close, null);
        builder.show();
    }

    // =========================================================================
    // GUARDAR Y GRABAR EN CHIP SPI
    // =========================================================================
    private void saveAndFlash() {
        if (patchTransaction.getModifiedCount() == 0) {
            Toast.makeText(this, R.string.str_no_pending_modifications, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File biosFile = (currentLoadedFile != null && currentLoadedFile.canWrite())
                    ? currentLoadedFile
                    : new File(getFilesDir(), "bios.bin");

            byte[] fullDump = getAllDumpBytes();
            try (FileOutputStream fos = new FileOutputStream(biosFile)) {
                fos.write(fullDump);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.str_dialog_save_and_flash_title);
            builder.setMessage(getString(R.string.str_dialog_save_and_flash_msg,
                    patchTransaction.getModifiedCount(), biosFile.getName()));

            builder.setPositiveButton(R.string.str_btn_write_to_chip, (dialog, which) -> {
                Intent result = new Intent();
                result.putExtra("trigger_flash", true);
                setResult(RESULT_OK, result);
                finish();
            });

            builder.setNeutralButton(R.string.str_btn_export_storage, (dialog, which) -> {
                promptExportToStorage();
            });

            builder.setNegativeButton(R.string.str_btn_save_only, (dialog, which) -> {
                Toast.makeText(this, getString(R.string.str_toast_changes_saved, biosFile.getName()), Toast.LENGTH_SHORT).show();
            });

            builder.show();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.str_toast_save_error, e.getMessage()), Toast.LENGTH_LONG).show();
        }
    }

    private void promptExportToStorage() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/octet-stream");
        String exportName = (currentLoadedFile != null) ? currentLoadedFile.getName() : "bios.bin";
        if ("bios.bin".equals(exportName) && patchTransaction.getModifiedCount() > 0) {
            exportName = "bios_modified.bin";
        }
        if (!exportName.contains(".")) {
            exportName += ".bin";
        }
        intent.putExtra(Intent.EXTRA_TITLE, exportName);
        exportFileLauncher.launch(intent);
    }

    private void exportDumpToUri(Uri uri) {
        try (OutputStream out = getContentResolver().openOutputStream(uri)) {
            if (out == null) {
                throw new IOException(getString(R.string.str_err_open_file));
            }
            byte[] fullDump = getAllDumpBytes();
            out.write(fullDump);
            Toast.makeText(this, R.string.str_export_success_toast, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.str_toast_save_error, e.getMessage()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    // =========================================================================
    // ADAPTER CON RESALTADO EN COLOR ÁMBAR (#FFB300) PARA CELDAS MODIFICADAS
    // =========================================================================
    interface OnByteClickListener {
        void onByteClick(long offset);
    }

    static class HexAdapter extends RecyclerView.Adapter<HexAdapter.HexViewHolder> {
        private final HexDataSource dataSource;
        private final long startAddress;
        private final PatchTransaction patchTransaction;
        private final OnByteClickListener byteClickListener;

        public HexAdapter(HexDataSource dataSource, long startAddress,
                          PatchTransaction patchTransaction, OnByteClickListener byteClickListener) {
            this.dataSource = dataSource;
            this.startAddress = startAddress;
            this.patchTransaction = patchTransaction;
            this.byteClickListener = byteClickListener;
        }

        @NonNull
        @Override
        public HexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hex_row, parent, false);
            return new HexViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HexViewHolder holder, int position) {
            long rowStart = (long) position * 16;
            long currentAddr = startAddress + rowStart;
            byte[] rowData = new byte[16];
            int length = dataSource.read(rowStart, rowData, 0, 16);

            SpannableStringBuilder hexSpannable = new SpannableStringBuilder();
            SpannableStringBuilder asciiSpannable = new SpannableStringBuilder();

            for (int i = 0; i < 16; i++) {
                if (i < length) {
                    long byteOffset = currentAddr + i;
                    byte origByte = rowData[i];
                    byte activeByte = patchTransaction != null ? patchTransaction.getByte(byteOffset, origByte) : origByte;
                    boolean isModified = patchTransaction != null && patchTransaction.isModified(byteOffset);

                    int startHexIdx = hexSpannable.length();
                    hexSpannable.append(String.format(Locale.US, "%02X ", activeByte & 0xFF));
                    if (isModified) {
                        hexSpannable.setSpan(new ForegroundColorSpan(0xFFFFB300), startHexIdx, startHexIdx + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        hexSpannable.setSpan(new StyleSpan(Typeface.BOLD), startHexIdx, startHexIdx + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        hexSpannable.setSpan(new BackgroundColorSpan(0x33FFB300), startHexIdx, startHexIdx + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    int startAsciiIdx = asciiSpannable.length();
                    char c = (activeByte >= 32 && activeByte <= 126) ? (char) activeByte : '.';
                    asciiSpannable.append(c);
                    if (isModified) {
                        asciiSpannable.setSpan(new ForegroundColorSpan(0xFFFFB300), startAsciiIdx, startAsciiIdx + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        asciiSpannable.setSpan(new StyleSpan(Typeface.BOLD), startAsciiIdx, startAsciiIdx + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else {
                    hexSpannable.append("   ");
                    asciiSpannable.append(" ");
                }
            }

            holder.tvAddress.setText(String.format(Locale.US, "%08X", currentAddr));
            holder.tvHex.setText(hexSpannable);
            holder.tvAscii.setText(asciiSpannable);

            // Detección de pulsación in-place en cada celda individual
            holder.tvHex.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP && byteClickListener != null) {
                    int charPos = holder.tvHex.getOffsetForPosition(event.getX(), event.getY());
                    int byteIdx = Math.min(15, Math.max(0, charPos / 3));
                    if (byteIdx < length) {
                        byteClickListener.onByteClick(currentAddr + byteIdx);
                    }
                }
                return true;
            });

            holder.tvAscii.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP && byteClickListener != null) {
                    int charPos = holder.tvAscii.getOffsetForPosition(event.getX(), event.getY());
                    if (charPos >= 0 && charPos < length) {
                        byteClickListener.onByteClick(currentAddr + charPos);
                    }
                }
                return true;
            });
        }

        @Override
        public int getItemCount() {
            return (int) Math.ceil((double) dataSource.size() / 16.0);
        }

        static class HexViewHolder extends RecyclerView.ViewHolder {
            TextView tvAddress, tvHex, tvAscii;

            HexViewHolder(View itemView) {
                super(itemView);
                tvAddress = itemView.findViewById(R.id.tvAddress);
                tvHex = itemView.findViewById(R.id.tvHex);
                tvAscii = itemView.findViewById(R.id.tvAscii);
            }
        }
    }
}
