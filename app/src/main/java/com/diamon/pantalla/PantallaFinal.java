package com.diamon.pantalla;

import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class PantallaFinal extends Pantalla {

	private Bitmap fondo;

	private Bitmap creditos;

	private Bitmap menuFinal;

	private Bitmap selector;

	private float posicionY;

	private boolean cambio;

	private boolean cambio2;

	private float tiemoMovimiento;

	private boolean toque;

	public PantallaFinal(Juego juego) {
		super(juego);

		fondo = this.crearBitmap(recurso.getImagen("finNivel.png"), Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		creditos = this.crearBitmap(recurso.getImagen("creditos.png"), Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		menuFinal = this.crearBitmap(recurso.getImagen("menuFinal.png"), Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		selector = this.crearBitmap(recurso.getImagen("selector2.png"), 16, 16);

		posicionY = 288;

		cambio = false;

		cambio2 = false;

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

		tiemoMovimiento += delta;

		if (tiemoMovimiento / 5 >= 1) {

			cambio = true;

		}

		if (tiemoMovimiento / 33.33f >= 1) {

			cambio2 = true;

			tiemoMovimiento = 0;
		}

	}

	@Override
	public void dibujar(Canvas pincel, float delta) {

		if (cambio) {
			dibujarImagen(pincel, creditos, 0, 0);

		} else {

			dibujarImagen(pincel, fondo, 0, 0);

		}

		if (cambio2) {

			dibujarImagen(pincel, menuFinal, 0, 0);

			dibujarImagen(pincel, selector, 202, posicionY);

		}

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

			if (cambio2) {
				if (posicionY == 288) {

					juego.setPantalla(new PantallaMenu(juego));
				}

				if (posicionY == 322) {

					juego.setPantalla(new PantallaExtra(juego));
				}

			}

			break;

		case KeyEvent.KEYCODE_1:

			posicionY = 288;

			break;
		case KeyEvent.KEYCODE_3:

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

			if (cambio2) {
				if (posicionY == 288) {
					juego.setPantalla(new PantallaExtra(juego));
				}
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

			if (cambio2) {
				if (posicionY == 322) {
					juego.setPantalla(new PantallaMenu(juego));
				}
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
