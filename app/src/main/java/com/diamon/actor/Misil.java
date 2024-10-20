package com.diamon.actor;

import com.diamon.graficos.Animacion2D;
import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;

public class Misil extends Actor {

    private float tiempoSalidaHumo;

    public static final float VELOCIDAD_MAQUINA = 0.5f;

    private Jugador jugador;

    private float velocidad = 3;

    public Misil(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto) {
        super(pantalla, textura, x, y, ancho, alto);
    }

    public Misil(Pantalla pantalla, Textura textura, float x, float y) {
        super(pantalla, textura, x, y);
    }

    public Misil(
            Pantalla pantalla,
            Textura[] texturas,
            float x,
            float y,
            float ancho,
            float alto,
            float tiempoAnimacion) {
        super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);
    }

    public void setVelocidad(float velocidad) {
        this.velocidad = velocidad;
    }

    public float getVelocidad() {
        return velocidad;
    }

    @Override
    public void obtenerActores() {

        for (int i = 0; i < actores.size(); i++) {

            if (actores.get(i) instanceof Jugador) {

                jugador = (Jugador) actores.get(i);
            }
        }
    }

    @Override
    public void actualizar(float delta) {

        super.actualizar(delta);

        tiempoSalidaHumo += delta;

        if (jugador != null) {

            // Lógica para ajustar la dirección del misil hacia el jugador
            float direccionX = jugador.getX() - this.x;
            float direccionY = jugador.getY() - this.y;

            // Normalizamos la dirección para que siempre tenga magnitud 1
            float magnitud = (float) Math.sqrt(direccionX * direccionX + direccionY * direccionY);
            direccionX /= magnitud;
            direccionY /= magnitud;

            // Actualiza la posición del misil usando velocidad y el tiempo delta
            this.x += velocidad / Juego.DELTA_A_PIXEL * direccionX * delta;
            this.y += velocidad / Juego.DELTA_A_PIXEL * direccionY * delta;

            if (x >= Juego.ANCHO_PANTALLA) {

                remover = true;
            }

            if (tiempoSalidaHumo / 0.25f >= 1) {

                humo();

                tiempoSalidaHumo = 0;
            }
        }
    }

    public void explosion() {

        Textura[] texturas =
                new Textura[] {
                    recurso.getTextura("explosion1.png"),
                    recurso.getTextura("explosion2.png"),
                    recurso.getTextura("explosion3.png"),
                    recurso.getTextura("explosion4.png")
                };

        Explosion explosion = new Explosion(pantalla, texturas, x, y, 32, 32, 4);

        explosion.getAnimacion().setModo(Animacion2D.NORMAL);

        if (explosion.getX() <= Juego.ANCHO_PANTALLA) {

            actores.add(explosion);
        }
    }

    public void humo() {

        Textura[] texturas =
                new Textura[] {
                    recurso.getTextura("humoMisil1.png"),
                    recurso.getTextura("humoMisil2.png"),
                    recurso.getTextura("humoMisil3.png")
                };

        Humo humo = new Humo(pantalla, texturas, x, y - 4, 16, 16, 5);

        if (humo.getX() <= Juego.ANCHO_PANTALLA) {

            actores.add(humo);
        }
    }

    @Override
    public void colision(Actor actor) {

        if (actor instanceof Bala
                || actor instanceof Jugador
                || actor instanceof BalaEspecial
                || actor instanceof ExplosionB
                || actor instanceof BalaInteligente) {

            recurso.getSonido("explosion.wav").reproducir(1);

            explosion();

            remover = true;
        }
    }
}
