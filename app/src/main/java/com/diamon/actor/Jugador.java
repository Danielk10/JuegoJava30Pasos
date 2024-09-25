package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.utilidad.Texturas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;


public class Jugador extends Actor {

	private float deltaXTactil, deltaYTactil;

	private float xDelta = 0;

	private float xDeltaReferencia = 0;

	private float x1 = 0;

	private float y1 = 0;

	private float velocidadX;

	private float velocidadY;

	private final static float VELOCIDAD_JUGADOR = 5;

	private boolean arriba, abajo, izquierda, derecha, disparar;

	private float tiemoDisparo;

	private float tiemoDisparoB;

	private float tiempoParpadeo;

	private float cuadros = 7;

	private float timpo = 0;;

	protected int frames = 0;

	private Bitmap[] imagenes1 = new Bitmap[2];

	private Bitmap[] imagenes2 = new Bitmap[2];

	private boolean dezplazamientoInicial;

	private boolean parpadeo;

	private boolean lado;

	private int vida;

	private boolean inmune;

	private boolean poder;

	private int tipoPoder;

	private float velocidad;

	private Satelite satelite1;

	private Satelite satelite2;

	public Jugador(Pantalla pantalla) {
		super(pantalla);

		velocidadX = 0;

		velocidadY = 0;

		deltaXTactil = deltaYTactil = 0;

		dezplazamientoInicial = true;

		arriba = abajo = izquierda = derecha = disparar = false;

		parpadeo = false;

		velocidad = VELOCIDAD_JUGADOR;

		lado = false;

		poder = false;

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

		actores.add(satelite1);

		actores.add(satelite2);

	}

