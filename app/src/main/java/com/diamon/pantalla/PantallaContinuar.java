package com.diamon.pantalla;

import android.view.KeyEvent;

import com.diamon.graficos.Pantalla2D;
import com.diamon.graficos.Textura2D;
import com.diamon.nucleo.Graficos;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Textura;

public class PantallaContinuar extends Pantalla2D {

    private Textura fondo;

    private Textura selector;

    private float posicionY;

    private boolean toque;

    public PantallaContinuar(Juego juego) {
        super(juego);

        fondo =
                new Textura2D(
                        juego.getRecurso().getTextura("continuar.png").getBipmap(),
                        Juego.ANCHO_PANTALLA,
                        Juego.ALTO_PANTALLA);

        selector =
                new Textura2D(juego.getRecurso().getTextura("selector2.png").getBipmap(), 16, 16);

        posicionY = 288;

        toque = false;
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

        pincel.dibujarTextura(fondo, 0, 0);

        pincel.dibujarTextura(selector, 202, posicionY);
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
                if (posicionY == 288) {

                    juego.setPantalla(new PantallaNivel(juego));
                }

                if (posicionY == 322) {

                    juego.setPantalla(new PantallaMenu(juego));
                }

                break;

            case KeyEvent.KEYCODE_1:
                posicionY = 288;

                break;
            case KeyEvent.KEYCODE_2:
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

            if (posicionY == 288) {
                juego.setPantalla(new PantallaNivel(juego));
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

            if (posicionY == 322) {

                juego.setPantalla(new PantallaMenu(juego));
            }
        }
    }

    @Override
    public void toqueDeslizando(float x, float y, int puntero) {}

    @Override
    public void acelerometro(float x, float y, float z) {}
}
