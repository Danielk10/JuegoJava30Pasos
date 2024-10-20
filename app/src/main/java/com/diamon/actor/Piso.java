package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;

public class Piso extends Actor {

    private static final float VELOCIDAD_PISO = 1;

    public Piso(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto) {
        super(pantalla, textura, x, y, ancho, alto);

        x = 640;

        y = Juego.ALTO_PANTALLA - 64;
    }

    public Piso(Pantalla pantalla, Textura textura, float x, float y) {
        super(pantalla, textura, x, y);

        x = 640;

        y = Juego.ALTO_PANTALLA - 64;
    }

    public Piso(
            Pantalla pantalla,
            Textura[] texturas,
            float x,
            float y,
            float ancho,
            float alto,
            float tiempoAnimacion) {
        super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

        x = 640;

        y = Juego.ALTO_PANTALLA - 64;
    }

    @Override
    public void obtenerActores() {
        // TODO: Implement this method
    }

    @Override
    public void actualizar(float delta) {

        super.actualizar(delta);

        x -= VELOCIDAD_PISO / Juego.DELTA_A_PIXEL * delta;

        if (x <= -128) {

            x = Juego.ANCHO_PANTALLA;
        }
    }

    @Override
    public void colision(Actor actor) {}
}
