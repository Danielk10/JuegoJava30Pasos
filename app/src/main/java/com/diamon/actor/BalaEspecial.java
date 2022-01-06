package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.utilidad.Texturas;

import android.graphics.Bitmap;

public class BalaEspecial extends Actor {

	public final static byte PODER_S = 1;

	public final static byte BALA_W = 2;

	public final static byte BALA_L = 3;

	public final static byte BALA_B = 4;

	public final static float VELOCIDAD_BALA = 10;

	private boolean lado;

	private byte bala;

	private float tiemoExplosion;

	private float velocidad;

	public BalaEspecial(Pantalla pantalla) {
		super(pantalla);

		lado = true;

		bala = 0;

		velocidad = VELOCIDAD_BALA;
	}

	public byte getBala() {
		return bala;
	}

	public float getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(float velocidad) {
		this.velocidad = velocidad;
	}

	public void setBala(byte bala) {
		this.bala = bala;
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

			x -= velocidad / Juego.DELTA_A_PIXEL * delta;

			if (x <= -ancho) {

				remover = true;
			}

		}

		tiemoExplosion += delta;

		if (tiemoExplosion / 0.083f >= 1) {

			for (int i = 0; i < actores.size(); i++) {

				if (actores.get(i) instanceof ExplosionB) {
					ExplosionB e = (ExplosionB) actores.get(i);

					e.remover();

				}
			}
			tiemoExplosion = 0;

		}

	}

	public void explosion() {

		ExplosionB explosion = new ExplosionB(pantalla);

		explosion.setTamano(64, 64);

		explosion.setPosicion(x - 32, y - 32);

		explosion.setImagenes(new Bitmap[] { Texturas.explosionB2, Texturas.explosionB3, Texturas.explosionB4,
				Texturas.explosionB5 });

		explosion.setCuadros(2);

		if (explosion.getX() <= 640) {

			actores.add(explosion);

		}

	}

	public void setLado(boolean lado) {
		this.lado = lado;
	}

	@Override
	public void colision(Actor actor) {

		if (actor instanceof Volador || actor instanceof LanzaMisil || actor instanceof Caja
				|| actor instanceof MaquinaFinal || actor instanceof MaquinaPared || actor instanceof Robot
				|| actor instanceof Saltador || actor instanceof Misil || actor instanceof AntiAreo
				|| actor instanceof BalaEnemigoDestruible) {
			if (bala == BalaEspecial.BALA_B) {
				explosion();
			}
			remover = true;

		}

	}

}
