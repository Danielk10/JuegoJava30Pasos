package com.diamon.nucleo;

import java.util.List;

public interface Entrada {

    public static class EventoDeTecla {

        public static final int TECLA_ABAJO = 0;

        public static final int TECLA_ARRIBA = 1;

        public int tipoEventoDeTecla;

        public int codigoDeTecla;

        public char caracterDeTecla;
    }

    public static class EventoDeToque {

        public static final int TOQUE_ABAJO = 0;

        public static final int TOQUE_ARRIBA = 1;

        public static final int TOQUE_DESLIZANDO = 2;

        public int tipoEventoDeToque;

        public float x, y;

        public int puntero;
    }

    public boolean isTeclaPrecionada(int codigoDeTecla);

    public boolean isToque(int puntero);

    public float getToqueEnX(int puntero);

    public float getToqueEnY(int puntero);

    public float getAcelerometroEnX();

    public float getAcelerometroEnY();

    public float getAcelerometroEnZ();

    public List<EventoDeTecla> getEventosDeTecla();

    public List<EventoDeToque> getEventosDeToque();
}
