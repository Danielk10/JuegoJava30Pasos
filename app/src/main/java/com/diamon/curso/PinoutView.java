package com.diamon.curso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.ImageView;

import com.diamon.graficos.Graficos2D;
import com.diamon.graficos.Textura2D;
import com.diamon.nucleo.Graficos;

/**
 * PinoutView: dibuja diagramas de pinout de hardware usando
 * Graficos2D/Textura2D
 * completamente en memoria (sin cargar recursos externos).
 *
 * Uso:
 * ImageView iv = new ImageView(context);
 * PinoutView.dibujarCH341A(context, iv);
 */
public class PinoutView {

    // Paleta de colores del tema oscuro de la app
    private static final int COL_BG = 0xFF1B1E2B;
    private static final int COL_PANEL = 0xFF252A3C;
    private static final int COL_BORDE = 0xFF546E7A;
    private static final int COL_LINEA = 0xFF78909C;
    private static final int COL_TITULO = 0xFFCFD8DC;
    private static final int COL_LABEL = 0xFFB0BEC5;
    private static final int COL_PIN_NUM = 0xFF80CBC4; // teal para números de pin
    private static final int COL_AVISO = 0xFFFF7043; // naranja para advertencias
    private static final int COL_VCC = 0xFFEF5350; // rojo VCC
    private static final int COL_GND = 0xFF78909C; // gris GND
    private static final int COL_SPI = 0xFF66BB6A; // verde señales SPI
    private static final int COL_CHIP = 0xFF1565C0; // azul cuerpo del chip

    private static final int W = 520;
    private static final int H = 380;

    // ────────── Fábrica pública ──────────────────────────────────────────────

    public static void dibujarCH341A(Context ctx, ImageView target) {
        Textura2D tex = crearTextura();
        Graficos2D g = new Graficos2D(tex);
        dibujarHeaderPinout(g, "CH341A Mini Programmer — Header SPI");
        dibujarChipSOIC8(g, 40, 90, new String[] { "CS", "MISO", "WP", "GND", "MOSI", "CLK", "HOLD", "VCC" }, true);
        dibujarPinHeader(g, 290, 90, new String[] { "CS", "MISO", "WP", "GND", "MOSI", "CLK", "HOLD", "VCC" });
        dibujarNota(g, "⚠ Jumper 1-2: SPI  ⚠ Jumper 2-3: UART/I2C  ⚠ SOLO 3.3V");
        aplicar(tex, target);
        tex.dispose();
    }

    public static void dibujarSOIC8(Context ctx, ImageView target) {
        Textura2D tex = crearTextura();
        Graficos2D g = new Graficos2D(tex);
        dibujarHeaderPinout(g, "Chip Flash SOIC8 / DIP8 — Conexión a CH341A");
        dibujarChipSOIC8(g, 54, 90, new String[] { "CS", "DO", "WP", "GND", "DI", "CLK", "HOLD", "VCC" }, false);
        dibujarFlecha(g, 270, 190);
        dibujarTablaConexion(g, 310, 80,
                new String[] { "1-CS", "2-DO", "3-WP", "4-GND", "5-DI", "6-CLK", "7-HOLD", "8-VCC" },
                new String[] { "1-CS", "2-MISO", "3-WP(VCC)", "4-GND", "5-MOSI", "6-CLK", "7-HOLD(VCC)", "8-VCC" });
        dibujarNota(g, "⚠ HOLD y WP → VCC si no se usan  ⚠ 3.3V máximo");
        aplicar(tex, target);
        tex.dispose();
    }

    public static void dibujarSPI(Context ctx, ImageView target) {
        Textura2D tex = crearTextura();
        Graficos2D g = new Graficos2D(tex);
        dibujarHeaderPinout(g, "Bus SPI — Serial Peripheral Interface");
        dibujarBusSPI(g);
        dibujarNota(g, "CPOL=0, CPHA=0 (Modo 0)  |  Velocidad típica: 1–50 MHz");
        aplicar(tex, target);
        tex.dispose();
    }

    public static void dibujarI2C(Context ctx, ImageView target) {
        Textura2D tex = crearTextura();
        Graficos2D g = new Graficos2D(tex);
        dibujarHeaderPinout(g, "Bus I2C — Inter-Integrated Circuit");
        dibujarBusI2C(g);
        dibujarNota(g, "Pull-up 4.7 KΩ recomendado  |  100/400 KHz");
        aplicar(tex, target);
        tex.dispose();
    }

