package com.diamon.curso;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HexViewerActivity extends AppCompatActivity {

    private TextView tvHexBuffer, tvHexSummary;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    // Limite máximo de lectura visual (por ejemplo 1MB) para no colapsar la RAM del
    // TextView de la Interfaz
    private static final int MAX_VIEW_SIZE_BYTES = 1024 * 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hex_viewer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Visor Hexadecimal ROM");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvHexSummary = findViewById(R.id.tvHexSummary);
        tvHexBuffer = findViewById(R.id.tvHexBuffer);

        loadHexData();
    }

    private void loadHexData() {
        executor.execute(() -> {
            File biosFile = new File(getFilesDir(), "bios.bin");
            if (!biosFile.exists()) {
                runOnUiThread(() -> {
                    tvHexSummary.setText("Error");
                    tvHexBuffer.setText(
                            "No se encontró el archivo 'bios.bin'.\nDebes leer un chip o importar una ROM primero.");
                });
                return;
            }

            long totalSize = biosFile.length();
            int bytesToRead = (int) Math.min(totalSize, MAX_VIEW_SIZE_BYTES);

            runOnUiThread(() -> tvHexSummary.setText("Tamaño ROM: " + totalSize + " bytes\n" +
                    (totalSize > MAX_VIEW_SIZE_BYTES ? "Mostrando primeros " + MAX_VIEW_SIZE_BYTES + " bytes..."
                            : "")));

            StringBuilder hexBuilder = new StringBuilder();
            try (FileInputStream fis = new FileInputStream(biosFile)) {
                byte[] buffer = new byte[16];
                int read;
                int address = 0;

                while ((read = fis.read(buffer)) != -1 && address < bytesToRead) {
                    hexBuilder.append(String.format("%08X | ", address));

                    // Colores ANSI no soportan directo en texto simple, usaremos caracteres
                    // normales
                    // Hex part
                    for (int i = 0; i < 16; i++) {
                        if (i < read) {
                            hexBuilder.append(String.format("%02X ", buffer[i]));
                        } else {
                            hexBuilder.append("   ");
                        }
                    }

                    hexBuilder.append("| ");

                    // ASCII part
                    for (int i = 0; i < read; i++) {
                        char c = (char) buffer[i];
                        if (c >= 32 && c <= 126) {
                            hexBuilder.append(c);
                        } else {
                            hexBuilder.append(".");
                        }
                    }

                    hexBuilder.append("\n");
                    address += 16;
                }

                final String finalText = hexBuilder.toString();
                runOnUiThread(() -> tvHexBuffer.setText(finalText));

            } catch (Exception e) {
                runOnUiThread(() -> tvHexBuffer.setText("Error leyendo binario: " + e.getMessage()));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
