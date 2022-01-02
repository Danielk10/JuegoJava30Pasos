package com.diamon.pantalla;

import java.util.Random;

import com.diamon.actor.AntiAreo;
import com.diamon.actor.Caja;
import com.diamon.actor.Fondo;
import com.diamon.actor.Jugador;
import com.diamon.actor.JugadorMuriendo;
import com.diamon.actor.LanzaMisil;
import com.diamon.actor.MaquinaFinal;
import com.diamon.actor.MaquinaPared;
import com.diamon.actor.Robot;
import com.diamon.actor.Saltador;
import com.diamon.actor.Vida;
import com.diamon.actor.Volador;
import com.diamon.juego.FinalMision;
import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.utilidad.Rectangulo;
import com.diamon.utilidad.Recurso;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.diamon.utilidad.Texturas;


public class PantallaJuego extends Pantalla
{

	private boolean pausa;

	private Vida[] vidas;

	private int ciclo;

	private int cicloIntro;

	private Fondo[] fondo;

	private Jugador jugador;

	private MaquinaFinal maquina;

	private boolean musicaMuriendo1;

	private boolean musicaMuriendo2;

	private int cicloMuriendo;

	private int cicloParaEnemigos;

	private boolean musicaIntro1;

	private boolean musicaIntro2;


	public PantallaJuego(FinalMision juego)
	{
		super(juego);

		pausa = true;
		

		fondo = new Fondo[21];

		vidas = new Vida[4];

		ciclo = 0;

		cicloIntro = 0;

		cicloParaEnemigos = 0;

		musicaMuriendo1 = false;

		musicaMuriendo2 = true;

		musicaIntro1 = false;
		musicaIntro2 = true;

		cicloMuriendo = 0;

		iniciar();

	}

	private void iniciar()
	{

		int contador = 1;

		int posicion = 0;

		int velocidad = 2560;

		for (int i = 0; i < fondo.length - 1; i++)
		{

			fondo[i] = new Fondo(this);

			fondo[i].setTamano(640, 480);

			fondo[i].setPosicion(posicion, 0);

			fondo[i].setVelocidad(1);

			if (posicion > velocidad)
			{
				fondo[i].setVelocidad(0);

			}

			fondo[i].setImagenes(new Bitmap[] {
									 Recurso.crearBitmap(juego.getRecurso().getImagen("fondo" + contador + ".png"), 640, 480) });

			fondo[i].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

			contador += 1;

			posicion += 640;

			actores.add(fondo[i]);

		}

		fondo[20] = new Fondo(this);

		fondo[20].setTamano(640, 480);

		fondo[20].setPosicion(640, 0);

		fondo[20].setImagenes(
			new Bitmap[] { Recurso.crearBitmap(juego.getRecurso().getImagen("fondo21MGF.png"), 640, 480) });

		fondo[20].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

		fondo[20].setVelocidad(0);

		actores.add(fondo[20]);

		Volador[] voladores = new Volador[50];

		Random r = new Random();

		for (int i = 0; i < voladores.length; i++)
		{
			voladores[i] = new Volador(this);

			voladores[i].setTamano(32, 32);

			voladores[i].setCuadros(7);

			voladores[i].setPosicion(r.nextInt(30000) + 640, r.nextInt(480));

			voladores[i].setImagenes(
				new Bitmap[] { Texturas.voladorI1, Texturas.voladorI2, Texturas.voladorI3 });

			voladores[i].setVelocidadY((int) (Math.random() * 7 - 5));

			actores.add(voladores[i]);

		}

		jugador = new Jugador(this);

		jugador.setTamano(64, 64);

		jugador.setPosicion(0, (Juego.ALTO_PANTALLA / 3) + jugador.getAncho());

		jugador.setImagenes(new Bitmap[] { Texturas.jugador1D1 });

		actores.add(jugador);

		int posicionCaja = 64;

		Caja[] cajas = new Caja[4];

		for (int i = 0; i < cajas.length; i++)
		{
			cajas[i] = new Caja(this);

			cajas[i].setTamano(32, 32);

			cajas[i].setPosicion(1920, posicionCaja);

			cajas[i].setCuadros(10);

			cajas[i].setImagenes(new Bitmap[] { Texturas.cajaPoder1, Texturas.cajaPoder2,
									 Texturas.cajaPoder3, Texturas.cajaPoder4 });

			posicionCaja += 96;

		}

		cajas[0].setAgilidad(Caja.AGILIDAD_S);
		cajas[1].setPoderBala(Caja.PODER_B);
		cajas[2].setPoderBala(Caja.PODER_W);
		cajas[3].setPoderBala(Caja.PODER_L);

		actores.add(cajas[0]);
		actores.add(cajas[1]);
		actores.add(cajas[2]);
		actores.add(cajas[3]);

		maquina = new MaquinaFinal(this);

		maquina.setTamano(64, 64);

		maquina.setPosicion(0, 480);

		maquina.setImagenes(new Bitmap[] { Texturas.maquinaFinal });

		maquina.setCuadros(20);

		actores.add(maquina);

		int pocicionVida = 32;

		for (int i = 0; i < vidas.length; i++)
		{

			vidas[i] = new Vida(this);

			vidas[i].setTamano(16, 32);

			vidas[i].setPosicion(pocicionVida, 32);

			vidas[i].setImagenes(new Bitmap[] { Texturas.vida1 });

			pocicionVida += 32;

			actores.add(vidas[i]);

		}

       juego.getRecurso().playMusica("comienzo1.wav", 1);

	}

