package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.utilidad.Texturas; 

import android.graphics.Bitmap;

public class AntiAreo extends Actor {

	private float tiemoDisparo;

	private float tiemoExplosion;

	public AntiAreo(Pantalla pantalla) {
		super(pantalla);

	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		if (x <= -ancho) {

			remover = true;

		}

		x -= 1 / Juego.DELTA_A_PIXEL * delta;

		tiemoDisparo += delta;

		if (tiemoDisparo / 0.5f >= 1) {

			for (int i = 0; i < actores.size(); i++) {

				if (actores.get(i) instanceof Explosion) {
					Explosion e = (Explosion) actores.get(i);

					e.remover();

				}
			}

			tiemoDisparo = 0;
		}

		tiemoExplosion += delta;

		if (tiemoExplosion / 0.5f >= 1) {

			if (Math.random() < 0.08f) {
				disparar();

			}

			tiemoExplosion = 0;
		}

		if (x <= -ancho) {

			remover = true;
		}

	}

	public void disparar() {

		BalaEnemigo bala = new BalaEnemigo(pantalla);

		bala.setTamano(12, 12);

		bala.setPosicion(x, y + 12);

		bala.setImagenes(new Bitmap[] { Texturas.balaE1, Texturas.balaE2, Texturas.balaE3, Texturas.balaE4 });

		bala.setCuadros(3);

		bala.setLado(BalaEnemigo.LADO_IZQUIERDO);

		if (bala.getX() <= 640) {

			actores.add(bala);
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
		if (actor instanceof Bala || actor instanceof Jugador || actor instanceof BalaEspecial
				|| actor instanceof ExplosionB) {
			recurso.playMusica("explosion.wav", 1);

			explosion();

			remover = true;

		}

	}

}
