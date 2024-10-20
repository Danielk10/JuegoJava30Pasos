package com.diamon.tutorial.archivo;

import android.app.Activity;
import android.os.Environment;

import com.diamon.dato.DatosJuego;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Contenido {
    private DatosJuego datos;

    public static final int ASSET = 1;

    public static final int INTERNO = 2;

    public static final int EXTERNO = 3;

    private int tipo;

    public Contenido(Activity actividad, int tipo) {

        datos = new DatosJuego(actividad);

        this.tipo = tipo;
    }

    public ArrayList<String> leerArchivo(final String nombre) {

        BufferedReader buferarchivoLeer = null;

        ArrayList<String> textos = new ArrayList<String>();

        try {

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                if (tipo == Contenido.ASSET) {

                    tipo = Contenido.ASSET;

                } else {

                    tipo = Contenido.EXTERNO;
                }

            } else {

                if (tipo == Contenido.ASSET) {

                    tipo = Contenido.ASSET;

                } else {

                    tipo = Contenido.INTERNO;
                }
            }

            if (tipo == Contenido.ASSET) {

                buferarchivoLeer =
                        new BufferedReader(new InputStreamReader(datos.leerAsset(nombre), "UTF-8"));
            }

            if (tipo == Contenido.EXTERNO) {

                buferarchivoLeer =
                        new BufferedReader(
                                new InputStreamReader(datos.leerDatoExterno(nombre), "UTF-8"));
            }

            if (tipo == Contenido.INTERNO) {

                buferarchivoLeer =
                        new BufferedReader(
                                new InputStreamReader(datos.leerDatoInterno(nombre), "UTF-8"));
            }

            String lineas = "";

            while ((lineas = buferarchivoLeer.readLine()) != null) {

                textos.add(lineas);
            }

        } catch (IOException e) {

        } finally {
            try {
                if (buferarchivoLeer != null) {

                    buferarchivoLeer.close();
                }

            } catch (IOException e) {

            }
        }

        return textos;
    }

    public void escribirArchivo(final String nombre) {

        BufferedWriter buferarchivoEscribir = null;

        try {

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                tipo = Contenido.EXTERNO;

            } else {

                tipo = Contenido.INTERNO;
            }

            if (tipo == Contenido.EXTERNO) {

                buferarchivoEscribir =
                        new BufferedWriter(
                                new OutputStreamWriter(datos.escribirDatoExterno(nombre), "UTF-8"));
            }

            if (tipo == Contenido.INTERNO) {

                buferarchivoEscribir =
                        new BufferedWriter(
                                new OutputStreamWriter(datos.escribirDatoInterno(nombre), "UTF-8"));
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

    public String leerCodigo(final String nombre) {

        BufferedReader buferarchivoLeer = null;

        StringBuilder texto = new StringBuilder();

        try {

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                if (tipo == Contenido.ASSET) {

                    tipo = Contenido.ASSET;

                } else {

                    tipo = Contenido.EXTERNO;
                }

            } else {

                if (tipo == Contenido.ASSET) {

                    tipo = Contenido.ASSET;

                } else {

                    tipo = Contenido.INTERNO;
                }
            }

            if (tipo == Contenido.ASSET) {

                buferarchivoLeer =
                        new BufferedReader(new InputStreamReader(datos.leerAsset("tutorial"+File.separator+nombre)));
            }

            if (tipo == Contenido.EXTERNO) {

                buferarchivoLeer =
                        new BufferedReader(
                                new InputStreamReader(datos.leerDatoExterno(nombre), "UTF-8"));
            }

            if (tipo == Contenido.INTERNO) {

                buferarchivoLeer =
                        new BufferedReader(
                                new InputStreamReader(datos.leerDatoInterno(nombre), "UTF-8"));
            }

            String lineas = "";

            while ((lineas = buferarchivoLeer.readLine()) != null) {

                texto.append(lineas);
                texto.append("\n");
            }

        } catch (IOException e) {

        } finally {
            try {
                if (buferarchivoLeer != null) {

                    buferarchivoLeer.close();
                }

            } catch (IOException e) {

            }
        }

        return texto.toString();
    }
}
