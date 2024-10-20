package com.diamon.pantalla;

import android.view.KeyEvent;

import com.diamon.graficos.Pantalla2D;
import com.diamon.graficos.Textura2D;
import com.diamon.nucleo.Graficos;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Textura;

public class PantallaFinal extends Pantalla2D {

    private Textura fondo;

    private Textura creditos;

    private Textura menuFinal;

    private Textura selector;

    private float posicionY;

    private boolean cambio;

    private boolean cambio2;

    private float tiemoMovimiento;

    private boolean toque;

    public PantallaFinal(Juego juego) {
        super(juego);

        fondo =
                new Textura2D(
                        recurso.getTextura("finNivel.png").getBipmap(),
                        Juego.ANCHO_PANTALLA,
                        Juego.ALTO_PANTALLA);

        creditos =
                new Textura2D(
                        recurso.getTextura("creditos.png").getBipmap(),
                        Juego.ANCHO_PANTALLA,
                        Juego.ALTO_PANTALLA);

        menuFinal =
                new Textura2D(
                        recurso.getTextura("menuFinal.png").getBipmap(),
                        Juego.ANCHO_PANTALLA,
                        Juego.ALTO_PANTALLA);

        selector = new Textura2D(recurso.getTextura("selector2.png").getBipmap(), 16, 16);

        posicionY = 288;

        cambio = false;

        cambio2 = false;

        toque = false;
    }

    @Override
    public void mostrar() {}

    @Override
    public void resume() {}

    @Override
    public void colisiones() {}

    @Override
    public void actualizar(float delta) {

        tiemoMovimiento += delta;

        if (tiemoMovimiento / 5 >= 1) {

            cambio = true;
        }

        if (tiemoMovimiento / 10f >= 1) {

            cambio2 = true;

            tiemoMovimiento = 0;
        }
    }

    @Override
    public void dibujar(Graficos pincel, float delta) {

        if (cambio) {
            pincel.dibujarTextura(creditos, 0, 0);

        } else {

            pincel.dibujarTextura(fondo, 0, 0);
        }

        if (cambio2) {

            pincel.dibujarTextura(menuFinal, 0, 0);

            pincel.dibujarTextura(selector, 202, posicionY);
        }
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
    public void teclaPresionada(int codigoDeTecla) {

        switch (codigoDeTecla) {
            case KeyEvent.KEYCODE_0:
                if (cambio2) {
                    if (posicionY == 288) {

                        juego.setPantalla(new PantallaMenu(juego));
                    }

                    if (posicionY == 322) {

                        juego.setPantalla(new PantallaExtra(juego));
                    }
                }

                break;

            case KeyEvent.KEYCODE_1:
                posicionY = 288;

                break;
            case KeyEvent.KEYCODE_3:
                posicionY = 322;

                break;

            default:
                break;
        }
    }

    @Override
    public void teclaLevantada(int codigoDeTecla) {}

    @Override
    public void toquePresionado(float x, float y, int puntero) {

        if (puntero == 1) {

            if (cambio2) {
                if (posicionY == 288) {
                    juego.setPantalla(new PantallaExtra(juego));
                }
            }
        }
    }

    @Override
    public void toqueLevantado(float x, float y, int puntero) {

        if (puntero == 0) {
            toque = !toque;
            if (toque) {
                posicionY = 288;

            } else {

                posicionY = 322;
            }
        }
        if (puntero == 1) {
            if (cambio2) {
                if (posicionY == 322) {
                    juego.setPantalla(new PantallaMenu(juego));
                }
            }
        }
    }

    @Override
    public void toqueDeslizando(float x, float y, int puntero) {}

    @Override
    public void acelerometro(float x, float y, float z) {}
}
