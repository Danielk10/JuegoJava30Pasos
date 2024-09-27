package com.diamon.dato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.res.AssetManager;
import android.app.Activity;
import android.os.Environment;

import java.util.ArrayList;

import com.diamon.nucleo.Actor;
import com.diamon.utilidad.Vector2D;


public class ConfiguracionesDeJuego
{

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
	private ArrayList<Vector2D>[] posicionActores;

	private ArrayList<String>[] tipoActores;

	private int t[] = new int[5];
	
	private int numeroNivel;
	
	
	private boolean sonido;

	private int puntuaciones[] = new int[20];

	private boolean leerDatosAsset;

	private DatosJuego datos;

	public static final int ASSET = 1;

	public static final int INTERNO = 0;

	public static final int EXTERNO = 3;
	
	private int tipo;

	public ConfiguracionesDeJuego(Activity actividad, int tipo)
	{

		datos = new DatosJuego(actividad);

		this.tipo = tipo;

		sonido = true;

		leerDatosAsset = true;
		
		numeroNivel = 1;
		
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
	

	public boolean isSonido()
	{
		return sonido;
	}

	public boolean isLeerDatosAsset()
	{
		return leerDatosAsset;
	}

	public void setLeerDatosAsset(boolean leerDatosAsset)
	{
		this.leerDatosAsset = leerDatosAsset;
	}

	public void setSonido(boolean sonido)
	{
		this.sonido = sonido;
	}

	public int[] getPuntuaciones()
	{
		return puntuaciones;
	}

	public void setPuntuaciones(int[] puntuaciones)
	{
		this.puntuaciones = puntuaciones;
	}

	public ConfiguracionesDeJuego cargarConfiguraciones()
	{

		BufferedReader buferarchivoLeer = null;

		try
		{



			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
				if (tipo == ConfiguracionesDeJuego.ASSET)
				{

					tipo = ConfiguracionesDeJuego.ASSET;


				}
				else
				{

					tipo = ConfiguracionesDeJuego.EXTERNO;

				}

			}
			else
			{

				if (tipo == ConfiguracionesDeJuego.ASSET)
				{

					tipo = ConfiguracionesDeJuego.ASSET;


				}
				else
				{

					tipo = ConfiguracionesDeJuego.INTERNO;

				}


			}

			if (tipo == ConfiguracionesDeJuego.ASSET)
			{

				buferarchivoLeer = new BufferedReader(new InputStreamReader(datos.leerAsset("datos.txt"), "UTF-8"));

			}



			if (tipo == ConfiguracionesDeJuego.EXTERNO)
			{

				buferarchivoLeer = new BufferedReader(new InputStreamReader(datos.leerDatoExterno(DatosJuego.DATOS), "UTF-8"));

			}



			if (tipo == ConfiguracionesDeJuego.INTERNO)
			{

				buferarchivoLeer = new BufferedReader(new InputStreamReader(datos.leerDatoInterno(DatosJuego.DATOS), "UTF-8"));

			}


			sonido = Boolean.parseBoolean(buferarchivoLeer.readLine());

			leerDatosAsset = Boolean.parseBoolean(buferarchivoLeer.readLine());

			for (int i = 0; i < puntuaciones.length; i++)
			{

				puntuaciones[i] = Integer.parseInt(buferarchivoLeer.readLine());

			}
			
			
			numeroNivel = Integer.parseInt(buferarchivoLeer.readLine());

			for (int i = 0; i < t.length; i++) {

				t[i] = Integer.parseInt(buferarchivoLeer.readLine());
			}

			obtenerPosiciones(buferarchivoLeer);

			obtenerTipos(buferarchivoLeer);
			
			
			

		}
		catch (IOException e)
		{

		}
		finally
		{
			try
			{
				if (buferarchivoLeer != null)
				{

					buferarchivoLeer.close();
				}

			}
			catch (IOException e)
			{

			}

		}

		return this;

	}

	public void guardarConfiguraciones()
	{

		BufferedWriter buferarchivoEscribir = null;

		try
		{

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{
				tipo = ConfiguracionesDeJuego.EXTERNO;

			}
			else
			{

				tipo = ConfiguracionesDeJuego.INTERNO;


			}

			
			if (tipo == ConfiguracionesDeJuego.EXTERNO)
			{

			buferarchivoEscribir = new BufferedWriter(new OutputStreamWriter(datos.escribirDatoExterno(DatosJuego.DATOS),  "UTF-8"));

			}
	
			if (tipo == ConfiguracionesDeJuego.INTERNO)
			{

				buferarchivoEscribir = new BufferedWriter(new OutputStreamWriter(datos.escribirDatoInterno(DatosJuego.DATOS),  "UTF-8"));

			}
			
			
			buferarchivoEscribir.write(Boolean.toString(sonido));

			buferarchivoEscribir.newLine();

			buferarchivoEscribir.write(Boolean.toString(leerDatosAsset));

			buferarchivoEscribir.newLine();

			for (int i = 0; i < puntuaciones.length; i++)
			{

				buferarchivoEscribir.write(Integer.toString(puntuaciones[i]));

				buferarchivoEscribir.newLine();
			}
			
			
			guardarPoiciones(buferarchivoEscribir);

			guardarTipos(buferarchivoEscribir);
			
			
		}
		catch (IOException e)
		{

		}
		finally
		{
			if (buferarchivoEscribir != null)
			{
				try
				{

					buferarchivoEscribir.close();

				}
				catch (IOException e)
				{

				}

			}

		} 

	}

	public void anadirPuntuaciones(int puntuacion)
	{

		for (int i = 0; i < puntuaciones.length; i++)
		{
			if (puntuaciones[i] < puntuacion)
			{
				for (int j = (puntuaciones.length - 1); j > i; j--)
				{
					puntuaciones[j] = puntuaciones[j - 1];
				}
				puntuaciones[i] = puntuacion;
				break;
			}

		}

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
