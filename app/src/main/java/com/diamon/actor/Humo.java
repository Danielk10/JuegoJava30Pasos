package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;

public class Humo extends Actor {

    private int vueltas;

    public Humo(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto) {
        super(pantalla, textura, x, y, ancho, alto);
    }

    public Humo(Pantalla pantalla, Textura textura, float x, float y) {
        super(pantalla, textura, x, y);
    }

    public Humo(
            Pantalla pantalla,
            Textura[] texturas,
            float x,
            float y,
            float ancho,
            float alto,
            float tiempoAnimacion) {
        super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);
    }

    @Override
    public void obtenerActores() {
        // TODO: Implement this method
    }

    @Override
    public void actualizar(float delta) {

        super.actualizar(delta);

        if (animacion.isFinAnimcion()) {
            vueltas++;
        }

        if (vueltas == 5) {

            remover = true;
        }

        if (x <= -ancho) {

            remover = true;
        }
    }

    @Override
    public void colision(Actor actor) {

        if (actor instanceof Jugador
                || actor instanceof Bala
                || actor instanceof BalaEspecial
                || actor instanceof BalaInteligente) {

            remover = true;
        }
    }
}
