package com.diamon.ptc;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Ejecutor para el compilador SDCC.
 */
public class SdccExecutor {
    private static final String TAG = "SdccExecutor";

    private final File workDir;
    private final File nativeLibDir;
    private final File sdccShareDir;
    private final File gpUtilsShareDir;

    public SdccExecutor(Context context) {
        this.workDir = context.getFilesDir();
        this.nativeLibDir = new File(context.getApplicationInfo().nativeLibraryDir);
        this.sdccShareDir = new File(workDir, "usr/share/sdcc");
        this.gpUtilsShareDir = new File(workDir, "usr/share/gputils");
    }

    public String executeSdcc(String... args) {
        return executeBinary(workDir, "sdcc", args);
    }

    public String executeSdcc(File workingDir, String... args) {
        return executeBinary(workingDir, "sdcc", args);
    }

    public String executeBinary(String binaryName, String... args) {
        return executeBinary(workDir, binaryName, args);
    }

    public String executeBinary(File workingDir, String binaryName, String... args) {
        File binaryFile = new File(nativeLibDir, "lib" + binaryName + ".so");
        if (!binaryFile.exists()) {
            return "Error: No se encontro el binario " + binaryFile.getAbsolutePath();
        }

        List<String> command = new ArrayList<>();
        command.add(binaryFile.getAbsolutePath());

        command.add("-I" + new File(sdccShareDir, "include").getAbsolutePath());
        command.add("-I" + new File(sdccShareDir, "non-free/include").getAbsolutePath());
        command.add("-L" + new File(sdccShareDir, "lib").getAbsolutePath());
        command.add("-L" + new File(sdccShareDir, "non-free/lib").getAbsolutePath());
        for (String arg : args) {
            command.add(arg);
        }

        setupSymlinks();

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(workingDir);
            pb.redirectErrorStream(true);

            Map<String, String> env = pb.environment();
            env.put("SDCC_HOME", new File(workDir, "usr").getAbsolutePath());

            File usrLibDir = new File(new File(workDir, "usr"), "lib");
            if (!usrLibDir.exists()) usrLibDir.mkdirs();
            env.put("LD_LIBRARY_PATH", usrLibDir.getAbsolutePath() + ":" + nativeLibDir.getAbsolutePath());

            env.put("GPUTILS_HEADER_PATH", new File(gpUtilsShareDir, "header").getAbsolutePath());
            env.put("GPUTILS_LKR_PATH", new File(gpUtilsShareDir, "lkr").getAbsolutePath());

            String path = env.get("PATH");
            String binPath = new File(workDir, "usr/bin").getAbsolutePath();
            env.put("PATH", binPath + ":" + nativeLibDir.getAbsolutePath() + (path != null ? ":" + path : ""));

            Process process = pb.start();
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    Log.d(TAG, "SDCC > " + line);
                }
            }

            int exitCode = process.waitFor();
            String result = output.toString().trim();
            StringBuilder fullLog = new StringBuilder();
            fullLog.append("Comando: ").append(String.join(" ", command)).append("\n");
            fullLog.append("Código de salida: ").append(exitCode);
            if (!result.isEmpty()) {
                fullLog.append("\n").append(result);
            }
            if (exitCode != 0 && result.isEmpty()) {
                fullLog.append("\nError: SDCC termino con codigo ").append(exitCode).append(". Revisa Logcat para mas detalles.");
            }
            return fullLog.toString().trim();

        } catch (Exception e) {
            Log.e(TAG, "Error ejecutando SDCC: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    public List<String> getSetupIssues() {
        List<String> issues = new ArrayList<>();
        if (!new File(nativeLibDir, "libsdcc.so").exists()) issues.add("Falta libsdcc.so en jniLibs");
        if (!new File(nativeLibDir, "libcc1.so").exists()) issues.add("Falta libcc1.so en jniLibs");
        if (!new File(nativeLibDir, "libsdcpp.so").exists()) issues.add("Falta libsdcpp.so en jniLibs");
        if (!new File(sdccShareDir, "include").exists()) issues.add("Falta include de SDCC (usr/share/sdcc/include)");
        if (!new File(sdccShareDir, "lib").exists()) issues.add("Falta lib de SDCC (usr/share/sdcc/lib)");
        return issues;
    }

    /**
     * Crea enlaces simbolicos para que SDCC encuentre sus componentes internos.
     */
    private void setupSymlinks() {
        try {
            File usrDir = new File(workDir, "usr");
            File libexecBase = new File(usrDir, "libexec/sdcc/aarch64-unknown-linux-gnu/12.1.0");
            if (!libexecBase.exists()) libexecBase.mkdirs();

            File libexecGeneric = new File(usrDir, "libexec/sdcc");
            if (!libexecGeneric.exists()) libexecGeneric.mkdirs();

            File binDir = new File(usrDir, "bin");
            if (!binDir.exists()) binDir.mkdirs();

            String libcc1 = new File(nativeLibDir, "libcc1.so").getAbsolutePath();
            String libsdcpp = new File(nativeLibDir, "libsdcpp.so").getAbsolutePath();
            String libgpasm = new File(nativeLibDir, "libgpasm.so").getAbsolutePath();
            String libgplink = new File(nativeLibDir, "libgplink.so").getAbsolutePath();

            createSymlink(new File(libexecBase, "cc1"), libcc1);
            createSymlink(new File(libexecGeneric, "cc1"), libcc1);
            createSymlink(new File(binDir, "cc1"), libcc1);
            createSymlink(new File(binDir, "sdcc-cc1"), libcc1);
            createSymlink(new File(binDir, "sdcpp"), libsdcpp);
            createSymlink(new File(binDir, "sdcc-sdcpp"), libsdcpp);
            createSymlink(new File(binDir, "gpasm"), libgpasm);
            createSymlink(new File(binDir, "gplink"), libgplink);

            File libDir = new File(usrDir, "lib");
            if (!libDir.exists()) libDir.mkdirs();

            String targetZ = new File("/system/lib64/libz.so").exists() ? "/system/lib64/libz.so" : "/system/lib/libz.so";
            createSymlink(new File(libDir, "libz.so.1"), targetZ);

            File localZstd = new File(nativeLibDir, "libzstd.so");
            if (!localZstd.exists()) localZstd = new File(nativeLibDir, "libzstd.so.1");
            if (localZstd.exists()) {
                createSymlink(new File(libDir, "libzstd.so.1"), localZstd.getAbsolutePath());
            } else {
                Log.e(TAG, "ERROR CRITICO: libzstd local no encontrada en jniLibs.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al configurar symlinks: " + e.getMessage());
        }
    }

    private void createSymlink(File symlink, String targetPath) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                if (symlink.exists() || Files.isSymbolicLink(symlink.toPath())) {
                    symlink.delete();
                }
            } else {
                symlink.delete();
            }
            android.system.Os.symlink(targetPath, symlink.getAbsolutePath());
            Log.d(TAG, "Symlink creado: " + symlink.getName() + " -> " + targetPath);
        } catch (Exception e) {
            Log.e(TAG, "Error al crear enlace " + symlink.getName() + ": " + e.getMessage());
        }
    }
}
