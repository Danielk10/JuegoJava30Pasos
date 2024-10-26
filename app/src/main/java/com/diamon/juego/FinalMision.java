package com.diamon.juego;

import android.app.Activity;

import com.diamon.nucleo.Juego;
import com.diamon.pantalla.PantallaPresentacion;

public class FinalMision extends Juego {
    public FinalMision(Activity actividad, int ancho, int alto) {
        super(actividad, ancho, alto);

        setPantalla(new PantallaPresentacion(this));
    }
}
