package com.diamon.pantalla;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.diamon.juego.FinalMision;
import com.diamon.nucleo.Pantalla;
import com.diamon.utilidad.Recurso;

public class PantallaMenu extends Pantalla {
	
	private Recurso recurso;

	public PantallaMenu(FinalMision juego) {
		super(juego);
		
		recurso = new Recurso();
		
		recurso.playSonido("explosion.wav");
		
		
	}

	@Override
	public void pausa() {
		

	}

	@Override
	public void resume() { 
		

	}

	@Override
	public void actualizar(float delta) {
		

	}

	@Override
	public void dibujar(Graphics2D pincel, float delta) {
		
		
		pincel.setColor(Color.GREEN);
		
		pincel.fillRect(20, 20, 100, 100);
		

	}

	@Override
	public void colisiones() {
		

	}

	@Override
	public void ocultar() {
		

	}

	@Override
	public void mostrar() {
		

	}

	@Override
	public void reajustarPantalla(int ancho, int alto) {
		

	}

	@Override
	public void teclaPresionada(KeyEvent ev) {
		
		switch (ev.getKeyCode()) {

		case KeyEvent.VK_D:
			
			juego.setPantalla(new PantallaJuego(juego));

			break;

		default:

			break;

		}
		

	}

	@Override
	public void teclaLevantada(KeyEvent ev) {
		

	}

	@Override
	public void teclaTipo(KeyEvent ev) {
		

	}

}
