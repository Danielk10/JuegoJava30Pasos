package com.diamon.tutorial;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.util.Log;

import com.diamon.tutorial.capitulos.CapituloRefactorizado;
import com.diamon.tutorial.contenido.ContenidoEducativo;
import com.diamon.tutorial.database.ContenidoDAO;
import com.diamon.tutorial.models.ContenidoTutorial;
import com.diamon.tutorial.adapters.TutorialExpandableAdapter;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase principal que gestiona el tutorial completo
 * Reemplaza la funcionalidad de Tutorial.java con una arquitectura mejorada
 * 
 * Esta clase maneja:
 * - Inicialización del contenido educativo
 * - Navegación entre capítulos
 * - Seguimiento del progreso del usuario
 * - Integración con la base de datos refactorizada
 * 
 * @author Daniel Diamon
 * @version 2.0
 */
public class TutorialManager {
    
    private static final String TAG = "TutorialManager";
    
    private Context context;
    private ContenidoDAO contenidoDAO;
    private ContenidoEducativo contenidoEducativo;
    private TutorialExpandableAdapter adapter;
    private ExpandableListView expandableListView;
    
    // Datos para el ExpandableListView
    private List<String> gruposCapitulos;
    private HashMap<String, List<ContenidoTutorial>> contenidoPorCapitulo;
    
    // Estado del tutorial
    private int capituloActual = 1;
    private boolean tutorialInicializado = false;
    
    /**
     * Constructor principal
     * @param context Contexto de la aplicación
     */
    public TutorialManager(Context context) {
        this.context = context;
        this.contenidoDAO = new ContenidoDAO(context);
        this.contenidoEducativo = new ContenidoEducativo(context);
        
        inicializarDatos();
    }
    
    /**
     * Inicializa todos los datos del tutorial
     */
    private void inicializarDatos() {
        try {
            Log.d(TAG, "Inicializando datos del tutorial...");
            
            // Verificar si ya tenemos contenido en la base de datos
            if (!contenidoEducativo.tieneContenido()) {
                Log.d(TAG, "No hay contenido, inicializando contenido educativo...");
                contenidoEducativo.inicializarContenidoCompleto();
            }
            
            // Cargar estructura de capítulos
            cargarEstructuraCapitulos();
            
            tutorialInicializado = true;
            Log.d(TAG, "Tutorial inicializado correctamente");
            
        } catch (Exception e) {
            Log.e(TAG, "Error al inicializar tutorial: " + e.getMessage(), e);
            mostrarMensajeError("Error al inicializar el tutorial. " + 
                               "Por favor, reinicia la aplicación.");
        }
    }
    
    /**
     * Carga la estructura de capítulos desde la base de datos
     */
    private void cargarEstructuraCapitulos() {
        gruposCapitulos = new ArrayList<>();
        contenidoPorCapitulo = new HashMap<>();
        
        try {
            // Obtener número total de capítulos
            int totalCapitulos = contenidoDAO.obtenerNumeroCapitulos();
            
            if (totalCapitulos == 0) {
                // Si no hay capítulos, crear estructura básica
                crearEstructuraBasica();
                return;
            }
            
            // Cargar contenido para cada capítulo
            for (int i = 1; i <= Math.max(totalCapitulos, 30); i++) {
                String nombreGrupo = obtenerNombreCapitulo(i);
                gruposCapitulos.add(nombreGrupo);
                
                List<ContenidoTutorial> contenidosCapitulo = 
                    contenidoDAO.obtenerContenidoPorCapitulo(i);
                
                if (contenidosCapitulo.isEmpty()) {
                    // Crear contenido placeholder si no existe
                    contenidosCapitulo = crearContenidoPlaceholder(i);
                }
                
                contenidoPorCapitulo.put(nombreGrupo, contenidosCapitulo);
            }
            
            Log.d(TAG, "Estructura cargada: " + gruposCapitulos.size() + " capítulos");
            
        } catch (Exception e) {
            Log.e(TAG, "Error al cargar estructura: " + e.getMessage(), e);
            crearEstructuraBasica();
        }
    }
    
    /**
     * Crea una estructura básica cuando no hay datos
     */
    private void crearEstructuraBasica() {
        gruposCapitulos.clear();
        contenidoPorCapitulo.clear();
        
        for (int i = 1; i <= 30; i++) {
            String nombreGrupo = obtenerNombreCapitulo(i);
            gruposCapitulos.add(nombreGrupo);
            
            List<ContenidoTutorial> contenidoBasico = crearContenidoPlaceholder(i);
            contenidoPorCapitulo.put(nombreGrupo, contenidoBasico);
        }
    }
    
