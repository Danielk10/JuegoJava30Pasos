package com.diamon.tutorial.models;

import java.util.Date;

/**
 * Modelo de datos para el contenido educativo del tutorial
 * Representa una lección o paso específico en el tutorial de Java
 * 
 * Esta clase encapsula toda la información necesaria para un elemento
 * del tutorial, incluyendo código, explicaciones, multimedia y metadatos
 * 
 * @author Daniel Diamon
 * @version 1.0
 */
public class ContenidoTutorial {
    
    private long id;
    private int capitulo;
    private String titulo;
    private String descripcion;
    private String codigo;
    private String explicacion;
    private String imagenUrl;
    private String videoUrl;
    private int orden;
    private boolean completado;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    
    /**
     * Constructor por defecto
     */
    public ContenidoTutorial() {
        this.completado = false;
        this.orden = 0;
        this.fechaCreacion = new Date();
        this.fechaActualizacion = new Date();
    }
    
    /**
     * Constructor con parámetros básicos
     * @param capitulo Número del capítulo
     * @param titulo Título del contenido
     * @param descripcion Descripción breve
     * @param codigo Código de ejemplo
     * @param explicacion Explicación detallada
     */
    public ContenidoTutorial(int capitulo, String titulo, String descripcion, 
                           String codigo, String explicacion) {
        this();
        this.capitulo = capitulo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.explicacion = explicacion;
    }
    
    /**
     * Constructor completo
     */
    public ContenidoTutorial(int capitulo, String titulo, String descripcion, 
                           String codigo, String explicacion, String imagenUrl, 
                           String videoUrl, int orden) {
        this(capitulo, titulo, descripcion, codigo, explicacion);
        this.imagenUrl = imagenUrl;
        this.videoUrl = videoUrl;
        this.orden = orden;
    }
    
    // Getters y Setters
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public int getCapitulo() {
        return capitulo;
    }
    
    public void setCapitulo(int capitulo) {
        this.capitulo = capitulo;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getExplicacion() {
        return explicacion;
    }
    
    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
    }
    
    public String getImagenUrl() {
        return imagenUrl;
    }
    
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
    
    public String getVideoUrl() {
        return videoUrl;
    }
    
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    
    public int getOrden() {
        return orden;
    }
    
    public void setOrden(int orden) {
        this.orden = orden;
    }
    
    public boolean isCompletado() {
        return completado;
    }
    
    public void setCompletado(boolean completado) {
        this.completado = completado;
        if (completado) {
            this.fechaActualizacion = new Date();
        }
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    /**
     * Verifica si el contenido tiene código de ejemplo
     * @return true si tiene código, false en caso contrario
     */
    public boolean tieneCodigo() {
        return codigo != null && !codigo.trim().isEmpty();
    }
    
    /**
     * Verifica si el contenido tiene imagen asociada
     * @return true si tiene imagen, false en caso contrario
     */
    public boolean tieneImagen() {
        return imagenUrl != null && !imagenUrl.trim().isEmpty();
    }
    
    /**
     * Verifica si el contenido tiene video asociado
     * @return true si tiene video, false en caso contrario
     */
    public boolean tieneVideo() {
        return videoUrl != null && !videoUrl.trim().isEmpty();
    }
    
    /**
     * Obtiene un resumen del contenido para mostrar en listas
     * @return Resumen del contenido
     */
    public String getResumen() {
        if (descripcion == null || descripcion.length() <= 100) {
            return descripcion;
        }
        return descripcion.substring(0, 97) + "...";
    }
    
    /**
     * Obtiene el nombre del archivo de la imagen (si existe)
     * @return Nombre del archivo o null
     */
    public String getNombreArchivoImagen() {
        if (!tieneImagen()) {
            return null;
        }
        
        String[] partes = imagenUrl.split("/");
        return partes[partes.length - 1];
    }
    
    /**
     * Calcula una puntuación de progreso basada en completado
     * @return Puntuación (0 si no completado, 100 si completado)
     */
    public int getPuntuacionProgreso() {
        return completado ? 100 : 0;
    }
    
    /**
     * Valida que el contenido tenga los campos mínimos necesarios
     * @return true si es válido, false en caso contrario
     */
    public boolean esValido() {
        return capitulo > 0 && 
               titulo != null && !titulo.trim().isEmpty() &&
               descripcion != null && !descripcion.trim().isEmpty();
    }
    
    /**
     * Obtiene la categoría del capítulo basada en el número
     * @return Categoría del capítulo
     */
    public String getCategoriaCapitulo() {
        switch (capitulo) {
            case 1:
                return "Fundamentos";
            case 2:
            case 3:
            case 4:
                return "Conceptos Básicos";
            case 5:
            case 6:
            case 7:
                return "Programación Orientada a Objetos";
            case 8:
            case 9:
            case 10:
                return "Estructuras de Control";
            case 11:
            case 12:
            case 13:
                return "Arrays y Colecciones";
            case 14:
            case 15:
            case 16:
                return "Interfaces Gráficas";
            case 17:
            case 18:
            case 19:
                return "Manejo de Archivos";
            case 20:
            case 21:
            case 22:
                return "Multithreading";
            case 23:
            case 24:
            case 25:
                return "Desarrollo de Juegos";
            case 26:
            case 27:
            case 28:
                return "Optimización";
            case 29:
            case 30:
                return "Proyecto Final";
            default:
                return "Otros";
        }
    }
    
    /**
     * Obtiene el nivel de dificultad basado en el capítulo
     * @return Nivel de dificultad (Principiante, Intermedio, Avanzado)
     */
    public String getNivelDificultad() {
        if (capitulo <= 10) {
            return "Principiante";
        } else if (capitulo <= 20) {
            return "Intermedio";
        } else {
            return "Avanzado";
        }
    }
    
    /**
     * Estima el tiempo de lectura en minutos
     * @return Tiempo estimado en minutos
     */
    public int getTiempoEstimadoMinutos() {
        int palabras = 0;
        
        if (titulo != null) {
            palabras += titulo.split("\\s+").length;
        }
        if (descripcion != null) {
            palabras += descripcion.split("\\s+").length;
        }
        if (explicacion != null) {
            palabras += explicacion.split("\\s+").length;
        }
        if (codigo != null) {
            palabras += codigo.split("\\s+").length * 2; // El código toma más tiempo
        }
        
        // Promedio de 200 palabras por minuto de lectura
        int tiempoBase = Math.max(1, palabras / 200);
        
        // Agregar tiempo por multimedia
        if (tieneImagen()) {
            tiempoBase += 1;
        }
        if (tieneVideo()) {
            tiempoBase += 5; // Videos típicamente de 5 minutos
        }
        
        return tiempoBase;
    }
    
    @Override
    public String toString() {
        return "ContenidoTutorial{" +
                "id=" + id +
                ", capitulo=" + capitulo +
                ", titulo='" + titulo + '\'' +
                ", orden=" + orden +
                ", completado=" + completado +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ContenidoTutorial that = (ContenidoTutorial) obj;
        return id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}