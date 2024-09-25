package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.utilidad.Texturas;

import android.graphics.Bitmap;


public class Robot extends Actor {

	private float tiempoDisparo;

	private float tiempoExplosion;

	public final static int LADO_IZQUIERDO = 1;

	public final static int LADO_DERECHO = 2;

	private int velocidad;

	private int lado;

	private boolean disparar;

	public Robot(Pantalla pantalla) {
		super(pantalla);

		velocidad = 0;

		disparar = false;
	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		if (lado == Robot.LADO_DERECHO) {
			x += velocidad / Juego.DELTA_A_PIXEL * delta;

			if (!disparar) {

				if (x <= 100) {

					x += 2 / Juego.DELTA_A_PIXEL * delta;
					y += 2 / Juego.DELTA_A_PIXEL * delta;
				}

			}

			if (disparar)

			{
				x -= 2 / Juego.DELTA_A_PIXEL * delta;
				y -= 2 / Juego.DELTA_A_PIXEL * delta;

			}

			if (x >= Juego.ANCHO_PANTALLA) {

				remover = true;
			}

		}

		if (lado == Robot.LADO_IZQUIERDO) {

			x -= velocidad / Juego.DELTA_A_PIXEL * delta;

			if (!disparar) {
				if (x >= 500) {

					x -= 2 / Juego.DELTA_A_PIXEL * delta;

					y += 2 / Juego.DELTA_A_PIXEL * delta;

				}
			}

			if (disparar)

			{
				x += 2 / Juego.DELTA_A_PIXEL * delta;
				y -= 2 / Juego.DELTA_A_PIXEL * delta;

			}
			if (x <= -ancho) {

				remover = true;
			}

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

				disparar = true;

			}

			tiempoDisparo = 0;

		}

	}

	private void disparar() {

		if (lado == Robot.LADO_DERECHO) {

			BalaEnemigo bala = new BalaEnemigo(pantalla);

			bala.setTamano(12, 12);

			bala.setPosicion(x + ancho, y + 16);

			bala.setImagenes(new Bitmap[] { Texturas.balaE1, Texturas.balaE2, Texturas.balaE3, Texturas.balaE4 });

			bala.setCuadros(3);

			bala.setLado(BalaEnemigo.LADO_DERECHO);

			if (bala.getX() <= 640) {

				actores.add(bala);
			}

		}

		if (lado == Robot.LADO_IZQUIERDO) {

			BalaEnemigo bala = new BalaEnemigo(pantalla);

			bala.setTamano(12, 12);

			bala.setPosicion(x, y + 16);

			bala.setImagenes(new Bitmap[] { Texturas.balaE1, Texturas.balaE2, Texturas.balaE3, Texturas.balaE4 });

			bala.setCuadros(3);

			bala.setLado(BalaEnemigo.LADO_IZQUIERDO);

			if (bala.getX() <= 640) {

				actores.add(bala);
			}

		}

	}

	private void explosion() {

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

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public int getLado() {
		return lado;
	}

	public void setLado(int lado) {
		this.lado = lado;
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
