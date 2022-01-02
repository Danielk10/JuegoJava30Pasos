package com.diamon.nucleo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.diamon.dato.Datos;
import com.diamon.graficos.JuegoME;
import com.diamon.utilidad.Recurso;

public abstract class Juego extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = -2561463112024866241L;

	private Thread hilo;

	private JFrame ventana;

	public static final String TITULO_JUEGO = "JuegoMe";

	public static final int ANCHO_PANTALLA = 640;

	public static final int ALTO_PANTALLA = 480;

	private final static int UNIDAD_TIEMPO = 1000000000;

	private float delta = UNIDAD_TIEMPO;

	private final static byte CICLOS = 60;

	private final static float LIMITE_CICLOS = UNIDAD_TIEMPO / CICLOS;

	private BufferStrategy bufer;

	private volatile boolean iniciar;

	private Pantalla pantalla;

	private Recurso recurso;

	private Datos datos;

	public Juego() {
		super();

		datos = new Datos(Datos.DATOS);

		setSize(Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		setBackground(Color.BLACK);

		recurso = new Recurso();

		BufferedImage cursor = recurso.createCompatible(10, 10, Transparency.BITMASK);

		cursor.getGraphics().drawImage(recurso.cargarImagen("burbuja.png"), 0, 0, 5, 5, this);

		Toolkit t = Toolkit.getDefaultToolkit();
		Cursor c = t.createCustomCursor(cursor, new Point(5, 5), "null");
		setCursor(c);

		requestFocus();

		setFocusable(true);

		setIgnoreRepaint(true);

		addKeyListener(this);

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

		//ventana.getGraphicsConfiguration().getDevice().setFullScreenWindow(ventana);

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

		pantalla = null;

		iniciar = false;

	}

	public void iniciar() {

		hilo.start();

		iniciar = true;

	}

	public void renderizar(Graphics2D pincel, float delta) {
		if (pantalla != null) {
			pantalla.dibujar(pincel, delta);

		}

		pincel.setColor(Color.BLUE);

		pincel.drawString(getFPS() + " FPS", 0, 10);
	}

	public void actualizar(float delta) {
		if (pantalla != null) {
			pantalla.actualizar(delta);

		}
	}

	public void reajustarPantalla(int ancho, int alto) {
		if (pantalla != null) {
			pantalla.reajustarPantalla(ancho, ancho);
		}
	}

	public void resumen() {
		if (pantalla != null) {

			iniciar = true;
			hilo = new Thread(this);
			hilo.start();
			pantalla.resume();
		}
	}

	public void pausa() {
		if (pantalla != null) {

			pantalla.pausa();
			iniciar = false;
			while (true) {
				try {
					hilo.join();

					return;

				} catch (InterruptedException e) {

				}

			}

		}
	}

	public void liberarRecursos() {
		if (pantalla != null) {
			pantalla.ocultar();
		}
	}

	public void setPantalla(Pantalla pantalla) {
		if (this.pantalla != null) {
			this.pantalla.ocultar();
		}

		this.pantalla = pantalla;

		if (this.pantalla != null) {
			this.pantalla.mostrar();
			this.pantalla.reajustarPantalla(getWidth(), getHeight());

		}
	}

	public Pantalla getPantalla() {
		return pantalla;

	}

	@Override
	public void run() {

		final Graphics2D pincel = (Graphics2D) bufer.getDrawGraphics();

		double referencia = System.nanoTime();

		while (iniciar) {

			final double tiempoInicial = System.nanoTime();
			
			

			delta = (float) (tiempoInicial - referencia) / UNIDAD_TIEMPO;

			pincel.setColor(Color.BLACK);

			pincel.fillRect(0, 0, JuegoME.ANCHO_PANTALLA, JuegoME.ALTO_PANTALLA);

			colisiones();

			actualizar((float) delta);

			renderizar(pincel, (float) delta);

			bufer.show();

			Toolkit.getDefaultToolkit().sync();
			// Guardo la referencia

			referencia = tiempoInicial;

			// Limite de cudros

			do {

				Thread.yield();

			} while (System.nanoTime() - tiempoInicial < LIMITE_CICLOS);

		}

	}

	public void colisiones() {
		if (pantalla != null) {
			pantalla.colisiones();

		}

	}

	public int getFPS() {

		return (int) (1 / delta);
	}

	@Override
	public void keyPressed(KeyEvent ev) {
		if (pantalla != null) {
			pantalla.teclaPresionada(ev);

		}

		if (ev.getKeyCode() == KeyEvent.VK_ESCAPE) {

			System.exit(0);

		}

	}

	@Override
	public void keyReleased(KeyEvent ev) {
		if (pantalla != null) {
			pantalla.teclaLevantada(ev);

		}

	}

	@Override
	public void keyTyped(KeyEvent ev) {
		if (pantalla != null) {
			pantalla.teclaTipo(ev);

		}

	}

	public Datos getDatos() {
		return datos;
	}

}