    /** Arduino UNO como programador serprog: pines SPI + conexión a chip flash */
    public static void dibujarArduinoSerprog(Context ctx, ImageView target) {
        Textura2D tex = crearTextura();
        Graficos2D g = new Graficos2D(tex);
        dibujarHeaderPinout(g, "Arduino UNO (serprog) — Conexión a Flash SPI");
        dibujarChipSOIC8(g, 54, 55, new String[] { "CS", "DO", "WP", "GND", "DI", "CLK", "HOLD", "VCC" }, false);
        dibujarFlecha(g, 270, 155);
        dibujarTablaConexionGeneral(g, 310, 50,
                "Flash Chip", "Arduino UNO",
                new String[] { "1-CS", "2-DO(MISO)", "3-WP", "4-GND", "5-DI(MOSI)", "6-CLK", "7-HOLD", "8-VCC" },
                new String[] { "Pin 10 (SS)", "Pin 12 (MISO)", "3.3V", "GND", "Pin 11 (MOSI)", "Pin 13 (SCK)", "3.3V",
                        "3.3V" });
        dibujarNota(g, "⚠ Arduino UNO es 5V — usar level shifter (HEF4050) a 3.3V");
        aplicar(tex, target);
        tex.dispose();
    }

    /** Bus Pirate como programador: pines del header a chip flash SPI */
    public static void dibujarBusPirate(Context ctx, ImageView target) {
        Textura2D tex = crearTextura();
        Graficos2D g = new Graficos2D(tex);
        dibujarHeaderPinout(g, "Bus Pirate — Conexión a Flash SPI");
        dibujarChipSOIC8(g, 54, 55, new String[] { "CS", "DO", "WP", "GND", "DI", "CLK", "HOLD", "VCC" }, false);
        dibujarFlecha(g, 270, 155);
        dibujarTablaConexionGeneral(g, 310, 50,
                "Flash Chip", "Bus Pirate",
                new String[] { "1-CS", "2-DO(MISO)", "3-WP", "4-GND", "5-DI(MOSI)", "6-CLK", "7-HOLD", "8-VCC" },
                new String[] { "CS", "MISO", "3.3V", "GND", "MOSI", "CLK", "3.3V", "3.3V (Vout)" });
        dibujarNota(g, "⚠ Bus Pirate v3: max ~8MHz SPI  ⚠ Alimentar chip desde Vout");
        aplicar(tex, target);
        tex.dispose();
    }

    /** SPIDriver como programador: header de 6 pines a chip flash SPI */
    public static void dibujarSPIDriver(Context ctx, ImageView target) {
        Textura2D tex = crearTextura();
        Graficos2D g = new Graficos2D(tex);
        dibujarHeaderPinout(g, "SPIDriver — Conexión a Flash SPI");
        dibujarChipSOIC8(g, 54, 55, new String[] { "CS", "DO", "WP", "GND", "DI", "CLK", "HOLD", "VCC" }, false);
        dibujarFlecha(g, 270, 155);
        dibujarTablaConexionGeneral(g, 310, 50,
                "Flash Chip", "SPIDriver",
                new String[] { "1-CS", "2-DO(MISO)", "3-WP", "4-GND", "5-DI(MOSI)", "6-CLK", "7-HOLD", "8-VCC" },
                new String[] { "CS (A)", "MISO", "3.3V", "GND", "MOSI", "SCK", "3.3V", "3.3V" });
        dibujarNota(g, "⚠ SPIDriver opera a 3.3V nativo — no requiere level shifter");
        aplicar(tex, target);
        tex.dispose();
    }

    // ────────── Helpers de dibujo ────────────────────────────────────────────

    private static Textura2D crearTextura() {
        return new Textura2D(W, H, Graficos.FormatoTextura.ARGB8888);
    }

    private static void dibujarHeaderPinout(Graficos2D g, String titulo) {
        g.limpiar(COL_BG);
        // Barra de título
        g.dibujarRectangulo(0, 0, W, 32, 0xFF1A237E);
        configurarTexto(g, 15f, true);
        g.dibujarTexto(titulo, 12, 22, COL_TITULO);
        // Línea separadora
        g.dibujarLinea(0, 32, W, 32, COL_BORDE);
    }

