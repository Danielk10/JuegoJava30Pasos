package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Pantalla;

public class Poder extends Actor {

	public final static int VELOCIDAD_PODER = 2;

	private int cicloDesplazamiento;

	private boolean mover;

	private int poder;

	private byte agilidad;

	public Poder(Pantalla pantalla) {
		super(pantalla);

		cicloDesplazamiento = 0;

		mover = false;

		poder = 0;

		agilidad = 0;

	}

	public byte getAgilidad() {
		return agilidad;
	}

	public void setAgilidad(byte agilidad) {
		this.agilidad = agilidad;
	}

	public int getPoder() {
		return poder;
	}

	public void setPoder(int poder) {
		this.poder = poder;
	}

	@Override
	public void colision(Actor actor) {
		if (actor instanceof Jugador) {

			remover = true;

		}

	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		x--;

		cicloDesplazamiento++;

		if (cicloDesplazamiento % 150 == 0) {

			mover = true;

			cicloDesplazamiento = 0;

		}

		if (mover) {

			x -= Poder.VELOCIDAD_PODER;
		}

		if (x <= -ancho) {

			remover = true;
		}

	}

}
