package com.diamon.graficos;

import java.util.ArrayList;

import com.diamon.dato.ConfiguracionesDeJuego;
import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.utilidad.Recurso;

public abstract class Pantalla2D implements Pantalla {

	protected final Juego juego;

	protected ArrayList<Actor> actores;

	protected ConfiguracionesDeJuego configuracionesDeJuego;

	protected Recurso recurso;

	public Pantalla2D(Juego juego) {

		this.juego = juego;

		configuracionesDeJuego = juego.getConfiguracionesDeJuego();

		actores = new ArrayList<Actor>();

		recurso = juego.getRecurso();

	}

	public Juego getJuego() {
		return juego;
	}

	public ArrayList<Actor> getActores() {
		return actores;
	}

}
