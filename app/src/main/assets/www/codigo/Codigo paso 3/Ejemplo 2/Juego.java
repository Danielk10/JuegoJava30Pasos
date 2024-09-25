package com.diamon.nucleo;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

public abstract class Juego extends JFrame {

	public final static int ANCHO_PANTALLA = 640;

	public final static int ALTO_PANTALLA = 480;

	public Juego() {

	

		super("Ventana");

		setSize(Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);
		
		setDefaultCloseOperation(Juego.EXIT_ON_CLOSE); 

		setVisible(true);

	
	
	}

	@Override
	public void paint(Graphics g) {

		super.paint(g);

	
		g.setColor(Color.BLUE);

		g.fillRect(10, 10, 100, 100);

	}



}
