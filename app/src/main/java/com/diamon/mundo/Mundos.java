package com.diamon.mundo;

import com.diamon.actor.Jugador;
import com.diamon.nucleo.Graficos;
import com.diamon.nucleo.Mundo;
import com.diamon.nucleo.Pantalla;

public class Mundos extends Mundo {

    public Mundos(Pantalla pantalla, Jugador jugador) {
        super(pantalla, jugador);
    }

    @Override
    protected void iniciar() {}

    @Override
    public void actualizar(float delta) {}

    @Override
    public void dibujar(Graficos pincel, float delta) {}

    @Override
    public void guardarDatos() {}

    @Override
    public void liberarRecursos() {}
}
