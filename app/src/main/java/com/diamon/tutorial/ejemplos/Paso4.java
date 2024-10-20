package com.diamon.tutorial.ejemplos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import com.diamon.tutorial.base.Vista;

public class Paso4 extends Vista {
    
    private int x;

    public Paso4(Context contexto) {
        super(contexto);

        x = 0;
    }

    @Override
    public void dibujar(Canvas pincel) {

        lapiz.setColor(Color.BLUE);

        x++;

        pincel.drawARGB(a, r, g, b);

        pincel.drawRect(x, 50, ((x + 100) - 1), ((50 + 100) - 1), lapiz);

        invalidate();
    }

    @Override
    public void reiniciar() {

        x = 0;
    }

    @Override
    public View getVista() {

        return this;
    }
}
