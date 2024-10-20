/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.diamon.graficos;

import com.diamon.nucleo.Textura;

public class Animacion2D {

    public static final int REPETIR = 0;

    public static final int NORMAL = 1;

    private Textura[] texturas;

    private float duracionCuadros;

    private int modo;

    private boolean finAnimcion;

    public Animacion2D(float duracionCuadros, Textura... texturas) {

        this.duracionCuadros = duracionCuadros;

        this.texturas = texturas;

        modo = NORMAL;

        finAnimcion = false;
    }

    public void setFinAnimcion(boolean finAnimcion) {
        this.finAnimcion = finAnimcion;
    }

    public boolean isFinAnimcion() {
        return finAnimcion;
    }

    public void setTexturas(Textura... texturas) {

        this.texturas = texturas;
    }

    public void setDuracionCuadros(float duracionCuadros) {

        this.duracionCuadros = duracionCuadros;
    }

    public int getModo() {
        return modo;
    }

    public void setModo(int modo) {
        this.modo = modo;
    }

    public Textura getKeyFrame(float tiempo) {

        int numeroCuadros = (int) (tiempo / duracionCuadros);

        if (modo == NORMAL) {

            numeroCuadros = Math.min(texturas.length - 1, numeroCuadros);
        }

        if (modo == REPETIR) {

            numeroCuadros = numeroCuadros % texturas.length;
        }

        if (texturas.length == numeroCuadros + 1) {

            finAnimcion = true;

        } else {

            finAnimcion = false;
        }

        return texturas[numeroCuadros];
    }
}
