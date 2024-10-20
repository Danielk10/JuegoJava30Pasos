package com.diamon.nucleo;

import com.diamon.actor.Jugador;
import com.diamon.dato.ConfiguracionesDeJuego;
import com.diamon.graficos.Pantalla2D;
import com.diamon.utilidad.Recurso;

import java.util.ArrayList;

public abstract class Mundo {

    protected Pantalla pantalla;

    protected Jugador jugador;

    protected Juego juego;

    protected ArrayList<Actor> actores;

    protected Recurso recurso;

    protected ConfiguracionesDeJuego configuracionesDeJuego;

    public Mundo(Pantalla pantalla, Jugador jugador) {

        this.pantalla = pantalla;

        this.juego = ((Pantalla2D) pantalla).getJuego();

        this.jugador = jugador;

        this.actores = ((Pantalla2D) pantalla).getActores();

        this.recurso = ((Pantalla2D) pantalla).getJuego().getRecurso();

        this.configuracionesDeJuego =
                ((Pantalla2D) pantalla).getJuego().getConfiguracionesDeJuego();

        iniciar();
    }

    protected abstract void iniciar();

    public abstract void actualizar(float delta);

    public abstract void dibujar(Graficos pincel, float delta);

    public abstract void guardarDatos();

    public abstract void liberarRecursos();
}
