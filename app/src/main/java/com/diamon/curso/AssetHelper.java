package com.diamon.curso;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class AssetHelper {
    private static final String TAG = "AssetHelper";

    public static void copyAssets(Context context) {
        File filesDir = context.getFilesDir();
        File usrDir = new File(filesDir, "usr");

        if (!usrDir.exists()) {
            boolean created = usrDir.mkdirs();
            Log.d(TAG, "usr directory created: " + created);
        }

        // Always attempt copy to ensure any missing or updated files are written.
        copyAssetFolder(context, "usr", usrDir.getAbsolutePath());
    }

    private static boolean copyAssetFolder(Context context, String srcName, String dstName) {
        try {
            boolean res = true;
            String[] files = context.getAssets().list(srcName);
            if (files == null || files.length == 0) {
                // If the array is empty, it's a file, not a directory.
                return copyAssetFile(context, srcName, dstName);
            } else {
                File dir = new File(dstName);
                if (!dir.exists()) {
                    res = dir.mkdirs();
                    if (!res) {
                        Log.e(TAG, "Failed to create directory: " + dstName);
                        return false;
                    }
                }
                for (String file : files) {
                    // Skip any weird empty strings
                    if (file == null || file.isEmpty())
                        continue;
                    res &= copyAssetFolder(context, srcName + "/" + file, dstName + "/" + file);
                }
                return res;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error copying assets directory " + srcName, e);
            return false;
        }
    }

    private static boolean copyAssetFile(Context context, String srcName, String dstName) {
        try {
            File outFile = new File(dstName);

            // For this basic copy, we won't overwrite existing files to save time on each
            // startup,
            // unless strictly needed. But executables must be marked +x
            if (!outFile.exists()) {
                InputStream in = context.getAssets().open(srcName);
                OutputStream out = new FileOutputStream(dstName);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                out.flush();
                out.close();
                Log.d(TAG, "Copied file: " + dstName);
            }

            // Mark bin/ and sbin/ files as executable
            if (dstName.contains("/bin/") || dstName.contains("/sbin/")) {
                outFile.setExecutable(true, false);
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error copying asset file " + srcName, e);
            return false;
        }
    }
}
