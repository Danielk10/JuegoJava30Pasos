package com.diamon.dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.diamon.nucleo.Actor;
import com.diamon.utilidad.Vector2D;

public class Configuraciones {

	public final static String NIVEL_1 = "Nivel 1";
	public final static String NIVEL_2 = "Nivel 2";
	public final static String NIVEL_3 = "Nivel 3";
	public final static String NIVEL_4 = "Nivel 4";
	public final static String NIVEL_5 = "Nivel 5";

	public final static String ANTI_AEREO = "com.diamon.actor.AntiAereo";
	public final static String VOLADOR = "com.diamon.actor.Volador";
	public final static String ROBOT = "com.diamon.actor.Robot";
	public final static String SALTADOR = "com.diamon.actor.Saltador";
	public final static String CAJA = "com.diamon.actor.Caja";
	public final static String LANZA_MISIL = "com.diamon.actor.LanzaMisil";
	public final static String MAQUINA_PARED = "com.diamon.actor.MaquinaPared";

	private boolean sonido;

	private int puntuaciones[] = new int[20];

	private int numeroNivel;

	private DatosJuego datos;

	private Configuraciones configuracion;

	private ArrayList<Vector2D>[] posicionActores;

	private ArrayList<String>[] tipoActores;

	private int t[] = new int[5];

	@SuppressWarnings("unchecked")
	public Configuraciones() {

		super();

		sonido = true;

		numeroNivel = 1;

		datos = new DatosJuego();

		posicionActores = new ArrayList[5];

		tipoActores = new ArrayList[5];

		for (int i = 0; i < posicionActores.length; i++) {

			posicionActores[i] = new ArrayList<Vector2D>();

		}

		for (int i = 0; i < tipoActores.length; i++) {

			tipoActores[i] = new ArrayList<String>();

		}

		for (int i = 0; i < t.length; i++) {

			t[i] = 0;

		}

	}

	public int getNumeroNivel() {
		return numeroNivel;
	}

