package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;
import com.diamon.graficos.Animacion2D;

public class Volador extends Actor
{

	public final static float VELOCIDAD_MAQUINA = 2f;

	private float tiempoDisparo;

	private float velocidad;

	private float tiempo;

	private float distanciaMovimiento;

	private float pocicionY;

	private Jugador jugador;

	public Volador(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto)
	{
		super(pantalla, textura, x, y, ancho, alto);

		velocidad = Volador.VELOCIDAD_MAQUINA;


		pocicionY = y;
	}

	public Volador(Pantalla pantalla, Textura textura, float x, float y)
	{
		super(pantalla, textura, x, y);

		velocidad = Volador.VELOCIDAD_MAQUINA;

		pocicionY = y;

	}

	public Volador(Pantalla pantalla, Textura[] texturas, float x, float y, float ancho, float alto,
				   float tiempoAnimacion)
	{
		super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

		velocidad = Volador.VELOCIDAD_MAQUINA;

		pocicionY = y;

	}

	public void setDistanciaMovimiento(float distanciaMovimiento)
	{
		this.distanciaMovimiento = distanciaMovimiento;
	}

	public float getDistanciaMovimiento()
	{
		return distanciaMovimiento;
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


	public void setVelocidad(float velocidad)
	{
		this.velocidad = velocidad;
	}

	@Override
	public void actualizar(float delta)
	{

		super.actualizar(delta);

		x -= Volador.VELOCIDAD_MAQUINA / Juego.DELTA_A_PIXEL * delta;

		if (x <= -ancho)
		{

			remover = true;

		}

		tiempo += delta;

		y = (float)(pocicionY + distanciaMovimiento + (distanciaMovimiento * Math.sin(Math.toDegrees(tiempo * velocidad))));

		tiempoDisparo += delta;


		if (tiempoDisparo / 0.3f >= 1)
		{

			if (Math.random() < 0.08f)
			{
				disparar();

			}

			tiempoDisparo = 0;

		}



	}

	public void disparar()
	{


		// Calcular la dirección del disparo hacia el jugador
		float deltaX = jugador.getX() - this.x;
		float deltaY = jugador.getY() - this.y;

		// Calcular la magnitud o distancia entre el Volador y el Jugador
		float distancia = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

		// Normalizar la dirección (deltaX y deltaY) dividiendo por la distancia
		float direccionX = deltaX / distancia;
		float direccionY = deltaY / distancia;

		// Crear la bala con la textura
		Textura[] texturas = new Textura[] { 
			recurso.getTextura("balaE1.png"), 
			recurso.getTextura("balaE2.png"),
			recurso.getTextura("balaE3.png"), 
			recurso.getTextura("balaE4.png") 
		};

		BalaEnemigo bala = new BalaEnemigo(pantalla, texturas, x - 16, y, 12, 12, 3);

		bala.setModoClasico(false);
		// Asignar la dirección de la bala (velocidadX y velocidadY)
		bala.setVelocidad(direccionX * BalaEnemigo.VELOCIDAD_BALA, direccionY * BalaEnemigo.VELOCIDAD_BALA);

		// Agregar la bala a los actores si está dentro de los límites de la pantalla
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
