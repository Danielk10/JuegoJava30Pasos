package com.diamon.nucleo;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public abstract class Juego extends Frame {

	public final static int ANCHO_PANTALLA = 640;

	public final static int ALTO_PANTALLA = 480;

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

	}

	@Override
	public void paint(Graphics g) {

		g.setColor(Color.BLUE);

		g.fillRect(10, 10, 100, 100);

	}

}
