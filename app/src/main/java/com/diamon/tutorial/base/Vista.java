package com.diamon.tutorial.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public abstract class Vista extends View {
    
    protected Paint lapiz;

    protected int a, r, g, b;

    public Vista(Context contexto) {
        super(contexto);

        lapiz = new Paint();

        lapiz.setStyle(Paint.Style.FILL_AND_STROKE);

        this.a = 0;

        this.r = 0;

        this.g = 0;

        this.b = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        dibujar(canvas);

        super.draw(canvas);
    }

    public void setColor(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public abstract void dibujar(Canvas pincel);

    public abstract void reiniciar();

    public abstract View getVista();
}
