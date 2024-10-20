package com.diamon.nucleo;

public interface Pantalla {

    public abstract void mostrar();

    public abstract void resume();

    public abstract void colisiones();

    public abstract void actualizar(float delta);

    public abstract void dibujar(Graficos pincel, float delta);

    public abstract void reajustarPantalla(int ancho, int alto);

    public abstract void pausa();

    public abstract void ocultar();

    public abstract void liberarRecursos();

    public abstract void teclaPresionada(int codigoDeTecla);

    public abstract void teclaLevantada(int codigoDeTecla);

    public abstract void toquePresionado(float x, float y, int puntero);

    public abstract void toqueLevantado(float x, float y, int puntero);

    public abstract void toqueDeslizando(float x, float y, int puntero);

    public abstract void acelerometro(float x, float y, float z);
}
