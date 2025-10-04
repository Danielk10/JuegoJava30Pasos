package com.diamon.tutorial.capitulos;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.Button;

import com.diamon.tutorial.database.ContenidoDAO;
import com.diamon.tutorial.models.ContenidoTutorial;

import java.util.List;

/**
 * Clase refactorizada para manejar capítulos del tutorial
 * Soluciona los errores SQLite y mejora la arquitectura
 * 
 * Esta clase reemplaza la funcionalidad de la clase Capitulo original
 * con mejor manejo de errores, arquitectura limpia y código mantenible
 * 
 * @author Daniel Diamon
 * @version 2.0
 */
public class CapituloRefactorizado {
    
    private static final String TAG = "CapituloRefactorizado";
    
    private Context context;
    private ContenidoDAO contenidoDAO;
    private int numeroCapitulo;
    private View vistaCapitulo;
    
    /**
     * Constructor principal
     * @param context Contexto de la aplicación
     * @param numeroCapitulo Número del capítulo a mostrar
     */
    public CapituloRefactorizado(Context context, int numeroCapitulo) {
        this.context = context;
        this.numeroCapitulo = numeroCapitulo;
        this.contenidoDAO = new ContenidoDAO(context);
        inicializarVista();
    }
    
    /**
     * Inicializa la vista del capítulo
     */
    private void inicializarVista() {
        try {
            // Crear layout principal como ScrollView
            ScrollView scrollView = new ScrollView(context);
            scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            ));
            
            // Layout vertical para el contenido
            LinearLayout layoutPrincipal = new LinearLayout(context);
            layoutPrincipal.setOrientation(LinearLayout.VERTICAL);
            layoutPrincipal.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            
            // Configurar padding
            int padding = dpToPx(16);
            layoutPrincipal.setPadding(padding, padding, padding, padding);
            
            // Agregar contenido del capítulo
            cargarContenidoCapitulo(layoutPrincipal);
            
