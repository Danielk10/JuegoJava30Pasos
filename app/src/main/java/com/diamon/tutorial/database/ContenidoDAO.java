package com.diamon.tutorial.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.diamon.tutorial.models.ContenidoTutorial;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para gestionar operaciones CRUD
 * de la tabla contenido en la base de datos SQLite
 * 
 * Implementa el patrón DAO para abstraer el acceso a datos
 * y proporcionar una interfaz limpia para operaciones de base de datos
 * 
 * @author Daniel Diamon
 * @version 1.0
 */
public class ContenidoDAO {
    
    private static final String TAG = "ContenidoDAO";
    private DatabaseHelper dbHelper;
    
    /**
     * Constructor que inicializa el helper de base de datos
     * @param context Contexto de la aplicación
     */
    public ContenidoDAO(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }
    
    /**
     * Inserta un nuevo contenido en la base de datos
     * @param contenido Objeto ContenidoTutorial a insertar
     * @return ID del registro insertado, -1 si hay error
     */
    public long insertarContenido(ContenidoTutorial contenido) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        try {
            values.put(DatabaseHelper.COLUMN_CAPITULO, contenido.getCapitulo());
            values.put(DatabaseHelper.COLUMN_TITULO, contenido.getTitulo());
            values.put(DatabaseHelper.COLUMN_DESCRIPCION, contenido.getDescripcion());
            values.put(DatabaseHelper.COLUMN_CODIGO, contenido.getCodigo());
            values.put(DatabaseHelper.COLUMN_EXPLICACION, contenido.getExplicacion());
            values.put(DatabaseHelper.COLUMN_IMAGEN_URL, contenido.getImagenUrl());
            values.put(DatabaseHelper.COLUMN_VIDEO_URL, contenido.getVideoUrl());
            values.put(DatabaseHelper.COLUMN_ORDEN, contenido.getOrden());
            values.put(DatabaseHelper.COLUMN_COMPLETADO, contenido.isCompletado() ? 1 : 0);
            
            long id = db.insert(DatabaseHelper.TABLE_CONTENIDO, null, values);
            Log.d(TAG, "Contenido insertado con ID: " + id);
            return id;
            
        } catch (Exception e) {
            Log.e(TAG, "Error al insertar contenido: " + e.getMessage(), e);
            return -1;
        } finally {
            db.close();
        }
    }
    
    /**
     * Obtiene todo el contenido de un capítulo específico
     * @param numeroCapitulo Número del capítulo
     * @return Lista de contenidos del capítulo
     */
    public List<ContenidoTutorial> obtenerContenidoPorCapitulo(int numeroCapitulo) {
        List<ContenidoTutorial> lista = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        
        try {
            db = dbHelper.getReadableDatabase();
            
            // Verificar que la tabla existe antes de hacer la consulta
            if (!dbHelper.verificarIntegridadBaseDatos()) {
                Log.w(TAG, "La tabla contenido no existe, reiniciando base de datos...");
                dbHelper.reiniciarBaseDatos();
                db = dbHelper.getReadableDatabase();
            }
            
            String selection = DatabaseHelper.COLUMN_CAPITULO + " = ?";
            String[] selectionArgs = {String.valueOf(numeroCapitulo)};
            String orderBy = DatabaseHelper.COLUMN_ORDEN + " ASC";
            
            cursor = db.query(
                DatabaseHelper.TABLE_CONTENIDO,
                null,
                selection,
                selectionArgs,
                null,
                null,
                orderBy
            );
            
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ContenidoTutorial contenido = crearContenidoDesdeCursor(cursor);
                    lista.add(contenido);
                } while (cursor.moveToNext());
            }
            
            Log.d(TAG, "Obtenidos " + lista.size() + " elementos para capítulo " + numeroCapitulo);
            
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener contenido del capítulo " + numeroCapitulo + ": " + e.getMessage(), e);
            // En caso de error, intentar reconstruir la base de datos
            if (dbHelper != null) {
                dbHelper.reiniciarBaseDatos();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        
        return lista;
    }
    
    /**
     * Obtiene todo el contenido disponible en la base de datos
     * @return Lista completa de contenidos
     */
    public List<ContenidoTutorial> obtenerTodoElContenido() {
        List<ContenidoTutorial> lista = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        
        try {
            db = dbHelper.getReadableDatabase();
            
            // Verificar integridad antes de consultar
            if (!dbHelper.verificarIntegridadBaseDatos()) {
                Log.w(TAG, "Problemas de integridad detectados, reiniciando...");
                dbHelper.reiniciarBaseDatos();
                db = dbHelper.getReadableDatabase();
            }
            
            String orderBy = DatabaseHelper.COLUMN_CAPITULO + " ASC, " + 
                           DatabaseHelper.COLUMN_ORDEN + " ASC";
            
            cursor = db.query(
                DatabaseHelper.TABLE_CONTENIDO,
                null,
                null,
                null,
                null,
                null,
                orderBy
            );
            
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ContenidoTutorial contenido = crearContenidoDesdeCursor(cursor);
                    lista.add(contenido);
                } while (cursor.moveToNext());
            }
            
            Log.d(TAG, "Total de contenidos obtenidos: " + lista.size());
            
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener todo el contenido: " + e.getMessage(), e);
            if (dbHelper != null) {
                dbHelper.reiniciarBaseDatos();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        
        return lista;
    }
    
    /**
     * Actualiza el estado de completado de un contenido
     * @param id ID del contenido
     * @param completado Nuevo estado de completado
     * @return true si se actualizó correctamente
     */
    public boolean actualizarEstadoCompletado(long id, boolean completado) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        try {
            values.put(DatabaseHelper.COLUMN_COMPLETADO, completado ? 1 : 0);
            values.put(DatabaseHelper.COLUMN_FECHA_ACTUALIZACION, "CURRENT_TIMESTAMP");
            
            String whereClause = DatabaseHelper.COLUMN_ID + " = ?";
            String[] whereArgs = {String.valueOf(id)};
            
            int rowsAffected = db.update(
                DatabaseHelper.TABLE_CONTENIDO,
                values,
                whereClause,
                whereArgs
            );
            
            Log.d(TAG, "Actualizado estado completado para ID " + id + ": " + completado);
            return rowsAffected > 0;
            
        } catch (Exception e) {
            Log.e(TAG, "Error al actualizar estado completado: " + e.getMessage(), e);
            return false;
        } finally {
            db.close();
        }
    }
    
    /**
     * Obtiene un contenido específico por su ID
     * @param id ID del contenido
     * @return ContenidoTutorial o null si no se encuentra
     */
    public ContenidoTutorial obtenerContenidoPorId(long id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        
        try {
            db = dbHelper.getReadableDatabase();
            
            String selection = DatabaseHelper.COLUMN_ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};
            
            cursor = db.query(
                DatabaseHelper.TABLE_CONTENIDO,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
            );
            
            if (cursor != null && cursor.moveToFirst()) {
                return crearContenidoDesdeCursor(cursor);
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener contenido por ID " + id + ": " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        
        return null;
    }
    
    /**
     * Obtiene el número total de capítulos disponibles
     * @return Número de capítulos
     */
    public int obtenerNumeroCapitulos() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        
        try {
            db = dbHelper.getReadableDatabase();
            
            cursor = db.rawQuery(
                "SELECT COUNT(DISTINCT " + DatabaseHelper.COLUMN_CAPITULO + ") FROM " + 
                DatabaseHelper.TABLE_CONTENIDO, null
            );
            
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error al obtener número de capítulos: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        
        return 0;
    }
    
    /**
     * Crea un objeto ContenidoTutorial desde un cursor de base de datos
     * @param cursor Cursor con los datos
     * @return Objeto ContenidoTutorial
     */
    private ContenidoTutorial crearContenidoDesdeCursor(Cursor cursor) {
        ContenidoTutorial contenido = new ContenidoTutorial();
        
        contenido.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
        contenido.setCapitulo(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CAPITULO)));
        contenido.setTitulo(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITULO)));
        contenido.setDescripcion(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPCION)));
        contenido.setCodigo(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CODIGO)));
        contenido.setExplicacion(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EXPLICACION)));
        contenido.setImagenUrl(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGEN_URL)));
        contenido.setVideoUrl(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_VIDEO_URL)));
        contenido.setOrden(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ORDEN)));
        contenido.setCompletado(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_COMPLETADO)) == 1);
        
        return contenido;
    }
    
    /**
     * Elimina todo el contenido de la base de datos
     * (Usar con precaución)
     */
    public void limpiarTodoElContenido() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.delete(DatabaseHelper.TABLE_CONTENIDO, null, null);
            Log.d(TAG, "Todo el contenido ha sido eliminado");
        } catch (Exception e) {
            Log.e(TAG, "Error al limpiar contenido: " + e.getMessage(), e);
        } finally {
            db.close();
        }
    }
}