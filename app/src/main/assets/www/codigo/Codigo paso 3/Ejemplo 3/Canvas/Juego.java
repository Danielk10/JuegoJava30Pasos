package com.diamon.nucleo;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

public abstract class Juego extends Canvas {

	public final static int ANCHO_PANTALLA = 640;

	public final static int ALTO_PANTALLA = 480;
	
	private JFrame ventana;

	public Juego() {
		super();
		
		setSize(Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);
		
		ventana = new JFrame("Ventana");

		ventana.setSize(Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);
		
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		ventana.add(this);

		ventana.setVisible(true);

		
	}

	@Override
	public void paint(Graphics g) {

		super.paint(g);
		
		g.setColor(Color.BLUE);

		g.fillRect(10, 10, 100, 100);

		
	}

}
