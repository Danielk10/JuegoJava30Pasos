package com.diamon.nucleo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

public abstract class Juego extends Canvas implements Runnable {

	public final static int ANCHO_PANTALLA = 640;

	public final static int ALTO_PANTALLA = 480;

	private Thread hilo;

	private int x;

	private JFrame ventana;

	private volatile boolean iniciar;

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

		while (iniciar) {

			repaint();
			
			try {
				Thread.sleep(60);
				
			} catch (InterruptedException e) {
				
			} 

		}

	}

	public void iniciar() {
		hilo.start();
		iniciar = true;

	}

}
