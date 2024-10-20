package com.diamon.entrada;

import android.view.View.OnTouchListener;

import com.diamon.nucleo.Entrada.EventoDeToque;

import java.util.List;

public interface ManejadorDeToque extends OnTouchListener {

    public boolean isToque(int puntero);

    public float getToqueEnX(int puntero);

    public float getToqueEnY(int puntero);

    public List<EventoDeToque> getEventosDeToque();
}