	public float getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(float velocidad) {
		this.velocidad = velocidad;
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

		x += velocidadX / Juego.DELTA_A_PIXEL * delta;

		y += velocidadY / Juego.DELTA_A_PIXEL * delta;

		timpo += delta;

		if (!lado) {

			int frameNumber = (int) (timpo / (cuadros / Juego.FPS));

			frames = frameNumber % imagenes1.length;

		}

		else {

			int frameNumber = (int) (timpo / (cuadros / Juego.FPS));

			frames = frameNumber % imagenes2.length;

		}

		if (dezplazamientoInicial) {

			if (x <= (Juego.ANCHO_PANTALLA / 2) - ancho) {

				x += 3 / Juego.DELTA_A_PIXEL * delta;

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

		tiemoDisparo += delta;

		tiemoDisparoB += delta;

		if (disparar) {

			if (!poder) {

				disparar();
			}

			if (poder) {
				disparoEspecial();

			}

		}

		if (tiempoParpadeo % 2 == 0) {

			parpadeo = true;

		} else {

			parpadeo = false;
		}

		if (tiempoParpadeo <= 0) {

			tiempoParpadeo = 0;

			inmune = true;
		} else {
			inmune = false;
		}

		tiempoParpadeo -= 1;

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

				tiempoParpadeo = 100;

				vida--;

			}

		}

		if (actor instanceof Poder) {

			Poder p = (Poder) actor;

			recurso.playMusica("poder.wav", 1);

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

			lado = true;

			break;

		case KeyEvent.KEYCODE_1:

			derecha = true;
			dezplazamientoInicial = false;

			lado = false;

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

			if (y1 >= ((Juego.ALTO_PANTALLA - 32) - alto)) {

				y1 = (Juego.ALTO_PANTALLA - 32) - alto;

			}
			if (x1 >= (Juego.ANCHO_PANTALLA - 32) - ancho) {
				x1 = (Juego.ANCHO_PANTALLA - 32) - ancho;
			}

			if (y1 <= 32) {
				y1 = 32;

			}

			break;

		case MotionEvent.ACTION_CANCEL:

			break;
		case MotionEvent.ACTION_UP:

			disparar = false;

			satelite1.setDisparar(false);

			satelite2.setDisparar(false);
			break;
		case MotionEvent.ACTION_MOVE:

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
			if (y1 >= (Juego.ALTO_PANTALLA - 32) - alto) {
				y1 = (Juego.ALTO_PANTALLA - 32) - alto;
			}

			if (x1 >= (Juego.ANCHO_PANTALLA - 32) - ancho) {
				x1 = (Juego.ANCHO_PANTALLA - 32) - ancho;
			}
			if (y1 <= 32) {

				y1 = 32;
			}

			x = x1;

			y = y1;

			dezplazamientoInicial = false;

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

		if (tiemoDisparo / 0.33f >= 1) {

			if (!lado) {
				Bala bala = new Bala(pantalla);

				bala.setTamano(16, 4);

				bala.setPosicion(x + 56, y + 16);

				bala.setLado(true);

				bala.setImagenes(new Bitmap[] { Texturas.balaHD });

				actores.add(bala);

			} else {
				Bala bala = new Bala(pantalla);

				bala.setTamano(16, 4);

				bala.setPosicion(x, y + 16);

				bala.setLado(false);

				bala.setImagenes(new Bitmap[] { Texturas.balaHI });

				actores.add(bala);

			}

			tiemoDisparo = 0;

			recurso.playMusica("disparo.wav", 1);
		}

	}

	public void disparoEspecial() {

		if (!lado) {

			if (tiemoDisparo / 0.33f >= 1) {

				if (tipoPoder == BalaEspecial.BALA_W) {

					BalaEspecial bala = new BalaEspecial(pantalla);

					bala.setLado(true);

					bala.setCuadros(5);

					bala.setBala(BalaEspecial.BALA_W);
					bala.setPosicion(x + 56, y - 16);

					bala.setTamano(16, 64);

					bala.setImagenes(new Bitmap[] {

							Texturas.balaWD1, Texturas.balaWD2, Texturas.balaWD3

					});

					actores.add(bala);

				}

				if (tipoPoder == BalaEspecial.BALA_L) {

					BalaEspecial bala = new BalaEspecial(pantalla);

					bala.setLado(true);

					bala.setCuadros(5);

					bala.setBala(BalaEspecial.BALA_L);

					bala.setTamano(256, 4);

					bala.setPosicion(x - 48, y + 16);

					bala.setImagenes(new Bitmap[] {

							Texturas.balaLD1, Texturas.balaLD2, Texturas.balaLD3

					});

					recurso.playMusica("disparoL.wav", 1);

					actores.add(bala);

				}

				tiemoDisparo = 0;

			}

			if (tiemoDisparoB / 0.5f >= 1) {

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

					recurso.playMusica("disparo.wav", 1);

					actores.add(bala);

				}

				tiemoDisparoB = 0;
			}

		} else {

			if (tiemoDisparo / 0.33f >= 1) {

				if (tipoPoder == BalaEspecial.BALA_W) {

					BalaEspecial bala = new BalaEspecial(pantalla);

					bala.setLado(false);

					bala.setCuadros(5);

					bala.setBala(BalaEspecial.BALA_W);

					bala.setTamano(16, 64);

					bala.setPosicion(x, y - 16);

					bala.setImagenes(new Bitmap[] {

							Texturas.balaWI1, Texturas.balaWI2, Texturas.balaWI3

					});

					actores.add(bala);

				}

				if (tipoPoder == BalaEspecial.BALA_L) {

					BalaEspecial bala = new BalaEspecial(pantalla);

					bala.setLado(false);

					bala.setCuadros(5);

					bala.setBala(BalaEspecial.BALA_L);

					bala.setTamano(256, 4);

					bala.setPosicion(x - 144, y + 16);

					bala.setImagenes(new Bitmap[] {

							Texturas.balaLI1, Texturas.balaLI2, Texturas.balaLI3

					});

					recurso.playMusica("disparoL.wav", 1);

					actores.add(bala);

				}

				tiemoDisparo = 0;

			}

			if (tiemoDisparoB / 0.5f >= 1) {

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

					recurso.playMusica("disparo.wav", 1);

					actores.add(bala);
				}

				tiemoDisparoB = 0;

			}

		}

	}

}
