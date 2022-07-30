package com.diamon.curso;

import com.diamon.juego.Inicio;
import com.diamon.publicidad.MostrarPublicidad;
import com.diamon.utilidad.PantallaCompleta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;


public class Tutorial extends Activity {

	private ImageButton botonTutorial;

	private ImageButton botonDemo;

	private ImageButton botonTerminos;

	private PantallaCompleta pantallaCompleta;

	private MostrarPublicidad publicidad;
	
	private InterstitialAd mInterstitialAd;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		AppCenter.start(getApplication(), "cf7ac082-49cd-4cef-bd2d-3f1a3377efa9", Analytics.class, Crashes.class);

		pantallaCompleta = new PantallaCompleta(this);

		pantallaCompleta.pantallaCompleta();

		pantallaCompleta.ocultarBotonesVirtuales();

		publicidad = new MostrarPublicidad(this);

		setContentView(R.layout.main);
		
		 MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
		
		
		
		
		AdRequest adRequest = new AdRequest.Builder().build();

		   InterstitialAd.load(this,"ca-app-pub-5141499161332805/8275351662", adRequest,
        new InterstitialAdLoadCallback() {
      @Override
      public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
     
        mInterstitialAd = interstitialAd;
       
      }

      @Override
      public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
        
        mInterstitialAd = null;
      }
    });


		botonTutorial = (ImageButton) findViewById(R.id.boton);

		botonDemo = (ImageButton) findViewById(R.id.boton1);

		botonTerminos = (ImageButton) findViewById(R.id.boton2);

		botonTutorial.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (mInterstitialAd != null) {
                 mInterstitialAd.show(Tutorial.this);
                 } else {
  
                  }

				accionBoton();

			}
		});

		botonDemo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				accionBoton1();

			}
		});

		botonTerminos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				accionBoton2();

			}
		});

	}

	public void accionBoton() {
		Intent nuevaActividad = new Intent(Tutorial.this, Pagina.class);
		startActivity(nuevaActividad);

		publicidad.mostrarInterstitial();
	}

	public void accionBoton1() {
		Intent nuevaActividad = new Intent(Tutorial.this, Inicio.class);
		startActivity(nuevaActividad);
	}

	public void accionBoton2() {
		Intent nuevaActividad = new Intent(Tutorial.this, Terminos.class);
		startActivity(nuevaActividad);
	}

	@Override
	public void onBackPressed() {

		publicidad.botonAtrasInterstitial();

		super.onBackPressed();

	}

}
