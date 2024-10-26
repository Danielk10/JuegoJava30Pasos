package com.diamon.terminos;

import com.diamon.utilidad.PantallaCompleta;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import com.diamon.curso.R;

public class Terminos extends Activity {

    private WebView pagina;

    private PantallaCompleta pantallaCompleta;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pantallaCompleta = new PantallaCompleta(this);

        pantallaCompleta.pantallaCompleta();

        pantallaCompleta.ocultarBotonesVirtuales();

        setContentView(R.layout.terminos);

        pagina = (WebView) findViewById(R.id.terminos);

        pagina.getSettings().setJavaScriptEnabled(true);

        pagina.loadUrl("https://www.e-droid.net/privacy.php?ida=1454194&idl=es");
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
