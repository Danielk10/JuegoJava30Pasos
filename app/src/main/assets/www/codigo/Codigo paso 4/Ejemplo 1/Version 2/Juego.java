package com.diamon.nucleo;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

public abstract class Juego extends JFrame implements Runnable{

	public final static int ANCHO_PANTALLA = 640;

	public final static int ALTO_PANTALLA = 480;
	
	private Thread hilo;
	
	private int x;

	public Juego() {
		super("Ventana");

		setSize(Juego.ANCHO_PANTALLA, Juego.ALTO_PANTALLA);

		
		setDefaultCloseOperation(Juego.EXIT_ON_CLOSE); 
		
		setVisible(true);


		
		
		x = 0;
		
		hilo = new Thread(this);

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
		
		while(true)
		{
			
			repaint();
			
		}
		
		
	}

	public void iniciar()
	{
		hilo.start();
		
	}

}
