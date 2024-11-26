package com.diamon.pantalla;

import android.graphics.Color;
import com.diamon.graficos.Pantalla2D;
import com.diamon.graficos.Textura2D;
import com.diamon.nucleo.Graficos;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Musica;

public class PantallaIntroduccion extends Pantalla2D {

    private Musica musica;

    private float tiempo;

    private Textura2D textura1;

    private Textura2D textura2;

    private float x;

    public PantallaIntroduccion(Juego juego) {
        super(juego);

        tiempo = 0;

        textura1 =
                new Textura2D(
                        juego.getRecurso().getTextura("fondoIntroduccion3.png").getBipmap(),
                        Juego.ANCHO_PANTALLA,
                        Juego.ALTO_PANTALLA);

        textura2 =
                new Textura2D(
                        juego.getRecurso().getTextura("fondoIntroduccion3.png").getBipmap(),
                        Juego.ANCHO_PANTALLA,
                        Juego.ALTO_PANTALLA);

        musica = juego.getRecurso().getMusica("introduccion.wav");

        musica.reproducir();

        x = 0;
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

        if (tiempo / 108f >= 1) {

            juego.setPantalla(new PantallaMenu(juego));

            tiempo = 0;
        }

        x--;

        if (x <= -Juego.ANCHO_PANTALLA) {

            x = 0;
        }
    }

    @Override
    public void dibujar(Graficos pincel, float delta) {

        pincel.dibujarTextura(textura1, x, 0);

        pincel.dibujarTextura(textura2, x + Juego.ANCHO_PANTALLA, 0);
        
        pincel.dibujarTexto("Para ver el cambio, esperar que termine la musica",(Juego.ANCHO_PANTALLA/2)/2, Juego.ALTO_PANTALLA-32,Color.BLUE);
    }

    @Override
    public void reajustarPantalla(int ancho, int alto) {}

    @Override
    public void pausa() {}

    @Override
    public void ocultar() {

        musica.terminar();

        
    }

    @Override
    public void liberarRecursos() {

        musica.terminar();

        musica.liberarRecurso();
    }

    @Override
    public void teclaPresionada(int codigoDeTecla) {}

    @Override
    public void teclaLevantada(int codigoDeTecla) {}

    @Override
    public void toquePresionado(float x, float y, int puntero) {

        juego.setPantalla(new PantallaMenu(juego));
    }

    @Override
    public void toqueLevantado(float x, float y, int puntero) {

        juego.setPantalla(new PantallaMenu(juego));
    }

    @Override
    public void toqueDeslizando(float x, float y, int puntero) {}

    @Override
    public void acelerometro(float x, float y, float z) {}
}
