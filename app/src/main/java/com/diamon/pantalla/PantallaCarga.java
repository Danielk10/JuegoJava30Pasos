package com.diamon.pantalla;

import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.utilidad.Recurso;
import com.diamon.utilidad.Texturas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class PantallaCarga extends Pantalla {

	private Bitmap fondo;

	private float tiemoMovimiento;

	private float x;

	private Bitmap barra;

	public PantallaCarga(Juego juego) {
		super(juego);

		fondo = this.crearBitmap(recurso.getImagen("introduccion.png"), Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		barra = this.crearBitmap(recurso.getImagen("barraProgreso1.png"), Juego.ANCHO_PANTALLA, 64);

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

		tiemoMovimiento += delta;

		if (tiemoMovimiento / 1 >= 1) {

			/*
			 * recurso.cargarImagen("fondo1.png");
			 * 
			 * recurso.cargarImagen("fondo2.png");
			 * 
			 * recurso.cargarImagen("fondo3.png");
			 * 
			 * recurso.cargarImagen("fondo4.png");
			 * 
			 * recurso.cargarImagen("fondo5.png");
			 * 
			 * recurso.cargarImagen("fondo6.png");
			 * 
			 * recurso.cargarImagen("fondo7.png");
			 * 
			 * recurso.cargarImagen("fondo8.png");
			 * 
			 * recurso.cargarImagen("fondo9.png");
			 * 
			 * recurso.cargarImagen("fondo10.png");
			 * 
			 * recurso.cargarImagen("fondo11.png");
			 * 
			 * recurso.cargarImagen("fondo12.png");
			 * 
			 * recurso.cargarImagen("fondo13.png");
			 * 
			 * recurso.cargarImagen("fondo14.png");
			 * 
			 * recurso.cargarImagen("fondo15.png");
			 * 
			 * recurso.cargarImagen("fondo16.png");
			 * 
			 * recurso.cargarImagen("fondo17.png");
			 * 
			 * recurso.cargarImagen("fondo18.png");
			 * 
			 * recurso.cargarImagen("fondo19.png");
			 * 
			 * recurso.cargarImagen("fondo20.png");
			 * 
			 * recurso.cargarImagen("fondo21MGF.png");
			 * 
			 * recurso.cargarImagen("maquinaFinal.png");
			 * 
			 * recurso.cargarImagen("fondoIntroduccion1.png");
			 * 
			 * recurso.cargarImagen("fondoIntroduccion2.png");
			 * 
			 * recurso.cargarImagen("fondoIntroduccion3.png");
			 * 
			 * recurso.cargarImagen("menu1.png");
			 * 
			 * recurso.cargarImagen("menu2.png");
			 * 
			 * recurso.cargarImagen("menuFinal.png");
			 * 
			 * recurso.cargarImagen("creditos.png");
			 * 
			 * recurso.cargarImagen("continuar.png");
			 * 
			 * recurso.cargarImagen("nivel1.png");
			 * 
			 * recurso.cargarImagen("finJuego.png");
			 * 
			 * recurso.cargarImagen("finNivel.png");
			 * 
			 * recurso.cargarImagen("jugador1D1.png");
			 * 
			 * recurso.cargarImagen("jugador1D2.png");
			 * 
			 * recurso.cargarImagen("jugador1D3.png");
			 * 
			 * recurso.cargarImagen("jugador1D4.png");
			 * 
			 * recurso.cargarImagen("jugador1D5.png");
			 * 
			 * recurso.cargarImagen("jugador1D6.png");
			 * 
			 * recurso.cargarImagen("jugador1I1.png");
			 * 
			 * recurso.cargarImagen("jugador1I2.png");
			 * 
			 * recurso.cargarImagen("jugador1I3.png");
			 * 
			 * recurso.cargarImagen("jugador2D1.png");
			 * 
			 * recurso.cargarImagen("jugador2D2.png");
			 * 
			 * recurso.cargarImagen("jugador2D3.png");
			 * 
			 * recurso.cargarImagen("jugador2D4.png");
			 * 
			 * recurso.cargarImagen("jugador2I1.png");
			 * 
			 * recurso.cargarImagen("humoMisil1.png");
			 * 
			 * recurso.cargarImagen("humoMisil2.png");
			 * 
			 * recurso.cargarImagen("humoMisil3.png");
			 * 
			 * recurso.cargarImagen("lanzaMisilI1.png");
			 * 
			 * recurso.cargarImagen("lanzaMisilI2.png");
			 * 
			 * recurso.cargarImagen("lanzaMisilI3.png");
			 * 
			 * recurso.cargarImagen("lanzaMisilD1.png");
			 * 
			 * recurso.cargarImagen("lanzaMisilD2.png");
			 * 
			 * recurso.cargarImagen("lanzaMisilD3.png");
			 * 
			 * recurso.cargarImagen("maquinaParedI1.png");
			 * 
			 * recurso.cargarImagen("maquinaParedI2.png");
			 * 
			 * recurso.cargarImagen("maquinaParedD1.png");
			 * 
			 * recurso.cargarImagen("maquinaParedD2.png");
			 * 
			 * recurso.cargarImagen("poderB.png");
			 * 
			 * recurso.cargarImagen("poderL.png");
			 * 
			 * recurso.cargarImagen("poderS.png");
			 * 
			 * recurso.cargarImagen("poderW.png");
			 * 
			 * recurso.cargarImagen("robotD.png");
			 * 
			 * recurso.cargarImagen("robotI.png");
			 * 
			 * recurso.cargarImagen("saltador1.png");
			 * 
			 * recurso.cargarImagen("saltador2.png");
			 * 
			 * recurso.cargarImagen("saltador3.png");
			 * 
			 * recurso.cargarImagen("saltador4.png");
			 * 
			 * recurso.cargarImagen("sateliteDD1.png");
			 * 
			 * recurso.cargarImagen("sateliteDD2.png");
			 * 
			 * recurso.cargarImagen("sateliteDI1.png");
			 * 
			 * recurso.cargarImagen("sateliteDI2.png");
			 * 
			 * recurso.cargarImagen("sateliteHD1.png");
			 * 
			 * recurso.cargarImagen("sateliteHD2.png");
			 * 
			 * recurso.cargarImagen("sateliteHI1.png");
			 * 
			 * recurso.cargarImagen("sateliteHI2.png");
			 * 
			 * recurso.cargarImagen("sateliteVA1.png");
			 * 
			 * recurso.cargarImagen("sateliteVA2.png");
			 * 
			 * recurso.cargarImagen("sateliteVB1.png");
			 * 
			 * recurso.cargarImagen("sateliteVB2.png");
			 * 
			 * recurso.cargarImagen("selector1.png");
			 * 
			 * recurso.cargarImagen("selector2.png");
			 * 
			 * recurso.cargarImagen("vida1.png");
			 * 
			 * recurso.cargarImagen("vida2.png");
			 * 
			 * recurso.cargarImagen("voladorI1.png");
			 * 
			 * recurso.cargarImagen("voladorI2.png");
			 * 
			 * recurso.cargarImagen("voladorI3.png");
			 * 
			 * recurso.cargarImagen("voladorD1.png");
			 * 
			 * recurso.cargarImagen("voladorD2.png");
			 * 
			 * recurso.cargarImagen("voladorD3.png");
			 * 
			 * recurso.cargarImagen("antiAreoD1.png");
			 * 
			 * recurso.cargarImagen("antiAreoH1.png");
			 * 
			 * recurso.cargarImagen("antiAreoV1.png");
			 * 
			 * recurso.cargarImagen("antiAreoD2.png");
			 * 
			 * recurso.cargarImagen("antiAreoH2.png");
			 * 
			 * recurso.cargarImagen("antiAreoV2.png");
			 * 
			 * recurso.cargarImagen("antiAreoEspecialD.png");
			 * 
			 * recurso.cargarImagen("antiAreoEspecialH.png");
			 * 
			 * recurso.cargarImagen("antiAreoEspecialV1.png");
			 * 
			 * recurso.cargarImagen("antiAreoEspecialV2.png");
			 * 
			 * recurso.cargarImagen("antiAreoEspecialV3.png");
			 * 
			 * recurso.cargarImagen("antiAreoEspecialV4.png");
			 * 
			 * recurso.cargarImagen("antiAreoEspecialV5.png");
			 * 
			 * recurso.cargarImagen("antiAreoEspecialV6.png");
			 * 
			 * recurso.cargarImagen("antiAreoEspecialV7.png");
			 * 
			 * recurso.cargarImagen("antiAreoEspecialV8.png");
			 * 
			 * recurso.cargarImagen("antiAreoEspecialV9.png");
			 * 
			 * recurso.cargarImagen("antiAreoEspecialV10.png");
			 * 
			 * recurso.cargarImagen("antiAreoEspecialV11.png");
			 * 
			 * recurso.cargarImagen("antiAreoEspecialV12.png");
			 * 
			 * recurso.cargarImagen("antiAreoEspecialV13.png");
			 * 
			 * recurso.cargarImagen("antiAreoEspecialV14.png");
			 * 
			 * recurso.cargarImagen("misilD1.png");
			 * 
			 * recurso.cargarImagen("misilH1.png");
			 * 
			 * recurso.cargarImagen("misilV1.png");
			 * 
			 * recurso.cargarImagen("misilD2.png");
			 * 
			 * recurso.cargarImagen("misilH2.png");
			 * 
			 * recurso.cargarImagen("misilV2.png");
			 * 
			 * recurso.cargarImagen("balaBD.png");
			 * 
			 * recurso.cargarImagen("balaBI.png");
			 * 
			 * recurso.cargarImagen("balaE1.png");
			 * 
			 * recurso.cargarImagen("balaE2.png");
			 * 
			 * recurso.cargarImagen("balaE3.png");
			 * 
			 * recurso.cargarImagen("balaE4.png");
			 * 
			 * recurso.cargarImagen("balaHD.png");
			 * 
			 * recurso.cargarImagen("balaHI.png");
			 * 
			 * recurso.cargarImagen("balaLD1.png");
			 * 
			 * recurso.cargarImagen("balaLD2.png");
			 * 
			 * recurso.cargarImagen("balaLD3.png");
			 * 
			 * recurso.cargarImagen("balaLI1.png");
			 * 
			 * recurso.cargarImagen("balaLI2.png");
			 * 
			 * recurso.cargarImagen("balaLI3.png");
			 * 
			 * recurso.cargarImagen("balaParedI.png");
			 * 
			 * recurso.cargarImagen("balaParedD.png");
			 * 
			 * recurso.cargarImagen("balaSaltador1.png");
			 * 
			 * recurso.cargarImagen("balaSaltador2.png");
			 * 
			 * recurso.cargarImagen("balaSatelite.png");
			 * 
			 * recurso.cargarImagen("balaWD1.png");
			 * 
			 * recurso.cargarImagen("balaWD2.png");
			 * 
			 * recurso.cargarImagen("balaWD3.png");
			 * 
			 * recurso.cargarImagen("balaWI1.png");
			 * 
			 * recurso.cargarImagen("balaWI2.png");
			 * 
			 * recurso.cargarImagen("balaWI3.png");
			 * 
			 * recurso.cargarImagen("bola1.png");
			 * 
			 * recurso.cargarImagen("bola2.png");
			 * 
			 * recurso.cargarImagen("bola3.png");
			 * 
			 * recurso.cargarImagen("bola4.png");
			 * 
			 * recurso.cargarImagen("bola5.png");
			 * 
			 * recurso.cargarImagen("bola6.png");
			 * 
			 * recurso.cargarImagen("bola7.png");
			 * 
			 * recurso.cargarImagen("bola8.png");
			 * 
			 * recurso.cargarImagen("bola9.png");
			 * 
			 * recurso.cargarImagen("bola10.png");
			 * 
			 * recurso.cargarImagen("bola11.png");
			 * 
			 * recurso.cargarImagen("bola12.png");
			 * 
			 * recurso.cargarImagen("bola13.png");
			 * 
			 * recurso.cargarImagen("bola14.png");
			 * 
			 * recurso.cargarImagen("bola15.png");
			 * 
			 * recurso.cargarImagen("bola16.png");
			 * 
			 * recurso.cargarImagen("cajaPoder1.png");
			 * 
			 * recurso.cargarImagen("cajaPoder2.png");
			 * 
			 * recurso.cargarImagen("cajaPoder3.png");
			 * 
			 * recurso.cargarImagen("cajaPoder4.png");
			 * 
			 * recurso.cargarImagen("explosion1.png");
			 * 
			 * recurso.cargarImagen("explosion2.png");
			 * 
			 * recurso.cargarImagen("explosion3.png");
			 * 
			 * recurso.cargarImagen("explosion4.png");
			 * 
			 * recurso.cargarImagen("explosionB1.png");
			 * 
			 * recurso.cargarImagen("explosionB2.png");
			 * 
			 * recurso.cargarImagen("explosionB3.png");
			 * 
			 * recurso.cargarImagen("explosionB4.png");
			 * 
			 * recurso.cargarImagen("explosionB5.png");
			 * 
			 * recurso.cargarImagen("explosionMisil1.png");
			 * 
			 * recurso.cargarImagen("explosionMisil2.png");
			 * 
			 * recurso.cargarImagen("explosionMisil3.png");
			 * 
			 * recurso.cargarImagen("explosionMisil4.png");
			 */

			recurso.cargarMusica("musica.wav");

			recurso.cargarMusica("menu.wav");

			recurso.cargarMusica("precentacion1.wav");

			recurso.cargarMusica("comienzo1.wav");

			recurso.cargarMusica("disparo.wav");

			recurso.cargarMusica("disparoL.wav");

			recurso.cargarMusica("poder.wav");

			recurso.cargarMusica("introduccion.wav");

			recurso.cargarMusica("finDeJuego.wav");

			recurso.cargarMusica("muriendo.wav");

			recurso.cargarMusica("explosion.wav");

			recurso.cargarMusica("pausa.wav");

			Texturas.antiAreoD1 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoD1.png"), 32, 32);
			Texturas.antiAreoD2 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoD2.png"), 32, 32);
			Texturas.antiAreoEspecialD = Recurso.crearBitmap(recurso.cargarImagen("antiAreoEspecialD.png"), 16, 16);
			Texturas.antiAreoEspecialH = Recurso.crearBitmap(recurso.cargarImagen("antiAreoEspecialH.png"), 16, 16);
			Texturas.antiAreoEspecialV1 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoEspecialV1.png"), 16, 16);
			Texturas.antiAreoEspecialV2 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoEspecialV2.png"), 16, 16);
			Texturas.antiAreoEspecialV3 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoEspecialV3.png"), 16, 16);
			Texturas.antiAreoEspecialV4 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoEspecialV4.png"), 16, 16);
			Texturas.antiAreoEspecialV5 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoEspecialV5.png"), 16, 16);
			Texturas.antiAreoEspecialV6 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoEspecialV6.png"), 16, 16);
			Texturas.antiAreoEspecialV7 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoEspecialV7.png"), 16, 16);
			Texturas.antiAreoEspecialV8 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoEspecialV8.png"), 16, 16);
			Texturas.antiAreoEspecialV9 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoEspecialV9.png"), 16, 16);
			Texturas.antiAreoEspecialV10 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoEspecialV10.png"), 16, 16);
			Texturas.antiAreoEspecialV11 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoEspecialV11.png"), 16, 16);
			Texturas.antiAreoEspecialV12 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoEspecialV12.png"), 16, 16);
			Texturas.antiAreoEspecialV13 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoEspecialV13.png"), 16, 16);
			Texturas.antiAreoEspecialV14 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoEspecialV14.png"), 16, 16);

			Texturas.antiAreoH1 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoH1.png"), 32, 32);
			Texturas.antiAreoH2 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoH2.png"), 32, 32);
			Texturas.antiAreoV1 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoV1.png"), 32, 32);
			Texturas.antiAreoV2 = Recurso.crearBitmap(recurso.cargarImagen("antiAreoV2.png"), 32, 32);
			Texturas.ayuda = Recurso.crearBitmap(recurso.cargarImagen("ayuda.png"), 640, 480);
			Texturas.balaBD = Recurso.crearBitmap(recurso.cargarImagen("balaBD.png"), 12, 12);
			Texturas.balaBI = Recurso.crearBitmap(recurso.cargarImagen("balaBI.png"), 12, 12);
			Texturas.balaE1 = Recurso.crearBitmap(recurso.cargarImagen("balaE1.png"), 12, 12);
			Texturas.balaE2 = Recurso.crearBitmap(recurso.cargarImagen("balaE2.png"), 12, 12);
			Texturas.balaE3 = Recurso.crearBitmap(recurso.cargarImagen("balaE3.png"), 12, 12);
			Texturas.balaE4 = Recurso.crearBitmap(recurso.cargarImagen("balaE4.png"), 12, 12);

			Texturas.balaHD = Recurso.crearBitmap(recurso.cargarImagen("balaHD.png"), 16, 4);
			Texturas.balaHI = Recurso.crearBitmap(recurso.cargarImagen("balaHI.png"), 16, 4);
			Texturas.balaLD1 = Recurso.crearBitmap(recurso.cargarImagen("balaLD1.png"), 256, 4);
			Texturas.balaLD2 = Recurso.crearBitmap(recurso.cargarImagen("balaLD2.png"), 256, 4);
			Texturas.balaLD3 = Recurso.crearBitmap(recurso.cargarImagen("balaLD3.png"), 256, 4);
			Texturas.balaLI1 = Recurso.crearBitmap(recurso.cargarImagen("balaLI1.png"), 256, 4);
			Texturas.balaLI2 = Recurso.crearBitmap(recurso.cargarImagen("balaLI2.png"), 256, 4);
			Texturas.balaLI3 = Recurso.crearBitmap(recurso.cargarImagen("balaLI3.png"), 256, 4);
			Texturas.balaParedD = Recurso.crearBitmap(recurso.cargarImagen("balaParedD.png"), 32, 12);
			Texturas.balaParedI = Recurso.crearBitmap(recurso.cargarImagen("balaParedI.png"), 32, 12);
			Texturas.balaSaltador1 = Recurso.crearBitmap(recurso.cargarImagen("balaSaltador1.png"), 32, 32);
			Texturas.balaSaltador2 = Recurso.crearBitmap(recurso.cargarImagen("balaSaltador2.png"), 32, 32);
			Texturas.balaSatelite = Recurso.crearBitmap(recurso.cargarImagen("balaSatelite.png"), 8, 8);
			Texturas.balaWD1 = Recurso.crearBitmap(recurso.cargarImagen("balaWD1.png"), 16, 64);
			Texturas.balaWD2 = Recurso.crearBitmap(recurso.cargarImagen("balaWD2.png"), 16, 64);
			Texturas.balaWD3 = Recurso.crearBitmap(recurso.cargarImagen("balaWD3.png"), 16, 64);
			Texturas.balaWI1 = Recurso.crearBitmap(recurso.cargarImagen("balaWI1.png"), 16, 64);
			Texturas.balaWI2 = Recurso.crearBitmap(recurso.cargarImagen("balaWI2.png"), 16, 64);
			Texturas.balaWI3 = Recurso.crearBitmap(recurso.cargarImagen("balaWI3.png"), 16, 64);
			Texturas.barraProgreso = Recurso.crearBitmap(recurso.cargarImagen("barraProgreso.png"), 16, 16);
			Texturas.barraProgreso1 = Recurso.crearBitmap(recurso.cargarImagen("barraProgreso1.png"), 640, 64);
			Texturas.bola1 = Recurso.crearBitmap(recurso.cargarImagen("bola1.png"), 16, 16);
			Texturas.bola2 = Recurso.crearBitmap(recurso.cargarImagen("bola2.png"), 16, 16);
			Texturas.bola3 = Recurso.crearBitmap(recurso.cargarImagen("bola3.png"), 16, 16);
			Texturas.bola4 = Recurso.crearBitmap(recurso.cargarImagen("bola4.png"), 16, 16);
			Texturas.bola5 = Recurso.crearBitmap(recurso.cargarImagen("bola5.png"), 16, 16);
			Texturas.bola6 = Recurso.crearBitmap(recurso.cargarImagen("bola6.png"), 16, 16);
			Texturas.bola7 = Recurso.crearBitmap(recurso.cargarImagen("bola7.png"), 16, 16);
			Texturas.bola8 = Recurso.crearBitmap(recurso.cargarImagen("bola8.png"), 16, 16);
			Texturas.bola9 = Recurso.crearBitmap(recurso.cargarImagen("bola9.png"), 16, 16);
			Texturas.bola10 = Recurso.crearBitmap(recurso.cargarImagen("bola10.png"), 16, 16);
			Texturas.bola11 = Recurso.crearBitmap(recurso.cargarImagen("bola11.png"), 16, 16);
			Texturas.bola12 = Recurso.crearBitmap(recurso.cargarImagen("bola12.png"), 16, 16);
			Texturas.bola13 = Recurso.crearBitmap(recurso.cargarImagen("bola13.png"), 16, 16);
			Texturas.bola14 = Recurso.crearBitmap(recurso.cargarImagen("bola14.png"), 16, 16);
			Texturas.bola15 = Recurso.crearBitmap(recurso.cargarImagen("bola15.png"), 16, 16);
			Texturas.bola16 = Recurso.crearBitmap(recurso.cargarImagen("bola16.png"), 16, 16);
			Texturas.cajaPoder1 = Recurso.crearBitmap(recurso.cargarImagen("cajaPoder1.png"), 32, 32);
			Texturas.cajaPoder2 = Recurso.crearBitmap(recurso.cargarImagen("cajaPoder2.png"), 32, 32);
			Texturas.cajaPoder3 = Recurso.crearBitmap(recurso.cargarImagen("cajaPoder3.png"), 32, 32);
			Texturas.cajaPoder4 = Recurso.crearBitmap(recurso.cargarImagen("cajaPoder4.png"), 32, 32);
			Texturas.continuar = Recurso.crearBitmap(recurso.cargarImagen("continuar.png"), 640, 480);
			Texturas.creditos = Recurso.crearBitmap(recurso.cargarImagen("creditos.png"), 640, 640);
			Texturas.explosion1 = Recurso.crearBitmap(recurso.cargarImagen("explosion1.png"), 64, 64);
			Texturas.explosion2 = Recurso.crearBitmap(recurso.cargarImagen("explosion2.png"), 64, 64);
			Texturas.explosion3 = Recurso.crearBitmap(recurso.cargarImagen("explosion3.png"), 64, 64);
			Texturas.explosion4 = Recurso.crearBitmap(recurso.cargarImagen("explosion4.png"), 64, 64);
			Texturas.explosionB1 = Recurso.crearBitmap(recurso.cargarImagen("explosionB1.png"), 64, 64);
			Texturas.explosionB2 = Recurso.crearBitmap(recurso.cargarImagen("explosionB2.png"), 64, 64);
			Texturas.explosionB3 = Recurso.crearBitmap(recurso.cargarImagen("explosionB3.png"), 64, 64);
			Texturas.explosionB4 = Recurso.crearBitmap(recurso.cargarImagen("explosionB4.png"), 64, 64);
			Texturas.explosionB5 = Recurso.crearBitmap(recurso.cargarImagen("explosionB5.png"), 64, 64);
			Texturas.explosionMisil1 = Recurso.crearBitmap(recurso.cargarImagen("explosionMisil1.png"), 32, 32);
			Texturas.explosionMisil2 = Recurso.crearBitmap(recurso.cargarImagen("explosionMisil2.png"), 32, 32);
			Texturas.explosionMisil3 = Recurso.crearBitmap(recurso.cargarImagen("explosionMisil3.png"), 32, 32);
			Texturas.explosionMisil4 = Recurso.crearBitmap(recurso.cargarImagen("explosionMisil4.png"), 32, 32);
			Texturas.finJuego = Recurso.crearBitmap(recurso.cargarImagen("finJuego.png"), 640, 480);
			Texturas.finNivel = Recurso.crearBitmap(recurso.cargarImagen("finNivel.png"), 640, 480);
			Texturas.fondo1 = Recurso.crearBitmap(recurso.cargarImagen("fondo1.png"), 640, 480);
			Texturas.fondo2 = Recurso.crearBitmap(recurso.cargarImagen("fondo2.png"), 640, 480);
			Texturas.fondo3 = Recurso.crearBitmap(recurso.cargarImagen("fondo3.png"), 640, 480);
			Texturas.fondo4 = Recurso.crearBitmap(recurso.cargarImagen("fondo4.png"), 640, 480);
			Texturas.fondo5 = Recurso.crearBitmap(recurso.cargarImagen("fondo5.png"), 640, 480);
			Texturas.fondo6 = Recurso.crearBitmap(recurso.cargarImagen("fondo6.png"), 640, 480);
			Texturas.fondo7 = Recurso.crearBitmap(recurso.cargarImagen("fondo7.png"), 640, 480);
			Texturas.fondo8 = Recurso.crearBitmap(recurso.cargarImagen("fondo8.png"), 640, 480);
			Texturas.fondo9 = Recurso.crearBitmap(recurso.cargarImagen("fondo9.png"), 640, 480);
			Texturas.fondo10 = Recurso.crearBitmap(recurso.cargarImagen("fondo10.png"), 640, 480);
			Texturas.fondo11 = Recurso.crearBitmap(recurso.cargarImagen("fondo11.png"), 640, 480);
			Texturas.fondo12 = Recurso.crearBitmap(recurso.cargarImagen("fondo12.png"), 640, 480);
			Texturas.fondo13 = Recurso.crearBitmap(recurso.cargarImagen("fondo13.png"), 640, 480);
			Texturas.fondo14 = Recurso.crearBitmap(recurso.cargarImagen("fondo14.png"), 640, 480);
			Texturas.fondo15 = Recurso.crearBitmap(recurso.cargarImagen("fondo15.png"), 640, 480);
			Texturas.fondo16 = Recurso.crearBitmap(recurso.cargarImagen("fondo16.png"), 640, 480);
			Texturas.fondo17 = Recurso.crearBitmap(recurso.cargarImagen("fondo17.png"), 640, 480);
			Texturas.fondo18 = Recurso.crearBitmap(recurso.cargarImagen("fondo18.png"), 640, 480);
			Texturas.fondo19 = Recurso.crearBitmap(recurso.cargarImagen("fondo19.png"), 640, 480);
			Texturas.fondo20 = Recurso.crearBitmap(recurso.cargarImagen("fondo20.png"), 640, 480);
			Texturas.fondo21MGF = Recurso.crearBitmap(recurso.cargarImagen("fondo21MGF.png"), 640, 480);
			Texturas.fondoIntroduccion1 = Recurso.crearBitmap(recurso.cargarImagen("fondoIntroduccion1.png"), 640, 480);
			Texturas.fondoIntroduccion2 = Recurso.crearBitmap(recurso.cargarImagen("fondoIntroduccion2.png"), 640, 480);
			Texturas.fondoIntroduccion3 = Recurso.crearBitmap(recurso.cargarImagen("fondoIntroduccion3.png"), 640, 480);
			Texturas.humoMisil1 = Recurso.crearBitmap(recurso.cargarImagen("humoMisil1.png"), 16, 16);
			Texturas.humoMisil2 = Recurso.crearBitmap(recurso.cargarImagen("humoMisil2.png"), 16, 16);
			Texturas.humoMisil3 = Recurso.crearBitmap(recurso.cargarImagen("humoMisil3.png"), 16, 16);
			Texturas.introduccion = Recurso.crearBitmap(recurso.cargarImagen("introduccion.png"), 640, 480);
			Texturas.jugador1D1 = Recurso.crearBitmap(recurso.cargarImagen("jugador1D1.png"), 64, 64);
			Texturas.jugador1D2 = Recurso.crearBitmap(recurso.cargarImagen("jugador1D2.png"), 64, 64);
			Texturas.jugador1D3 = Recurso.crearBitmap(recurso.cargarImagen("jugador1D3.png"), 64, 64);
			Texturas.jugador1D4 = Recurso.crearBitmap(recurso.cargarImagen("jugador1D4.png"), 64, 64);
			Texturas.jugador1D5 = Recurso.crearBitmap(recurso.cargarImagen("jugador1D5.png"), 64, 64);
			Texturas.jugador1D6 = Recurso.crearBitmap(recurso.cargarImagen("jugador1D6.png"), 64, 64);
			Texturas.jugador1I1 = Recurso.crearBitmap(recurso.cargarImagen("jugador1I1.png"), 64, 64);
			Texturas.jugador1I2 = Recurso.crearBitmap(recurso.cargarImagen("jugador1I2.png"), 64, 64);
			Texturas.jugador1I3 = Recurso.crearBitmap(recurso.cargarImagen("jugador1I3.png"), 64, 64);
			Texturas.jugador2D1 = Recurso.crearBitmap(recurso.cargarImagen("jugador2D1.png"), 64, 64);
			Texturas.jugador2D2 = Recurso.crearBitmap(recurso.cargarImagen("jugador2D2.png"), 64, 64);
			Texturas.jugador2D3 = Recurso.crearBitmap(recurso.cargarImagen("jugador2D3.png"), 64, 64);
			Texturas.jugador2D4 = Recurso.crearBitmap(recurso.cargarImagen("jugador2D4.png"), 64, 64);
			Texturas.jugador2I1 = Recurso.crearBitmap(recurso.cargarImagen("jugador2I1.png"), 64, 64);
			Texturas.lanzaMisilD1 = Recurso.crearBitmap(recurso.cargarImagen("lanzaMisilD1.png"), 48, 32);
			Texturas.lanzaMisilD2 = Recurso.crearBitmap(recurso.cargarImagen("lanzaMisilD2.png"), 48, 32);
			Texturas.lanzaMisilD3 = Recurso.crearBitmap(recurso.cargarImagen("lanzaMisilD3.png"), 48, 32);
			Texturas.lanzaMisilI1 = Recurso.crearBitmap(recurso.cargarImagen("lanzaMisilI1.png"), 48, 32);
			Texturas.lanzaMisilI2 = Recurso.crearBitmap(recurso.cargarImagen("lanzaMisilI2.png"), 48, 32);
			Texturas.lanzaMisilI3 = Recurso.crearBitmap(recurso.cargarImagen("lanzaMisilI3.png"), 48, 32);
			Texturas.maquinaFinal = Recurso.crearBitmap(recurso.cargarImagen("maquinaFinal.png"), 64, 64);
			Texturas.maquinaParedD1 = Recurso.crearBitmap(recurso.cargarImagen("maquinaParedD1.png"), 32, 32);
			Texturas.maquinaParedD2 = Recurso.crearBitmap(recurso.cargarImagen("maquinaParedD2.png"), 32, 32);
			Texturas.maquinaParedI1 = Recurso.crearBitmap(recurso.cargarImagen("maquinaParedI1.png"), 32, 32);
			Texturas.maquinaParedI2 = Recurso.crearBitmap(recurso.cargarImagen("maquinaParedI2.png"), 32, 32);
			Texturas.menu1 = Recurso.crearBitmap(recurso.cargarImagen("menu1.png"), 640, 480);
			Texturas.menu2 = Recurso.crearBitmap(recurso.cargarImagen("menu2.png"), 640, 480);
			Texturas.menuFinal = Recurso.crearBitmap(recurso.cargarImagen("menuFinal.png"), 640, 480);
			Texturas.misilD1 = Recurso.crearBitmap(recurso.cargarImagen("misilD1.png"), 16, 8);
			Texturas.misilD2 = Recurso.crearBitmap(recurso.cargarImagen("misilD2.png"), 16, 8);
			Texturas.misilH1 = Recurso.crearBitmap(recurso.cargarImagen("misilH1.png"), 16, 8);
			Texturas.misilH2 = Recurso.crearBitmap(recurso.cargarImagen("misilH2.png"), 16, 8);
			Texturas.misilV1 = Recurso.crearBitmap(recurso.cargarImagen("misilV1.png"), 16, 8);
			Texturas.misilV2 = Recurso.crearBitmap(recurso.cargarImagen("misilV2.png"), 16, 8);
			Texturas.nivel1 = Recurso.crearBitmap(recurso.cargarImagen("nivel1.png"), 640, 480);
			Texturas.poderB = Recurso.crearBitmap(recurso.cargarImagen("poderB.png"), 32, 32);
			Texturas.poderL = Recurso.crearBitmap(recurso.cargarImagen("poderL.png"), 32, 32);
			Texturas.poderS = Recurso.crearBitmap(recurso.cargarImagen("poderS.png"), 32, 32);
			Texturas.poderW = Recurso.crearBitmap(recurso.cargarImagen("poderW.png"), 32, 32);
			Texturas.precentacion = Recurso.crearBitmap(recurso.cargarImagen("precentacion.png"), 640, 480);
			Texturas.robotD = Recurso.crearBitmap(recurso.cargarImagen("robotD.png"), 64, 64);
			Texturas.robotI = Recurso.crearBitmap(recurso.cargarImagen("robotI.png"), 64, 64);
			Texturas.saltador1 = Recurso.crearBitmap(recurso.cargarImagen("saltador1.png"), 48, 64);
			Texturas.saltador2 = Recurso.crearBitmap(recurso.cargarImagen("saltador2.png"), 48, 64);
			Texturas.saltador3 = Recurso.crearBitmap(recurso.cargarImagen("saltador3.png"), 48, 64);
			Texturas.saltador4 = Recurso.crearBitmap(recurso.cargarImagen("saltador4.png"), 48, 64);
			Texturas.sateliteDD1 = Recurso.crearBitmap(recurso.cargarImagen("sateliteDD1.png"), 16, 16);
			Texturas.sateliteDD2 = Recurso.crearBitmap(recurso.cargarImagen("sateliteDD2.png"), 16, 16);
			Texturas.sateliteDI1 = Recurso.crearBitmap(recurso.cargarImagen("sateliteDI1.png"), 16, 16);
			Texturas.sateliteDI2 = Recurso.crearBitmap(recurso.cargarImagen("sateliteDI2.png"), 16, 16);
			Texturas.sateliteHD1 = Recurso.crearBitmap(recurso.cargarImagen("sateliteHD1.png"), 16, 16);
			Texturas.sateliteHD2 = Recurso.crearBitmap(recurso.cargarImagen("sateliteHD2.png"), 16, 16);
			Texturas.sateliteHI1 = Recurso.crearBitmap(recurso.cargarImagen("sateliteHI1.png"), 16, 16);
			Texturas.sateliteHI2 = Recurso.crearBitmap(recurso.cargarImagen("sateliteHI2.png"), 16, 16);
			Texturas.sateliteVA1 = Recurso.crearBitmap(recurso.cargarImagen("sateliteVA1.png"), 16, 16);
			Texturas.sateliteVA2 = Recurso.crearBitmap(recurso.cargarImagen("sateliteVA2.png"), 16, 16);
			Texturas.sateliteVB1 = Recurso.crearBitmap(recurso.cargarImagen("sateliteVB1.png"), 16, 16);
			Texturas.sateliteVB2 = Recurso.crearBitmap(recurso.cargarImagen("sateliteVB2.png"), 16, 16);
			Texturas.selector1 = Recurso.crearBitmap(recurso.cargarImagen("selector1.png"), 16, 16);
			Texturas.selector2 = Recurso.crearBitmap(recurso.cargarImagen("selector2.png"), 16, 16);
			Texturas.vida1 = Recurso.crearBitmap(recurso.cargarImagen("vida1.png"), 16, 32);
			Texturas.vida2 = Recurso.crearBitmap(recurso.cargarImagen("vida2.png"), 16, 32);
			Texturas.voladorD1 = Recurso.crearBitmap(recurso.cargarImagen("voladorD1.png"), 32, 32);
			Texturas.voladorD2 = Recurso.crearBitmap(recurso.cargarImagen("voladorD2.png"), 32, 32);
			Texturas.voladorD3 = Recurso.crearBitmap(recurso.cargarImagen("voladorD3.png"), 32, 32);
			Texturas.voladorI1 = Recurso.crearBitmap(recurso.cargarImagen("voladorI1.png"), 32, 32);
			Texturas.voladorI2 = Recurso.crearBitmap(recurso.cargarImagen("voladorI2.png"), 32, 32);
			Texturas.voladorI3 = Recurso.crearBitmap(recurso.cargarImagen("voladorI3.png"), 32, 32);

			juego.setPantalla(new PantallaMenu(juego));

			tiemoMovimiento = 0;
		}

	}

	@Override
	public void dibujar(Canvas pincel, float delta) {

		x += 12 / Juego.DELTA_A_PIXEL * delta;

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

	@Override
	public void reajustarPantalla(float ancho, float alto) {
		// TODO Auto-generated method stub

	}

}
