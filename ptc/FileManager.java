package com.diamon.ptc;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Clase utilitaria para gestionar archivos internos y exportar a la carpeta
 * pública de Descargas.
 */
public class FileManager {
    private static final String TAG = "FileManager";

    /**
     * Guarda una cadena de texto en un archivo en el directorio interno de la app.
     */
    public static boolean writeInternalFile(Context context, String fileName, String content) {
        File file = new File(context.getFilesDir(), fileName);
        try (FileOutputStream vos = new FileOutputStream(file)) {
            vos.write(content.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error escribiendo archivo interno: " + fileName, e);
            return false;
        }
    }

    /**
     * Lee el contenido de un archivo del directorio interno.
     */
    public static String readInternalFile(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        if (!file.exists())
            return "";

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            return new String(data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Log.e(TAG, "Error leyendo archivo interno: " + fileName, e);
            return "";
        }
    }

    /**
     * Exporta un archivo del directorio interno a la carpeta de Descargas del
     * sistema.
     * Compatible con Android 10+ (Scoped Storage).
     */
    public static boolean exportToDownloads(Context context, String fileName) {
        File sourceFile = new File(context.getFilesDir(), fileName);
        if (!sourceFile.exists()) {
            Log.e(TAG, "Archivo de origen no existe: " + fileName);
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
            values.put(MediaStore.Downloads.MIME_TYPE, "application/octet-stream");
            values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/C-PIC");

            Uri externalUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
            Uri fileUri = context.getContentResolver().insert(externalUri, values);

            if (fileUri != null) {
                try (OutputStream out = context.getContentResolver().openOutputStream(fileUri);
                        InputStream in = new FileInputStream(sourceFile)) {
                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                    return true;
                } catch (IOException e) {
                    Log.e(TAG, "Error exportando via MediaStore", e);
                }
            }
        } else {
            // Para versiones antiguas de Android
            File destFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "C-PIC");
            if (!destFolder.exists() && !destFolder.mkdirs())
                return false;

            File destFile = new File(destFolder, fileName);
            try (InputStream in = new FileInputStream(sourceFile);
                    OutputStream out = new FileOutputStream(destFile)) {
                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error exportando via File API", e);
            }
        }
        return false;
    }


    public static boolean writeToFile(File file, String content) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error escribiendo archivo: " + file.getAbsolutePath(), e);
            return false;
        }
    }

    public static String readFile(File file) {
        if (!file.exists()) return "";
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            if (fis.read(data) < 0) return "";
            return new String(data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Log.e(TAG, "Error leyendo archivo: " + file.getAbsolutePath(), e);
            return "";
        }
    }

}