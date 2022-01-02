package com.diamon.pantalla;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.diamon.actor.Jugador;
import com.diamon.juego.FinalMision;
import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Pantalla;

public class PantallaJuego extends Pantalla {

	private Jugador bola;

	public PantallaJuego(FinalMision juego) {
		super(juego);

	

		bola = new Jugador();

		bola.setTamano(32, 32);

		bola.setPosicion(200, 200);

		bola.setImagen(juego.getRecurso().getImagen("burbuja.png"));

		actores.add(bola);

	}

	@Override
	public void pausa() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void actualizar(float delta) {
		
		for(Actor actor: actores)
		{
			
		   actor.actualizar(delta); 
		}
		
	}

	@Override
	public void dibujar(Graphics2D pincel, float delta) {

		for (Actor actor : actores) {
			

			actor.dibujar(pincel, delta);
		}
	}

	@Override
	public void colisiones() {

	}

	@Override
	public void ocultar() {
		juego.getConfiguracioin().guardarConfiguraciones(juego.getDatos());
	}

	@Override
	public void mostrar() {

	}

	@Override
	public void reajustarPantalla(int ancho, int alto) {

	}

	@Override
	public void teclaPresionada(KeyEvent ev) {

		for (Actor actor : actores) {
			Jugador a = (Jugador) actor;

			a.teclaPresionada(ev);
		}
	}

	@Override
	public void teclaLevantada(KeyEvent ev) {

		for (Actor actor : actores) {
			Jugador a = (Jugador) actor;

			a.teclaLevantada(ev);
		}

	}

	@Override
	public void teclaTipo(KeyEvent ev) {

	}

}
