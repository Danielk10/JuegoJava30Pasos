package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;

public class BalaEspecial extends Actor {

    public static final byte PODER_S = 1;

    public static final byte BALA_W = 2;

    public static final byte BALA_L = 3;

    public static final byte BALA_B = 4;

    public static final int DERECHO_ARRIBA = 0;

    public static final int DERECHO_ABAJO = 1;

    public static final int DERECHO = 2;

    public static final int IZQUIERDO_ARRIBA = 3;

    public static final int IZQUIERDO_ABAJO = 4;

    public static final int IZQUIERDO = 5;

    public static final int ARRIBA = 6;

    public static final int ABAJO = 7;

    public static final float VELOCIDAD_BALA = 10;

    private int lado;

    private byte bala;

    private float velocidad;

    public BalaEspecial(
            Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto) {
        super(pantalla, textura, x, y, ancho, alto);

        lado = BalaEspecial.DERECHO;

        bala = 0;

        velocidad = VELOCIDAD_BALA;
    }

    public BalaEspecial(Pantalla pantalla, Textura textura, float x, float y) {
        super(pantalla, textura, x, y);

        lado = BalaEspecial.DERECHO;

        bala = 0;

        velocidad = VELOCIDAD_BALA;
    }

    public BalaEspecial(
            Pantalla pantalla,
            Textura[] texturas,
            float x,
            float y,
            float ancho,
            float alto,
            float tiempoAnimacion) {
        super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

        lado = BalaEspecial.DERECHO;

        bala = 0;

        velocidad = VELOCIDAD_BALA;
    }

    @Override
    public void obtenerActores() {
        // TODO: Implement this method
    }

    public byte getBala() {
        return bala;
    }

    public float getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(float velocidad) {
        this.velocidad = velocidad;
    }

    public void setBala(byte bala) {
        this.bala = bala;
    }

    @Override
    public void actualizar(float delta) {

        super.actualizar(delta);

        if (lado == BalaEspecial.DERECHO) {
            x += velocidad / Juego.DELTA_A_PIXEL * delta;

            if (x >= Juego.ANCHO_PANTALLA) {

                remover = true;
            }
        }

        if (lado == BalaEspecial.IZQUIERDO) {

            x -= velocidad / Juego.DELTA_A_PIXEL * delta;

            if (x <= -ancho) {

                remover = true;
            }
        }

        if (lado == BalaEspecial.ARRIBA) {

            y -= velocidad / Juego.DELTA_A_PIXEL * delta;

            if (y <= -alto) {

                remover = true;
            }
        }

        if (lado == BalaEspecial.ABAJO) {

            y += velocidad / Juego.DELTA_A_PIXEL * delta;

            if (y >= Juego.ALTO_PANTALLA) {

                remover = true;
            }
        }

        if (lado == BalaEspecial.IZQUIERDO_ABAJO) {

            x -= velocidad / Juego.DELTA_A_PIXEL * delta;

            y += velocidad / Juego.DELTA_A_PIXEL * delta;

            if (y >= Juego.ALTO_PANTALLA && x <= -ancho) {

                remover = true;
            }
        }

        if (lado == BalaEspecial.IZQUIERDO_ARRIBA) {

            x -= velocidad / Juego.DELTA_A_PIXEL * delta;

            y -= velocidad / Juego.DELTA_A_PIXEL * delta;

            if (y <= -alto && x <= -ancho) {

                remover = true;
            }
        }

        if (lado == BalaEspecial.DERECHO_ABAJO) {

            x += velocidad / Juego.DELTA_A_PIXEL * delta;

            y += velocidad / Juego.DELTA_A_PIXEL * delta;

            if (y >= Juego.ALTO_PANTALLA && x >= Juego.ANCHO_PANTALLA) {

                remover = true;
            }
        }

        if (lado == BalaEspecial.DERECHO_ARRIBA) {

            x += velocidad / Juego.DELTA_A_PIXEL * delta;

            y -= velocidad / Juego.DELTA_A_PIXEL * delta;

            if (y <= -alto && x >= Juego.ANCHO_PANTALLA) {

                remover = true;
            }
        }
    }

    public void explosion() {

        Textura[] texturas =
                new Textura[] {
                    recurso.getTextura("explosionB1.png"),
                    recurso.getTextura("explosionB2.png"),
                    recurso.getTextura("explosionB3.png"),
                    recurso.getTextura("explosionB4.png"),
                    recurso.getTextura("explosionB5.png")
                };

        ExplosionB explosion = new ExplosionB(pantalla, texturas, x - 32, y - 32, 64, 64, 2);

        if (explosion.getX() <= Juego.ANCHO_PANTALLA) {

            actores.add(explosion);
        }
    }

    public void setLado(int lado) {
        this.lado = lado;
    }

    @Override
    public void colision(Actor actor) {

        if (actor instanceof Volador
                || actor instanceof LanzaMisil
                || actor instanceof Caja
                || actor instanceof MaquinaFinal
                || actor instanceof MaquinaPared
                || actor instanceof Robot
                || actor instanceof Saltador
                || actor instanceof Misil
                || actor instanceof AntiAreo
                || actor instanceof BalaEnemigoDestruible) {
            if (bala == BalaEspecial.BALA_B) {
                explosion();
            }
            remover = true;
        }
    }
}
