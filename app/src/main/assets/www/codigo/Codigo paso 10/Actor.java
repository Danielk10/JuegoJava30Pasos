package com.diamon.nucleo;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public abstract class Actor implements ImageObserver {

	protected int x;

	protected int y;

	protected BufferedImage imagen;

	protected Dimension tamano;

	public Actor() {

		x = 0;
		y = 0;

		imagen = null;

		tamano = new Dimension(0, 0);

	}

	public void setPosicion(int x, int y) {
		this.x = x;
		this.y = y;

	}

	public void setImagen(BufferedImage imagen) {

		this.imagen = imagen;
	}

	public abstract void actualizar(float delta);

	public abstract void dibujar(Graphics2D pincel,float delta);

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

	public Rectangle getRectangulo() {
		return new Rectangle(x, y, tamano.width, tamano.height);

	}

	public void setTamano(int ancho, int alto) {

		tamano.setSize(ancho, alto);

	}

	public Dimension getTamano() {

		return tamano;
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
		return (infoflags & (ALLBITS | ABORT)) == 0;
	}

	public abstract void colision(Actor actor);

}
