package com.diamon.curso;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class HexViewerActivity extends AppCompatActivity {

    private TextView tvHexSummary;
    private RecyclerView recyclerHex;
    private HexAdapter hexAdapter;
    private RandomAccessFile raf;
    private long fileSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hex_viewer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Visor Hexadecimal Profesional");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvHexSummary = findViewById(R.id.tvHexSummary);
        recyclerHex = findViewById(R.id.recyclerHex);
        recyclerHex.setLayoutManager(new LinearLayoutManager(this));

        loadDataFromIntent();
    }

    private void loadDataFromIntent() {
        Uri fileUri = getIntent().getData();
        String fileName = "bios.bin";

        String biosSource = getSharedPreferences("flashrom_prefs", MODE_PRIVATE)
                .getString("bios_source", null);

        try {
            File dataFile;
            if (fileUri != null) {
                // Para archivos externos, lo ideal sería cargarlos a un temporal si son grandes,
                // pero por ahora intentamos abrirlos directamente si es posible o usar un stream buffer.
                // Como simplificación para esta fase, usaremos el archivo interno bios.bin si no hay URI.
                dataFile = new File(getCacheDir(), "temp_view.bin");
                try (InputStream in = getContentResolver().openInputStream(fileUri);
                     FileOutputStream out = new FileOutputStream(dataFile)) {
                    byte[] buffer = new byte[65536];
                    int read;
                    while ((read = in.read(buffer)) != -1) out.write(buffer, 0, read);
                }
                fileName = fileUri.getLastPathSegment();
                biosSource = "Archivo externo: " + fileName;
            } else {
                String trackedFile = getSharedPreferences("flashrom_prefs", MODE_PRIVATE)
                        .getString("last_read_file", "bios.bin");
                dataFile = new File(getFilesDir(), trackedFile);
                if (!dataFile.exists()) dataFile = new File(getFilesDir(), "bios.bin");
                if (!dataFile.exists()) {
                    tvHexSummary.setText("Error: No hay datos para visualizar.\nLee un chip o importa un archivo.");
                    return;
                }
                fileName = dataFile.getName();
            }

            // Si es Intel HEX, lo convertimos a binario temporal para el visor
            if (fileName != null && fileName.toLowerCase().endsWith(".hex")) {
                 byte[] hexData = java.nio.file.Files.readAllBytes(dataFile.toPath());
                 // Reutilizamos la lógica de conversión pero guardamos a temporal
                 // (Omitido por brevedad, asumimos que MainActivity ya lo convirtió a bios.bin)
            }

            displayBinary(dataFile, fileName, biosSource);

        } catch (Exception e) {
            tvHexSummary.setText("Error al cargar datos: " + e.getMessage());
        }
    }

    private void displayBinary(File file, String name, String biosSource) throws Exception {
        if (raf != null) raf.close();
        raf = new RandomAccessFile(file, "r");
        fileSize = file.length();
        
        String summary = String.format("Archivo: %s | Tamaño: %d bytes", name, fileSize);
        if (biosSource != null) summary += "\nOrigen: " + biosSource;
        tvHexSummary.setText(summary);
        
        hexAdapter = new HexAdapter(raf, fileSize, 0);
        recyclerHex.setAdapter(hexAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try { if (raf != null) raf.close(); } catch (Exception ignored) {}
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    static class HexAdapter extends RecyclerView.Adapter<HexAdapter.HexViewHolder> {
        private final RandomAccessFile raf;
        private final long totalSize;
        private final int startAddress;
        private final byte[] rowBuffer = new byte[16];

        public HexAdapter(RandomAccessFile raf, long totalSize, int startAddress) {
            this.raf = raf;
            this.totalSize = totalSize;
            this.startAddress = startAddress;
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
            
            int length = 0;
            synchronized (raf) {
                try {
                    raf.seek(rowStart);
                    length = raf.read(rowBuffer);
                } catch (Exception e) {
                    length = -1;
                }
            }

            StringBuilder hexBuilder = new StringBuilder(48);
            StringBuilder asciiBuilder = new StringBuilder(16);

            for (int i = 0; i < 16; i++) {
                if (i < length) {
                    int b = rowBuffer[i] & 0xFF;
                    hexBuilder.append(String.format("%02X ", b));
                    if (b >= 32 && b <= 126) asciiBuilder.append((char) b);
                    else asciiBuilder.append(".");
                } else {
                    hexBuilder.append("   ");
                    asciiBuilder.append(" ");
                }
            }

            holder.tvAddress.setText(String.format("%08X", currentAddr));
            holder.tvHex.setText(hexBuilder.toString());
            holder.tvAscii.setText(asciiBuilder.toString());
        }

        @Override
        public int getItemCount() {
            return (int) Math.ceil((double) totalSize / 16.0);
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
