package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.utilidad.Texturas;

import android.graphics.Bitmap;

public class Misil extends Actor {

	private float tiemoDisparoHumo;

	private float tiempoExplosion;

	private float tiempoHumo;

	public final static float VELOCIDAD_MAQUINA = 2;

	public Misil(Pantalla pantalla) {

		super(pantalla);

	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		tiempoExplosion += delta;

		tiemoDisparoHumo += delta;

		tiempoHumo += delta;

		if (tiempoExplosion / 0.5f >= 1) {

			for (int i = 0; i < actores.size(); i++) {

				if (actores.get(i) instanceof Explosion) {
					Explosion e = (Explosion) actores.get(i);

					e.remover();

				}
			}

			tiempoExplosion = 0;

		}

		x += Misil.VELOCIDAD_MAQUINA / Juego.DELTA_A_PIXEL * delta;

		if (x >= Juego.ANCHO_PANTALLA) {

			remover = true;

		}

		if (tiemoDisparoHumo / 0.25f >= 1) {

			humo();

			tiemoDisparoHumo = 0;

		}

		if (tiempoHumo / 0.5f >= 1) {

			for (int i = 0; i < actores.size(); i++) {

				if (actores.get(i) instanceof Humo) {
					Humo e = (Humo) actores.get(i);

					e.remover();

				}
			}

			tiempoHumo = 0;

		}

	}

	public void explosion() {

		Explosion explosion = new Explosion(pantalla);

		explosion.setTamano(32, 32);

		explosion.setPosicion(x, y);

		explosion.setImagenes(new Bitmap[] { Texturas.explosionMisil1, Texturas.explosionMisil2,
				Texturas.explosionMisil3, Texturas.explosionMisil4 });

		explosion.setCuadros(4);

		if (explosion.getX() <= 640) {

			actores.add(explosion);

		}

	}

	public void humo() {

		Humo humo = new Humo(pantalla);

		humo.setTamano(16, 16);

		humo.setPosicion(x, y - 4);

		humo.setCuadros(5);

		humo.setImagenes(new Bitmap[] { Texturas.humoMisil1, Texturas.humoMisil2, Texturas.humoMisil3 });

		if (humo.getX() <= 640) {

			actores.add(humo);
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
