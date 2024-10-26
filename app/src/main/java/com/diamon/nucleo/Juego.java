package com.diamon.nucleo;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.diamon.dato.ConfiguracionesDeJuego;
import com.diamon.dato.DatosJuego;
import com.diamon.entrada.EntradaDeControles;
import com.diamon.graficos.Graficos2D;
import com.diamon.graficos.Textura2D;
import com.diamon.nucleo.Entrada.EventoDeTecla;
import com.diamon.nucleo.Entrada.EventoDeToque;
import com.diamon.nucleo.Graficos.FormatoTextura;
import com.diamon.utilidad.Recurso;

import java.util.List;

public abstract class Juego extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    public static final float ANCHO_PANTALLA = 640;

    public static final float ALTO_PANTALLA = 480;

    public static final float DELTA_A_PIXEL = 0.0166666666666667F;

    public static final int FPS = 60;

    private Thread hilo;

    public static final String TITULO_JUEGO = "Final Mision";

    private volatile boolean iniciar;

    private static final int UNIDAD_TIEMPO = 1000000000;

    private double delta;

    private static final byte CICLOS = 60;

    private static final double LIMITE_CICLOS = UNIDAD_TIEMPO / CICLOS;

    protected Pantalla pantalla;

    private SurfaceHolder holder;

    protected Recurso recurso;

    protected DatosJuego datos;

    protected ConfiguracionesDeJuego configuracionesDeJuego;

    private Textura bufer;

    protected EntradaDeControles entraDeControles;

    private boolean mostrarFPS;

    private boolean colorAzul;

    @SuppressWarnings("deprecation")
    public Juego(Activity actividad, int ancho, int alto) {

        super(actividad);

        mostrarFPS = true;

        colorAzul = false;

        bufer = new Textura2D(ancho, alto, FormatoTextura.ARGB8888);

        float escalaX = (float) ancho / actividad.getWindowManager().getDefaultDisplay().getWidth();

        float escalaY = (float) alto / actividad.getWindowManager().getDefaultDisplay().getHeight();

        configuracionesDeJuego =
                new ConfiguracionesDeJuego(actividad, ConfiguracionesDeJuego.INTERNO);

        configuracionesDeJuego = configuracionesDeJuego.cargarConfiguraciones();

        if (configuracionesDeJuego.isLeerDatosAsset()) {

            ConfiguracionesDeJuego configuracionesDeJuegoInterna =
                    new ConfiguracionesDeJuego(actividad, ConfiguracionesDeJuego.ASSET);

            configuracionesDeJuego = configuracionesDeJuegoInterna.cargarConfiguraciones();

            configuracionesDeJuego.setLeerDatosAsset(false);

            configuracionesDeJuego.guardarConfiguraciones();
        }

        delta = 0;

        hilo = new Thread(this);

        iniciar = false;

        pantalla = null;

        holder = getHolder();

        holder.addCallback(this);

        holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);

        recurso = new Recurso(actividad);

        entraDeControles = new EntradaDeControles(actividad, this, escalaX, escalaY);

        setFocusable(true);

        requestFocus();

        setFocusableInTouchMode(true);
    }

    @Override
    public void run() {

        Rect rectangulo = new Rect();

        Canvas pincel;

        final Graficos pincelBufer = new Graficos2D(bufer);

        double referencia = System.nanoTime();

        while (iniciar) {

            final double tiempoInicial = System.nanoTime();

            delta = (float) (tiempoInicial - referencia) / UNIDAD_TIEMPO;

            if (!holder.getSurface().isValid()) {
                continue;
            }

            eventos();

            colisiones();

            actualizar((float) delta);

            pincel = holder.getSurface().lockHardwareCanvas();

            pincel.getClipBounds(rectangulo);

            pincelBufer.limpiar(Color.BLUE);

            renderizar(pincelBufer, (float) delta);

            pincel.drawBitmap(bufer.getBipmap(), null, rectangulo, null);

            holder.getSurface().unlockCanvasAndPost(pincel);

            referencia = tiempoInicial;

            do {

                Thread.yield();

            } while (System.nanoTime() - tiempoInicial < LIMITE_CICLOS);
        }
    }

    public void renderizar(Graficos pincel, float delta) {
        if (pantalla != null) {
            pantalla.dibujar(pincel, delta);
        }

        if (mostrarFPS) {

            pincel.getLapiz().setTextSize(20);

            if (colorAzul) {

                pincel.dibujarTexto((int) getFPS() + " FPS", 20, 20, Color.BLUE);

            } else {
                pincel.dibujarTexto((int) getFPS() + " FPS", 20, 20, Color.GREEN);
            }
        }
    }

    public void actualizar(float delta) {
        if (pantalla != null) {
            pantalla.actualizar(delta);
        }
    }

    public void colisiones() {
        if (pantalla != null) {
            pantalla.colisiones();
        }
    }

    public void reajustarPantalla(int ancho, int alto) {
        if (pantalla != null) {
            pantalla.reajustarPantalla(ancho, alto);
        }
    }

    public void resumen() {

        if (pantalla != null) {

            iniciar = true;
            hilo = new Thread(this);
            hilo.start();
            pantalla.mostrar();
            pantalla.resume();
        }
    }

    public void pausa() {
        if (pantalla != null) {

            pantalla.pausa();
            iniciar = false;
            while (true) {
                try {
                    hilo.join();

                    return;

                } catch (InterruptedException e) {

                }
            }
        }
    }

    public void liberarRecursos() {
        if (pantalla != null) {

            pantalla.ocultar();

            pantalla.liberarRecursos();
        }
    }

    public void setPantalla(Pantalla pantalla) {
        if (this.pantalla != null) {

            this.pantalla.ocultar();
        }

        this.pantalla = pantalla;

        if (this.pantalla != null) {

            this.pantalla.mostrar();

            this.pantalla.reajustarPantalla(getWidth(), getHeight());
        }
    }

    public Pantalla getPantalla() {

        return pantalla;
    }

    public float getFPS() {

        return (float) (1 / delta);
    }

    public void teclaPresionada(int codigoDeTecla) {

        if (pantalla != null) {

            pantalla.teclaPresionada(codigoDeTecla);
        }
    }

    public void teclaLevantada(int codigoDeTecla) {

        if (pantalla != null) {

            pantalla.teclaLevantada(codigoDeTecla);
        }
    }

    public void toquePresionado(float x, float y, int puntero) {

        if (pantalla != null) {

            pantalla.toquePresionado(x, y, puntero);
        }
    }

    public void toqueLevantado(float x, float y, int puntero) {

        if (pantalla != null) {

            pantalla.toqueLevantado(x, y, puntero);
        }
    }

    public void toqueDeslizando(float x, float y, int puntero) {

        if (pantalla != null) {

            pantalla.toqueDeslizando(x, y, puntero);
        }
    }

    public void acelerometro(float x, float y, float z) {

        if (pantalla != null) {

            pantalla.acelerometro(x, y, z);
        }
    }

    private void eventos() {

        List<EventoDeToque> eventosDeToque = entraDeControles.getEventosDeToque();

        List<EventoDeTecla> eventosDeTecla = entraDeControles.getEventosDeTecla();

        acelerometro(
                entraDeControles.getAcelerometroEnX(),
                entraDeControles.getAcelerometroEnY(),
                entraDeControles.getAcelerometroEnZ());

        for (int i = 0; i < eventosDeToque.size(); i++) {

            EventoDeToque eventoDeToque = eventosDeToque.get(i);

            switch (eventoDeToque.tipoEventoDeToque) {
                case EventoDeToque.TOQUE_ARRIBA:
                    this.toqueLevantado(eventoDeToque.x, eventoDeToque.y, eventoDeToque.puntero);

                    break;

                case EventoDeToque.TOQUE_ABAJO:
                    this.toquePresionado(eventoDeToque.x, eventoDeToque.y, eventoDeToque.puntero);

                    break;

                case EventoDeToque.TOQUE_DESLIZANDO:
                    this.toqueDeslizando(eventoDeToque.x, eventoDeToque.y, eventoDeToque.puntero);

                    break;

                default:
                    break;
            }
        }

        for (int i = 0; i < eventosDeTecla.size(); i++) {

            EventoDeTecla eventoDeTecla = eventosDeTecla.get(i);

            switch (eventoDeTecla.tipoEventoDeTecla) {
                case EventoDeTecla.TECLA_ARRIBA:
                    this.teclaLevantada(eventoDeTecla.codigoDeTecla);

                    break;

                case EventoDeTecla.TECLA_ABAJO:
                    this.teclaPresionada(eventoDeTecla.codigoDeTecla);

                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfeceHolder) {}

    @Override
    public void surfaceChanged(SurfaceHolder surfeceHolder, int p2, int p3, int p4) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder surfeceHolder) {}

    public Recurso getRecurso() {
        return recurso;
    }

    public ConfiguracionesDeJuego getConfiguracionesDeJuego() {
        return configuracionesDeJuego;
    }

    public EntradaDeControles getEntraDeControles() {
        return entraDeControles;
    }

    public boolean getMostrarFPS() {
        return this.mostrarFPS;
    }

    public void setMostrarFPS(boolean mostrarFPS) {
        this.mostrarFPS = mostrarFPS;
    }

    public boolean getColorAzul() {
        return this.colorAzul;
    }

    public void setColorAzul(boolean colorAzul) {
        this.colorAzul = colorAzul;
    }
}
