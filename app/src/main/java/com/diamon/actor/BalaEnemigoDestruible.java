package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;
import com.diamon.graficos.Animacion2D;

public class BalaEnemigoDestruible extends Actor
{

	public static final int DERECHO_ARRIBA = 0;

	public static final int DERECHO_ABAJO = 1;

	public static final int DERECHO = 2;

	public static final int IZQUIERDO_ARRIBA = 3;

	public static final int IZQUIERDO_ABAJO = 4;

	public static final int IZQUIERDO = 5;

	public static final int ARRIBA = 6;

	public static final int ABAJO = 7;

	private int lado;

	public final static float VELOCIDAD_BALA = 3;

	private float velocidad;

	public BalaEnemigoDestruible(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto)
	{
		super(pantalla, textura, x, y, ancho, alto);

		velocidad = VELOCIDAD_BALA;

		lado = BalaEnemigoDestruible.IZQUIERDO;
	}

	public BalaEnemigoDestruible(Pantalla pantalla, Textura textura, float x, float y)
	{
		super(pantalla, textura, x, y);

		velocidad = VELOCIDAD_BALA;

		lado = BalaEnemigoDestruible.IZQUIERDO;
	}

	public BalaEnemigoDestruible(Pantalla pantalla, Textura[] texturas, float x, float y, float ancho, float alto,
								 float tiempoAnimacion)
	{
		super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

		velocidad = VELOCIDAD_BALA;

		lado = BalaEnemigoDestruible.IZQUIERDO;
	}

	public void setLado(int lado)
	{
		this.lado = lado;
	}

	public int getLado()
	{
		return lado;
	}

	@Override
	public void obtenerActores()
	{
		// TODO: Implement this method
	}


	@Override
	public void actualizar(float delta)
	{

		super.actualizar(delta);


		if (lado == BalaEnemigoDestruible.DERECHO)
		{
			x += velocidad / Juego.DELTA_A_PIXEL * delta;

			if (x >= Juego.ANCHO_PANTALLA)
			{

				remover = true;
			}

		}

		if (lado == BalaEnemigoDestruible.IZQUIERDO)
		{

			x -= velocidad / Juego.DELTA_A_PIXEL * delta;

			if (x <= -ancho)
			{

				remover = true;
			}

		}

		if (lado == BalaEnemigoDestruible.ARRIBA)
		{

			y -= velocidad / Juego.DELTA_A_PIXEL * delta;

			if (y <= -alto)
			{

				remover = true;
			}

		}

		if (lado == BalaEnemigoDestruible.ABAJO)
		{

			y += velocidad / Juego.DELTA_A_PIXEL * delta;

			if (y >= Juego.ALTO_PANTALLA)
			{

				remover = true;
			}

		}

		if (lado == BalaEnemigoDestruible.IZQUIERDO_ABAJO)
		{

			x -= velocidad / Juego.DELTA_A_PIXEL * delta;

			y += velocidad / Juego.DELTA_A_PIXEL * delta;

			if (y >= Juego.ALTO_PANTALLA && x <= -ancho)
			{

				remover = true;
			}

		}

		if (lado == BalaEnemigoDestruible.IZQUIERDO_ARRIBA)
		{

			x -= velocidad / Juego.DELTA_A_PIXEL * delta;

			y -= velocidad / Juego.DELTA_A_PIXEL * delta;

			if (y <= -alto && x <= -ancho)
			{

				remover = true;
			}



		}

		if (lado == BalaEnemigoDestruible.DERECHO_ABAJO)
		{

			x += velocidad / Juego.DELTA_A_PIXEL * delta;

			y += velocidad / Juego.DELTA_A_PIXEL * delta;

			if (y >= Juego.ALTO_PANTALLA && x >= Juego.ANCHO_PANTALLA)
			{

				remover = true;
			}

		}

		if (lado == BalaEnemigoDestruible.DERECHO_ARRIBA)
		{

			x += velocidad / Juego.DELTA_A_PIXEL * delta;

			y -= velocidad / Juego.DELTA_A_PIXEL * delta;


			if (y <= -alto && x >= Juego.ANCHO_PANTALLA)
			{

				remover = true;
			}



		}


	}

	public void explosion()
	{

		Textura[] texturas = new Textura[] { recurso.getTextura("explosion1.png"), recurso.getTextura("explosion2.png"),
			recurso.getTextura("explosion3.png"), recurso.getTextura("explosion4.png") };

		Explosion explosion = new Explosion(pantalla, texturas, x - 32, y - 32, 64, 64, 4);

		explosion.getAnimacion().setModo(Animacion2D.NORMAL);

		if (explosion.getX() <= 640)
		{

			actores.add(explosion);

		}

	}

	@Override
	public void colision(Actor actor)
	{

		if (actor instanceof Jugador || actor instanceof Bala || actor instanceof BalaEspecial || actor instanceof BalaInteligente)
		{

			recurso.getSonido("explosion.wav").reproducir(1);

			explosion();

			remover = true;

		}

	}

}
