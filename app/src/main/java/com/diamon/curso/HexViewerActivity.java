package com.diamon.curso;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class HexViewerActivity extends AppCompatActivity {

    private TextView tvHexSummary;
    private RecyclerView recyclerHex;
    private HexAdapter hexAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hex_viewer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Visor Hexadecimal ROM");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvHexSummary = findViewById(R.id.tvHexSummary);
        recyclerHex = findViewById(R.id.recyclerHex);
        recyclerHex.setLayoutManager(new LinearLayoutManager(this));

        loadHexData();
    }

    private void loadHexData() {
        File biosFile = new File(getFilesDir(), "bios.bin");
        if (!biosFile.exists()) {
            tvHexSummary.setText(
                    "Error: No se encontró el archivo 'bios.bin'.\nDebes leer un chip o importar una ROM primero.");
            return;
        }

        long totalSize = biosFile.length();
        tvHexSummary.setText(String.format("Tamaño ROM: %d bytes (Mapeo directo)", totalSize));

        hexAdapter = new HexAdapter(biosFile);
        recyclerHex.setAdapter(hexAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    static class HexAdapter extends RecyclerView.Adapter<HexAdapter.HexViewHolder> {
        private MappedByteBuffer buffer;
        private long fileSize = 0;

        public HexAdapter(File file) {
            try {
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                FileChannel channel = raf.getChannel();
                this.fileSize = channel.size();
                this.buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @NonNull
        @Override
        public HexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hex_row, parent, false);
            return new HexViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HexViewHolder holder, int position) {
            if (buffer == null)
                return;

            int address = position * 16;
            int length = (int) Math.min(16, fileSize - address);
            byte[] rowBytes = new byte[length];

            buffer.position(address);
            buffer.get(rowBytes, 0, length);

            StringBuilder hexBuilder = new StringBuilder(48);
            StringBuilder asciiBuilder = new StringBuilder(16);

            for (int i = 0; i < 16; i++) {
                if (i < length) {
                    hexBuilder.append(String.format("%02X ", rowBytes[i]));
                    char c = (char) rowBytes[i];
                    if (c >= 32 && c <= 126) {
                        asciiBuilder.append(c);
                    } else {
                        asciiBuilder.append(".");
                    }
                } else {
                    hexBuilder.append("   ");
                }
            }

            holder.tvAddress.setText(String.format("%08X", address));
            holder.tvHex.setText(hexBuilder.toString());
            holder.tvAscii.setText(asciiBuilder.toString());
        }

        @Override
        public int getItemCount() {
            return (int) Math.ceil((double) fileSize / 16.0);
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
