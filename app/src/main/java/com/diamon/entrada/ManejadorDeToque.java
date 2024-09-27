package com.diamon.entrada;

import java.util.List;

import com.diamon.nucleo.Entrada.EventoDeToque;

import android.view.View.OnTouchListener;

public interface ManejadorDeToque extends OnTouchListener {

	public boolean isToque(int puntero);

	public float getToqueEnX(int puntero);

	public float getToqueEnY(int puntero);

	public List<EventoDeToque> getEventosDeToque();

}
