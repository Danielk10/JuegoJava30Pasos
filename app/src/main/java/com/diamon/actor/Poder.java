package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;

public class Poder extends Actor {

	public final static float VELOCIDAD_PODER = 2;

	private float tiempoDesplazamiento;

	private boolean mover;

	private int poder;

	private byte agilidad;

	public Poder(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto) {
		super(pantalla, textura, x, y, ancho, alto);

		mover = false;

		poder = 0;

		agilidad = 0;
	}

	public Poder(Pantalla pantalla, Textura textura, float x, float y) {
		super(pantalla, textura, x, y);

		mover = false;

		poder = 0;

		agilidad = 0;
	}

	public Poder(Pantalla pantalla, Textura[] texturas, float x, float y, float ancho, float alto,
			float tiempoAnimacion) {
		super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

		mover = false;

		poder = 0;

		agilidad = 0;
	}
@Override
	public void obtenerActores()
	{
		// TODO: Implement this method
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

		x -= 1 / Juego.DELTA_A_PIXEL * delta;
		;

		tiempoDesplazamiento += delta;

		if (tiempoDesplazamiento / 2.5f >= 1) {

			mover = true;

			tiempoDesplazamiento = 0;

		}

		if (mover) {

			x -= Poder.VELOCIDAD_PODER / Juego.DELTA_A_PIXEL * delta;
		}

		if (x <= -ancho) {

			remover = true;
		}

	}

}
