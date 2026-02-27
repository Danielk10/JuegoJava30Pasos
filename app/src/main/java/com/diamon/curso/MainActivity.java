package com.diamon.curso;

import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FlashromApp";
    private static final String ACTION_USB_PERMISSION = "com.diamon.curso.USB_PERMISSION";
    private static final String PREFS = "flashrom_prefs";
    private static final String KEY_PROGRAMMER = "selected_programmer";

    private UsbManager usbManager;
    private UsbDeviceConnection currentConnection;
    private int currentFd = -1;

    private LinearLayout layoutLoading;
    private LinearLayout layoutMainUI;
    private ScrollView scrollLog;
    private TextView tvStatus, tvLog, tvLoadingText;
    private Button btnConnect, btnProbe, btnVerify, btnRead, btnWrite, btnImport, btnExport;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private String selectedProgrammer = "ch341a_spi";

    // API para importar (Cargar archivo de cualquier carpeta)
    private final ActivityResultLauncher<Intent> fileOpenLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        importRomFile(uri);
                    }
                }
            });

    // API para exportar (Guardar archivo en carpeta seleccionada por el usuario)
    private final ActivityResultLauncher<Intent> fileSaveLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        exportRomFileToUri(uri);
                    }
                }
            });

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
        btnImport = findViewById(R.id.btnImport);
        btnExport = findViewById(R.id.btnExport);

        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        selectedProgrammer = getSharedPreferences(PREFS, MODE_PRIVATE).getString(KEY_PROGRAMMER, "ch341a_spi");
        setupLogCopySupport();

        // Ocultar UI y mostrar ProgressBar mientras carga assets
        layoutMainUI.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.VISIBLE);

        executor.execute(() -> {
            // AutoLimpieza de directorios erróneos (para arreglar el error de usr/usr)
            File buggedDir = new File(getFilesDir(), "usr/usr");
            if (buggedDir.exists()) {
                deleteRecursively(buggedDir);
                Log.d("MainActivity", "Carpeta residual usr/usr eliminada automáticamente.");
            }

            boolean wasExtracted = AssetHelper.areAssetsExtracted(getApplicationContext());
            if (!wasExtracted) {
                runOnUiThread(() -> tvLoadingText.setText("Extrayendo binarios nativos por primera vez..."));
            } else {
                runOnUiThread(() -> tvLoadingText.setText("Verificando dependencias locales..."));
            }

            boolean runtimeReady = AssetHelper.ensureRuntimeReady(getApplicationContext());

            runOnUiThread(() -> {
                layoutLoading.setVisibility(View.GONE);
                layoutMainUI.setVisibility(View.VISIBLE);
                logRuntimeInfo();
                logDependencyChecklist();
                if (!runtimeReady) {
                    log("[WARN] No se pudieron preparar todas las dependencias locales.");
                } else if (!wasExtracted) {
                    log("Assets copiados correctamente al almacenamiento interno.");
                } else {
                    log("App iniciada. Dependencias locales ya estaban listas (no se recargaron).");
                }
            });
        });

        // Setup Boradcast Receiver
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        ContextCompat.registerReceiver(this, usbReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED);

        // Listener setup para todos los botones
        btnConnect.setOnClickListener(v -> searchAndRequestProgrammer());

        btnProbe.setOnClickListener(v -> ensureProgrammerThenRun(() -> executeFlashromTask("-p", selectedProgrammer)));
        btnVerify.setOnClickListener(v -> ensureProgrammerThenRun(() -> executeFlashromTask("-p", selectedProgrammer, "-v", "bios.bin")));
        btnRead.setOnClickListener(v -> ensureProgrammerThenRun(() -> executeFlashromTask("-p", selectedProgrammer, "-r", "bios.bin")));
        btnWrite.setOnClickListener(v -> ensureProgrammerThenRun(() -> executeFlashromTask("-p", selectedProgrammer, "-w", "bios.bin")));

        btnExport.setOnClickListener(v -> {
            File sourceFile = new File(getFilesDir(), "bios.bin");
            if (!sourceFile.exists()) {
                log("Error: No hay ningún 'bios.bin' leído internamente aún. ¡Léelo primero desde el chip!");
                return;
            }

            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/octet-stream");
            intent.putExtra(Intent.EXTRA_TITLE, "bios_backup.bin");
            fileSaveLauncher.launch(intent);
        });

        btnImport.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            fileOpenLauncher.launch(intent);
        });
    }

    private void exportRomFileToUri(Uri uri) {
        File sourceFile = new File(getFilesDir(), "bios.bin");
        try (InputStream in = new java.io.FileInputStream(sourceFile);
                OutputStream out = getContentResolver().openOutputStream(uri)) {

            if (out == null)
                throw new Exception("No se pudo obtener acceso de escritura al archivo destino.");

            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            log("Éxito: ROM respaldada correctamente en la carpeta seleccionada.");
        } catch (Exception e) {
            log("Error guardando el archivo: " + e.getMessage());
        }
    }

    private void importRomFile(Uri uri) {
        try (InputStream in = getContentResolver().openInputStream(uri);
                OutputStream out = new FileOutputStream(new File(getFilesDir(), "bios.bin"))) {
            if (in == null) {
                throw new IllegalStateException("No se pudo abrir el archivo seleccionado para lectura.");
            }
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            log("ROM importada exitosamente como 'bios.bin'. ¡Lista para Flashear!");
        } catch (Exception e) {
            log("Error copiando ROM desde almacenamiento: " + e.getMessage());
        }
    }

    private void searchAndRequestProgrammer() {
        Map<String, UsbDevice> devices = usbManager.getDeviceList();
        if (devices == null || devices.isEmpty()) {
            log("No se detectó ningún dispositivo USB conectado.");
            return;
        }

        List<UsbDevice> candidates = new ArrayList<>(devices.values());
        Collections.sort(candidates, new Comparator<UsbDevice>() {
            @Override
            public int compare(UsbDevice a, UsbDevice b) {
                int vid = Integer.compare(a.getVendorId(), b.getVendorId());
                if (vid != 0) return vid;
                int pid = Integer.compare(a.getProductId(), b.getProductId());
                if (pid != 0) return pid;
                return Integer.compare(a.getDeviceId(), b.getDeviceId());
            }
        });
        if (candidates.size() == 1) {
            requestUsbPermission(candidates.get(0));
            return;
        }

        CharSequence[] labels = new CharSequence[candidates.size()];
        for (int i = 0; i < candidates.size(); i++) {
            labels[i] = formatUsbDeviceLabel(candidates.get(i));
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("Selecciona dispositivo USB")
                .setItems(labels, (dialog, which) -> requestUsbPermission(candidates.get(which)))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void requestUsbPermission(UsbDevice device) {
        String deviceName = device.getProductName() == null ? "Dispositivo USB" : device.getProductName();
        log("Dispositivo detectado: " + deviceName + " | Solicitando enlace...");
        log("VID:PID detectado => " + String.format(Locale.US, "%04x:%04x", device.getVendorId(), device.getProductId()));

        if (usbManager.hasPermission(device)) {
            connectToDevice(device);
        } else {
            int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0;
            PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION),
                    flags);
            usbManager.requestPermission(device, permissionIntent);
        }
    }

    private String formatUsbDeviceLabel(UsbDevice device) {
        String productName = device.getProductName();
        if (productName == null || productName.trim().isEmpty()) {
            productName = "Dispositivo USB";
        }
        String manufacturer = device.getManufacturerName();
        if (manufacturer == null || manufacturer.trim().isEmpty()) {
            manufacturer = "Fabricante desconocido";
        }
        return productName + " (" + manufacturer + ")\nVID:PID "
                + String.format(Locale.US, "%04x:%04x", device.getVendorId(), device.getProductId());
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
        log("Conectado a USB VID:PID " + String.format(Locale.US, "%04x:%04x", device.getVendorId(), device.getProductId())
                + " | DeviceId: " + device.getDeviceId());

        btnProbe.setEnabled(true);
        btnVerify.setEnabled(true);
        btnRead.setEnabled(true);
        btnWrite.setEnabled(true);

        if (selectedProgrammer == null || selectedProgrammer.trim().isEmpty()) {
            selectedProgrammer = "ch341a_spi";
        }
        log("Programador flashrom activo: " + selectedProgrammer);
    }

    private void ensureProgrammerThenRun(Runnable action) {
        if (selectedProgrammer == null || selectedProgrammer.trim().isEmpty()) {
            showProgrammerDialog(action);
            return;
        }
        action.run();
    }

    private void showProgrammerDialog(Runnable action) {
        android.widget.EditText input = new android.widget.EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Ej: ch341a_spi, ft2232_spi:type=2232H, serprog:dev=/dev/ttyUSB0");
        input.setText(selectedProgrammer == null ? "ch341a_spi" : selectedProgrammer);

        new android.app.AlertDialog.Builder(this)
                .setTitle("Programador flashrom")
                .setMessage("Ingresa el parámetro de programador para -p (compatible con todos los drivers soportados por flashrom).")
                .setView(input)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String value = input.getText() == null ? "" : input.getText().toString().trim();
                    if (value.isEmpty()) {
                        value = "ch341a_spi";
                    }
                    selectedProgrammer = value;
                    getSharedPreferences(PREFS, MODE_PRIVATE).edit().putString(KEY_PROGRAMMER, value).apply();
                    log("Programador flashrom actualizado: " + selectedProgrammer);
                    if (action != null) {
                        action.run();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void executeFlashromTask(String... args) {
        if (currentFd == -1) {
            log("Error lógico: El FD de USB se perdió.");
            return;
        }

        File preferredFlashromBin = new File(getFilesDir(), "usr/sbin/flashrom");
        if (!preferredFlashromBin.exists()) {
            // Fallback directo a jniLibs por si la creación de enlaces falló.
            log("[WARN] flashrom en files/usr/sbin no encontrado; usando fallback jniLibs.");
            preferredFlashromBin = new File(getApplicationInfo().nativeLibraryDir, "libflashrom_bin.so");
        }
        final File flashromBin = preferredFlashromBin;
        if (!flashromBin.exists()) {
            log("Fallo crítico: Binario 'flashrom' no existe. (" + flashromBin.getAbsolutePath() + ")");
            return;
        }

        log("------------\n[INICIANDO OPERACIÓN] flashrom " + String.join(" ", args));
        log("Directorio de trabajo: " + getFilesDir().getAbsolutePath());
        log("Binario objetivo: " + flashromBin.getAbsolutePath());

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

            // Recrear las variables de entorno de PTC que incluyen la ruta PATH necesaria
            // si fuesemos a usar otras libs anidadas
            String jniLibs = getApplicationInfo().nativeLibraryDir;
            String fallbackPath = System.getenv("PATH");
            env.put("LD_LIBRARY_PATH", jniLibs + ":" + new File(getFilesDir(), "usr/lib").getAbsolutePath());
            env.put("PATH", jniLibs + (fallbackPath != null ? ":" + fallbackPath : ""));

            log("Entorno flashrom => ANDROID_USB_FD=" + currentFd);
            log("Entorno flashrom => LD_LIBRARY_PATH=" + env.get("LD_LIBRARY_PATH"));
            log("Entorno flashrom => PATH=" + env.get("PATH"));

            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log("[native] " + line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log("[PROCESO TERMINADO] Exit Code: " + exitCode + " (OK)\n");
            } else {
                log("[PROCESO TERMINADO] Exit Code: " + exitCode + " (ERROR)\n");
            }

        } catch (Exception e) {
            Log.e(TAG, "Error fatal de SO ejecutando: flashrom", e);
            log("[CRITICAL] ProcessBuilder falló: " + e.getMessage());
            log(stackTrace(e));
        }
    }

    private void log(String message) {
        Log.i(TAG, message);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            appendLogOnUi(message);
        } else {
            runOnUiThread(() -> appendLogOnUi(message));
        }
    }

    private void appendLogOnUi(String message) {
        String current = tvLog.getText().toString();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        tvLog.setText(current + "\n" + "[" + time + "] " + message);
        scrollLog.post(() -> scrollLog.fullScroll(ScrollView.FOCUS_DOWN));
    }

    private String stackTrace(Throwable error) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        error.printStackTrace(pw);
        return sw.toString();
    }

    private void setupLogCopySupport() {
        tvLog.setTextIsSelectable(true);
        tvLog.setLongClickable(true);
        tvLog.setOnLongClickListener(v -> {
            String logs = tvLog.getText() == null ? "" : tvLog.getText().toString();
            if (logs.trim().isEmpty()) {
                android.widget.Toast.makeText(this, "No hay texto en el log para copiar.", android.widget.Toast.LENGTH_SHORT).show();
                return true;
            }
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            if (clipboard == null) {
                android.widget.Toast.makeText(this, "No se pudo acceder al portapapeles.", android.widget.Toast.LENGTH_SHORT).show();
                return true;
            }
            clipboard.setPrimaryClip(ClipData.newPlainText("flash_eeprom_tool_logs", logs));
            android.widget.Toast.makeText(this, "Logs copiados al portapapeles.", android.widget.Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void logRuntimeInfo() {
        File nativeDir = new File(getApplicationInfo().nativeLibraryDir);
        File usrLibDir = new File(getFilesDir(), "usr/lib");
        File usrShareDir = new File(getFilesDir(), "usr/share");
        String assetRuntimeRoot = AssetHelper.getResolvedRuntimeRoot(getApplicationContext());
        log("App iniciada. Dependencias locales en orden.");
        log("Android " + Build.VERSION.RELEASE + " (SDK " + Build.VERSION.SDK_INT + ")");
        log("Runtime root en assets: " + (assetRuntimeRoot == null ? "NO DETECTADO" : assetRuntimeRoot));
        log("Ruta nativeLibraryDir: " + nativeDir.getAbsolutePath());
        log("Ruta usr/lib runtime: " + usrLibDir.getAbsolutePath());
        log("Ruta usr/share runtime: " + usrShareDir.getAbsolutePath());
        log("libflashrom_bin.so presente: " + new File(nativeDir, "libflashrom_bin.so").exists());
        log("libcrypto.so.3 en runtime: " + new File(usrLibDir, "libcrypto.so.3").exists()
                + " (jni origen: " + findNativeName(nativeDir, "libcrypto.so.3") + ")");
        log("libssl.so.3 en runtime: " + new File(usrLibDir, "libssl.so.3").exists()
                + " (jni origen: " + findNativeName(nativeDir, "libssl.so.3") + ")");
        log("libz.so.1 en runtime: " + new File(usrLibDir, "libz.so.1").exists()
                + " (jni origen: " + findNativeName(nativeDir, "libz.so.1") + ")");
        log("pci.ids.gz en runtime: " + new File(usrShareDir, "pci.ids.gz").exists());
    }

    private void logDependencyChecklist() {
        File nativeDir = new File(getApplicationInfo().nativeLibraryDir);
        String[] requiredBins = {"libflashrom_bin.so", "libsetpci.so", "libpcilmr.so", "liblspci.so", "libupdate-pciids.so", "libftdi_eeprom.so"};
        for (String name : requiredBins) {
            log("jniLibs binario " + name + ": " + new File(nativeDir, name).exists());
        }
        String[] requiredLibs = {"libusb-1.0.so", "libflashrom.so", "libpci.so", "libftdi1.so", "libjaylink.so", "libcrypto.so.3", "libssl.so.3", "libz.so.1", "libconfuse.so", "libc++_shared.so"};
        for (String name : requiredLibs) {
            String nativeName = findNativeName(nativeDir, name);
            log("jniLibs librería " + name + ": " + !"NO".equals(nativeName)
                    + " (archivo: " + nativeName + ")");
        }
    }

    private String findNativeName(File nativeDir, String runtimeSoname) {
        File exact = new File(nativeDir, runtimeSoname);
        if (exact.exists()) {
            return runtimeSoname;
        }
        int marker = runtimeSoname.indexOf(".so.");
        if (marker > 0) {
            String altName = runtimeSoname.substring(0, marker) + "_"
                    + runtimeSoname.substring(marker + 4).replace('.', '_') + ".so";
            if (new File(nativeDir, altName).exists()) {
                return altName;
            }
        }
        return "NO";
    }

    // Función de soporte para limpiar directorios defectuosos guardados por el
    // sistema
    private void deleteRecursively(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            File[] children = fileOrDirectory.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursively(child);
                }
            }
        }
        fileOrDirectory.delete();
    }

    private void setButtonsEnabled(boolean enabled) {
        btnProbe.setEnabled(enabled);
        btnVerify.setEnabled(enabled);
        btnRead.setEnabled(enabled);
        btnWrite.setEnabled(enabled);
        btnConnect.setEnabled(enabled);
        btnImport.setEnabled(enabled);
        btnExport.setEnabled(enabled);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_hex_viewer) {
            startActivity(new Intent(this, HexViewerActivity.class));
            return true;
        } else if (id == R.id.action_programmer) {
            showProgrammerDialog(null);
            return true;
        } else if (id == R.id.action_clear_logs) {
            tvLog.setText("--- Log ---");
            log("Terminal reiniciada.");
            return true;
        } else if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        } else if (id == R.id.action_policy) {
            startActivity(new Intent(this, PolicyActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showAboutDialog() {
        TextView aboutText = new TextView(this);
        int padding = (int) (24 * getResources().getDisplayMetrics().density);
        aboutText.setPadding(padding, padding, padding, padding / 2);
        aboutText.setTextColor(getResources().getColor(android.R.color.black, getTheme()));
        aboutText.setLinkTextColor(getResources().getColor(android.R.color.holo_blue_dark, getTheme()));
        aboutText.setMovementMethod(LinkMovementMethod.getInstance());
        String aboutHtml = "<b>Flash EEPROM Tool</b><br/>"
                + "Aplicación Android para lectura, verificación y escritura SPI/I2C con flashrom.<br/><br/>"
                + "<b>Licencia del proyecto:</b> GPLv3.<br/><br/>"
                + "<b>Dependencias principales y licencias:</b><br/>"
                + "• <a href='https://github.com/libusb/libusb'>libusb</a> (LGPL-2.1+)<br/>"
                + "• <a href='https://github.com/pciutils/pciutils'>pciutils</a> (GPL-2.0+)<br/>"
                + "• <a href='https://developer.intra2net.com/git/libftdi'>libftdi</a> (LGPL-2.1+)<br/>"
                + "• <a href='https://gitlab.zapb.de/libjaylink/libjaylink'>libjaylink</a> (GPL-2.0+)<br/>"
                + "• <a href='https://github.com/flashrom/flashrom'>flashrom</a> (GPL-2.0+)";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            aboutText.setText(Html.fromHtml(aboutHtml, Html.FROM_HTML_MODE_LEGACY));
        } else {
            @SuppressWarnings("deprecation")
            android.text.Spanned legacyHtml = Html.fromHtml(aboutHtml);
            aboutText.setText(legacyHtml);
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("Acerca de")
                .setView(aboutText)
                .setPositiveButton("Aceptar", null)
                .show();
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
