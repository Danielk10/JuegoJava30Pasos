package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Pantalla;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.diamon.utilidad.*;

public class Satelite extends Actor {

	private int cicloDisparo;

	private boolean disparar;

	private int lado;

	public final static int LADO_DERECHO = 1;

	public final static int LADO_IZQUIERDO = 2;

	private boolean activar;

	public Satelite(Pantalla pantalla) {
		super(pantalla);

		cicloDisparo = 0;

		disparar = false;

		activar = false;

		lado = 0;
	}

	public boolean isActivar() {
		return activar;
	}

	public void setActivar(boolean activar) {
		this.activar = activar;
	}

	@Override
	public void actualizar(float delta) {
		// TODO Auto-generated method stub
		super.actualizar(delta);

		cicloDisparo++;

		if (disparar) {

			if (cicloDisparo % 20 == 0) {

				disparar();

				cicloDisparo = 0;

			}

		}

		if (lado == Satelite.LADO_DERECHO) {

		}

		if (lado == Satelite.LADO_IZQUIERDO) {

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

	@Override
	public void dibujar(Canvas pincel, float delta) {

		if (!activar) {

			this.setImagenes(new Bitmap[] { Texturas.sateliteHD1 });

		}

		if (activar) {

			this.setImagenes(new Bitmap[] { Texturas.sateliteHD2 });

		}
		super.dibujar(pincel, delta);
	}

	public void disparar() {

		Bala bala = new Bala(pantalla);

		bala.setTamano(8, 8);

		bala.setPosicion(x, y + 4);

		bala.setLado(true);

		bala.setImagenes(new Bitmap[] { Texturas.balaSatelite });

		pantalla.getActores().add(bala);

	}

	@Override
	public void colision(Actor actor) {
		// TODO Auto-generated method stub

	}

}
