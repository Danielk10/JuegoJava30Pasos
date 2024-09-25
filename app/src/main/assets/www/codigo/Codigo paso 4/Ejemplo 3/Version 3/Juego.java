package com.diamon.nucleo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

public abstract class Juego extends Canvas implements Runnable {

	public final static int ANCHO_PANTALLA = 640;

	public final static int ALTO_PANTALLA = 480;

	private JFrame ventana;
	
	private Thread hilo;

	private int x;

	private volatile boolean iniciar;

	private final static int UNIDAD_TIEMPO = 1000000000;

	private float delta = UNIDAD_TIEMPO;

	private final static byte CICLOS = 60;

	private final static float LIMITE_CICLOS = UNIDAD_TIEMPO / CICLOS;

	public Juego() {

		super();

		setSize(Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		ventana = new JFrame("Ventana");

		ventana.setSize(Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ventana.add(this);

		ventana.setVisible(true);

		x = 0;

		hilo = new Thread(this);

		iniciar = false;
	}

	@Override
	public void paint(Graphics g) {

		
		super.paint(g);
		
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

			// Aquí actualización y dibújo

			repaint();

			// Hasta aquí

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