    private static void dibujarNota(Graficos2D g, String nota) {
        g.dibujarRectangulo(0, H - 26, W, 26, 0xFF1A1C27);
        g.dibujarLinea(0, H - 26, W, H - 26, COL_AVISO);
        configurarTexto(g, 11f, false);
        g.dibujarTexto(nota, 8, H - 9, COL_AVISO);
    }

    /**
     * Dibuja un chip SOIC8 con etiquetas de pines.
     * 
     * @param conPunto true = mostrar punto de identificación de pin 1
     */
    private static void dibujarChipSOIC8(Graficos2D g, float x, float y,
            String[] pines, boolean conPunto) {
        float CW = 100, CH = 170;
        float pinW = 22, pinH = 16, gap = (CH - 4 * pinH) / 5f;

        // Cuerpo del chip
        g.dibujarRectangulo(x, y, CW, CH, COL_CHIP);
        g.dibujarLinea(x, y, x + CW, y, COL_BORDE);
        g.dibujarLinea(x + CW, y, x + CW, y + CH, COL_BORDE);
        g.dibujarLinea(x, y + CH, x + CW, y + CH, COL_BORDE);
        g.dibujarLinea(x, y, x, y + CH, COL_BORDE);

        // Muesca de identificación
        g.dibujarLinea(x + CW / 2 - 14, y, x + CW / 2 + 14, y, 0xFF90CAF9);

        // Punto pin 1
        if (conPunto)
            g.dibujarPixel(x + 6, y + 10, 0xFFFFFFFF);

        configurarTexto(g, 11f, true);
        g.dibujarTexto("SOIC8", x + 24, y + CH / 2 + 5, 0xFF90CAF9);

        // Pines izquierda (1-4) y derecha (5-8)
        for (int i = 0; i < 4; i++) {
            float py = y + gap + i * (pinH + gap);

            // Izquierda (pins 1-4) — hacia la izquierda
            g.dibujarRectangulo(x - pinW, py, pinW - 2, pinH - 2, COL_PANEL);
            g.dibujarLinea(x - pinW, py, x, py, COL_LINEA);
            g.dibujarLinea(x - pinW, py + pinH, x, py + pinH, COL_LINEA);
            g.dibujarLinea(x - pinW, py, x - pinW, py + pinH, COL_LINEA);
            configurarTexto(g, 10f, false);
            g.dibujarTexto(String.valueOf(i + 1), x - pinW + 2, py + pinH - 3, COL_PIN_NUM);
            configurarTexto(g, 9f, false);
            int col = colorPin(pines[i]);
            g.dibujarTexto(pines[i], x - pinW - 38, py + pinH - 3, col);

            // Derecha (pins 8-5) — hacia la derecha, orden invertido
            int ri = 7 - i;
            float prx = x + CW + 2;
            g.dibujarRectangulo(prx, py, pinW - 2, pinH - 2, COL_PANEL);
            g.dibujarLinea(prx, py, prx + pinW, py, COL_LINEA);
            g.dibujarLinea(prx, py + pinH, prx + pinW, py + pinH, COL_LINEA);
            g.dibujarLinea(prx + pinW, py, prx + pinW, py + pinH, COL_LINEA);
            configurarTexto(g, 10f, false);
            g.dibujarTexto(String.valueOf(ri + 1), prx + 2, py + pinH - 3, COL_PIN_NUM);
            configurarTexto(g, 9f, false);
            int colR = colorPin(pines[ri]);
            g.dibujarTexto(pines[ri], prx + pinW + 4, py + pinH - 3, colR);
        }
    }

    /** Dibuja un conector de 8 pines en línea (tipo cable plano / dupont) */
    private static void dibujarPinHeader(Graficos2D g, float x, float y, String[] etiquetas) {
        float pH = 20, pW = 14, espacio = 4;
        configurarTexto(g, 11f, true);
        g.dibujarTexto("Conector SPI", x, y - 6, COL_LABEL);
        for (int i = 0; i < 8; i++) {
            float py = y + i * (pH + espacio);
            g.dibujarRectangulo(x, py, pW, pH, COL_PANEL);
            g.dibujarLinea(x, py, x + pW, py, COL_BORDE);
            g.dibujarLinea(x + pW, py, x + pW, py + pH, COL_BORDE);
            g.dibujarLinea(x, py + pH, x + pW, py + pH, COL_BORDE);
            g.dibujarLinea(x, py, x, py + pH, COL_BORDE);
            configurarTexto(g, 9f, false);
            g.dibujarTexto(String.valueOf(i + 1), x + 3, py + pH - 4, COL_PIN_NUM);
            int col = colorPin(etiquetas[i]);
            g.dibujarTexto(etiquetas[i], x + pW + 6, py + pH - 4, col);
        }
    }

