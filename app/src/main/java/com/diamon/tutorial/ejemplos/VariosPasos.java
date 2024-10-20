package com.diamon.tutorial.ejemplos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.diamon.graficos.Pantalla2D;
import com.diamon.nucleo.Graficos;
import com.diamon.nucleo.Juego;
import com.diamon.tutorial.base.Vista;

public class VariosPasos extends Juego {

    private PanatallaMuretra pantalla;

    public VariosPasos(AppCompatActivity actividad) {
        super(actividad);

        actividad.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        this.setMostrarFPS(false);

        pantalla = new PanatallaMuretra(this);

        setPantalla(pantalla);
    }

    public void reiniciar() {

        pantalla.reiniciar();
    }

    class PanatallaMuretra extends Pantalla2D {

        private int x;

        public PanatallaMuretra(Juego juego) {
            super(juego);
            x = 0;
        }

        @Override
        public void mostrar() {}

        @Override
        public void resume() {}

        @Override
        public void colisiones() {}

        @Override
        public void actualizar(float delta) {}

        @Override
        public void dibujar(Graficos pincel, float delta) {

            pincel.limpiar(Color.LTGRAY);

            x++;

            pincel.dibujarRectangulo(x, 50, 100, 100, Color.BLUE);
        }

        public void reiniciar() {
            x = 0;
        }

        @Override
        public void reajustarPantalla(int ancho, int alto) {}

        @Override
        public void pausa() {}

        @Override
        public void ocultar() {}

        @Override
        public void liberarRecursos() {}

        @Override
        public void teclaPresionada(int codigoDeTecla) {}

        @Override
        public void teclaLevantada(int codigoDeTecla) {}

        @Override
        public void toquePresionado(float x, float y, int puntero) {}

        @Override
        public void toqueLevantado(float x, float y, int puntero) {}

        @Override
        public void toqueDeslizando(float x, float y, int puntero) {}

        @Override
        public void acelerometro(float x, float y, float z) {}
    }
}
