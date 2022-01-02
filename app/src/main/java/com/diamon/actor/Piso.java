package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;

public class Piso extends Actor {

	private final static int VELOCIDAD_PISO = 1;

	public Piso(Pantalla pantalla) {

		super(pantalla);
		x = 640;

		y = Juego.ALTO_PANTALLA - 64;
	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		x -= VELOCIDAD_PISO;

		if (x <= -128) {

			x = Juego.ANCHO_PANTALLA;

		}

	}

	@Override
	public void colision(Actor actor) {
		// TODO Auto-generated method stub

	}

}
