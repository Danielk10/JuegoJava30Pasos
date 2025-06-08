package com.diamon.juego;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.diamon.nucleo.Juego;
import com.diamon.utilidad.PantallaCompleta;

public class Inicio extends Activity {

    private WakeLock wakeLock;

    private FinalMision juego;

    private PantallaCompleta pantallaCompleta;

    @SuppressLint("InvalidWakeLockTag")
    @SuppressWarnings({"deprecation", "unused"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        pantallaCompleta = new PantallaCompleta(this);

        pantallaCompleta.pantallaCompleta();

        pantallaCompleta.ocultarBotonesVirtuales();

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        boolean isModoHorizontal =
                getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_LANDSCAPE;

        int imagenBuferAncho = 0;

        int imagenBuferAlto = 0;

        if (isModoHorizontal) {

            imagenBuferAncho = (int) Juego.ANCHO_PANTALLA;

            imagenBuferAlto = (int) Juego.ALTO_PANTALLA;

        } else {

            imagenBuferAncho = (int) Juego.ALTO_PANTALLA;

            imagenBuferAlto = (int) Juego.ANCHO_PANTALLA;
        }

        juego = new FinalMision(this, imagenBuferAncho, imagenBuferAlto);

        RelativeLayout mainLayout = new RelativeLayout(this);

        FrameLayout frame = new FrameLayout(this);

        RelativeLayout.LayoutParams mrecParameters =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        mrecParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mrecParameters.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        frame.addView(
                juego,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        pantallaCompleta.ocultarBotonesVirtuales();

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
