package com.diamon.nucleo;

import java.util.ArrayList;

import com.diamon.actor.Jugador;
import com.diamon.dato.Configuraciones;
import com.diamon.utilidad.Recurso;

import android.graphics.Canvas;

public abstract class Nivel {

	protected Juego juego;

	protected Pantalla pantalla;

	protected ArrayList<Actor> actores;

	protected Recurso recurso;

	protected Configuraciones configuracion;

	protected Jugador jugador;

	protected Camara2D camara;

	public Nivel(Pantalla pantalla, Jugador jugador) {

		this.juego = pantalla.juego;

		this.pantalla = pantalla;

		this.recurso = pantalla.recurso;

		this.configuracion = pantalla.configuracion;

		this.actores = pantalla.actores;

		this.jugador = jugador;

		this.camara = pantalla.camara;

		iniciar();
	}

	protected abstract void iniciar();

	public abstract void actualizar(float delta);

	public abstract void dibujar(Canvas pincel, float delta);

	public abstract void guardarDatos();

	public abstract void liberarRecursos();

}
