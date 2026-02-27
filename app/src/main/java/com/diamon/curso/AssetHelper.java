package com.diamon.curso;

import android.content.Context;
import android.content.res.AssetManager;
import android.system.Os;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AssetHelper {
    private static final String TAG = "AssetHelper";
    private static final int BUFFER_SIZE = 8192;
    private static volatile String cachedAssetRuntimeRoot;
    private static final String COMPILED_RUNTIME_ROOT = "data/data/com.diamon.curso/files/usr";

    /**
     * Prepara runtime de assets una sola vez. Si ya existen, no vuelve a recorrer todo el árbol;
     * solamente repara archivos críticos faltantes.
     */
    public static boolean ensureRuntimeReady(Context context) {
        String runtimeRoot = resolveAssetRuntimeRoot(context);
        if (runtimeRoot == null) {
            Log.e(TAG, "No se encontró ruta runtime en assets (data/data/*/files/usr).");
            return false;
        }

        File usrDir = new File(context.getFilesDir(), "usr");
        boolean alreadyExtracted = areAssetsExtracted(context);

        if (!alreadyExtracted) {
            if (!extractAssets(context, runtimeRoot, usrDir)) {
                return false;
            }
            // Tras extracción completa, preferimos enlazar ejecutables/librerías hacia jniLibs.
            return ensureNativeToolLinks(context);
        }

        // Reparación mínima de recursos críticos del runtime compilado (no ejecutables).
        AssetManager assetManager = context.getAssets();
        File pciIdsTarget = new File(usrDir, "share/pci.ids.gz");
        if (!pciIdsTarget.exists() && !copyCriticalPciIds(assetManager, runtimeRoot, pciIdsTarget)) {
            return false;
        }
        return ensureNativeToolLinks(context);
    }

    public static String getResolvedRuntimeRoot(Context context) {
        return resolveAssetRuntimeRoot(context);
    }

    private static String resolveAssetRuntimeRoot(Context context) {
        if (cachedAssetRuntimeRoot != null) {
            return cachedAssetRuntimeRoot;
        }

        AssetManager assetManager = context.getAssets();
        try {
            // 1) Prioridad absoluta: ruta exacta de compilación documentada.
            String[] exactChildren = assetManager.list(COMPILED_RUNTIME_ROOT);
            if (exactChildren != null && exactChildren.length > 0) {
                cachedAssetRuntimeRoot = COMPILED_RUNTIME_ROOT;
                Log.i(TAG, "Runtime root de assets detectado (exacto): " + cachedAssetRuntimeRoot);
                return cachedAssetRuntimeRoot;
            }

            String[] pkgCandidates = assetManager.list("data/data");
            if (pkgCandidates == null || pkgCandidates.length == 0) {
                return null;
            }

            String expectedPkg = context.getPackageName();
            List<String> ordered = new ArrayList<>();
            // Priorizamos el package real de la app
            ordered.add(expectedPkg);
            for (String candidate : pkgCandidates) {
                if (candidate != null && !candidate.equals(expectedPkg)) {
                    ordered.add(candidate);
                }
            }

            for (String pkg : ordered) {
                if (pkg == null || pkg.isEmpty()) {
                    continue;
                }
                String candidateRoot = "data/data/" + pkg + "/files/usr";
                String[] children = assetManager.list(candidateRoot);
                if (children != null && children.length > 0) {
                    cachedAssetRuntimeRoot = candidateRoot;
                    Log.i(TAG, "Runtime root de assets detectado: " + candidateRoot);
                    return cachedAssetRuntimeRoot;
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "No se pudo resolver runtime root de assets: " + e.getMessage());
        }
        return null;
    }

    public static boolean extractAssets(Context context, String assetPath, File destDir) {
        AssetManager assetManager = context.getAssets();

        try {
            String[] files = assetManager.list(assetPath);

            if (files == null || files.length == 0) {
                // Es un archivo, copiarlo
                return copyAssetFile(assetManager, assetPath, destDir);
            } else {
                // Es un directorio, crearlo y procesar recursivamente
                if (!destDir.exists() && !destDir.mkdirs()) {
                    Log.e(TAG, "No se pudo crear directorio: " + destDir.getAbsolutePath());
                    return false;
                }

                for (String fileName : files) {
                    if (fileName == null || fileName.isEmpty())
                        continue;

                    String childAssetPath = assetPath + "/" + fileName;
                    File childDestDir = new File(destDir, fileName);

                    // Verificar si es un directorio o archivo
                    String[] subFiles = assetManager.list(childAssetPath);
                    if (subFiles != null && subFiles.length > 0) {
                        // Es un subdirectorio
                        if (!extractAssets(context, childAssetPath, childDestDir)) {
                            return false;
                        }
                    } else {
                        // Es un archivo
                        if (!copyAssetFile(assetManager, childAssetPath, destDir)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error extrayendo assets: " + e.getMessage());
            return false;
        }
    }

    private static boolean copyAssetFile(AssetManager assetManager, String assetPath, File destDir) {
        String fileName = assetPath.substring(assetPath.lastIndexOf('/') + 1);
        if (assetPath.endsWith("/share/pci.ids")) {
            // En algunos empaquetados Android, pci.ids.gz puede quedar listado como pci.ids.
            // Conservamos el nombre runtime esperado por pciutils/flashrom.
            fileName = "pci.ids.gz";
        }
        File destFile = new File(destDir, fileName);

        // Si el archivo ya existe, omitir
        if (destFile.exists()) {
            return true;
        }

        // Asegurar que el directorio padre existe
        if (!destDir.exists() && !destDir.mkdirs()) {
            Log.e(TAG, "No se pudo crear directorio: " + destDir.getAbsolutePath());
            return false;
        }

        try (InputStream in = assetManager.open(assetPath);
                OutputStream out = new FileOutputStream(destFile)) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
            if (assetPath.contains("/bin/") || assetPath.contains("/sbin/")) {
                //noinspection ResultOfMethodCallIgnored
                destFile.setExecutable(true, true);
            }
            Log.d(TAG, "Copiado: " + destFile.getAbsolutePath());
            return true;

        } catch (IOException e) {
            Log.e(TAG, "Error copiando " + assetPath + ": " + e.getMessage());
            return false;
        }
    }

    public static boolean areAssetsExtracted(Context context) {
        // En flashrom el componente mas importante a nivel de lectura suele ser share o
        // sbin
        // Pero como los de sbin/bin los pasamos a jniLibs, revisaremos si se extrajo
        // share
        File shareDir = new File(context.getFilesDir(), "usr/share");
        return shareDir.exists() && shareDir.isDirectory() && shareDir.list() != null && shareDir.list().length > 0;
    }

    private static boolean copyCriticalPciIds(AssetManager assetManager, String runtimeRoot, File target) {
        String[] candidates = new String[] {
                runtimeRoot + "/share/pci.ids.gz",
                runtimeRoot + "/share/pci.ids"
        };

        File parent = target.getParentFile();
        if (parent == null) {
            return false;
        }

        for (String candidate : candidates) {
            try (InputStream ignored = assetManager.open(candidate)) {
                Log.i(TAG, "Copiando pci.ids crítico desde asset: " + candidate);
                return copyAssetFile(assetManager, candidate, parent);
            } catch (IOException ignored) {
                // Intentar siguiente candidato
            }
        }

        Log.e(TAG, "No se encontró pci.ids(.gz) en assets para reconstruir runtime crítico.");
        return false;
    }

    private static boolean ensureNativeToolLinks(Context context) {
        File filesDir = context.getFilesDir();
        File nativeLibDir = new File(context.getApplicationInfo().nativeLibraryDir);
        File usrBin = new File(filesDir, "usr/bin");
        File usrSbin = new File(filesDir, "usr/sbin");
        File usrLib = new File(filesDir, "usr/lib");
        File pythonSitePackages = new File(usrLib, "python3.12/site-packages");

        if (!usrBin.exists()) usrBin.mkdirs();
        if (!usrSbin.exists()) usrSbin.mkdirs();
        if (!usrLib.exists()) usrLib.mkdirs();
        if (!pythonSitePackages.exists()) pythonSitePackages.mkdirs();

        // Ejecutables/librerías principales se resuelven desde jniLibs con nombres clásicos vía symlink.
        boolean ok = true;
        ok &= linkTool(new File(usrSbin, "flashrom"), new File(nativeLibDir, "libflashrom_bin.so"));
        ok &= linkTool(new File(usrBin, "lspci"), new File(nativeLibDir, "liblspci.so"));
        ok &= linkTool(new File(usrSbin, "setpci"), new File(nativeLibDir, "libsetpci.so"));
        ok &= linkTool(new File(usrSbin, "pcilmr"), new File(nativeLibDir, "libpcilmr.so"));
        ok &= linkTool(new File(usrSbin, "update-pciids"), new File(nativeLibDir, "libupdate-pciids.so"));
        ok &= linkTool(new File(usrBin, "ftdi_eeprom"), new File(nativeLibDir, "libftdi_eeprom.so"));
        ok &= linkTool(new File(usrBin, "libftdi1-config"), new File(nativeLibDir, "liblibftdi1-config.so"));

        // Sonames esperados por binarios/nativas: apuntan al nombre Android copiable (.so).
        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libflashrom.so");
        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libflashrom.so.1");
        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libflashrom.so.1.0.0");

        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libpci.so");
        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libpci.so.3");
        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libpci.so.3.14.0");

        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libftdi1.so");
        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libftdi1.so.2");
        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libftdi1.so.2.6.0");

        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libftdipp1.so");
        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libftdipp1.so.3");
        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libftdipp1.so.2.6.0");

        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libusb-1.0.so");
        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libjaylink.so");
        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libcrypto.so.3");
        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libssl.so.3");
        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libz.so.1");
        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libconfuse.so");
        ok &= linkRuntimeSoname(usrLib, nativeLibDir, "libc++_shared.so");
        // Extensión Python: nombre Android en jniLibs y nombre original vía symlink en site-packages.
        linkTool(new File(pythonSitePackages, "_pyftdi1.so"), new File(nativeLibDir, "libpyftdi1.so"));

        // Validación mínima de dependencias críticas para herramientas principales.
        ok &= ensurePresent(new File(usrLib, "libcrypto.so.3"));
        ok &= ensurePresent(new File(usrLib, "libssl.so.3"));
        ok &= ensurePresent(new File(usrLib, "libconfuse.so"));
        ok &= ensurePresent(new File(usrLib, "libz.so.1"));
        ok &= ensurePresent(new File(usrLib, "libc++_shared.so"));

        return ok;
    }

    private static boolean linkRuntimeSoname(File usrLib, File nativeLibDir, String runtimeSoname) {
        File linkPath = new File(usrLib, runtimeSoname);
        File source = resolveNativeLibrary(nativeLibDir, runtimeSoname);
        if (source == null) {
            Log.w(TAG, "No se encontró librería para soname runtime: " + runtimeSoname);
            return false;
        }
        return linkTool(linkPath, source);
    }

    private static File resolveNativeLibrary(File nativeLibDir, String runtimeSoname) {
        for (String candidate : getNativeCandidates(runtimeSoname)) {
            File file = new File(nativeLibDir, candidate);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    private static List<String> getNativeCandidates(String runtimeSoname) {
        Set<String> candidates = new LinkedHashSet<>();
        candidates.add(runtimeSoname);

        int soMarker = runtimeSoname.indexOf(".so.");
        if (soMarker > 0) {
            String base = runtimeSoname.substring(0, soMarker);
            String suffix = runtimeSoname.substring(soMarker + 4).replace('.', '_');
            candidates.add(base + "_" + suffix + ".so");
        }

        return new ArrayList<>(candidates);
    }

    private static boolean ensurePresent(File file) {
        if (!file.exists()) {
            Log.w(TAG, "Falta dependencia crítica: " + file.getAbsolutePath());
            return false;
        }
        return true;
    }

    private static boolean linkTool(File linkPath, File target) {
        if (!target.exists()) {
            Log.w(TAG, "Target no existe para enlace: " + target.getAbsolutePath());
            return false;
        }

        try {
            if (linkPath.exists()) {
                linkPath.delete();
            }
            Os.symlink(target.getAbsolutePath(), linkPath.getAbsolutePath());
            return true;
        } catch (Exception e) {
            Log.w(TAG, "Symlink falló para " + linkPath.getName() + ": " + e.getMessage() + ". Intentando copia.");
            return copyFile(target, linkPath);
        }
    }

    private static boolean copyFile(File source, File dest) {
        File parent = dest.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            return false;
        }
        try (InputStream in = new java.io.FileInputStream(source);
                OutputStream out = new FileOutputStream(dest)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
            // Si es herramienta de ejecución, forzar permiso de ejecución.
            File binParent = dest.getParentFile();
            String parentName = binParent != null ? binParent.getName() : "";
            if ("bin".equals(parentName) || "sbin".equals(parentName)) {
                //noinspection ResultOfMethodCallIgnored
                dest.setExecutable(true, true);
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error copiando archivo de fallback: " + e.getMessage());
            return false;
        }
    }
}
