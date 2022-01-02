package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;

public class Bala extends Actor {

	public final static int VELOCIDAD_BALA = 8;

	private boolean lado;

	private int velocidad;

	public Bala(Pantalla pantalla) {

		super(pantalla);

		lado = true;

		velocidad = VELOCIDAD_BALA;

	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		if (lado) {
			x += velocidad;

			if (x >= Juego.ANCHO_PANTALLA) {

				remover = true;
			}

		} else {

			x -= velocidad;

			if (x <= -ancho) {

				remover = true;
			}

		}

	}

	public void setLado(boolean lado) {
		this.lado = lado;
	}

	@Override
	public void colision(Actor actor) {

		if (actor instanceof Volador || actor instanceof Caja || actor instanceof Saltador || actor instanceof BalaEnemigoDestruible
				|| actor instanceof MaquinaFinal || actor instanceof MaquinaPared || actor instanceof LanzaMisil
				|| actor instanceof Misil || actor instanceof Robot || actor instanceof AntiAreo) {

			remover = true;

		}

	}

}
