package com.diamon.ptc;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clase para ejecutar los binarios de GPUTILS (gpasm, gpdasm, gplink, etc.).
 */
public class GpUtilsExecutor {
    private static final String TAG = "GpUtilsExecutor";

    private final File workDir;
    private final File nativeLibDir;
    private final File gpUtilsShareDir;

    public GpUtilsExecutor(Context context) {
        this.workDir = context.getFilesDir();
        this.nativeLibDir = new File(context.getApplicationInfo().nativeLibraryDir);
        this.gpUtilsShareDir = new File(workDir, "usr/share/gputils");
    }

    public String executeGpasm(String... args) {
        return executeBinary(workDir, "gpasm", args);
    }

    public String executeGpasm(File workingDir, String... args) {
        return executeBinary(workingDir, "gpasm", args);
    }

    public String executeGpdasm(String... args) {
        return executeBinary(workDir, "gpdasm", args);
    }

    public String executeGplink(String... args) {
        return executeBinary(workDir, "gplink", args);
    }

    public String executeGplib(String... args) {
        return executeBinary(workDir, "gplib", args);
    }

    public String executeBinary(String binaryName, String... args) {
        return executeBinary(workDir, binaryName, args);
    }

    public String executeBinary(File workingDir, String binaryName, String... args) {
        File binaryFile = new File(nativeLibDir, "lib" + binaryName + ".so");

        if (!binaryFile.exists()) {
            String[] files = nativeLibDir.list();
            String available = files != null ? String.join(", ", files) : "ninguno";
            Log.e(TAG, "Binario no encontrado: " + binaryFile.getAbsolutePath());
            return "Error: No se encontro el binario " + binaryFile.getAbsolutePath() + "\nDisponibles: " + available;
        }

        List<String> command = new ArrayList<>();
        command.add(binaryFile.getAbsolutePath());
        for (String arg : args) {
            command.add(arg);
        }

        Log.d(TAG, "Ejecutando: " + String.join(" ", command));

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(workingDir);
            pb.redirectErrorStream(true);

            Map<String, String> env = pb.environment();
            env.put("GPUTILS_HEADER_PATH", new File(gpUtilsShareDir, "header").getAbsolutePath());
            env.put("GPUTILS_LKR_PATH", new File(gpUtilsShareDir, "lkr").getAbsolutePath());
            env.put("LD_LIBRARY_PATH", nativeLibDir.getAbsolutePath() + ":" + new File(workDir, "usr/lib").getAbsolutePath());

            String path = env.get("PATH");
            String binPath = new File(workDir, "usr/bin").getAbsolutePath();
            env.put("PATH", binPath + ":" + nativeLibDir.getAbsolutePath() + (path != null ? ":" + path : ""));

            Process process = pb.start();
            StringBuilder output = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    Log.d(TAG, binaryName + " > " + line);
                }
            }

            int exitCode = process.waitFor();
            Log.d(TAG, "Codigo de salida: " + exitCode);

            String result = output.toString().trim();
            StringBuilder fullLog = new StringBuilder();
            fullLog.append("Comando: ").append(String.join(" ", command)).append("\n");
            fullLog.append("Código de salida: ").append(exitCode);
            if (!result.isEmpty()) {
                fullLog.append("\n").append(result);
            }
            return fullLog.toString().trim();

        } catch (Exception e) {
            Log.e(TAG, "Error ejecutando " + binaryName + ": " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    public List<String> getSetupIssues() {
        List<String> issues = new ArrayList<>();
        if (!new File(nativeLibDir, "libgpasm.so").exists()) {
            issues.add("Falta libgpasm.so en jniLibs");
        }
        if (!new File(gpUtilsShareDir, "header").exists()) {
            issues.add("Falta carpeta de headers gputils (usr/share/gputils/header)");
        }
        if (!new File(gpUtilsShareDir, "lkr").exists()) {
            issues.add("Falta carpeta lkr gputils (usr/share/gputils/lkr)");
        }
        return issues;
    }
}
