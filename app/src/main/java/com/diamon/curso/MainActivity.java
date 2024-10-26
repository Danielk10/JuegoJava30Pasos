package com.diamon.curso;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.diamon.juego.Inicio;
import com.diamon.publicidad.MostrarPublicidad;
import com.diamon.terminos.Terminos;
import com.diamon.tutorial.Tutorial;
import com.diamon.utilidad.PantallaCompleta;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class MainActivity extends AppCompatActivity {

    private PantallaCompleta pantallaCompleta;

    private MostrarPublicidad publicidad;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCenter.start(
                getApplication(),
                "cf7ac082-49cd-4cef-bd2d-3f1a3377efa9",
                Analytics.class,
                Crashes.class);

        pantallaCompleta = new PantallaCompleta(this);

        pantallaCompleta.pantallaCompleta();

        pantallaCompleta.ocultarBotonesVirtuales();

        publicidad = new MostrarPublicidad(this);

        publicidad.cargarInterstial();

        // Crear background con bordes redondeados programáticamente
        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setColor(Color.parseColor("#1E1E2C")); // Color de fondo de la tarjeta
        backgroundDrawable.setCornerRadius(30f); // Bordes redondeados

        // Crear Layout principal de la actividad (LinearLayout vertical)
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        mainLayout.setPadding(32, 32, 32, 32);

        GradientDrawable fondoDegradado =
                new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[] {Color.parseColor("#2196F3"), Color.parseColor("#3F51B5")});
        fondoDegradado.setCornerRadius(0f); // Sin bordes redondeados

        mainLayout.setBackground(fondoDegradado);

        // Crear y configurar el Toolbar
        Toolbar toolbar = new Toolbar(this);
        toolbar.setLayoutParams(
                new Toolbar.LayoutParams(
                        Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT));
        // toolbar.setBackground(backgroundDrawable );
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Juego Java 30 Pasos");
        setSupportActionBar(toolbar);

        toolbar.setBackground(
                backgroundDrawable); // Agregar sombras en API 21 o superior (minSdkVersion >= 21)
        toolbar.setElevation(8);

        // Añadir Toolbar al layout principal
        mainLayout.addView(toolbar);

        // Crear Botón para la actividad "Tutorial"
        Button tutorialButton = new Button(this);
        tutorialButton.setText("Tutorial");
        tutorialButton.setTextSize(18);
        tutorialButton.setTextColor(Color.BLACK);
        tutorialButton.setPadding(16, 16, 16, 16);
        tutorialButton.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        tutorialButton.setOnClickListener(
                view -> {
                    publicidad.mostrarInterstitial();

                    Intent nuevaActividad = new Intent(MainActivity.this, Tutorial.class);
                
                    startActivity(nuevaActividad);
                });

        // Crear Botón para la actividad "Demo"
        Button demoButton = new Button(this);
        demoButton.setText("Demo");
        demoButton.setTextSize(18);
        demoButton.setTextColor(Color.BLACK);
        demoButton.setPadding(16, 16, 16, 16);
        demoButton.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        demoButton.setOnClickListener(
                view -> {
                    Intent nuevaActividad = new Intent(MainActivity.this, Inicio.class);
                    startActivity(nuevaActividad);
                });

        // Crear Botón para la actividad "Políticas de Privacidad"
        Button politicasButton = new Button(this);
        politicasButton.setText("Políticas de Privacidad");
        politicasButton.setTextSize(18);
        politicasButton.setTextColor(Color.BLACK);
        politicasButton.setPadding(16, 16, 16, 16);
        politicasButton.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        politicasButton.setOnClickListener(
                view -> {
                    Intent nuevaActividad = new Intent(MainActivity.this, Terminos.class);
                    startActivity(nuevaActividad);
                });

        // Añadir los botones al layout principal
        mainLayout.addView(tutorialButton);
        mainLayout.addView(demoButton);
        mainLayout.addView(politicasButton);

        // Establecer el layout principal como la vista de la actividad
        setContentView(mainLayout);
    }

    @Override
    public void onBackPressed() {

        publicidad.botonAtrasInterstitial();

        super.onBackPressed();
    }
}
