package com.diamon.tutorial.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Clase DatabaseHelper para gestionar la base de datos SQLite del tutorial
 * Maneja la creación, actualización y versionado de la base de datos
 * 
 * @author Daniel Diamon
 * @version 1.0
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String TAG = "DatabaseHelper";
    
    // Configuración de la base de datos
    private static final String DATABASE_NAME = "tutorial_game.db";
    private static final int DATABASE_VERSION = 2;
    
    // Tabla contenido - almacena el contenido educativo de los capítulos
    public static final String TABLE_CONTENIDO = "contenido";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CAPITULO = "capitulo";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_CODIGO = "codigo";
    public static final String COLUMN_EXPLICACION = "explicacion";
    public static final String COLUMN_IMAGEN_URL = "imagen_url";
    public static final String COLUMN_VIDEO_URL = "video_url";
    public static final String COLUMN_ORDEN = "orden";
    public static final String COLUMN_COMPLETADO = "completado";
    public static final String COLUMN_FECHA_CREACION = "fecha_creacion";
    public static final String COLUMN_FECHA_ACTUALIZACION = "fecha_actualizacion";
    
    // Tabla progreso - almacena el progreso del usuario
    public static final String TABLE_PROGRESO = "progreso";
    public static final String COLUMN_USUARIO_ID = "usuario_id";
    public static final String COLUMN_PUNTUACION = "puntuacion";
    public static final String COLUMN_TIEMPO_TOTAL = "tiempo_total";
    public static final String COLUMN_NIVEL_ACTUAL = "nivel_actual";
    
    // SQL para crear tabla contenido
    private static final String CREATE_TABLE_CONTENIDO = 
        "CREATE TABLE " + TABLE_CONTENIDO + " (" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_CAPITULO + " INTEGER NOT NULL, " +
        COLUMN_TITULO + " TEXT NOT NULL, " +
        COLUMN_DESCRIPCION + " TEXT, " +
        COLUMN_CODIGO + " TEXT, " +
        COLUMN_EXPLICACION + " TEXT, " +
        COLUMN_IMAGEN_URL + " TEXT, " +
        COLUMN_VIDEO_URL + " TEXT, " +
        COLUMN_ORDEN + " INTEGER DEFAULT 0, " +
        COLUMN_COMPLETADO + " INTEGER DEFAULT 0, " +
        COLUMN_FECHA_CREACION + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
        COLUMN_FECHA_ACTUALIZACION + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
        ")";
    
    // SQL para crear tabla progreso
    private static final String CREATE_TABLE_PROGRESO = 
        "CREATE TABLE " + TABLE_PROGRESO + " (" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_USUARIO_ID + " TEXT UNIQUE NOT NULL, " +
        COLUMN_PUNTUACION + " INTEGER DEFAULT 0, " +
        COLUMN_TIEMPO_TOTAL + " INTEGER DEFAULT 0, " +
        COLUMN_NIVEL_ACTUAL + " INTEGER DEFAULT 1, " +
        COLUMN_FECHA_CREACION + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
        COLUMN_FECHA_ACTUALIZACION + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
        ")";
    
    private static DatabaseHelper instance;
    
    /**
     * Constructor privado para implementar patrón Singleton
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    /**
     * Obtiene la instancia única de DatabaseHelper (Patrón Singleton)
     * @param context Contexto de la aplicación
     * @return Instancia única de DatabaseHelper
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creando base de datos...");
        
        try {
            // Crear tabla contenido
            db.execSQL(CREATE_TABLE_CONTENIDO);
            Log.d(TAG, "Tabla " + TABLE_CONTENIDO + " creada exitosamente");
            
            // Crear tabla progreso
            db.execSQL(CREATE_TABLE_PROGRESO);
            Log.d(TAG, "Tabla " + TABLE_PROGRESO + " creada exitosamente");
            
            // Insertar datos iniciales
            insertarDatosIniciales(db);
            
        } catch (Exception e) {
            Log.e(TAG, "Error al crear tablas: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Actualizando base de datos de versión " + oldVersion + " a " + newVersion);
        
        try {
            if (oldVersion < 2) {
                // Agregar nuevas columnas si no existen
                db.execSQL("ALTER TABLE " + TABLE_CONTENIDO + " ADD COLUMN " + 
                          COLUMN_IMAGEN_URL + " TEXT");
                db.execSQL("ALTER TABLE " + TABLE_CONTENIDO + " ADD COLUMN " + 
                          COLUMN_VIDEO_URL + " TEXT");
                Log.d(TAG, "Columnas agregadas en actualización");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error en actualización: " + e.getMessage(), e);
            // Si falla la actualización, recrear tablas
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTENIDO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESO);
            onCreate(db);
        }
    }
    
    /**
     * Inserta los datos iniciales en la base de datos
     */
    private void insertarDatosIniciales(SQLiteDatabase db) {
        Log.d(TAG, "Insertando datos iniciales...");
        
        try {
            // Datos del Capítulo 1: Introducción a Java
            db.execSQL("INSERT INTO " + TABLE_CONTENIDO + " (" +
                COLUMN_CAPITULO + ", " + COLUMN_TITULO + ", " + COLUMN_DESCRIPCION + ", " + 
                COLUMN_CODIGO + ", " + COLUMN_EXPLICACION + ", " + COLUMN_ORDEN + ") VALUES (" +
                "1, 'Introducción a Java', 'Conceptos básicos de programación en Java', " +
                "'public class HolaMundo { public static void main(String[] args) { System.out.println(\"Hola Mundo\"); } }', " +
                "'Este es el programa más básico en Java. Imprime \"Hola Mundo\" en la consola.', 1)");
            
            // Datos del Capítulo 2: Variables y Tipos de Datos
            db.execSQL("INSERT INTO " + TABLE_CONTENIDO + " (" +
                COLUMN_CAPITULO + ", " + COLUMN_TITULO + ", " + COLUMN_DESCRIPCION + ", " + 
                COLUMN_CODIGO + ", " + COLUMN_EXPLICACION + ", " + COLUMN_ORDEN + ") VALUES (" +
                "2, 'Variables y Tipos de Datos', 'Aprende sobre variables en Java', " +
                "'int numero = 10; String texto = \"Hola\"; boolean esVerdadero = true;', " +
                "'En Java existen diferentes tipos de datos: enteros (int), texto (String), booleanos (boolean), etc.', 1)");
            
            // Insertar progreso inicial
            db.execSQL("INSERT INTO " + TABLE_PROGRESO + " (" +
                COLUMN_USUARIO_ID + ", " + COLUMN_NIVEL_ACTUAL + ") VALUES ('default', 1)");
            
            Log.d(TAG, "Datos iniciales insertados exitosamente");
            
        } catch (Exception e) {
            Log.e(TAG, "Error al insertar datos iniciales: " + e.getMessage(), e);
        }
    }
    
    /**
     * Verifica si la tabla contenido existe y tiene datos
     */
    public boolean verificarIntegridadBaseDatos() {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            // Verificar si existe la tabla
            db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CONTENIDO, null).close();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error en verificación de integridad: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Reinicia la base de datos completamente
     */
    public void reiniciarBaseDatos() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTENIDO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESO);
            onCreate(db);
            Log.d(TAG, "Base de datos reiniciada exitosamente");
        } catch (Exception e) {
            Log.e(TAG, "Error al reiniciar base de datos: " + e.getMessage(), e);
        }
    }
}