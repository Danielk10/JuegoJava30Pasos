package com.diamon.nucleo;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public abstract class Juego extends Frame implements Runnable {

	public final static int ANCHO_PANTALLA = 640;

	public final static int ALTO_PANTALLA = 480;

	private Thread hilo;

	private int x;

	private volatile boolean iniciar;

	private final static int UNIDAD_TIEMPO = 1000000000;

	private float delta = UNIDAD_TIEMPO;

	private final static byte CICLOS = 60;

	private final static float LIMITE_CICLOS = UNIDAD_TIEMPO / CICLOS;

	public Juego() {

		super("Ventana");

		setSize(Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		setVisible(true);

		addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent ev) {

			}

			@Override
			public void windowClosed(WindowEvent ev) {

			}

			@Override
			public void windowClosing(WindowEvent ev) {

				System.exit(0);
			}

			@Override
			public void windowDeactivated(WindowEvent ev) {

			}

			@Override
			public void windowDeiconified(WindowEvent ev) {

			}

			@Override
			public void windowIconified(WindowEvent ev) {

			}

			@Override
			public void windowOpened(WindowEvent ev) {

			}

		});

		x = 0;

		hilo = new Thread(this);

		iniciar = false;
	}

	@Override
	public void paint(Graphics g) {

		x++;

		g.setColor(Color.BLUE);

		g.fillRect(x, 10, 100, 100);

	}

	@Override
	public void run() {

		double referencia = System.nanoTime();

		while (iniciar) {

			final double tiempoInicial = System.nanoTime();

			delta = (float) (tiempoInicial - referencia) / UNIDAD_TIEMPO;
			
			
			//Aqu� actualizaci�n y dib�jo
			
			
			repaint();
			
			//Hasta aqu�
			
			
			referencia = tiempoInicial;

			// Limite de cuadros

			do {

				Thread.yield();

			} while (System.nanoTime() - tiempoInicial < LIMITE_CICLOS);

		}

	}

	public void iniciar() {
		hilo.start();
		iniciar = true;

	}

}
