package com.diamon.utilidad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class PantallaCompleta {

    private Activity actividad;

    public PantallaCompleta(Activity actividad) {

        this.actividad = actividad;
    }

    @SuppressWarnings("deprecation")
    public void pantallaCompleta() {
        actividad.requestWindowFeature(Window.FEATURE_NO_TITLE);
        actividad
                .getWindow()
                .setFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        actividad.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void ocultarBotonesVirtuales() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            actividad
                    .getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }
}
