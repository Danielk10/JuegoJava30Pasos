package com.diamon.nucleo;

import com.diamon.utilidad.Rectangulo;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class Actor {

	protected int x;

	protected int y;

	protected int ancho;

	protected int alto;

	protected boolean remover;

	private int cuadros;

	private int timpo;

	private int frames;

	protected Bitmap[] imagenes;

	protected Pantalla pantalla;

	private boolean animar;

	public Actor(Pantalla pantalla) {

		this.pantalla = pantalla;

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

	public void setPosicion(int x, int y) {
		this.x = x;
		this.y = y;

	}

	public void setCuadros(int cuadros) {
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
			timpo++;

			if (timpo % cuadros == 0) {
				timpo = 0;
				frames = (frames + 1) % imagenes.length;

			}

		}

	}

	public void dibujar(Canvas pincel, float delta) {

		pincel.drawBitmap(imagenes[frames], x, y, null);

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Rectangulo getRectangulo() {
		return new Rectangulo(x, y, ancho, alto);

	}

	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public void setTamano(int ancho, int alto) {

		this.ancho = ancho;

		this.alto = alto;

	}

	public abstract void colision(Actor actor);

	public void remover() {

		remover = true;
	}

}
