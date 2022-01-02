package com.diamon.pantalla;

import com.diamon.juego.FinalMision;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class PantallaIntroduccion extends Pantalla {

	private int ciclo;

	private Bitmap fondo1;

	private Bitmap fondo2;

	private int x;

	public PantallaIntroduccion(FinalMision juego) {
		super(juego);

		fondo1 = this.crearBitmap(juego.getRecurso().getImagen("fondoIntroduccion3.png"), Juego.ANCHO_PANTALLA,
				Juego.ALTO_PANTALLA);
		fondo2 = this.crearBitmap(juego.getRecurso().getImagen("fondoIntroduccion3.png"), Juego.ANCHO_PANTALLA,
				Juego.ALTO_PANTALLA);

		ciclo = 0;

		juego.getRecurso().playMusica("introduccion.wav", 1);

		x = 0;
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

		ciclo++;

		if (ciclo % 6330 == 0) {

			juego.setPantalla(new PantallaMenu(juego));

			ciclo = 0;
		}

		x--;

		if (x <= -Juego.ANCHO_PANTALLA) {

			x = 0;
		}

	}

	@Override
	public void dibujar(Canvas pincel, float delta) {

		dibujarImagen(pincel, fondo1, x, 0);

		dibujarImagen(pincel, fondo2, x + Juego.ANCHO_PANTALLA, 0);

	}

	@Override
	public void colisiones() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ocultar() {

		juego.getRecurso().pararMusica(juego.getRecurso().getMusica("introduccion.wav"));

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

			juego.setPantalla(new PantallaMenu(juego));

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

			juego.setPantalla(new PantallaMenu(juego));

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
