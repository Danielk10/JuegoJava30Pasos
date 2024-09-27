package com.diamon.dato;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.diamon.nucleo.Datos;

import android.content.res.AssetManager;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;


public class DatosJuego implements Datos {

	public final static String DATOS = "datos.txt";

	private AssetManager manejador;
	
	private Activity actividad;

	public DatosJuego(Activity  actividad) {

		this.actividad = actividad;
		
		this.manejador = actividad.getAssets();

	}

	 @Override
	public InputStream leerDatoExterno(String nombre) throws IOException {
		return new FileInputStream(
				Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + nombre);
	}

	@Override
	public OutputStream escribirDatoExterno(String nombre) throws IOException {

		return new FileOutputStream(
				Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + nombre);
	}

    @Override
		public InputStream leerDatoInterno(String nombre) throws IOException
		{
			return actividad.openFileInput(nombre);
		}


		@Override
		public OutputStream escribirDatoInterno(String nombre) throws IOException
		{
			return actividad.openFileOutput(nombre, Context.MODE_PRIVATE);
		}


	@Override
	public InputStream leerAsset(String nombre) throws IOException {

		return manejador.open(nombre);
	}

}
