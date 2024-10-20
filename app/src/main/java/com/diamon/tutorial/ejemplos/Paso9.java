package com.diamon.tutorial.ejemplos;

import android.graphics.Color;
import android.media.AudioManager;

import androidx.appcompat.app.AppCompatActivity;

import com.diamon.audio.MusicaDeJuego;
import com.diamon.graficos.Pantalla2D;
import com.diamon.graficos.Textura2D;
import com.diamon.nucleo.Actor;
import com.diamon.nucleo.Graficos;
import com.diamon.nucleo.Juego;
import com.diamon.nucleo.Pantalla;
import com.diamon.nucleo.Textura;

public class Paso9 extends Juego {

    public Paso9(AppCompatActivity actividad) {
        super(actividad);

        actividad.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        this.setMostrarFPS(true);

        this.setColorAzul(true);

        PanatallaMenu pantalla = new PanatallaMenu(this);

        setPantalla(pantalla);
    }

    class PanatallaMenu extends Pantalla2D {

        private MusicaDeJuego musica;

        public PanatallaMenu(Juego juego) {
            super(juego);

            musica = (MusicaDeJuego) recurso.cargarMusica("explosion.wav");
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

            pincel.dibujarTexto("Por favor toque la Pantalla", 20, 400, Color.BLACK);
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

            musica.setVolumen(1);

            musica.reproducir();

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

        private Textura2D textura;

        private Jugador jugador;

        public PanatallaJuego(Juego juego) {
            super(juego);

            textura = new Textura2D(recurso.cargarTextura("burbuja.png").getBipmap(), 32, 32);

            jugador = new Jugador(this, textura, 20, 20, 32, 32);
        }

        @Override
        public void mostrar() {}

        @Override
        public void resume() {}

        @Override
        public void colisiones() {}

        @Override
        public void actualizar(float delta) {

            jugador.actualizar(delta);
        }

        @Override
        public void dibujar(Graficos pincel, float delta) {
            pincel.limpiar(Color.RED);

            jugador.dibujar(pincel, delta);

            pincel.getLapiz().setTextSize(28);

            pincel.dibujarTexto("Deslice el dedo por la Pantalla", 20, 400, Color.BLACK);
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
        public void toqueDeslizando(float x, float y, int puntero) {

            jugador.setX(x);

            jugador.setY(y);
        }

        @Override
        public void acelerometro(float x, float y, float z) {}
    }

    class Jugador extends Actor {

        public Jugador(
                Pantalla pantalla, Textura textura, float x, float y, float ancho, float alto) {
            super(pantalla, textura, x, y, ancho, alto);
        }

        public Jugador(Pantalla pantalla, Textura textura, float x, float y) {
            super(pantalla, textura, x, y);
        }

        public Jugador(
                Pantalla pantalla,
                Textura[] texturas,
                float x,
                float y,
                float ancho,
                float alto,
                float tiempoAnimacion) {
            super(pantalla, texturas, x, y, ancho, alto, tiempoAnimacion);
        }

        @Override
        public void obtenerActores() {}

        @Override
        public void colision(Actor actor) {}
    }
}
