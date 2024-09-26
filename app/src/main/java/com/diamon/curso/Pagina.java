package com.diamon.curso;

import com.diamon.publicidad.MostrarPublicidad;
import com.diamon.utilidad.PantallaCompleta;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


/**
 * @author Daniel Diamon
 *
 */
public class Pagina extends Activity {

	private WebView pagina;

	private PantallaCompleta pantallaCompleta;
	
	private AdView mAdView;

	@SuppressWarnings("unused")
	private MostrarPublicidad publicidad;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pantallaCompleta = new PantallaCompleta(this);

		pantallaCompleta.pantallaCompleta();

		pantallaCompleta.ocultarBotonesVirtuales();

		publicidad = new MostrarPublicidad(this);

		setContentView(R.layout.activity_pagina_web);
		
		mAdView = findViewById(R.id.adView);
		
		
        AdRequest adRequest = new AdRequest.Builder().build();
		
		
        mAdView.loadAd(adRequest);

		
		pagina = (WebView) findViewById(R.id.pagina);

		pagina.getSettings().setJavaScriptEnabled(true);

		pagina.loadUrl("file:///android_asset/www/index.html");

	}

	@Override
	public void onWindowFocusChanged(boolean focus) {
		super.onWindowFocusChanged(focus);
		if (focus) {

			pantallaCompleta.ocultarBotonesVirtuales();
		}
	}

	@Override
	public void onResume() {

		super.onResume();

	}

	@Override
	public void onPause() {

		super.onPause();

	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();

	}

}
