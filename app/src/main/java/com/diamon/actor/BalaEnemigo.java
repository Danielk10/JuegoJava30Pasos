package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;

public class BalaEnemigo extends Actor {

	public final static int VELOCIDAD_BALA = 3;

	public final static int LADO_IZQUIERDO = 1;

	public final static int LADO_DERECHO = 2;

	private int mover;

	public final static int MOVER_ARRIBA = 3;

	public final static int MOVER_ABAJO = 4;

	private int velocidad;

	private int lado;

	private int velocidadY;

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

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public int getVelocidadY() {
		return velocidadY;
	}

	public void setVelocidadY(int velocidadY) {
		this.velocidadY = velocidadY;
	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		if (lado == BalaEnemigo.LADO_DERECHO) {
			x += velocidad;

			if (x >= Juego.ANCHO_PANTALLA) {

				remover = true;
			}

		}

		if (lado == BalaEnemigo.LADO_IZQUIERDO) {

			x -= velocidad;

			if (x <= -ancho) {

				remover = true;
			}

		}

		if (mover == BalaEnemigo.MOVER_ABAJO) {

			y += velocidadY;

			if (y >= Juego.ALTO_PANTALLA) {

				remover = true;
			}

		}

		if (mover == BalaEnemigo.MOVER_ARRIBA) {

			y -= velocidadY;

			if (y <= -alto) {

				remover = true;
			}

		}

	}

	@Override
	public void colision(Actor actor) {

	}

}
