package com.diamon.curso;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HexDiffActivity extends AppCompatActivity {

    private TextView tvDiffSummary, tvDiffStats;
    private RecyclerView recyclerDiff;
    private Button btnLoadFile1, btnLoadFile2;

    private File fileA = null;
    private File fileB = null;
    private RandomAccessFile rafA = null;
    private RandomAccessFile rafB = null;
    private String nameA = "Archivo A";
    private String nameB = "Archivo B";
    
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final ActivityResultLauncher<Intent> file1Launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        executor.execute(() -> {
                            try {
                                fileA = copyUriToTemp(uri, "temp_diff_a.bin");
                                nameA = getFileName(uri);
                                runOnUiThread(() -> {
                                    btnLoadFile1.setText("A: " + nameA);
                                    tryCompare();
                                });
                            } catch (Exception e) {
                                runOnUiThread(() -> tvDiffSummary.setText("Error cargando A: " + e.getMessage()));
                            }
                        });
                    }
                }
            });

    private final ActivityResultLauncher<Intent> file2Launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        executor.execute(() -> {
                            try {
                                fileB = copyUriToTemp(uri, "temp_diff_b.bin");
                                nameB = getFileName(uri);
                                runOnUiThread(() -> {
                                    btnLoadFile2.setText("B: " + nameB);
                                    tryCompare();
                                });
                            } catch (Exception e) {
                                runOnUiThread(() -> tvDiffSummary.setText("Error cargando B: " + e.getMessage()));
                            }
                        });
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hex_diff);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Comparar HEX (Diff)");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvDiffSummary = findViewById(R.id.tvDiffSummary);
        tvDiffStats = findViewById(R.id.tvDiffStats);
        recyclerDiff = findViewById(R.id.recyclerDiff);
        recyclerDiff.setLayoutManager(new LinearLayoutManager(this));
        btnLoadFile1 = findViewById(R.id.btnLoadFile1);
        btnLoadFile2 = findViewById(R.id.btnLoadFile2);

        // Pre-cargar bios.bin automáticamente si existe
        File biosFile = new File(getFilesDir(), "bios.bin");
        if (biosFile.exists()) {
            fileA = biosFile;
            nameA = "bios.bin (interno)";
            btnLoadFile1.setText("A: " + nameA);
        }

        btnLoadFile1.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            file1Launcher.launch(intent);
        });

        btnLoadFile2.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            file2Launcher.launch(intent);
        });
    }

    private File copyUriToTemp(Uri uri, String tempName) throws Exception {
        File temp = new File(getCacheDir(), tempName);
        try (InputStream in = getContentResolver().openInputStream(uri);
             FileOutputStream out = new FileOutputStream(temp)) {
            byte[] buffer = new byte[65536];
            int read;
            while ((read = in.read(buffer)) != -1) out.write(buffer, 0, read);
        }
        return temp;
    }

    private void tryCompare() {
        if (fileA == null || fileB == null) {
            tvDiffSummary.setText("Carga ambos archivos para comparar.");
            return;
        }

        tvDiffSummary.setText("Escaneando diferencias...");
        executor.execute(() -> {
            try {
                if (rafA != null) rafA.close();
                if (rafB != null) rafB.close();
                rafA = new RandomAccessFile(fileA, "r");
                rafB = new RandomAccessFile(fileB, "r");
                
                long lenA = fileA.length();
                long lenB = fileB.length();
                long maxLen = Math.max(lenA, lenB);
                long diffCount = 0;
                
                // Escaneo por bloques para no saturar CPU/RAM
                byte[] bufA = new byte[16384];
                byte[] bufB = new byte[16384];
                for (long i = 0; i < maxLen; i += bufA.length) {
                    int rA = rafA.read(bufA);
                    int rB = rafB.read(bufB);
                    int limit = Math.max(rA, rB);
                    for (int j = 0; j < limit; j++) {
                        byte bA = j < rA ? bufA[j] : (byte) 0xFF;
                        byte bB = j < rB ? bufB[j] : (byte) 0xFF;
                        if (bA != bB) diffCount++;
                    }
                }

                long finalDiffCount = diffCount;
                runOnUiThread(() -> {
                    tvDiffSummary.setText(String.format("A: %s (%d bytes) vs B: %s (%d bytes)",
                            nameA, lenA, nameB, lenB));
                    tvDiffStats.setVisibility(View.VISIBLE);
                    if (finalDiffCount == 0) {
                        tvDiffStats.setText("✅ Archivos idénticos — 0 diferencias.");
                        tvDiffStats.setTextColor(0xFF4CAF50);
                    } else {
                        double pct = (finalDiffCount * 100.0) / maxLen;
                        tvDiffStats.setText(String.format("⚠ %d bytes diferentes (%.1f%%) de %d bytes totales.",
                                finalDiffCount, pct, maxLen));
                        tvDiffStats.setTextColor(0xFFFF5252);
                    }
                    recyclerDiff.setAdapter(new DiffAdapter(rafA, rafB, maxLen));
                });

            } catch (Exception e) {
                runOnUiThread(() -> tvDiffSummary.setText("Error en comparación: " + e.getMessage()));
            }
        });
    }

    private String getFileName(Uri uri) {
        String name = "archivo";
        try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                if (idx >= 0) name = cursor.getString(idx);
            }
        } catch (Exception ignored) {}
        return name;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
        try { if (rafA != null) rafA.close(); } catch (Exception ignored) {}
        try { if (rafB != null) rafB.close(); } catch (Exception ignored) {}
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    static class DiffAdapter extends RecyclerView.Adapter<DiffAdapter.DiffViewHolder> {
        private final RandomAccessFile rafA, rafB;
        private final long maxLen;
        private final byte[] bufA = new byte[16];
        private final byte[] bufB = new byte[16];

        DiffAdapter(RandomAccessFile rafA, RandomAccessFile rafB, long maxLen) {
            this.rafA = rafA;
            this.rafB = rafB;
            this.maxLen = maxLen;
        }

        @NonNull
        @Override
        public DiffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hex_row, parent, false);
            return new DiffViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DiffViewHolder holder, int position) {
            long rowStart = (long) position * 16;
            int rA, rB;
            synchronized (rafA) {
                try {
                    rafA.seek(rowStart);
                    rA = rafA.read(bufA);
                } catch (Exception e) { rA = -1; }
            }
            synchronized (rafB) {
                try {
                    rafB.seek(rowStart);
                    rB = rafB.read(bufB);
                } catch (Exception e) { rB = -1; }
            }

            int length = Math.max(rA, rB);
            if (length <= 0) return;

            StringBuilder hexBuilder = new StringBuilder();
            StringBuilder asciiBuilder = new StringBuilder();
            boolean rowHasDiff = false;

            for (int i = 0; i < 16; i++) {
                if (i < length) {
                    byte a = i < rA ? bufA[i] : (byte) 0xFF;
                    byte b = i < rB ? bufB[i] : (byte) 0xFF;
                    boolean isDiff = (a != b);
                    if (isDiff) rowHasDiff = true;

                    if (isDiff) hexBuilder.append(String.format("%02X→%02X ", a & 0xFF, b & 0xFF));
                    else hexBuilder.append(String.format("%02X     ", a & 0xFF));

                    if ((a & 0xFF) >= 32 && (a & 0xFF) <= 126) asciiBuilder.append((char) a);
                    else asciiBuilder.append(".");
                } else {
                    hexBuilder.append("       ");
                    asciiBuilder.append(" ");
                }
            }

            holder.tvAddress.setText(String.format("%08X", rowStart));
            holder.tvHex.setText(hexBuilder.toString());
            holder.tvAscii.setText(asciiBuilder.toString());
            
            if (rowHasDiff) {
                holder.tvHex.setTextColor(0xFFFF5252);
                holder.tvAddress.setTextColor(0xFFFF9800);
            } else {
                holder.tvHex.setTextColor(0xFFE0E0E0);
                holder.tvAddress.setTextColor(0xFF2196F3);
            }
        }

        @Override
        public int getItemCount() {
            return (int) Math.ceil((double) maxLen / 16.0);
        }

        static class DiffViewHolder extends RecyclerView.ViewHolder {
            TextView tvAddress, tvHex, tvAscii;
            DiffViewHolder(View itemView) {
                super(itemView);
                tvAddress = itemView.findViewById(R.id.tvAddress);
                tvHex = itemView.findViewById(R.id.tvHex);
                tvAscii = itemView.findViewById(R.id.tvAscii);
            }
        }
    }
}
