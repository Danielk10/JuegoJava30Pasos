package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;

public class BalaInteligente extends Actor
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

	private float direccionX, direccionY;

	public BalaInteligente(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto)
	{
		super(pantalla, textura, x, y, ancho, alto);

		lado = Bala.DERECHO;

		velocidad = VELOCIDAD_BALA;
	}

	public BalaInteligente(Pantalla pantalla, Textura textura, float x, float y)
	{
		super(pantalla, textura, x, y);

		lado = Bala.DERECHO;

		velocidad = VELOCIDAD_BALA;
	}

	public BalaInteligente(Pantalla pantalla, Textura[] texturas, float x, float y, float ancho, float alto,
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

	public void setDireccion(float direccionX, float direccionY)
	{

		this.direccionX = direccionX;

		this.direccionY = direccionY;
	}


	@Override
	public void actualizar(float delta)
	{

		super.actualizar(delta);


		x += direccionX * velocidad / Juego.DELTA_A_PIXEL * delta;

		y += direccionY * velocidad / Juego.DELTA_A_PIXEL * delta;



		if (x >= Juego.ANCHO_PANTALLA)
		{

			remover = true;
		}


		if (x <= -ancho)
		{

			remover = true;
		}

		if (y <= -alto)
		{

			remover = true;
		}

		if (y >= Juego.ALTO_PANTALLA)
		{

			remover = true;
		}

		if (y >= Juego.ALTO_PANTALLA && x <= -ancho)
		{

			remover = true;
		}


		if (y <= -alto && x <= -ancho)
		{

			remover = true;
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