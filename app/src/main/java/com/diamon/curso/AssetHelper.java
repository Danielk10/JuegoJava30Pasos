package com.diamon.curso;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AssetHelper {
    private static final String TAG = "AssetHelper";
    private static final int BUFFER_SIZE = 8192;

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
}
