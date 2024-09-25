package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.utilidad.Texturas;

import android.graphics.Bitmap;


public class BalaEnemigoDestruible extends Actor {

	public final static float VELOCIDAD_BALA = 3;

	private float velocidad;

	private float tiemoExplosion;

	public BalaEnemigoDestruible(Pantalla pantalla) {
		super(pantalla);

		velocidad = VELOCIDAD_BALA;

	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		tiemoExplosion += delta;

		x -= velocidad / Juego.DELTA_A_PIXEL * delta;

		if (x <= -ancho) {

			remover = true;

		}

		if (y <= -alto) {

			remover = true;
		}

		if (x >= Juego.ANCHO_PANTALLA) {

			remover = true;
		}

		if (y >= Juego.ALTO_PANTALLA) {

			remover = true;
		}

		if (tiemoExplosion / 0.5f >= 1) {

			for (int i = 0; i < actores.size(); i++) {

				if (actores.get(i) instanceof Explosion) {
					Explosion e = (Explosion) actores.get(i);

					e.remover();

				}

				tiemoExplosion = 0;

			}

		}

	}

	public void explosion() {

		Explosion explosion = new Explosion(pantalla);

		explosion.setTamano(64, 64);

		explosion.setPosicion(x - 32, y - 32);

		explosion.setImagenes(
				new Bitmap[] { Texturas.explosion1, Texturas.explosion2, Texturas.explosion3, Texturas.explosion4 });

		explosion.setCuadros(4);

		if (explosion.getX() <= 640) {

			actores.add(explosion);

		}

	}

	@Override
	public void colision(Actor actor) {

		if (actor instanceof Jugador || actor instanceof Bala || actor instanceof BalaEspecial) {

			recurso.playMusica("explosion.wav", 1);

			explosion();

			remover = true;

		}

	}

}
