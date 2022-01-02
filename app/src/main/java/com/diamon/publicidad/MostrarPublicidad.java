package com.diamon.publicidad;

import android.app.Activity;

import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

public class MostrarPublicidad implements Publicidad {

	private Activity actividad;

	public MostrarPublicidad(Activity actividad) {

		this.actividad = actividad;

	}

	public static void IniciarPublicidad(Activity actividad) {

		StartAppSDK.init(actividad, Publicidad.ID_DE_APLICACION, Publicidad.ANUNCIOS_DE_DEVOLUCION);

		StartAppAd.disableSplash();

		StartAppAd.disableAutoInterstitial();

	}

	@Override
	public void mostrarInterstitial() {

		// StartAppAd.disbleAutoInterstitial();

		StartAppAd.showAd(actividad);

		// StartAppAd.enableAutoInterstitial();

	}

	@Override
	public void botonAtrasInterstitial() {
		StartAppAd.onBackPressed(actividad);

	}

}