	@Override
	public void pausa()
	{


		pausa = !pausa;

		

		if (jugador.getVida() != 0)
		{
			if (pausa)
			{
				juego.getRecurso().repetirMusica("musica.wav", 1);
			}
			else
			{
				
			
				juego.getRecurso().pararMusica(juego.getRecurso().getMusica("musica.wav"));


			}}

	}

	@Override
	public void resume()
	{




		pausa = !pausa;


		if (jugador.getVida() != 0)
		{
			if (pausa)
			{
				juego.getRecurso().repetirMusica("musica.wav", 1);
			}
			else
			{
				juego.getRecurso().pararMusica(juego.getRecurso().getMusica("musica.wav"));
				
				
			}}

	}

	private void moverFondo()
	{

		if (fondo[4].getX() == 0 && fondo[4].getY() == 0)
		{

			fondo[4].setPosicion(0, 0);

			fondo[4].setVelocidad(1);

			fondo[4].setDireccion(Fondo.VERTICAL_ABAJO);

			fondo[5].setPosicion(0, -480);

			fondo[5].setVelocidad(1);

			fondo[5].setDireccion(Fondo.VERTICAL_ABAJO);

			fondo[6].setPosicion(0, -960);

			fondo[6].setVelocidad(1);

			fondo[6].setDireccion(Fondo.VERTICAL_ABAJO);

			fondo[7].setPosicion(0, -1380);

			fondo[7].setVelocidad(1);

			fondo[7].setDireccion(Fondo.VERTICAL_ABAJO);

		}

		if (fondo[7].getX() == 0 && fondo[7].getY() == 0)
		{

			fondo[7].setPosicion(0, 0);

			fondo[7].setVelocidad(1);

			fondo[7].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

			fondo[8].setPosicion(640, 0);

			fondo[8].setVelocidad(1);

			fondo[8].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

			fondo[9].setPosicion(1280, 0);

			fondo[9].setVelocidad(1);

			fondo[9].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

			fondo[10].setPosicion(1920, 0);

			fondo[10].setVelocidad(1);

			fondo[10].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

		}

		if (fondo[10].getX() == 0 && fondo[10].getY() == 0)
		{

			fondo[10].setPosicion(0, 0);

			fondo[10].setVelocidad(1);

			fondo[10].setDireccion(Fondo.VERTICAL_ARRIBA);

			fondo[11].setPosicion(0, 480);

			fondo[11].setVelocidad(1);

			fondo[11].setDireccion(Fondo.VERTICAL_ARRIBA);

		}

		if (fondo[11].getX() == 0 && fondo[11].getY() == 0)
		{

			fondo[11].setPosicion(0, 0);

			fondo[11].setVelocidad(1);

			fondo[11].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

			fondo[12].setPosicion(640, 0);

			fondo[12].setVelocidad(1);

			fondo[12].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

			fondo[13].setPosicion(1280, 0);

			fondo[13].setVelocidad(1);

			fondo[13].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

		}

		if (fondo[13].getX() == 0 && fondo[13].getY() == 0)
		{

			fondo[13].setPosicion(0, 0);

			fondo[13].setVelocidad(1);

			fondo[13].setDireccion(Fondo.VERTICAL_ARRIBA);

			fondo[14].setPosicion(0, 480);

			fondo[14].setVelocidad(1);

			fondo[14].setDireccion(Fondo.VERTICAL_ARRIBA);

			fondo[15].setPosicion(0, 960);

			fondo[15].setVelocidad(1);

			fondo[15].setDireccion(Fondo.VERTICAL_ARRIBA);

		}

		if (fondo[15].getX() == 0 && fondo[15].getY() == 0)
		{

			fondo[15].setPosicion(0, 0);

			fondo[15].setVelocidad(1);

			fondo[15].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

			fondo[16].setPosicion(640, 0);

			fondo[16].setVelocidad(1);

			fondo[16].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

			fondo[17].setPosicion(1280, 0);

			fondo[17].setVelocidad(1);

			fondo[17].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

			fondo[18].setPosicion(1920, 0);

			fondo[18].setVelocidad(1);

			fondo[18].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

			fondo[19].setPosicion(2560, 0);

			fondo[19].setVelocidad(1);

			fondo[19].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

			fondo[20].setPosicion(3200, 0);

			fondo[20].setVelocidad(1);

			fondo[20].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

		}

		if (!fondo[20].isParar())
		{

			if (fondo[20].getX() == 0 && fondo[20].getY() == 0)
			{

				fondo[20].setParar(true);

				maquina.setPosicion(512, 176);

				maquina.setDisparar(true);

			}

		}

	}

