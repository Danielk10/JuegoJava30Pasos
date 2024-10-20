package com.diamon.tutorial.capitulos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.diamon.curso1.R;
import com.diamon.graficos.Textura2D;
import com.diamon.tutorial.archivo.basedatos.DatabaseHelper;
import com.diamon.tutorial.ejemplos.Paso10;
import com.diamon.tutorial.ejemplos.Paso11;
import com.diamon.tutorial.ejemplos.Paso3;
import com.diamon.tutorial.ejemplos.Paso4;
import com.diamon.tutorial.ejemplos.Paso5;
import com.diamon.tutorial.ejemplos.Paso6;
import com.diamon.tutorial.ejemplos.Paso7;
import com.diamon.tutorial.ejemplos.Paso8;
import com.diamon.tutorial.ejemplos.Paso9;
import com.diamon.tutorial.ejemplos.VariosPasos;
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

        recurso = new Recurso(actividad);
    }

    public ViewGroup getCapitilo(int numero) {

        ViewGroup vista = new LinearLayout(contexto);

        switch (numero) {
            case 0:
                vista = gitVista1();

                break;
            case 1:
                vista = gitVista2();

                break;

            case 2:
                vista = gitVista3();

                break;
            case 3:
                vista = gitVista4();

                break;

            case 4:
                vista = gitVista5();

                break;
            case 5:
                vista = gitVista6();

                break;

            case 6:
                vista = gitVista7();

                break;
            case 7:
                vista = gitVista8();

                break;

            case 8:
                vista = gitVista9();

                break;
            case 9:
                vista = gitVista10();

                break;

            case 10:
                vista = gitVista11();

                break;
            case 11:
                vista = gitVista12();

                break;

            case 12:
                // vista = gitVista13();

                break;
            case 13:
                //   vista = gitVista14();

                break;

            case 14:
                //     vista = gitVista15();

                break;
            case 15:
                //    vista = gitVista16();

                break;

            case 16:
                //      vista = gitVista17();

                break;
            case 17:
                //    vista = gitVista18();

                break;

            case 18:
                //     vista = gitVista19();

                break;

            case 19:
                //     vista = gitVista20();

                break;
            case 20:
                //     vista = gitVista21();

                break;

            case 21:
                //     vista = gitVista22();

                break;

            case 22:
                //     vista = gitVista23();

                break;
            case 23:
                //     vista = gitVista24();

                break;

            case 24:
                //     vista = gitVista25();

                break;

            case 25:
                //     vista = gitVista26();

                break;

            case 26:
                //     vista = gitVista27();

                break;

            case 27:
                //     vista = gitVista28();

                break;
            case 28:
                //     vista = gitVista29();

                break;
            case 29:
                //     vista = gitVista30();

                break;
            case 30:
                //     vista = gitVista30();

                break;

            default:
                break;
        }

        return vista;
    }

    private ViewGroup gitVista1() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!parrafoTexto.isEmpty()) {

                    TextView parrafo = new TextView(contexto);

                    parrafo.setTextSize(18);

                    parrafo.setTextColor(Color.BLACK);

                    parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                    parrafo.setText(parrafoTexto);

                    disenoPrincipal.addView(parrafo, parametrosUI1);
                }

                if (!tituloTexto.isEmpty()) {

                    TextView tituloCodigo = new TextView(contexto);

                    tituloCodigo.setTextColor(Color.BLUE);

                    tituloCodigo.setPadding(0, 0, 0, 16);

                    tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                    tituloCodigo.setTextSize(28);

                    tituloCodigo.setText(tituloTexto);

                    disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                }

                if (!imagenTexto.isEmpty()) {

                    TextView imagen = new TextView(contexto);

                    imagen.setTextSize(28);

                    imagen.setTextColor(Color.GREEN);

                    imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                    disenoPrincipal.addView(imagen, parametrosUI1);
                }

                if (!codigoTexto.isEmpty()) {

                    TextView codigo = new TextView(contexto);

                    codigo.setTextSize(18);

                    codigo.setTextColor(Color.BLUE);

                    codigo.setText(codigoTexto);

                    CodeFormatter fuente = new CodeFormatter();

                    disenoPrincipal.addView(codigo, parametrosUI1);

                    disenoPrincipal.addView(fuente.formatCode(contexto, codigoTexto));
                }

                if (!ejemploTexto.isEmpty()) {

                    TextView ejemplo = new TextView(contexto);

                    ejemplo.setTextSize(18);

                    ejemplo.setTextColor(Color.RED);

                    ejemplo.setText(ejemploTexto);

                    disenoPrincipal.addView(ejemplo, parametrosUI1);
                }

                if (!notaTexto.isEmpty()) {

                    TextView nota = new TextView(contexto);

                    nota.setTextSize(18);

                    nota.setTextColor(Color.GREEN);

                    nota.setText(notaTexto);

                    disenoPrincipal.addView(nota, parametrosUI1);
                }

                if (!recursoTexto.isEmpty()) {

                    TextView recurso = new TextView(contexto);

                    recurso.setTextSize(18);

                    recurso.setTextColor(Color.RED);

                    recurso.setText(recursoTexto);

                    disenoPrincipal.addView(recurso, parametrosUI1);
                }

                if (!vistaTexto.isEmpty()) {

                    TextView vista = new TextView(contexto);

                    vista.setTextSize(18);

                    vista.setTextColor(Color.RED);

                    vista.setText(vistaTexto);

                    disenoPrincipal.addView(vista, parametrosUI1);
                }

                if (capituloTexto.equals("Capitulo 1")) {

                    cursor.close();

                    db.close();

                    return disenoPrincipal;
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista2() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 1")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(18);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(18);

                        nota.setTextColor(Color.GREEN);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(18);

                        recurso.setTextColor(Color.RED);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);
                    }

                    if (!vistaTexto.isEmpty()) {

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        disenoPrincipal.addView(vista, parametrosUI1);
                    }

                    if (capituloTexto.equals("Capitulo 2")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista3() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 2")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 1.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(18);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(18);

                        nota.setTextColor(Color.GREEN);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(18);

                        recurso.setTextColor(Color.RED);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);
                    }

                    if (!vistaTexto.isEmpty()) {

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        disenoPrincipal.addView(vista, parametrosUI1);
                    }

                    if (capituloTexto.equals("Capitulo 3")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista4() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        int numeroVistas = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 3")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 2.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(28);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(28);

                        nota.setTextColor(Color.GREEN);

                        nota.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(28);

                        recurso.setTextColor(Color.RED);

                        recurso.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);
                    }

                    if (!vistaTexto.isEmpty()) {

                        numeroVistas++;

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        final LinearLayout diseno = new LinearLayout(contexto);

                        LayoutParams parametros =
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        diseno.setOrientation(LinearLayout.VERTICAL);

                        final PopupWindow ventana =
                                new PopupWindow(
                                        diseno,
                                        actividad.getWindowManager().getDefaultDisplay().getWidth(),
                                        actividad.getWindowManager().getDefaultDisplay().getHeight()
                                                / 2,
                                        true);

                        Button desplegar = new Button(contexto);

                        desplegar.setText("Ver Ejemplo");

                        desplegar.setTextColor(Color.WHITE);
                        desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                        desplegar.setPadding(16, 8, 16, 8);
                        desplegar.setTypeface(null, Typeface.BOLD);
                        desplegar.setAllCaps(false);

                        LinearLayout.LayoutParams buttonParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.setMargins(0, 16, 0, 0);
                        desplegar.setLayoutParams(buttonParams);

                        final Paso3 paso3 = new Paso3(contexto);

                        if (numeroVistas == 1) {

                            paso3.setColor(255, 255, 255, 255);
                        }

                        if (numeroVistas == 2) {

                            paso3.setColor(255, 200, 200, 200);
                        }

                        if (numeroVistas == 3) {

                            paso3.setColor(255, 200, 200, 200);
                        }

                        diseno.addView(paso3, parametros);

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        paso3.reiniciar();

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });
                    }

                    if (capituloTexto.equals("Capitulo 4")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista5() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        int numeroVistas = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 4")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 2.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(28);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(28);

                        nota.setTextColor(Color.GREEN);

                        nota.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(28);

                        recurso.setTextColor(Color.RED);

                        recurso.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);
                    }

                    if (!vistaTexto.isEmpty()) {

                        numeroVistas++;

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        final LinearLayout diseno = new LinearLayout(contexto);

                        LayoutParams parametros =
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        diseno.setOrientation(LinearLayout.VERTICAL);

                        final PopupWindow ventana =
                                new PopupWindow(
                                        diseno,
                                        actividad.getWindowManager().getDefaultDisplay().getWidth(),
                                        actividad.getWindowManager().getDefaultDisplay().getHeight()
                                                / 2,
                                        true);

                        Button desplegar = new Button(contexto);

                        desplegar.setText("Ver Ejemplo");

                        desplegar.setTextColor(Color.WHITE);
                        desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                        desplegar.setPadding(16, 8, 16, 8);
                        desplegar.setTypeface(null, Typeface.BOLD);
                        desplegar.setAllCaps(false);

                        LinearLayout.LayoutParams buttonParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.setMargins(0, 16, 0, 0);
                        desplegar.setLayoutParams(buttonParams);

                        final Paso4 paso4 = new Paso4(contexto);

                        final VariosPasos pasos = new VariosPasos(actividad);

                        pasos.resumen();

                        if (numeroVistas == 1) {

                            paso4.setColor(255, 200, 200, 200);

                            diseno.addView(paso4, parametros);
                        }

                        if (numeroVistas == 2) {

                            diseno.addView(pasos, parametros);
                        }

                        if (numeroVistas == 3) {

                            diseno.addView(pasos, parametros);
                        }

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        paso4.reiniciar();

                                        pasos.reiniciar();

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });
                    }

                    if (capituloTexto.equals("Capitulo 5")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista6() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        int numeroVistas = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 5")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 2.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(28);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(28);

                        nota.setTextColor(Color.GREEN);

                        nota.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(28);

                        recurso.setTextColor(Color.RED);

                        recurso.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);
                    }

                    if (!vistaTexto.isEmpty()) {

                        numeroVistas++;

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        final LinearLayout diseno = new LinearLayout(contexto);

                        LayoutParams parametros =
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        diseno.setOrientation(LinearLayout.VERTICAL);

                        final PopupWindow ventana =
                                new PopupWindow(
                                        diseno,
                                        actividad.getWindowManager().getDefaultDisplay().getWidth(),
                                        actividad.getWindowManager().getDefaultDisplay().getHeight()
                                                / 2,
                                        true);

                        Button desplegar = new Button(contexto);

                        desplegar.setText("Ver Ejemplo");

                        desplegar.setTextColor(Color.WHITE);
                        desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                        desplegar.setPadding(16, 8, 16, 8);
                        desplegar.setTypeface(null, Typeface.BOLD);
                        desplegar.setAllCaps(false);

                        LinearLayout.LayoutParams buttonParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.setMargins(0, 16, 0, 0);
                        desplegar.setLayoutParams(buttonParams);

                        final Paso5 paso = new Paso5(actividad);

                        paso.resumen();

                        if (numeroVistas == 1) {

                            diseno.addView(paso, parametros);
                        }

                        if (numeroVistas == 2) {

                            diseno.addView(paso, parametros);
                        }

                        if (numeroVistas == 3) {

                            diseno.addView(paso, parametros);
                        }

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        paso.reiniciar();

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });
                    }

                    if (capituloTexto.equals("Capitulo 6")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista7() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        int numeroVistas = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 6")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 2.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(28);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(28);

                        nota.setTextColor(Color.GREEN);

                        nota.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(28);

                        recurso.setTextColor(Color.RED);

                        recurso.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);
                    }

                    if (!vistaTexto.isEmpty()) {

                        numeroVistas++;

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        final LinearLayout diseno = new LinearLayout(contexto);

                        LayoutParams parametros =
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        diseno.setOrientation(LinearLayout.VERTICAL);

                        final PopupWindow ventana =
                                new PopupWindow(
                                        diseno,
                                        actividad.getWindowManager().getDefaultDisplay().getWidth(),
                                        actividad.getWindowManager().getDefaultDisplay().getHeight()
                                                / 2,
                                        true);

                        Button desplegar = new Button(contexto);

                        desplegar.setText("Ver Ejemplo");

                        desplegar.setTextColor(Color.WHITE);
                        desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                        desplegar.setPadding(16, 8, 16, 8);
                        desplegar.setTypeface(null, Typeface.BOLD);
                        desplegar.setAllCaps(false);

                        LinearLayout.LayoutParams buttonParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.setMargins(0, 16, 0, 0);
                        desplegar.setLayoutParams(buttonParams);

                        final Paso6 paso = new Paso6(actividad);

                        paso.resumen();

                        if (numeroVistas == 1) {

                            diseno.addView(paso, parametros);
                        }

                        if (numeroVistas == 2) {

                            diseno.addView(paso, parametros);
                        }

                        if (numeroVistas == 3) {

                            diseno.addView(paso, parametros);
                        }

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        paso.reiniciar();

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });
                    }

                    if (capituloTexto.equals("Capitulo 7")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista8() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        int numeroVistas = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 7")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 2.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(28);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(28);

                        nota.setTextColor(Color.GREEN);

                        nota.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(28);

                        recurso.setTextColor(Color.RED);

                        recurso.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);
                    }

                    if (!vistaTexto.isEmpty()) {

                        numeroVistas++;

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        final LinearLayout diseno = new LinearLayout(contexto);

                        LayoutParams parametros =
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        diseno.setOrientation(LinearLayout.VERTICAL);

                        final PopupWindow ventana =
                                new PopupWindow(
                                        diseno,
                                        actividad.getWindowManager().getDefaultDisplay().getWidth(),
                                        actividad.getWindowManager().getDefaultDisplay().getHeight()
                                                / 2,
                                        true);

                        Button desplegar = new Button(contexto);

                        desplegar.setText("Ver Ejemplo");

                        desplegar.setTextColor(Color.WHITE);
                        desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                        desplegar.setPadding(16, 8, 16, 8);
                        desplegar.setTypeface(null, Typeface.BOLD);
                        desplegar.setAllCaps(false);

                        LinearLayout.LayoutParams buttonParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.setMargins(0, 16, 0, 0);
                        desplegar.setLayoutParams(buttonParams);

                        final Paso7 paso = new Paso7(actividad);

                        paso.resumen();

                        if (numeroVistas == 1) {

                            diseno.addView(paso, parametros);
                        }

                        if (numeroVistas == 2) {

                            diseno.addView(paso, parametros);
                        }

                        if (numeroVistas == 3) {

                            diseno.addView(paso, parametros);
                        }

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });
                    }

                    if (capituloTexto.equals("Capitulo 8")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista9() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        int numeroVistas = 0;

        int numeroRecursos = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 8")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 2.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(28);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(28);

                        nota.setTextColor(Color.GREEN);

                        nota.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        numeroRecursos++;

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(28);

                        recurso.setTextColor(Color.RED);

                        recurso.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);

                        if (numeroRecursos == 1) {
                            TextView img = new TextView(contexto);

                            img.setTextSize(18);

                            img.setTextColor(Color.GREEN);

                            img.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                            img.setText("burbuja.png");

                            disenoPrincipal.addView(img, parametrosUI1);
                        }
                        if (numeroRecursos == 2) {
                            TextView aud = new TextView(contexto);

                            aud.setTextSize(18);

                            aud.setTextColor(Color.GREEN);

                            aud.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                            aud.setText("explosion.wav");

                            disenoPrincipal.addView(aud, parametrosUI1);
                        }
                    }

                    if (!vistaTexto.isEmpty()) {

                        numeroVistas++;

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        final LinearLayout diseno = new LinearLayout(contexto);

                        LayoutParams parametros =
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        diseno.setOrientation(LinearLayout.VERTICAL);

                        final PopupWindow ventana =
                                new PopupWindow(
                                        diseno,
                                        actividad.getWindowManager().getDefaultDisplay().getWidth(),
                                        actividad.getWindowManager().getDefaultDisplay().getHeight()
                                                / 2,
                                        true);

                        Button desplegar = new Button(contexto);

                        desplegar.setText("Ver Ejemplo");

                        desplegar.setTextColor(Color.WHITE);
                        desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                        desplegar.setPadding(16, 8, 16, 8);
                        desplegar.setTypeface(null, Typeface.BOLD);
                        desplegar.setAllCaps(false);

                        LinearLayout.LayoutParams buttonParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.setMargins(0, 16, 0, 0);
                        desplegar.setLayoutParams(buttonParams);

                        Paso8 paso = new Paso8(actividad);

                        paso.resumen();

                        diseno.addView(paso, parametros);

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });
                    }

                    if (capituloTexto.equals("Capitulo 9")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista10() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        int numeroVistas = 0;

        int numeroRecursos = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 9")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 2.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(28);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(28);

                        nota.setTextColor(Color.GREEN);

                        nota.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        numeroRecursos++;

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(28);

                        recurso.setTextColor(Color.RED);

                        recurso.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);
                        if (numeroRecursos == 1) {
                            TextView img = new TextView(contexto);

                            img.setTextSize(18);

                            img.setTextColor(Color.GREEN);

                            img.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                            img.setText("burbuja.png");

                            disenoPrincipal.addView(img, parametrosUI1);
                        }
                        if (numeroRecursos == 2) {
                            TextView aud = new TextView(contexto);

                            aud.setTextSize(18);

                            aud.setTextColor(Color.GREEN);

                            aud.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                            aud.setText("explosion.wav");

                            disenoPrincipal.addView(aud, parametrosUI1);
                        }
                    }

                    if (!vistaTexto.isEmpty()) {

                        numeroVistas++;

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        final LinearLayout diseno = new LinearLayout(contexto);

                        LayoutParams parametros =
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        diseno.setOrientation(LinearLayout.VERTICAL);

                        final PopupWindow ventana =
                                new PopupWindow(
                                        diseno,
                                        actividad.getWindowManager().getDefaultDisplay().getWidth(),
                                        actividad.getWindowManager().getDefaultDisplay().getHeight()
                                                / 2,
                                        true);

                        Button desplegar = new Button(contexto);

                        desplegar.setText("Ver Ejemplo");

                        desplegar.setTextColor(Color.WHITE);
                        desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                        desplegar.setPadding(16, 8, 16, 8);
                        desplegar.setTypeface(null, Typeface.BOLD);
                        desplegar.setAllCaps(false);

                        LinearLayout.LayoutParams buttonParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.setMargins(0, 16, 0, 0);
                        desplegar.setLayoutParams(buttonParams);

                        Paso9 paso = new Paso9(actividad);

                        paso.resumen();

                        diseno.addView(paso, parametros);

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });
                    }

                    if (capituloTexto.equals("Capitulo 10")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista11() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        int numeroVistas = 0;

        int numeroRecursos = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 10")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 2.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(28);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(28);

                        nota.setTextColor(Color.GREEN);

                        nota.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        numeroRecursos++;

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(28);

                        recurso.setTextColor(Color.RED);

                        recurso.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);

                        if (numeroRecursos == 1) {
                            TextView img = new TextView(contexto);

                            img.setTextSize(18);

                            img.setTextColor(Color.GREEN);

                            img.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                            img.setText("burbuja.png");

                            disenoPrincipal.addView(img, parametrosUI1);
                        }
                        if (numeroRecursos == 2) {
                            TextView aud = new TextView(contexto);

                            aud.setTextSize(18);

                            aud.setTextColor(Color.GREEN);

                            aud.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                            aud.setText("explosion.wav");

                            disenoPrincipal.addView(aud, parametrosUI1);
                        }
                    }

                    if (!vistaTexto.isEmpty()) {

                        numeroVistas++;

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        final LinearLayout diseno = new LinearLayout(contexto);

                        LayoutParams parametros =
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        diseno.setOrientation(LinearLayout.VERTICAL);

                        final PopupWindow ventana =
                                new PopupWindow(
                                        diseno,
                                        actividad.getWindowManager().getDefaultDisplay().getWidth(),
                                        actividad.getWindowManager().getDefaultDisplay().getHeight()
                                                / 2,
                                        true);

                        Button desplegar = new Button(contexto);

                        desplegar.setText("Ver Ejemplo");

                        desplegar.setTextColor(Color.WHITE);
                        desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                        desplegar.setPadding(16, 8, 16, 8);
                        desplegar.setTypeface(null, Typeface.BOLD);
                        desplegar.setAllCaps(false);

                        LinearLayout.LayoutParams buttonParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.setMargins(0, 16, 0, 0);
                        desplegar.setLayoutParams(buttonParams);

                        Paso10 paso = new Paso10(actividad);

                        paso.resumen();

                        diseno.addView(paso, parametros);

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });
                    }

                    if (capituloTexto.equals("Capitulo 11")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista12() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        int numeroVistas = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 11")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 2.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(28);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(28);

                        nota.setTextColor(Color.GREEN);

                        nota.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(28);

                        recurso.setTextColor(Color.RED);

                        recurso.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);
                    }

                    if (!vistaTexto.isEmpty()) {

                        numeroVistas++;

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        final LinearLayout diseno = new LinearLayout(contexto);

                        LayoutParams parametros =
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        diseno.setOrientation(LinearLayout.VERTICAL);

                        final PopupWindow ventana =
                                new PopupWindow(
                                        diseno,
                                        actividad.getWindowManager().getDefaultDisplay().getWidth(),
                                        actividad.getWindowManager().getDefaultDisplay().getHeight()
                                                / 2,
                                        true);

                        Button desplegar = new Button(contexto);

                        desplegar.setText("Ver Ejemplo");

                        desplegar.setTextColor(Color.WHITE);
                        desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                        desplegar.setPadding(16, 8, 16, 8);
                        desplegar.setTypeface(null, Typeface.BOLD);
                        desplegar.setAllCaps(false);

                        LinearLayout.LayoutParams buttonParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.setMargins(0, 16, 0, 0);
                        desplegar.setLayoutParams(buttonParams);

                        Paso11 paso = new Paso11(actividad);

                        paso.resumen();

                        diseno.addView(paso, parametros);

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });
                    }

                    if (capituloTexto.equals("Capitulo 12")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista13() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        int numeroVistas = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 12")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 2.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(28);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(28);

                        nota.setTextColor(Color.GREEN);

                        nota.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(28);

                        recurso.setTextColor(Color.RED);

                        recurso.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);
                    }

                    if (!vistaTexto.isEmpty()) {

                        numeroVistas++;

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        final LinearLayout diseno = new LinearLayout(contexto);

                        LayoutParams parametros =
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        diseno.setOrientation(LinearLayout.VERTICAL);

                        final PopupWindow ventana =
                                new PopupWindow(
                                        diseno,
                                        actividad.getWindowManager().getDefaultDisplay().getWidth(),
                                        actividad.getWindowManager().getDefaultDisplay().getHeight()
                                                / 2,
                                        true);

                        Button desplegar = new Button(contexto);

                        desplegar.setText("Ver Ejemplo");

                        desplegar.setTextColor(Color.WHITE);
                        desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                        desplegar.setPadding(16, 8, 16, 8);
                        desplegar.setTypeface(null, Typeface.BOLD);
                        desplegar.setAllCaps(false);

                        LinearLayout.LayoutParams buttonParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.setMargins(0, 16, 0, 0);
                        desplegar.setLayoutParams(buttonParams);

                        final Paso3 paso3 = new Paso3(contexto);

                        if (numeroVistas == 1) {

                            paso3.setColor(255, 255, 255, 255);
                        }

                        if (numeroVistas == 2) {

                            paso3.setColor(255, 200, 200, 200);
                        }

                        if (numeroVistas == 3) {

                            paso3.setColor(255, 200, 200, 200);
                        }

                        diseno.addView(paso3, parametros);

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        paso3.reiniciar();

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });
                    }

                    if (capituloTexto.equals("Capitulo 13")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista14() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        int numeroVistas = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 13")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 2.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(28);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(28);

                        nota.setTextColor(Color.GREEN);

                        nota.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(28);

                        recurso.setTextColor(Color.RED);

                        recurso.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);
                    }

                    if (!vistaTexto.isEmpty()) {

                        numeroVistas++;

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        final LinearLayout diseno = new LinearLayout(contexto);

                        LayoutParams parametros =
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        diseno.setOrientation(LinearLayout.VERTICAL);

                        final PopupWindow ventana =
                                new PopupWindow(
                                        diseno,
                                        actividad.getWindowManager().getDefaultDisplay().getWidth(),
                                        actividad.getWindowManager().getDefaultDisplay().getHeight()
                                                / 2,
                                        true);

                        Button desplegar = new Button(contexto);

                        desplegar.setText("Ver Ejemplo");

                        desplegar.setTextColor(Color.WHITE);
                        desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                        desplegar.setPadding(16, 8, 16, 8);
                        desplegar.setTypeface(null, Typeface.BOLD);
                        desplegar.setAllCaps(false);

                        LinearLayout.LayoutParams buttonParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.setMargins(0, 16, 0, 0);
                        desplegar.setLayoutParams(buttonParams);

                        final Paso3 paso3 = new Paso3(contexto);

                        if (numeroVistas == 1) {

                            paso3.setColor(255, 255, 255, 255);
                        }

                        if (numeroVistas == 2) {

                            paso3.setColor(255, 200, 200, 200);
                        }

                        if (numeroVistas == 3) {

                            paso3.setColor(255, 200, 200, 200);
                        }

                        diseno.addView(paso3, parametros);

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        paso3.reiniciar();

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });
                    }

                    if (capituloTexto.equals("Capitulo 14")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista15() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        int numeroVistas = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 14")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 2.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(28);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(28);

                        nota.setTextColor(Color.GREEN);

                        nota.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(28);

                        recurso.setTextColor(Color.RED);

                        recurso.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);
                    }

                    if (!vistaTexto.isEmpty()) {

                        numeroVistas++;

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        final LinearLayout diseno = new LinearLayout(contexto);

                        LayoutParams parametros =
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        diseno.setOrientation(LinearLayout.VERTICAL);

                        final PopupWindow ventana =
                                new PopupWindow(
                                        diseno,
                                        actividad.getWindowManager().getDefaultDisplay().getWidth(),
                                        actividad.getWindowManager().getDefaultDisplay().getHeight()
                                                / 2,
                                        true);

                        Button desplegar = new Button(contexto);

                        desplegar.setText("Ver Ejemplo");

                        desplegar.setTextColor(Color.WHITE);
                        desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                        desplegar.setPadding(16, 8, 16, 8);
                        desplegar.setTypeface(null, Typeface.BOLD);
                        desplegar.setAllCaps(false);

                        LinearLayout.LayoutParams buttonParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.setMargins(0, 16, 0, 0);
                        desplegar.setLayoutParams(buttonParams);

                        final Paso3 paso3 = new Paso3(contexto);

                        if (numeroVistas == 1) {

                            paso3.setColor(255, 255, 255, 255);
                        }

                        if (numeroVistas == 2) {

                            paso3.setColor(255, 200, 200, 200);
                        }

                        if (numeroVistas == 3) {

                            paso3.setColor(255, 200, 200, 200);
                        }

                        diseno.addView(paso3, parametros);

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        paso3.reiniciar();

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });
                    }

                    if (capituloTexto.equals("Capitulo 15")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista16() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        int numeroVistas = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 15")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 2.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(28);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(28);

                        nota.setTextColor(Color.GREEN);

                        nota.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(28);

                        recurso.setTextColor(Color.RED);

                        recurso.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);
                    }

                    if (!vistaTexto.isEmpty()) {

                        numeroVistas++;

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        final LinearLayout diseno = new LinearLayout(contexto);

                        LayoutParams parametros =
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        diseno.setOrientation(LinearLayout.VERTICAL);

                        final PopupWindow ventana =
                                new PopupWindow(
                                        diseno,
                                        actividad.getWindowManager().getDefaultDisplay().getWidth(),
                                        actividad.getWindowManager().getDefaultDisplay().getHeight()
                                                / 2,
                                        true);

                        Button desplegar = new Button(contexto);

                        desplegar.setText("Ver Ejemplo");

                        desplegar.setTextColor(Color.WHITE);
                        desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                        desplegar.setPadding(16, 8, 16, 8);
                        desplegar.setTypeface(null, Typeface.BOLD);
                        desplegar.setAllCaps(false);

                        LinearLayout.LayoutParams buttonParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.setMargins(0, 16, 0, 0);
                        desplegar.setLayoutParams(buttonParams);

                        final Paso3 paso3 = new Paso3(contexto);

                        if (numeroVistas == 1) {

                            paso3.setColor(255, 255, 255, 255);
                        }

                        if (numeroVistas == 2) {

                            paso3.setColor(255, 200, 200, 200);
                        }

                        if (numeroVistas == 3) {

                            paso3.setColor(255, 200, 200, 200);
                        }

                        diseno.addView(paso3, parametros);

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        paso3.reiniciar();

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });
                    }

                    if (capituloTexto.equals("Capitulo 16")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista17() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        int numeroVistas = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 16")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 2.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(28);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(28);

                        nota.setTextColor(Color.GREEN);

                        nota.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(28);

                        recurso.setTextColor(Color.RED);

                        recurso.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);
                    }

                    if (!vistaTexto.isEmpty()) {

                        numeroVistas++;

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        final LinearLayout diseno = new LinearLayout(contexto);

                        LayoutParams parametros =
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        diseno.setOrientation(LinearLayout.VERTICAL);

                        final PopupWindow ventana =
                                new PopupWindow(
                                        diseno,
                                        actividad.getWindowManager().getDefaultDisplay().getWidth(),
                                        actividad.getWindowManager().getDefaultDisplay().getHeight()
                                                / 2,
                                        true);

                        Button desplegar = new Button(contexto);

                        desplegar.setText("Ver Ejemplo");

                        desplegar.setTextColor(Color.WHITE);
                        desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                        desplegar.setPadding(16, 8, 16, 8);
                        desplegar.setTypeface(null, Typeface.BOLD);
                        desplegar.setAllCaps(false);

                        LinearLayout.LayoutParams buttonParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.setMargins(0, 16, 0, 0);
                        desplegar.setLayoutParams(buttonParams);

                        final Paso3 paso3 = new Paso3(contexto);

                        if (numeroVistas == 1) {

                            paso3.setColor(255, 255, 255, 255);
                        }

                        if (numeroVistas == 2) {

                            paso3.setColor(255, 200, 200, 200);
                        }

                        if (numeroVistas == 3) {

                            paso3.setColor(255, 200, 200, 200);
                        }

                        diseno.addView(paso3, parametros);

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        paso3.reiniciar();

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });
                    }

                    if (capituloTexto.equals("Capitulo 17")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista18() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        int numeroVistas = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 17")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 2.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(28);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(28);

                        nota.setTextColor(Color.GREEN);

                        nota.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(28);

                        recurso.setTextColor(Color.RED);

                        recurso.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);
                    }

                    if (!vistaTexto.isEmpty()) {

                        numeroVistas++;

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        final LinearLayout diseno = new LinearLayout(contexto);

                        LayoutParams parametros =
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        diseno.setOrientation(LinearLayout.VERTICAL);

                        final PopupWindow ventana =
                                new PopupWindow(
                                        diseno,
                                        actividad.getWindowManager().getDefaultDisplay().getWidth(),
                                        actividad.getWindowManager().getDefaultDisplay().getHeight()
                                                / 2,
                                        true);

                        Button desplegar = new Button(contexto);

                        desplegar.setText("Ver Ejemplo");

                        desplegar.setTextColor(Color.WHITE);
                        desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                        desplegar.setPadding(16, 8, 16, 8);
                        desplegar.setTypeface(null, Typeface.BOLD);
                        desplegar.setAllCaps(false);

                        LinearLayout.LayoutParams buttonParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.setMargins(0, 16, 0, 0);
                        desplegar.setLayoutParams(buttonParams);

                        final Paso3 paso3 = new Paso3(contexto);

                        if (numeroVistas == 1) {

                            paso3.setColor(255, 255, 255, 255);
                        }

                        if (numeroVistas == 2) {

                            paso3.setColor(255, 200, 200, 200);
                        }

                        if (numeroVistas == 3) {

                            paso3.setColor(255, 200, 200, 200);
                        }

                        diseno.addView(paso3, parametros);

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        paso3.reiniciar();

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });
                    }

                    if (capituloTexto.equals("Capitulo 18")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }

    private ViewGroup gitVista19() {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("contenido", null, null, null, null, null, null);

        String capitulo = "";

        int numeroVistas = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                String tituloTexto = cursor.getString(cursor.getColumnIndex("titulo_codigo"));
                String parrafoTexto = cursor.getString(cursor.getColumnIndex("parrafo"));
                String notaTexto = cursor.getString(cursor.getColumnIndex("nota"));
                String ejemploTexto = cursor.getString(cursor.getColumnIndex("ejemplo"));
                String imagenTexto = cursor.getString(cursor.getColumnIndex("imagen"));
                String vistaTexto = cursor.getString(cursor.getColumnIndex("vista"));
                String recursoTexto = cursor.getString(cursor.getColumnIndex("recurso"));
                String codigoTexto = cursor.getString(cursor.getColumnIndex("codigo"));
                String capituloTexto = cursor.getString(cursor.getColumnIndex("capitulo"));

                if (!capituloTexto.isEmpty()) {
                    capitulo = capituloTexto;
                }

                if (capitulo.equals("Capitulo 18")) {

                    if (!parrafoTexto.isEmpty()) {

                        TextView parrafo = new TextView(contexto);

                        parrafo.setTextSize(18);

                        parrafo.setTextColor(Color.BLACK);

                        parrafo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        parrafo.setText(parrafoTexto);

                        disenoPrincipal.addView(parrafo, parametrosUI1);
                    }

                    if (!tituloTexto.isEmpty()) {

                        TextView tituloCodigo = new TextView(contexto);

                        tituloCodigo.setTextColor(Color.BLUE);

                        tituloCodigo.setPadding(0, 0, 0, 16);

                        tituloCodigo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        tituloCodigo.setTextSize(28);

                        tituloCodigo.setText(tituloTexto);

                        disenoPrincipal.addView(tituloCodigo, parametrosUI1);
                    }

                    if (!imagenTexto.isEmpty()) {

                        TextView imagen = new TextView(contexto);

                        imagen.setTextSize(28);

                        imagen.setTextColor(Color.GREEN);

                        imagen.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        imagen.setText(imagenTexto);

                        ImageView imagenR = new ImageView(contexto);

                        imagenR.setImageResource(R.mipmap.ic_launcher);

                        LinearLayout.LayoutParams imageParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 640);
                        imagenR.setLayoutParams(imageParams);
                        imagenR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imagenR.setImageBitmap(
                                new Textura2D(
                                                recurso.cargarTextura("Clase 2.jpg").getBipmap(),
                                                640,
                                                480)
                                        .getBipmap());

                        imagen.setText(imagenTexto);

                        disenoPrincipal.addView(imagen, parametrosUI1);
                        disenoPrincipal.addView(imagenR, parametrosUI1);
                    }

                    if (!codigoTexto.isEmpty()) {

                        LinearLayout codigoLayout = new LinearLayout(contexto);
                        codigoLayout.setOrientation(LinearLayout.VERTICAL);
                        codigoLayout.setPadding(24, 24, 24, 24);

                        // Crear background con bordes redondeados programáticamente
                        GradientDrawable backgroundDrawable = new GradientDrawable();
                        backgroundDrawable.setColor(
                                Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
                        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados
                        codigoLayout.setBackground(backgroundDrawable);

                        // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
                        codigoLayout.setElevation(8);

                        CodeFormatter fuente = new CodeFormatter();

                        codigoLayout.addView(fuente.formatCode(contexto, codigoTexto));

                        disenoPrincipal.addView(codigoLayout);
                    }
                    if (!ejemploTexto.isEmpty()) {

                        TextView ejemplo = new TextView(contexto);

                        ejemplo.setTextSize(28);

                        ejemplo.setTextColor(Color.RED);

                        ejemplo.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        ejemplo.setText(ejemploTexto);

                        disenoPrincipal.addView(ejemplo, parametrosUI1);
                    }

                    if (!notaTexto.isEmpty()) {

                        TextView nota = new TextView(contexto);

                        nota.setTextSize(28);

                        nota.setTextColor(Color.GREEN);

                        nota.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        nota.setText(notaTexto);

                        disenoPrincipal.addView(nota, parametrosUI1);
                    }

                    if (!recursoTexto.isEmpty()) {

                        TextView recurso = new TextView(contexto);

                        recurso.setTextSize(28);

                        recurso.setTextColor(Color.RED);

                        recurso.setShadowLayer(1.5f, -1, 1, Color.BLACK);

                        recurso.setText(recursoTexto);

                        disenoPrincipal.addView(recurso, parametrosUI1);
                    }

                    if (!vistaTexto.isEmpty()) {

                        numeroVistas++;

                        TextView vista = new TextView(contexto);

                        vista.setTextSize(18);

                        vista.setTextColor(Color.RED);

                        vista.setText(vistaTexto);

                        final LinearLayout diseno = new LinearLayout(contexto);

                        LayoutParams parametros =
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        diseno.setOrientation(LinearLayout.VERTICAL);

                        final PopupWindow ventana =
                                new PopupWindow(
                                        diseno,
                                        actividad.getWindowManager().getDefaultDisplay().getWidth(),
                                        actividad.getWindowManager().getDefaultDisplay().getHeight()
                                                / 2,
                                        true);

                        Button desplegar = new Button(contexto);

                        desplegar.setText("Ver Ejemplo");

                        desplegar.setTextColor(Color.WHITE);
                        desplegar.setBackgroundColor(Color.parseColor("#25D366"));
                        desplegar.setPadding(16, 8, 16, 8);
                        desplegar.setTypeface(null, Typeface.BOLD);
                        desplegar.setAllCaps(false);

                        LinearLayout.LayoutParams buttonParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParams.setMargins(0, 16, 0, 0);
                        desplegar.setLayoutParams(buttonParams);

                        final Paso3 paso3 = new Paso3(contexto);

                        if (numeroVistas == 1) {

                            paso3.setColor(255, 255, 255, 255);
                        }

                        if (numeroVistas == 2) {

                            paso3.setColor(255, 200, 200, 200);
                        }

                        if (numeroVistas == 3) {

                            paso3.setColor(255, 200, 200, 200);
                        }

                        diseno.addView(paso3, parametros);

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        paso3.reiniciar();

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });
                    }

                    if (capituloTexto.equals("Capitulo 19")) {

                        cursor.close();

                        db.close();

                        return disenoPrincipal;
                    }
                }
            }
        }

        cursor.close();

        db.close();

        return disenoPrincipal;
    }
}
