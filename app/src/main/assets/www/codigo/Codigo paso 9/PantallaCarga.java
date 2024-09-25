package com.diamon.pantalla;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.diamon.juego.FinalMision;
import com.diamon.nucleo.Pantalla;

public class PantallaCarga extends Pantalla {

	public PantallaCarga(FinalMision juego) {
		super(juego);
		// TODO Auto-generated constructor stub
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


	
		
		juego.getRecurso().cargarImagen("burbuja.png");
		
		juego.getRecurso().cargarSonido("explosion.wav");
		
		juego.setPantalla(new PantallaMenu(juego));


	}

	@Override
	public void dibujar(Graphics2D pincel, float delta) {
		// TODO Auto-generated method stub

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
	public void teclaTipo(KeyEvent ev) {
		// TODO Auto-generated method stub

	}

}