    /** Diagrama de bus SPI con Master / Slave y flechas de señal */
    private static void dibujarBusSPI(Graficos2D g) {
        float mx = 50, sy = 70, bW = 140, bH = 160;

        // Bloque MASTER
        g.dibujarRectangulo(mx, sy, bW, bH, COL_CHIP);
        bordes(g, mx, sy, bW, bH, COL_BORDE);
        configurarTexto(g, 12f, true);
        g.dibujarTexto("MASTER", mx + 30, sy + 20, COL_TITULO);
        configurarTexto(g, 10f, false);
        g.dibujarTexto("CH341A", mx + 35, sy + 35, COL_LABEL);

        // Bloque SLAVE
        float sx = mx + bW + 120;
        g.dibujarRectangulo(sx, sy, bW, bH, 0xFF1B5E20);
        bordes(g, sx, sy, bW, bH, COL_BORDE);
        configurarTexto(g, 12f, true);
        g.dibujarTexto("SLAVE", sx + 35, sy + 20, COL_TITULO);
        configurarTexto(g, 10f, false);
        g.dibujarTexto("Flash Chip", sx + 28, sy + 35, COL_LABEL);

        // Señales SPI con flechas
        String[] sigs = { "MOSI", "MISO", "CLK", "CS" };
        boolean[] esEntrada = { true, false, true, true }; // true=M→S, false=S→M
        int[] cols = { COL_SPI, 0xFF29B6F6, 0xFFFFCA28, 0xFFCE93D8 };
        float yBase = sy + 60;
        float lineX1 = mx + bW, lineX2 = sx;
        float midX = (lineX1 + lineX2) / 2f;

        configurarTexto(g, 10f, false);
        for (int i = 0; i < 4; i++) {
            float fy = yBase + i * 26;
            g.dibujarLinea(lineX1, fy, lineX2, fy, cols[i]);
            // Flecha
            if (esEntrada[i]) {
                flecha(g, midX - 6, fy, midX + 8, fy, cols[i]);
            } else {
                flecha(g, midX + 6, fy, midX - 8, fy, cols[i]);
            }
            g.dibujarTexto(sigs[i], midX - 14, fy - 4, cols[i]);

            // Etiqueta en Master
            g.dibujarTexto(sigs[i], mx + bW - 38, fy + 4, cols[i]);
            // Etiqueta en Slave
            String slaveLabel = esEntrada[i] ? "→" + sigs[i] : sigs[i] + "→";
            g.dibujarTexto(slaveLabel, sx + 4, fy + 4, cols[i]);
        }

        // Descripción
        configurarTexto(g, 10f, false);
        g.dibujarTexto("MOSI = Master Out Slave In", 20, sy + bH + 22, COL_LABEL);
        g.dibujarTexto("MISO = Master In Slave Out", 20, sy + bH + 36, COL_LABEL);
        g.dibujarTexto("CLK  = Reloj (genera Master)", 20, sy + bH + 50, COL_LABEL);
        g.dibujarTexto("CS   = Chip Select (bajo = activo)", 280, sy + bH + 22, COL_LABEL);
    }

