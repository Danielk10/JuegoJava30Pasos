package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.diamon.utilidad.*;

public class Saltador extends Actor {

	private int cicloExplosion;

	private int cicloDisparo;

	private boolean preparado;

	private boolean salta;

	private boolean suelo;

	public final static int VELOCIDAD_MAQUINA = 5;

	private int velocidadY;

	private int xJugador;

	public Saltador(Pantalla pantalla) {
		super(pantalla);

		cicloExplosion = 0;

		cicloDisparo = 0;

		xJugador = 0;

		preparado = false;

		suelo = false;

		salta = false;

		velocidadY = VELOCIDAD_MAQUINA;
	}

	@Override
	public void actualizar(float delta) {
		// TODO Auto-generated method stub
		super.actualizar(delta);

		cicloExplosion++;

		cicloDisparo++;

		x--;

		for (int i = 0; i < pantalla.getActores().size(); i++) {

			if (pantalla.getActores().get(i) instanceof Jugador) {
				Jugador j = (Jugador) pantalla.getActores().get(i);

				xJugador = j.getX();

			}

		}

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

			disparar();

			cicloDisparo = 0;

		}

		if (y == 352) {

			suelo = true;

		} else {
			suelo = false;

		}

		if (suelo) {

			if (xJugador == x) {

				salta = true;

			}
			if (xJugador != x) {

				salta = false;

			}

		}

		if (salta) {

			y += velocidadY;

		}

		if (y <= 0 || y >= Juego.ALTO_PANTALLA - alto - 63) {

			velocidadY = -velocidadY;

		}

		if (x <= -ancho) {

			remover = true;
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

	public void disparar() {

		BalaEnemigoDestruible bala = new BalaEnemigoDestruible(pantalla);

		bala.setTamano(32, 32);

		bala.setPosicion(x, y + 12);

		bala.setImagenes(
				new Bitmap[] { Texturas.balaSaltador1, Texturas.balaSaltador2 });

		bala.setCuadros(5);

		if (bala.getX() <= 640) {

			pantalla.getActores().add(bala);
		}

	}

	@Override
	public void dibujar(Canvas pincel, float delta) {

		super.dibujar(pincel, delta);

		if (suelo) {

			setCuadros(20);

			setImagenes(new Bitmap[] { Texturas.saltador2 });

			if (preparado) {
				setCuadros(20);
				setImagenes(new Bitmap[] { Texturas.saltador2, Texturas.saltador1,
						Texturas.saltador3 });

			}

		}

		if (salta) {

			setCuadros(1);

			setImagenes(new Bitmap[] { Texturas.saltador4 });

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
