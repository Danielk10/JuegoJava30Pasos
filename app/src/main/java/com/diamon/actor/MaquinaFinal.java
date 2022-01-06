package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Pantalla;
import com.diamon.utilidad.Texturas;

import android.graphics.Bitmap;


public class MaquinaFinal extends Actor {

	private float tiempoDisparo;

	private float tiempoExplosion;

	private float tiempoChoque;

	private float tiempoDisparoDestruible;

	private int vida;

	private boolean disparar;

	private boolean choque;

	public MaquinaFinal(Pantalla pantalla) {
		super(pantalla);

		vida = 50;

		disparar = false;

		choque = false;
	}

	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public boolean isDisparar() {
		return disparar;
	}

	public void setDisparar(boolean disparar) {
		this.disparar = disparar;
	}

	@Override
	public void actualizar(float delta) {
		// TODO Auto-generated method stub
		super.actualizar(delta);

		tiempoDisparo += delta;

		tiempoChoque += delta;

		tiempoDisparoDestruible += delta;

		if (vida == 0) {

			vida = 0;

			tiempoExplosion += delta;

			if (tiempoExplosion / 0.5f >= 1) {

				for (int i = 0; i < actores.size(); i++) {

					if (actores.get(i) instanceof Explosion) {
						Explosion e = (Explosion) actores.get(i);

						e.remover();

					}
				}

				tiempoExplosion = 0;

			}

		}

		if (disparar) {

			if (tiempoDisparo / 2 >= 1) {

				disparar();

				tiempoDisparo = 0;

			}

			if (tiempoDisparoDestruible / 1 >= 1) {

				dispararBalaDestruible();

				tiempoDisparoDestruible = 0;

			}

		}

		if (choque) {

			if (tiempoChoque / 0.33f >= 1) {

				vida--;

				tiempoChoque = 0;

				choque = false;

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

	public void disparar() {

		BalaEnemigo bala = new BalaEnemigo(pantalla);

		bala.setTamano(32, 12);

		bala.setPosicion(x, y + 26);

		bala.setLado(BalaEnemigo.LADO_IZQUIERDO);

		bala.setImagenes(new Bitmap[] { Texturas.balaParedI });

		if (bala.getX() <= 640) {

			actores.add(bala);
		}

	}

	public void dispararBalaDestruible() {

		BalaEnemigoDestruible bala1 = new BalaEnemigoDestruible(pantalla);

		bala1.setTamano(32, 32);

		bala1.setPosicion(x, y - 128);

		bala1.setCuadros(20);

		bala1.setImagenes(new Bitmap[] { Texturas.balaSaltador1, Texturas.balaSaltador2 });

		if (bala1.getX() <= 640) {

			actores.add(bala1);
		}

		BalaEnemigoDestruible bala2 = new BalaEnemigoDestruible(pantalla);

		bala2.setTamano(32, 32);

		bala2.setPosicion(x, y + 128);

		bala2.setCuadros(20);

		bala2.setImagenes(new Bitmap[] { Texturas.balaSaltador1, Texturas.balaSaltador2 });

		if (bala2.getX() <= 640) {

			actores.add(bala2);
		}

	}

	@Override
	public void colision(Actor actor) {

		if (actor instanceof Bala || actor instanceof Jugador || actor instanceof BalaEspecial
				|| actor instanceof ExplosionB) {

			recurso.playMusica("explosion.wav", 1);
			if (vida == 0) {

				explosion();
				remover = true;

			}

			choque = true;

		}

	}
}