    /** Diagrama de bus I2C con pull-ups y EEPROM */
    private static void dibujarBusI2C(Graficos2D g) {
        float busY_SDA = 100, busY_SCL = 140;
        float busX1 = 30, busX2 = W - 30;

        // Líneas de bus
        g.dibujarLinea(busX1, busY_SDA, busX2, busY_SDA, 0xFF29B6F6);
        g.dibujarLinea(busX1, busY_SCL, busX2, busY_SCL, 0xFFFFCA28);
        configurarTexto(g, 10f, true);
        g.dibujarTexto("SDA", busX1 - 28, busY_SDA + 4, 0xFF29B6F6);
        g.dibujarTexto("SCL", busX1 - 28, busY_SCL + 4, 0xFFFFCA28);

        // VCC y pull-ups (x=120 y x=220)
        float vccY = 50;
        g.dibujarLinea(busX1 + 90, vccY, busX1 + 90, busY_SDA, 0xFFEF9A9A); // pull-up SDA
        g.dibujarLinea(busX1 + 90, vccY, busX1 + 90, busY_SCL, 0xFFEF9A9A); // pull-up SCL ambos de mismo punto
        // En realidad separados
        float p1x = 120, p2x = 200;
        g.dibujarLinea(p1x, vccY, p1x, busY_SDA, 0xFFEF9A9A);
        g.dibujarLinea(p2x, vccY, p2x, busY_SCL, 0xFFEF9A9A);
        g.dibujarRectangulo(p1x - 6, vccY + 12, 12, 28, COL_AVISO); // resistencia SDA
        g.dibujarRectangulo(p2x - 6, vccY + 12, 12, 28, COL_AVISO); // resistencia SCL
        configurarTexto(g, 9f, false);
        g.dibujarTexto("4.7K", p1x + 8, vccY + 30, COL_AVISO);
        g.dibujarTexto("4.7K", p2x + 8, vccY + 30, COL_AVISO);
        g.dibujarTexto("VCC", p1x - 8, vccY - 4, COL_VCC);
        g.dibujarTexto("VCC", p2x - 8, vccY - 4, COL_VCC);

        // Bloque MASTER
        float mX = 60, mY = busY_SCL + 30, mW = 90, mH = 60;
        g.dibujarRectangulo(mX, mY, mW, mH, COL_CHIP);
        bordes(g, mX, mY, mW, mH, COL_BORDE);
        g.dibujarLinea(mX + mW / 2, mY, mX + mW / 2, busY_SDA, 0xFF29B6F6);
        g.dibujarLinea(mX + mW / 2 + 10, mY, mX + mW / 2 + 10, busY_SCL, 0xFFFFCA28);
        configurarTexto(g, 11f, true);
        g.dibujarTexto("MASTER", mX + 10, mY + 22, COL_TITULO);
        configurarTexto(g, 9f, false);
        g.dibujarTexto("CH341A", mX + 18, mY + 40, COL_LABEL);

        // Bloque EEPROM SOIC8
        float eX = 280, eY = busY_SCL + 20, eW = 130, eH = 130;
        g.dibujarRectangulo(eX, eY, eW, eH, 0xFF1B5E20);
        bordes(g, eX, eY, eW, eH, COL_BORDE);
        configurarTexto(g, 11f, true);
        g.dibujarTexto("EEPROM I2C", eX + 8, eY + 18, COL_TITULO);
        String[] eepromPins = { "A0", "A1", "A2", "GND", "SDA", "SCL", "WP", "VCC" };
        int[] ePy = { (int) (eY + 28), (int) (eY + 42), (int) (eY + 56), (int) (eY + 70), (int) (eY + 84),
                (int) (eY + 98), (int) (eY + 112), (int) (eY + 28) };
        int[] ePx = { (int) (eX + 4), (int) (eX + 4), (int) (eX + 4), (int) (eX + 4), (int) (eX + eW - 46),
                (int) (eX + eW - 46), (int) (eX + eW - 46), (int) (eX + eW - 30) };

        configurarTexto(g, 9f, false);
        for (int i = 0; i < 4; i++) {
            g.dibujarTexto((i + 1) + "-" + eepromPins[i], ePx[i], ePy[i], COL_LABEL);
            g.dibujarTexto((8 - i) + "-" + eepromPins[7 - i], ePx[7 - i], ePy[7 - i], colorPin(eepromPins[7 - i]));
        }
        // Conexiones SDA/SCL al bus
        g.dibujarLinea(eX + eW / 2, eY, eX + eW / 2, busY_SDA, 0xFF29B6F6);
        g.dibujarLinea(eX + eW / 2 + 15, eY, eX + eW / 2 + 15, busY_SCL, 0xFFFFCA28);
    }

    /** Tabla de conexión chip→programador */
    private static void dibujarTablaConexion(Graficos2D g, float x, float y,
            String[] col1, String[] col2) {
        float rowH = 22, col1W = 80, col2W = 100;
        configurarTexto(g, 11f, true);
        g.dibujarTexto("Flash Chip → CH341A", x, y - 6, COL_LABEL);
        g.dibujarRectangulo(x, y, col1W + col2W, rowH, 0xFF1A237E);
        g.dibujarTexto("Pin chip", x + 4, y + 15, COL_TITULO);
        g.dibujarTexto("CH341A", x + col1W + 4, y + 15, COL_TITULO);
        for (int i = 0; i < col1.length; i++) {
            float ry = y + (i + 1) * rowH;
            int bg = (i % 2 == 0) ? COL_PANEL : COL_BG;
            g.dibujarRectangulo(x, ry, col1W + col2W, rowH, bg);
            configurarTexto(g, 10f, false);
            g.dibujarTexto(col1[i], x + 4, ry + 15, COL_PIN_NUM);
            g.dibujarTexto(col2[i], x + col1W + 4, ry + 15, colorPin(col2[i]));
        }
    }

