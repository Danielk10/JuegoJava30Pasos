package com.diamon.pantalla;

import com.diamon.actor.Fondo;
import com.diamon.graficos.Pantalla2D;
import com.diamon.nucleo.Graficos;
import com.diamon.nucleo.Juego;

public class PantallaExtra extends Pantalla2D {

    public PantallaExtra(Juego juego) {
        super(juego);

        Fondo f = new Fondo(this, recurso.getTextura("jugador1D1.png"), 0, 0, 64, 64);
    }

    @Override
    public void mostrar() {}

    @Override
    public void resume() {}

    @Override
    public void colisiones() {}

    @Override
    public void actualizar(float delta) {}

    @Override
    public void dibujar(Graficos pincel, float delta) {
        // TODO Auto-generated method stub

    }

    @Override
    public void reajustarPantalla(int ancho, int alto) {}

    @Override
    public void pausa() {}

    @Override
    public void ocultar() {}

    @Override
    public void liberarRecursos() {}

    @Override
    public void teclaPresionada(int codigoDeTecla) {}

    @Override
    public void teclaLevantada(int codigoDeTecla) {}

    @Override
    public void toquePresionado(float x, float y, int puntero) {}

    @Override
    public void toqueLevantado(float x, float y, int puntero) {}

    @Override
    public void toqueDeslizando(float x, float y, int puntero) {}

    @Override
    public void acelerometro(float x, float y, float z) {}
}