	public void setNumeroNivel(int numeroNivel) {
		this.numeroNivel = numeroNivel;
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

	private void guardarPoiciones(BufferedWriter buferarchivoEscribir) throws IOException {

		for (int i = 0; i < posicionActores.length; i++) {

			for (int j = 0; j < posicionActores[i].size(); j++) {

				buferarchivoEscribir.write(Float.toString(posicionActores[i].get(j).x));

				buferarchivoEscribir.newLine();

				buferarchivoEscribir.write(Float.toString(posicionActores[i].get(j).y));

				buferarchivoEscribir.newLine();

			}

		}

	}

	private void guardarTipos(BufferedWriter buferarchivoEscribir) throws IOException {

		for (int i = 0; i < tipoActores.length; i++) {

			for (int j = 0; j < tipoActores[i].size(); j++) {

				buferarchivoEscribir.write(tipoActores[i].get(j));

				buferarchivoEscribir.newLine();

			}

		}

	}

	private void obtenerPosiciones(BufferedReader BuferarchivoLeer) throws IOException {

		int v1 = 0;

		int v2 = 0;

		for (int j = 0; j < t[numeroNivel - 1]; j++) {

			v1 = Integer.parseInt(BuferarchivoLeer.readLine());

			v2 = Integer.parseInt(BuferarchivoLeer.readLine());

			posicionActores[numeroNivel - 1].add(new Vector2D(v1, v2));

		}

	}

	private void obtenerTipos(BufferedReader BuferarchivoLeer) throws IOException {

		for (int j = 0; j < t[numeroNivel - 1]; j++) {

			tipoActores[numeroNivel - 1].add(BuferarchivoLeer.readLine());
		}

	}

	public Configuraciones caragarConfiguraciones() {

		BufferedReader BuferarchivoLeer = null;

		try {
			BuferarchivoLeer = new BufferedReader(new InputStreamReader(datos.leerDato(DatosJuego.DATOS)));

			sonido = Boolean.parseBoolean(BuferarchivoLeer.readLine());

			for (int i = 0; i < puntuaciones.length; i++) {

				puntuaciones[i] = Integer.parseInt(BuferarchivoLeer.readLine());

			}

			numeroNivel = Integer.parseInt(BuferarchivoLeer.readLine());

			for (int i = 0; i < t.length; i++) {

				t[i] = Integer.parseInt(BuferarchivoLeer.readLine());
			}

			obtenerPosiciones(BuferarchivoLeer);

			obtenerTipos(BuferarchivoLeer);

		} catch (IOException e) {

		} finally {
			try {
				if (BuferarchivoLeer != null) {

					BuferarchivoLeer.close();
				}

			} catch (IOException e) {

			}

		}

		configuracion = this;

		return configuracion;

	}

	public void guardarConfiguraciones() {

		BufferedWriter buferarchivoEscribir = null;

		try {

			buferarchivoEscribir = new BufferedWriter(new OutputStreamWriter(datos.escribirDato(DatosJuego.DATOS)));

			buferarchivoEscribir.write(Boolean.toString(sonido));

			buferarchivoEscribir.newLine();

			for (int i = 0; i < puntuaciones.length; i++) {

				buferarchivoEscribir.write(Integer.toString(puntuaciones[i]));

				buferarchivoEscribir.newLine();
			}

			buferarchivoEscribir.write(Integer.toString(numeroNivel));

			buferarchivoEscribir.newLine();

			for (int i = 0; i < t.length; i++) {

				t[i] = posicionActores[numeroNivel - 1].size();

				buferarchivoEscribir.write(Integer.toString(t[i]));

				buferarchivoEscribir.newLine();

			}

			guardarPoiciones(buferarchivoEscribir);

			guardarTipos(buferarchivoEscribir);

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

	public void gurdarActores(ArrayList<Actor> personajes, String tipo, String nivel) {

		String ni = "";

		int n = 1;

		for (int i = 0; i < posicionActores.length; i++) {

			ni = "Nivel " + n;

			if (nivel.contentEquals(ni)) {

				for (int j = 0; j < personajes.size(); j++) {

					if (tipo.contentEquals(personajes.get(j).getClass().getName().toString())) {

						posicionActores[i].add(new Vector2D(personajes.get(j).getX(), personajes.get(j).getY()));

						tipoActores[i].add(personajes.get(j).getClass().getName().toString());

					}

				}

			}

			n++;
		}

	}

	public ArrayList<Vector2D> getPosicionActores(String tipo, String nivel) {

		ArrayList<Vector2D> v = new ArrayList<Vector2D>();

		String ni = "";

		int n = 1;

		for (int i = 0; i < posicionActores.length; i++) {

			ni = "Nivel " + n;

			if (nivel.contentEquals(ni)) {

				for (int j = 0; j < tipoActores[i].size(); j++) {

					if (tipo.contentEquals(tipoActores[i].get(j))) {

						v.add(posicionActores[i].get(j));

					}

				}

			}

			n++;

		}

		return v;

	}

	public ArrayList<Vector2D> getTamanoArray(String nivel) {

		ArrayList<Vector2D> v = new ArrayList<Vector2D>();

		String ni = "";

		int n = 1;

		for (int i = 0; i < posicionActores.length; i++) {

			ni = "Nivel " + n;

			if (nivel.contentEquals(ni)) {

				for (int j = 0; j < posicionActores[i].size(); j++) {

					v.add(posicionActores[i].get(j));

				}

			}

			n++;
		}

		return v;

	}

	public void eliminarActores(String nivel) {

		String ni = "";

		int n = 1;

		for (int i = 0; i < posicionActores.length; i++) {

			ni = "Nivel " + n;

			if (nivel.contentEquals(ni)) {

				posicionActores[i].clear();

				tipoActores[i].clear();

			}

			n++;

		}

	}

	public void eliminarActor(String nivel, String tipo, int indice) {

		String ni = "";

		int n = 1;

		for (int i = 0; i < posicionActores.length; i++) {

			ni = "Nivel " + n;

			if (nivel.contentEquals(ni)) {

				for (int j = 0; j < tipoActores[i].size(); j++) {

					if (tipo.contentEquals(tipoActores[i].get(j))) {

						if (indice == j) {

							posicionActores[i].remove(j);

							tipoActores[i].remove(j);

						}

					}

				}

			}

			n++;

		}

	}

}