    /**
     * Obtiene el nombre formateado de un capítulo
     */
    private String obtenerNombreCapitulo(int numeroCapitulo) {
        String categoria = obtenerCategoriaCapitulo(numeroCapitulo);
        return String.format("Capítulo %d: %s", numeroCapitulo, 
                           obtenerTituloCapitulo(numeroCapitulo)) + 
               " (" + categoria + ")";
    }
    
    /**
     * Obtiene la categoría de un capítulo
     */
    private String obtenerCategoriaCapitulo(int capitulo) {
        if (capitulo <= 5) return "Fundamentos";
        if (capitulo <= 10) return "POO";
        if (capitulo <= 15) return "Estructuras de Datos";
        if (capitulo <= 20) return "Interfaces Gráficas";
        if (capitulo <= 25) return "Desarrollo de Juegos";
        return "Optimización";
    }
    
    /**
     * Obtiene el título de un capítulo
     */
    private String obtenerTituloCapitulo(int capitulo) {
        switch (capitulo) {
            case 1: return "Tu Primer Programa";
            case 2: return "Variables y Tipos";
            case 3: return "Operadores";
            case 4: return "Condicionales";
            case 5: return "Bucles";
            case 6: return "Clases y Objetos";
            case 7: return "Encapsulación";
            case 8: return "Herencia";
            case 9: return "Polimorfismo";
            case 10: return "Interfaces";
            default: return "En Desarrollo";
        }
    }
    
    /**
     * Crea contenido placeholder para capítulos sin datos
     */
    private List<ContenidoTutorial> crearContenidoPlaceholder(int capitulo) {
        List<ContenidoTutorial> lista = new ArrayList<>();
        
        ContenidoTutorial placeholder = new ContenidoTutorial(
            capitulo,
            "Contenido del Capítulo " + capitulo,
            "Este capítulo está siendo desarrollado. Pronto tendrá contenido " +
            "educativo completo sobre " + obtenerCategoriaCapitulo(capitulo).toLowerCase() + ".",
            "// Código de ejemplo para el capítulo " + capitulo + "\n" +
            "// Próximamente...",
            "Este capítulo forma parte de la sección \"" + 
            obtenerCategoriaCapitulo(capitulo) + 
            "\" del tutorial. Mantente atento a las actualizaciones.",
            1
        );
        
        lista.add(placeholder);
        return lista;
    }
    
    /**
     * Configura el ExpandableListView con el adapter
     */
    public void configurarExpandableListView(ExpandableListView expandableListView) {
        this.expandableListView = expandableListView;
        
        if (!tutorialInicializado) {
            mostrarMensajeError("Tutorial no inicializado correctamente");
            return;
        }
        
        try {
            // Crear y configurar adapter
            adapter = new TutorialExpandableAdapter(
                context, 
                gruposCapitulos, 
                contenidoPorCapitulo
            );
            
            expandableListView.setAdapter(adapter);
            
            // Configurar listeners
            configurarListeners();
            
            Log.d(TAG, "ExpandableListView configurado correctamente");
            
        } catch (Exception e) {
            Log.e(TAG, "Error al configurar ExpandableListView: " + e.getMessage(), e);
            mostrarMensajeError("Error al configurar la lista de capítulos");
        }
    }
    
