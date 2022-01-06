package com.diamon.pantalla;

import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class PantallaFinDeJuego extends Pantalla {

	private float tiemoMovimiento;

	private Bitmap fondo;

	public PantallaFinDeJuego(Juego juego) {
		super(juego);

		fondo = this.crearBitmap(recurso.getImagen("finJuego.png"), Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		recurso.playMusica("finDeJuego.wav", 1);
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

		if (tiemoMovimiento / 5.66f >= 1) {

			recurso.pararSonido(recurso.getSonido("musica.wav"));

			juego.setPantalla(new PantallaContinuar(juego));

			tiemoMovimiento = 0;
		}

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
	public void teclaPresionada(KeyEvent ev) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
