package com.diamon.nivel;

import com.diamon.actor.Jugador;
import com.diamon.nucleo.Nivel;
import com.diamon.nucleo.Pantalla;

import android.graphics.Canvas;

public class Niveles extends Nivel {

	public Niveles(Pantalla pantalla, Jugador jugador) {
		super(pantalla, jugador);

	}

	@Override
	public void iniciar() {

	}

	@Override
	public void actualizar(float delta) {

	}

	@Override
	public void guardarDatos() {

		configuracion.guardarConfiguraciones();

	}

	@Override
	public void liberarRecursos() {

		actores.clear();

	}

	@Override
	public void dibujar(Canvas pincel, float delta) {

	}

}
