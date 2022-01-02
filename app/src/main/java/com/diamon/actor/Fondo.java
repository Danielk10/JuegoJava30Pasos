package com.diamon.actor;

import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;

public class Fondo extends Actor {

	public final static int VELOCIDAD_FONDO = 1;

	public final static int HORIZONTAL_IZQUIERDA = 1;

	public final static int HORIZONTAL_DERECHA = 2;

	public final static int VERTICAL_ABAJO = 3;

	public final static int VERTICAL_ARRIBA = 4;

	private int direccion;

	private boolean parar;

	private int velocidad;

	private int  nombre;

	public Fondo(Pantalla pantalla) {

		super(pantalla);

		direccion = Fondo.HORIZONTAL_IZQUIERDA;

		parar = false;

		velocidad = VELOCIDAD_FONDO;

		nombre = 0;

	}

	public int getVelocidad() {
		return velocidad;
	}

	

	public int getNombre() {
		return nombre;
	}

	public boolean isParar() {
		return parar;
	}

	public void setNombre(int nombre) {
		this.nombre = nombre;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public void setParar(boolean parar) {
		this.parar = parar;
	}

	@Override
	public void actualizar(float delta) {

		super.actualizar(delta);

		if (direccion == Fondo.HORIZONTAL_IZQUIERDA) {

			if (!parar) {
				x -= velocidad;

			}

			if (x <= -Juego.ANCHO_PANTALLA) {

				remover = true;

			}

		}

		if (direccion == Fondo.VERTICAL_ABAJO) {

			if (!parar) {

				y += velocidad;
			}

			if (y >= Juego.ALTO_PANTALLA) {

				remover = true;

			}

		}

		if (direccion == Fondo.VERTICAL_ARRIBA) {

			if (!parar) {
				y -= velocidad;

			}

			if (y <= -Juego.ALTO_PANTALLA) {

				remover = true;

			}
		}

		if (direccion == Fondo.HORIZONTAL_DERECHA) {

			if (!parar) {
				x += velocidad;

			}

			if (x >= Juego.ANCHO_PANTALLA) {

				remover = true;

			}

		}

	}

	public void setDireccion(int direccion) {
		this.direccion = direccion;
	}

	@Override
	public void colision(Actor actor) {

	}

}
