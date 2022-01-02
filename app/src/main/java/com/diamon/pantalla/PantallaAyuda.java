package com.diamon.pantalla;

import com.diamon.juego.FinalMision;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class PantallaAyuda extends Pantalla {

	private Bitmap fondo;

	public PantallaAyuda(FinalMision juego) {
		super(juego);

		fondo = this.crearBitmap(juego.getRecurso().getImagen("ayuda.png"), Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);
	
		
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
	public void reajustarPantalla(int ancho, int alto) {
		// TODO Auto-generated method stub

	}

	@Override
	public void teclaPresionada(KeyEvent ev) {

		switch (ev.getKeyCode()) {

		case KeyEvent.KEYCODE_0:

			juego.setPantalla(new PantallaMenu(juego));

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

	@SuppressWarnings("unused")
	@Override
	public void multiToque(MotionEvent ev) {

		int accion = ev.getAction() & MotionEvent.ACTION_MASK;

		int punteroIndice = (ev.getAction()
				& MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

		int punteroID = ev.getPointerId(punteroIndice);

		switch (accion) {

		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_POINTER_DOWN:

			break;
		case MotionEvent.ACTION_UP:

				juego.setPantalla(new PantallaCarga(juego));

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
	public void acelerometro(SensorEvent ev) {
		// TODO Auto-generated method stub

	}

}