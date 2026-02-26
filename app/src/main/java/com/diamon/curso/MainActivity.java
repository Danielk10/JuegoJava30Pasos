package com.diamon.curso;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FlashromApp";
    private static final String ACTION_USB_PERMISSION = "com.diamon.curso.USB_PERMISSION";

    private UsbManager usbManager;
    private UsbDeviceConnection currentConnection;
    private int currentFd = -1;

    private LinearLayout layoutLoading;
    private LinearLayout layoutMainUI;
    private ScrollView scrollLog;
    private TextView tvStatus, tvLog, tvLoadingText;
    private Button btnConnect, btnProbe, btnVerify, btnRead, btnWrite;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE, UsbDevice.class);
                    } else {
                        @SuppressWarnings("deprecation")
                        UsbDevice d = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        device = d;
                    }

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            connectToDevice(device);
                        }
                    } else {
                        log("Permiso USB denegado para " + device);
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutLoading = findViewById(R.id.layoutLoading);
        layoutMainUI = findViewById(R.id.layoutMainUI);
        tvLoadingText = findViewById(R.id.tvLoadingText);

        tvStatus = findViewById(R.id.tvStatus);
        tvLog = findViewById(R.id.tvLog);
        scrollLog = findViewById(R.id.scrollLog);

        btnConnect = findViewById(R.id.btnConnect);
        btnProbe = findViewById(R.id.btnProbe);
        btnVerify = findViewById(R.id.btnVerify);
        btnRead = findViewById(R.id.btnRead);
        btnWrite = findViewById(R.id.btnWrite);

        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        // Ocultar UI y mostrar ProgressBar mientras carga assets
        layoutMainUI.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.VISIBLE);

        executor.execute(() -> {
            boolean wasExtracted = new File(getFilesDir(), "usr/sbin/flashrom").exists();
            if (!wasExtracted) {
                runOnUiThread(() -> tvLoadingText.setText("Extrayendo binarios nativos por primera vez..."));
            }

            AssetHelper.copyAssets(getApplicationContext());

            runOnUiThread(() -> {
                layoutLoading.setVisibility(View.GONE);
                layoutMainUI.setVisibility(View.VISIBLE);
                if (!wasExtracted) {
                    log("Assets copiados correctamente al almacenamiento interno.");
                } else {
                    log("App iniciada. Dependencias locales en orden.");
                }
            });
        });

        // Setup Boradcast Receiver
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(usbReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(usbReceiver, filter);
        }

        // Listener setup para todos los botones
        btnConnect.setOnClickListener(v -> searchAndRequestProgrammer());

        btnProbe.setOnClickListener(v -> executeFlashromTask("-p", "ch341a_spi")); // Solo -p sonda el chip sin operar
        btnVerify.setOnClickListener(v -> executeFlashromTask("-p", "ch341a_spi", "-v", "bios.bin")); // Validara contra
                                                                                                      // un bin anterior
        btnRead.setOnClickListener(v -> executeFlashromTask("-p", "ch341a_spi", "-r", "bios.bin"));
        btnWrite.setOnClickListener(v -> executeFlashromTask("-p", "ch341a_spi", "-w", "bios.bin"));
    }

    private void searchAndRequestProgrammer() {
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);
        if (availableDrivers.isEmpty()) {
            log("No se detectó ningún CH341A / programador conectado.");
            return;
        }

        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDevice device = driver.getDevice();
        log("Dispositivo detectado: " + device.getProductName() + " | Solicitando enlace...");

        if (usbManager.hasPermission(device)) {
            connectToDevice(device);
        } else {
            int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0;
            PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION),
                    flags);
            usbManager.requestPermission(device, permissionIntent);
        }
    }

    private void connectToDevice(UsbDevice device) {
        currentConnection = usbManager.openDevice(device);
        if (currentConnection == null) {
            log(device.getProductName() + " falló en enlazarse a la app (openDevice == null)");
            return;
        }

        currentFd = currentConnection.getFileDescriptor();
        tvStatus.setText("Status: " + device.getProductName() + " Conectado");
        log("¡Permiso otorgado! Token interno de USB: " + currentFd);

        btnProbe.setEnabled(true);
        btnVerify.setEnabled(true);
        btnRead.setEnabled(true);
        btnWrite.setEnabled(true);
    }

    private void executeFlashromTask(String... args) {
        if (currentFd == -1) {
            log("Error lógico: El FD de USB se perdió.");
            return;
        }

        File flashromBin = new File(getFilesDir(), "usr/sbin/flashrom");
        if (!flashromBin.exists()) {
            log("Fallo crítico: Binario 'flashrom' no existe. Reinicie la aplicación para reextraerlo.");
            return;
        }

        log("------------\n[INICIANDO OPERACIÓN] flashrom " + String.join(" ", args));

        // Deshabilitar botones mientras trabaja para evitar crasheos por hilos
        // paralelos
        setButtonsEnabled(false);

        executor.execute(() -> {
            runFlashromProcess(flashromBin, args);
            runOnUiThread(() -> setButtonsEnabled(true));
        });
    }

    private void runFlashromProcess(File flashromBin, String[] args) {
        List<String> command = new ArrayList<>();
        command.add(flashromBin.getAbsolutePath());
        for (String arg : args) {
            command.add(arg);
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(getFilesDir()); // Corriendo desde la raid de Archivos
            pb.redirectErrorStream(true); // Redirige System.err a System.out

            // Inyectando entorno para las librerías fake_root
            Map<String, String> env = pb.environment();
            env.put("ANDROID_USB_FD", String.valueOf(currentFd));
            env.put("LD_LIBRARY_PATH", getApplicationInfo().nativeLibraryDir);

            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    final String uiLine = line;
                    runOnUiThread(() -> log(" " + uiLine));
                }
            }

            int exitCode = process.waitFor();
            runOnUiThread(() -> log("[PROCESO TERMINADO] Exit Code: " + exitCode + "\n"));

        } catch (Exception e) {
            Log.e(TAG, "Error fatal de SO ejecutando: flashrom", e);
            runOnUiThread(() -> log("[CRITICAL] ProcessBuilder falló: " + e.getMessage()));
        }
    }

    private void log(String message) {
        Log.i(TAG, message);
        String current = tvLog.getText().toString();
        tvLog.setText(current + "\n" + message);

        // Auto Scroll simple
        scrollLog.post(() -> scrollLog.fullScroll(ScrollView.FOCUS_DOWN));
    }

    private void setButtonsEnabled(boolean enabled) {
        btnProbe.setEnabled(enabled);
        btnVerify.setEnabled(enabled);
        btnRead.setEnabled(enabled);
        btnWrite.setEnabled(enabled);
        btnConnect.setEnabled(enabled);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(usbReceiver);
        } catch (Exception e) {
        }

        if (currentConnection != null) {
            currentConnection.close();
        }
        executor.shutdownNow(); // Finalizar todos los hilos
    }
}