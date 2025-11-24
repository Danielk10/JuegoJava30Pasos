package com.diamon.tutorial.capitulos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.diamon.curso.R;
import com.diamon.graficos.Textura2D;
import com.diamon.nucleo.Juego;
import com.diamon.tutorial.archivo.basedatos.DatabaseHelper;
import com.diamon.tutorial.ejemplos.*;
import com.diamon.utilidad.CodeFormatter;
import com.diamon.utilidad.Recurso;

public class Capitulo {

    private Context contexto;
    private DatabaseHelper dbHelper;
    private AppCompatActivity actividad;
    private Recurso recurso;

    public Capitulo(Context contexto, AppCompatActivity actividad, DatabaseHelper dbHelper) {
        this.contexto = contexto;
        this.actividad = actividad;
        this.dbHelper = dbHelper;
        this.recurso = new Recurso(actividad);
    }

    public ViewGroup getCapitilo(int numero) {
        return construirVista(numero);
    }

    private ViewGroup construirVista(int numeroCapitulo) {
        final LinearLayout disenoPrincipal = new LinearLayout(contexto);
        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        // Use standard layout params
        LayoutParams parametrosUI1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Reverted optimization: The 'capitulo' column is sparse (only present on
        // header rows).
        // We must scan the DB and track the current chapter state.
        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String targetCapitulo = (numeroCapitulo == 0) ? "" : "Capitulo " + numeroCapitulo;
        String currentCapitulo = "";
        int numeroVistas = 0;
        boolean foundTarget = false;

        if (cursor != null) {
            LayoutInflater inflater = LayoutInflater.from(contexto);

            while (cursor.moveToNext()) {
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (capituloTexto != null && !capituloTexto.isEmpty()) {
                    currentCapitulo = capituloTexto;
                }

                boolean shouldRender = false;

                if (numeroCapitulo == 0) {
                    // Intro: Render until we hit "Capitulo 1"
                    if ("Capitulo 1".equals(currentCapitulo)) {
                        break;
                    }
                    shouldRender = true;
                } else {
                    // Chapters: Render if current matches target
                    if (targetCapitulo.equals(currentCapitulo)) {
                        foundTarget = true;
                        shouldRender = true;
                    } else {
                        if (foundTarget) {
                            // We passed the target chapter
                            break;
                        }
                        // Continue searching
                        continue;
                    }
                }

                if (!shouldRender)
                    continue;

                // Inflate the reusable item layout
                View itemView = inflater.inflate(R.layout.item_capitulo_content, disenoPrincipal, false);

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));

                // Populate fields
                populateTextView(itemView.findViewById(R.id.tvTitulo), tituloTexto);
                populateTextView(itemView.findViewById(R.id.tvParrafo), parrafoTexto);
                populateTextView(itemView.findViewById(R.id.tvNota), notaTexto);
                populateTextView(itemView.findViewById(R.id.tvEjemplo), ejemploTexto);
                populateTextView(itemView.findViewById(R.id.tvRecurso), recursoTexto);
                populateTextView(itemView.findViewById(R.id.tvVista), vistaTexto);

                // Handle Image
                TextView tvImagen = itemView.findViewById(R.id.tvImagenTexto);
                ImageView ivImagen = itemView.findViewById(R.id.ivImagen);

                if (imagenTexto != null && !imagenTexto.isEmpty()) {
                    tvImagen.setText(imagenTexto);
                    tvImagen.setVisibility(View.VISIBLE);

                    // Logic to load specific images based on text or context
                    ivImagen.setImageResource(R.mipmap.ic_launcher);
                    ivImagen.setVisibility(View.VISIBLE);

                    // Try to load "Clase X.jpg" based on chapter number if possible
                    try {
                        String imageName = "Clase " + (numeroCapitulo + 1) + ".jpg";
                        // Placeholder for texture loading logic
                    } catch (Exception e) {
                        // Ignore
                    }
                }

                // Handle Code
                if (codigoTexto != null && !codigoTexto.isEmpty()) {
                    FrameLayout containerCodigo = itemView.findViewById(R.id.containerCodigo);
                    containerCodigo.setVisibility(View.VISIBLE);

                    // Create background programmatically to match original style
                    GradientDrawable backgroundDrawable = new GradientDrawable();
                    backgroundDrawable.setColor(Color.parseColor("#1E1E2C"));
                    backgroundDrawable.setCornerRadius(30f);
                    containerCodigo.setBackground(backgroundDrawable);
                    containerCodigo.setElevation(8);

                    CodeFormatter fuente = new CodeFormatter();
                    containerCodigo.addView(fuente.formatCode(contexto, codigoTexto));
                }

                // Handle Popup / Vista
                if (vistaTexto != null && !vistaTexto.isEmpty()) {
                    numeroVistas++;

                    // Add the "Ver Ejemplo" button
                    Button desplegar = new Button(contexto);
                    desplegar.setText("Ver Ejemplo");
                    desplegar.setTextColor(Color.WHITE);
                    desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                    desplegar.setPadding(16, 8, 16, 8);
                    desplegar.setTypeface(null, Typeface.BOLD);
                    desplegar.setAllCaps(false);

                    LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    buttonParams.setMargins(0, 16, 0, 0);
                    desplegar.setLayoutParams(buttonParams);

                    // Add button to the item view (or parent layout)
                    ((LinearLayout) itemView).addView(desplegar);

                    final int currentVistas = numeroVistas;
                    desplegar.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent ev) {
                            if (ev.getAction() == MotionEvent.ACTION_UP) {
                                mostrarPopup(numeroCapitulo, currentVistas);
                            }
                            return true;
                        }
                    });
                }

                disenoPrincipal.addView(itemView);
            }
            cursor.close();
        }
        db.close();

        return disenoPrincipal;
    }

    private void populateTextView(TextView tv, String text) {
        if (text != null && !text.isEmpty()) {
            tv.setText(text);
            tv.setVisibility(View.VISIBLE);
        }
    }

    private void mostrarPopup(int numeroCapitulo, int numeroVistas) {
        int ancho = actividad.getWindowManager().getDefaultDisplay().getWidth();
        int alto = actividad.getWindowManager().getDefaultDisplay().getHeight() / 2;

        final LinearLayout diseno = new LinearLayout(contexto);
        diseno.setOrientation(LinearLayout.VERTICAL);
        LayoutParams parametros = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        final PopupWindow ventana = new PopupWindow(diseno, ancho, alto, true);

        View pasoView = obtenerVistaPaso(numeroCapitulo, numeroVistas, ancho, alto);

        if (pasoView != null) {
            diseno.addView(pasoView, parametros);

            // Centralized logic to start the game loop if it's a Juego instance
            if (pasoView instanceof Juego) {
                ((Juego) pasoView).resumen();
            }

            // Restart logic if applicable (for non-Juego views or if Juego needs explicit
            // restart)
            try {
                pasoView.getClass().getMethod("reiniciar").invoke(pasoView);
            } catch (Exception e) {
                // Method might not exist or be accessible, ignore
            }

            ventana.showAtLocation(diseno, Gravity.TOP | Gravity.CENTER, 0, 0);
        }
    }

    private View obtenerVistaPaso(int numeroCapitulo, int numeroVistas, int ancho, int alto) {
        // Map chapter number (0-indexed) to Paso classes

        int capituloReal = numeroCapitulo + 1;

        switch (capituloReal) {
            case 3:
                return new Paso3(contexto);
            case 4:
                if (numeroVistas == 1) {
                    return new Paso4(contexto);
                } else {
                    return new VariosPasos(actividad, ancho, alto);
                }
            case 5:
                return new Paso5(actividad, ancho, alto);
            case 6:
                return new Paso6(actividad, ancho, alto);
            case 7:
                return new Paso7(actividad, ancho, alto);
            case 8:
                return new Paso8(actividad, ancho, alto);
            case 9:
                return new Paso9(actividad, ancho, alto);
            case 10:
                return new Paso10(actividad, ancho, alto);
            case 11:
                return new Paso11(actividad, ancho, alto);
            // Add more cases as needed or default to Paso3
            default:
                // Fallback for later chapters that used Paso3 in the original code
                if (capituloReal >= 12) {
                    return new Paso3(contexto);
                }
                return null;
        }
    }
}
