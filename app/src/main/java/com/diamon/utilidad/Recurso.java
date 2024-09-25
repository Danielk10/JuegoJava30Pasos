package com.diamon.utilidad;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;

public class Recurso implements OnCompletionListener {

	private HashMap<String, Bitmap> imagenes;

	private HashMap<String, SoundPool> sonidos;

	private HashMap<String, MediaPlayer> musicas;

	private HashMap<String, Integer> idSonidos;

	private Context contexto;

	private boolean preparado = false;

	private SoundPool sonido;

	@SuppressWarnings("deprecation")
	public Recurso(Context contexto) {

		sonidos = new HashMap<String, SoundPool>();

		musicas = new HashMap<String, MediaPlayer>();

		imagenes = new HashMap<String, Bitmap>();

		idSonidos = new HashMap<String, Integer>();

		this.contexto = contexto;

		sonido = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);

	}

	public Bitmap cargarImagen(String nombre) {

		InputStream entrada = null;

		Bitmap imagen = null;

		try {
			entrada = contexto.getAssets().open(nombre);

			imagen = BitmapFactory.decodeStream(entrada);

			imagenes.put(nombre, imagen);

			return imagenes.get(nombre);

		} catch (IOException e) {

		} finally {
			if (entrada != null) {
				try {
					entrada.close();

				} catch (IOException e) {

				}

			}
		}

		return null;

	}

	public MediaPlayer cargarMusica(String nombre) {

		MediaPlayer musica = new MediaPlayer();

		AssetFileDescriptor descriptor = null;

		try {
			descriptor = contexto.getAssets().openFd(nombre);
		} catch (IOException e) {

		}

		try {
			musica.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());

			musica.prepare();

			preparado = true;

			musica.setOnCompletionListener(this);

		} catch (IllegalArgumentException e) {

		} catch (IllegalStateException e) {

		} catch (IOException e) {

		}

		musicas.put(nombre, musica);

		return musicas.get(nombre);

	}

	public MediaPlayer getMusica(String nombre) {
		MediaPlayer musica;
		musica = (MediaPlayer) musicas.get(nombre);

		if (musica == null) {

			musica = cargarMusica(nombre);

			musicas.put(nombre, musica);
		}

		return musica;

	}

	public void playMusica(final String nombre, final int volumen) {
		new Thread(new Runnable() {
			public void run() {

				if (getMusica(nombre).isPlaying()) {
					return;
				}

				if (!preparado) {
					try {
						getMusica(nombre).prepare();
					} catch (IllegalStateException e) {

					} catch (IOException e) {

					}
				}

				getMusica(nombre).setVolume(volumen, volumen);

				getMusica(nombre).start();

			}
		}).start();
	}

	public void repetirMusica(final String nombre, final int volumen) {
		new Thread(new Runnable() {
			public void run() {
				if (getMusica(nombre).isPlaying()) {
					return;
				}

				if (!preparado) {
					try {
						getMusica(nombre).prepare();
					} catch (IllegalStateException e) {

					} catch (IOException e) {

					}
				}

				getMusica(nombre).setVolume(volumen, volumen);

				getMusica(nombre).start();

				getMusica(nombre).setLooping(true);
			}
		}).start();
	}

	public void pusaMusica(final String nombre) {
		new Thread(new Runnable() {
			public void run() {

				if (getMusica(nombre).isPlaying()) {

					getMusica(nombre).pause();

				}

			}
		}).start();
	}

	public SoundPool cargarSonido(String nombre) {

		AssetFileDescriptor descriptor = null;

		try {

			descriptor = contexto.getAssets().openFd(nombre);

			int idSonido = sonido.load(descriptor, 0);

			idSonidos.put(nombre, idSonido);

		} catch (IOException e1) {

		}

		try {

			sonidos.put(nombre, sonido);

			return sonidos.get(nombre);

		} catch (Exception e) {
			return null;
		}

	}

	public SoundPool getSonido(String nombre) {

		SoundPool sonido = (SoundPool) sonidos.get(nombre);

		if (sonido == null) {

			sonido = cargarSonido(nombre);

			sonidos.put(nombre, sonido);
		}

		return sonido;

	}

	public void playSonido(final String nombre, final int volumen) {
		new Thread(new Runnable() {
			public void run() {

				int idSonido = (int) idSonidos.get(nombre);

				getSonido(nombre).play(idSonido, volumen, volumen, 0, 0, 1);
			}
		}).start();
	}

	public void pararSonido(SoundPool sonido) {

		sonido.release();

	}

	public void pararMusica(MediaPlayer musica) {

		if (musica.isPlaying()) {
			musica.stop();

			preparado = false;
		}

	}

	public Bitmap getImagen(String nombre) {
		Bitmap imagen;
		imagen = (Bitmap) imagenes.get(nombre);

		if (imagen == null) {

			imagen = cargarImagen(nombre);

			imagenes.put(nombre, imagen);
		}

		return imagen;

	}

	@Override
	public void onCompletion(MediaPlayer media) {

		preparado = false;
	}

	public static Bitmap crearBitmap(Bitmap imagen, float ancho, float alto) {

		int w = imagen.getWidth();
		int h = imagen.getHeight();
		float sw = ancho / w;
		float sh = alto / h;
		Matrix max = new Matrix();
		max.postScale(sw, sh);
		return Bitmap.createBitmap(imagen, 0, 0, w, h, max, false);

	}

}
