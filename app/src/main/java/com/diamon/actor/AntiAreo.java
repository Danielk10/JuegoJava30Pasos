package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Pantalla;

import android.graphics.Bitmap;
import com.diamon.utilidad.*;

public class AntiAreo extends Actor {

	private int cicloDisparo;

	private int cicloExplosion;

	public AntiAreo(Pantalla pantalla) {
		super(pantalla);

		cicloDisparo = 0;

		cicloExplosion = 0;

	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		if (x <= -ancho) {

			remover = true;

		}

		x--;

		cicloDisparo++;

		cicloExplosion++;

		if (cicloExplosion % 30 == 0) {

			for (int i = 0; i < pantalla.getActores().size(); i++) {

				if (pantalla.getActores().get(i) instanceof Explosion) {
					Explosion e = (Explosion) pantalla.getActores().get(i);

					e.remover();

				}
			}

			cicloExplosion = 0;

		}
		if (cicloDisparo % 10 == 0) {

			if (Math.random() < 0.08f) {
				disparar();

			}

			cicloDisparo = 0;

		}

		if (x <= -ancho) {

			remover = true;
		}

	}

	public void disparar() {

		BalaEnemigo bala = new BalaEnemigo(pantalla);

		bala.setTamano(12, 12);

		bala.setPosicion(x, y + 12);

		bala.setImagenes(new Bitmap[] { Texturas.balaE1, Texturas.balaE2,
							 Texturas.balaE3, Texturas.balaE4 });

		bala.setCuadros(3);

		bala.setLado(BalaEnemigo.LADO_IZQUIERDO);

		if (bala.getX() <= 640) {

			pantalla.getActores().add(bala);
		}

	}

	public void explosion() {

		Explosion explosion = new Explosion(pantalla);

		explosion.setTamano(64, 64);

		explosion.setPosicion(x - 32, y - 32);

		explosion.setImagenes(
			new Bitmap[] { Texturas.explosion1, Texturas.explosion2,
				Texturas.explosion3, Texturas.explosion4 });

		explosion.setCuadros(4);

		if (explosion.getX() <= 640) {

			pantalla.getActores().add(explosion);

		}

	}

	@Override
	public void colision(Actor actor) {
		if (actor instanceof Bala || actor instanceof Jugador || actor instanceof BalaEspecial
				|| actor instanceof ExplosionB) {
			pantalla.getJuego().getRecurso().playMusica("explosion.wav", 1);

			explosion();

			remover = true;

		}

	}

}
