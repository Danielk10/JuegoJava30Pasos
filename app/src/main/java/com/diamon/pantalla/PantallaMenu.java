package com.diamon.pantalla;

import com.diamon.graficos.Pantalla2D;
import com.diamon.graficos.Textura2D;
import com.diamon.nucleo.Graficos;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Textura;
import com.diamon.nucleo.Sonido;

import android.view.KeyEvent;

public class PantallaMenu extends Pantalla2D {

	private Textura fondo;

	private Textura selector;

	private int posicionY;

	private float tiemoMovimiento;

	private boolean toque;

	public PantallaMenu(Juego juego) {
		super(juego);

		fondo = new Textura2D(recurso.getTextura("menu2.png").getBipmap(), Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		selector = new Textura2D(recurso.getTextura("selector1.png").getBipmap(), 16, 16);

		posicionY = 320;

		    recurso.getSonido("menu.wav").reproducir(1);



		toque = false;
	}

	@Override
	public void mostrar() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void colisiones() {

	}

	@Override
	public void actualizar(float delta) {

		tiemoMovimiento += delta;

		if (tiemoMovimiento / 5f >= 1) {

			juego.setPantalla(new PantallaIntroduccion(juego));

			tiemoMovimiento = 0;
		}

	}

	@Override
	public void dibujar(Graficos pincel, float delta) {

		pincel.dibujarTextura(fondo, 0, 0);

		pincel.dibujarTextura(selector, 186, posicionY);

	} 

	@Override
	public void reajustarPantalla(int ancho, int alto) {

	}

	@Override
	public void pausa() {

	}

	@Override
	public void ocultar() {

	}

	@Override
	public void liberarRecursos() {

	}

	@Override
	public void teclaPresionada(int codigoDeTecla) {

		switch (codigoDeTecla) {

		case KeyEvent.KEYCODE_0:

			if (posicionY == 320) {
				juego.setPantalla(new PantallaNivel(juego));
			}

			if (posicionY == 354) {
				juego.setPantalla(new PantallaMenu(juego));
			}

			break;

		case KeyEvent.KEYCODE_1:

			posicionY = 320;

			break;
		case KeyEvent.KEYCODE_2:

			posicionY = 354;

			break;

		case KeyEvent.KEYCODE_3:

			juego.setPantalla(new PantallaAyuda(juego));

			break;

		default:

			break;

		}

	}

	@Override
	public void teclaLevantada(int codigoDeTecla) {

	}

@Override
	public void toquePresionado(float x, float y, int puntero) {

		if (puntero == 1)
		{
		
		if (posicionY == 320) {
			juego.setPantalla(new PantallaNivel(juego));

		}
		}

	}

	@Override
	public void toqueLevantado(float x, float y, int puntero) {

		if (puntero == 0)
		{
		toque = !toque;

		if (toque) {
			posicionY = 320;

		} else {

			posicionY = 354;
		}
		
		}
		
		if (puntero == 1)
		{

		if (posicionY == 354) {

			juego.setPantalla(new PantallaMenu(juego));
		}
		}

	}

	@Override
	public void toqueDeslizando(float x, float y, int puntero) {

	}

	@Override
	public void acelerometro(float x, float y, float z) {

	}

}
