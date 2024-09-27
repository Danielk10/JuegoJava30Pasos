package com.diamon.juego;

import com.diamon.nucleo.Juego;
import com.diamon.pantalla.PantallaPresentacion;

import android.app.Activity;

public class FinalMision extends Juego {
	public FinalMision(Activity actividad) {
		super(actividad);

		setPantalla(new PantallaPresentacion(this));
	}

}
