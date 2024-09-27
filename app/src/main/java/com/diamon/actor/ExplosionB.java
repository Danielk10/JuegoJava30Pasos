package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;

public class ExplosionB extends Actor
{

	private int vueltas; 


	public ExplosionB(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto)
	{
		super(pantalla, textura, x, y, ancho, alto);

	}

	public ExplosionB(Pantalla pantalla, Textura textura, float x, float y)
	{
		super(pantalla, textura, x, y);

	}

	public ExplosionB(Pantalla pantalla, Textura[] texturas, float x, float y, float ancho, float alto,
					  float tiempoAnimacion)
	{
		super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

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


		if (animacion.isFinAnimcion())
		{
            vueltas++;


		} 

		
		if (vueltas == 5)
		{


			remover = true;


		}


	}



	@Override
	public void colision(Actor actor)
	{

	}

}
