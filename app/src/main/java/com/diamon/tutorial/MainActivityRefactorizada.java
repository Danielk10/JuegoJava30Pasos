package com.diamon.tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.util.Log;

import com.diamon.tutorial.TutorialManager;
import com.diamon.tutorial.database.DatabaseHelper;

/**
 * MainActivity refactorizada que demuestra la implementación de la nueva arquitectura
 * 
 * Esta clase reemplaza la MainActivity original con:
 * - Manejo robusto de errores SQLite
 * - Arquitectura limpia y mantenible
 * - Interfaz de usuario mejorada
 * - Seguimiento de progreso
 * 
 * @author Daniel Diamon
 * @version 2.0
 */
public class MainActivityRefactorizada extends Activity {
    
    private static final String TAG = "MainActivityRefactorizada";
    
    // Componentes de UI
    private LinearLayout layoutPrincipal;
    private TextView tituloApp;
    private TextView estadoBaseDatos;
    private ProgressBar progresoGeneral;
    private TextView textoProgreso;
    private ExpandableListView expandableListView;
    private Button botonReiniciar;
    private Button botonVerificarIntegridad;
    
    // Gestores
    private TutorialManager tutorialManager;
    private DatabaseHelper databaseHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            Log.d(TAG, "Iniciando MainActivity refactorizada...");
            
            // Crear la interfaz de usuario
            crearInterfazUsuario();
            
            // Inicializar gestores
            inicializarGestores();
            
            // Configurar la lista de capítulos
            configurarListaCapitulos();
            
            // Actualizar estado inicial
            actualizarEstadoUI();
            
