package com.diamon.tutorial.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.diamon.tutorial.models.ContenidoTutorial;

import java.util.HashMap;
import java.util.List;

/**
 * Adapter personalizado para el ExpandableListView del tutorial
 * Maneja la visualización de capítulos y su contenido educativo
 * 
 * Este adapter reemplaza la funcionalidad original con mejor
 * manejo de datos, vistas personalizadas y seguimiento de progreso
 * 
 * @author Daniel Diamon
 * @version 1.0
 */
public class TutorialExpandableAdapter extends BaseExpandableListAdapter {
    
    private Context context;
    private List<String> gruposCapitulos;
    private HashMap<String, List<ContenidoTutorial>> contenidoPorCapitulo;
    
    /**
     * Constructor del adapter
     * @param context Contexto de la aplicación
     * @param gruposCapitulos Lista de nombres de capítulos
     * @param contenidoPorCapitulo Mapa de contenido por capítulo
     */
    public TutorialExpandableAdapter(Context context, 
                                   List<String> gruposCapitulos,
                                   HashMap<String, List<ContenidoTutorial>> contenidoPorCapitulo) {
        this.context = context;
        this.gruposCapitulos = gruposCapitulos;
        this.contenidoPorCapitulo = contenidoPorCapitulo;
    }
    
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String nombreGrupo = gruposCapitulos.get(groupPosition);
        List<ContenidoTutorial> contenidos = contenidoPorCapitulo.get(nombreGrupo);
        return contenidos != null ? contenidos.get(childPosition) : null;
    }
    
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                           boolean isLastChild, View convertView, ViewGroup parent) {
        
        final ContenidoTutorial contenido = (ContenidoTutorial) getChild(groupPosition, childPosition);
        
        if (convertView == null) {
            convertView = crearVistaHijo(contenido);
        } else {
            actualizarVistaHijo(convertView, contenido);
        }
        
        return convertView;
    }
    
    /**
     * Crea una nueva vista para un elemento hijo (contenido)
     */
    private View crearVistaHijo(ContenidoTutorial contenido) {
        // Layout principal
        LinearLayout layoutPrincipal = new LinearLayout(context);
        layoutPrincipal.setOrientation(LinearLayout.VERTICAL);
        layoutPrincipal.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        
        // Padding
        int padding = dpToPx(12);
        layoutPrincipal.setPadding(padding * 2, padding, padding, padding);
        
        // Layout horizontal para título e ícono
        LinearLayout layoutTitulo = new LinearLayout(context);
        layoutTitulo.setOrientation(LinearLayout.HORIZONTAL);
        layoutTitulo.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        
        // Ícono de estado
        ImageView iconoEstado = new ImageView(context);
        iconoEstado.setLayoutParams(new ViewGroup.LayoutParams(
            dpToPx(24), dpToPx(24)
        ));
        iconoEstado.setTag("icono_estado");
        
        // Título del contenido
        TextView titulo = new TextView(context);
        titulo.setTextSize(16);
        titulo.setTextColor(0xFF2E7D32); // Verde oscuro
        titulo.setTypeface(titulo.getTypeface(), Typeface.BOLD);
        titulo.setPadding(dpToPx(8), 0, 0, 0);
        titulo.setTag("titulo");
        
        layoutTitulo.addView(iconoEstado);
        layoutTitulo.addView(titulo);
        
        // Descripción
        TextView descripcion = new TextView(context);
        descripcion.setTextSize(14);
        descripcion.setTextColor(0xFF616161); // Gris medio
        descripcion.setPadding(dpToPx(32), dpToPx(4), 0, 0);
        descripcion.setTag("descripcion");
        
        // Información adicional
        LinearLayout layoutInfo = new LinearLayout(context);
        layoutInfo.setOrientation(LinearLayout.HORIZONTAL);
        layoutInfo.setPadding(dpToPx(32), dpToPx(8), 0, dpToPx(8));
        
        // Tiempo estimado
        TextView tiempoEstimado = new TextView(context);
        tiempoEstimado.setTextSize(12);
        tiempoEstimado.setTextColor(0xFF757575); // Gris
        tiempoEstimado.setTag("tiempo");
        
        // Dificultad
        TextView dificultad = new TextView(context);
        dificultad.setTextSize(12);
        dificultad.setTextColor(0xFF757575); // Gris
        dificultad.setPadding(dpToPx(16), 0, 0, 0);
        dificultad.setTag("dificultad");
        
        layoutInfo.addView(tiempoEstimado);
        layoutInfo.addView(dificultad);
        
        // Barra de progreso (solo si es necesario)
        ProgressBar barraProgreso = new ProgressBar(context, null, 
            android.R.attr.progressBarStyleHorizontal);
        barraProgreso.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dpToPx(4)
        ));
        barraProgreso.setMax(100);
        barraProgreso.setTag("progreso");
        
        // Añadir todas las vistas
        layoutPrincipal.addView(layoutTitulo);
        layoutPrincipal.addView(descripcion);
        layoutPrincipal.addView(layoutInfo);
        layoutPrincipal.addView(barraProgreso);
        
        // Actualizar con datos del contenido
        actualizarVistaHijo(layoutPrincipal, contenido);
        
        return layoutPrincipal;
    }
    
    /**
     * Actualiza una vista existente con nuevos datos
     */
    private void actualizarVistaHijo(View vista, ContenidoTutorial contenido) {
        if (contenido == null) return;
        
        try {
            // Actualizar ícono de estado
            ImageView iconoEstado = vista.findViewWithTag("icono_estado");
            if (iconoEstado != null) {
                if (contenido.isCompletado()) {
                    iconoEstado.setBackgroundColor(0xFF4CAF50); // Verde
                    iconoEstado.setContentDescription("Completado");
                } else {
                    iconoEstado.setBackgroundColor(0xFFE0E0E0); // Gris claro
                    iconoEstado.setContentDescription("Pendiente");
                }
            }
            
            // Actualizar título
            TextView titulo = vista.findViewWithTag("titulo");
            if (titulo != null) {
                titulo.setText(contenido.getTitulo() != null ? 
                             contenido.getTitulo() : "Sin título");
            }
            
            // Actualizar descripción
            TextView descripcion = vista.findViewWithTag("descripcion");
            if (descripcion != null) {
                descripcion.setText(contenido.getResumen());
            }
            
            // Actualizar tiempo estimado
            TextView tiempo = vista.findViewWithTag("tiempo");
            if (tiempo != null) {
                tiempo.setText("⏱ " + contenido.getTiempoEstimadoMinutos() + " min");
            }
            
            // Actualizar dificultad
            TextView dificultad = vista.findViewWithTag("dificultad");
            if (dificultad != null) {
                dificultad.setText("🎯 " + contenido.getNivelDificultad());
            }
            
            // Actualizar barra de progreso
            ProgressBar progreso = vista.findViewWithTag("progreso");
            if (progreso != null) {
                progreso.setProgress(contenido.getPuntuacionProgreso());
                progreso.setVisibility(contenido.isCompletado() ? 
                                     View.VISIBLE : View.GONE);
            }
            
        } catch (Exception e) {
            // Log error pero no fallar
            e.printStackTrace();
        }
    }
    
    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition >= gruposCapitulos.size()) return 0;
        
        String nombreGrupo = gruposCapitulos.get(groupPosition);
        List<ContenidoTutorial> contenidos = contenidoPorCapitulo.get(nombreGrupo);
        return contenidos != null ? contenidos.size() : 0;
    }
    
    @Override
    public Object getGroup(int groupPosition) {
        return gruposCapitulos.get(groupPosition);
    }
    
    @Override
    public int getGroupCount() {
        return gruposCapitulos.size();
    }
    
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                           View convertView, ViewGroup parent) {
        
        String nombreCapitulo = (String) getGroup(groupPosition);
        
        if (convertView == null) {
            convertView = crearVistaGrupo(nombreCapitulo, groupPosition);
        } else {
            actualizarVistaGrupo(convertView, nombreCapitulo, groupPosition);
        }
        
        return convertView;
    }
    
    /**
     * Crea una nueva vista para un grupo (capítulo)
     */
    private View crearVistaGrupo(String nombreCapitulo, int position) {
        // Layout principal
        LinearLayout layoutPrincipal = new LinearLayout(context);
        layoutPrincipal.setOrientation(LinearLayout.HORIZONTAL);
        layoutPrincipal.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        
        // Padding y fondo
        int padding = dpToPx(16);
        layoutPrincipal.setPadding(padding, padding, padding, padding);
        layoutPrincipal.setBackgroundColor(0xFFF3E5F5); // Fondo morado claro
        
        // Ícono del capítulo
        TextView iconoCapitulo = new TextView(context);
        iconoCapitulo.setLayoutParams(new ViewGroup.LayoutParams(
            dpToPx(40), dpToPx(40)
        ));
        iconoCapitulo.setBackgroundColor(0xFF9C27B0); // Morado
        iconoCapitulo.setTextColor(0xFFFFFFFF); // Blanco
        iconoCapitulo.setTextSize(16);
        iconoCapitulo.setTypeface(iconoCapitulo.getTypeface(), Typeface.BOLD);
        iconoCapitulo.setGravity(android.view.Gravity.CENTER);
        iconoCapitulo.setText(String.valueOf(position + 1));
        
        // Layout para texto
        LinearLayout layoutTexto = new LinearLayout(context);
        layoutTexto.setOrientation(LinearLayout.VERTICAL);
        layoutTexto.setPadding(dpToPx(12), 0, 0, 0);
        LinearLayout.LayoutParams paramsTexto = new LinearLayout.LayoutParams(
            0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f
        );
        layoutTexto.setLayoutParams(paramsTexto);
        
        // Título del capítulo
        TextView titulo = new TextView(context);
        titulo.setTextSize(18);
        titulo.setTextColor(0xFF2E7D32); // Verde oscuro
        titulo.setTypeface(titulo.getTypeface(), Typeface.BOLD);
        titulo.setTag("titulo_grupo");
        
        // Progreso del capítulo
        TextView progreso = new TextView(context);
        progreso.setTextSize(14);
        progreso.setTextColor(0xFF757575); // Gris
        progreso.setPadding(0, dpToPx(4), 0, 0);
        progreso.setTag("progreso_grupo");
        
        layoutTexto.addView(titulo);
        layoutTexto.addView(progreso);
        
        // Indicador de expansión
        TextView indicadorExpansion = new TextView(context);
        indicadorExpansion.setLayoutParams(new ViewGroup.LayoutParams(
            dpToPx(24), dpToPx(24)
        ));
        indicadorExpansion.setTextSize(18);
        indicadorExpansion.setTextColor(0xFF757575);
        indicadorExpansion.setGravity(android.view.Gravity.CENTER);
        indicadorExpansion.setText("▶"); // Flecha derecha
        indicadorExpansion.setTag("indicador");
        
        layoutPrincipal.addView(iconoCapitulo);
        layoutPrincipal.addView(layoutTexto);
        layoutPrincipal.addView(indicadorExpansion);
        
        // Actualizar con datos
        actualizarVistaGrupo(layoutPrincipal, nombreCapitulo, position);
        
        return layoutPrincipal;
    }
    
    /**
     * Actualiza una vista de grupo existente
     */
    private void actualizarVistaGrupo(View vista, String nombreCapitulo, int position) {
        try {
            // Actualizar título
            TextView titulo = vista.findViewWithTag("titulo_grupo");
            if (titulo != null) {
                titulo.setText(nombreCapitulo);
            }
            
            // Calcular y actualizar progreso
            TextView progreso = vista.findViewWithTag("progreso_grupo");
            if (progreso != null) {
                int progresoCapitulo = calcularProgresoCapitulo(position);
                int totalContenidos = getChildrenCount(position);
                progreso.setText("Progreso: " + progresoCapitulo + "% (" + 
                               obtenerCompletados(position) + "/" + totalContenidos + ")");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Calcula el progreso de un capítulo
     */
    private int calcularProgresoCapitulo(int groupPosition) {
        String nombreGrupo = gruposCapitulos.get(groupPosition);
        List<ContenidoTutorial> contenidos = contenidoPorCapitulo.get(nombreGrupo);
        
        if (contenidos == null || contenidos.isEmpty()) {
            return 0;
        }
        
        int completados = 0;
        for (ContenidoTutorial contenido : contenidos) {
            if (contenido.isCompletado()) {
                completados++;
            }
        }
        
        return (completados * 100) / contenidos.size();
    }
    
    /**
     * Obtiene el número de contenidos completados en un capítulo
     */
    private int obtenerCompletados(int groupPosition) {
        String nombreGrupo = gruposCapitulos.get(groupPosition);
        List<ContenidoTutorial> contenidos = contenidoPorCapitulo.get(nombreGrupo);
        
        if (contenidos == null) return 0;
        
        int completados = 0;
        for (ContenidoTutorial contenido : contenidos) {
            if (contenido.isCompletado()) {
                completados++;
            }
        }
        
        return completados;
    }
    
    @Override
    public boolean hasStableIds() {
        return false;
    }
    
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    /**
     * Convierte DP a pixels
     */
    private int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    
    /**
     * Actualiza todos los datos del adapter
     */
    public void actualizarDatos(List<String> nuevosGrupos, 
                               HashMap<String, List<ContenidoTutorial>> nuevoContenido) {
        this.gruposCapitulos = nuevosGrupos;
        this.contenidoPorCapitulo = nuevoContenido;
        notifyDataSetChanged();
    }
}