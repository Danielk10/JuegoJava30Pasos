package com.diamon.pantalla;


import com.diamon.juego.FinalMision;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class PantallaNivel extends Pantalla {

	private int ciclo;

	private Bitmap fondo;

	public PantallaNivel(FinalMision juego) {
		super(juego);

		ciclo = 0;

		
		fondo = this.crearBitmap(juego.getRecurso().getImagen("nivel1.png"), Juego.ANCHO_PANTALLA,
				Juego.ALTO_PANTALLA);

		juego.getRecurso().playMusica("precentacion1.wav",1);
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

		if (ciclo % 100 == 0) {

			juego.getRecurso().pararMusica(juego.getRecurso().getMusica("precentacion1.wav"));

			juego.setPantalla(new PantallaJuego(juego));

			ciclo = 0;

		}

	}

	@Override
	public void dibujar(Canvas pincel, float delta) {

		dibujarImagen(pincel,fondo, 0, 0); 

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



}
