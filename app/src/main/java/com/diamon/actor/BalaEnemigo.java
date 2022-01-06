package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;

public class BalaEnemigo extends Actor {

	public final static float VELOCIDAD_BALA = 3;

	public final static int LADO_IZQUIERDO = 1;

	public final static int LADO_DERECHO = 2;

	private int mover;

	public final static int MOVER_ARRIBA = 3;

	public final static int MOVER_ABAJO = 4;

	private float velocidad;

	private int lado;

	private float velocidadY;

	public BalaEnemigo(Pantalla pantalla) {
		super(pantalla);

		velocidad = VELOCIDAD_BALA;

		velocidadY = 0;

	}

	public int getMover() {
		return mover;
	}

	public void setMover(int mover) {
		this.mover = mover;
	}

	public int getLado() {
		return lado;
	}

	public void setLado(int lado) {
		this.lado = lado;
	}

	public float getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(float velocidad) {
		this.velocidad = velocidad;
	}

	public float getVelocidadY() {
		return velocidadY;
	}

	public void setVelocidadY(float velocidadY) {
		this.velocidadY = velocidadY;
	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		if (lado == BalaEnemigo.LADO_DERECHO) {
			x += velocidad / Juego.DELTA_A_PIXEL * delta;

			if (x >= Juego.ANCHO_PANTALLA) {

				remover = true;
			}

		}

		if (lado == BalaEnemigo.LADO_IZQUIERDO) {

			x -= velocidad / Juego.DELTA_A_PIXEL * delta;

			if (x <= -ancho) {

				remover = true;
			}

		}

		if (mover == BalaEnemigo.MOVER_ABAJO) {

			y += velocidadY / Juego.DELTA_A_PIXEL * delta;

			if (y >= Juego.ALTO_PANTALLA) {

				remover = true;
			}

		}

		if (mover == BalaEnemigo.MOVER_ARRIBA) {

			y -= velocidadY / Juego.DELTA_A_PIXEL * delta;

			if (y <= -alto) {

				remover = true;
			}

		}

	}

	@Override
	public void colision(Actor actor) {

	}

}
