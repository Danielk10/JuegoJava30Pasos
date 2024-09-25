package com.diamon.dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.diamon.nucleo.Datos;

public class Configuraciones {

	private boolean sonido;

	private int puntuaciones[] = new int[20];

	public Configuraciones() {

		super();
		
		sonido = true;

	}

	public boolean isSonido() {
		return sonido;
	}

	public void setSonido(boolean sonido) {
		this.sonido = sonido;
	}

	public int[] getPuntuaciones() {
		return puntuaciones;
	}

	public void setPuntuaciones(int[] puntuaciones) {
		this.puntuaciones = puntuaciones;
	}

	public void caragarConfiguraciones(Datos datos) {

		BufferedReader BuferarchivoLeer = null;

		try {
			BuferarchivoLeer = new BufferedReader(new InputStreamReader(datos.leerDato(DatosJuego.DATOS)));

			sonido = Boolean.parseBoolean(BuferarchivoLeer.readLine());

			for (int i = 0; i < puntuaciones.length; i++) {

				puntuaciones[i] = Integer.parseInt(BuferarchivoLeer.readLine());

			}

		} catch (IOException e) {

		} finally {
			try {
				if (BuferarchivoLeer != null) {

					BuferarchivoLeer.close();
				}

			} catch (IOException e) {

			}

		}

	}

	public void guardarConfiguraciones(Datos datos) {

		BufferedWriter buferarchivoEscribir = null;

		try {

			buferarchivoEscribir = new BufferedWriter(new OutputStreamWriter(datos.escribirDato(DatosJuego.DATOS)));

			buferarchivoEscribir.write(Boolean.toString(sonido));

			buferarchivoEscribir.newLine();

			for (int i = 0; i < puntuaciones.length; i++) {

				buferarchivoEscribir.write(Integer.toString(puntuaciones[i]));

				buferarchivoEscribir.newLine();
			}
		} catch (IOException e) {

		} finally {
			if (buferarchivoEscribir != null) {
				try {

					buferarchivoEscribir.close();

				} catch (IOException e) {

				}

			}

		}

	}

	public void anadirPuntuaciones(int puntuacion) {

		for (int i = 0; i < puntuaciones.length; i++) {
			if (puntuaciones[i] < puntuacion) {
				for (int j = (puntuaciones.length - 1); j > i; j--) {
					puntuaciones[j] = puntuaciones[j - 1];
				}
				puntuaciones[i] = puntuacion;
				break;
			}

		}

	}

}
