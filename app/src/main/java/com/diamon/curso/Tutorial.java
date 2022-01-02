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

public class Tutorial extends Activity {

	private ImageButton botonTutorial;
	
	private ImageButton botonDemo;
	
	private ImageButton botonTerminos;

	private PantallaCompleta pantallaCompleta;
	
	private MostrarPublicidad publicidad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		AppCenter.start(getApplication(), "cf7ac082-49cd-4cef-bd2d-3f1a3377efa9",
                  Analytics.class, Crashes.class);

		pantallaCompleta = new PantallaCompleta(this);

		pantallaCompleta.pantallaCompleta();

		pantallaCompleta.ocultarBotonesVirtuales();
		
		publicidad = new MostrarPublicidad(this);

		setContentView(R.layout.main);

		 botonTutorial = (ImageButton) findViewById(R.id.boton);
		
		 botonDemo = (ImageButton) findViewById(R.id.boton1);
		
		 botonTerminos = (ImageButton) findViewById(R.id.boton2);

		botonTutorial.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

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
