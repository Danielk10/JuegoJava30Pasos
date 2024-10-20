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

public class Paso7 extends Juego {

    public Paso7(AppCompatActivity actividad) {
        super(actividad);

        actividad.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        this.setMostrarFPS(true);

        this.setColorAzul(true);

        PanatallaMenu pantalla = new PanatallaMenu(this);

        setPantalla(pantalla);
    }

    class PanatallaMenu extends Pantalla2D {

        public PanatallaMenu(Juego juego) {
            super(juego);
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

            pincel.limpiar(Color.RED);

            pincel.dibujarRectangulo(50, 100, 100, 100, Color.GREEN);
            
            pincel.getLapiz().setTextSize(28);
            
            pincel.dibujarTexto("Por favor toque la Pantalla",20,400,Color.BLACK);
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
        public void toquePresionado(float x, float y, int puntero) {

            juego.setPantalla(new PanatallaJuego(juego));
        }

        @Override
        public void toqueLevantado(float x, float y, int puntero) {}

        @Override
        public void toqueDeslizando(float x, float y, int puntero) {}

        @Override
        public void acelerometro(float x, float y, float z) {}
    }

    class PanatallaJuego extends Pantalla2D {

        public PanatallaJuego(Juego juego) {
            super(juego);
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
            pincel.limpiar(Color.RED);
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
