package com.diamon.nucleo;

import java.util.ArrayList;

import com.diamon.dato.Configuraciones;
import com.diamon.utilidad.Recurso;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class Pantalla {

	protected final Juego juego;

	protected ArrayList<Actor> actores;

	protected Recurso recurso;

	protected Configuraciones configuracion;

	protected Camara2D camara;

	public Pantalla(Juego juego) {

		this.juego = juego;

		actores = new ArrayList<Actor>();

		this.recurso = juego.recurso;

		this.configuracion = juego.configuracion;

		this.camara = juego.camara;

	}

	public abstract void pausa();

	public abstract void resume();

	public abstract void actualizar(float delta);

	public abstract void dibujar(Canvas pincel, float delta);

	public abstract void colisiones();

	public abstract void ocultar();

	public abstract void mostrar();

	public abstract void reajustarPantalla(float ancho, float alto);

	public abstract void teclaPresionada(KeyEvent ev);

	public abstract void teclaLevantada(KeyEvent ev);

	public abstract void toque(MotionEvent ev);

	public abstract void multiToque(MotionEvent ev);

	public abstract void acelerometro(SensorEvent ev);

	public void dibujarImagen(Canvas pincel, Bitmap imagen, float x, float y) {

		pincel.drawBitmap(imagen, x, y, null);

	}

	public Bitmap crearBitmap(Bitmap imagen, float ancho, float alto) {

		int w = imagen.getWidth();
		int h = imagen.getHeight();
		float sw = ancho / w;
		float sh = alto / h;
		Matrix max = new Matrix();
		max.postScale(sw, sh);
		return Bitmap.createBitmap(imagen, 0, 0, w, h, max, false);

	}

}
