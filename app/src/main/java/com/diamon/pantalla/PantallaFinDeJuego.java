package com.diamon.pantalla;

import com.diamon.graficos.Pantalla2D;
import com.diamon.graficos.Textura2D;
import com.diamon.nucleo.Graficos;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Musica;

public class PantallaFinDeJuego extends Pantalla2D {

    private float tiempo;

    private Musica musica;

    private Textura2D textura;

    public PantallaFinDeJuego(Juego juego) {
        super(juego);

        tiempo = 0;

        textura =
                new Textura2D(
                        juego.getRecurso().getTextura("finJuego.png").getBipmap(),
                        Juego.ANCHO_PANTALLA,
                        Juego.ALTO_PANTALLA);

        musica = juego.getRecurso().getMusica("finDeJuego.wav");

        musica.reproducir();
    }

    @Override
    public void mostrar() {}

    @Override
    public void resume() {}

    @Override
    public void colisiones() {}

    @Override
    public void actualizar(float delta) {

        tiempo += delta;

        if (tiempo / 5.66f >= 1) {

            musica.terminar();

            juego.setPantalla(new PantallaContinuar(juego));

            tiempo = 0;
        }
    }

    @Override
    public void dibujar(Graficos pincel, float delta) {

        pincel.dibujarTextura(textura, 0, 0);
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
