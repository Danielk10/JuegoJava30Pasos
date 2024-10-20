package com.diamon.entrada;

import android.view.MotionEvent;
import android.view.View;

import com.diamon.nucleo.Entrada.EventoDeToque;
import com.diamon.utilidad.Pool;
import com.diamon.utilidad.Pool.PoolObjectFactory;

import java.util.ArrayList;
import java.util.List;

public class ManejadorDeUnToque implements ManejadorDeToque {

    boolean tocando;

    private float x;

    private float y;

    private float escalaX;

    private float escalaY;

    private Pool<EventoDeToque> eventoDeToquePool;

    private List<EventoDeToque> eventosDeToqueBufer;

    private List<EventoDeToque> eventosDeToque;

    public ManejadorDeUnToque(View vista, float escalaX, float escalaY) {

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
            EventoDeToque eventoDeToque = eventoDeToquePool.nuevoObjeto();

            switch (eventoDeMovimiento.getAction()) {
                case MotionEvent.ACTION_UP:
                    eventoDeToque.tipoEventoDeToque = EventoDeToque.TOQUE_ARRIBA;

                    tocando = false;

                    break;

                case MotionEvent.ACTION_DOWN:
                    eventoDeToque.tipoEventoDeToque = EventoDeToque.TOQUE_ABAJO;

                    tocando = true;

                    break;

                case MotionEvent.ACTION_CANCEL:
                    break;

                case MotionEvent.ACTION_MOVE:
                    eventoDeToque.tipoEventoDeToque = EventoDeToque.TOQUE_DESLIZANDO;

                    tocando = true;

                    break;

                default:
                    break;
            }

            eventoDeToque.x = x = eventoDeMovimiento.getX() * escalaX;

            eventoDeToque.y = y = eventoDeMovimiento.getY() * escalaY;

            eventosDeToqueBufer.add(eventoDeToque);

            return true;
        }
    }

    @Override
    public boolean isToque(int puntero) {

        synchronized (this) {
            if (puntero == 0) {

                return tocando;

            } else {

                return false;
            }
        }
    }

    @Override
    public float getToqueEnX(int puntero) {

        synchronized (this) {
            return x;
        }
    }

    @Override
    public float getToqueEnY(int puntero) {

        synchronized (this) {
            return y;
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