            Log.d(TAG, "MainActivity inicializada correctamente");
            
        } catch (Exception e) {
            Log.e(TAG, "Error crítico al inicializar MainActivity: " + e.getMessage(), e);
            mostrarErrorCritico(e);
        }
    }
    
    /**
     * Crea la interfaz de usuario programaticamente
     */
    private void crearInterfazUsuario() {
        // Layout principal
        layoutPrincipal = new LinearLayout(this);
        layoutPrincipal.setOrientation(LinearLayout.VERTICAL);
        layoutPrincipal.setBackgroundColor(0xFFF5F5F5); // Fondo gris claro
        
        // Padding del layout principal
        int padding = dpToPx(16);
        layoutPrincipal.setPadding(padding, padding, padding, padding);
        
        // Título de la aplicación
        tituloApp = new TextView(this);
        tituloApp.setText("Juego Java 30 Pasos - Tutorial Refactorizado");
        tituloApp.setTextSize(20);
        tituloApp.setTextColor(0xFF1976D2); // Azul
        tituloApp.setTypeface(tituloApp.getTypeface(), android.graphics.Typeface.BOLD);
        tituloApp.setPadding(0, 0, 0, dpToPx(16));
        
        // Estado de la base de datos
        estadoBaseDatos = new TextView(this);
        estadoBaseDatos.setTextSize(14);
        estadoBaseDatos.setPadding(0, 0, 0, dpToPx(8));
        
        // Sección de progreso
        TextView etiquetaProgreso = new TextView(this);
        etiquetaProgreso.setText("Progreso General");
        etiquetaProgreso.setTextSize(16);
        etiquetaProgreso.setTextColor(0xFF424242); // Gris oscuro
        etiquetaProgreso.setTypeface(etiquetaProgreso.getTypeface(), android.graphics.Typeface.BOLD);
        etiquetaProgreso.setPadding(0, dpToPx(8), 0, dpToPx(4));
        
        // Barra de progreso
        progresoGeneral = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progresoGeneral.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(8)
        ));
        progresoGeneral.setMax(100);
        progresoGeneral.setProgress(0);
        
        // Texto de progreso
        textoProgreso = new TextView(this);
        textoProgreso.setTextSize(12);
        textoProgreso.setTextColor(0xFF757575); // Gris
        textoProgreso.setPadding(0, dpToPx(4), 0, dpToPx(16));
        
        // Botones de control
        LinearLayout layoutBotones = new LinearLayout(this);
        layoutBotones.setOrientation(LinearLayout.HORIZONTAL);
        layoutBotones.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        
        botonReiniciar = new Button(this);
        botonReiniciar.setText("Reiniciar Progreso");
        botonReiniciar.setBackgroundColor(0xFFFF9800); // Naranja
        botonReiniciar.setTextColor(0xFFFFFFFF); // Blanco
        
        botonVerificarIntegridad = new Button(this);
        botonVerificarIntegridad.setText("Verificar Integridad");
        botonVerificarIntegridad.setBackgroundColor(0xFF2196F3); // Azul
        botonVerificarIntegridad.setTextColor(0xFFFFFFFF); // Blanco
        botonVerificarIntegridad.setPadding(dpToPx(8), 0, 0, 0);
        
        layoutBotones.addView(botonReiniciar);
        layoutBotones.addView(botonVerificarIntegridad);
        
        // Lista expandible de capítulos
        TextView etiquetaCapitulos = new TextView(this);
        etiquetaCapitulos.setText("Capítulos del Tutorial");
        etiquetaCapitulos.setTextSize(16);
        etiquetaCapitulos.setTextColor(0xFF424242); // Gris oscuro
        etiquetaCapitulos.setTypeface(etiquetaCapitulos.getTypeface(), android.graphics.Typeface.BOLD);
        etiquetaCapitulos.setPadding(0, dpToPx(16), 0, dpToPx(8));
        
        expandableListView = new ExpandableListView(this);
        expandableListView.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0, 1.0f // Peso para ocupar el espacio restante
        ));
        expandableListView.setBackgroundColor(0xFFFFFFFF); // Fondo blanco
        
        // Añadir todas las vistas al layout principal
        layoutPrincipal.addView(tituloApp);
        layoutPrincipal.addView(estadoBaseDatos);
        layoutPrincipal.addView(etiquetaProgreso);
        layoutPrincipal.addView(progresoGeneral);
        layoutPrincipal.addView(textoProgreso);
        layoutPrincipal.addView(layoutBotones);
        layoutPrincipal.addView(etiquetaCapitulos);
        layoutPrincipal.addView(expandableListView);
        
        setContentView(layoutPrincipal);
    }
    
    /**
     * Inicializa los gestores principales
     */
    private void inicializarGestores() {
        try {
            // Inicializar DatabaseHelper
            databaseHelper = DatabaseHelper.getInstance(this);
            
            // Inicializar TutorialManager
            tutorialManager = new TutorialManager(this);
            
            // Configurar listeners de botones
            configurarListeners();
            
            Log.d(TAG, "Gestores inicializados correctamente");
            
        } catch (Exception e) {
            Log.e(TAG, "Error al inicializar gestores: " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Configura los listeners de los botones
     */
    private void configurarListeners() {
        botonReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciarProgreso();
            }
        });
        
        botonVerificarIntegridad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarIntegridad();
            }
        });
    }
    
    /**
     * Configura la lista de capítulos
     */
    private void configurarListaCapitulos() {
        try {
            if (tutorialManager != null && tutorialManager.isTutorialInicializado()) {
                tutorialManager.configurarExpandableListView(expandableListView);
                Log.d(TAG, "Lista de capítulos configurada");
            } else {
                Log.w(TAG, "TutorialManager no inicializado correctamente");
                mostrarMensajeError("Error al cargar los capítulos");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al configurar lista: " + e.getMessage(), e);
            mostrarMensajeError("Error al configurar la lista de capítulos");
        }
    }
    
    /**
     * Actualiza el estado de la interfaz de usuario
     */
    private void actualizarEstadoUI() {
        try {
            // Verificar estado de la base de datos
            boolean integridadOK = tutorialManager != null && 
                                  tutorialManager.verificarIntegridad();
            
            if (integridadOK) {
                estadoBaseDatos.setText("✓ Base de datos: OK");
                estadoBaseDatos.setTextColor(0xFF4CAF50); // Verde
            } else {
                estadoBaseDatos.setText("⚠ Base de datos: Problemas detectados");
                estadoBaseDatos.setTextColor(0xFFE91E63); // Rojo
            }
            
            // Actualizar progreso general
            if (tutorialManager != null) {
                int progreso = tutorialManager.obtenerProgresoGeneral();
                progresoGeneral.setProgress(progreso);
                textoProgreso.setText("Completado: " + progreso + "%");
            } else {
                progresoGeneral.setProgress(0);
                textoProgreso.setText("Tutorial no disponible");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error al actualizar UI: " + e.getMessage(), e);
        }
    }
    
    /**
     * Reinicia el progreso del tutorial
     */
    private void reiniciarProgreso() {
        try {
            Log.d(TAG, "Reiniciando progreso...");
            
            if (tutorialManager != null) {
                tutorialManager.reiniciarProgreso();
                actualizarEstadoUI();
                mostrarMensaje("Progreso reiniciado correctamente");
            } else {
                mostrarMensajeError("TutorialManager no disponible");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error al reiniciar progreso: " + e.getMessage(), e);
            mostrarMensajeError("Error al reiniciar el progreso");
        }
    }
    
    /**
     * Verifica la integridad de la base de datos
     */
    private void verificarIntegridad() {
        try {
            Log.d(TAG, "Verificando integridad...");
            
            boolean integridadOK = false;
            
            if (databaseHelper != null) {
                integridadOK = databaseHelper.verificarIntegridadBaseDatos();
            }
            
            if (integridadOK) {
                mostrarMensaje("✓ Integridad de la base de datos: OK");
            } else {
                mostrarMensajeError("⚠ Problemas de integridad detectados");
                
                // Intentar reparar
                if (databaseHelper != null) {
                    databaseHelper.reiniciarBaseDatos();
                    mostrarMensaje("Base de datos reparada");
                }
            }
            
            actualizarEstadoUI();
            
        } catch (Exception e) {
            Log.e(TAG, "Error al verificar integridad: " + e.getMessage(), e);
            mostrarMensajeError("Error en verificación de integridad");
        }
    }
    
    /**
     * Muestra un error crítico y permite recovery
     */
    private void mostrarErrorCritico(Exception e) {
        try {
            // Crear layout de error
            LinearLayout layoutError = new LinearLayout(this);
            layoutError.setOrientation(LinearLayout.VERTICAL);
            layoutError.setBackgroundColor(0xFFFFEBEE); // Fondo rojo claro
            int padding = dpToPx(16);
            layoutError.setPadding(padding, padding, padding, padding);
            
            TextView tituloError = new TextView(this);
            tituloError.setText("⚠ Error Crítico");
            tituloError.setTextSize(18);
            tituloError.setTextColor(0xFFD32F2F); // Rojo
            tituloError.setTypeface(tituloError.getTypeface(), android.graphics.Typeface.BOLD);
            
            TextView descripcionError = new TextView(this);
            descripcionError.setText("Se ha detectado un error crítico en la aplicación:\n\n" +
                                   e.getMessage() + "\n\n" +
                                   "La aplicación intentará recuperarse automáticamente.");
            descripcionError.setTextSize(14);
            descripcionError.setTextColor(0xFF424242);
            descripcionError.setPadding(0, dpToPx(8), 0, dpToPx(16));
            
            Button botonRecuperar = new Button(this);
            botonRecuperar.setText("Intentar Recuperación");
            botonRecuperar.setBackgroundColor(0xFF4CAF50); // Verde
            botonRecuperar.setTextColor(0xFFFFFFFF); // Blanco
            
            botonRecuperar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentarRecuperacion();
                }
            });
            
            layoutError.addView(tituloError);
            layoutError.addView(descripcionError);
            layoutError.addView(botonRecuperar);
            
            setContentView(layoutError);
            
        } catch (Exception ex) {
            // Si no podemos ni mostrar el error, al menos loguearlo
            Log.e(TAG, "Error crítico al mostrar error: " + ex.getMessage(), ex);
        }
    }
    
    /**
     * Intenta recuperarse de un error crítico
     */
    private void intentarRecuperacion() {
        try {
            Log.d(TAG, "Intentando recuperación...");
            
            // Reinicializar database helper
            databaseHelper = DatabaseHelper.getInstance(this);
            databaseHelper.reiniciarBaseDatos();
            
            // Recrear la actividad
            recreate();
            
        } catch (Exception e) {
            Log.e(TAG, "Error en recuperación: " + e.getMessage(), e);
            Toast.makeText(this, "Error en recuperación: " + e.getMessage(), 
                         Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Muestra un mensaje de error
     */
    private void mostrarMensajeError(String mensaje) {
        Toast.makeText(this, "Error: " + mensaje, Toast.LENGTH_LONG).show();
        Log.e(TAG, mensaje);
    }
    
    /**
     * Muestra un mensaje informativo
     */
    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        Log.d(TAG, mensaje);
    }
    
    /**
     * Convierte DP a pixels
     */
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // Actualizar estado cuando se resume la actividad
        if (tutorialManager != null && tutorialManager.isTutorialInicializado()) {
            actualizarEstadoUI();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        // Limpiar recursos
        if (tutorialManager != null) {
            tutorialManager.limpiar();
        }
        
        Log.d(TAG, "MainActivity destruida, recursos liberados");
    }
}