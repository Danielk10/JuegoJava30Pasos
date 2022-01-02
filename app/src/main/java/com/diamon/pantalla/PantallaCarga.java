package com.diamon.pantalla;

import com.diamon.juego.FinalMision;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.utilidad.Texturas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.diamon.utilidad.*;

public class PantallaCarga extends Pantalla {

	private Bitmap fondo;

	private int ciclo;

	private int x;

	private Bitmap barra;

	public PantallaCarga(FinalMision juego) {
		super(juego);

		fondo = this.crearBitmap(juego.getRecurso().getImagen("introduccion.png"), Juego.ANCHO_PANTALLA,
				Juego.ALTO_PANTALLA);

		barra = this.crearBitmap(juego.getRecurso().getImagen("barraProgreso1.png"), Juego.ANCHO_PANTALLA, 64);

		ciclo = 0;

		x = 0;
	}

	@Override
	public void pausa() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void actualizar(float delta) {

		ciclo++;

		if (ciclo % 60 == 0) {

			/*juego.getRecurso().cargarImagen("fondo1.png");

			juego.getRecurso().cargarImagen("fondo2.png");

			juego.getRecurso().cargarImagen("fondo3.png");

			juego.getRecurso().cargarImagen("fondo4.png");

			juego.getRecurso().cargarImagen("fondo5.png");

			juego.getRecurso().cargarImagen("fondo6.png");

			juego.getRecurso().cargarImagen("fondo7.png");

			juego.getRecurso().cargarImagen("fondo8.png");

			juego.getRecurso().cargarImagen("fondo9.png");

			juego.getRecurso().cargarImagen("fondo10.png");

			juego.getRecurso().cargarImagen("fondo11.png");

			juego.getRecurso().cargarImagen("fondo12.png");

			juego.getRecurso().cargarImagen("fondo13.png");

			juego.getRecurso().cargarImagen("fondo14.png");

			juego.getRecurso().cargarImagen("fondo15.png");

			juego.getRecurso().cargarImagen("fondo16.png");

			juego.getRecurso().cargarImagen("fondo17.png");

			juego.getRecurso().cargarImagen("fondo18.png");

			juego.getRecurso().cargarImagen("fondo19.png");

			juego.getRecurso().cargarImagen("fondo20.png");

			juego.getRecurso().cargarImagen("fondo21MGF.png");

			juego.getRecurso().cargarImagen("maquinaFinal.png");

			juego.getRecurso().cargarImagen("fondoIntroduccion1.png");

			juego.getRecurso().cargarImagen("fondoIntroduccion2.png");

			juego.getRecurso().cargarImagen("fondoIntroduccion3.png");

			juego.getRecurso().cargarImagen("menu1.png");

			juego.getRecurso().cargarImagen("menu2.png");

			juego.getRecurso().cargarImagen("menuFinal.png");

			juego.getRecurso().cargarImagen("creditos.png");

			juego.getRecurso().cargarImagen("continuar.png");

			juego.getRecurso().cargarImagen("nivel1.png");

			juego.getRecurso().cargarImagen("finJuego.png");

			juego.getRecurso().cargarImagen("finNivel.png");

			juego.getRecurso().cargarImagen("jugador1D1.png");

			juego.getRecurso().cargarImagen("jugador1D2.png");

			juego.getRecurso().cargarImagen("jugador1D3.png");

			juego.getRecurso().cargarImagen("jugador1D4.png");

			juego.getRecurso().cargarImagen("jugador1D5.png");

			juego.getRecurso().cargarImagen("jugador1D6.png");

			juego.getRecurso().cargarImagen("jugador1I1.png");

			juego.getRecurso().cargarImagen("jugador1I2.png");

			juego.getRecurso().cargarImagen("jugador1I3.png");

			juego.getRecurso().cargarImagen("jugador2D1.png");

			juego.getRecurso().cargarImagen("jugador2D2.png");

			juego.getRecurso().cargarImagen("jugador2D3.png");

			juego.getRecurso().cargarImagen("jugador2D4.png");

			juego.getRecurso().cargarImagen("jugador2I1.png");

			juego.getRecurso().cargarImagen("humoMisil1.png");

			juego.getRecurso().cargarImagen("humoMisil2.png");

			juego.getRecurso().cargarImagen("humoMisil3.png");

			juego.getRecurso().cargarImagen("lanzaMisilI1.png");

			juego.getRecurso().cargarImagen("lanzaMisilI2.png");

			juego.getRecurso().cargarImagen("lanzaMisilI3.png");

			juego.getRecurso().cargarImagen("lanzaMisilD1.png");

			juego.getRecurso().cargarImagen("lanzaMisilD2.png");

			juego.getRecurso().cargarImagen("lanzaMisilD3.png");

			juego.getRecurso().cargarImagen("maquinaParedI1.png");

			juego.getRecurso().cargarImagen("maquinaParedI2.png");

			juego.getRecurso().cargarImagen("maquinaParedD1.png");

			juego.getRecurso().cargarImagen("maquinaParedD2.png");

			juego.getRecurso().cargarImagen("poderB.png");

			juego.getRecurso().cargarImagen("poderL.png");

			juego.getRecurso().cargarImagen("poderS.png");

			juego.getRecurso().cargarImagen("poderW.png");

			juego.getRecurso().cargarImagen("robotD.png");

			juego.getRecurso().cargarImagen("robotI.png");

			juego.getRecurso().cargarImagen("saltador1.png");

			juego.getRecurso().cargarImagen("saltador2.png");

			juego.getRecurso().cargarImagen("saltador3.png");

			juego.getRecurso().cargarImagen("saltador4.png");

			juego.getRecurso().cargarImagen("sateliteDD1.png");

			juego.getRecurso().cargarImagen("sateliteDD2.png");

			juego.getRecurso().cargarImagen("sateliteDI1.png");

			juego.getRecurso().cargarImagen("sateliteDI2.png");

			juego.getRecurso().cargarImagen("sateliteHD1.png");

			juego.getRecurso().cargarImagen("sateliteHD2.png");

			juego.getRecurso().cargarImagen("sateliteHI1.png");

			juego.getRecurso().cargarImagen("sateliteHI2.png");

			juego.getRecurso().cargarImagen("sateliteVA1.png");

			juego.getRecurso().cargarImagen("sateliteVA2.png");

			juego.getRecurso().cargarImagen("sateliteVB1.png");

			juego.getRecurso().cargarImagen("sateliteVB2.png");

			juego.getRecurso().cargarImagen("selector1.png");

			juego.getRecurso().cargarImagen("selector2.png");

			juego.getRecurso().cargarImagen("vida1.png");

			juego.getRecurso().cargarImagen("vida2.png");

			juego.getRecurso().cargarImagen("voladorI1.png");

			juego.getRecurso().cargarImagen("voladorI2.png");

			juego.getRecurso().cargarImagen("voladorI3.png");

			juego.getRecurso().cargarImagen("voladorD1.png");

			juego.getRecurso().cargarImagen("voladorD2.png");

			juego.getRecurso().cargarImagen("voladorD3.png");

			juego.getRecurso().cargarImagen("antiAreoD1.png");

			juego.getRecurso().cargarImagen("antiAreoH1.png");

			juego.getRecurso().cargarImagen("antiAreoV1.png");

			juego.getRecurso().cargarImagen("antiAreoD2.png");

			juego.getRecurso().cargarImagen("antiAreoH2.png");

			juego.getRecurso().cargarImagen("antiAreoV2.png");

			juego.getRecurso().cargarImagen("antiAreoEspecialD.png");

			juego.getRecurso().cargarImagen("antiAreoEspecialH.png");

			juego.getRecurso().cargarImagen("antiAreoEspecialV1.png");

			juego.getRecurso().cargarImagen("antiAreoEspecialV2.png");

			juego.getRecurso().cargarImagen("antiAreoEspecialV3.png");

			juego.getRecurso().cargarImagen("antiAreoEspecialV4.png");

			juego.getRecurso().cargarImagen("antiAreoEspecialV5.png");

			juego.getRecurso().cargarImagen("antiAreoEspecialV6.png");

			juego.getRecurso().cargarImagen("antiAreoEspecialV7.png");

			juego.getRecurso().cargarImagen("antiAreoEspecialV8.png");

			juego.getRecurso().cargarImagen("antiAreoEspecialV9.png");

			juego.getRecurso().cargarImagen("antiAreoEspecialV10.png");

			juego.getRecurso().cargarImagen("antiAreoEspecialV11.png");

			juego.getRecurso().cargarImagen("antiAreoEspecialV12.png");

			juego.getRecurso().cargarImagen("antiAreoEspecialV13.png");

			juego.getRecurso().cargarImagen("antiAreoEspecialV14.png");

			juego.getRecurso().cargarImagen("misilD1.png");

			juego.getRecurso().cargarImagen("misilH1.png");

			juego.getRecurso().cargarImagen("misilV1.png");

			juego.getRecurso().cargarImagen("misilD2.png");

			juego.getRecurso().cargarImagen("misilH2.png");

			juego.getRecurso().cargarImagen("misilV2.png");

			juego.getRecurso().cargarImagen("balaBD.png");

			juego.getRecurso().cargarImagen("balaBI.png");

			juego.getRecurso().cargarImagen("balaE1.png");

			juego.getRecurso().cargarImagen("balaE2.png");

			juego.getRecurso().cargarImagen("balaE3.png");

			juego.getRecurso().cargarImagen("balaE4.png");

			juego.getRecurso().cargarImagen("balaHD.png");

			juego.getRecurso().cargarImagen("balaHI.png");

			juego.getRecurso().cargarImagen("balaLD1.png");

			juego.getRecurso().cargarImagen("balaLD2.png");

			juego.getRecurso().cargarImagen("balaLD3.png");

			juego.getRecurso().cargarImagen("balaLI1.png");

			juego.getRecurso().cargarImagen("balaLI2.png");

			juego.getRecurso().cargarImagen("balaLI3.png");

			juego.getRecurso().cargarImagen("balaParedI.png");

			juego.getRecurso().cargarImagen("balaParedD.png");

			juego.getRecurso().cargarImagen("balaSaltador1.png");

			juego.getRecurso().cargarImagen("balaSaltador2.png");

			juego.getRecurso().cargarImagen("balaSatelite.png");

			juego.getRecurso().cargarImagen("balaWD1.png");

			juego.getRecurso().cargarImagen("balaWD2.png");

			juego.getRecurso().cargarImagen("balaWD3.png");

			juego.getRecurso().cargarImagen("balaWI1.png");

			juego.getRecurso().cargarImagen("balaWI2.png");

			juego.getRecurso().cargarImagen("balaWI3.png");

			juego.getRecurso().cargarImagen("bola1.png");

			juego.getRecurso().cargarImagen("bola2.png");

			juego.getRecurso().cargarImagen("bola3.png");

			juego.getRecurso().cargarImagen("bola4.png");

			juego.getRecurso().cargarImagen("bola5.png");

			juego.getRecurso().cargarImagen("bola6.png");

			juego.getRecurso().cargarImagen("bola7.png");

			juego.getRecurso().cargarImagen("bola8.png");

			juego.getRecurso().cargarImagen("bola9.png");

			juego.getRecurso().cargarImagen("bola10.png");

			juego.getRecurso().cargarImagen("bola11.png");

			juego.getRecurso().cargarImagen("bola12.png");

			juego.getRecurso().cargarImagen("bola13.png");

			juego.getRecurso().cargarImagen("bola14.png");

			juego.getRecurso().cargarImagen("bola15.png");

			juego.getRecurso().cargarImagen("bola16.png");

			juego.getRecurso().cargarImagen("cajaPoder1.png");

			juego.getRecurso().cargarImagen("cajaPoder2.png");

			juego.getRecurso().cargarImagen("cajaPoder3.png");

			juego.getRecurso().cargarImagen("cajaPoder4.png");

			juego.getRecurso().cargarImagen("explosion1.png");

			juego.getRecurso().cargarImagen("explosion2.png");

			juego.getRecurso().cargarImagen("explosion3.png");

			juego.getRecurso().cargarImagen("explosion4.png");

			juego.getRecurso().cargarImagen("explosionB1.png");

			juego.getRecurso().cargarImagen("explosionB2.png");

			juego.getRecurso().cargarImagen("explosionB3.png");

			juego.getRecurso().cargarImagen("explosionB4.png");

			juego.getRecurso().cargarImagen("explosionB5.png");

			juego.getRecurso().cargarImagen("explosionMisil1.png");

			juego.getRecurso().cargarImagen("explosionMisil2.png");

			juego.getRecurso().cargarImagen("explosionMisil3.png");

			juego.getRecurso().cargarImagen("explosionMisil4.png");*/

			juego.getRecurso().cargarMusica("musica.wav");

			juego.getRecurso().cargarMusica("menu.wav");

			juego.getRecurso().cargarMusica("precentacion1.wav");

			juego.getRecurso().cargarMusica("comienzo1.wav");

			juego.getRecurso().cargarMusica("disparo.wav");

			juego.getRecurso().cargarMusica("disparoL.wav");

			juego.getRecurso().cargarMusica("poder.wav");

			juego.getRecurso().cargarMusica("introduccion.wav");

			juego.getRecurso().cargarMusica("finDeJuego.wav");

			juego.getRecurso().cargarMusica("muriendo.wav");

			juego.getRecurso().cargarMusica("explosion.wav");

			juego.getRecurso().cargarMusica("pausa.wav");
			
			
			
			Texturas.antiAreoD1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoD1.png"), 32, 32);
			Texturas.antiAreoD2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoD2.png"), 32, 32);
			Texturas.antiAreoEspecialD = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoEspecialD.png"), 16, 16);
			Texturas.antiAreoEspecialH = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoEspecialH.png"), 16, 16);
			Texturas.antiAreoEspecialV1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoEspecialV1.png"), 16, 16);
			Texturas.antiAreoEspecialV2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoEspecialV2.png"), 16, 16);
			Texturas.antiAreoEspecialV3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoEspecialV3.png"), 16, 16);
			Texturas.antiAreoEspecialV4 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoEspecialV4.png"), 16, 16);
			Texturas.antiAreoEspecialV5 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoEspecialV5.png"), 16, 16);
			Texturas.antiAreoEspecialV6 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoEspecialV6.png"), 16, 16);
			Texturas.antiAreoEspecialV7 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoEspecialV7.png"), 16, 16);
			Texturas.antiAreoEspecialV8 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoEspecialV8.png"), 16, 16);
			Texturas.antiAreoEspecialV9 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoEspecialV9.png"), 16, 16);
			Texturas.antiAreoEspecialV10 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoEspecialV10.png"), 16, 16);
			Texturas.antiAreoEspecialV11 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoEspecialV11.png"), 16, 16);
			Texturas.antiAreoEspecialV12 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoEspecialV12.png"), 16, 16);
			Texturas.antiAreoEspecialV13 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoEspecialV13.png"), 16, 16);
			Texturas.antiAreoEspecialV14 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoEspecialV14.png"), 16, 16);

			Texturas.antiAreoH1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoH1.png"), 32, 32);
			Texturas.antiAreoH2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoH2.png"), 32, 32);
			Texturas.antiAreoV1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoV1.png"), 32, 32);
			Texturas.antiAreoV2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("antiAreoV2.png"), 32, 32);
			Texturas.ayuda = Recurso.crearBitmap(juego.getRecurso().cargarImagen("ayuda.png"), 640, 480);
			Texturas.balaBD = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaBD.png"), 12, 12);
			Texturas.balaBI = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaBI.png"), 12, 12);
			Texturas.balaE1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaE1.png"), 12, 12);
			Texturas.balaE2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaE2.png"), 12, 12);
			Texturas.balaE3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaE3.png"), 12, 12);
			Texturas.balaE4 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaE4.png"), 12, 12);

			Texturas.balaHD = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaHD.png"), 16, 4);
			Texturas.balaHI = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaHI.png"), 16, 4);
			Texturas.balaLD1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaLD1.png"), 256, 4);
			Texturas.balaLD2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaLD2.png"), 256, 4);
			Texturas.balaLD3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaLD3.png"), 256, 4);
			Texturas.balaLI1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaLI1.png"), 256, 4);
			Texturas.balaLI2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaLI2.png"), 256, 4);
			Texturas.balaLI3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaLI3.png"), 256, 4);
			Texturas.balaParedD = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaParedD.png"), 32, 12);
			Texturas.balaParedI = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaParedI.png"), 32, 12);
			Texturas.balaSaltador1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaSaltador1.png"), 32, 32);
			Texturas.balaSaltador2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaSaltador2.png"), 32, 32);
			Texturas.balaSatelite = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaSatelite.png"), 8, 8);
			Texturas.balaWD1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaWD1.png"), 16, 64);
			Texturas.balaWD2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaWD2.png"), 16, 64);
			Texturas.balaWD3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaWD3.png"), 16, 64);
			Texturas.balaWI1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaWI1.png"), 16, 64);
			Texturas.balaWI2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaWI2.png"), 16, 64);
			Texturas.balaWI3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("balaWI3.png"), 16, 64);
			Texturas.barraProgreso = Recurso.crearBitmap(juego.getRecurso().cargarImagen("barraProgreso.png"), 16, 16);
			Texturas.barraProgreso1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("barraProgreso1.png"), 640, 64);
			Texturas.bola1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("bola1.png"), 16, 16);
			Texturas.bola2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("bola2.png"), 16, 16);
			Texturas.bola3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("bola3.png"), 16, 16);
			Texturas.bola4 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("bola4.png"), 16, 16);
			Texturas.bola5 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("bola5.png"), 16, 16);
			Texturas.bola6 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("bola6.png"), 16, 16);
			Texturas.bola7 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("bola7.png"), 16, 16);
			Texturas.bola8 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("bola8.png"), 16, 16);
			Texturas.bola9 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("bola9.png"), 16, 16);
			Texturas.bola10 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("bola10.png"), 16, 16);
			Texturas.bola11 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("bola11.png"), 16, 16);
			Texturas.bola12 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("bola12.png"), 16, 16);
			Texturas.bola13 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("bola13.png"), 16, 16);
			Texturas.bola14 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("bola14.png"), 16, 16);
			Texturas.bola15 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("bola15.png"), 16, 16);
			Texturas.bola16 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("bola16.png"), 16, 16);
			Texturas.cajaPoder1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("cajaPoder1.png"), 32, 32);
			Texturas.cajaPoder2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("cajaPoder2.png"), 32, 32);
			Texturas.cajaPoder3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("cajaPoder3.png"), 32, 32);
			Texturas.cajaPoder4 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("cajaPoder4.png"), 32, 32);
			Texturas.continuar = Recurso.crearBitmap(juego.getRecurso().cargarImagen("continuar.png"), 640, 480);
			Texturas.creditos = Recurso.crearBitmap(juego.getRecurso().cargarImagen("creditos.png"), 640, 640);
			Texturas.explosion1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("explosion1.png"), 64, 64);
			Texturas.explosion2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("explosion2.png"), 64, 64);
			Texturas.explosion3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("explosion3.png"), 64, 64);
			Texturas.explosion4 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("explosion4.png"), 64, 64);
			Texturas.explosionB1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("explosionB1.png"), 64, 64);
			Texturas.explosionB2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("explosionB2.png"), 64, 64);
			Texturas.explosionB3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("explosionB3.png"), 64, 64);
			Texturas.explosionB4 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("explosionB4.png"), 64, 64);
			Texturas.explosionB5 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("explosionB5.png"), 64, 64);
			Texturas.explosionMisil1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("explosionMisil1.png"), 32, 32);
			Texturas.explosionMisil2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("explosionMisil2.png"), 32, 32);
			Texturas.explosionMisil3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("explosionMisil3.png"), 32, 32);
			Texturas.explosionMisil4 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("explosionMisil4.png"), 32, 32);
			Texturas.finJuego = Recurso.crearBitmap(juego.getRecurso().cargarImagen("finJuego.png"), 640, 480);
			Texturas.finNivel = Recurso.crearBitmap(juego.getRecurso().cargarImagen("finNivel.png"), 640, 480);
			Texturas.fondo1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo1.png"), 640, 480);
			Texturas.fondo2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo2.png"), 640, 480);
			Texturas.fondo3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo3.png"), 640, 480);
			Texturas.fondo4 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo4.png"), 640, 480);
			Texturas.fondo5 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo5.png"), 640, 480);
			Texturas.fondo6 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo6.png"), 640, 480);
			Texturas.fondo7 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo7.png"), 640, 480);
			Texturas.fondo8 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo8.png"), 640, 480);
			Texturas.fondo9 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo9.png"), 640, 480);
			Texturas.fondo10 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo10.png"), 640, 480);
			Texturas.fondo11 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo11.png"), 640, 480);
		    Texturas.fondo12 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo12.png"), 640, 480);
			Texturas.fondo13 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo13.png"), 640, 480);
			Texturas.fondo14 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo14.png"), 640, 480);
			Texturas.fondo15 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo15.png"), 640, 480);
			Texturas.fondo16 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo16.png"), 640, 480);
			Texturas.fondo17 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo17.png"), 640, 480);
			Texturas.fondo18 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo18.png"), 640, 480);
			Texturas.fondo19 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo19.png"), 640, 480);
			Texturas.fondo20 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo20.png"), 640, 480);
			Texturas.fondo21MGF = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondo21MGF.png"), 640, 480);
			Texturas.fondoIntroduccion1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondoIntroduccion1.png"), 640, 480);
			Texturas.fondoIntroduccion2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondoIntroduccion2.png"), 640, 480);
			Texturas.fondoIntroduccion3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("fondoIntroduccion3.png"), 640, 480);
			Texturas.humoMisil1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("humoMisil1.png"), 16, 16);
			Texturas.humoMisil2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("humoMisil2.png"), 16, 16);
			Texturas.humoMisil3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("humoMisil3.png"), 16, 16);
			Texturas.introduccion = Recurso.crearBitmap(juego.getRecurso().cargarImagen("introduccion.png"), 640, 480);
			Texturas.jugador1D1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("jugador1D1.png"), 64, 64);
			Texturas.jugador1D2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("jugador1D2.png"), 64, 64);
			Texturas.jugador1D3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("jugador1D3.png"), 64, 64);
			Texturas.jugador1D4 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("jugador1D4.png"), 64, 64);
			Texturas.jugador1D5 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("jugador1D5.png"), 64, 64);
			Texturas.jugador1D6 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("jugador1D6.png"), 64, 64);
			Texturas.jugador1I1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("jugador1I1.png"), 64, 64);
			Texturas.jugador1I2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("jugador1I2.png"), 64, 64);
			Texturas.jugador1I3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("jugador1I3.png"), 64, 64);
			Texturas.jugador2D1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("jugador2D1.png"), 64, 64);
			Texturas.jugador2D2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("jugador2D2.png"), 64, 64);
			Texturas.jugador2D3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("jugador2D3.png"), 64, 64);
			Texturas.jugador2D4 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("jugador2D4.png"), 64, 64);
			Texturas.jugador2I1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("jugador2I1.png"), 64, 64);
			Texturas.lanzaMisilD1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("lanzaMisilD1.png"), 48, 32);
			Texturas.lanzaMisilD2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("lanzaMisilD2.png"), 48, 32);
			Texturas.lanzaMisilD3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("lanzaMisilD3.png"), 48, 32);
			Texturas.lanzaMisilI1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("lanzaMisilI1.png"), 48, 32);
			Texturas.lanzaMisilI2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("lanzaMisilI2.png"), 48, 32);
			Texturas.lanzaMisilI3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("lanzaMisilI3.png"), 48, 32);
			Texturas.maquinaFinal = Recurso.crearBitmap(juego.getRecurso().cargarImagen("maquinaFinal.png"), 64, 64);
			Texturas.maquinaParedD1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("maquinaParedD1.png"), 32, 32);
			Texturas.maquinaParedD2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("maquinaParedD2.png"), 32, 32);
			Texturas.maquinaParedI1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("maquinaParedI1.png"), 32, 32);
			Texturas.maquinaParedI2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("maquinaParedI2.png"), 32, 32);
			Texturas.menu1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("menu1.png"), 640, 480);
			Texturas.menu2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("menu2.png"), 640, 480);
			Texturas.menuFinal = Recurso.crearBitmap(juego.getRecurso().cargarImagen("menuFinal.png"), 640, 480);
			Texturas.misilD1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("misilD1.png"), 16, 8);
			Texturas.misilD2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("misilD2.png"), 16, 8);
			Texturas.misilH1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("misilH1.png"), 16, 8);
			Texturas.misilH2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("misilH2.png"), 16, 8);
			Texturas.misilV1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("misilV1.png"), 16, 8);
			Texturas.misilV2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("misilV2.png"), 16, 8);
			Texturas.nivel1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("nivel1.png"), 640, 480);
			Texturas.poderB = Recurso.crearBitmap(juego.getRecurso().cargarImagen("poderB.png"), 32, 32);
			Texturas.poderL = Recurso.crearBitmap(juego.getRecurso().cargarImagen("poderL.png"), 32, 32);
			Texturas.poderS = Recurso.crearBitmap(juego.getRecurso().cargarImagen("poderS.png"), 32, 32);
			Texturas.poderW = Recurso.crearBitmap(juego.getRecurso().cargarImagen("poderW.png"), 32, 32);
			Texturas.precentacion = Recurso.crearBitmap(juego.getRecurso().cargarImagen("precentacion.png"), 640, 480);
			Texturas.robotD = Recurso.crearBitmap(juego.getRecurso().cargarImagen("robotD.png"), 64, 64);
			Texturas.robotI = Recurso.crearBitmap(juego.getRecurso().cargarImagen("robotI.png"), 64, 64);
			Texturas.saltador1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("saltador1.png"), 48, 64);
			Texturas.saltador2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("saltador2.png"), 48, 64);
			Texturas.saltador3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("saltador3.png"), 48, 64);
			Texturas.saltador4 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("saltador4.png"), 48, 64);
			Texturas.sateliteDD1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("sateliteDD1.png"), 16, 16);
			Texturas.sateliteDD2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("sateliteDD2.png"), 16, 16);
			Texturas.sateliteDI1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("sateliteDI1.png"), 16, 16);
			Texturas.sateliteDI2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("sateliteDI2.png"), 16, 16);
			Texturas.sateliteHD1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("sateliteHD1.png"), 16, 16);
			Texturas.sateliteHD2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("sateliteHD2.png"), 16, 16);
			Texturas.sateliteHI1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("sateliteHI1.png"), 16, 16);
			Texturas.sateliteHI2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("sateliteHI2.png"), 16, 16);
			Texturas.sateliteVA1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("sateliteVA1.png"), 16, 16);
			Texturas.sateliteVA2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("sateliteVA2.png"), 16, 16);
			Texturas.sateliteVB1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("sateliteVB1.png"), 16, 16);
			Texturas.sateliteVB2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("sateliteVB2.png"), 16, 16);
			Texturas.selector1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("selector1.png"), 16, 16);
			Texturas.selector2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("selector2.png"), 16, 16);
			Texturas.vida1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("vida1.png"), 16, 32);
			Texturas.vida2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("vida2.png"), 16, 32);
			Texturas.voladorD1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("voladorD1.png"), 32, 32);
			Texturas.voladorD2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("voladorD2.png"), 32, 32);
			Texturas.voladorD3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("voladorD3.png"), 32, 32);
			Texturas.voladorI1 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("voladorI1.png"), 32, 32);
			Texturas.voladorI2 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("voladorI2.png"), 32, 32);
			Texturas.voladorI3 = Recurso.crearBitmap(juego.getRecurso().cargarImagen("voladorI3.png"), 32, 32);
			
			

			juego.getConfiguracioin().caragarConfiguraciones(juego.getDatos());

			juego.setPantalla(new PantallaMenu(juego));

			ciclo = 0;
		}

	}

	@Override
	public void dibujar(Canvas pincel, float delta) {

		x += 12;

		dibujarImagen(pincel, fondo, 0, 0);

		dibujarImagen(pincel, barra, x, 160);

	}

	@Override
	public void colisiones() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ocultar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void mostrar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reajustarPantalla(int ancho, int alto) {
		// TODO Auto-generated method stub

	}

	@Override
	public void teclaPresionada(KeyEvent ev) {
		// TODO Auto-generated method stub

	}

	@Override
	public void teclaLevantada(KeyEvent ev) {
		// TODO Auto-generated method stub

	}

	@Override
	public void toque(MotionEvent ev) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void multiToque(MotionEvent ev) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acelerometro(SensorEvent ev) {
		// TODO Auto-generated method stub
		
	}

}
