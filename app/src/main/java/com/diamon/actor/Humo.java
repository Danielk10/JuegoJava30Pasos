package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Pantalla;

public class Humo extends Actor {

	public Humo(Pantalla pantalla) {
		super(pantalla);
	}

	@Override
	public void actualizar(float delta) {
		// TODO Auto-generated method stub
		super.actualizar(delta);

		if (x <= -ancho) {

			remover = true;
		}

	}

	@Override
	public void colision(Actor actor) {

		if (actor instanceof Jugador || actor instanceof Bala || actor instanceof BalaEspecial) {

			remover = true;

		}

	}

}
