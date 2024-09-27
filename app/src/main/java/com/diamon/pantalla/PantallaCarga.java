package com.diamon.pantalla;

import com.diamon.graficos.Pantalla2D;
import com.diamon.graficos.Textura2D;
import com.diamon.nucleo.Graficos;
import com.diamon.nucleo.Juego;

public class PantallaCarga extends Pantalla2D {

	private float tiempo;

	private Textura2D textura;

	private float x;

	private Textura2D textura1;

	public PantallaCarga(Juego juego) {
		super(juego);

		tiempo = 0;

		x = 0;

		textura = new Textura2D(juego.getRecurso().getTextura("introduccion.png").getBipmap(), Juego.ANCHO_PANTALLA,
				Juego.ALTO_PANTALLA);

		textura1 = new Textura2D(juego.getRecurso().getTextura("barraProgreso1.png").getBipmap(), Juego.ANCHO_PANTALLA,
				64);
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

		tiempo += delta;

		if (tiempo / 2 >= 1) {

			juego.getRecurso().cargarTextura("fondo1.png");

			juego.getRecurso().cargarTextura("fondo2.png");

			juego.getRecurso().cargarTextura("fondo3.png");

			juego.getRecurso().cargarTextura("fondo4.png");

			juego.getRecurso().cargarTextura("fondo5.png");

			juego.getRecurso().cargarTextura("fondo6.png");

			juego.getRecurso().cargarTextura("fondo7.png");

			juego.getRecurso().cargarTextura("fondo8.png");

			juego.getRecurso().cargarTextura("fondo9.png");

			juego.getRecurso().cargarTextura("fondo10.png");

			juego.getRecurso().cargarTextura("fondo11.png");

			juego.getRecurso().cargarTextura("fondo12.png");

			juego.getRecurso().cargarTextura("fondo13.png");

			juego.getRecurso().cargarTextura("fondo14.png");

			juego.getRecurso().cargarTextura("fondo15.png");

			juego.getRecurso().cargarTextura("fondo16.png");

			juego.getRecurso().cargarTextura("fondo17.png");

			juego.getRecurso().cargarTextura("fondo18.png");

			juego.getRecurso().cargarTextura("fondo19.png");

			juego.getRecurso().cargarTextura("fondo20.png");

			juego.getRecurso().cargarTextura("fondo21MGF.png");

			juego.getRecurso().cargarTextura("maquinaFinal.png");

			juego.getRecurso().cargarTextura("fondoIntroduccion1.png");

			juego.getRecurso().cargarTextura("fondoIntroduccion2.png");

			juego.getRecurso().cargarTextura("fondoIntroduccion3.png");

			juego.getRecurso().cargarTextura("menu1.png");

			juego.getRecurso().cargarTextura("menu2.png");

			juego.getRecurso().cargarTextura("menuFinal.png");

			juego.getRecurso().cargarTextura("creditos.png");

			juego.getRecurso().cargarTextura("continuar.png");

			juego.getRecurso().cargarTextura("nivel1.png");

			juego.getRecurso().cargarTextura("finJuego.png");

			juego.getRecurso().cargarTextura("finNivel.png");

			juego.getRecurso().cargarTextura("jugador1D1.png");

			juego.getRecurso().cargarTextura("jugador1D2.png");

			juego.getRecurso().cargarTextura("jugador1D3.png");

			juego.getRecurso().cargarTextura("jugador1D4.png");

			juego.getRecurso().cargarTextura("jugador1D5.png");

			juego.getRecurso().cargarTextura("jugador1D6.png");

			juego.getRecurso().cargarTextura("jugador1I1.png");

			juego.getRecurso().cargarTextura("jugador1I2.png");

			juego.getRecurso().cargarTextura("jugador1I3.png");

			juego.getRecurso().cargarTextura("jugador2D1.png");

			juego.getRecurso().cargarTextura("jugador2D2.png");

			juego.getRecurso().cargarTextura("jugador2D3.png");

			juego.getRecurso().cargarTextura("jugador2D4.png");

			juego.getRecurso().cargarTextura("jugador2I1.png");

			juego.getRecurso().cargarTextura("humoMisil1.png");

			juego.getRecurso().cargarTextura("humoMisil2.png");

			juego.getRecurso().cargarTextura("humoMisil3.png");

			juego.getRecurso().cargarTextura("lanzaMisilI1.png");

			juego.getRecurso().cargarTextura("lanzaMisilI2.png");

			juego.getRecurso().cargarTextura("lanzaMisilI3.png");

			juego.getRecurso().cargarTextura("lanzaMisilD1.png");

			juego.getRecurso().cargarTextura("lanzaMisilD2.png");

			juego.getRecurso().cargarTextura("lanzaMisilD3.png");

			juego.getRecurso().cargarTextura("maquinaParedI1.png");

			juego.getRecurso().cargarTextura("maquinaParedI2.png");

			juego.getRecurso().cargarTextura("maquinaParedD1.png");

			juego.getRecurso().cargarTextura("maquinaParedD2.png");

			juego.getRecurso().cargarTextura("poderB.png");

			juego.getRecurso().cargarTextura("poderL.png");

			juego.getRecurso().cargarTextura("poderS.png");

			juego.getRecurso().cargarTextura("poderW.png");

			juego.getRecurso().cargarTextura("robotD.png");

			juego.getRecurso().cargarTextura("robotI.png");

			juego.getRecurso().cargarTextura("saltador1.png");

			juego.getRecurso().cargarTextura("saltador2.png");

			juego.getRecurso().cargarTextura("saltador3.png");

			juego.getRecurso().cargarTextura("saltador4.png");

			juego.getRecurso().cargarTextura("sateliteDD1.png");

			juego.getRecurso().cargarTextura("sateliteDD2.png");

			juego.getRecurso().cargarTextura("sateliteDI1.png");

			juego.getRecurso().cargarTextura("sateliteDI2.png");

			juego.getRecurso().cargarTextura("sateliteHD1.png");

			juego.getRecurso().cargarTextura("sateliteHD2.png");

			juego.getRecurso().cargarTextura("sateliteHI1.png");

			juego.getRecurso().cargarTextura("sateliteHI2.png");

			juego.getRecurso().cargarTextura("sateliteVA1.png");

			juego.getRecurso().cargarTextura("sateliteVA2.png");

			juego.getRecurso().cargarTextura("sateliteVB1.png");

			juego.getRecurso().cargarTextura("sateliteVB2.png");

			juego.getRecurso().cargarTextura("selector1.png");

			juego.getRecurso().cargarTextura("selector2.png");

			juego.getRecurso().cargarTextura("vida1.png");

			juego.getRecurso().cargarTextura("vida2.png");

			juego.getRecurso().cargarTextura("voladorI1.png");

			juego.getRecurso().cargarTextura("voladorI2.png");

			juego.getRecurso().cargarTextura("voladorI3.png");

			juego.getRecurso().cargarTextura("voladorD1.png");

			juego.getRecurso().cargarTextura("voladorD2.png");

			juego.getRecurso().cargarTextura("voladorD3.png");

			juego.getRecurso().cargarTextura("antiAreoD1.png");

			juego.getRecurso().cargarTextura("antiAreoH1.png");

			juego.getRecurso().cargarTextura("antiAreoV1.png");

			juego.getRecurso().cargarTextura("antiAreoD2.png");

			juego.getRecurso().cargarTextura("antiAreoH2.png");

			juego.getRecurso().cargarTextura("antiAreoV2.png");

			juego.getRecurso().cargarTextura("antiAreoEspecialD.png");

			juego.getRecurso().cargarTextura("antiAreoEspecialH.png");

			juego.getRecurso().cargarTextura("antiAreoEspecialV1.png");

			juego.getRecurso().cargarTextura("antiAreoEspecialV2.png");

			juego.getRecurso().cargarTextura("antiAreoEspecialV3.png");

			juego.getRecurso().cargarTextura("antiAreoEspecialV4.png");

			juego.getRecurso().cargarTextura("antiAreoEspecialV5.png");

			juego.getRecurso().cargarTextura("antiAreoEspecialV6.png");

			juego.getRecurso().cargarTextura("antiAreoEspecialV7.png");

			juego.getRecurso().cargarTextura("antiAreoEspecialV8.png");

			juego.getRecurso().cargarTextura("antiAreoEspecialV9.png");

			juego.getRecurso().cargarTextura("antiAreoEspecialV10.png");

			juego.getRecurso().cargarTextura("antiAreoEspecialV11.png");

			juego.getRecurso().cargarTextura("antiAreoEspecialV12.png");

			juego.getRecurso().cargarTextura("antiAreoEspecialV13.png");

			juego.getRecurso().cargarTextura("antiAreoEspecialV14.png");

			juego.getRecurso().cargarTextura("misilD1.png");

			juego.getRecurso().cargarTextura("misilH1.png");

			juego.getRecurso().cargarTextura("misilV1.png");

			juego.getRecurso().cargarTextura("misilD2.png");

			juego.getRecurso().cargarTextura("misilH2.png");

			juego.getRecurso().cargarTextura("misilV2.png");

			juego.getRecurso().cargarTextura("balaBD.png");

			juego.getRecurso().cargarTextura("balaBI.png");

			juego.getRecurso().cargarTextura("balaE1.png");

			juego.getRecurso().cargarTextura("balaE2.png");

			juego.getRecurso().cargarTextura("balaE3.png");

			juego.getRecurso().cargarTextura("balaE4.png");

			juego.getRecurso().cargarTextura("balaHD.png");

			juego.getRecurso().cargarTextura("balaHI.png");

			juego.getRecurso().cargarTextura("balaLD1.png");

			juego.getRecurso().cargarTextura("balaLD2.png");

			juego.getRecurso().cargarTextura("balaLD3.png");

			juego.getRecurso().cargarTextura("balaLI1.png");

			juego.getRecurso().cargarTextura("balaLI2.png");

			juego.getRecurso().cargarTextura("balaLI3.png");

			juego.getRecurso().cargarTextura("balaParedI.png");

			juego.getRecurso().cargarTextura("balaParedD.png");

			juego.getRecurso().cargarTextura("balaSaltador1.png");

			juego.getRecurso().cargarTextura("balaSaltador2.png");

			juego.getRecurso().cargarTextura("balaSatelite.png");

			juego.getRecurso().cargarTextura("balaWD1.png");

			juego.getRecurso().cargarTextura("balaWD2.png");

			juego.getRecurso().cargarTextura("balaWD3.png");

			juego.getRecurso().cargarTextura("balaWI1.png");

			juego.getRecurso().cargarTextura("balaWI2.png");

			juego.getRecurso().cargarTextura("balaWI3.png");

			juego.getRecurso().cargarTextura("bola1.png");

			juego.getRecurso().cargarTextura("bola2.png");

			juego.getRecurso().cargarTextura("bola3.png");

			juego.getRecurso().cargarTextura("bola4.png");

			juego.getRecurso().cargarTextura("bola5.png");

			juego.getRecurso().cargarTextura("bola6.png");

			juego.getRecurso().cargarTextura("bola7.png");

			juego.getRecurso().cargarTextura("bola8.png");

			juego.getRecurso().cargarTextura("bola9.png");

			juego.getRecurso().cargarTextura("bola10.png");

			juego.getRecurso().cargarTextura("bola11.png");

			juego.getRecurso().cargarTextura("bola12.png");

			juego.getRecurso().cargarTextura("bola13.png");

			juego.getRecurso().cargarTextura("bola14.png");

			juego.getRecurso().cargarTextura("bola15.png");

			juego.getRecurso().cargarTextura("bola16.png");

			juego.getRecurso().cargarTextura("cajaPoder1.png");

			juego.getRecurso().cargarTextura("cajaPoder2.png");

			juego.getRecurso().cargarTextura("cajaPoder3.png");

			juego.getRecurso().cargarTextura("cajaPoder4.png");

			juego.getRecurso().cargarTextura("explosion1.png");

			juego.getRecurso().cargarTextura("explosion2.png");

			juego.getRecurso().cargarTextura("explosion3.png");

			juego.getRecurso().cargarTextura("explosion4.png");

			juego.getRecurso().cargarTextura("explosionB1.png");

			juego.getRecurso().cargarTextura("explosionB2.png");

			juego.getRecurso().cargarTextura("explosionB3.png");

			juego.getRecurso().cargarTextura("explosionB4.png");

			juego.getRecurso().cargarTextura("explosionB5.png");

			juego.getRecurso().cargarTextura("explosionMisil1.png");

			juego.getRecurso().cargarTextura("explosionMisil2.png");

			juego.getRecurso().cargarTextura("explosionMisil3.png");

			juego.getRecurso().cargarTextura("explosionMisil4.png");

			juego.getRecurso().cargarMusica("musica.wav");

			juego.getRecurso().cargarSonido("menu.wav");

			juego.getRecurso().cargarMusica("precentacion1.wav");

			juego.getRecurso().cargarMusica("comienzo1.wav");

			juego.getRecurso().cargarSonido("disparo.wav");

			juego.getRecurso().cargarSonido("disparoL.wav");

			juego.getRecurso().cargarSonido("poder.wav");

			juego.getRecurso().cargarMusica("introduccion.wav");

			juego.getRecurso().cargarMusica("finDeJuego.wav");

			juego.getRecurso().cargarSonido("muriendo.wav");

			juego.getRecurso().cargarSonido("explosion.wav");

			juego.getRecurso().cargarSonido("pausa.wav");

			juego.setPantalla(new PantallaMenu(juego));

			tiempo = 0;
		}

	}

	@Override
	public void dibujar(Graficos pincel, float delta) {

		x += 12 / Juego.DELTA_A_PIXEL * delta;

		pincel.dibujarTextura(textura, 0, 0);

		pincel.dibujarTextura(textura1, x, 160);

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

	}

	@Override
	public void teclaLevantada(int codigoDeTecla) {

	}

	@Override
	public void toquePresionado(float x, float y, int puntero) {

	}

	@Override
	public void toqueLevantado(float x, float y, int puntero) {

	}

	@Override
	public void toqueDeslizando(float x, float y, int puntero) {

	}

	@Override
	public void acelerometro(float x, float y, float z) {

	}

}
