package com.diamon.dato;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.diamon.nucleo.Datos;

public class DatosJuego implements Datos {

	public final static String DATOS = "datos.txt";

	public DatosJuego() {

	}

	@Override
	public InputStream leerDato(String nombre) throws IOException {
		return new FileInputStream(nombre);
	}

	@Override
	public OutputStream escribirDato(String nombre) throws IOException {

		return new FileOutputStream(nombre);
	}

}