            scrollView.addView(layoutPrincipal);
            this.vistaCapitulo = scrollView;
            
        } catch (Exception e) {
            Log.e(TAG, "Error al inicializar vista del capítulo " + numeroCapitulo, e);
            crearVistaError();
        }
    }
    
    /**
     * Carga el contenido del capítulo desde la base de datos
     * @param layoutPrincipal Layout donde agregar el contenido
     */
    private void cargarContenidoCapitulo(LinearLayout layoutPrincipal) {
        try {
            Log.d(TAG, "Cargando contenido para capítulo " + numeroCapitulo);
            
            // Obtener contenido de la base de datos
            List<ContenidoTutorial> contenidos = contenidoDAO.obtenerContenidoPorCapitulo(numeroCapitulo);
            
            if (contenidos.isEmpty()) {
                // Si no hay contenido, crear contenido por defecto
                Log.w(TAG, "No se encontró contenido para capítulo " + numeroCapitulo + ", creando contenido por defecto");
                crearContenidoPorDefecto(layoutPrincipal);
            } else {
                // Mostrar contenido obtenido de la base de datos
                for (ContenidoTutorial contenido : contenidos) {
                    agregarContenidoAVista(layoutPrincipal, contenido);
                }
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error al cargar contenido del capítulo: " + e.getMessage(), e);
            mostrarMensajeError(layoutPrincipal, "Error al cargar el contenido del capítulo");
        }
    }
    
    /**
     * Agrega un contenido específico a la vista
     * @param layout Layout padre
     * @param contenido Contenido a agregar
     */
    private void agregarContenidoAVista(LinearLayout layout, ContenidoTutorial contenido) {
        try {
            // Título
            if (contenido.getTitulo() != null && !contenido.getTitulo().isEmpty()) {
                TextView titulo = crearTitulo(contenido.getTitulo());
                layout.addView(titulo);
            }
            
            // Descripción
            if (contenido.getDescripcion() != null && !contenido.getDescripcion().isEmpty()) {
                TextView descripcion = crearDescripcion(contenido.getDescripcion());
                layout.addView(descripcion);
            }
            
            // Código
            if (contenido.tieneCodigo()) {
                TextView codigo = crearCodigo(contenido.getCodigo());
                layout.addView(codigo);
            }
            
            // Explicación
            if (contenido.getExplicacion() != null && !contenido.getExplicacion().isEmpty()) {
                TextView explicacion = crearExplicacion(contenido.getExplicacion());
                layout.addView(explicacion);
            }
            
            // Botón de completado
            Button botonCompletado = crearBotonCompletado(contenido);
            layout.addView(botonCompletado);
            
            // Separador
            View separador = crearSeparador();
            layout.addView(separador);
            
        } catch (Exception e) {
            Log.e(TAG, "Error al agregar contenido a vista: " + e.getMessage(), e);
        }
    }
    
    /**
     * Crea contenido por defecto cuando no hay datos en la base de datos
     */
    private void crearContenidoPorDefecto(LinearLayout layout) {
        ContenidoTutorial contenidoDefecto = obtenerContenidoPorDefectoParaCapitulo(numeroCapitulo);
        
        if (contenidoDefecto != null) {
            // Guardar en base de datos para futuros usos
            long id = contenidoDAO.insertarContenido(contenidoDefecto);
            contenidoDefecto.setId(id);
            
            // Mostrar en la vista
            agregarContenidoAVista(layout, contenidoDefecto);
        } else {
            mostrarMensajeError(layout, "Capítulo " + numeroCapitulo + " en desarrollo");
        }
    }
    
    /**
     * Obtiene contenido por defecto según el número de capítulo
     */
    private ContenidoTutorial obtenerContenidoPorDefectoParaCapitulo(int capitulo) {
        switch (capitulo) {
            case 1:
                return new ContenidoTutorial(
                    1,
                    "Paso 1: Tu Primer Programa en Java",
                    "En este primer paso, aprenderemos a crear nuestro primer programa en Java. " +
                    "Este es el punto de partida para cualquier desarrollador de Java.",
                    "public class HolaMundo {\n" +
                    "    public static void main(String[] args) {\n" +
                    "        System.out.println(\"¡Hola, mundo del desarrollo de juegos!\");\n" +
                    "    }\n" +
                    "}",
                    "Este programa demuestra la estructura básica de una clase Java. " +
                    "La palabra clave 'public' hace que la clase sea accesible desde cualquier lugar. " +
                    "El método 'main' es el punto de entrada del programa."
                );
                
            case 2:
                return new ContenidoTutorial(
                    2,
                    "Paso 2: Variables y Tipos de Datos",
                    "Aprenderemos sobre los diferentes tipos de datos en Java y cómo declarar variables.",
                    "// Tipos de datos primitivos\n" +
                    "int puntuacion = 100;\n" +
                    "double velocidad = 5.5;\n" +
                    "boolean juegoActivo = true;\n" +
                    "char inicial = 'J';\n" +
                    "\n" +
                    "// Tipos de datos por referencia\n" +
                    "String nombreJugador = \"Desarrollador\";\n" +
                    "int[] puntuaciones = {100, 200, 300};",
                    "Java tiene dos categorías de tipos de datos: primitivos y por referencia. " +
                    "Los tipos primitivos almacenan valores directamente, mientras que los " +
                    "tipos por referencia almacenan referencias a objetos en memoria."
                );
                
            case 3:
                return new ContenidoTutorial(
                    3,
                    "Paso 3: Operadores y Expresiones",
                    "Conoceremos los operadores aritméticos, lógicos y de comparación en Java.",
                    "// Operadores aritméticos\n" +
                    "int a = 10, b = 5;\n" +
                    "int suma = a + b;        // 15\n" +
                    "int resta = a - b;       // 5\n" +
                    "int multiplicacion = a * b; // 50\n" +
                    "int division = a / b;    // 2\n" +
                    "\n" +
                    "// Operadores de comparación\n" +
                    "boolean esMayor = a > b;     // true\n" +
                    "boolean esIgual = a == b;    // false\n" +
                    "\n" +
                    "// Operadores lógicos\n" +
                    "boolean resultado = (a > b) && (a != 0); // true",
                    "Los operadores nos permiten realizar cálculos y comparaciones. " +
                    "Son fundamentales para crear la lógica de nuestros juegos."
                );
                
            default:
                return new ContenidoTutorial(
                    capitulo,
                    "Capítulo " + capitulo + ": En Desarrollo",
                    "Este capítulo está siendo desarrollado. Pronto estará disponible con contenido " +
                    "educativo completo sobre desarrollo de juegos en Java.",
                    "// Código del capítulo " + capitulo + " próximamente",
                    "Estamos trabajando en crear el mejor contenido educativo para este capítulo. " +
                    "Mantente atento a las actualizaciones."
                );
        }
    }
    
    /**
     * Crea un TextView para el título
     */
    private TextView crearTitulo(String texto) {
        TextView titulo = new TextView(context);
        titulo.setText(texto);
        titulo.setTextSize(24);
        titulo.setTextColor(0xFF2E7D32); // Verde oscuro
        titulo.setPadding(0, dpToPx(16), 0, dpToPx(8));
        titulo.setTypeface(null, android.graphics.Typeface.BOLD);
        return titulo;
    }
    
    /**
     * Crea un TextView para la descripción
     */
    private TextView crearDescripcion(String texto) {
        TextView descripcion = new TextView(context);
        descripcion.setText(texto);
        descripcion.setTextSize(16);
        descripcion.setTextColor(0xFF424242); // Gris oscuro
        descripcion.setPadding(0, 0, 0, dpToPx(12));
        descripcion.setLineSpacing(dpToPx(4), 1.0f);
        return descripcion;
    }
    
    /**
     * Crea un TextView para el código
     */
    private TextView crearCodigo(String codigo) {
        TextView textoCodigo = new TextView(context);
        textoCodigo.setText(codigo);
        textoCodigo.setTextSize(14);
        textoCodigo.setTextColor(0xFF1565C0); // Azul
        textoCodigo.setBackgroundColor(0xFFF5F5F5); // Fondo gris claro
        textoCodigo.setTypeface(android.graphics.Typeface.MONOSPACE);
        int padding = dpToPx(12);
        textoCodigo.setPadding(padding, padding, padding, padding);
        textoCodigo.setHorizontallyScrolling(true);
        return textoCodigo;
    }
    
    /**
     * Crea un TextView para la explicación
     */
    private TextView crearExplicacion(String texto) {
        TextView explicacion = new TextView(context);
        explicacion.setText(texto);
        explicacion.setTextSize(15);
        explicacion.setTextColor(0xFF616161); // Gris medio
        explicacion.setPadding(0, dpToPx(8), 0, dpToPx(16));
        explicacion.setLineSpacing(dpToPx(2), 1.0f);
        return explicacion;
    }
    
    /**
     * Crea un botón para marcar como completado
     */
    private Button crearBotonCompletado(ContenidoTutorial contenido) {
        Button boton = new Button(context);
        actualizarEstadoBoton(boton, contenido);
        
        boton.setOnClickListener(v -> {
            boolean nuevoEstado = !contenido.isCompletado();
            contenido.setCompletado(nuevoEstado);
            contenidoDAO.actualizarEstadoCompletado(contenido.getId(), nuevoEstado);
            actualizarEstadoBoton(boton, contenido);
        });
        
        return boton;
    }
    
    /**
     * Actualiza el estado visual del botón de completado
     */
    private void actualizarEstadoBoton(Button boton, ContenidoTutorial contenido) {
        if (contenido.isCompletado()) {
            boton.setText("✓ Completado");
            boton.setBackgroundColor(0xFF4CAF50); // Verde
            boton.setTextColor(0xFFFFFFFF); // Blanco
        } else {
            boton.setText("Marcar como completado");
            boton.setBackgroundColor(0xFF2196F3); // Azul
            boton.setTextColor(0xFFFFFFFF); // Blanco
        }
    }
    
    /**
     * Crea un separador visual
     */
    private View crearSeparador() {
        View separador = new View(context);
        separador.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dpToPx(1)
        ));
        separador.setBackgroundColor(0xFFE0E0E0); // Gris claro
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dpToPx(1)
        );
        params.setMargins(0, dpToPx(16), 0, dpToPx(16));
        separador.setLayoutParams(params);
        return separador;
    }
    
    /**
     * Muestra un mensaje de error en la vista
     */
    private void mostrarMensajeError(LinearLayout layout, String mensaje) {
        TextView error = new TextView(context);
        error.setText(mensaje);
        error.setTextSize(16);
        error.setTextColor(0xFFD32F2F); // Rojo
        error.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
        error.setBackgroundColor(0xFFFFEBEE); // Fondo rojo claro
        layout.addView(error);
    }
    
    /**
     * Crea una vista de error cuando falla la inicialización
     */
    private void crearVistaError() {
        LinearLayout layoutError = new LinearLayout(context);
        layoutError.setOrientation(LinearLayout.VERTICAL);
        layoutError.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ));
        
        mostrarMensajeError(layoutError, 
            "Error al cargar el capítulo " + numeroCapitulo + 
            ". Por favor, reinicia la aplicación.");
        
        this.vistaCapitulo = layoutError;
    }
    
    /**
     * Convierte DP a pixels
     */
    private int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    
    /**
     * Obtiene la vista del capítulo
     * @return Vista del capítulo
     */
    public View getVista() {
        return vistaCapitulo;
    }
    
    /**
     * Obtiene el número del capítulo
     * @return Número del capítulo
     */
    public int getNumeroCapitulo() {
        return numeroCapitulo;
    }
    
    /**
     * Recarga el contenido del capítulo
     */
    public void recargarContenido() {
        inicializarVista();
    }
    
    /**
     * Libera recursos
     */
    public void limpiar() {
        this.context = null;
        this.contenidoDAO = null;
        this.vistaCapitulo = null;
    }
}