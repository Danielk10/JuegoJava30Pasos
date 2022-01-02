package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.diamon.utilidad.*;

public class Jugador extends Actor {

	private int deltaXTactil, deltaYTactil;

	private int xDelta = 0;

	private int xDeltaReferencia = 0;

	private int x1 = 0;

	private int y1 = 0;

	private int velocidadX;

	private int velocidadY;

	private final static int VELOCIDAD_JUGADOR = 5;

	private boolean arriba, abajo, izquierda, derecha, disparar;

	private int cicloDisparo;

	private int cicloDisparoB;

	private int cicloParpadeo;

	private int cuadros = 7;

	private int timpo = 0;;

	protected int frames = 0;

	private Bitmap[] imagenes1 = new Bitmap[2];

	private Bitmap[] imagenes2 = new Bitmap[2];

	private boolean dezplazamientoInicial;

	private boolean parpadeo;

	private boolean lado;

	private boolean pausado;

	private int vida;

	private boolean inmune;

	private boolean poder;

	private int tipoPoder;

	private int velocidad;

	private Satelite satelite1;

	private Satelite satelite2;

	public Jugador(Pantalla pantalla) {
		super(pantalla);

		velocidadX = 0;

		velocidadY = 0;

		deltaXTactil = deltaYTactil = 0;

		dezplazamientoInicial = true;

		arriba = abajo = izquierda = derecha = disparar = false;

		cicloParpadeo = 0;

		cicloDisparoB = 0;

		cicloDisparo = 0;

		parpadeo = false;

		velocidad = VELOCIDAD_JUGADOR;

		lado = false;

		poder = false;

		pausado = true;

		vida = 3;

		inmune = false;

		tipoPoder = 0;

		satelite1 = new Satelite(pantalla);

		satelite2 = new Satelite(pantalla);

		satelite1.setTamano(16, 16);

		satelite1.setPosicion(0, 0);

		satelite1.setCuadros(1);

		satelite1.setImagenes(new Bitmap[] { Texturas.sateliteHD1 });

		satelite2.setTamano(16, 16);

		satelite2.setPosicion(0, 0);

		satelite2.setCuadros(1);

		satelite2.setImagenes(new Bitmap[] { Texturas.sateliteHD1 });

		satelite1.setDisparar(false);

		satelite2.setDisparar(false);

		pantalla.getActores().add(satelite1);

		pantalla.getActores().add(satelite2);

	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public void setPausado(boolean pausado) {
		this.pausado = pausado;
	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		if (!lado) {

			satelite1.setPosicion((x + 46), (y - 16));

			satelite2.setPosicion(x + 46, y + 36);

		} else {
			satelite1.setPosicion(x + 64, y - 16);

			satelite2.setPosicion(x + 64, y + 36);

		}

		x += velocidadX;

		y += velocidadY;

		timpo++;

		if (!lado) {
			if (timpo % cuadros == 0) {
				timpo = 0;
				frames = (frames + 1) % imagenes1.length;

			}

		}

		else {

			if (timpo % cuadros == 0) {
				timpo = 0;
				frames = (frames + 1) % imagenes2.length;

			}

		}

		if (dezplazamientoInicial) {

			if (x <= (Juego.ANCHO_PANTALLA / 2) - ancho) {

				x += 3;

			} else {

				dezplazamientoInicial = false;
			}

		}

		if (x >= (Juego.ANCHO_PANTALLA - 32) - ancho) {

			x = (Juego.ANCHO_PANTALLA - 32) - ancho;

		}

		if (x <= 32) {

			x = 32;

		}

		if (y >= (Juego.ALTO_PANTALLA - 32) - alto) {

			y = (Juego.ALTO_PANTALLA - 32) - alto;

		}

		if (y <= 32) {

			y = 32;

		}

		cicloDisparoB++;

		cicloDisparo++;

		if (disparar) {

			if (!poder) {

				disparar();
			}

			if (poder) {
				disparoEspecial();

			}

		}

		if (cicloParpadeo % 2 == 0) {

			parpadeo = true;

		} else {

			parpadeo = false;
		}

		if (cicloParpadeo <= 0) {
			cicloParpadeo = 0;

			inmune = true;
		} else {
			inmune = false;
		}
		cicloParpadeo--;

		if (vida == 0) {

			vida = 0;

			satelite1.remover();
			satelite2.remover();

			remover = true;
		}

	}

	@Override
	public void dibujar(Canvas pincel, float delta) {

		if (!parpadeo) {

			if (disparar) {

				if (!lado) {

					imagenes1[0] = imagenes[0] = Texturas.jugador1D1;

					imagenes1[1] = imagenes[0] = Texturas.jugador1D2;

					pincel.drawBitmap(imagenes1[frames], x, y, null);

				} else {

					imagenes2[0] = Texturas.jugador1I1;

					imagenes2[1] = Texturas.jugador1I2;

					pincel.drawBitmap(imagenes2[frames], x, y, null);

				}

			} else {

				if (!lado) {
					imagenes[0] = Texturas.jugador1D1;

					pincel.drawBitmap(imagenes[0], x, y, null);

				} else {

					imagenes[0] = Texturas.jugador1I1;

					pincel.drawBitmap(imagenes[0], x, y, null);
				}

			}

		}

	}

	@Override
	public void colision(Actor actor) {

		if (inmune) {

			if (actor instanceof BalaEnemigo || actor instanceof Volador || actor instanceof LanzaMisil
					|| actor instanceof Caja || actor instanceof BalaEnemigoDestruible || actor instanceof MaquinaFinal
					|| actor instanceof Misil || actor instanceof MaquinaPared || actor instanceof Saltador
					|| actor instanceof AntiAreo || actor instanceof Robot) {

				poder = false;

				satelite1.setActivar(false);

				satelite2.setActivar(false);

				velocidad = Jugador.VELOCIDAD_JUGADOR;

				cicloParpadeo = 100;

				vida--;

			}

		}

		if (actor instanceof Poder) {

			Poder p = (Poder) actor;

			pantalla.getJuego().getRecurso().playMusica("poder.wav", 1);

			if (p.getPoder() != Caja.AGILIDAD_S) {

				tipoPoder = p.getPoder();

				poder = true;
			}

			if (p.getPoder() == Caja.AGILIDAD_S) {

				velocidad += 1;

			}

		}

	}

	public int getVida() {
		return vida;
	}

	public void teclaPresionada(KeyEvent ev) {
		switch (ev.getKeyCode()) {

		case KeyEvent.KEYCODE_0:

			izquierda = true;
			dezplazamientoInicial = false;

			if (pausado) {

				lado = true;
			}

			break;

		case KeyEvent.KEYCODE_1:

			derecha = true;
			dezplazamientoInicial = false;

			if (pausado) {
				lado = false;
			}

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

			satelite1.setActivar(!satelite1.isActivar());

			satelite2.setActivar(!satelite2.isActivar());
			break;

		default:

			break;

		}

		actualizarVelocidad();

	}

	public void teclaLevantada(KeyEvent ev) {
		switch (ev.getKeyCode()) {

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

	public void toque(MotionEvent ev) {

		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:
			
				if (pausado) {

			xDeltaReferencia = x;

			disparar = true;

			satelite1.setDisparar(true);

			satelite2.setDisparar(true);

			x1 = x;

			y1 = y;

			x = x1;

			y = y1;

			deltaXTactil = (int) ev.getX() - x1;

			deltaYTactil = (int) ev.getY() - y1;

			if (x1 <= 32) {
				x1 = 32;

			}

			if (y1 >= ((Juego.ALTO_PANTALLA -32)- alto)) {

				y1 = (Juego.ALTO_PANTALLA-32) - alto;

			}
			if (x1 >= (Juego.ANCHO_PANTALLA -32) - ancho) {
				x1 = (Juego.ANCHO_PANTALLA  -32)- ancho;
			}

			if (y1 <= 32) {
				y1 = 32;

			}}

			break;

		case MotionEvent.ACTION_CANCEL:

			break;
		case MotionEvent.ACTION_UP:

			disparar = false;

			satelite1.setDisparar(false);

			satelite2.setDisparar(false);
			break;
		case MotionEvent.ACTION_MOVE:

				if (pausado) {
					
					xDelta = (int) ev.getX();

			if (xDelta >= xDeltaReferencia) {

				// Derecho

				lado = false;

			} else {

				// Izquierdo

				lado = true;

			}

			x1 = (int) ev.getX() - deltaXTactil;
			y1 = (int) ev.getY() - deltaYTactil;

			if (x1 <= 32) {

				x1 = 32;
			}
			if (y1 >= (Juego.ALTO_PANTALLA-32) - alto) {
				y1 = (Juego.ALTO_PANTALLA-32) - alto;
			}

			if (x1 >= (Juego.ANCHO_PANTALLA-32) - ancho) {
				x1 = (Juego.ANCHO_PANTALLA-32) - ancho;
			}
			if (y1 <= 32) {

				y1 = 32;
			}

			x = x1;

			y = y1;

			dezplazamientoInicial = false;

			}
			break;

		default:

			break;

		}

	}

	protected void actualizarVelocidad() {

		velocidadX = 0;

		velocidadY = 0;

		if (abajo) {
			velocidadY = velocidad;
		}

		if (arriba) {

			velocidadY = -velocidad;
		}

		if (izquierda) {
			velocidadX = -velocidad;

		}

		if (derecha) {
			velocidadX = velocidad;

		}

	}

	public void disparar() {

		if (cicloDisparo % 20 == 0) {

			if (!lado) {
				Bala bala = new Bala(pantalla);

				bala.setTamano(16, 4);

				bala.setPosicion(x + 56, y + 16);

				bala.setLado(true);

				bala.setImagenes(new Bitmap[] { Texturas.balaHD });

				pantalla.getActores().add(bala);

			} else {
				Bala bala = new Bala(pantalla);

				bala.setTamano(16, 4);

				bala.setPosicion(x, y + 16);

				bala.setLado(false);

				bala.setImagenes(new Bitmap[] { Texturas.balaHI });

				pantalla.getActores().add(bala);

			}

			cicloDisparo = 0;

			pantalla.getJuego().getRecurso().playMusica("disparo.wav", 1);

		}

	}

	public void disparoEspecial() {

		if (!lado) {

			if (cicloDisparo % 20 == 0) {

				if (tipoPoder == BalaEspecial.BALA_W) {

					BalaEspecial bala = new BalaEspecial(pantalla);

					bala.setLado(true);

					bala.setCuadros(5);

					bala.setBala(BalaEspecial.BALA_W);
					bala.setPosicion(x + 56, y - 16);

					bala.setTamano(16, 64);

					bala.setImagenes(new Bitmap[] {

							Texturas.balaWD1, Texturas.balaWD2,
							Texturas.balaWD3

					});

					pantalla.getActores().add(bala);

				}

				if (tipoPoder == BalaEspecial.BALA_L) {

					BalaEspecial bala = new BalaEspecial(pantalla);

					bala.setLado(true);

					bala.setCuadros(5);

					bala.setBala(BalaEspecial.BALA_L);

					bala.setTamano(256, 4);

					bala.setPosicion(x - 48, y + 16);

					bala.setImagenes(new Bitmap[] {

							Texturas.balaLD1, Texturas.balaLD2,
							Texturas.balaLD3

					});

					pantalla.getJuego().getRecurso().playMusica("disparoL.wav", 1);

					pantalla.getActores().add(bala);

				}

				cicloDisparo = 0;

			}

			if (cicloDisparoB % 30 == 0) {

				if (tipoPoder == BalaEspecial.BALA_B) {

					BalaEspecial bala = new BalaEspecial(pantalla);

					bala.setLado(true);

					bala.setCuadros(5);

					bala.setBala(BalaEspecial.BALA_B);

					bala.setTamano(12, 12);

					bala.setPosicion(x + 56, y + 12);

					bala.setImagenes(new Bitmap[] {

							Texturas.balaBD

					});

					pantalla.getJuego().getRecurso().playMusica("disparo.wav", 1);

					pantalla.getActores().add(bala);

				}

				cicloDisparoB = 0;

			}

		} else {

			if (cicloDisparo % 20 == 0) {

				if (tipoPoder == BalaEspecial.BALA_W) {

					BalaEspecial bala = new BalaEspecial(pantalla);

					bala.setLado(false);

					bala.setCuadros(5);

					bala.setBala(BalaEspecial.BALA_W);

					bala.setTamano(16, 64);

					bala.setPosicion(x, y - 16);

					bala.setImagenes(new Bitmap[] {

							Texturas.balaWI1, Texturas.balaWI2,
							Texturas.balaWI3

					});

					pantalla.getActores().add(bala);

				}

				if (tipoPoder == BalaEspecial.BALA_L) {

					BalaEspecial bala = new BalaEspecial(pantalla);

					bala.setLado(false);

					bala.setCuadros(5);

					bala.setBala(BalaEspecial.BALA_L);

					bala.setTamano(256, 4);

					bala.setPosicion(x - 144, y + 16);

					bala.setImagenes(new Bitmap[] {

							Texturas.balaLI1, Texturas.balaLI2,
							Texturas.balaLI3

					});

					pantalla.getJuego().getRecurso().playMusica("disparoL.wav", 1);

					pantalla.getActores().add(bala);

				}

				cicloDisparo = 0;

			}

			if (cicloDisparoB % 30 == 0) {

				if (tipoPoder == BalaEspecial.BALA_B) {

					BalaEspecial bala = new BalaEspecial(pantalla);

					bala.setLado(false);

					bala.setCuadros(5);

					bala.setBala(BalaEspecial.BALA_B);

					bala.setTamano(12, 12);

					bala.setPosicion(x, y + 12);

					bala.setImagenes(new Bitmap[] {

							Texturas.balaBI

					});

					pantalla.getJuego().getRecurso().playMusica("disparo.wav", 1);

					pantalla.getActores().add(bala);
				}

				cicloDisparoB = 0;

			}

		}

	}

}
