package com.diamon.actor;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.diamon.nucleo.Actor;

public class Jugador extends Actor {

	private int velocidadX;
	private int velocidadY;
	private final static int VELOCIDAD_JUGADOR = 5;
	private boolean arriba, abajo, izquierda, derecha;

	public Jugador() {

		velocidadX = 0;

		velocidadY = 0;

		arriba = abajo = izquierda = derecha = false;
	}

	@Override
	public void actualizar(float delta) {

		x += velocidadX;

		y += velocidadY;

	}

	@Override
	public void dibujar(Graphics2D pincel, float delta) {

		pincel.drawImage(imagen, x, y, tamano.width, tamano.height, this);

	}

	@Override
	public void colision(Actor actor) {
		// TODO Auto-generated method stub

	}

	public void teclaPresionada(KeyEvent ev) {
		switch (ev.getKeyCode()) {

		case KeyEvent.VK_LEFT:

			izquierda = true;

			break;

		case KeyEvent.VK_RIGHT:

			derecha = true;

			break;
		case KeyEvent.VK_UP:

			arriba = true;
			break;
		case KeyEvent.VK_DOWN:

			abajo = true;
			break;

		default:

			break;

		}

		actualizarVelocidad();

	}

	public void teclaLevantada(KeyEvent ev) {
		switch (ev.getKeyCode()) {

		case KeyEvent.VK_LEFT:

			izquierda = false;

			break;

		case KeyEvent.VK_RIGHT:

			derecha = false;

			break;
		case KeyEvent.VK_UP:

			arriba = false;
			break;
		case KeyEvent.VK_DOWN:

			abajo = false;
			break;

		default:

			break;

		}

		actualizarVelocidad();

	}

	public void teclaTipo(KeyEvent ev) {
		

	}

	protected void actualizarVelocidad() {

		velocidadX = 0;

		velocidadY = 0; 

		if (abajo) {
			velocidadY = VELOCIDAD_JUGADOR;
		}

		if (arriba) {

			velocidadY = -VELOCIDAD_JUGADOR;
		}

		if (izquierda) {
			velocidadX = -VELOCIDAD_JUGADOR;

		}

		if (derecha) {
			velocidadX = VELOCIDAD_JUGADOR;

		}

	}

}
