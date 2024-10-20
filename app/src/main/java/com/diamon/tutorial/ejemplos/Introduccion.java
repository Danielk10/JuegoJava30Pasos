package com.diamon.tutorial.ejemplos;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.diamon.tutorial.base.Vista;

public class Introduccion extends Vista {

    public Introduccion(Context contexto) {
        super(contexto);
    }

    @Override
    public void dibujar(Canvas pincel) {}

    @Override
    public void reiniciar() {}

    @Override
    public View getVista() {

        return this;
    }
}
