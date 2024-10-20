package com.diamon.audio;

import android.media.SoundPool;

import com.diamon.nucleo.Sonido;

public class EfectoDeSonido implements Sonido {

    private int id;

    private SoundPool sonidoPool;

    public EfectoDeSonido(int id, SoundPool sonidoPool) {

        this.id = id;

        this.sonidoPool = sonidoPool;
    }

    @Override
    public void reproducir(float volumen) {

        sonidoPool.play(id, volumen, volumen, 0, 0, 1);
    }

    @Override
    public void liberarRecurso() {

        sonidoPool.unload(id);
    }
}
