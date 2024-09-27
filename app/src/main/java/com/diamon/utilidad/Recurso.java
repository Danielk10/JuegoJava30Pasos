package com.diamon.utilidad;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.diamon.audio.EfectoDeSonido;
import com.diamon.audio.MusicaDeJuego;
import com.diamon.graficos.Textura2D;
import com.diamon.nucleo.Musica;
import com.diamon.nucleo.Sonido;
import com.diamon.nucleo.Textura;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;

public class Recurso
{

	private HashMap<String, Textura> texturas;

	private HashMap<String, Sonido> sonidos;

	private HashMap<String, Musica> musicas;

	private Context contexto;

	public Recurso(Context contexto)
	{

		sonidos = new HashMap<String, Sonido>();

		musicas = new HashMap<String, Musica>();

		texturas = new HashMap<String, Textura>();

		this.contexto = contexto;

	}

	public Textura cargarTextura(String nombre)
	{

		InputStream entrada = null;

		Textura imagen = null;

		final BitmapFactory.Options options = new BitmapFactory.Options();



		try
		{
			entrada = contexto.getAssets().open(nombre);

			options.inJustDecodeBounds = true;

			// Decodificar solo las dimensiones
			BitmapFactory.decodeStream(entrada, null, options);

			// Cerrar el stream
			entrada.close();

			options.inSampleSize = calculateInSampleSize(options, 640);

			options.inJustDecodeBounds = false;

			entrada = contexto.getAssets().open(nombre);

			imagen = new Textura2D(BitmapFactory.decodeStream(entrada, null, options));

			texturas.put(nombre, imagen);

		}
		catch (IOException e)
		{

		}
		finally
		{
			if (entrada != null)
			{
				try
				{
					entrada.close();

				}
				catch (IOException e)
				{

				}

			}
		}

		return texturas.get(nombre);

	}


	private int calculateInSampleSize(BitmapFactory.Options options, int maxTextureSize)
	{
		// Obtener el ancho y alto originales
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		// Si las dimensiones originales superan el tamaño máximo permitido
		if (height > maxTextureSize || width > maxTextureSize)
		{
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calcular el valor adecuado de inSampleSize para reducir el bitmap
			while ((halfHeight / inSampleSize) >= maxTextureSize && (halfWidth / inSampleSize) >= maxTextureSize)
			{
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}


	public Textura getTextura(String nombre)
	{

		Textura imagen = texturas.get(nombre);

		if (imagen == null)
		{

			imagen = cargarTextura(nombre);

			texturas.put(nombre, imagen);
		}

		return imagen;

	}

	public Musica cargarMusica(String nombre)
	{

		AssetFileDescriptor descriptor = null;

		try
		{

			descriptor = contexto.getAssets().openFd(nombre);

		}
		catch (IOException e)
		{

		}

		Musica musica = new MusicaDeJuego(descriptor);

		musicas.put(nombre, musica);

		return musicas.get(nombre);

	}

	public Musica getMusica(String nombre)
	{

		Musica musica = musicas.get(nombre);

		if (musica == null)
		{

			musica = (Musica) cargarMusica(nombre);

			musicas.put(nombre, musica);
		}

		return musica;

	}

	@SuppressWarnings("deprecation")
	public Sonido cargarSonido(String nombre)
	{

		AssetFileDescriptor descriptor = null;

		try
		{

			descriptor = contexto.getAssets().openFd(nombre);

		}
		catch (IOException e)
		{

		}

		SoundPool sonidoPool = new SoundPool(200, AudioManager.STREAM_MUSIC, 0);

		int id = sonidoPool.load(descriptor, 0);

		Sonido sonido = new EfectoDeSonido(id, sonidoPool);

		sonidos.put(nombre, sonido);

		return sonidos.get(nombre);

	}

	public Sonido getSonido(String nombre)
	{

		Sonido sonido = sonidos.get(nombre);

		if (sonido == null)
		{

			sonido = (Sonido) cargarSonido(nombre);

			sonidos.put(nombre, sonido);
		}

		return sonido;

	}

}
