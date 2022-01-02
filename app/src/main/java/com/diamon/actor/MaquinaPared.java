package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.diamon.utilidad.*;

public class MaquinaPared extends Actor {

	private int cicloExplosion;

	private int cicloDisparo;

	private int yJugador;

	private boolean disparar;

	private boolean disparando;

	private int velocidadY;

	private int mover;

	public final static int MOVER_ARRIBA = 1;

	public final static int MOVER_ABAJO = 2;

	public final static int LADO_IZQUIERDO = 3;

	public final static int LADO_DERECHO = 4;

	private int vida;

	private boolean choque;

	private int cicloChoque;

	private int lado;

	public MaquinaPared(Pantalla pantalla) {
		super(pantalla);

		cicloExplosion = 0;

		cicloDisparo = 0;

		mover = 0;

		cicloChoque = 0;

		yJugador = 0;

		velocidadY = 0;

		disparar = false;

		disparando = true;

		choque = false;

		vida = 4;

	}

	@Override
	public void actualizar(float delta) {
		// TODO Auto-generated method stub
		super.actualizar(delta);

		for (int i = 0; i < pantalla.getActores().size(); i++) {

			if (pantalla.getActores().get(i) instanceof Jugador) {
				Jugador j = (Jugador) pantalla.getActores().get(i);

				yJugador = j.getY();

			}

		}

		cicloExplosion++;

		cicloDisparo++;

		cicloChoque++;

		if (cicloExplosion % 30 == 0) {

			for (int i = 0; i < pantalla.getActores().size(); i++) {

				if (pantalla.getActores().get(i) instanceof Explosion) {
					Explosion e = (Explosion) pantalla.getActores().get(i);

					e.remover();

				}
			}

			cicloExplosion = 0;

		}

		if (disparando) {
			if (yJugador + 4 == y) {

				disparando = false;

				disparar = true;

			}

		}

		if (disparar) {
			cicloDisparo++;
			if (cicloDisparo % 20 == 0) {

				disparar();
				disparar = false;
				disparando = true;

				cicloDisparo = 0;
			}
		}

		if (mover == MaquinaPared.MOVER_ABAJO) {

			y += velocidadY;

			if (y >= Juego.ALTO_PANTALLA) {

				remover = true;
			}

		}

		if (mover == MaquinaPared.MOVER_ARRIBA) {

			y -= velocidadY;

			if (y <= -alto) {

				remover = true;
			}

		}

		if (choque) {

			if (cicloChoque % 20 == 0) {

				vida--;

				cicloChoque = 0;

				choque = false;

			}

		}

	}

	public int getMover() {
		return mover;
	}

	public void setMover(int mover) {
		this.mover = mover;
	}

	public int getLado() {
		return lado;
	}

	public void setLado(int lado) {
		this.lado = lado;
	}

	public int getVelocidadY() {
		return velocidadY;
	}

	public void setVelocidadY(int velocidadY) {
		this.velocidadY = velocidadY;
	}

	@Override
	public void dibujar(Canvas pincel, float delta) {

		if (lado == MaquinaPared.LADO_IZQUIERDO) {

			if (disparar)

			{

				setImagenes(new Bitmap[] { Texturas.maquinaParedI2 });

			} else {

				setImagenes(new Bitmap[] { Texturas.maquinaParedI1});

			}

		}

		if (lado == MaquinaPared.LADO_DERECHO) {

			if (disparar)

			{

				setImagenes(new Bitmap[] { Texturas.maquinaParedD2 });

			} else {

				setImagenes(new Bitmap[] { Texturas.maquinaParedD1 });

			}

		}

		super.dibujar(pincel, delta);
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

		if (lado == MaquinaPared.LADO_IZQUIERDO) {

			BalaEnemigo bala = new BalaEnemigo(pantalla);

			bala.setTamano(32, 12);

			bala.setPosicion(x, y + 12);

			bala.setLado(BalaEnemigo.LADO_IZQUIERDO);

			bala.setMover(BalaEnemigo.MOVER_ABAJO);

			bala.setVelocidadY(1);

		

			bala.setImagenes(new Bitmap[] {Texturas.balaParedI});

			if (bala.getX() <= 640) {

				pantalla.getActores().add(bala);
			}

		}

		if (lado == MaquinaPared.LADO_DERECHO) {

			BalaEnemigo bala = new BalaEnemigo(pantalla);

			bala.setTamano(32, 12);

			bala.setPosicion(x + 32, y + 12);

			bala.setLado(BalaEnemigo.LADO_DERECHO);

			bala.setMover(BalaEnemigo.MOVER_ARRIBA);

			bala.setVelocidadY(1);

			bala.setImagenes(new Bitmap[] {Texturas.balaParedD});

			if (bala.getX() <= 640) {

				pantalla.getActores().add(bala);
			}

		}

	}

	@Override
	public void colision(Actor actor) {

		if (actor instanceof Bala || actor instanceof Jugador || actor instanceof BalaEspecial
				|| actor instanceof ExplosionB) {

			pantalla.getJuego().getRecurso().playMusica("explosion.wav",1);

			choque = true;

			if (vida == 0) {

				vida = 0;

				explosion();

				remover = true;

			}

		}

	}

}
