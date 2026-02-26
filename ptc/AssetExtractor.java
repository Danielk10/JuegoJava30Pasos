package com.diamon.ptc;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Clase utilitaria para extraer archivos de assets al directorio interno de la
 * aplicacion.
 * Crea la estructura de subdirectorios exactamente como esta en assets.
 */
public class AssetExtractor {
    private static final String TAG = "AssetExtractor";
    private static final int BUFFER_SIZE = 8192;

    /**
     * Extrae recursivamente archivos desde assets al directorio de destino.
     * Solo copia archivos que no existan o sean mas antiguos.
     *
     * @param context   Contexto de la aplicacion
     * @param assetPath Ruta dentro de assets (ej:
     *                  "data/data/com.diamon.ptc/files/usr/share")
     * @param destDir   Directorio de destino (ej: context.getFilesDir())
     * @return true si la extraccion fue exitosa
     */
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

    /**
     * Copia un archivo individual desde assets.
     */
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

    /**
     * Verifica si los assets ya fueron extraidos.
     * Comprueba la existencia del directorio gputils.
     */
    public static boolean areAssetsExtracted(Context context) {
        File gpUtilsDir = new File(context.getFilesDir(), "usr/share/gputils");
        File sdccDir = new File(context.getFilesDir(), "usr/share/sdcc");
        File binDir = new File(context.getFilesDir(), "usr/bin");
        return gpUtilsDir.exists() && sdccDir.exists() && binDir.exists();
    }
}
