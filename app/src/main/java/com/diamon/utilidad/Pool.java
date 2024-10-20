package com.diamon.utilidad;

import java.util.ArrayList;
import java.util.List;

public class Pool<T> {

    public interface PoolObjectFactory<T> {

        T crearObjeto();
    }

    private final PoolObjectFactory<T> factory;

    private final List<T> objetosLibres;

    private final int tamanoMaximoDePool;

    public Pool(PoolObjectFactory<T> factory, int tamanoMaximo) {

        this.factory = factory;

        this.tamanoMaximoDePool = tamanoMaximo;

        this.objetosLibres = new ArrayList<T>(tamanoMaximo);
    }

    public T nuevoObjeto() {

        if (this.objetosLibres.size() == 0) {

            return this.factory.crearObjeto();
        }
        return this.objetosLibres.remove(this.objetosLibres.size() - 1);
    }

    public void objetoLibre(T objeto) {

        if (this.objetosLibres.size() < this.tamanoMaximoDePool) {

            this.objetosLibres.add(objeto);
        }
    }
}
