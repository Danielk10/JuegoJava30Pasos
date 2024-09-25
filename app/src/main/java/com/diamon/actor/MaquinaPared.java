package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.utilidad.Texturas;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class MaquinaPared extends Actor {

	private float tiempoDisparo;

	private float tiempoExplosion;

	private float tiempoChoque;

	private float yJugador;

	private boolean disparar;

	private boolean disparando;

	private float velocidadY;

	private float mover;

	public final static int MOVER_ARRIBA = 1;

	public final static int MOVER_ABAJO = 2;

	public final static int LADO_IZQUIERDO = 3;

	public final static int LADO_DERECHO = 4;

	private int vida;

	private boolean choque;

	private int lado;

	public MaquinaPared(Pantalla pantalla) {
		super(pantalla);

		mover = 0;

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

		for (int i = 0; i < actores.size(); i++) {

			if (actores.get(i) instanceof Jugador) {
				Jugador j = (Jugador) actores.get(i);

				yJugador = j.getY();

			}

		}

		tiempoExplosion += delta;

		tiempoChoque += delta;

		if (tiempoExplosion / 0.5f >= 1) {

			for (int i = 0; i < actores.size(); i++) {

				if (actores.get(i) instanceof Explosion) {
					Explosion e = (Explosion) actores.get(i);

					e.remover();

				}
			}

			tiempoExplosion = 0;
		}

		if (disparando) {
			if (yJugador + 4 == y) {

				disparando = false;

				disparar = true;

			}

		}

		if (disparar) {

			tiempoDisparo += delta;

			if (tiempoDisparo / 0.33f >= 1) {

				disparar();
				disparar = false;
				disparando = true;

				tiempoDisparo = 0;
			}

		}

		if (mover == MaquinaPared.MOVER_ABAJO) {

			y += velocidadY / Juego.DELTA_A_PIXEL * delta;

			if (y >= Juego.ALTO_PANTALLA) {

				remover = true;
			}

		}

		if (mover == MaquinaPared.MOVER_ARRIBA) {

			y -= velocidadY / Juego.DELTA_A_PIXEL * delta;

			if (y <= -alto) {

				remover = true;
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

	public float getVelocidadY() {
		return velocidadY;
	}

	public void setVelocidadY(float velocidadY) {
		this.velocidadY = velocidadY;
	}

	public float getMover() {
		return mover;
	}

	public void setMover(float mover) {
		this.mover = mover;
	}

	public int getLado() {
		return lado;
	}

	public void setLado(int lado) {
		this.lado = lado;
	}

	@Override
	public void dibujar(Canvas pincel, float delta) {

		if (lado == MaquinaPared.LADO_IZQUIERDO) {

			if (disparar)

			{

				setImagenes(new Bitmap[] { Texturas.maquinaParedI2 });

			} else {

				setImagenes(new Bitmap[] { Texturas.maquinaParedI1 });

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
				new Bitmap[] { Texturas.explosion1, Texturas.explosion2, Texturas.explosion3, Texturas.explosion4 });
		explosion.setCuadros(4);

		if (explosion.getX() <= 640) {

			actores.add(explosion);

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

			bala.setImagenes(new Bitmap[] { Texturas.balaParedI });

			if (bala.getX() <= 640) {

				actores.add(bala);
			}

		}

		if (lado == MaquinaPared.LADO_DERECHO) {

			BalaEnemigo bala = new BalaEnemigo(pantalla);

			bala.setTamano(32, 12);

			bala.setPosicion(x + 32, y + 12);

			bala.setLado(BalaEnemigo.LADO_DERECHO);

			bala.setMover(BalaEnemigo.MOVER_ARRIBA);

			bala.setVelocidadY(1);

			bala.setImagenes(new Bitmap[] { Texturas.balaParedD });

			if (bala.getX() <= 640) {

				actores.add(bala);
			}

		}

	}

	@Override
	public void colision(Actor actor) {

		if (actor instanceof Bala || actor instanceof Jugador || actor instanceof BalaEspecial
				|| actor instanceof ExplosionB) {

			recurso.playMusica("explosion.wav", 1);

			choque = true;

			if (vida == 0) {

				vida = 0;

				explosion();

				remover = true;

			}

		}

	}

}
