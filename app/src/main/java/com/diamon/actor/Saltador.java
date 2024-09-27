package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;
import com.diamon.graficos.Animacion2D;

public class Saltador extends Actor {

	private float tiempoDisparo;

	private boolean salta;

	private boolean suelo;

	public final static float VELOCIDAD_MAQUINA = 5;

	private float velocidadY;

	private float xJugador;

	public Saltador(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto) {
		super(pantalla, textura, x, y, ancho, alto);

		xJugador = 0;

		suelo = false;

		salta = false;

		velocidadY = VELOCIDAD_MAQUINA;
	}

	public Saltador(Pantalla pantalla, Textura textura, float x, float y) {
		super(pantalla, textura, x, y);

		xJugador = 0;

		suelo = false;

		salta = false;

		velocidadY = VELOCIDAD_MAQUINA;
	}

	public Saltador(Pantalla pantalla, Textura[] texturas, float x, float y, float ancho, float alto,
			float tiempoAnimacion) {
		super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

		xJugador = 0;

		suelo = false;

		salta = false;

		velocidadY = VELOCIDAD_MAQUINA;
	}

@Override
	public void obtenerActores()
	{
		// TODO: Implement this method
	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		tiempoDisparo += delta;

		x -= 1 / Juego.DELTA_A_PIXEL * delta;

		for (int i = 0; i < actores.size(); i++) {

			if (actores.get(i) instanceof Jugador) {
				Jugador j = (Jugador) actores.get(i);

				xJugador = j.getX();

			}

		}
		

		if (tiempoDisparo / 0.66f >= 1) {

			disparar();

			tiempoDisparo = 0;

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

			y += velocidadY / Juego.DELTA_A_PIXEL * delta;

		}

		if (y <= 0 || y >= Juego.ALTO_PANTALLA - alto - 63) {

			velocidadY = -velocidadY / Juego.DELTA_A_PIXEL * delta;

		}

		if (x <= -ancho) {

			remover = true;
		} 

	}

	public void explosion() {

		Textura[] texturas = new Textura[] { recurso.getTextura("explosion1.png"), recurso.getTextura("explosion2.png"),
				recurso.getTextura("explosion3.png"), recurso.getTextura("explosion4.png") };

		Explosion explosion = new Explosion(pantalla, texturas, x - 32, y - 32, 64, 64, 4);
		
		explosion.getAnimacion().setModo(Animacion2D.NORMAL);
		

		if (explosion.getX() <= Juego.ANCHO_PANTALLA) {

			actores.add(explosion);

		}

	}

	public void disparar() {

		Textura[] texturas = new Textura[] { recurso.getTextura("balaSaltador1.png"),
				recurso.getTextura("balaSaltador2.png") };

		BalaEnemigoDestruible bala = new BalaEnemigoDestruible(pantalla, texturas, x, y + 12, 32, 32, 5);

		if (bala.getX() <= Juego.ANCHO_PANTALLA) {

			actores.add(bala);
		}

	}

	@Override
	public void colision(Actor actor) {

		if (actor instanceof Bala || actor instanceof Jugador || actor instanceof BalaEspecial
				|| actor instanceof ExplosionB|| actor instanceof BalaInteligente) {

			recurso.getSonido("explosion.wav").reproducir(1);

			explosion();

			remover = true;
		}

	}

}
