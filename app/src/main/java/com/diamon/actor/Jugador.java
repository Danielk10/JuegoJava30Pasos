package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Graficos;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;
import com.diamon.graficos.Textura2D;
import com.diamon.graficos.Animacion2D;
import android.view.KeyEvent;

public class Jugador extends Actor
{

	private float deltaXTactil, deltaYTactil;

	private float xDelta = 0;

	private float xDeltaReferencia = 0;

	private float coordenadaPantallaX, coordenadaPantallaY;

	private float direccionDezplazamientoX1, direccionDezplazamientoY1;

	private float direccionDezplazamientoX2, direccionDezplazamientoY2;

	private float x1 = 0;

	private float y1 = 0;

	private float velocidadX;

	private float velocidadY;

	private final static float VELOCIDAD_JUGADOR = 5;

	private boolean arriba, abajo, izquierda, derecha, disparar;

	private float tiemoDisparo;

	private float tiemoDisparoB;

	private float tiempoParpadeo;

	protected int frames = 0;

	private boolean dezplazamientoInicial;

	private boolean parpadeo;

	private int ladoJugador;

	private int vida;

	private boolean inmune;

	private boolean poder;

	private int tipoPoder;

	private float velocidad;

	private Satelite satelite1;

	private Satelite satelite2;

	private Textura[]texturasJ;

	private final static int LADO_DERECHO = 1;

	private final static int LADO_IZQUIERDO = 2;

	private float igual1;

	private float igual2;

	private int ciclo;

	private boolean dezplazamiento;


	public Jugador(Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto)
	{
		super(pantalla, textura, x, y, ancho, alto);

		velocidadX = 0;

		velocidadY = 0;

		deltaXTactil = deltaYTactil = 0;

		dezplazamientoInicial = true;

		arriba = abajo = izquierda = derecha = disparar = false;

		parpadeo = false;

		velocidad = VELOCIDAD_JUGADOR;

		ladoJugador = Jugador.LADO_DERECHO;

		poder = false;

		vida = 3;

		inmune = false;

		tipoPoder = 0;

		texturasJ = new Textura[4];

		texturasJ[0] = new Textura2D(recurso.getTextura("jugador1D1.png").getBipmap(), ancho, alto);

		texturasJ[1] = new Textura2D(recurso.getTextura("jugador1D2.png").getBipmap() , ancho, alto);

		texturasJ[2] = new Textura2D(recurso.getTextura("jugador1I1.png").getBipmap(), ancho, alto);

		texturasJ[3] = new Textura2D(recurso.getTextura("jugador1I2.png").getBipmap(), ancho, alto);


	}

	public Jugador(Pantalla pantalla, Textura textura, float x, float y)
	{
		super(pantalla, textura, x, y);

		velocidadX = 0;

		velocidadY = 0;

		deltaXTactil = deltaYTactil = 0;

		dezplazamientoInicial = true;

		arriba = abajo = izquierda = derecha = disparar = false;

		parpadeo = false;

		velocidad = VELOCIDAD_JUGADOR;

		ladoJugador = Jugador.LADO_DERECHO;

		poder = false;

		vida = 3;

		inmune = false;

		tipoPoder = 0;

		texturasJ = new Textura[4];

		texturasJ[0] = new Textura2D(recurso.getTextura("jugador1D1.png").getBipmap(), ancho, alto);

		texturasJ[1] = new Textura2D(recurso.getTextura("jugador1D2.png").getBipmap() , ancho, alto);

		texturasJ[2] = new Textura2D(recurso.getTextura("jugador1I1.png").getBipmap(), ancho, alto);

		texturasJ[3] = new Textura2D(recurso.getTextura("jugador1I2.png").getBipmap(), ancho, alto);

	}

	public Jugador(Pantalla pantalla, Textura[] texturas, float x, float y, float ancho, float alto,
				   float tiempoAnimacion)
	{
		super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);

		velocidadX = 0;

		velocidadY = 0;

		deltaXTactil = deltaYTactil = 0;

		dezplazamientoInicial = true;

