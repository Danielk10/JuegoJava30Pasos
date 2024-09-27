package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Graficos;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;
import com.diamon.graficos.Animacion2D;

public class MaquinaPared extends Actor
{

	private float tiempoDisparo;

	private float tiempoChoque;

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

	private Jugador jugador;

	public MaquinaPared(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto)
	{
		super(pantalla, textura, x, y, ancho, alto);

		mover = 0;

		velocidadY = 0;

		disparar = false;

		disparando = true;

		choque = false;

		vida = 4; 

	}

	public MaquinaPared(Pantalla pantalla, Textura textura, float x, float y)
	{
		super(pantalla, textura, x, y);

		mover = 0;

		velocidadY = 0;

		disparar = false;

		disparando = true;

		choque = false;

		vida = 4;

	}

	public MaquinaPared(Pantalla pantalla, Textura[] texturas, float x, float y, float ancho, float alto,
						float tiempoAnimacion)
	{
		super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

		mover = 0;

		velocidadY = 0;

		disparar = false;

		disparando = true;

		choque = false;

		vida = 20;

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

		for (int i = 0; i < actores.size(); i++)
		{

			if (actores.get(i) instanceof Jugador)
			{

				jugador = (Jugador) actores.get(i);



			}

		}

		if (disparando)
		{

			if (jugador.getY() <= y + alto && jugador.getY() + jugador.getAlto() >= y
				&& jugador.getX() <= x + ancho)


			{

				disparando = false;

				disparar = true;

			}

		}

		if (disparar)
		{

			tiempoDisparo += delta;

			if (tiempoDisparo / 1f >= 1)
			{

				disparar();
				disparar = false;
				disparando = true;

				tiempoDisparo = 0;
			}

		}

		if (mover == MaquinaPared.MOVER_ABAJO)
		{

			y += velocidadY / Juego.DELTA_A_PIXEL * delta;

			if (y >= Juego.ALTO_PANTALLA)
			{

				remover = true;
			}

		}

		if (mover == MaquinaPared.MOVER_ARRIBA)
		{

			y -= velocidadY / Juego.DELTA_A_PIXEL * delta;

			if (y <= -alto)
			{

				remover = true;
			}

		}

		if (choque)
		{

			if (tiempoChoque / 1f >= 1)
			{

				vida--;

				tiempoChoque = 0;

				choque = false;

			}

		}

	}

	public float getVelocidadY()
	{
		return velocidadY;
	}

	public void setVelocidadY(float velocidadY)
	{
		this.velocidadY = velocidadY;
	}

	public float getMover()
	{
		return mover;
	}

	public void setMover(float mover)
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
	
	@Override
		public void dibujar(Graficos pincel, float delta)
		{
	
			if (lado == MaquinaPared.LADO_IZQUIERDO)
			{
	
				if (disparar)
	
				{
	
					texturas = new Textura []{new Textura2D(recurso.getTextura("maquinaParedI2.png")
					.getBipmap(),32,32)};
					
					animacion.setTexturas(texturas);
	
				}
				else
				{
	
					texturas = new Textura []{new Textura2D(recurso.getTextura("maquinaParedI1.png")
															.getBipmap(),32,32)};
					
					animacion.setTexturas(texturas);
	
				}
	
			}
	
			if (lado == MaquinaPared.LADO_DERECHO)
			{
	
				if (disparar)
	
				{
	
	
					texturas = new Textura []{new Textura2D(recurso.getTextura("maquinaParedD2.png")
															.getBipmap(),32,32)};
					
	
					animacion.setTexturas(texturas);
	
	
	
				}
				else
				{
	
					texturas = new Textura []{new Textura2D(recurso.getTextura("maquinaParedD1.png")
															.getBipmap(),32,32)};
					
					animacion.setTexturas(texturas);
	
				}
	
			}
	
			super.dibujar(pincel, delta);
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

	public void disparar()
	{

		if (lado == MaquinaPared.LADO_IZQUIERDO)
		{

			BalaEnemigo bala = new BalaEnemigo(pantalla, recurso.getTextura("balaParedI.png"), x, y + 12, 32, 12);

    bala.setModoClasico(true);

			bala.setLado(BalaEnemigo.IZQUIERDO);

			bala.setMover(BalaEnemigo.MOVER_ABAJO);

			bala.setVelocidadY(1);

			if (bala.getX() <= Juego.ANCHO_PANTALLA)
			{

				actores.add(bala);
			}

		}

		if (lado == MaquinaPared.LADO_DERECHO)
		{

			BalaEnemigo bala = new BalaEnemigo(pantalla, recurso.getTextura("balaParedD.png"), x + 32, y + 12, 32, 12);

    bala.setModoClasico(true);

			bala.setLado(BalaEnemigo.DERECHO);

			bala.setMover(BalaEnemigo.MOVER_ARRIBA);

			bala.setVelocidadY(1);

			if (bala.getX() <= Juego.ANCHO_PANTALLA)
			{

				actores.add(bala);
			}

		}

	}

	@Override
	public void colision(Actor actor)
	{

		if (actor instanceof Bala || actor instanceof Jugador || actor instanceof BalaEspecial
			|| actor instanceof ExplosionB || actor instanceof BalaInteligente)
		{

			recurso.getSonido("explosion.wav").reproducir(1);

			choque = true;

			if (vida == 0)
			{

				vida = 0;

				explosion();

				remover = true;

			}

		}

	}

}