	public void jugadorMuriendo()
	{

		JugadorMuriendo j = new JugadorMuriendo(this);

		j.setTamano(64, 64);

		j.setPosicion(jugador.getX(), jugador.getY());

		j.setCuadros(15);

		j.setImagenes(new Bitmap[] { Texturas.jugador1D4, Texturas.jugador1D5,
						  Texturas.jugador1D6 });

		actores.add(j);

	}

	@Override
	public void actualizar(float delta)
	{

		if (pausa)
		{

			for (int i = 0; i < actores.size(); i++)
			{

				actores.get(i).actualizar(delta);

			}

			moverFondo();

			if (cicloParaEnemigos <= 10100)
			{

				cicloParaEnemigos++;

			}

		}

		// Colocando enemigos cada cierto Tiempo

		colocarEnemigos();

		////////////////////////////////

		if (musicaIntro2)
		{

			cicloIntro++;

			if (cicloIntro % 220 == 0)
			{

				musicaIntro2 = false;

				musicaIntro1 = true;

				cicloIntro = 0;

			}

		}

		if (musicaIntro1)
		{

			juego.getRecurso().repetirMusica("musica.wav", 1);

			musicaIntro1 = false;
		}

		if (jugador.getVida() <= 0)
		{

			ciclo++;

			if (ciclo % 200 == 0)
			{

				juego.setPantalla(new PantallaFinDeJuego(juego));

				ciclo = 0;
			}

		}

		if (maquina.getVida() == 0)
		{

			ciclo++;

			if (ciclo % 100 == 0)
			{

				juego.getRecurso().pararMusica(juego.getRecurso().getMusica("musica.wav"));

				juego.setPantalla(new PantallaFinal(juego));

				ciclo = 0;

			}

		}

		cicloMuriendo++;

		if (jugador.getVida() <= 0)
		{

			musicaMuriendo1 = true;

			if (cicloMuriendo % 50 == 0)
			{

				for (int i = 0; i < actores.size(); i++)
				{

					if (actores.get(i) instanceof JugadorMuriendo)
					{
						JugadorMuriendo j = (JugadorMuriendo) actores.get(i);

						j.remover();

					}
				}

				cicloMuriendo = 0;

			}

		}

		if (musicaMuriendo1)
		{

			if (musicaMuriendo2)
			{

				juego.getRecurso().pararMusica(juego.getRecurso().getMusica("musica.wav"));

				juego.getRecurso().playMusica("muriendo.wav", 1);

				jugadorMuriendo();

				musicaMuriendo2 = false;
			}

		}

	}

