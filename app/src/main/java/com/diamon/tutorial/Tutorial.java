package com.diamon.tutorial;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.diamon.curso1.R;
import com.diamon.tutorial.archivo.basedatos.DatabaseHelper;
import com.diamon.tutorial.base.Vista;
import com.diamon.tutorial.capitulos.Capitulo;
import com.diamon.tutorial.ui.ListaExpandibleModificadaAdapter;
import com.diamon.utilidad.CodeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tutorial extends AppCompatActivity {

    private ExpandableListView listaExpandibleView;

    private HashMap<ViewGroup, List<ViewGroup>> listaInterna;

    private List<ViewGroup> listaExterna;

    private ListaExpandibleModificadaAdapter listaExpandibleAdapter;

    private boolean[] clicks;

    private Capitulo capitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        dbHelper.initializeDatabase();

        dbHelper.close();

        capitulo = new Capitulo(this, this, dbHelper);

        clicks = new boolean[31];

        for (int i = 0; i < clicks.length; ++i) {

            clicks[i] = false;
        }

        listaExpandibleView = new ExpandableListView(this);

        crearItemLista();

        listaExpandibleAdapter =
                new ListaExpandibleModificadaAdapter(this, listaInterna, listaExterna);

        listaExpandibleView.setAdapter(listaExpandibleAdapter);

        // ListView Child Click listener
        listaExpandibleView.setOnChildClickListener(
                new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(
                            ExpandableListView parent,
                            View v,
                            int groupPosition,
                            int childPosition,
                            long id) {

                        return false;
                    }
                });

        // ListView Group Click listener
        listaExpandibleView.setOnGroupClickListener(
                new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(
                            ExpandableListView parent, View v, int groupPosition, long id) {

                        return false;
                    }
                });

        // ListView Group Expand listener
        listaExpandibleView.setOnGroupExpandListener(
                new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int groupPosition) {

                        switch (groupPosition) {
                            case 0:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;

                            case 1:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;

                            case 2:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }
                                break;

                            case 3:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;
                            case 4:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;

                            case 5:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }
                                break;
                            case 6:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;

                            case 7:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;

                            case 8:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }
                                break;

                            case 9:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;
                            case 10:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;

                            case 11:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }
                                break;

                            case 12:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }
                                break;

                            case 13:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;
                            case 14:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;

                            case 15:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;
                            case 16:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;

                            case 17:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;
                            case 18:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;

                            case 19:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;

                            case 20:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;

                            case 21:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;
                            case 22:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;

                            case 23:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;
                            case 24:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;

                            case 25:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;
                            case 26:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;

                            case 27:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;
                            case 28:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;

                            case 29:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;
                            case 30:
                                if (!clicks[groupPosition]) {

                                    // Esto es igual porque la segunda lista tiene un solo item
                                    listaInterna
                                            .get(listaExterna.get(groupPosition))
                                            .get(0)
                                            .addView(capitulo.getCapitilo(groupPosition));

                                    clicks[groupPosition] = true;
                                }

                                break;

                            default:
                                break;
                        }
                    }
                });

        // List Group collapsed listener
        listaExpandibleView.setOnGroupCollapseListener(
                new ExpandableListView.OnGroupCollapseListener() {
                    @Override
                    public void onGroupCollapse(int groupPosition) {}
                });

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametros =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        layout.addView(listaExpandibleView, parametros);

        setContentView(layout);
    }

    private ViewGroup diseno(
            Context contexto,
            Activity actividad,
            String textos[],
            int imagenes[],
            String codigos[],
            final Vista vistas[]) {

        final LinearLayout disenoPrincipal = new LinearLayout(contexto);

        disenoPrincipal.setOrientation(LinearLayout.VERTICAL);

        LayoutParams parametrosUI1 =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        // final LinearLayout dis2 = new LinearLayout(this);

        //  dis2.setOrientation(LinearLayout.HORIZONTAL);

        int indiceCodigo = 0;

        int indiceImagen = 0;

        int indiceVista = 0;

        for (int i = 0; i < textos.length; i++) {

            TextView parrafo = new TextView(contexto);

            parrafo.setTextSize(18);

            parrafo.setTextColor(Color.BLACK);

            parrafo.setText(textos[i]);

            disenoPrincipal.addView(parrafo, parametrosUI1);

            if (textos[i] != null) {

                // Para Codigos

                if (textos[i].length() >= 6) {

                    if (textos[i].substring(0, 6).equals("Código")) {
                        parrafo.setTextSize(28);

                        parrafo.setTextColor(Color.BLUE);

                        CodeFormatter codigo = new CodeFormatter();

                        disenoPrincipal.addView(
                                codigo.formatCode(contexto, codigos[indiceCodigo]), parametrosUI1);

                        indiceCodigo++;
                    }
                }

                // Imagenes

                if (textos[i].length() >= 6) {

                    if (textos[i].substring(0, 6).equals("Imagen")) {
                        parrafo.setTextSize(28);

                        parrafo.setTextColor(Color.GREEN);

                        ImageView imagen = new ImageView(contexto);

                        imagen.setImageResource(imagenes[indiceImagen]);

                        disenoPrincipal.addView(imagen, parametrosUI1);

                        indiceImagen++;
                    }
                }

                // Para Ejemplo

                if (textos[i].length() >= 7) {

                    if (textos[i].substring(0, 7).equals("Ejemplo")) {
                        parrafo.setTextSize(28);

                        parrafo.setTextColor(Color.RED);
                    }
                }

                // Para Nota

                if (textos[i].length() >= 4) {

                    if (textos[i].substring(0, 4).equals("Nota")) {
                        parrafo.setTextSize(28);

                        parrafo.setTextColor(Color.RED);
                    }
                }

                // Para Recurso

                if (textos[i].length() >= 7) {

                    if (textos[i].substring(0, 7).equals("Recurso")) {
                        parrafo.setTextSize(28);

                        parrafo.setTextColor(Color.RED);
                    }
                }

                // Para Vistas

                if (textos[i].length() >= 23) {

                    if (textos[i].substring(0, 23).equals("Demostración Aproximada")) {
                        parrafo.setTextSize(28);

                        parrafo.setTextColor(Color.YELLOW);

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

                        diseno.addView(vistas[indiceVista].getVista(), parametros);

                        final int indice = indiceVista;

                        disenoPrincipal.addView(desplegar, parametrosUI1);

                        desplegar.setOnTouchListener(
                                new OnTouchListener() {

                                    @Override
                                    public boolean onTouch(View v, MotionEvent ev) {

                                        vistas[indice].reiniciar();

                                        ventana.showAtLocation(
                                                diseno,
                                                Gravity.TOP | Gravity.CENTER,
                                                LayoutParams.FILL_PARENT,
                                                LayoutParams.FILL_PARENT);

                                        return true;
                                    }
                                });

                        indiceVista++;
                    }
                }
            }
        }

        return disenoPrincipal;
    }

    private ViewGroup[] disenos(Context contexto, Activity actividad) {

        ViewGroup[] disenos = new ViewGroup[31];

        for (int i = 0; i < disenos.length; i++) {

            disenos[i] = new LinearLayout(this);

            ((LinearLayout) disenos[i]).setOrientation(LinearLayout.VERTICAL);
        }

        return disenos;
    }

    private ViewGroup crearIntem(Context contexto, int imgLogo, String titulo) {
        final LinearLayout interfazUI = new LinearLayout(contexto);

        interfazUI.setOrientation(LinearLayout.HORIZONTAL);

        LayoutParams parametrosUI =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        TextView texto = new TextView(this);

        ImageView imagen = new ImageView(this);

        imagen.setImageResource(imgLogo);

        interfazUI.setBackgroundColor(Color.LTGRAY);

        texto.setTextColor(Color.BLACK);

        texto.setTextSize(18);

        texto.setText(titulo);

        interfazUI.addView(imagen, parametrosUI);

        interfazUI.addView(texto, parametrosUI);

        return interfazUI;
    }

    private void crearItemLista() {

        ViewGroup dis1 = crearIntem(this, R.mipmap.ic_launcher, "Introducción");

        ViewGroup dis2 =
                crearIntem(
                        this,
                        R.mipmap.ic_launcher,
                        "Paso 1. Conociendo las herramientas de programación");

        ViewGroup dis3 = crearIntem(this, R.mipmap.ic_launcher, "Paso 2. Creando nuestro proyecto");

        ViewGroup dis4 =
                crearIntem(
                        this, R.mipmap.ic_launcher, "Paso 3. Conociendo las APIS graficas en Java");

        ViewGroup dis5 =
                crearIntem(
                        this,
                        R.mipmap.ic_launcher,
                        "Paso 4. Creando nuestro GameLoop o bucle principal del Juego");

        ViewGroup dis6 =
                crearIntem(
                        this,
                        R.mipmap.ic_launcher,
                        "Paso 5. Optimizando gráficos, implementado doble búfer y mostrar en pantalla completa el dibujo");

        ViewGroup dis7 =
                crearIntem(
                        this,
                        R.mipmap.ic_launcher,
                        "Paso 6. Controlando estados del Juego y manejar eventos de teclado");

        ViewGroup dis8 =
                crearIntem(
                        this,
                        R.mipmap.ic_launcher,
                        "Paso 7. Creando Pantallas del Juego y como gestionarlas");

        ViewGroup dis9 =
                crearIntem(
                        this,
                        R.mipmap.ic_launcher,
                        "Paso 8. Como cargar recursos del Juego y manejarlos");

        ViewGroup dis10 =
                crearIntem(
                        this,
                        R.mipmap.ic_launcher,
                        "Paso 9. Definiendo nuestros sprites o actores de nuestro juego");

        ViewGroup dis11 =
                crearIntem(
                        this,
                        R.mipmap.ic_launcher,
                        "Paso 10. Implementando persistencia de Datos y colisiones");

        ViewGroup dis12 =
                crearIntem(
                        this,
                        R.mipmap.ic_launcher,
                        "Paso 11. Comenzando con la creación de nuestro juego y dibujando una escena");

        ViewGroup dis13 = crearIntem(this, R.mipmap.ic_launcher, "Paso 12. Próximamente");

        ViewGroup dis14 = crearIntem(this, R.mipmap.ic_launcher, "Paso 13. Próximamente");

        ViewGroup dis15 = crearIntem(this, R.mipmap.ic_launcher, "Paso 14. Próximamente");

        ViewGroup dis16 = crearIntem(this, R.mipmap.ic_launcher, "Paso 15. Próximamente");

        ViewGroup dis17 = crearIntem(this, R.mipmap.ic_launcher, "Paso 16. Próximamente");

        ViewGroup dis18 = crearIntem(this, R.mipmap.ic_launcher, "Paso 17. Próximamente");

        ViewGroup dis19 = crearIntem(this, R.mipmap.ic_launcher, "Paso 18. Próximamente");

        ViewGroup dis20 = crearIntem(this, R.mipmap.ic_launcher, "Paso 19. Próximamente");

        ViewGroup dis21 = crearIntem(this, R.mipmap.ic_launcher, "Paso 20. Próximamente");

        ViewGroup dis22 = crearIntem(this, R.mipmap.ic_launcher, "Paso 21. Próximamente");

        ViewGroup dis23 = crearIntem(this, R.mipmap.ic_launcher, "Paso 22. Próximamente");

        ViewGroup dis24 = crearIntem(this, R.mipmap.ic_launcher, "Paso 23. Próximamente");

        ViewGroup dis25 = crearIntem(this, R.mipmap.ic_launcher, "Paso 24. Próximamente");

        ViewGroup dis26 = crearIntem(this, R.mipmap.ic_launcher, "Paso 25. Próximamente");

        ViewGroup dis27 = crearIntem(this, R.mipmap.ic_launcher, "Paso 26. Próximamente");

        ViewGroup dis28 = crearIntem(this, R.mipmap.ic_launcher, "Paso 27. Próximamente");

        ViewGroup dis29 = crearIntem(this, R.mipmap.ic_launcher, "Paso 28. Próximamente");

        ViewGroup dis30 = crearIntem(this, R.mipmap.ic_launcher, "Paso 29. Próximamente");

        ViewGroup dis31 = crearIntem(this, R.mipmap.ic_launcher, "Paso 30. Próximamente");

        listaExterna = new ArrayList<ViewGroup>();

        listaInterna = new HashMap<ViewGroup, List<ViewGroup>>();

        listaExterna.add(dis1);

        listaExterna.add(dis2);

        listaExterna.add(dis3);

        listaExterna.add(dis4);

        listaExterna.add(dis5);

        listaExterna.add(dis6);

        listaExterna.add(dis7);

        listaExterna.add(dis8);

        listaExterna.add(dis9);

        listaExterna.add(dis10);

        listaExterna.add(dis11);

        listaExterna.add(dis12);

        listaExterna.add(dis13);

        listaExterna.add(dis14);

        listaExterna.add(dis15);

        listaExterna.add(dis16);

        listaExterna.add(dis17);

        listaExterna.add(dis18);

        listaExterna.add(dis19);

        listaExterna.add(dis20);

        listaExterna.add(dis21);

        listaExterna.add(dis22);

        listaExterna.add(dis23);

        listaExterna.add(dis24);

        listaExterna.add(dis25);

        listaExterna.add(dis26);

        listaExterna.add(dis27);

        listaExterna.add(dis28);

        listaExterna.add(dis29);

        listaExterna.add(dis30);

        listaExterna.add(dis31);

        for (int i = 0; i < listaExterna.size(); i++) {

            List<ViewGroup> contenido = new ArrayList<ViewGroup>();

            contenido.add(disenos(this, this)[i]);

            listaInterna.put(listaExterna.get(i), contenido);
        }
    }
}
