package com.diamon.tutorial.archivo.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME =
            "contenido.db"; // Nombre de tu base de datos en 'assets'
    private static final String DATABASE_PATH =
            "/data/data/com.diamon.curso/databases/"; // Ruta del almacenamiento interno
    private Context context;
    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // No necesitamos crear tablas, ya que la base de datos viene preconfigurada
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Lógica de actualización si es necesario
    }

    // Método para verificar si la base de datos ya ha sido copiada al almacenamiento interno
    private boolean checkDatabaseExists() {
        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    // Método para copiar la base de datos desde 'assets'
    private void copyDatabase() throws IOException {
        InputStream input = context.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream output = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        input.close();
    }

    // Método para abrir la base de datos
    public void openDatabase() throws SQLiteException {
        String path = DATABASE_PATH + DATABASE_NAME;
        database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    // Método para copiar la base de datos desde 'assets' si aún no existe, y luego abrirla
    public void initializeDatabase() {
        if (!checkDatabaseExists()) {
            this.getReadableDatabase(); // Crea la carpeta de bases de datos si no existe
            try {
                copyDatabase();
                
               // Toast.makeText(context, "Base de datos copiada desde assets.", Toast.LENGTH_SHORT).show();
									
                  } catch (IOException e) {
                
                Toast.makeText(context, "Error al copiar la base de datos", Toast.LENGTH_SHORT).show();
				
                throw new Error("Error al copiar la base de datos");
            }
        }
        openDatabase(); // Abre la base de datos
    }

    // Método para cerrar la base de datos
    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }
}
