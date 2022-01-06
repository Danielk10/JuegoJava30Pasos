package com.diamon.pantalla;

import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class PantallaMenu extends Pantalla {

	private Bitmap fondo;

	private Bitmap selector;

	private int posicionY;

	private float tiemoMovimiento;

	private boolean toque;

	public PantallaMenu(Juego juego) {
		super(juego);

		fondo = this.crearBitmap(recurso.getImagen("menu2.png"), Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		selector = this.crearBitmap(recurso.getImagen("selector1.png"), 16, 16);

		posicionY = 320;

		recurso.playMusica("menu.wav", 1);

		toque = false;

	}

	@Override
	public void pausa() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void actualizar(float delta) {

		tiemoMovimiento += delta;

		if (tiemoMovimiento / 5f >= 1) {

			juego.setPantalla(new PantallaIntroduccion(juego));

			tiemoMovimiento = 0;
		}

	}

	@Override
	public void dibujar(Canvas pincel, float delta) {

		dibujarImagen(pincel, fondo, 0, 0);

		dibujarImagen(pincel, selector, 186, posicionY);

	}

	@Override
	public void colisiones() {

	}

	@Override
	public void ocultar() {

	}

	@Override
	public void mostrar() {

	}

	@Override
	public void teclaPresionada(KeyEvent ev) {

		switch (ev.getKeyCode()) {

		case KeyEvent.KEYCODE_0:

			if (posicionY == 320) {
				juego.setPantalla(new PantallaNivel(juego));
			}

			if (posicionY == 354) {
				juego.setPantalla(new PantallaMenu(juego));
			}

			break;

		case KeyEvent.KEYCODE_1:

			posicionY = 320;

			break;
		case KeyEvent.KEYCODE_2:

			posicionY = 354;

			break;

		case KeyEvent.KEYCODE_3:

			juego.setPantalla(new PantallaAyuda(juego));

			break;

		default:

			break;

		}

	}

	@Override
	public void teclaLevantada(KeyEvent ev) {
		// TODO Auto-generated method stub

	}

	@Override
	public void toque(MotionEvent ev) {

		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:

			break;

		case MotionEvent.ACTION_CANCEL:

			break;
		case MotionEvent.ACTION_UP:

			break;
		case MotionEvent.ACTION_MOVE:

			break;

		default:

			break;

		}

	}

	@Override
	public void multiToque(MotionEvent ev) {

		int accion = ev.getAction() & MotionEvent.ACTION_MASK;

		int punteroIndice = (ev.getAction()
				& MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

		@SuppressWarnings("unused")
		int punteroID = ev.getPointerId(punteroIndice);

		switch (accion) {

		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_POINTER_DOWN:

			if (posicionY == 320) {
				juego.setPantalla(new PantallaNivel(juego));
			}

			break;
		case MotionEvent.ACTION_UP:

			toque = !toque;
			if (toque) {
				posicionY = 320;

			} else {

				posicionY = 354;
			}

			break;
		case MotionEvent.ACTION_POINTER_UP:

			if (posicionY == 354) {
				juego.setPantalla(new PantallaMenu(juego));
			}

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
	public void acelerometro(SensorEvent ev) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reajustarPantalla(float ancho, float alto) {
		// TODO Auto-generated method stub

	}

}
