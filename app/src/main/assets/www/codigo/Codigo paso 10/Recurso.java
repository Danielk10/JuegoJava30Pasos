package com.diamon.utilidad;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Recurso implements ImageObserver {

	private HashMap<String, BufferedImage> imagenes = new HashMap<String, BufferedImage>();

	private HashMap<String, AudioClip> sonidos = new HashMap<String, AudioClip>();

	public Recurso() {

	}

	public BufferedImage cargarImagen(String nombre) {
		URL rutaImagen = getClass().getClassLoader().getResource("res/" + nombre);
		BufferedImage imagen;
		try {
			imagen = ImageIO.read(rutaImagen);

			BufferedImage compatible = createCompatible(imagen.getWidth(), imagen.getHeight(), Transparency.BITMASK);
			Graphics2D g = (Graphics2D) compatible.getGraphics();
			g.drawImage(imagen, 0, 0, this);

			imagenes.put(nombre, compatible);

			return imagenes.get(nombre);

		} catch (IOException e) {
			return null;
		}

	}

	public AudioClip cargarSonido(String nombre) {
		URL rutaSonido = getClass().getClassLoader().getResource("res/" + nombre);
		AudioClip sonido;
		try {
			sonido = Applet.newAudioClip(rutaSonido);

			sonidos.put(nombre, sonido);

			return sonidos.get(nombre);

		} catch (Exception e) {
			return null;
		}

	}

	public AudioClip getSonido(String nombre) {
		AudioClip sonido;
		sonido = (AudioClip) sonidos.get(nombre);

		if (sonido == null) {

			sonido = cargarSonido(nombre);

			sonidos.put(nombre, sonido);
		}

		return sonido;

	}

	public void playSonido(final String nombre) {
		new Thread(new Runnable() {
			public void run() {
				getSonido(nombre).play();
			}
		}).start();
	}

	public void repetirSonido(final String nombre) {
		new Thread(new Runnable() {
			public void run() {
				getSonido(nombre).loop();
			}
		}).start();
	}

	public BufferedImage getImagen(String nombre) {
		BufferedImage imagen;
		imagen = (BufferedImage) imagenes.get(nombre);

		if (imagen == null) {

			imagen = cargarImagen(nombre);

			imagenes.put(nombre, imagen);
		}

		return imagen;

	}

	public BufferedImage createCompatible(int width, int height, int transparency) {
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		BufferedImage compatible = gc.createCompatibleImage(width, height, transparency);
		return compatible;
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
		return (infoflags & (ALLBITS | ABORT)) == 0;
	}

}
