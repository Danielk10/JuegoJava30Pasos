package com.diamon.nucleo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public abstract class Juego extends JFrame implements Runnable {

	public final static int ANCHO_PANTALLA = 640;

	public final static int ALTO_PANTALLA = 480;

	private Thread hilo;

	private int x;

	private volatile boolean iniciar;

	private final static int UNIDAD_TIEMPO = 1000000000;

	private float delta = UNIDAD_TIEMPO;

	private final static byte CICLOS = 60;

	private final static float LIMITE_CICLOS = UNIDAD_TIEMPO / CICLOS;
	
	private BufferedImage bufer;

	public Juego() {

		super("Ventana");

		setSize(Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		
		setDefaultCloseOperation(Juego.EXIT_ON_CLOSE); 
		
		setVisible(true);



		x = 0;

		hilo = new Thread(this);

		iniciar = false;
		
		
		bufer = new BufferedImage(Juego.ANCHO_PANTALLA,Juego.ALTO_PANTALLA, BufferedImage.TYPE_INT_RGB);
	}

	@Override
	public void paint(Graphics g) {

		super.paint(g);
		
		Graphics g1 =bufer.getGraphics();
		
		x++;
		
		g1.setColor(Color.RED);

		g1.fillRect(0,0,Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		g1.setColor(Color.BLUE);

		g1.fillRect(x, 10, 100, 100);
		
		g.drawImage(bufer, 0,0,null);

	}

	@Override
	public void run() {

		double referencia = System.nanoTime();

		while (iniciar) {

			final double tiempoInicial = System.nanoTime();

			delta = (float) (tiempoInicial - referencia) / UNIDAD_TIEMPO;
			
			
			//Aquí actualización y dibújo
			
			
			repaint();
			
			
			
			//Hasta aquí
			
			
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
