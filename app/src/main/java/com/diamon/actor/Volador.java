package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.utilidad.Texturas;

import android.graphics.Bitmap;

public class Volador extends Actor {

	public final static float VELOCIDAD_MAQUINA = 2;

	private float tiempoDisparo;

	private float tiempoExplosion;

	private float velocidadY;

	public Volador(Pantalla pantalla) {

		super(pantalla);

		velocidadY = 0;

	}

	public void setVelocidadY(int velocidadY) {
		this.velocidadY = velocidadY;
	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		x -= Volador.VELOCIDAD_MAQUINA / Juego.DELTA_A_PIXEL * delta;

		if (x <= -ancho) {

			remover = true;

		}

		tiempoExplosion += delta;

		tiempoDisparo += delta;

		if (tiempoExplosion / 0.5f >= 1) {

			for (int i = 0; i < actores.size(); i++) {

				if (actores.get(i) instanceof Explosion) {
					Explosion e = (Explosion) actores.get(i);

					e.remover();

				}
			}

			tiempoExplosion = 0;

		}
		if (tiempoDisparo / 0.66f >= 1) {

			if (Math.random() < 0.08f) {
				disparar();

			}

			tiempoDisparo = 0;

		}

		y += velocidadY;

		if (y <= 0 || y >= Juego.ALTO_PANTALLA - alto) {

			velocidadY = -velocidadY / Juego.DELTA_A_PIXEL * delta;

		}

	}

	public void disparar() {

		BalaEnemigo bala = new BalaEnemigo(pantalla);

		bala.setTamano(12, 12);

		bala.setPosicion(x - 16, y);

		bala.setLado(BalaEnemigo.LADO_IZQUIERDO);

		bala.setImagenes(new Bitmap[] { Texturas.balaE1, Texturas.balaE2, Texturas.balaE3, Texturas.balaE4 });

		bala.setCuadros(3);

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
