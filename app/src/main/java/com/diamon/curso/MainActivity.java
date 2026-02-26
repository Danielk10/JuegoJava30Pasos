package com.diamon.curso;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FlashromApp";
    private static final String ACTION_USB_PERMISSION = "com.diamon.curso.USB_PERMISSION";

    static {
        // Cargar librerías compartidas copiadas a jniLibs (el orden importa según
        // dependencias)
        System.loadLibrary("usb-1.0");
        System.loadLibrary("pci");
        System.loadLibrary("ftdi1");
        // System.loadLibrary("jaylink"); // Descomentar si la incluimos
        System.loadLibrary("curso"); // Este es nuestro `native-lib.cpp` (definido así en CMake)
    }

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
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
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
            AssetHelper.copyAssets(getApplicationContext());
            runOnUiThread(() -> log("Assets (flashrom/config) copiados al almacenamiento interno."));
        });

        // Registrar receptor
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(usbReceiver, filter, Context.RECEIVER_NOT_EXPORTED);

        btnConnect.setOnClickListener(v -> searchAndRequestProgrammer());

        btnRead.setOnClickListener(v -> executeFlashromTask("-p ch341a_spi -r bios.bin"));
        btnWrite.setOnClickListener(v -> executeFlashromTask("-p ch341a_spi -w bios.bin"));
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
            PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION),
                    PendingIntent.FLAG_MUTABLE);
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

    private void executeFlashromTask(String fullArgs) {
        if (currentFd == -1) {
            log("No hay un programador conectado.");
            return;
        }

        File flashromBin = new File(getFilesDir() + "/usr/sbin/flashrom");
        if (!flashromBin.exists()) {
            log("CRÍTICO: El ejecutable de flashrom no existe en " + flashromBin.getAbsolutePath());
            return;
        }

        log("--- EJECUTANDO FLASHROM --- \nArgs: " + fullArgs);
        executor.execute(() -> {
            // Llama a nuestra librería nativa para settear el ambiente y hacer bash/exec
            int result = runNativeFlashrom(currentFd, flashromBin.getAbsolutePath(), fullArgs);
            runOnUiThread(() -> log("El proceso flashrom terminó con código: " + result));
        });
    }

    private void log(String message) {
        Log.i(TAG, message);
        String current = tvLog.getText().toString();
        tvLog.setText(current + "\n" + message);
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
    }

    /**
     * Declaración del método JNI nativo ubicado en native-lib.cpp
     */
    public native int runNativeFlashrom(int fd, String binPath, String args);
}