		arriba = abajo = izquierda = derecha = disparar = false;

		parpadeo = false;

		velocidad = VELOCIDAD_JUGADOR;

		ladoJugador = Jugador.LADO_DERECHO;

		poder = false;

		vida = 3;

		inmune = false;

		tipoPoder = 0;

		texturasJ = new Textura[4];

		texturasJ[0] = new Textura2D(recurso.getTextura("jugador1D1.png").getBipmap(), ancho, alto);

		texturasJ[1] = new Textura2D(recurso.getTextura("jugador1D2.png").getBipmap() , ancho, alto);

		texturasJ[2] = new Textura2D(recurso.getTextura("jugador1I1.png").getBipmap(), ancho, alto);

		texturasJ[3] = new Textura2D(recurso.getTextura("jugador1I2.png").getBipmap(), ancho, alto);


	}



	public void agregarSatelites()
	{

		satelite1 = new Satelite(pantalla, recurso.getTextura("sateliteHD1.png"), 70, 50, 16, 16);

		satelite2 = new Satelite(pantalla, recurso.getTextura("sateliteHD1.png"), 70, -50, 16, 16);

		satelite1.setDisparar(false);

		satelite2.setDisparar(false);

		satelite1.setGradosIniciales(45);

		satelite2.setGradosIniciales(315);

		satelite1.setDistancia(70);

		satelite2.setDistancia(70);

		satelite1.setVelocidad(4);

		satelite2.setVelocidad(4);

		satelite1.setDireccion(Satelite.DIRECCION_ATRAS);

		satelite2.setDireccion(Satelite.DIRECCION_ADELANTE);

		actores.add(satelite1);

		actores.add(satelite2);

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


		x += velocidadX / Juego.DELTA_A_PIXEL * delta;

		y += velocidadY / Juego.DELTA_A_PIXEL * delta;



		if (dezplazamientoInicial)
		{

			if (x <= (Juego.ANCHO_PANTALLA / 2) - ancho)
			{

				x += 3 / Juego.DELTA_A_PIXEL * delta;

			}
			else
			{

				dezplazamientoInicial = false;
			}

		}

		if (x >= (Juego.ANCHO_PANTALLA - 32) - ancho)
		{

			x = (Juego.ANCHO_PANTALLA - 32) - ancho;

		}

		if (x <= 32)
		{

			x = 32;

		}

		if (y >= (Juego.ALTO_PANTALLA - 32) - alto)
		{

			y = (Juego.ALTO_PANTALLA - 32) - alto;

		}

		if (y <= 32)
		{

			y = 32;

		}

		tiemoDisparo += delta;

		tiemoDisparoB += delta;

		if (disparar)
		{

			if (!poder)
			{

				disparar();
			}

			if (poder)
			{
				disparoEspecial();

			}

		}

		if (tiempoParpadeo % 2 == 0)
		{

			parpadeo = true;

		}
		else
		{

			parpadeo = false;
		}

		if (tiempoParpadeo <= 0)
		{

			tiempoParpadeo = 0;

			inmune = true;
		}
		else
		{
			inmune = false;
		}

		tiempoParpadeo -= 1;

		if (vida == 0)
		{

			vida = 0;

			satelite1.remover();

			satelite2.remover();

			remover = true;
		}


		if (dezplazamiento)
		{

			if (ciclo >= 3)
			{


				ciclo = 0;

			}


			if (ciclo == 0)
			{

				igual1 = x;

				direccionDezplazamientoX1 = coordenadaPantallaX;



			}
			if (ciclo == 1)
			{


                igual2 = x;

				direccionDezplazamientoX2 = coordenadaPantallaX;


			}

			if ((int)igual1 == (int)igual2)
			{
				dezplazamiento = false;

				satelite1.setDezlizandoDedo(false);

				satelite2.setDezlizandoDedo(false);





			}
			else
			{



				dezplazamiento = true;

				satelite1.setDezlizandoDedo(true);

				satelite2.setDezlizandoDedo(true);

			}


			if (ciclo == 2)
			{

				if ((int)direccionDezplazamientoX1 > (int)direccionDezplazamientoX2)
				{


					satelite1.setLado(Satelite.LADO_IZQUIERDO);

					satelite2.setLado(Satelite.LADO_IZQUIERDO);



				}

				if ((int)direccionDezplazamientoX1 < (int)direccionDezplazamientoX2)
				{


					satelite1.setLado(Satelite.LADO_DERECHO);

					satelite2.setLado(Satelite.LADO_DERECHO);


				}


			}


			ciclo++;

		}


	}

	@Override
	public void dibujar(Graficos pincel, float delta)
	{

		if (!parpadeo)
		{

			if (disparar)
			{

				animar = true;

				if (ladoJugador == Jugador.LADO_DERECHO)
				{

					texturas[0] = texturasJ[0];

					texturas[1] = texturasJ[1];

					super.dibujar(pincel, delta);

				}
				if (ladoJugador == Jugador.LADO_IZQUIERDO)
				{

					texturas[0] = texturasJ[2];

					texturas[1] = texturasJ[3];

					super.dibujar(pincel, delta);
				}

			}
			else
			{



				animar = false;

				if (ladoJugador == Jugador.LADO_DERECHO)
				{

					texturas[0] = texturasJ[0];

					texturas[1] = texturasJ[0];

					super.dibujar(pincel, delta);

				}
				if (ladoJugador == Jugador.LADO_IZQUIERDO)
				{

					texturas[0] = texturasJ[2];
					texturas[1] = texturasJ[2];

					super.dibujar(pincel, delta);
				}

			}

		}

	}

	@Override
	public void colision(Actor actor)
	{

		if (inmune)
		{

			if (actor instanceof BalaEnemigo || actor instanceof Volador || actor instanceof LanzaMisil
				|| actor instanceof Caja || actor instanceof BalaEnemigoDestruible || actor instanceof MaquinaFinal
				|| actor instanceof Misil || actor instanceof MaquinaPared || actor instanceof Saltador
				|| actor instanceof AntiAreo || actor instanceof Robot)
			{

				poder = false;

				velocidad = Jugador.VELOCIDAD_JUGADOR;

				tiempoParpadeo = 100;

				vida--;

			}

		}

		if (actor instanceof Poder)
		{

			Poder p = (Poder) actor;

			recurso.getSonido("poder.wav").reproducir(1);

			if (p.getPoder() != Caja.AGILIDAD_S)
			{

				tipoPoder = p.getPoder();

				poder = true;
			}

			if (p.getPoder() == Caja.AGILIDAD_S)
			{

				velocidad += 1;

			}

		}

	}

	public int getVida()
	{
		return vida;
	}

	protected void actualizarVelocidad()
	{

		velocidadX = 0;

		velocidadY = 0;

		if (abajo)
		{
			velocidadY = velocidad;
		}

		if (arriba)
		{

			velocidadY = -velocidad;
		}

		if (izquierda)
		{
			velocidadX = -velocidad;

		}

		if (derecha)
		{
			velocidadX = velocidad;

		}

	}

	public void disparar()
	{

		if (tiemoDisparo / 0.33f >= 1)
		{

			if (ladoJugador == Jugador.LADO_DERECHO)
			{


				Bala bala = new Bala(pantalla, recurso.getTextura("balaHD.png"), x + 56, y + 16, 16, 4);

				bala.setLado(Bala.DERECHO);

				actores.add(bala);


			}
			if (ladoJugador == Jugador.LADO_IZQUIERDO)
			{

				Bala bala = new Bala(pantalla, recurso.getTextura("balaHI.png"), x, y + 16, 16, 4);

				bala.setLado(Bala.IZQUIERDO);

				actores.add(bala);

			}

			tiemoDisparo = 0;

			recurso.getSonido("disparo.wav").reproducir(1);

		}

	}

	public void disparoEspecial()
	{

		if (ladoJugador == Jugador.LADO_DERECHO)
		{

			if (tiemoDisparo / 0.33f >= 1)
			{

				if (tipoPoder == BalaEspecial.BALA_W)
				{

					Textura[] texturas = new Textura[] { recurso.getTextura("balaWD1.png"),
						recurso.getTextura("balaWD2.png"), recurso.getTextura("balaWD3.png") };

					BalaEspecial bala = new BalaEspecial(pantalla, texturas, x + 56, y - 16, 16, 64, 5);

					bala.setLado(BalaEspecial.DERECHO);

					bala.setBala(BalaEspecial.BALA_W);

					actores.add(bala);

				}

				if (tipoPoder == BalaEspecial.BALA_L)
				{

					Textura[] texturas = new Textura[] { recurso.getTextura("balaLD1.png"),
						recurso.getTextura("balaLD2.png"), recurso.getTextura("balaLD3.png") };

					BalaEspecial bala = new BalaEspecial(pantalla, texturas, x - 48, y + 16, 256, 4, 5);

					bala.setLado(BalaEspecial.DERECHO);

					bala.setBala(BalaEspecial.BALA_L);

					recurso.getSonido("disparoL.wav").reproducir(1);

					actores.add(bala);

				}

				tiemoDisparo = 0;

			}

			if (tiemoDisparoB / 0.5f >= 1)
			{

				if (tipoPoder == BalaEspecial.BALA_B)
				{

					BalaEspecial bala = new BalaEspecial(pantalla, recurso.getTextura("balaBD.png"), x + 56, y + 12, 12,
														 12);

					bala.setLado(BalaEspecial.DERECHO);

					bala.setBala(BalaEspecial.BALA_B);

					recurso.getSonido("disparo.wav").reproducir(1);

					actores.add(bala);

				}

				tiemoDisparoB = 0;
			}

		}
		if (ladoJugador == Jugador.LADO_IZQUIERDO)
		{

			if (tiemoDisparo / 0.33f >= 1)
			{

				if (tipoPoder == BalaEspecial.BALA_W)
				{

					Textura[] texturas = new Textura[] { recurso.getTextura("balaWI1.png"),
						recurso.getTextura("balaWI2.png"), recurso.getTextura("balaWI3.png") };

					BalaEspecial bala = new BalaEspecial(pantalla, texturas, x, y - 16, 16, 64, 5);

					bala.setLado(BalaEspecial.IZQUIERDO);

					bala.setBala(BalaEspecial.BALA_W);

					actores.add(bala);

				}

				if (tipoPoder == BalaEspecial.BALA_L)
				{

					Textura[] texturas = new Textura[] { recurso.getTextura("balaLI1.png"),
						recurso.getTextura("balaLI2.png"), recurso.getTextura("balaLI3.png") };

					BalaEspecial bala = new BalaEspecial(pantalla, texturas, x - 144, y + 16, 256, 4, 5);

					bala.setLado(BalaEspecial.IZQUIERDO);

					bala.setBala(BalaEspecial.BALA_L);

					recurso.getSonido("disparoL.wav").reproducir(1);

					actores.add(bala);

				}

				tiemoDisparo = 0;

			}

			if (tiemoDisparoB / 0.5f >= 1)
			{

				if (tipoPoder == BalaEspecial.BALA_B)
				{

					BalaEspecial bala = new BalaEspecial(pantalla, recurso.getTextura("balaBI.png"), x, y + 12, 12, 12);

					bala.setLado(BalaEspecial.IZQUIERDO);

					bala.setBala(BalaEspecial.BALA_B);

					recurso.getSonido("disparo.wav").reproducir(1);

					actores.add(bala);
				}

				tiemoDisparoB = 0;

			}

		}

	}

	public void teclaPresionada(int codigoDeTecla)
	{

		switch (codigoDeTecla)
		{

			case KeyEvent.KEYCODE_0:

				izquierda = true;
				dezplazamientoInicial = false;

				ladoJugador = Jugador.LADO_DERECHO;

				break;

			case KeyEvent.KEYCODE_1:

				derecha = true;
				dezplazamientoInicial = false;

				ladoJugador = Jugador.LADO_IZQUIERDO;

				break;
			case KeyEvent.KEYCODE_2:

				arriba = true;
				dezplazamientoInicial = false;
				break;
			case KeyEvent.KEYCODE_3:

				abajo = true;
				dezplazamientoInicial = false;
				break;

			case KeyEvent.KEYCODE_4:

				disparar = true;

				satelite1.setDisparar(true);

				satelite2.setDisparar(true);

				break;

			case KeyEvent.KEYCODE_5:

				break;

			default:

				break;

		}

		actualizarVelocidad();

	}

	public void teclaLevantada(int codigoDeTecla)
	{

		switch (codigoDeTecla)
		{

			case KeyEvent.KEYCODE_0:

				izquierda = false;
				dezplazamientoInicial = false;

				break;

			case KeyEvent.KEYCODE_1:

				derecha = false;
				dezplazamientoInicial = false;

				break;
			case KeyEvent.KEYCODE_2:

				arriba = false;
				dezplazamientoInicial = false;
				break;
			case KeyEvent.KEYCODE_3:

				abajo = false;
				dezplazamientoInicial = false;
				break;

			case KeyEvent.KEYCODE_4:

				disparar = false;

				satelite1.setDisparar(false);

				satelite2.setDisparar(false);

				break;

			default:

				break;

		}

		actualizarVelocidad();

	}

	public void toquePresionado(float xPantalla, float yPantalla, int puntero)
	{

		if (puntero == 0)
		{

            dezplazamiento = false;

			xDeltaReferencia = x;

			disparar = true;

			satelite1.setDisparar(true);

			satelite2.setDisparar(true);

			satelite1.setDezlizandoDedo(false);

			satelite2.setDezlizandoDedo(false);


			x1 = x;

			y1 = y;

			x = x1;

			y = y1;

			deltaXTactil = (int) xPantalla - x1;

			deltaYTactil = (int) yPantalla - y1;

			if (x1 <= 32)
			{
				x1 = 32;

			}

			if (y1 >= ((Juego.ALTO_PANTALLA - 32) - alto))
			{

				y1 = (Juego.ALTO_PANTALLA - 32) - alto;

			}
			if (x1 >= (Juego.ANCHO_PANTALLA - 32) - ancho)
			{
				x1 = (Juego.ANCHO_PANTALLA - 32) - ancho;
			}

			if (y1 <= 32)
			{
				y1 = 32;

			}

		}

	}

	public void toqueLevantado(float x, float y, int puntero)
	{
		if (puntero == 0)
		{

			dezplazamiento = false;

			disparar = false;

			satelite1.setDisparar(false);

			satelite2.setDisparar(false);

			satelite1.setDezlizandoDedo(false);

			satelite2.setDezlizandoDedo(false);
		}

	}

	public void toqueDeslizando(float xPantalla, float yPantalla, int puntero)
	{

		if (puntero == 0)
		{


			xDelta = (int) xPantalla;

			if (xDelta >= xDeltaReferencia)
			{

				// Derecho

				ladoJugador = Jugador.LADO_DERECHO;



			}
			else
			{

				// Izquierdo

				ladoJugador = Jugador.LADO_IZQUIERDO;



			}

			dezplazamiento = true;

            this.coordenadaPantallaX = xPantalla;

			this.coordenadaPantallaY = yPantalla;


			x1 = (int) xPantalla - deltaXTactil;

			y1 = (int) yPantalla - deltaYTactil;

			if (x1 <= 32)
			{

				x1 = 32;
			}
			if (y1 >= (Juego.ALTO_PANTALLA - 32) - alto)
			{
				y1 = (Juego.ALTO_PANTALLA - 32) - alto;
			}

			if (x1 >= (Juego.ANCHO_PANTALLA - 32) - ancho)
			{
				x1 = (Juego.ANCHO_PANTALLA - 32) - ancho;
			}
			if (y1 <= 32)
			{

				y1 = 32;
			}

			x = x1;

			y = y1;

			dezplazamientoInicial = false;


		}

	}

	public void acelerometro(float x, float y, float z)
	{


	}

} 