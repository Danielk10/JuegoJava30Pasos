package com.diamon.pantalla;

import com.diamon.graficos.Pantalla2D;
import com.diamon.graficos.Textura2D;
import com.diamon.nucleo.Graficos;
import com.diamon.nucleo.Juego;

import android.view.KeyEvent;

public class PantallaAyuda extends Pantalla2D {

	private Textura2D textura;

	public PantallaAyuda(Juego juego) {
		super(juego);

		textura = new Textura2D(juego.getRecurso().getTextura("ayuda.png").getBipmap(), Juego.ANCHO_PANTALLA,
				Juego.ALTO_PANTALLA);
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

	}

	@Override
	public void dibujar(Graficos pincel, float delta) {

		pincel.dibujarTextura(textura, 0, 0);

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

			juego.setPantalla(new PantallaMenu(juego));

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

	}

	@Override
	public void toqueLevantado(float x, float y, int puntero) {

		juego.setPantalla(new PantallaCarga(juego));

	}

	@Override
	public void toqueDeslizando(float x, float y, int puntero) {

	}

	@Override
	public void acelerometro(float x, float y, float z) {

	}

}