	private void colocarEnemigos()
	{

		if (cicloParaEnemigos % 600 == 0)
		{

			if (600 == cicloParaEnemigos)
			{

				agregarMaquinaAntiAreo();

				agregarVolador();
			}

		}

		if (cicloParaEnemigos % 800 == 0)
		{

			if (800 == cicloParaEnemigos)
			{

				agregarSaltador();

			}

		}

		if (cicloParaEnemigos % 2500 == 0)
		{

			agregarRobot();

			agregarLanzaMisil();

		}
		if (cicloParaEnemigos % 2750 == 0)
		{

			if (2750 == cicloParaEnemigos)
			{

				agregarMaquinaPared();
			}

		}

		if (cicloParaEnemigos % 3000 == 0)
		{

			if (3000 == cicloParaEnemigos)
			{

				agregarMaquinaPared();
			}

		}

		if (cicloParaEnemigos % 3200 == 0)
		{

			if (3200 == cicloParaEnemigos)
			{

				agregarMaquinaPared();
			}

		}

		if (cicloParaEnemigos % 7000 == 0)
		{

			if (7000 == cicloParaEnemigos)
			{

				agregarPoder();
			}

		}

		if (cicloParaEnemigos % 10000 == 0)
		{

			cicloParaEnemigos = 10100;

		}

	}

	private void agregarLanzaMisil()
	{

		LanzaMisil lanzaMisil = new LanzaMisil(this);

		lanzaMisil.setTamano(48, 32);

		lanzaMisil.setPosicion(640, 100);

		lanzaMisil.setCuadros(20);

		lanzaMisil.setImagenes(
			new Bitmap[] { Texturas.lanzaMisilI1,Texturas.lanzaMisilI2, 
			Texturas.lanzaMisilI3 });

		actores.add(lanzaMisil);
	}

	private void agregarVolador()
	{

		Random r = new Random();

		Volador volador = new Volador(this);

		volador.setTamano(32, 32);

		volador.setCuadros(7);

		volador.setPosicion(r.nextInt(640) + 640, r.nextInt(480) - 32);

		volador.setImagenes(new Bitmap[] { Texturas.voladorI1, 
								Texturas.voladorI2, Texturas.voladorI3 });

		volador.setVelocidadY((int) (Math.random() * 7 - 5));

		actores.add(volador);

	}

	private void agregarSaltador()
	{

		Saltador saltador = new Saltador(this);

		saltador.setTamano(48, 64);

		saltador.setPosicion(640, 320);

		saltador.setCuadros(20);

		saltador.setImagenes(new Bitmap[] { Texturas.saltador2 });

		actores.add(saltador);

	}

	private void agregarRobot()
	{

		Robot robot1 = new Robot(this);

		robot1.setTamano(64, 64);

		robot1.setPosicion(639, 0);

		robot1.setLado(Robot.LADO_IZQUIERDO);

		robot1.setImagenes(new Bitmap[] {Texturas.robotI });

		actores.add(robot1);

		Robot robot2 = new Robot(this);

		robot2.setTamano(64, 64);

		robot2.setPosicion(0, 0);

		robot2.setLado(Robot.LADO_DERECHO);

		robot2.setImagenes(new Bitmap[] { Texturas.robotD });

		actores.add(robot2);
	}

	private void agregarMaquinaPared()
	{

		MaquinaPared maquinaPared = new MaquinaPared(this);

		maquinaPared.setTamano(32, 32);

		maquinaPared.setPosicion(532, 0);

		maquinaPared.setVelocidadY(1);

		maquinaPared.setMover(MaquinaPared.MOVER_ABAJO);

		maquinaPared.setLado(MaquinaPared.LADO_IZQUIERDO);

		maquinaPared.setImagenes(new Bitmap[] { Texturas.maquinaParedD1 });

		actores.add(maquinaPared);

	}

	private void agregarMaquinaAntiAreo()
	{

		AntiAreo antiAreo = new AntiAreo(this);

		antiAreo.setTamano(32, 32);

		antiAreo.setPosicion(640, 352);

		antiAreo.setImagenes(new Bitmap[] { Texturas.antiAreoH1 });

		actores.add(antiAreo);

	}

	private void agregarPoder()
	{
		int posicionCaja = 64;

		Caja[] cajas = new Caja[4];

		for (int i = 0; i < cajas.length; i++)
		{
			cajas[i] = new Caja(this);

			cajas[i].setTamano(32, 32);

			cajas[i].setPosicion(1920, posicionCaja);

			cajas[i].setCuadros(10);

			cajas[i].setImagenes(new Bitmap[] { Texturas.cajaPoder1, Texturas.cajaPoder2,
									 Texturas.cajaPoder3, Texturas.cajaPoder4 });

			posicionCaja += 96;

		}

		cajas[0].setAgilidad(Caja.AGILIDAD_S);
		cajas[1].setPoderBala(Caja.PODER_B);
		cajas[2].setPoderBala(Caja.PODER_W);
		cajas[3].setPoderBala(Caja.PODER_L);

		actores.add(cajas[0]);
		actores.add(cajas[1]);
		actores.add(cajas[2]);
		actores.add(cajas[3]);
	}

