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
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class HexViewerActivity extends AppCompatActivity {

    private TextView tvHexSummary;
    private RecyclerView recyclerHex;
    private HexAdapter hexAdapter;

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

        // Leer el origen del archivo (importado vs leído del chip)
        String biosSource = getSharedPreferences("flashrom_prefs", MODE_PRIVATE)
                .getString("bios_source", null);

        byte[] data;
        try {
            if (fileUri != null) {
                data = readUriToBytes(fileUri);
                fileName = fileUri.getLastPathSegment();
                biosSource = "Archivo externo: " + fileName;
            } else {
                File biosFile = new File(getFilesDir(), "bios.bin");
                if (!biosFile.exists()) {
                    tvHexSummary.setText("Error: No hay datos para visualizar.\nLee un chip o importa un archivo.");
                    return;
                }
                data = java.nio.file.Files.readAllBytes(biosFile.toPath());
            }

            if (fileName != null && fileName.toLowerCase().endsWith(".hex")) {
                parseIntelHex(data, biosSource);
            } else {
                displayBinary(data, fileName, biosSource);
            }

        } catch (Exception e) {
            tvHexSummary.setText("Error al cargar datos: " + e.getMessage());
        }
    }

    private byte[] readUriToBytes(Uri uri) throws Exception {
        try (InputStream is = getContentResolver().openInputStream(uri)) {
            if (is == null)
                throw new IllegalStateException("No se pudo abrir el archivo.");
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        }
    }

    private void displayBinary(byte[] data, String name, String biosSource) {
        String summary = String.format("Archivo: %s | Tamaño: %d bytes", name, data.length);
        if (biosSource != null) {
            summary += "\nOrigen: " + biosSource;
        }
        tvHexSummary.setText(summary);
        hexAdapter = new HexAdapter(data, 0);
        recyclerHex.setAdapter(hexAdapter);
    }

    private void parseIntelHex(byte[] hexData, String biosSource) {
        try {
            String content = new String(hexData);
            String[] lines = content.split("\\r?\\n");

            // Intel HEX can be sparse, but for simplicity we'll find min/max and use a
            // buffer
            // or just show records. Let's show as a continuous buffer if possible.
            int minAddr = Integer.MAX_VALUE;
            int maxAddr = 0;

            // Preliminary pass to find size
            for (String line : lines) {
                if (!line.startsWith(":") || line.length() < 11)
                    continue;
                int byteCount = Integer.parseInt(line.substring(1, 3), 16);
                int address = Integer.parseInt(line.substring(3, 7), 16);
                int type = Integer.parseInt(line.substring(7, 9), 16);
                if (type == 0) { // Data record
                    minAddr = Math.min(minAddr, address);
                    maxAddr = Math.max(maxAddr, address + byteCount);
                }
            }

            if (minAddr == Integer.MAX_VALUE) {
                tvHexSummary.setText("Archivo HEX no contiene registros de datos válidos.");
                return;
            }

            byte[] binBuffer = new byte[maxAddr]; // Simplifies to global address space
            java.util.Arrays.fill(binBuffer, (byte) 0xFF);

            for (String line : lines) {
                if (!line.startsWith(":") || line.length() < 11)
                    continue;
                int byteCount = Integer.parseInt(line.substring(1, 3), 16);
                int address = Integer.parseInt(line.substring(3, 7), 16);
                int type = Integer.parseInt(line.substring(7, 9), 16);
                if (type == 0) {
                    for (int i = 0; i < byteCount; i++) {
                        binBuffer[address + i] = (byte) Integer.parseInt(line.substring(9 + i * 2, 11 + i * 2), 16);
                    }
                }
            }

            String summary = String.format("Intel HEX detectado | Rango: 0x%04X - 0x%04X", minAddr, maxAddr - 1);
            if (biosSource != null) {
                summary += "\nOrigen: " + biosSource;
            }
            tvHexSummary.setText(summary);
            hexAdapter = new HexAdapter(binBuffer, 0);
            recyclerHex.setAdapter(hexAdapter);

        } catch (Exception e) {
            tvHexSummary.setText("Error parseando Intel HEX: " + e.getMessage());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    static class HexAdapter extends RecyclerView.Adapter<HexAdapter.HexViewHolder> {
        private final byte[] data;
        private final int startAddress;

        public HexAdapter(byte[] data, int startAddress) {
            this.data = data;
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
            int rowStart = position * 16;
            int currentAddr = startAddress + rowStart;
            int length = Math.min(16, data.length - rowStart);

            StringBuilder hexBuilder = new StringBuilder(48);
            StringBuilder asciiBuilder = new StringBuilder(16);

            for (int i = 0; i < 16; i++) {
                if (i < length) {
                    byte b = data[rowStart + i];
                    hexBuilder.append(String.format("%02X ", b));
                    if (b >= 32 && b <= 126) {
                        asciiBuilder.append((char) b);
                    } else {
                        asciiBuilder.append(".");
                    }
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
            return (int) Math.ceil((double) data.length / 16.0);
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
