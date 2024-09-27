package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;

public class Caja extends Actor {

	private final static int PODER_0 = 0;

	public final static byte AGILIDAD_S = 1;

	public final static byte PODER_W = 2;

	public final static byte PODER_L = 3;

	public final static byte PODER_B = 4;

	public byte poderBala;

	private byte agilidad;

	public Caja(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto) {
		super(pantalla, textura, x, y, ancho, alto);

		poderBala = Caja.PODER_0;

		agilidad = 0;

	}

	public Caja(Pantalla pantalla, Textura textura, float x, float y) {
		super(pantalla, textura, x, y);

		poderBala = Caja.PODER_0;

		agilidad = 0;
	}

	public Caja(Pantalla pantalla, Textura[] texturas, float x, float y, float ancho, float alto,
			float tiempoAnimacion) {
		super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

		poderBala = Caja.PODER_0;

		agilidad = 0;
	}


@Override
	public void obtenerActores()
	{
		// TODO: Implement this method
	}
	

	public void setAgilidad(byte agilidad) {
		this.agilidad = agilidad;
	}

	public byte getPoderBala() {
		return poderBala;
	}

	public void setPoderBala(byte poderBala) {
		this.poderBala = poderBala;
	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		x -= 1 / Juego.DELTA_A_PIXEL * delta;

		if (x <= -ancho) {

			remover = true;
		}
	}

	private void poder() {

		Poder poder = null;

		if (agilidad == Caja.AGILIDAD_S) {

			poder = new Poder(pantalla, recurso.getTextura("poderS.png"), x, y, 32, 32);

			poder.setPoder(AGILIDAD_S);

			actores.add(poder);
		}

		if (poderBala == Caja.PODER_W) {

			poder = new Poder(pantalla, recurso.getTextura("poderW.png"), x, y, 32, 32);

			poder.setPoder(PODER_W);

			actores.add(poder);

		}

		if (poderBala == Caja.PODER_L) {

			poder = new Poder(pantalla, recurso.getTextura("poderL.png"), x, y, 32, 32);

			poder.setPoder(PODER_L);

			actores.add(poder);

		}

		if (poderBala == Caja.PODER_B) {

			poder = new Poder(pantalla, recurso.getTextura("poderB.png"), x, y, 32, 32);

			poder.setPoder(PODER_B);

			actores.add(poder);

		}

	}

	@Override
	public void colision(Actor actor) {

		if (actor instanceof Bala || actor instanceof Jugador || actor instanceof BalaEspecial || actor instanceof BalaInteligente) {

			poder();

			remover = true;

		}

	}

}
