package com.diamon.pantalla;

import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class PantallaContinuar extends Pantalla {

	private Bitmap fondo;

	private Bitmap selector;

	private float posicionY;

	private boolean toque;

	public PantallaContinuar(Juego juego) {
		super(juego);

		fondo = this.crearBitmap(recurso.getImagen("continuar.png"), Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		selector = this.crearBitmap(recurso.getImagen("selector2.png"), 16, 16);

		posicionY = 288;

		toque = false;
	}

	@Override
	public void pausa() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void actualizar(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dibujar(Canvas pincel, float delta) {

		dibujarImagen(pincel, fondo, 0, 0);

		dibujarImagen(pincel, selector, 202, posicionY);

	}

	@Override
	public void colisiones() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ocultar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void mostrar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void teclaPresionada(KeyEvent ev) {

		switch (ev.getKeyCode()) {

		case KeyEvent.KEYCODE_0:

			if (posicionY == 288) {

				juego.setPantalla(new PantallaNivel(juego));
			}

			if (posicionY == 322) {

				juego.setPantalla(new PantallaMenu(juego));
			}

			break;

		case KeyEvent.KEYCODE_1:

			posicionY = 288;

			break;
		case KeyEvent.KEYCODE_2:

			posicionY = 322;

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
		// TODO Auto-generated method stub

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

			if (posicionY == 288) {
				juego.setPantalla(new PantallaNivel(juego));
			}

			break;
		case MotionEvent.ACTION_UP:

			toque = !toque;
			if (toque) {
				posicionY = 288;

			} else {

				posicionY = 322;
			}

			break;
		case MotionEvent.ACTION_POINTER_UP:

			if (posicionY == 322) {
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
