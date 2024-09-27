package com.diamon.audio;

import com.diamon.nucleo.Sonido;

import android.media.SoundPool;

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