	@Override
	public void dibujar(Canvas pincel, float delta)
	{

		for (int i = 0; i < actores.size(); i++)
		{

			actores.get(i).dibujar(pincel, delta);

		}
		
	

		

	}

	@Override
	public void colisiones()
	{

		for (int i = 0; i < actores.size(); i++)
		{

			Actor a1 = (Actor) actores.get(i);

			Rectangulo r1 = a1.getRectangulo();

			for (int j = i + 1; j < actores.size(); j++)
			{
				Actor a2 = (Actor) actores.get(j);
				Rectangulo r2 = a2.getRectangulo();
				if (r1.Intersecion(r2))
				{

					a1.colision(a2);
					a2.colision(a1);

				}
			}

			Actor actor = (Actor) actores.get(i);

			if (actor.isRemover())
			{

				actores.remove(i);

			}

		}

		removerVida();

	}

	private void removerVida()
	{

		if (jugador.getVida() >= 0)
		{

			vidas[jugador.getVida()].remover();
		}

	}

	@Override
	public void ocultar()
	{
		juego.getConfiguracioin().guardarConfiguraciones(juego.getDatos());

		actores.clear();
	}

	@Override
	public void mostrar()
	{

	}

	@Override
	public void reajustarPantalla(int ancho, int alto)
	{

	}

	@Override
	public void teclaPresionada(KeyEvent ev)
	{

		for (int i = 0; i < actores.size(); i++)
		{

			if (actores.get(i) instanceof Jugador)
			{
				Jugador a = (Jugador) actores.get(i);

				a.teclaPresionada(ev);

			}
		}

		switch (ev.getKeyCode())
		{

			case KeyEvent.KEYCODE_0:

				if (jugador.getVida() != 0)
				{
					if (pausa)
					{
						juego.getRecurso().pararMusica(juego.getRecurso().getMusica("musica.wav"));
						juego.getRecurso().playMusica("pausa.wav", 1);
					}
					else
					{

						juego.getRecurso().repetirMusica("musica.wav", 1);
					}

					pausa = !pausa;

					for (int i = 0; i < actores.size(); i++)
					{

						if (actores.get(i) instanceof Jugador)
						{
							Jugador a = (Jugador) actores.get(i);

							a.setPausado(pausa);

						}
					}

				}

				break;

			default:

				break;

		}

	}

	@Override
	public void teclaLevantada(KeyEvent ev)
	{

		for (int i = 0; i < actores.size(); i++)
		{

			if (actores.get(i) instanceof Jugador)
			{
				Jugador a = (Jugador) actores.get(i);

				a.teclaLevantada(ev);

			}
		}

	}

	@Override
	public void toque(MotionEvent ev)
	{

		for (int i = 0; i < actores.size(); i++)
		{

			if (actores.get(i) instanceof Jugador)
			{
				Jugador a = (Jugador) actores.get(i);

				a.toque(ev);

			}
		}

	}

	@SuppressWarnings("unused")
	@Override
	public void multiToque(MotionEvent ev)
	{
		int accion = ev.getAction() & MotionEvent.ACTION_MASK;

		int punteroIndice = (ev.getAction()
			& MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

		int punteroID = ev.getPointerId(punteroIndice);

		switch (accion)
		{

			case MotionEvent.ACTION_DOWN:

				break;
			case MotionEvent.ACTION_POINTER_DOWN:

				if (jugador.getVida() != 0)
				{
					if (pausa)
					{
						juego.getRecurso().pararMusica(juego.getRecurso().getMusica("musica.wav"));
						juego.getRecurso().playMusica("pausa.wav", 1);
					}
					else
					{

						juego.getRecurso().repetirMusica("musica.wav", 1);
					}

					pausa = !pausa;

					for (int i = 0; i < actores.size(); i++)
					{

						if (actores.get(i) instanceof Jugador)
						{
							Jugador a = (Jugador) actores.get(i);

							a.setPausado(pausa);

						}
					}

				}

				break;
			case MotionEvent.ACTION_UP:




				break;
			case MotionEvent.ACTION_POINTER_UP:

				break;
			case MotionEvent.ACTION_CANCEL:

				break;

			case MotionEvent.ACTION_MOVE:

				break;

			default:

				break;

		}

	}

	@Override
	public void acelerometro(SensorEvent ev)
	{
		// TODO Auto-generated method stub

	}

}
