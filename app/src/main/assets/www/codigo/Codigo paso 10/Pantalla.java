package com.diamon.nucleo;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import com.diamon.juego.FinalMision;

public abstract class Pantalla implements ImageObserver {

	protected final FinalMision juego;

	protected ArrayList<Actor> actores = new ArrayList<Actor>();
	

	public Pantalla(FinalMision juego) {

		this.juego = juego;
		

	}

	public abstract void pausa();

	public abstract void resume();

	public abstract void actualizar(float delta);

	public abstract void dibujar(Graphics2D pincel, float delta);

	public abstract void colisiones();

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
		return (infoflags & (ALLBITS | ABORT)) == 0;
	}

	public abstract void ocultar();

	public abstract void mostrar();

	public abstract void reajustarPantalla(int ancho, int alto);

	public abstract void teclaPresionada(KeyEvent ev);

	public abstract void teclaLevantada(KeyEvent ev);

	public abstract void teclaTipo(KeyEvent ev);

}
