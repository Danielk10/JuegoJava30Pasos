package com.diamon.tutorial.ejemplos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import com.diamon.tutorial.base.Vista;

public class Paso3 extends Vista {

    public Paso3(Context contexto) {
        super(contexto);
    }

    @Override
    public void dibujar(Canvas pincel) {
        lapiz.setColor(Color.BLUE);

        pincel.drawARGB(a, r, g, b);

        pincel.drawRect(50, 50, 150, 150, lapiz);
    }

    @Override
    public void reiniciar() {}

    @Override
    public View getVista() {

        return this;
    }
}
