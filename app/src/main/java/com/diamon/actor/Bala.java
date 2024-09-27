package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;

public class Bala extends Actor
{

	public final static int VELOCIDAD_BALA = 8;

	public static final int DERECHO_ARRIBA = 0;

	public static final int DERECHO_ABAJO = 1;

	public static final int DERECHO = 2;

	public static final int IZQUIERDO_ARRIBA = 3;

	public static final int IZQUIERDO_ABAJO = 4;

	public static final int IZQUIERDO = 5;

	public static final int ARRIBA = 6;

	public static final int ABAJO = 7;

	private int lado;

	private float velocidad;

	public Bala(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto)
	{
		super(pantalla, textura, x, y, ancho, alto);

		lado = Bala.DERECHO;

		velocidad = VELOCIDAD_BALA;
	}

	public Bala(Pantalla pantalla, Textura textura, float x, float y)
	{
		super(pantalla, textura, x, y);

		lado = Bala.DERECHO;

		velocidad = VELOCIDAD_BALA;
	}

	public Bala(Pantalla pantalla, Textura[] texturas, float x, float y, float ancho, float alto,
				float tiempoAnimacion)
	{
		super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

	    lado = Bala.DERECHO;

		velocidad = VELOCIDAD_BALA;
	}

	@Override
	public void obtenerActores()
	{
		// TODO: Implement this method
	}


	public float getVelocidad()
	{
		return velocidad;
	}

	public void setVelocidad(float velocidad)
	{
		this.velocidad = velocidad;
	}

	@Override
	public void actualizar(float delta)
	{

		super.actualizar(delta);

		if (lado == Bala.DERECHO)
		{
			x += velocidad / Juego.DELTA_A_PIXEL * delta;

			if (x >= Juego.ANCHO_PANTALLA)
			{

				remover = true;
			}

		}

		if (lado == Bala.IZQUIERDO)
		{

			x -= velocidad / Juego.DELTA_A_PIXEL * delta;

			if (x <= -ancho)
			{

				remover = true;
			}

		}

		if (lado == Bala.ARRIBA)
		{

			y -= velocidad / Juego.DELTA_A_PIXEL * delta;

			if (y <= -alto)
			{

				remover = true;
			}

		}

		if (lado == Bala.ABAJO)
		{

			y += velocidad / Juego.DELTA_A_PIXEL * delta;

			if (y >= Juego.ALTO_PANTALLA)
			{

				remover = true;
			}

		}

		if (lado == Bala.IZQUIERDO_ABAJO)
		{

			x -= velocidad / Juego.DELTA_A_PIXEL * delta;

			y += velocidad / Juego.DELTA_A_PIXEL * delta;

			if (y >= Juego.ALTO_PANTALLA && x <= -ancho)
			{

				remover = true;
			}

		}

		if (lado == Bala.IZQUIERDO_ARRIBA)
		{

			x -= velocidad / Juego.DELTA_A_PIXEL * delta;

			y -= velocidad / Juego.DELTA_A_PIXEL * delta;

			if (y <= -alto && x <= -ancho)
			{

				remover = true;
			}



		}

		if (lado == Bala.DERECHO_ABAJO)
		{

			x += velocidad / Juego.DELTA_A_PIXEL * delta;

			y += velocidad / Juego.DELTA_A_PIXEL * delta;

			if (y >= Juego.ALTO_PANTALLA && x >= Juego.ANCHO_PANTALLA)
			{

				remover = true;
			}

		}

		if (lado == Bala.DERECHO_ARRIBA)
		{

			x += velocidad / Juego.DELTA_A_PIXEL * delta;

			y -= velocidad / Juego.DELTA_A_PIXEL * delta;


			if (y <= -alto && x >= Juego.ANCHO_PANTALLA)
			{

				remover = true;
			}



		}



	}

	public void setLado(int lado)
	{
		this.lado = lado;
	}

	@Override
	public void colision(Actor actor)
	{

		if (actor instanceof Volador || actor instanceof Caja || actor instanceof Saltador
			|| actor instanceof BalaEnemigoDestruible || actor instanceof MaquinaFinal
			|| actor instanceof MaquinaPared || actor instanceof LanzaMisil || actor instanceof Misil
			|| actor instanceof Robot || actor instanceof AntiAreo)
		{

			remover = true;

		}

	}

}
