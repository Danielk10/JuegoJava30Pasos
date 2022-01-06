package com.diamon.utilidad;

import android.graphics.RectF;

public class Rectangulo extends RectF {

	private float X1, Y1; // Posici�n del rect�ngulo m�vil
	private float P1, Q1; // Posici�n del rect�ngulo est�tico
	// Base y altura del rect�ngulo 1
	private float BaseRect1, AlturaRect1;
	// Base y altura del rect�ngulo 2
	private float BaseRect2, AlturaRect2;

	public Rectangulo(float x, float y, float ancho, float alto) {
		super((int) x, (int) y, (int) ancho, (int) alto);

		X1 = x;
		Y1 = y;
		BaseRect1 = ancho;
		AlturaRect1 = alto;

	}

	public boolean Intersecion(Rectangulo r) {
		P1 = r.X1;
		Q1 = r.Y1;
		BaseRect2 = r.BaseRect1;
		AlturaRect2 = r.AlturaRect1;
		// Deduce el punto (X2, Y2) del rect�ngulo 1
		float X2, Y2;
		X2 = X1 + BaseRect1;
		Y2 = Y1 + AlturaRect1; // Deduce el punto (P2, Q2) del rect�ngulo 2
		float P2, Q2;
		P2 = P1 + BaseRect2;
		Q2 = Q1 + AlturaRect2; // Chequea si hay colisi�n
		if (X1 >= P1 && X1 <= P2 && Y1 >= Q1 && Y1 <= Q2)
			return true;
		if (X2 >= P1 && X2 <= P2 && Y1 >= Q1 && Y1 <= Q2)
			return true;
		if (X1 >= P1 && X1 <= P2 && Y2 >= Q1 && Y2 <= Q2)
			return true;
		if (X2 >= P1 && X2 <= P2 && Y2 >= Q1 && Y2 <= Q2)
			return true;
		if (P1 >= X1 && P1 <= X2 && Q1 >= Y1 && Q1 <= Y2)
			return true;
		if (P2 >= X1 && P2 <= X2 && Q1 >= Y1 && Q1 <= Y2)
			return true;
		if (P1 >= X1 && P1 <= X2 && Q2 >= Y1 && Q2 <= Y2)
			return true;
		if (P2 >= X1 && P2 <= X2 && Q2 >= Y1 && Q2 <= Y2)
			return true;
		// No se detect� colisi�n
		return false;
	}

	public boolean IntersecionR(Rectangulo r) {
		if (X1 + BaseRect1 < r.X1) {
			return false;
		}
		if (Y1 + AlturaRect1 < r.Y1) {
			return false;
		}
		if (X1 > r.X1 + r.BaseRect1) {
			return false;
		}
		if (Y1 > r.Y1 + r.AlturaRect1) {
			return false;
		}
		return true;

	}

}
