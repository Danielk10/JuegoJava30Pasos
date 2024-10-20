package com.diamon.entrada;

import android.view.MotionEvent;
import android.view.View;

import com.diamon.nucleo.Entrada.EventoDeToque;
import com.diamon.utilidad.Pool;
import com.diamon.utilidad.Pool.PoolObjectFactory;

import java.util.ArrayList;
import java.util.List;

public class ManejadorDeMultiplesToque implements ManejadorDeToque {

    boolean tocando[];

    private float[] x;

    private float[] y;

    private float escalaX;

    private float escalaY;

    private Pool<EventoDeToque> eventoDeToquePool;

    private List<EventoDeToque> eventosDeToqueBufer;

    private List<EventoDeToque> eventosDeToque;

    public ManejadorDeMultiplesToque(View vista, float escalaX, float escalaY) {

        x = new float[20];

        y = new float[20];

        tocando = new boolean[20];

        eventosDeToqueBufer = new ArrayList<EventoDeToque>();

        eventosDeToque = new ArrayList<EventoDeToque>();

        PoolObjectFactory<EventoDeToque> factory =
                new PoolObjectFactory<EventoDeToque>() {

                    @Override
                    public EventoDeToque crearObjeto() {

                        return new EventoDeToque();
                    }
                };

        eventoDeToquePool = new Pool<EventoDeToque>(factory, 100);

        vista.setOnTouchListener(this);

        this.escalaX = escalaX;

        this.escalaY = escalaY;
    }

    @SuppressWarnings("unused")
    @Override
    public boolean onTouch(View vista, MotionEvent eventoDeMovimiento) {

        synchronized (this) {
            int accion = eventoDeMovimiento.getAction() & MotionEvent.ACTION_MASK;

            int punteroIndice =
                    (eventoDeMovimiento.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                            >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

            int punteroId = eventoDeMovimiento.getPointerId(punteroIndice);

            EventoDeToque eventoDeToque;

            switch (accion) {
                case MotionEvent.ACTION_POINTER_UP:
                    eventoDeToque = eventoDeToquePool.nuevoObjeto();

                    eventoDeToque.tipoEventoDeToque = EventoDeToque.TOQUE_ARRIBA;

                    eventoDeToque.puntero = punteroId;

                    eventoDeToque.x =
                            x[punteroId] = eventoDeMovimiento.getX(punteroIndice) * escalaX;

                    eventoDeToque.y =
                            y[punteroId] = eventoDeMovimiento.getY(punteroIndice) * escalaY;

                    tocando[punteroId] = false;

                    eventosDeToqueBufer.add(eventoDeToque);

                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    eventoDeToque = eventoDeToquePool.nuevoObjeto();

                    eventoDeToque.tipoEventoDeToque = EventoDeToque.TOQUE_ABAJO;

                    eventoDeToque.puntero = punteroId;

                    eventoDeToque.x =
                            x[punteroId] = eventoDeMovimiento.getX(punteroIndice) * escalaX;

                    eventoDeToque.y =
                            y[punteroId] = eventoDeMovimiento.getY(punteroIndice) * escalaY;

                    tocando[punteroId] = true;

                    eventosDeToqueBufer.add(eventoDeToque);

                    break;

                case MotionEvent.ACTION_UP:
                    eventoDeToque = eventoDeToquePool.nuevoObjeto();

                    eventoDeToque.tipoEventoDeToque = EventoDeToque.TOQUE_ARRIBA;

                    eventoDeToque.puntero = punteroId;

                    eventoDeToque.x =
                            x[punteroId] = eventoDeMovimiento.getX(punteroIndice) * escalaX;

                    eventoDeToque.y =
                            y[punteroId] = eventoDeMovimiento.getY(punteroIndice) * escalaY;

                    tocando[punteroId] = false;

                    eventosDeToqueBufer.add(eventoDeToque);

                    break;

                case MotionEvent.ACTION_DOWN:
                    eventoDeToque = eventoDeToquePool.nuevoObjeto();

                    eventoDeToque.tipoEventoDeToque = EventoDeToque.TOQUE_ABAJO;

                    eventoDeToque.puntero = punteroId;

                    eventoDeToque.x =
                            x[punteroId] = eventoDeMovimiento.getX(punteroIndice) * escalaX;

                    eventoDeToque.y =
                            y[punteroId] = eventoDeMovimiento.getY(punteroIndice) * escalaY;

                    tocando[punteroId] = true;

                    eventosDeToqueBufer.add(eventoDeToque);

                    break;

                case MotionEvent.ACTION_CANCEL:
                    eventoDeToque = eventoDeToquePool.nuevoObjeto();

                    eventoDeToque.tipoEventoDeToque = EventoDeToque.TOQUE_ARRIBA;

                    eventoDeToque.puntero = punteroId;

                    eventoDeToque.x =
                            x[punteroId] = eventoDeMovimiento.getX(punteroIndice) * escalaX;

                    eventoDeToque.y =
                            y[punteroId] = eventoDeMovimiento.getY(punteroIndice) * escalaY;

                    tocando[punteroId] = false;

                    eventosDeToqueBufer.add(eventoDeToque);

                    break;

                case MotionEvent.ACTION_MOVE:
                    int contadorPuntero = eventoDeMovimiento.getPointerCount();

                    for (int i = 0; i < contadorPuntero; i++) {

                        punteroIndice = i;

                        punteroId = eventoDeMovimiento.getPointerId(punteroIndice);

                        eventoDeToque = eventoDeToquePool.nuevoObjeto();

                        eventoDeToque.tipoEventoDeToque = EventoDeToque.TOQUE_DESLIZANDO;

                        eventoDeToque.puntero = punteroId;

                        eventoDeToque.x =
                                x[punteroId] = eventoDeMovimiento.getX(punteroIndice) * escalaX;

                        eventoDeToque.y =
                                y[punteroId] = eventoDeMovimiento.getY(punteroIndice) * escalaY;

                        eventosDeToqueBufer.add(eventoDeToque);
                    }

                    break;

                default:
                    break;
            }

            return true;
        }
    }

    @Override
    public boolean isToque(int puntero) {

        synchronized (this) {
            if (puntero < 0 || puntero >= 20) {

                return false;

            } else {

                return tocando[puntero];
            }
        }
    }

    @Override
    public float getToqueEnX(int puntero) {

        synchronized (this) {
            if (puntero < 0 || puntero >= 20) {

                return 0;

            } else {

                return x[puntero];
            }
        }
    }

    @Override
    public float getToqueEnY(int puntero) {

        synchronized (this) {
            if (puntero < 0 || puntero >= 20) {

                return 0;

            } else {

                return y[puntero];
            }
        }
    }

    @Override
    public List<EventoDeToque> getEventosDeToque() {

        synchronized (this) {
            int tamano = eventosDeToque.size();

            for (int i = 0; i < tamano; i++) {

                eventoDeToquePool.objetoLibre(eventosDeToque.get(i));
            }

            eventosDeToque.clear();

            eventosDeToque.addAll(eventosDeToqueBufer);

            eventosDeToqueBufer.clear();

            return eventosDeToque;
        }
    }
}
