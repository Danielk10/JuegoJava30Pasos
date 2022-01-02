package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Pantalla;

import android.graphics.Bitmap;
import com.diamon.utilidad.*;

public class LanzaMisil extends Actor {

	public final static int VELOCIDAD_MAQUINA = 2;

	private int cicloExplosion;

	private int cicloDisparo;

	public LanzaMisil(Pantalla pantalla) {

		super(pantalla);

		cicloExplosion = 0;

		cicloDisparo = 0;

	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		x -= LanzaMisil.VELOCIDAD_MAQUINA;

		if (x <= -ancho) {

			remover = true;

		}

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

		cicloDisparo++;

		if (cicloDisparo % 150 == 0) {

			disparar();

			cicloDisparo = 0;

		}

	}

	private void disparar() {

		Misil m = new Misil(pantalla);

		m.setTamano(16, 8);

		m.setPosicion(x + ancho, y + 12);

		m.setImagenes(new Bitmap[] { Texturas.misilH1 });

		pantalla.getActores().add(m);

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

		pantalla.getJuego().getRecurso().playMusica("explosion.wav",1);
			explosion();

			remover = true;

		}

	}

}
