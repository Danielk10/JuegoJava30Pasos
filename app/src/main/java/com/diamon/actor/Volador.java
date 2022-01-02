package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;

import android.graphics.Bitmap;
import com.diamon.utilidad.*;

public class Volador extends Actor {

	public final static int VELOCIDAD_MAQUINA = 2;

	private int cicloDisparo;

	private int cicloExplosion;

	private int velocidadY;

	public Volador(Pantalla pantalla) {

		super(pantalla);

		cicloDisparo = 0;

		cicloExplosion = 0;

		velocidadY = 0;

	}

	public void setVelocidadY(int velocidadY) {
		this.velocidadY = velocidadY;
	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		x -= Volador.VELOCIDAD_MAQUINA;

		if (x <= -ancho) {

			remover = true;

		}

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
		if (cicloDisparo % 40 == 0) {

			if (Math.random() < 0.08f) {
				disparar();

			}

			cicloDisparo = 0;

		}

		y += velocidadY;

		if (y <= 0 || y >= Juego.ALTO_PANTALLA - alto) {

			velocidadY = -velocidadY;

		}

	}

	public void disparar() {

		BalaEnemigo bala = new BalaEnemigo(pantalla);

		bala.setTamano(12, 12);

		bala.setPosicion(x - 16, y);

		bala.setLado(BalaEnemigo.LADO_IZQUIERDO);

		bala.setImagenes(new Bitmap[] { Texturas.balaE1, Texturas.balaE2,
				Texturas.balaE3, Texturas.balaE4 });

		bala.setCuadros(3);

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
