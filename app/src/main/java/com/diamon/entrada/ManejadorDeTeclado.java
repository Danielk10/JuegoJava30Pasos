package com.diamon.entrada;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

import com.diamon.nucleo.Entrada.EventoDeTecla;
import com.diamon.utilidad.Pool;
import com.diamon.utilidad.Pool.PoolObjectFactory;

import java.util.ArrayList;
import java.util.List;

public class ManejadorDeTeclado implements OnKeyListener {

    private boolean[] TeclasPrecionadas;

    private Pool<EventoDeTecla> eventoDeTeclaPool;

    private List<EventoDeTecla> eventosDeTeclaBufer;

    private List<EventoDeTecla> eventosDeTecla;

    public ManejadorDeTeclado(View vista) {

        TeclasPrecionadas = new boolean[128];

        eventosDeTeclaBufer = new ArrayList<EventoDeTecla>();

        eventosDeTecla = new ArrayList<EventoDeTecla>();

        PoolObjectFactory<EventoDeTecla> factory =
                new PoolObjectFactory<EventoDeTecla>() {

                    @Override
                    public EventoDeTecla crearObjeto() {

                        return new EventoDeTecla();
                    }
                };

        eventoDeTeclaPool = new Pool<EventoDeTecla>(factory, 100);

        vista.setOnKeyListener(this);

        vista.setFocusableInTouchMode(true);

        vista.requestFocus();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onKey(View vista, int codigoDeTecla, KeyEvent eventoDeTecla) {

        if (eventoDeTecla.getAction() == KeyEvent.ACTION_MULTIPLE) {

            return false;
        }
        synchronized (this) {
            EventoDeTecla eventoTecla = eventoDeTeclaPool.nuevoObjeto();

            eventoTecla.codigoDeTecla = codigoDeTecla;

            eventoTecla.caracterDeTecla = (char) eventoDeTecla.getUnicodeChar();

            if (eventoDeTecla.getAction() == KeyEvent.ACTION_DOWN) {

                eventoTecla.tipoEventoDeTecla = EventoDeTecla.TECLA_ABAJO;

                if (codigoDeTecla > 0 && codigoDeTecla < 127) {

                    TeclasPrecionadas[codigoDeTecla] = true;
                }
            }

            if (eventoDeTecla.getAction() == KeyEvent.ACTION_UP) {

                eventoTecla.tipoEventoDeTecla = EventoDeTecla.TECLA_ARRIBA;

                if (codigoDeTecla > 0 && codigoDeTecla < 127) {

                    TeclasPrecionadas[codigoDeTecla] = false;
                }
            }

            eventosDeTeclaBufer.add(eventoTecla);

            return false;
        }
    }

    public boolean isTeclaPrecionada(int codigoDeTecla) {

        synchronized (this) {
            if (codigoDeTecla < 0 || codigoDeTecla > 127) {

                TeclasPrecionadas[codigoDeTecla] = false;
            }

            return TeclasPrecionadas[codigoDeTecla];
        }
    }

    public List<EventoDeTecla> getEventosDeTecla() {

        synchronized (this) {
            int tamano = eventosDeTecla.size();

            for (int i = 0; i < tamano; i++) {

                eventoDeTeclaPool.objetoLibre(eventosDeTecla.get(i));
            }

            eventosDeTecla.clear();

            eventosDeTecla.addAll(eventosDeTeclaBufer);

            eventosDeTeclaBufer.clear();

            return eventosDeTecla;
        }
    }
}
