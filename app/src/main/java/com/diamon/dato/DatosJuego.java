package com.diamon.dato;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.diamon.nucleo.Datos;

import android.os.Environment;

public class DatosJuego implements Datos {

	public final static String DATOS = "datos.txt";

	public DatosJuego() {

	}

	@SuppressWarnings("deprecation")
	@Override
	public InputStream leerDato(String nombre) throws IOException {
		return new FileInputStream(
				Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + nombre);
	}

	@SuppressWarnings("deprecation")
	@Override
	public OutputStream escribirDato(String nombre) throws IOException {

		return new FileOutputStream(
				Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + nombre);
	}

}
