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
import com.diamon.graficos.Pantalla2D;
import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Graficos;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Musica;
import com.diamon.nucleo.Textura;
import com.diamon.utilidad.Rectangulo;

public class PantallaJuego extends Pantalla2D
{

	private Vida[] vidas;

	private float tiemoMovimiento;

	private float tiemoIntro;

	private Fondo[] fondo;

	private Jugador jugador;

	private MaquinaFinal maquina;

	private boolean musicaMuriendo1;

	private boolean musicaMuriendo2;

	private float tiemoMuriendo;

	private float tiemoParaEnemigos;

	private boolean musicaIntro1;

	private boolean musicaIntro2;

	private Musica musicaIntro;

	public PantallaJuego(Juego juego)
	{
		super(juego);

		fondo = new Fondo[21];

		vidas = new Vida[3];

		musicaMuriendo1 = false;

		musicaMuriendo2 = true;

		musicaIntro1 = false;

		musicaIntro2 = true;


	}

	private void iniciar()
	{

		int posicion = 0;

		int velocidad = 2560;

		for (int i = 0; i < fondo.length - 1; i++)
		{

			if (i == 0)
			{
				fondo[i] = new Fondo(this, recurso.getTextura("fondo1.png"), posicion, 0, 640, 480);

			}

			if (i == 1)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 2)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 3)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 4)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 5)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 6)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 7)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 8)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 9)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 10)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 11)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 12)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 13)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 14)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 15)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 16)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 17)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 18)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("vida1.png"), posicion, 0, 640, 480);

			}

			if (i == 19)
			{

				fondo[i] = new Fondo(this, recurso.getTextura("fondo1.png"), posicion, 0, 640, 480);

			}

			fondo[i].setVelocidad(0);

			if (posicion > velocidad)
			{

				fondo[i].setVelocidad(0);

			}

			fondo[i].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

			posicion += 640;

			//actores.add(fondo[i]);

		}

		actores.add(fondo[0]);

		fondo[20] = new Fondo(this, recurso.getTextura("vida1.png"), 640, 0, 640, 480);

		fondo[20].setDireccion(Fondo.HORIZONTAL_IZQUIERDA);

		fondo[20].setVelocidad(0);

		//actores.add(fondo[20]);

		Textura[] texturasJ = new Textura[] { recurso.getTextura("jugador1D1.png"),  recurso.getTextura("jugador1I1.png") };



		jugador = new Jugador(this, texturasJ, 0,
							  (Juego.ALTO_PANTALLA / 3) + 64, 64, 64, 7);


		actores.add(jugador);

		jugador.agregarSatelites();



		Volador[] voladores = new Volador[3];

		Random r = new Random();

		Textura[] texturas = new Textura[] { recurso.getTextura("voladorI1.png"), recurso.getTextura("voladorI2.png"),
			recurso.getTextura("voladorI3.png") };

		for (int i = 0; i < voladores.length; i++)
		{

			voladores[i] = new Volador(this, texturas, 640, r.nextInt(200), 32, 32, 7);

			voladores[i].setVelocidad(0.05f);

			voladores[i].setDistanciaMovimiento(150);

			actores.add(voladores[i]);

		}






		AntiAreo a = new AntiAreo(this, new Textura[]{
									  recurso.getTextura("antiAreoH1.png"),
									  recurso.getTextura("antiAreoD1.png"),
									  recurso.getTextura("antiAreoV1.png"),
									  recurso.getTextura("antiAreoD2.png"),
									  recurso.getTextura("antiAreoH2.png")
								  }, 500, 400, 32, 32, 1);


		actores.add(a);




		agregarLanzaMisil();


		Textura[] texturasCaja = new Textura[] { recurso.getTextura("cajaPoder1.png"),
			recurso.getTextura("cajaPoder2.png"), recurso.getTextura("cajaPoder3.png"),
			recurso.getTextura("cajaPoder4.png") };

		int posicionCaja = 64;

		Caja[] cajas = new Caja[4];

		for (int i = 0; i < cajas.length; i++)
		{

			cajas[i] = new Caja(this, texturasCaja, 1920, posicionCaja, 32, 32, 10);

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

		maquina = new MaquinaFinal(this, recurso.getTextura("maquinaFinal.png"), 0, 480, 64, 64);

		actores.add(maquina);

		int pocicionVida = 32;

		for (int i = 0; i < vidas.length; i++)
		{

			vidas[i] = new Vida(this, recurso.getTextura("vida1.png"), pocicionVida, 32, 16, 32);

			pocicionVida += 32;

			actores.add(vidas[i]);

		}

		musicaIntro = recurso.getMusica("comienzo1.wav");

		musicaIntro.setVolumen(1);

		musicaIntro.reproducir();

	}

	private void moverFondo()
	{

		if ((int) fondo[4].getX() == 0 && (int) fondo[4].getY() == 0)
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

		if ((int) fondo[7].getX() == 0 && (int) fondo[7].getY() == 0)
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

		if ((int) fondo[10].getX() == 0 && (int) fondo[10].getY() == 0)
		{

			fondo[10].setPosicion(0, 0);

			fondo[10].setVelocidad(1);

			fondo[10].setDireccion(Fondo.VERTICAL_ARRIBA);

			fondo[11].setPosicion(0, 480);

			fondo[11].setVelocidad(1);

			fondo[11].setDireccion(Fondo.VERTICAL_ARRIBA);

		}

		if ((int) fondo[11].getX() == 0 && (int) fondo[11].getY() == 0)
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

		if ((int) fondo[13].getX() == 0 && (int) fondo[13].getY() == 0)
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

		if ((int) fondo[15].getX() == 0 && (int) fondo[15].getY() == 0)
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

			if ((int) fondo[20].getX() == 0 && (int) fondo[20].getY() == 0)
			{

				fondo[20].setParar(true);

				maquina.setPosicion(512, 176);

				maquina.setDisparar(true);

			}

		}

	}

	public void jugadorMuriendo()
	{

		Textura[] texturas = new Textura[] { recurso.getTextura("jugador1D4.png"), recurso.getTextura("jugador1D5.png"),
			recurso.getTextura("jugador1D6.png") };

		JugadorMuriendo j = new JugadorMuriendo(this, texturas, jugador.getX(), jugador.getY(), 64, 64, 15);

		actores.add(j);

	}

	private void colocarEnemigos()
	{

		if (tiemoParaEnemigos / 10 >= 1)
		{

			if (40 == tiemoParaEnemigos)
			{

				agregarMaquinaAntiAreo();
			}

		}

		if (tiemoParaEnemigos / 13.33f >= 1)
		{

			if (13.33f == tiemoParaEnemigos)
			{

				agregarSaltador();
			}

		}

		if (tiemoParaEnemigos / 41.66 >= 1)
		{

			if (41.66f == tiemoParaEnemigos)
			{
				agregarRobot();

				agregarLanzaMisil();

				;
			}

		}

		if (tiemoParaEnemigos / 45.83f >= 1)
		{

			if (45.83f == tiemoParaEnemigos)
			{
				agregarMaquinaPared();

			}

		}

		if (tiemoParaEnemigos / 50 >= 1)
		{

			if (50 == tiemoParaEnemigos)
			{

				agregarMaquinaPared();
			}

		}

		if (tiemoParaEnemigos / 53.33f >= 1)
		{

			if (53.33f == tiemoParaEnemigos)
			{

				agregarMaquinaPared();
			}

		}

		if (tiemoParaEnemigos / 116.66f >= 1)
		{

			if (116.66f == tiemoParaEnemigos)
			{

				agregarPoder();
			}

		}

		if (tiemoParaEnemigos / 166.66f >= 1)
		{

			tiemoParaEnemigos = 166.66f;
		}

	}

	private void agregarLanzaMisil()
	{

		Textura[] texturas = new Textura[] { recurso.getTextura("lanzaMisilI1.png"), recurso.getTextura("lanzaMisilI2.png"),
			recurso.getTextura("lanzaMisilI3.png") };

		LanzaMisil lanzaMisil = new LanzaMisil(this, texturas, 640, 100, 48, 32, 20);


		actores.add(lanzaMisil);
	}




	private void agregarSaltador()
	{

		Saltador saltador = new Saltador(this, recurso.getTextura("saltador2.png"), 640, 320, 48, 64);

		actores.add(saltador);

	}

	private void agregarRobot()
	{

		Robot robot1 = new Robot(this, recurso.getTextura("robotI.png"), 639, 0, 64, 64);

		robot1.setLado(Robot.LADO_IZQUIERDO);

		actores.add(robot1);

		Robot robot2 = new Robot(this, recurso.getTextura("robotD.png"), 0, 0, 64, 64);

		robot2.setTamano(64, 64);

		robot2.setPosicion(0, 0);

		robot2.setLado(Robot.LADO_DERECHO);

		actores.add(robot2);
	}

	private void agregarMaquinaPared()
	{

		MaquinaPared maquinaPared = new MaquinaPared(this, recurso.getTextura("maquinaParedD1.png"), 532, 0, 32, 32);

		maquinaPared.setVelocidadY(1);

		maquinaPared.setMover(MaquinaPared.MOVER_ABAJO);

		maquinaPared.setLado(MaquinaPared.LADO_IZQUIERDO);

		actores.add(maquinaPared);

	}

	private void agregarMaquinaAntiAreo()
	{

		AntiAreo antiAreo = new AntiAreo(this, recurso.getTextura("antiAreoH1.png"), 640, 352, 32, 32);

		actores.add(antiAreo);

	}

	private void agregarPoder()
	{

		Textura[] texturasCaja = new Textura[] { recurso.getTextura("cajaPoder1.png"),
			recurso.getTextura("cajaPoder2.png"), recurso.getTextura("cajaPoder3.png"),
			recurso.getTextura("cajaPoder4.png") };

		int posicionCaja = 64;

		Caja[] cajas = new Caja[4];

		for (int i = 0; i < cajas.length; i++)
		{

			cajas[i] = new Caja(this, texturasCaja, 1920, posicionCaja, 32, 32, 10);

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
	public void mostrar()
	{

		iniciar();


	}

	@Override
	public void resume()
	{

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

	@Override
	public void actualizar(float delta)
	{

		if (true)
		{

			for (int i = 0; i < actores.size(); i++)
			{

				actores.get(i).actualizar(delta);

			}

			moverFondo();

			if (tiemoParaEnemigos <= 166.66f)
			{

				tiemoParaEnemigos += delta;

			}

		}

		// Colocando enemigos cada cierto Tiempo

		colocarEnemigos();

		////////////////////////////////

		if (musicaIntro2)
		{

			tiemoIntro += delta;

			if (tiemoIntro / 3.66f >= 1)
			{

				musicaIntro2 = false;

				musicaIntro1 = true;

				tiemoIntro = 0;
			}

		}

		if (musicaIntro1)
		{

			recurso.getMusica("musica.wav").setVolumen(1);

			recurso.getMusica("musica.wav").reproducir();

			recurso.getMusica("musica.wav").setRepetir(true);

			musicaIntro1 = false;
		}

		if (jugador.getVida() <= 0)
		{

			tiemoMovimiento += delta;

			if (tiemoMovimiento / 3.33f >= 1)
			{

				juego.setPantalla(new PantallaFinDeJuego(juego));

				tiemoMovimiento = 0;
			}

		}

		if (maquina.getVida() == 0)
		{

			tiemoMovimiento += delta;

			if (tiemoMovimiento / 1.66f >= 1)
			{

				recurso.getMusica("musica.wav").pausar();

				recurso.getMusica("musica.wav").terminar();

				juego.setPantalla(new PantallaFinal(juego));

				tiemoMovimiento = 0;
			}

		}

		tiemoMuriendo += delta;

		if (jugador.getVida() <= 0)
		{

			musicaMuriendo1 = true;

			if (tiemoMuriendo / 0.83f >= 1)
			{

				for (int i = 0; i < actores.size(); i++)
				{

					if (actores.get(i) instanceof JugadorMuriendo)
					{
						JugadorMuriendo j = (JugadorMuriendo) actores.get(i);

						j.remover();

					}
				}

				tiemoMuriendo = 0;
			}

		}

		if (musicaMuriendo1)
		{

			if (musicaMuriendo2)
			{

				recurso.getMusica("musica.wav").pausar();

				recurso.getMusica("musica.wav").terminar();

				recurso.getSonido("muriendo.wav").reproducir(1);

				jugadorMuriendo();

				musicaMuriendo2 = false;
			}

		}

	}

	@Override
	public void dibujar(Graficos pincel, float delta)
	{

		for (int i = 0; i < actores.size(); i++)
		{

			actores.get(i).dibujar(pincel, delta);

		}

	}

	@Override
	public void reajustarPantalla(int ancho, int alto)
	{

	}

	@Override
	public void pausa()
	{

	}

	@Override
	public void ocultar()
	{

		this.configuracionesDeJuego.guardarConfiguraciones();

		actores.clear();

	}

	@Override
	public void liberarRecursos()
	{

	}

	private void removerVida()
	{

		if (jugador.getVida() >= 0 && jugador.getVida() < 3)
		{

			vidas[jugador.getVida()].remover();
		}

	}

	@Override
	public void teclaPresionada(int codigoDeTecla)
	{

		for (int i = 0; i < actores.size(); i++)
		{

			if (actores.get(i) instanceof Jugador)
			{
				Jugador a = (Jugador) actores.get(i);

				a.teclaPresionada(codigoDeTecla);

			}
		}

	}

	@Override
	public void teclaLevantada(int codigoDeTecla)
	{
		// TODO Auto-generated method stub

		for (int i = 0; i < actores.size(); i++)
		{

			if (actores.get(i) instanceof Jugador)
			{
				Jugador a = (Jugador) actores.get(i);

				a.teclaLevantada(codigoDeTecla);

			}
		}

	}

	@Override
	public void toquePresionado(float x, float y, int puntero)
	{

		for (int i = 0; i < actores.size(); i++)
		{

			if (actores.get(i) instanceof Jugador)
			{
				Jugador a = (Jugador) actores.get(i);

				a.toquePresionado(x, y, puntero);

			}
		}

	}

	@Override
	public void toqueLevantado(float x, float y, int puntero)
	{

		for (int i = 0; i < actores.size(); i++)
		{

			if (actores.get(i) instanceof Jugador)
			{
				Jugador a = (Jugador) actores.get(i);

				a.toqueLevantado(x, y, puntero);

			}
		}

	}

	@Override
	public void toqueDeslizando(float x, float y, int puntero)
	{

		for (int i = 0; i < actores.size(); i++)
		{

			if (actores.get(i) instanceof Jugador)
			{
				Jugador a = (Jugador) actores.get(i);

				a.toqueDeslizando(x, y, puntero);

			}
		}

	}

	@Override
	public void acelerometro(float x, float y, float z)
	{

		for (int i = 0; i < actores.size(); i++)
		{

			if (actores.get(i) instanceof Jugador)
			{
				Jugador a = (Jugador) actores.get(i);

				a.acelerometro(x, y, z);

			}
		}

	}

}