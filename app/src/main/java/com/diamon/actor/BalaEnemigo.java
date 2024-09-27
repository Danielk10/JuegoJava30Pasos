package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;

public class BalaEnemigo extends Actor
{

	public final static float VELOCIDAD_BALA = 3;

	public static final int DERECHO_ARRIBA = 0;

	public static final int DERECHO_ABAJO = 1;

	public static final int DERECHO = 2;

	public static final int IZQUIERDO_ARRIBA = 3;

	public static final int IZQUIERDO_ABAJO = 4;

	public static final int IZQUIERDO = 5;

	public static final int ARRIBA = 6;

	public static final int ABAJO = 7;

	private int mover;

	public final static int MOVER_ARRIBA = 3;

	public final static int MOVER_ABAJO = 4;

	private float velocidad;

	private int lado;

	private float velocidadX, velocidadY;

	private boolean modoClasico;

	public BalaEnemigo(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto)
	{
		super(pantalla, textura, x, y, ancho, alto);

		velocidad = VELOCIDAD_BALA;

		velocidadY = 0;

		lado = BalaEnemigo.IZQUIERDO;
	}

	public BalaEnemigo(Pantalla pantalla, Textura textura, float x, float y)
	{
		super(pantalla, textura, x, y);

		velocidad = VELOCIDAD_BALA;

		velocidadY = 0;

		lado = BalaEnemigo.IZQUIERDO;
	}

	public BalaEnemigo(Pantalla pantalla, Textura[] texturas, float x, float y, float ancho, float alto,
					   float tiempoAnimacion)
	{
		super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

		velocidad = VELOCIDAD_BALA;

		velocidadY = 0;

		lado = BalaEnemigo.IZQUIERDO;
	}

	public void setModoClasico(boolean modoClasico)
	{
		this.modoClasico = modoClasico;
	}

	public boolean isModoClasico()
	{
		return modoClasico;
	}

	@Override
	public void obtenerActores()
	{
		// TODO: Implement this method
	}



	public int getMover()
	{
		return mover;
	}

	public void setMover(int mover)
	{
		this.mover = mover;
	}

	public int getLado()
	{
		return lado;
	}

	public void setLado(int lado)
	{
		this.lado = lado;
	}

	public float getVelocidad()
	{
		return velocidad;
	}

	public void setVelocidad(float velocidad)
	{
		this.velocidad = velocidad;
	}

	public void setVelocidad(float velocidadX, float velocidadY)
	{
		this.velocidadX = velocidadX;

		this.velocidadY = velocidadY;
	}


	public float getVelocidadY()
	{
		return velocidadY;
	}

	public void setVelocidadY(float velocidadY)
	{
		this.velocidadY = velocidadY;
	}

	@Override
	public void actualizar(float delta)
	{

		super.actualizar(delta);


		if (modoClasico)
		{

			if (lado == BalaEnemigo.DERECHO)
			{
				x += velocidad / Juego.DELTA_A_PIXEL * delta;

				if (x >= Juego.ANCHO_PANTALLA)
				{

					remover = true;
				}

			}

			if (lado == BalaEnemigo.IZQUIERDO)
			{

				x -= velocidad / Juego.DELTA_A_PIXEL * delta;

				if (x <= -ancho)
				{

					remover = true;
				}

			}

			if (lado == BalaEnemigo.ARRIBA)
			{

				y -= velocidad / Juego.DELTA_A_PIXEL * delta;

				if (y <= -alto)
				{

					remover = true;
				}

			}

			if (lado == BalaEnemigo.ABAJO)
			{

				y += velocidad / Juego.DELTA_A_PIXEL * delta;

				if (y >= Juego.ALTO_PANTALLA)
				{

					remover = true;
				}

			}

			if (lado == BalaEnemigo.IZQUIERDO_ABAJO)
			{

				x -= velocidad / Juego.DELTA_A_PIXEL * delta;

				y += velocidad / Juego.DELTA_A_PIXEL * delta;

				if (y >= Juego.ALTO_PANTALLA && x <= -ancho)
				{

					remover = true;
				}

			}

			if (lado == BalaEnemigo.IZQUIERDO_ARRIBA)
			{

				x -= velocidad / Juego.DELTA_A_PIXEL * delta;

				y -= velocidad / Juego.DELTA_A_PIXEL * delta;

				if (y <= -alto && x <= -ancho)
				{

					remover = true;
				}



			}

			if (lado == BalaEnemigo.DERECHO_ABAJO)
			{

				x += velocidad / Juego.DELTA_A_PIXEL * delta;

				y += velocidad / Juego.DELTA_A_PIXEL * delta;

				if (y >= Juego.ALTO_PANTALLA && x >= Juego.ANCHO_PANTALLA)
				{

					remover = true;
				}

			}

			if (lado == BalaEnemigo.DERECHO_ARRIBA)
			{

				x += velocidad / Juego.DELTA_A_PIXEL * delta;

				y -= velocidad / Juego.DELTA_A_PIXEL * delta;


				if (y <= -alto && x >= Juego.ANCHO_PANTALLA)
				{

					remover = true;
				}



			}


		}
		else
		{


			x +=  velocidadX / Juego.DELTA_A_PIXEL * delta;

			y +=  velocidadY / Juego.DELTA_A_PIXEL * delta;



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



	}

	@Override
	public void colision(Actor actor)
	{

	}

}
