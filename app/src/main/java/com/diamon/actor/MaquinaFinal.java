package com.diamon.actor;

import com.diamon.graficos.Animacion2D;
import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;

public class MaquinaFinal extends Actor {

    private float tiempoDisparo;

    private float tiempoChoque;

    private float tiempoDisparoDestruible;

    private int vida;

    private boolean disparar;

    private boolean choque;

    public MaquinaFinal(
            Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto) {
        super(pantalla, textura, x, y, ancho, alto);

        vida = 50;

        disparar = false;

        choque = false;
    }

    public MaquinaFinal(Pantalla pantalla, Textura textura, float x, float y) {
        super(pantalla, textura, x, y);

        vida = 50;

        disparar = false;

        choque = false;
    }

    public MaquinaFinal(
            Pantalla pantalla,
            Textura[] texturas,
            float x,
            float y,
            float ancho,
            float alto,
            float tiempoAnimacion) {
        super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

        vida = 50;

        disparar = false;

        choque = false;
    }

    @Override
    public void obtenerActores() {
        // TODO: Implement this method
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public boolean isDisparar() {
        return disparar;
    }

    public void setDisparar(boolean disparar) {
        this.disparar = disparar;
    }

    @Override
    public void actualizar(float delta) {
        // TODO Auto-generated method stub
        super.actualizar(delta);

        tiempoDisparo += delta;

        tiempoChoque += delta;

        tiempoDisparoDestruible += delta;

        if (vida == 0) {

            vida = 0;
        }

        if (disparar) {

            if (tiempoDisparo / 2 >= 1) {

                disparar();

                tiempoDisparo = 0;
            }

            if (tiempoDisparoDestruible / 1 >= 1) {

                dispararBalaDestruible();

                tiempoDisparoDestruible = 0;
            }
        }

        if (choque) {

            if (tiempoChoque / 0.33f >= 1) {

                vida--;

                tiempoChoque = 0;

                choque = false;
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

        Explosion explosion = new Explosion(pantalla, texturas, x - 32, y - 32, 64, 64, 4);

        explosion.getAnimacion().setModo(Animacion2D.NORMAL);

        if (explosion.getX() <= Juego.ANCHO_PANTALLA) {

            actores.add(explosion);
        }
    }

    public void disparar() {

        BalaEnemigo bala =
                new BalaEnemigo(pantalla, recurso.getTextura("balaParedI.png"), x, y + 26, 32, 12);

        bala.setModoClasico(true);

        bala.setLado(BalaEnemigo.IZQUIERDO);

        bala.setVelocidadY(1);

        if (bala.getX() <= 640) {

            actores.add(bala);
        }
    }

    public void dispararBalaDestruible() {

        Textura[] texturas1 =
                new Textura[] {
                    recurso.getTextura("balaSaltador1.png"), recurso.getTextura("balaSaltador2.png")
                };

        BalaEnemigoDestruible bala1 =
                new BalaEnemigoDestruible(pantalla, texturas1, x, y - 128, 32, 32, 20);

        if (bala1.getX() <= Juego.ANCHO_PANTALLA) {

            actores.add(bala1);
        }

        Textura[] texturas2 =
                new Textura[] {
                    recurso.getTextura("balaSaltador1.png"), recurso.getTextura("balaSaltador2.png")
                };

        BalaEnemigoDestruible bala2 =
                new BalaEnemigoDestruible(pantalla, texturas2, x, y + 128, 32, 32, 20);

        if (bala2.getX() <= Juego.ANCHO_PANTALLA) {

            actores.add(bala2);
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

            if (vida == 0) {

                explosion();
                remover = true;
            }

            choque = true;
        }
    }
}
