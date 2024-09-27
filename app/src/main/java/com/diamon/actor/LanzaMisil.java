package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;
import com.diamon.graficos.Animacion2D;

public class LanzaMisil extends Actor {

	public final static float VELOCIDAD_MAQUINA = 2;

	private float tiemoDisparo;

	public LanzaMisil(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto) {
		super(pantalla, textura, x, y, ancho, alto);

	}

	public LanzaMisil(Pantalla pantalla, Textura textura, float x, float y) {
		super(pantalla, textura, x, y);
	}

	public LanzaMisil(Pantalla pantalla, Textura[] texturas, float x, float y, float ancho, float alto,
			float tiempoAnimacion) {
		super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

	}

@Override
	public void obtenerActores()
	{
		// TODO: Implement this method
	}
	


	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		x -= LanzaMisil.VELOCIDAD_MAQUINA / Juego.DELTA_A_PIXEL * delta;

		if (x <= -ancho) {

			remover = true;

		}

		tiemoDisparo += delta;

		if (tiemoDisparo / 2.5f >= 1) {

			disparar();

			tiemoDisparo = 0;

		}

	}

	private void disparar() {

		Misil m = new Misil(pantalla, recurso.getTextura("misilH1.png"), x + ancho, y + 12, 16, 8);

		actores.add(m);

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
