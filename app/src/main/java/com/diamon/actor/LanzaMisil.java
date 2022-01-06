package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.utilidad.Texturas;

import android.graphics.Bitmap;


public class LanzaMisil extends Actor {

	public final static float VELOCIDAD_MAQUINA = 2;

	private float tiemoDisparo;

	private float tiemoExplosion;

	public LanzaMisil(Pantalla pantalla) {

		super(pantalla);

	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		x -= LanzaMisil.VELOCIDAD_MAQUINA / Juego.DELTA_A_PIXEL * delta;

		if (x <= -ancho) {

			remover = true;

		}

		tiemoExplosion += delta;

		if (tiemoExplosion / 0.5f >= 1) {

			for (int i = 0; i < actores.size(); i++) {

				if (actores.get(i) instanceof Explosion) {
					Explosion e = (Explosion) actores.get(i);

					e.remover();

				}
			}

			tiemoExplosion = 0;

		}

		tiemoDisparo += delta;

		if (tiemoDisparo / 2.5f >= 1) {

			disparar();

			tiemoDisparo = 0;

		}

	}

	private void disparar() {

		Misil m = new Misil(pantalla);

		m.setTamano(16, 8);

		m.setPosicion(x + ancho, y + 12);

		m.setImagenes(new Bitmap[] { Texturas.misilH1 });

		actores.add(m);

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
