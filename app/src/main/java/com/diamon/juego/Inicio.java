package com.diamon.juego;

import com.diamon.nucleo.Juego;
import com.diamon.publicidad.MostrarPublicidad;
import com.diamon.utilidad.PantallaCompleta;
import com.startapp.sdk.ads.banner.Banner;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class Inicio extends Activity {

	private WakeLock wakeLock;

	private FinalMision juego;

	private PantallaCompleta pantallaCompleta;

	@SuppressWarnings("unused")
	private MostrarPublicidad publicidad;

	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		pantallaCompleta = new PantallaCompleta(this);

		pantallaCompleta.pantallaCompleta();

		pantallaCompleta.ocultarBotonesVirtuales();

		publicidad = new MostrarPublicidad(this);

		boolean isModoHorizontal = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		int imagenBuferAncho = isModoHorizontal ? (int) Juego.ANCHO_PANTALLA : (int) Juego.ALTO_PANTALLA;
		int imagenBuferAlto = isModoHorizontal ? (int) Juego.ALTO_PANTALLA : (int) Juego.ANCHO_PANTALLA;
		Bitmap imagenBufer = Bitmap.createBitmap(imagenBuferAncho, imagenBuferAlto, Config.ARGB_8888);
		float escalaX = (float) imagenBuferAncho / getWindowManager().getDefaultDisplay().getWidth();
		float escalaY = (float) imagenBuferAlto / getWindowManager().getDefaultDisplay().getHeight();

		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		juego = new FinalMision(this, imagenBufer);

		RelativeLayout mainLayout = new RelativeLayout(this);

		FrameLayout frame = new FrameLayout(this);

		Banner baner = new Banner(this);

		baner.setScaleX(0.5f);

		baner.setScaleY(0.5f);

		RelativeLayout.LayoutParams mrecParameters = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		mrecParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mrecParameters.addRule(RelativeLayout.ALIGN_PARENT_TOP);

		mainLayout.addView(baner, mrecParameters);

		frame.addView(juego, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT));

		frame.addView(mainLayout);

		setContentView(frame);

		PowerManager powerManejador = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManejador.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");

	}

	@Override
	protected void onPause() {

		super.onPause();

		juego.pausa();

		wakeLock.release();

		if (isFinishing()) {
			juego.liberarRecursos();

		}

	}

	@Override
	protected void onResume() {

		super.onResume();

		juego.resumen();

		wakeLock.acquire();

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		if (hasFocus) {

			pantallaCompleta.ocultarBotonesVirtuales();
		}

	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();

		System.exit(0);

	}

}
