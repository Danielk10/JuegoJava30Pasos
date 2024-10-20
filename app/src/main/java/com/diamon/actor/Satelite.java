package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;

public class Satelite extends Actor {

    public static final int LADO_DERECHO = 1;

    public static final int LADO_IZQUIERDO = 2;

    public static final int DIRECCION_ADELANTE = 3;

    public static final int DIRECCION_ATRAS = 4;

    private float tiempoDisparo;

    private boolean disparar;

    private int lado;

    private Jugador jugador;

    private boolean dezlizandoDedo;

    private float distancia;

    private int direccion;

    private float x1, y1;

    private boolean detener;

    private float grados;

    private float velocidad;

    public Satelite(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto) {
        super(pantalla, textura, x, y, ancho, alto);

        disparar = false;

        detener = false;

        lado = Satelite.LADO_DERECHO;

        x1 = x;

        y1 = y;
    }

    public Satelite(Pantalla pantalla, Textura textura, float x, float y) {
        super(pantalla, textura, x, y);

        disparar = false;

        detener = false;

        lado = Satelite.LADO_DERECHO;

        x1 = x;

        y1 = y;
    }

    public Satelite(
            Pantalla pantalla,
            Textura[] texturas,
            float x,
            float y,
            float ancho,
            float alto,
            float tiempoAnimacion) {
        super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

        disparar = false;

        detener = false;

        lado = Satelite.LADO_DERECHO;

        x1 = x;

        y1 = y;
    }

    public void setVelocidad(float velocidad) {
        this.velocidad = velocidad;
    }

    public float getVelocidad() {
        return velocidad;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setGradosIniciales(float grados) {
        this.grados = grados;
    }

    public void setDetener(boolean detener) {
        this.detener = detener;
    }

    public boolean isDetener() {
        return detener;
    }

    public void setDireccion(int direccion) {
        this.direccion = direccion;
    }

    public int getDireccion() {
        return direccion;
    }

    public void setDezlizandoDedo(boolean dezlizandoDedo) {
        this.dezlizandoDedo = dezlizandoDedo;
    }

    public boolean isDezlizandoDedo() {
        return dezlizandoDedo;
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

        if (!detener) {

            if (direccion == Satelite.DIRECCION_ATRAS) {

                if (jugador != null) {

                    if (dezlizandoDedo) {

                        // Aumentamos o disminuimos el ángulo según el lado
                        if (lado == Satelite.LADO_DERECHO) {

                            grados += velocidad / Juego.DELTA_A_PIXEL * delta; // Velocidad angular

                            if (grados >= 135) {
                                grados = 135;
                            }

                        } else if (lado == Satelite.LADO_IZQUIERDO) {

                            grados -= velocidad / Juego.DELTA_A_PIXEL * delta;

                            if (grados <= 45) {
                                grados = 45;
                            }
                        }

                        // Convertimos los grados a radianes para las funciones trigonométricas
                        float radianes = (float) Math.toRadians(grados);

                        // Actualizamos la posición del satélite en base al ángulo
                        x1 = (float) (distancia * Math.cos(radianes));
                        y1 = (float) (distancia * Math.sin(radianes));
                    }
                }
            }

            if (direccion == Satelite.DIRECCION_ADELANTE) {

                if (jugador != null) {

                    if (dezlizandoDedo) {

                        // Aumentamos o disminuimos el ángulo según el lado
                        if (lado == Satelite.LADO_DERECHO) {

                            grados -= velocidad / Juego.DELTA_A_PIXEL * delta; // Velocidad angular

                            if (grados <= 225) {
                                grados = 225;
                            }

                        } else if (lado == Satelite.LADO_IZQUIERDO) {

                            grados += velocidad / Juego.DELTA_A_PIXEL * delta;

                            if (grados >= 315) {
                                grados = 315;
                            }
                        }

                        // Convertimos los grados a radianes para las funciones trigonométricas
                        float radianes = (float) Math.toRadians(grados);

                        // Actualizamos la posición del satélite en base al ángulo
                        x1 = (float) (distancia * Math.cos(radianes));
                        y1 = (float) (distancia * Math.sin(radianes));
                    }
                }
            }
        }

        setPosicion(jugador.getX() + x1, jugador.getY() + y1);

        tiempoDisparo += delta;

        if (disparar) {

            if (tiempoDisparo / 0.33f >= 1) {

                disparar();

                tiempoDisparo = 0;
            }
        }
    }

    public int getLado() {
        return lado;
    }

    public void setLado(int lado) {
        this.lado = lado;
    }

    public boolean isDisparar() {
        return disparar;
    }

    public void setDisparar(boolean disparar) {
        this.disparar = disparar;
    }

    /*public void disparar()
    {

    Bala bala = new Bala(pantalla, recurso.getTextura("balaSatelite.png"), x, y + 4, 8, 8);


    if (lado == Satelite.LADO_DERECHO)
    {
    bala.setLado(Bala.DERECHO);

    }

    if (lado == Satelite.LADO_IZQUIERDO)
    {
    bala.setLado(Bala.IZQUIERDO);

    }

    actores.add(bala);

    }*/

    private void disparar() {
        // Ángulo actual del satélite en radianes
        float radianes = (float) Math.toRadians(grados);

        // Calcular la dirección del disparo usando el ángulo del satélite
        float direccionX = (float) Math.cos(radianes);
        float direccionY = (float) Math.sin(radianes);

        // Crear una nueva instancia de proyectil
        BalaInteligente bala =
                new BalaInteligente(
                        pantalla, recurso.getTextura("balaSatelite.png"), x, y + 4, 8, 8);

        // Establecer la posición inicial del proyectil (desde el satélite)
        bala.setPosicion(jugador.getX() + x1, jugador.getY() + y1);

        // Establecer la dirección del proyectil basada en el ángulo del satélite
        bala.setDireccion(direccionX, direccionY);

        // Agregar el proyectil a la lista de proyectiles en el juego (si tienes una)
        actores.add(bala);
    }

    @Override
    public void colision(Actor actor) {}
}
