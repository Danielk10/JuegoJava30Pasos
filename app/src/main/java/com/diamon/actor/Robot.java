package com.diamon.actor;

import com.diamon.graficos.Animacion2D;
import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;

public class Robot extends Actor {

    private float tiempoDisparo;

    public static final int LADO_IZQUIERDO = 1;

    public static final int LADO_DERECHO = 2;

    private int velocidad;

    private int lado;

    private boolean disparar;

    public Robot(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto) {
        super(pantalla, textura, x, y, ancho, alto);

        velocidad = 0;

        disparar = false;
    }

    public Robot(Pantalla pantalla, Textura textura, float x, float y) {
        super(pantalla, textura, x, y);

        velocidad = 0;

        disparar = false;
    }

    public Robot(
            Pantalla pantalla,
            Textura[] texturas,
            float x,
            float y,
            float ancho,
            float alto,
            float tiempoAnimacion) {
        super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

        velocidad = 0;

        disparar = false;
    }

    @Override
    public void obtenerActores() {
        // TODO: Implement this method
    }

    @Override
    public void actualizar(float delta) {

        super.actualizar(delta);

        tiempoDisparo += delta;

        if (lado == Robot.LADO_DERECHO) {

            if (!disparar) {

                if (x <= 150) {

                    x += 2 / Juego.DELTA_A_PIXEL * delta;
                    y += 2 / Juego.DELTA_A_PIXEL * delta;
                }
            }

            if (disparar) {
                x -= 2 / Juego.DELTA_A_PIXEL * delta;
                y -= 2 / Juego.DELTA_A_PIXEL * delta;
            }

            if (x >= Juego.ANCHO_PANTALLA) {

                remover = true;
            }
        }

        if (lado == Robot.LADO_IZQUIERDO) {

            if (!disparar) {

                if (x >= 450) {

                    x -= 2 / Juego.DELTA_A_PIXEL * delta;

                    y += 2 / Juego.DELTA_A_PIXEL * delta;
                }
            }

            if (disparar) {
                x += 2 / Juego.DELTA_A_PIXEL * delta;

                y -= 2 / Juego.DELTA_A_PIXEL * delta;
            }
            if (x <= -ancho) {

                remover = true;
            }
        }

        if (tiempoDisparo / 1f >= 1) {

            if (Math.random() < 0.08f) {
                disparar();

                disparar = true;
            }

            tiempoDisparo = 0;
        }
    }

    private void disparar() {

        if (lado == Robot.LADO_DERECHO) {

            Textura[] texturas =
                    new Textura[] {
                        recurso.getTextura("balaE1.png"),
                        recurso.getTextura("balaE2.png"),
                        recurso.getTextura("balaE3.png"),
                        recurso.getTextura("balaE4.png")
                    };

            BalaEnemigo bala = new BalaEnemigo(pantalla, texturas, x + ancho, y + 16, 12, 12, 3);

            bala.setModoClasico(true);

            bala.setLado(BalaEnemigo.DERECHO);

            if (bala.getX() <= Juego.ANCHO_PANTALLA) {

                actores.add(bala);
            }
        }

        if (lado == Robot.LADO_IZQUIERDO) {

            Textura[] texturas =
                    new Textura[] {
                        recurso.getTextura("balaE1.png"),
                        recurso.getTextura("balaE2.png"),
                        recurso.getTextura("balaE3.png"),
                        recurso.getTextura("balaE4.png")
                    };

            BalaEnemigo bala = new BalaEnemigo(pantalla, texturas, x, y + 16, 12, 12, 3);

            bala.setModoClasico(true);

            bala.setLado(BalaEnemigo.IZQUIERDO);

            if (bala.getX() <= 640) {

                actores.add(bala);
            }
        }
    }

    private void explosion() {

        Textura[] texturas =
                new Textura[] {
                    recurso.getTextura("explosion1.png"),
                    recurso.getTextura("explosion2.png"),
                    recurso.getTextura("explosion3.png"),
                    recurso.getTextura("explosion4.png")
                };

        Explosion explosion = new Explosion(pantalla, texturas, x - 32, y - 32, 64, 64, 4);

        explosion.getAnimacion().setModo(Animacion2D.NORMAL);

        if (explosion.getX() <= Juego.ANCHO_PANTALLA) {

            actores.add(explosion);
        }
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public int getLado() {
        return lado;
    }

    public void setLado(int lado) {
        this.lado = lado;
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
