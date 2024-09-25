package com.diamon.juego;

import com.diamon.nucleo.Juego;
import com.diamon.pantalla.PantallaMenu;

public class FinalMision extends Juego {
	
	

	public FinalMision() {
		super();
		
		setPantalla(new PantallaMenu(this));

	}

}
