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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    private TextView tvStatus;
    private TextView tvLog;
    private Button btnConnect;
    private Button btnRead;
    private Button btnWrite;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    // Broadcast receiver para manejar el resultado del permiso USB
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
                        log("Permiso USB denegado para el dispositivo " + device);
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tvStatus);
        tvLog = findViewById(R.id.tvLog);
        btnConnect = findViewById(R.id.btnConnect);
        btnRead = findViewById(R.id.btnRead);
        btnWrite = findViewById(R.id.btnWrite);

        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        // EXTRAER ASSETS AL INICIO
        executor.execute(() -> {
            boolean wasExtracted = new File(getFilesDir(), "usr/sbin/flashrom").exists();
            AssetHelper.copyAssets(getApplicationContext());
            if (!wasExtracted) {
                runOnUiThread(() -> log("Assets iniciales copiados con éxito."));
            }
        });

        // Registrar receptor
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(usbReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(usbReceiver, filter);
        }

        btnConnect.setOnClickListener(v -> searchAndRequestProgrammer());

        btnRead.setOnClickListener(v -> executeFlashromTask("-p", "ch341a_spi", "-r", "bios.bin"));
        btnWrite.setOnClickListener(v -> executeFlashromTask("-p", "ch341a_spi", "-w", "bios.bin"));
    }

    private void searchAndRequestProgrammer() {
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);
        if (availableDrivers.isEmpty()) {
            log("No se encontraron programadores soportados conectados.");
            return;
        }

        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDevice device = driver.getDevice();
        log("Programador encontrado: " + device.getProductName() + " (VID: " + device.getVendorId() + ")");

        if (usbManager.hasPermission(device)) {
            connectToDevice(device);
        } else {
            log("Solicitando permiso USB...");
            int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0;
            PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION),
                    flags);
            usbManager.requestPermission(device, permissionIntent);
        }
    }

    private void connectToDevice(UsbDevice device) {
        currentConnection = usbManager.openDevice(device);
        if (currentConnection == null) {
            log("Error alojando la conexión USB (openDevice falló)");
            return;
        }

        currentFd = currentConnection.getFileDescriptor();
        tvStatus.setText("Status: Conectado (FD: " + currentFd + ")");
        log("Conexión exitosa. El Descriptor es: " + currentFd);

        btnRead.setEnabled(true);
        btnWrite.setEnabled(true);
    }

    private void executeFlashromTask(String... args) {
        if (currentFd == -1) {
            log("No hay un programador conectado.");
            return;
        }

        File flashromBin = new File(getFilesDir(), "usr/sbin/flashrom");
        if (!flashromBin.exists()) {
            log("CRÍTICO: El ejecutable no existe: " + flashromBin.getAbsolutePath());
            return;
        }

        log("--- EJECUTANDO FLASHROM ---");
        executor.execute(() -> runFlashromProcess(flashromBin, args));
    }

    private void runFlashromProcess(File flashromBin, String[] args) {
        List<String> command = new ArrayList<>();
        command.add(flashromBin.getAbsolutePath());
        for (String arg : args) {
            command.add(arg);
        }

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(getFilesDir());
            pb.redirectErrorStream(true);

            // Inyectar entorno:
            Map<String, String> env = pb.environment();

            // 1. EL PARCHE MAESTRO: pasar el FD en texto plano
            env.put("ANDROID_USB_FD", String.valueOf(currentFd));

            // 2. Ruta a las librerías dinámicas precompiladas (.so en jniLibs se copian acá
            // por Android)
            String jniLibs = getApplicationInfo().nativeLibraryDir;
            env.put("LD_LIBRARY_PATH", jniLibs);

            Process process = pb.start();

            // Leer línea por línea la salida estándar y error en tiempo real
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    final String uiLine = line;
                    runOnUiThread(() -> log("> " + uiLine));
                }
            }

            int exitCode = process.waitFor();
            runOnUiThread(() -> log("--- PROCESO TERMINADO (CÓDIGO: " + exitCode + ") ---"));

        } catch (Exception e) {
            Log.e(TAG, "Error ejecutando flashrom", e);
            runOnUiThread(() -> log("Error Fatal: " + e.getMessage()));
        }
    }

    private void log(String message) {
        Log.i(TAG, message);
        String current = tvLog.getText().toString();
        tvLog.setText(current + "\n" + message);

        // Auto-scroll could be added if you have a reference to the ScrollView
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(usbReceiver);
        } catch (Exception e) {
            // Ignorar
        }
        if (currentConnection != null) {
            currentConnection.close();
        }
        executor.shutdown();
    }
}