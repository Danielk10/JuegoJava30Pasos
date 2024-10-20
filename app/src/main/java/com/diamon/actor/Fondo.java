package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;

public class Fondo extends Actor {

    public static final float VELOCIDAD_FONDO = 1;

    public static final int HORIZONTAL_IZQUIERDA = 1;

    public static final int HORIZONTAL_DERECHA = 2;

    public static final int VERTICAL_ABAJO = 3;

    public static final int VERTICAL_ARRIBA = 4;

    private int direccion;

    private boolean parar;

    private float velocidad;

    private int nombre;

    public Fondo(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto) {
        super(pantalla, textura, x, y, ancho, alto);

        direccion = Fondo.HORIZONTAL_IZQUIERDA;

        parar = false;

        velocidad = VELOCIDAD_FONDO;

        nombre = 0;
    }

    public Fondo(Pantalla pantalla, Textura textura, float x, float y) {
        super(pantalla, textura, x, y);

        direccion = Fondo.HORIZONTAL_IZQUIERDA;

        parar = false;

        velocidad = VELOCIDAD_FONDO;

        nombre = 0;
    }

    public Fondo(
            Pantalla pantalla,
            Textura[] texturas,
            float x,
            float y,
            float ancho,
            float alto,
            float tiempoAnimacion) {
        super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

        direccion = Fondo.HORIZONTAL_IZQUIERDA;

        parar = false;

        velocidad = VELOCIDAD_FONDO;

        nombre = 0;
    }

    @Override
    public void obtenerActores() {
        // TODO: Implement this method
    }

    public float getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(float velocidad) {
        this.velocidad = velocidad;
    }

    public int getNombre() {
        return nombre;
    }

    public boolean isParar() {
        return parar;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public void setParar(boolean parar) {
        this.parar = parar;
    }

    @Override
    public void actualizar(float delta) {

        super.actualizar(delta);

        if (direccion == Fondo.HORIZONTAL_IZQUIERDA) {

            if (!parar) {
                x -= velocidad / Juego.DELTA_A_PIXEL * delta;
            }

            if (x <= -Juego.ANCHO_PANTALLA) {

                remover = true;
            }
        }

        if (direccion == Fondo.VERTICAL_ABAJO) {

            if (!parar) {

                y += velocidad / Juego.DELTA_A_PIXEL * delta;
            }

            if (y >= Juego.ALTO_PANTALLA) {

                remover = true;
            }
        }

        if (direccion == Fondo.VERTICAL_ARRIBA) {

            if (!parar) {
                y -= velocidad / Juego.DELTA_A_PIXEL * delta;
            }

            if (y <= -Juego.ALTO_PANTALLA) {

                remover = true;
            }
        }

        if (direccion == Fondo.HORIZONTAL_DERECHA) {

            if (!parar) {
                x += velocidad / Juego.DELTA_A_PIXEL * delta;
            }

            if (x >= Juego.ANCHO_PANTALLA) {

                remover = true;
            }
        }
    }

    public void setDireccion(int direccion) {
        this.direccion = direccion;
    }

    @Override
    public void colision(Actor actor) {}
}
