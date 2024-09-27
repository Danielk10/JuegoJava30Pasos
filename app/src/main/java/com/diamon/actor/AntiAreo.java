package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;
import com.diamon.graficos.Animacion2D;

public class AntiAreo extends Actor
{

	private float tiemoDisparo;

	private Jugador jugador;

	private Animacion2D animacion1;

	private Animacion2D animacion2;

	private Animacion2D animacion3;

	private Animacion2D animacion4;

	private Animacion2D animacion5;

	public AntiAreo(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto)
	{
		super(pantalla, textura, x, y, ancho, alto);

	}

	public AntiAreo(Pantalla pantalla, Textura textura, float x, float y)
	{
		super(pantalla, textura, x, y);

	}

	public AntiAreo(Pantalla pantalla, Textura[] texturas, float x, float y, float ancho, float alto,
					float tiempoAnimacion)
	{
		super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

		animacion1 = new Animacion2D(tiempoAnimacion, new Textura []{texturas[0]});

		animacion1.setModo(Animacion2D.NORMAL);

		this.animacion = animacion1;

		animacion2 = new Animacion2D(tiempoAnimacion, new Textura []{texturas[1]});

		animacion3 = new Animacion2D(tiempoAnimacion, new Textura []{texturas[2]});

		animacion4 = new Animacion2D(tiempoAnimacion, new Textura []{texturas[3]});

		animacion5 = new Animacion2D(tiempoAnimacion, new Textura []{texturas[4]});

	}

	@Override
	public void obtenerActores()
	{
		for (int i = 0; i < actores.size(); i++)
		{

			if (actores.get(i) instanceof Jugador)
			{

				jugador = (Jugador) actores.get(i);

			}

		}
	}


	@Override
	public void actualizar(float delta)
	{

		super.actualizar(delta);


		x -= 1 / Juego.DELTA_A_PIXEL * delta;


		if (jugador.getY() <= y + alto && jugador.getY() + jugador.getAlto() >= y && jugador.getX() <= x)

		{

			if (animacion1 != null)
			{

				animacion1.setModo(Animacion2D.REPETIR);

				animacion = animacion1;
			}

			tiemoDisparo += delta; 

			if (tiemoDisparo / 0.5f >= 1)
			{

				disparar(BalaEnemigo.IZQUIERDO);

				tiemoDisparo = 0;
			}

		}
		else
		{

			if (animacion1 != null)
			{

				animacion1.setModo(Animacion2D.NORMAL);

			}

		}


		if (jugador.getY() <= y + alto && jugador.getY() + jugador.getAlto() <= y
			&& jugador.getX() <= x - jugador.getAncho())

		{

			if (animacion2 != null)
			{

				animacion2.setModo(Animacion2D.REPETIR);

				animacion = animacion2;
			}

			tiemoDisparo += delta; 

			if (tiemoDisparo / 0.5f >= 1)
			{

				disparar(BalaEnemigo.IZQUIERDO_ARRIBA);

				tiemoDisparo = 0;
			}

		}
		else
		{

			if (animacion2 != null)
			{

				animacion2.setModo(Animacion2D.NORMAL);
			}
		}

		if (jugador.getY() <= y + alto && jugador.getX() >= x - jugador.getAncho() && jugador.getX() <= x + ancho)

		{

			if (animacion3 != null)
			{

				animacion3.setModo(Animacion2D.REPETIR);

				animacion = animacion3;
			}

			tiemoDisparo += delta; 

			if (tiemoDisparo / 0.5f >= 1)
			{

				disparar(BalaEnemigo.ARRIBA);

				tiemoDisparo = 0;
			}

		}
		else
		{

			if (animacion3 != null)
			{

				animacion3.setModo(Animacion2D.NORMAL);
			}

		}

		if (jugador.getY() <= y + alto && jugador.getX() >= x + ancho)

		{

			if (animacion4 != null)
			{

				animacion4.setModo(Animacion2D.REPETIR);

				animacion = animacion4;
			}

			tiemoDisparo += delta; 

			if (tiemoDisparo / 0.5f >= 1)
			{

				disparar(BalaEnemigo.DERECHO_ARRIBA);

				tiemoDisparo = 0;
			}

		}

		else
		{

			if (animacion4 != null)
			{

				animacion4.setModo(Animacion2D.NORMAL);
			}

		}


		if (jugador.getY() + jugador.getAlto() >= y  && jugador.getX() >= x + this.ancho)

		{

			if (animacion5 != null)
			{

				animacion5.setModo(Animacion2D.REPETIR);

				animacion = animacion5;
			}

			tiemoDisparo += delta; 

			if (tiemoDisparo / 0.5f >= 1)
			{

				disparar(BalaEnemigo.DERECHO);

				tiemoDisparo = 0;
			}

		}

		else
		{

			if (animacion5 != null)
			{

				animacion5.setModo(Animacion2D.NORMAL);

			}

		}


		if (x <= -ancho)
		{

			remover = true;
		}

	}

	public void disparar(int lado)
	{

		Textura[] texturas = new Textura[] { recurso.getTextura("balaE1.png"), recurso.getTextura("balaE2.png"),
			recurso.getTextura("balaE3.png"), recurso.getTextura("balaE4.png") };

		BalaEnemigo bala = new BalaEnemigo(pantalla, texturas, x, y + 12, 12, 12, 3);

bala.setModoClasico(true);

		bala.setLado(lado);

		if (bala.getX() <= Juego.ANCHO_PANTALLA)
		{

			actores.add(bala);
		}

	}

	public void explosion()
	{

		Textura[] texturas = new Textura[] { recurso.getTextura("explosion1.png"), recurso.getTextura("explosion2.png"),
			recurso.getTextura("explosion3.png"), recurso.getTextura("explosion4.png") };

		Explosion explosion = new Explosion(pantalla, texturas, x - 32, y - 32, 64, 64, 4);

		explosion.getAnimacion().setModo(Animacion2D.NORMAL);

		if (explosion.getX() <= Juego.ANCHO_PANTALLA)
		{

			actores.add(explosion);

		}

	}

	@Override
	public void colision(Actor actor)
	{
		if (actor instanceof Bala || actor instanceof Jugador || actor instanceof BalaEspecial
			|| actor instanceof ExplosionB || actor instanceof BalaInteligente)
		{

			recurso.getSonido("explosion.wav").reproducir(1);

			explosion();

			remover = true;

		}

	}

}
