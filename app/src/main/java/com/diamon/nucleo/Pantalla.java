package com.diamon.nucleo;

import java.util.ArrayList;

import com.diamon.juego.FinalMision;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class Pantalla {

	protected final FinalMision juego;

	protected ArrayList<Actor> actores = new ArrayList<Actor>();

	public Pantalla(FinalMision juego) {

		this.juego = juego;

	}

	public abstract void pausa();

	public abstract void resume();

	public abstract void actualizar(float delta);

	public abstract void dibujar(Canvas pincel, float delta);

	public abstract void colisiones();

	public abstract void ocultar();

	public abstract void mostrar();

	public abstract void reajustarPantalla(int ancho, int alto);

	public abstract void teclaPresionada(KeyEvent ev);

	public abstract void teclaLevantada(KeyEvent ev);

	public abstract void toque(MotionEvent ev);

	public abstract void multiToque(MotionEvent ev);

	public abstract void acelerometro(SensorEvent ev);

	public void dibujarImagen(Canvas pincel, Bitmap imagen, int x, int y) {

		pincel.drawBitmap(imagen, x, y, null);

	}

	public Bitmap crearBitmap(Bitmap imagen, int ancho, int alto) {

		int w = imagen.getWidth();
		int h = imagen.getHeight();
		float sw = ((float) ancho) / w;
		float sh = ((float) alto) / h;
		Matrix max = new Matrix();
		max.postScale(sw, sh);
		return Bitmap.createBitmap(imagen, 0, 0, w, h, max, false);

	}

	public ArrayList<Actor> getActores() {
		return actores;
	}

	public FinalMision getJuego() {
		return juego;
	}

}
