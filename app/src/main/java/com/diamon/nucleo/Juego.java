package com.diamon.nucleo;

import com.diamon.dato.Configuraciones;
import com.diamon.utilidad.Recurso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;

public abstract class Juego extends SurfaceView
		implements Runnable, OnKeyListener, SensorEventListener, OnTouchListener, SurfaceHolder.Callback {

	public final static int ANCHO_PANTALLA = 640;

	public final static int ALTO_PANTALLA = 480;

	public static final float DELTA_A_PIXEL = 0.0166666666666667F;

	public static final int FPS = 60;

	private Thread hilo;

	public static final String TITULO_JUEGO = "Final Mision";

	private volatile boolean iniciar;

	private final static int UNIDAD_TIEMPO = 1000000000;

	private double delta;

	private final static byte CICLOS = 60;

	private final static double LIMITE_CICLOS = UNIDAD_TIEMPO / CICLOS;

	private Pantalla pantalla;

	private SurfaceHolder holder;

	private Paint lapiz;

	protected Recurso recurso;

	protected Camara2D camara;

	protected Configuraciones configuracion;

	private Bitmap bufer;

	@SuppressWarnings("deprecation")
	public Juego(Context contexto, Bitmap bufer) {
		super(contexto);

		configuracion = new Configuraciones();

		configuracion = configuracion.caragarConfiguraciones();

		camara = new Camara2D();

		delta = 0;

		hilo = new Thread(this);

		iniciar = false;

		pantalla = null;

		holder = getHolder();

		holder.addCallback(this);

		holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);

		this.bufer = bufer;

		lapiz = new Paint();

		recurso = new Recurso(contexto);

		setOnKeyListener(this);

		setOnTouchListener(this);

		setFocusable(true);

		requestFocus();

		setFocusableInTouchMode(true);

	}

	@Override
	public void run() {

		Rect rectangulo = new Rect();

		Canvas pincel;

		final Canvas pincelBufer = new Canvas(bufer);

		double referencia = System.nanoTime();

		while (iniciar) {

			final double tiempoInicial = System.nanoTime();

			delta = (float) (tiempoInicial - referencia) / UNIDAD_TIEMPO;

			// Aqu� actualizaci�n y dib�jo

			if (!holder.getSurface().isValid()) {
				continue;
			}

			colisiones();

			actualizar((float) delta);

			pincel = holder.getSurface().lockHardwareCanvas();

			pincel.getClipBounds(rectangulo);

			lapiz.setColor(Color.BLUE);
			
			lapiz.setStyle(Style.FILL);
			
			pincelBufer.drawRect(0, 0, ANCHO_PANTALLA, ALTO_PANTALLA, lapiz);

			renderizar(pincelBufer, (float) delta);

			pincel.drawBitmap(bufer, null, rectangulo, null);

			holder.unlockCanvasAndPost(pincel);

			// Hasta aqu�

			referencia = tiempoInicial;

			// Limite de cuadros

			do {

				Thread.yield();

			} while (System.nanoTime() - tiempoInicial < LIMITE_CICLOS);

		}

	}

	public void renderizar(Canvas pincel, float delta) {
		if (pantalla != null) {
			pantalla.dibujar(pincel, delta);

		}

		lapiz.setColor(Color.GREEN);

		pincel.drawText(getFPS() + " FPS", 0, 20, lapiz);
	}

	public void actualizar(float delta) {
		if (pantalla != null) {
			pantalla.actualizar(delta);

		}
	}

	public void colisiones() {
		if (pantalla != null) {
			pantalla.colisiones();

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
			pantalla.mostrar();
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
	public boolean onKey(View v, int tecla, KeyEvent ev) {

		if (pantalla != null) {

			pantalla.teclaPresionada(ev);

			pantalla.teclaLevantada(ev);

		}
		return false;
	}

	@Override
	public void onAccuracyChanged(Sensor s, int c) {

	}

	@Override
	public void onSensorChanged(SensorEvent ev) {

		if (pantalla != null) {

			pantalla.acelerometro(ev);

		}

	}

	@SuppressWarnings("unused")
	@Override
	public boolean onTouch(View v, MotionEvent ev) {

		if (pantalla != null) {

			pantalla.toque(ev);

			// Multi Toque

			int accion = ev.getAction() & MotionEvent.ACTION_MASK;

			int punteroIndice = (ev.getAction()
					& MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

			int punteroID = ev.getPointerId(punteroIndice);

			pantalla.multiToque(ev);

		}

		return true;
	}

	public int getFPS() {

		return (int) (1 / delta);

	}

	@Override
	public void surfaceCreated(SurfaceHolder p1) {
		// TODO: Implement this method
	}

	@Override
	public void surfaceChanged(SurfaceHolder p1, int p2, int p3, int p4) {
		// TODO: Implement this method
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder p1) {
		// TODO: Implement this method
	}

}
