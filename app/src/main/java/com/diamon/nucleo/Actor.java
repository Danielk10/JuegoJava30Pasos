package com.diamon.nucleo;

import com.diamon.dato.ConfiguracionesDeJuego;
import com.diamon.graficos.Animacion2D;
import com.diamon.graficos.Pantalla2D;
import com.diamon.graficos.Textura2D;
import com.diamon.utilidad.Rectangulo;
import com.diamon.utilidad.Recurso;

import java.util.ArrayList;

public abstract class Actor {

    protected float x;

    protected float y;

    protected float ancho;

    protected float alto;

    protected boolean remover;

    private float tiempo;

    protected Textura[] texturas;

    protected Animacion2D animacion;

    protected Pantalla pantalla;

    protected Recurso recurso;

    protected float tiempoAnimacion;

    protected ConfiguracionesDeJuego configuracionesDeJuego;

    protected ArrayList<Actor> actores;

    protected boolean animar;

    public Actor(Pantalla pantalla, Textura textura, float x, float y) {

        this.pantalla = pantalla;

        this.actores = ((Pantalla2D) pantalla).getActores();

        this.recurso = ((Pantalla2D) pantalla).getJuego().getRecurso();

        this.configuracionesDeJuego =
                ((Pantalla2D) pantalla).getJuego().getConfiguracionesDeJuego();

        this.x = x;

        this.y = y;

        tiempo = 0;

        texturas = new Textura[1];

        texturas[0] = textura;

        tiempoAnimacion = 1;

        animacion = new Animacion2D(tiempoAnimacion / Juego.FPS, texturas);

        animacion.setModo(Animacion2D.NORMAL);

        ancho = textura.getAncho();

        alto = textura.getAlto();

        remover = false;

        animar = false;

        obtenerActores();
    }

    public Actor(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto) {

        this.pantalla = pantalla;

        this.actores = ((Pantalla2D) pantalla).getActores();

        this.recurso = ((Pantalla2D) pantalla).getJuego().getRecurso();

        this.configuracionesDeJuego =
                ((Pantalla2D) pantalla).getJuego().getConfiguracionesDeJuego();

        this.x = x;

        this.y = y;

        tiempo = 0;

        texturas = new Textura[1];

        texturas[0] = textura;

        tiempoAnimacion = 1;

        animacion = new Animacion2D(tiempoAnimacion / Juego.FPS, texturas);

        animacion.setModo(Animacion2D.NORMAL);

        this.ancho = ancho;

        this.alto = alto;

        this.setTamano(ancho, alto);

        remover = false;

        animar = false;

        obtenerActores();
    }

    public Actor(
            Pantalla pantalla,
            Textura[] texturas,
            float x,
            float y,
            float ancho,
            float alto,
            float tiempoAnimacion) {

        this.pantalla = pantalla;

        this.actores = ((Pantalla2D) pantalla).getActores();

        this.recurso = ((Pantalla2D) pantalla).getJuego().getRecurso();

        this.configuracionesDeJuego =
                ((Pantalla2D) pantalla).getJuego().getConfiguracionesDeJuego();

        this.x = x;

        this.y = y;

        tiempo = 0;

        this.texturas = texturas;

        this.tiempoAnimacion = tiempoAnimacion;

        animacion = new Animacion2D(tiempoAnimacion / Juego.FPS, texturas);

        animacion.setModo(Animacion2D.REPETIR);

        this.ancho = ancho;

        this.alto = alto;

        this.setTamano(ancho, alto);

        remover = false;

        animar = true;

        obtenerActores();
    }

    public void setAnimacion(Animacion2D animacion) {
        this.animacion = animacion;
    }

    public Animacion2D getAnimacion() {
        return animacion;
    }

    public void setPosicion(float x, float y) {

        this.x = x;

        this.y = y;
    }

    public boolean isRemover() {

        return remover;
    }

    public void actualizar(float delta) {

        if (animar) {

            if (delta == 0) {

                return;
            }

            if (delta > 0.1f) {

                delta = 0.1f;
            }

            tiempo += delta;
        }
    }

    public void dibujar(Graficos pincel, float delta) {

        if (animacion != null) {

            pincel.dibujarTextura(animacion.getKeyFrame(tiempo), x, y);
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setAncho(float ancho) {

        this.ancho = ancho;

        for (int i = 0; i < texturas.length; i++) {

            texturas[i] = new Textura2D(texturas[i].getBipmap(), ancho, alto);
        }
    }

    public void setAlto(float alto) {

        this.alto = alto;

        for (int i = 0; i < texturas.length; i++) {

            texturas[i] = new Textura2D(texturas[i].getBipmap(), ancho, alto);
        }
    }

    public float getAncho() {
        return ancho;
    }

    public float getAlto() {
        return alto;
    }

    public Rectangulo getRectangulo() {
        return new Rectangulo(x, y, ancho, alto);
    }

    public void setTamano(float ancho, float alto) {

        this.ancho = ancho;

        this.alto = alto;

        for (int i = 0; i < texturas.length; i++) {

            texturas[i] = new Textura2D(texturas[i].getBipmap(), ancho, alto);
        }
    }

    public abstract void obtenerActores();

    public abstract void colision(Actor actor);

    public void remover() {

        remover = true;
    }
}
