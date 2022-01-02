package com.diamon.nucleo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class Juego extends Canvas implements Runnable {

	public final static int ANCHO_PANTALLA = 640;

	public final static int ALTO_PANTALLA = 480;

	private Thread hilo;

	public static final String TITULO_JUEGO = "Ventana";

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

		setBackground(Color.BLACK);

		requestFocus();

		setFocusable(true);

		setIgnoreRepaint(true);

		hilo = new Thread(this);

		ventana = new JFrame(Juego.TITULO_JUEGO);

		ventana.setSize(Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Mejor rendimiento que canavas
		JPanel panel = (JPanel) ventana.getContentPane();

		panel.setSize(Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		panel.setLayout(null);

		panel.add(this);

		// Mejor rendimiento que canavas

		ventana.setLayout(null);

		ventana.setUndecorated(true);

		ventana.setResizable(false);

		ventana.setVisible(true);

		DisplayMode[] dis = ventana.getGraphicsConfiguration().getDevice().getDisplayModes();

		GraphicsDevice g = ventana.getGraphicsConfiguration().getDevice();

		ventana.getGraphicsConfiguration().getDevice().setFullScreenWindow(ventana);

		for (int i = 0; i < dis.length; i++) {

			if (dis != null && g.isDisplayChangeSupported()) {

				try {

					if ((dis[i].getWidth() == 640) && (dis[i].getHeight() == 480) && (dis[i].getBitDepth() == 32)
							&& (dis[i].getRefreshRate() == 60)) {

						g.setDisplayMode(dis[i]);

					}

				} catch (Exception e) {

				}

			}

		}

		createBufferStrategy(2);

		bufer = getBufferStrategy();

		iniciar = false;

	}

	@Override
	public void run() {

		final Graphics2D pincel = (Graphics2D) bufer.getDrawGraphics();

		double referencia = System.nanoTime();

		while (iniciar) {

			final double tiempoInicial = System.nanoTime();

			delta = (float) (tiempoInicial - referencia) / UNIDAD_TIEMPO;

			// Aquí actualización y dibújo

			x++;

			pincel.setColor(Color.RED);

			pincel.fillRect(0, 0, Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

			pincel.setColor(Color.BLUE);

			pincel.fillRect(x, 10, 100, 100);

			bufer.show();
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