    /** Tabla de conexión chip→programador (general, con headers parametrizables) */
    private static void dibujarTablaConexionGeneral(Graficos2D g, float x, float y,
            String headerLeft, String headerRight,
            String[] col1, String[] col2) {
        float rowH = 22, col1W = 80, col2W = 110;
        configurarTexto(g, 11f, true);
        g.dibujarTexto(headerLeft + " → " + headerRight, x, y - 6, COL_LABEL);
        g.dibujarRectangulo(x, y, col1W + col2W, rowH, 0xFF1A237E);
        g.dibujarTexto("Pin chip", x + 4, y + 15, COL_TITULO);
        g.dibujarTexto(headerRight, x + col1W + 4, y + 15, COL_TITULO);
        for (int i = 0; i < col1.length; i++) {
            float ry = y + (i + 1) * rowH;
            int bg = (i % 2 == 0) ? COL_PANEL : COL_BG;
            g.dibujarRectangulo(x, ry, col1W + col2W, rowH, bg);
            configurarTexto(g, 10f, false);
            g.dibujarTexto(col1[i], x + 4, ry + 15, COL_PIN_NUM);
            g.dibujarTexto(col2[i], x + col1W + 4, ry + 15, colorPin(col2[i]));
        }
    }

    private static void dibujarFlecha(Graficos2D g, float x, float y) {
        g.dibujarLinea(x, y - 16, x + 28, y, 0xFF90A4AE);
        g.dibujarLinea(x, y + 16, x + 28, y, 0xFF90A4AE);
        g.dibujarLinea(x + 28, y, x + 28, y, 0xFF90A4AE);
    }

    // ────────── Utilidades ───────────────────────────────────────────────────

    private static void configurarTexto(Graficos2D g, float size, boolean negrita) {
        Paint p = g.getLapiz();
        p.setTextSize(size);
        p.setTypeface(negrita ? Typeface.DEFAULT_BOLD : Typeface.MONOSPACE);
        p.setAntiAlias(true);
    }

    private static void bordes(Graficos2D g, float x, float y, float w, float h, int color) {
        g.dibujarLinea(x, y, x + w, y, color);
        g.dibujarLinea(x + w, y, x + w, y + h, color);
        g.dibujarLinea(x, y + h, x + w, y + h, color);
        g.dibujarLinea(x, y, x, y + h, color);
    }

    private static void flecha(Graficos2D g, float x1, float y, float x2, float y2, int color) {
        g.dibujarLinea(x1, y, x2, y2, color);
        float dx = x2 - x1;
        float sign = dx > 0 ? 1f : -1f;
        g.dibujarLinea(x2, y2, x2 - sign * 7, y2 - 5, color);
        g.dibujarLinea(x2, y2, x2 - sign * 7, y2 + 5, color);
    }

    private static int colorPin(String pin) {
        if (pin == null)
            return COL_LABEL;
        String p = pin.toUpperCase();
        if (p.contains("VCC") || p.contains("3.3"))
            return COL_VCC;
        if (p.contains("GND"))
            return COL_GND;
        if (p.contains("MOSI") || p.contains("DI"))
            return COL_SPI;
        if (p.contains("MISO") || p.contains("DO"))
            return 0xFF29B6F6;
        if (p.contains("CLK") || p.contains("SCK"))
            return 0xFFFFCA28;
        if (p.contains("CS"))
            return 0xFFCE93D8;
        if (p.contains("WP"))
            return COL_AVISO;
        if (p.contains("HOLD"))
            return 0xFF90A4AE;
        if (p.contains("SDA"))
            return 0xFF29B6F6;
        if (p.contains("SCL"))
            return 0xFFFFCA28;
        return COL_LABEL;
    }

    private static void aplicar(Textura2D tex, ImageView target) {
        // getBipmap() es el getter del Bitmap en la clase Textura2D del proyecto
        Bitmap bmp = Bitmap.createBitmap(tex.getBipmap());
        target.setImageBitmap(bmp);
        target.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }
}
