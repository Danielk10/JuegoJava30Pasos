package com.diamon.nucleo;

import java.util.ArrayList;

import com.diamon.dato.Configuraciones;
import com.diamon.utilidad.Rectangulo;
import com.diamon.utilidad.Recurso;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class Actor {

	protected float x;

	protected float y;

	protected float ancho;

	protected float alto;

	protected boolean remover;

	private float cuadros;

	private float timpo;

	private int frames;

	protected Bitmap[] imagenes;

	protected Pantalla pantalla;

	protected Recurso recurso;

	protected Configuraciones configuracion;

	protected ArrayList<Actor> actores;

	public Camara2D camara;

	private boolean animar;

	public Actor(Pantalla pantalla) {

		this.pantalla = pantalla;

		this.recurso = pantalla.recurso;

		this.configuracion = pantalla.configuracion;

		actores = pantalla.actores;

		this.camara = pantalla.camara;

		x = 0;

		y = 0;

		timpo = 0;

		frames = 0;

		cuadros = 1;

		imagenes = null;

		ancho = 0;

		alto = 0;

		remover = false;

		animar = false;

	}

	public void setPosicion(float x, float y) {
		this.x = x;
		this.y = y;

	}

	public void setCuadros(float cuadros) {
		this.cuadros = cuadros;

		animar = true;
	}

	public void setImagenes(Bitmap[] imagenes) {

		this.imagenes = imagenes;

	}

	public boolean isRemover() {
		return remover;
	}

	public void actualizar(float delta) {

		if (animar) {

			if (delta == 0) {

				return;

			}

			if (delta > 0.1f) {

				delta = 0.1f;
			}

			timpo += delta;

			int frameNumber = (int) (timpo / (cuadros / Juego.FPS));

			frames = frameNumber % imagenes.length;

			/*
			 * timpo++;
			 * 
			 * if (timpo % cuadros == 0) { timpo = 0; frames = (frames + 1) %
			 * imagenes.length;
			 * 
			 * }
			 */

		}

	}

	public void dibujar(Canvas pincel, float delta) {

		pincel.drawBitmap(imagenes[frames], x, y, null);

	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Rectangulo getRectangulo() {
		return new Rectangulo(x, y, ancho, alto);

	}

	public float getAncho() {
		return ancho;
	}

	public void setAncho(float ancho) {
		this.ancho = ancho;
	}

	public float getAlto() {
		return alto;
	}

	public void setAlto(float alto) {
		this.alto = alto;
	}

	public void setTamano(float ancho, float alto) {

		this.ancho = ancho;

		this.alto = alto;

	}

	public abstract void colision(Actor actor);

	public void remover() {

		remover = true;
	}

}
