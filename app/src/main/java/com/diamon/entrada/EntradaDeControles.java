package com.diamon.entrada;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

import com.diamon.nucleo.Entrada;

import java.util.List;

public class EntradaDeControles implements Entrada {

    private ManejadorDeAcelerometro manejadorDeAcelerometro;

    private ManejadorDeTeclado manejadorDeTeclado;

    private ManejadorDeToque manejadorDeToque;

    public EntradaDeControles(Context contexto, View vista, float escalaX, float escalaY) {

        manejadorDeAcelerometro = new ManejadorDeAcelerometro(contexto);

        manejadorDeTeclado = new ManejadorDeTeclado(vista);

        if (VERSION.SDK_INT > 5) {

            manejadorDeToque = new ManejadorDeMultiplesToque(vista, escalaX, escalaY);

        } else {

            manejadorDeToque = new ManejadorDeUnToque(vista, escalaX, escalaY);
        }
    }

    @Override
    public boolean isTeclaPrecionada(int codigoDeTecla) {

        return manejadorDeTeclado.isTeclaPrecionada(codigoDeTecla);
    }

    @Override
    public boolean isToque(int puntero) {

        return manejadorDeToque.isToque(puntero);
    }

    @Override
    public float getToqueEnX(int puntero) {

        return manejadorDeToque.getToqueEnX(puntero);
    }

    @Override
    public float getToqueEnY(int puntero) {

        return manejadorDeToque.getToqueEnY(puntero);
    }

    @Override
    public float getAcelerometroEnX() {

        return manejadorDeAcelerometro.getAcelerometroEnX();
    }

    @Override
    public float getAcelerometroEnY() {

        return manejadorDeAcelerometro.getAcelerometroEnY();
    }

    @Override
    public float getAcelerometroEnZ() {

        return manejadorDeAcelerometro.getAcelerometroEnZ();
    }

    @Override
    public List<EventoDeTecla> getEventosDeTecla() {

        return manejadorDeTeclado.getEventosDeTecla();
    }

    @Override
    public List<EventoDeToque> getEventosDeToque() {

        return manejadorDeToque.getEventosDeToque();
    }
}
