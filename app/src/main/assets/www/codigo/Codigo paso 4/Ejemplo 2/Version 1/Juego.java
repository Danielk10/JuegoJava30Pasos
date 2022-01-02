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
