package com.diamon.nucleo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public abstract class Juego extends Canvas implements Runnable {

	public final static int ANCHO_PANTALLA = 640;

	public final static int ALTO_PANTALLA = 480;

	private Thread hilo;

	private int x;

	private volatile boolean iniciar;

	private final static int UNIDAD_TIEMPO = 1000000000;

	private float delta = UNIDAD_TIEMPO;

	private final static byte CICLOS = 60;

	private final static float LIMITE_CICLOS = UNIDAD_TIEMPO / CICLOS;

	private BufferStrategy bufer;
	
	private JFrame ventana;

	public Juego() {

		super();

		setSize(Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		ventana = new JFrame("Ventana");

		ventana.setSize(Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ventana.add(this);

		ventana.setVisible(true);

		x = 0;
		
		
		
		createBufferStrategy(2);

		bufer = getBufferStrategy();
		
		
		

		hilo = new Thread(this);

		iniciar = false;

		
	}

	@Override
	public void paint(Graphics g) {
		
		super.paint(g);

		Graphics g1 = bufer.getDrawGraphics();

		x++;

		g1.setColor(Color.RED);

		g1.fillRect(0, 0, Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		g1.setColor(Color.BLUE);

		g1.fillRect(x, 10, 100, 100);

		bufer.show();
	

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
