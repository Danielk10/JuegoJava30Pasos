package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Pantalla;

import android.graphics.Bitmap;
import com.diamon.utilidad.*;

public class Caja extends Actor {

	private final static int PODER_0 = 0;

	public final static byte AGILIDAD_S = 1;

	public final static byte PODER_W = 2;

	public final static byte PODER_L = 3;

	public final static byte PODER_B = 4;

	public byte poderBala;

	private byte agilidad;

	public Caja(Pantalla pantalla) {
		super(pantalla);

		poderBala = Caja.PODER_0;

		agilidad = 0;

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

		x--;

		if (x <= -ancho) {

			remover = true;
		}
	}

	private void poder() {

		Poder poder = new Poder(pantalla);

		poder.setTamano(32, 32);

		poder.setPosicion(x, y);

		if (agilidad == Caja.AGILIDAD_S) {

			poder.setPoder(AGILIDAD_S);

			poder.setImagenes(new Bitmap[] { Texturas.poderS });

			pantalla.getActores().add(poder);
		}

		if (poderBala == Caja.PODER_W) {

			poder.setPoder(PODER_W);

			poder.setImagenes(new Bitmap[] { Texturas.poderW });

			pantalla.getActores().add(poder);
		}

		if (poderBala == Caja.PODER_L) {

			poder.setPoder(PODER_L);

			poder.setImagenes(new Bitmap[] { Texturas.poderL });

			pantalla.getActores().add(poder);
		}

		if (poderBala == Caja.PODER_B) {

			poder.setPoder(PODER_B);

			poder.setImagenes(new Bitmap[] { Texturas.poderB });

			pantalla.getActores().add(poder);
		}

	}

	@Override
	public void colision(Actor actor) {

		if (actor instanceof Bala || actor instanceof Jugador || actor instanceof BalaEspecial) {

			poder();

			remover = true;

		}

	}

}
