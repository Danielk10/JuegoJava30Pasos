package com.diamon.nucleo;

import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;

public interface Datos {

	public InputStream leerDato(String nombre) throws IOException;

	public OutputStream escribirDato(String nombre) throws IOException;

}