    /**
     * Configura los listeners del ExpandableListView
     */
    private void configurarListeners() {
        // Listener para expansión de grupos
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                try {
                    Log.d(TAG, "Expandiendo grupo: " + groupPosition);
                    
                    // Colapsar otros grupos (opcional)
                    for (int i = 0; i < adapter.getGroupCount(); i++) {
                        if (i != groupPosition) {
                            expandableListView.collapseGroup(i);
                        }
                    }
                    
                    // Actualizar capítulo actual
                    capituloActual = groupPosition + 1;
                    
                } catch (Exception e) {
                    Log.e(TAG, "Error en expansión de grupo: " + e.getMessage(), e);
                }
            }
        });
        
        // Listener para colapso de grupos
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.d(TAG, "Colapsando grupo: " + groupPosition);
            }
        });
        
        // Listener para clics en elementos hijos
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, 
                                      int groupPosition, int childPosition, long id) {
                try {
                    Log.d(TAG, "Clic en hijo - Grupo: " + groupPosition + 
                            ", Hijo: " + childPosition);
                    
                    // Abrir vista detallada del contenido
                    abrirVistaDetallada(groupPosition + 1, childPosition);
                    
                    return true;
                } catch (Exception e) {
                    Log.e(TAG, "Error en clic de hijo: " + e.getMessage(), e);
                    return false;
                }
            }
        });
    }
    
    /**
     * Abre la vista detallada de un capítulo específico
     */
    public void abrirVistaDetallada(int numeroCapitulo, int indiceContenido) {
        try {
            Log.d(TAG, "Abriendo vista detallada - Capítulo: " + numeroCapitulo + 
                      ", Índice: " + indiceContenido);
            
            // Crear instancia del capítulo refactorizado
            CapituloRefactorizado capitulo = new CapituloRefactorizado(context, numeroCapitulo);
            
            // Obtener la vista del capítulo
            View vistaCapitulo = capitulo.getVista();
            
            if (vistaCapitulo != null) {
                // Aquí puedes implementar la lógica para mostrar la vista
                // Por ejemplo, cambiar el contenido de un Fragment o Activity
                mostrarVistaCapitulo(vistaCapitulo, numeroCapitulo);
            } else {
                mostrarMensajeError("Error al cargar el capítulo " + numeroCapitulo);
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error al abrir vista detallada: " + e.getMessage(), e);
            mostrarMensajeError("Error al abrir el capítulo");
        }
    }
    
    /**
     * Muestra la vista de un capítulo (implementar según la arquitectura de UI)
     */
    private void mostrarVistaCapitulo(View vistaCapitulo, int numeroCapitulo) {
        // Esta implementación depende de cómo esté estructurada la UI
        // Puede ser un Fragment, Activity, o Dialog
        
        Log.d(TAG, "Mostrando vista del capítulo " + numeroCapitulo);
        // TODO: Implementar navegación según la arquitectura de la app
    }
    
    /**
     * Actualiza el progreso de un capítulo
     */
    public void actualizarProgreso(int numeroCapitulo, int indiceContenido, boolean completado) {
        try {
            String nombreGrupo = gruposCapitulos.get(numeroCapitulo - 1);
            List<ContenidoTutorial> contenidos = contenidoPorCapitulo.get(nombreGrupo);
            
            if (contenidos != null && indiceContenido < contenidos.size()) {
                ContenidoTutorial contenido = contenidos.get(indiceContenido);
                contenido.setCompletado(completado);
                
                // Actualizar en base de datos
                contenidoDAO.actualizarEstadoCompletado(contenido.getId(), completado);
                
                // Notificar al adapter
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                
                Log.d(TAG, "Progreso actualizado - Capítulo: " + numeroCapitulo + 
                          ", Completado: " + completado);
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error al actualizar progreso: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene el progreso general del tutorial
     */
    public int obtenerProgresoGeneral() {
        int totalContenidos = 0;
        int completados = 0;
        
        try {
            for (List<ContenidoTutorial> contenidos : contenidoPorCapitulo.values()) {
                for (ContenidoTutorial contenido : contenidos) {
                    totalContenidos++;
                    if (contenido.isCompletado()) {
                        completados++;
                    }
                }
            }
            
            return totalContenidos > 0 ? (completados * 100) / totalContenidos : 0;
            
        } catch (Exception e) {
            Log.e(TAG, "Error al calcular progreso: " + e.getMessage(), e);
            return 0;
        }
    }
    
    /**
     * Reinicia todo el progreso del tutorial
     */
    public void reiniciarProgreso() {
        try {
            // Reiniciar en base de datos
            contenidoEducativo.inicializarContenidoCompleto();
            
            // Recargar estructura
            cargarEstructuraCapitulos();
            
            // Actualizar adapter
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            
            capituloActual = 1;
            
            Log.d(TAG, "Progreso reiniciado correctamente");
            mostrarMensaje("Progreso reiniciado");
            
        } catch (Exception e) {
            Log.e(TAG, "Error al reiniciar progreso: " + e.getMessage(), e);
            mostrarMensajeError("Error al reiniciar el progreso");
        }
    }
    
    /**
     * Verifica la integridad de la base de datos
     */
    public boolean verificarIntegridad() {
        try {
            return contenidoDAO.obtenerNumeroCapitulos() > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error al verificar integridad: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Muestra un mensaje de error
     */
    private void mostrarMensajeError(String mensaje) {
        if (context != null) {
            Toast.makeText(context, "Error: " + mensaje, Toast.LENGTH_LONG).show();
        }
        Log.e(TAG, mensaje);
    }
    
    /**
     * Muestra un mensaje informativo
     */
    private void mostrarMensaje(String mensaje) {
        if (context != null) {
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, mensaje);
    }
    
    /**
     * Libera recursos
     */
    public void limpiar() {
        this.context = null;
        this.contenidoDAO = null;
        this.contenidoEducativo = null;
        this.adapter = null;
        this.expandableListView = null;
        
        if (gruposCapitulos != null) {
            gruposCapitulos.clear();
        }
        if (contenidoPorCapitulo != null) {
            contenidoPorCapitulo.clear();
        }
    }
    
    // Getters
    public int getCapituloActual() {
        return capituloActual;
    }
    
    public boolean isTutorialInicializado() {
        return tutorialInicializado;
    }
    
    public List<String> getGruposCapitulos() {
        return gruposCapitulos;
    }
    
    public HashMap<String, List<ContenidoTutorial>> getContenidoPorCapitulo() {
        return contenidoPorCapitulo;
    }
}