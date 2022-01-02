package com.diamon.juego;

import com.diamon.nucleo.Juego;
import com.diamon.pantalla.PantallaPrecentacion;

import android.content.Context;
import android.graphics.Bitmap;

public class FinalMision extends Juego
{


	public FinalMision(Context contexto, Bitmap bufer) {
		super(contexto, bufer);

		setPantalla(new PantallaPrecentacion(this));
	}

}
