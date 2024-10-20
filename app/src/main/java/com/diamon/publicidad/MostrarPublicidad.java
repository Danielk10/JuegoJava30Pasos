package com.diamon.publicidad;

import android.app.Activity;

public class MostrarPublicidad implements Publicidad {

    private Activity actividad;

    public MostrarPublicidad(Activity actividad) {

        this.actividad = actividad;
    }

    public static void IniciarPublicidad(Activity actividad) {}

    @Override
    public void mostrarInterstitial() {}

    @Override
    public void botonAtrasInterstitial() {}
}
