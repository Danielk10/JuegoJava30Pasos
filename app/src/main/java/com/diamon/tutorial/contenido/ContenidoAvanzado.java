package com.diamon.tutorial.contenido;

import android.content.Context;
import com.diamon.tutorial.database.ContenidoDAO;
import com.diamon.tutorial.models.ContenidoTutorial;

/**
 * Extensión de contenido educativo para capítulos avanzados
 * FASE 2 - PARTE 2: Capítulos 11-15 (Estructuras de Datos y Colecciones)
 * 
 * @author Daniel Diamon
 * @version 1.0
 */
public class ContenidoAvanzado {
    
    private Context context;
    private ContenidoDAO contenidoDAO;
    
    public ContenidoAvanzado(Context context) {
        this.context = context;
        this.contenidoDAO = new ContenidoDAO(context);
    }
    
    /**
     * Inserta los capítulos 11-15: Estructuras de Datos y Colecciones
     */
    public void insertarCapitulos11a15_EstructurasDatos() {
        
        // CAPÍTULO 11: Arrays Unidimensionales
        ContenidoTutorial cap11 = new ContenidoTutorial(
            11,
            "Paso 11: Arrays Unidimensionales - Listas de Elementos",
            "Los arrays son estructuras que nos permiten almacenar múltiples elementos del mismo tipo en una sola variable. En los juegos, los usamos para listas de enemigos, puntuaciones, inventarios, y muchísimas otras cosas. Son fundamentales para cualquier juego.",
            "public class ArraysEnJuegos {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"=== ARRAYS UNIDIMENSIONALES EN JUEGOS ===");\n" +
            "        \n" +
            "        // DECLARACIÓN E INICIALIZACIÓN DE ARRAYS\n" +
            "        \n" +
            "        // Array de puntuaciones (inicialización con valores)\n" +
            "        int[] puntuacionesAltas = {1500, 1200, 1000, 800, 500};\n" +
            "        \n" +
            "        // Array de nombres de jugadores\n" +
            "        String[] nombresJugadores = {\"ProGamer\", \"NoobMaster\", \"SpeedRunner\", \"Casual\", \"Beginner\"};\n" +
            "        \n" +
            "        // Array de inventario (inicialización con tamaño)\n" +
            "        String[] inventario = new String[10]; // 10 espacios vacíos\n" +
            "        inventario[0] = \"Espada\";\n" +
            "        inventario[1] = \"Poción de Vida\";\n" +
            "        inventario[2] = \"Escudo\";\n" +
            "        // inventario[3] a inventario[9] quedan null\n" +
            "        \n" +
            "        // Array de vida de enemigos\n" +
            "        int[] vidaEnemigos = new int[5]; // Todos empiezan en 0\n" +
            "        for (int i = 0; i < vidaEnemigos.length; i++) {\n" +
            "            vidaEnemigos[i] = 100 + (i * 20); // Enemigos más fuertes\n" +
            "        }\n" +
            "        \n" +
            "        // TRABAJAR CON ARRAYS\n" +
            "        System.out.println(\"\\n=== TABLA DE PUNTUACIONES ALTAS ===");\n" +
            "        for (int i = 0; i < puntuacionesAltas.length; i++) {\n" +
            "            System.out.println((i + 1) + \". \" + nombresJugadores[i] + \n" +
            "                             \": \" + puntuacionesAltas[i] + \" puntos\");\n" +
            "        }\n" +
            "        \n" +
            "        System.out.println(\"\\n=== INVENTARIO DEL JUGADOR ===");\n" +
            "        System.out.println(\"Objetos en inventario:\");\n" +
            "        for (int i = 0; i < inventario.length; i++) {\n" +
            "            if (inventario[i] != null) {\n" +
            "                System.out.println(\"  Slot \" + i + \": \" + inventario[i]);\n" +
            "            } else {\n" +
            "                System.out.println(\"  Slot \" + i + \": [Vacío]\");\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        System.out.println(\"\\n=== ESTADO DE ENEMIGOS ===");\n" +
            "        for (int i = 0; i < vidaEnemigos.length; i++) {\n" +
            "            String estado = vidaEnemigos[i] > 0 ? \"Vivo\" : \"Muerto\";\n" +
            "            System.out.println(\"Enemigo \" + (i + 1) + \": \" + vidaEnemigos[i] + \" HP (\" + estado + \")\");\n" +
            "        }\n" +
            "        \n" +
            "        // ENHANCED FOR LOOP (for-each)\n" +
            "        System.out.println(\"\\n=== FOR-EACH LOOP ===");\n" +
            "        System.out.println(\"Recorrido simplificado de puntuaciones:\");\n" +
            "        for (int puntuacion : puntuacionesAltas) {\n" +
            "            System.out.println(\"  Puntuación: \" + puntuacion);\n" +
            "        }\n" +
            "        \n" +
            "        // OPERACIONES COMUNES CON ARRAYS\n" +
            "        System.out.println(\"\\n=== OPERACIONES CON ARRAYS ===");\n" +
            "        \n" +
            "        // Encontrar puntuación máxima\n" +
            "        int maxPuntuacion = puntuacionesAltas[0];\n" +
            "        String mejorJugador = nombresJugadores[0];\n" +
            "        \n" +
            "        for (int i = 1; i < puntuacionesAltas.length; i++) {\n" +
            "            if (puntuacionesAltas[i] > maxPuntuacion) {\n" +
            "                maxPuntuacion = puntuacionesAltas[i];\n" +
            "                mejorJugador = nombresJugadores[i];\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        System.out.println(\"🏆 Mejor jugador: \" + mejorJugador + \" con \" + maxPuntuacion + \" puntos\");\n" +
            "        \n" +
            "        // Calcular promedio\n" +
            "        int sumaPuntuaciones = 0;\n" +
            "        for (int puntuacion : puntuacionesAltas) {\n" +
            "            sumaPuntuaciones += puntuacion;\n" +
            "        }\n" +
            "        double promedio = (double) sumaPuntuaciones / puntuacionesAltas.length;\n" +
            "        System.out.println(\"Puntuación promedio: \" + String.format(\"%.2f\", promedio));\n" +
            "        \n" +
            "        // Buscar elemento\n" +
            "        String jugadorBuscado = \"SpeedRunner\";\n" +
            "        int posicionEncontrada = -1;\n" +
            "        \n" +
            "        for (int i = 0; i < nombresJugadores.length; i++) {\n" +
            "            if (nombresJugadores[i].equals(jugadorBuscado)) {\n" +
            "                posicionEncontrada = i;\n" +
            "                break;\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        if (posicionEncontrada != -1) {\n" +
            "            System.out.println(\"🔍 Encontrado: \" + jugadorBuscado + \" en posición \" + \n" +
            "                             posicionEncontrada + \" con \" + puntuacionesAltas[posicionEncontrada] + \" puntos\");\n" +
            "        } else {\n" +
            "            System.out.println(\"Jugador \" + jugadorBuscado + \" no encontrado\");\n" +
            "        }\n" +
            "        \n" +
            "        // SIMULACIÓN DE COMBATE CON ARRAYS\n" +
            "        System.out.println(\"\\n=== SIMULACIÓN DE COMBATE ===");\n" +
            "        \n" +
            "        // Simular daño a todos los enemigos\n" +
            "        int danioAtaque = 25;\n" +
            "        System.out.println(\"Ataque de área - Daño: \" + danioAtaque);\n" +
            "        \n" +
            "        for (int i = 0; i < vidaEnemigos.length; i++) {\n" +
            "            vidaEnemigos[i] = Math.max(0, vidaEnemigos[i] - danioAtaque);\n" +
            "            String estado = vidaEnemigos[i] > 0 ? \"Herido\" : \"Derrotado\";\n" +
            "            System.out.println(\"  Enemigo \" + (i + 1) + \": \" + vidaEnemigos[i] + \" HP (\" + estado + \")\");\n" +
            "        }\n" +
            "        \n" +
            "        // Contar enemigos vivos\n" +
            "        int enemigosVivos = 0;\n" +
            "        for (int vida : vidaEnemigos) {\n" +
            "            if (vida > 0) {\n" +
            "                enemigosVivos++;\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        System.out.println(\"\\n🎆 Enemigos restantes: \" + enemigosVivos + \"/\" + vidaEnemigos.length);\n" +
            "        \n" +
            "        if (enemigosVivos == 0) {\n" +
            "            System.out.println(\"🏆 ¡VICTORIA! Todos los enemigos derrotados\");\n" +
            "        } else {\n" +
            "            System.out.println(\"⚔️ ¡Continúa la batalla!\");\n" +
            "        }\n" +
            "        \n" +
            "        System.out.println(\"\\n¡Arrays dominados!\");\n" +
            "    }\n" +
            "}",
            "**Conceptos Clave de Arrays:**\n\n" +
            "• **Declaración**: `int[] numeros` o `int numeros[]`\n" +
            "• **Inicialización con valores**: `{1, 2, 3, 4, 5}`\n" +
            "• **Inicialización con tamaño**: `new int[10]`\n" +
            "• **Índices**: Empiezan en 0, van hasta length-1\n" +
            "• **Tamaño fijo**: No se puede cambiar después de crear\n\n" +
            "**Operaciones Fundamentales:**\n" +
            "- **Acceso**: `array[indice]`\n" +
            "- **Modificación**: `array[indice] = valor`\n" +
            "- **Tamaño**: `array.length`\n" +
            "- **Recorrido**: for tradicional o for-each\n\n" +
            "**For-Each Loop:**\n" +
            "```java\n" +
            "for (TipoElemento elemento : array) {\n" +
            "    // trabajar con elemento\n" +
            "}\n" +
            "```\n\n" +
            "**Aplicaciones en Juegos:**\n" +
            "- Inventario de objetos\n" +
            "- Lista de enemigos en pantalla\n" +
            "- Tabla de puntuaciones altas\n" +
            "- Array de teclas presionadas\n" +
            "- Coordenadas de waypoints\n\n" +
            "**Cuidados Importantes:**\n" +
            "- ArrayIndexOutOfBoundsException si usas índice inválido\n" +
            "- NullPointerException si no inicializas elementos\n" +
            "- Tamaño fijo - no crece dinámicamente",
            1
        );
        contenidoDAO.insertarContenido(cap11);
        
        // CAPÍTULO 12: Arrays Multidimensionales
        ContenidoTutorial cap12 = new ContenidoTutorial(
            12,
            "Paso 12: Arrays Multidimensionales - Mapas y Grillas",
            "Los arrays multidimensionales son perfectos para representar mapas de juegos, grillas, tableros, o cualquier estructura 2D. Son como tablas donde cada celda puede contener información del juego.",
            "public class MapasYGrillas {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"=== ARRAYS MULTIDIMENSIONALES PARA JUEGOS ===");\n" +
            "        \n" +
            "        // MAPA DEL JUEGO 2D\n" +
            "        // 0 = espacio libre, 1 = pared, 2 = enemigo, 3 = tesoro, 4 = jugador\n" +
            "        int[][] mapa = {\n" +
            "            {1, 1, 1, 1, 1, 1, 1, 1},\n" +
            "            {1, 4, 0, 0, 2, 0, 0, 1},\n" +
            "            {1, 0, 1, 0, 1, 1, 0, 1},\n" +
            "            {1, 0, 0, 0, 0, 0, 3, 1},\n" +
            "            {1, 2, 1, 0, 1, 0, 0, 1},\n" +
            "            {1, 0, 0, 0, 2, 0, 0, 1},\n" +
            "            {1, 1, 1, 1, 1, 1, 1, 1}\n" +
            "        };\n" +
            "        \n" +
            "        // TABLERO DE AJEDREZ/DAMAS\n" +
            "        boolean[][] tablero = new boolean[8][8];\n" +
            "        // true = casilla blanca, false = casilla negra\n" +
            "        for (int fila = 0; fila < 8; fila++) {\n" +
            "            for (int columna = 0; columna < 8; columna++) {\n" +
            "                tablero[fila][columna] = (fila + columna) % 2 == 0;\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        // MATRIZ DE PUNTUACIONES 3D (nivel x fila x columna)\n" +
            "        int[][][] puntuacionesPorNivel = new int[3][5][5]; // 3 niveles, grilla 5x5\n" +
            "        \n" +
            "        // Llenar con puntuaciones aleatorias\n" +
            "        for (int nivel = 0; nivel < 3; nivel++) {\n" +
            "            for (int fila = 0; fila < 5; fila++) {\n" +
            "                for (int columna = 0; columna < 5; columna++) {\n" +
            "                    puntuacionesPorNivel[nivel][fila][columna] = \n" +
            "                        (int)(Math.random() * 100) + (nivel * 50);\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        // MOSTRAR EL MAPA\n" +
            "        System.out.println(\"\\n=== MAPA DEL JUEGO ===");\n" +
            "        String[] simbolos = {\".\", \"#\", \"👾\", \"💰\", \"🚀\"};\n" +
            "        String[] nombres = {\"Libre\", \"Pared\", \"Enemigo\", \"Tesoro\", \"Jugador\"};\n" +
            "        \n" +
            "        for (int fila = 0; fila < mapa.length; fila++) {\n" +
            "            for (int columna = 0; columna < mapa[fila].length; columna++) {\n" +
            "                System.out.print(simbolos[mapa[fila][columna]] + \" \");\n" +
            "            }\n" +
            "            System.out.println(); // Nueva línea\n" +
            "        }\n" +
            "        \n" +
            "        // LEYENDA DEL MAPA\n" +
            "        System.out.println(\"\\nLeyenda:\");\n" +
            "        for (int i = 0; i < simbolos.length; i++) {\n" +
            "            System.out.println(\"  \" + simbolos[i] + \" = \" + nombres[i]);\n" +
            "        }\n" +
            "        \n" +
            "        // ANÁLISIS DEL MAPA\n" +
            "        System.out.println(\"\\n=== ANÁLISIS DEL MAPA ===");\n" +
            "        int[] contadores = new int[5]; // Contador para cada tipo\n" +
            "        int jugadorX = -1, jugadorY = -1;\n" +
            "        \n" +
            "        for (int fila = 0; fila < mapa.length; fila++) {\n" +
            "            for (int columna = 0; columna < mapa[fila].length; columna++) {\n" +
            "                int tipo = mapa[fila][columna];\n" +
            "                contadores[tipo]++;\n" +
            "                \n" +
            "                if (tipo == 4) { // Encontrar posición del jugador\n" +
            "                    jugadorX = columna;\n" +
            "                    jugadorY = fila;\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        System.out.println(\"Elementos en el mapa:\");\n" +
            "        for (int i = 0; i < contadores.length; i++) {\n" +
            "            System.out.println(\"  \" + nombres[i] + \": \" + contadores[i]);\n" +
            "        }\n" +
            "        \n" +
            "        System.out.println(\"Posición del jugador: (\" + jugadorX + \", \" + jugadorY + \")\");\n" +
            "        \n" +
            "        // MOSTRAR TABLERO DE AJEDREZ\n" +
            "        System.out.println(\"\\n=== TABLERO DE AJEDREZ ===");\n" +
            "        for (int fila = 0; fila < 8; fila++) {\n" +
            "            for (int columna = 0; columna < 8; columna++) {\n" +
            "                System.out.print(tablero[fila][columna] ? \"⬜ \" : \"⬛ \"); // Blanco/Negro\n" +
            "            }\n" +
            "            System.out.println();\n" +
            "        }\n" +
            "        \n" +
            "        // PUNTUACIONES POR NIVEL\n" +
            "        System.out.println(\"\\n=== PUNTUACIONES POR NIVEL ===");\n" +
            "        for (int nivel = 0; nivel < puntuacionesPorNivel.length; nivel++) {\n" +
            "            System.out.println(\"\\nNivel \" + (nivel + 1) + \":\");\n" +
            "            for (int fila = 0; fila < puntuacionesPorNivel[nivel].length; fila++) {\n" +
            "                for (int columna = 0; columna < puntuacionesPorNivel[nivel][fila].length; columna++) {\n" +
            "                    System.out.printf(\"%4d \", puntuacionesPorNivel[nivel][fila][columna]);\n" +
            "                }\n" +
            "                System.out.println();\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        System.out.println(\"\\n¡Arrays multidimensionales dominados!\");\n" +
            "    }\n" +
            "}",
            "**Arrays Bidimensionales:**\n\n" +
            "**Declaración:**\n" +
            "```java\n" +
            "int[][] matriz = new int[filas][columnas];\n" +
            "int[][] valores = {{1,2,3}, {4,5,6}};\n" +
            "```\n\n" +
            "**Acceso:**\n" +
            "```java\n" +
            "matriz[fila][columna] = valor;\n" +
            "int valor = matriz[fila][columna];\n" +
            "```\n\n" +
            "**Recorrido con Bucles Anidados:**\n" +
            "```java\n" +
            "for (int i = 0; i < matriz.length; i++) {        // Filas\n" +
            "    for (int j = 0; j < matriz[i].length; j++) { // Columnas\n" +
            "        // trabajar con matriz[i][j]\n" +
            "    }\n" +
            "}\n" +
            "```\n\n" +
            "**Arrays Irregulares (Jagged Arrays):**\n" +
            "```java\n" +
            "int[][] irregular = new int[3][];\n" +
            "irregular[0] = new int[5]; // Primera fila: 5 elementos\n" +
            "irregular[1] = new int[3]; // Segunda fila: 3 elementos\n" +
            "irregular[2] = new int[7]; // Tercera fila: 7 elementos\n" +
            "```\n\n" +
            "**Aplicaciones en Juegos:**\n" +
            "- **Mapas de niveles**: Terreno, obstáculos, objetos\n" +
            "- **Tableros**: Ajedrez, damas, tic-tac-toe\n" +
            "- **Grillas de colisión**: Optimización de detección\n" +
            "- **Matrices de transformación**: Gráficos 3D\n" +
            "- **Mapas de bits**: Texturas, sprites\n\n" +
            "**Arrays 3D y superiores:**\n" +
            "- **3D**: `int[][][]` - Ideal para voxels, niveles con capas\n" +
            "- **4D+**: Raramente usado, pero posible\n\n" +
            "**Optimización de Memoria:**\n" +
            "- Arrays son eficientes en memoria\n" +
            "- Acceso O(1) - muy rápido\n" +
            "- Cache-friendly para recorridos secuenciales",
            1
        );
        contenidoDAO.insertarContenido(cap12);
        
        // CAPÍTULO 13: ArrayList y Colecciones Dinámicas
        ContenidoTutorial cap13 = new ContenidoTutorial(
            13,
            "Paso 13: ArrayList - Listas Dinámicas Inteligentes",
            "ArrayList es la evolución natural de los arrays. Puede crecer y decrecer dinámicamente, proporcionando flexibilidad que los arrays no tienen. Perfect para listas de enemigos que aparecen y desaparecen, inventarios que cambian, listas de proyectiles, etc.",
            "import java.util.ArrayList;\n" +
            "import java.util.Collections;\n" +
            "import java.util.Iterator;\n" +
            "\n" +
            "// Clase para representar un objeto del juego\n" +
            "class Objeto {\n" +
            "    private String nombre;\n" +
            "    private int valor;\n" +
            "    private String tipo;\n" +
            "    \n" +
            "    public Objeto(String nombre, int valor, String tipo) {\n" +
            "        this.nombre = nombre;\n" +
            "        this.valor = valor;\n" +
            "        this.tipo = tipo;\n" +
            "    }\n" +
            "    \n" +
            "    // Getters\n" +
            "    public String getNombre() { return nombre; }\n" +
            "    public int getValor() { return valor; }\n" +
            "    public String getTipo() { return tipo; }\n" +
            "    \n" +
            "    @Override\n" +
            "    public String toString() {\n" +
            "        return nombre + \" (\" + tipo + \", \" + valor + \" puntos)\";\n" +
            "    }\n" +
            "    \n" +
            "    @Override\n" +
            "    public boolean equals(Object obj) {\n" +
            "        if (obj instanceof Objeto) {\n" +
            "            Objeto otro = (Objeto) obj;\n" +
            "            return nombre.equals(otro.nombre);\n" +
            "        }\n" +
            "        return false;\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "public class InventarioDinamico {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"=== ARRAYLIST - INVENTARIO DINÁMICO ===");\n" +
            "        \n" +
            "        // CREAR ARRAYLIST\n" +
            "        ArrayList<Objeto> inventario = new ArrayList<>();\n" +
            "        ArrayList<String> logAcciones = new ArrayList<>();\n" +
            "        \n" +
            "        // AGREGAR OBJETOS AL INVENTARIO\n" +
            "        System.out.println(\"\\n=== RECOLECTANDO OBJETOS ===");\n" +
            "        inventario.add(new Objeto(\"Espada de Hierro\", 50, \"Arma\"));\n" +
            "        inventario.add(new Objeto(\"Poción de Vida\", 25, \"Consumible\"));\n" +
            "        inventario.add(new Objeto(\"Escudo de Madera\", 30, \"Defensa\"));\n" +
            "        inventario.add(new Objeto(\"Anillo Mágico\", 100, \"Accesorio\"));\n" +
            "        inventario.add(new Objeto(\"Manzana\", 10, \"Consumible\"));\n" +
            "        \n" +
            "        logAcciones.add(\"Jugador entró al dungeon\");\n" +
            "        logAcciones.add(\"Encontró cofre del tesoro\");\n" +
            "        logAcciones.add(\"Derrotó a goblin\");\n" +
            "        \n" +
            "        System.out.println(\"Objetos recolectados: \" + inventario.size());\n" +
            "        \n" +
            "        // MOSTRAR INVENTARIO COMPLETO\n" +
            "        System.out.println(\"\\n=== INVENTARIO COMPLETO ===");\n" +
            "        for (int i = 0; i < inventario.size(); i++) {\n" +
            "            System.out.println((i + 1) + \". \" + inventario.get(i));\n" +
            "        }\n" +
            "        \n" +
            "        // FOR-EACH CON ARRAYLIST\n" +
            "        System.out.println(\"\\n=== RECORRIDO CON FOR-EACH ===");\n" +
            "        for (Objeto objeto : inventario) {\n" +
            "            System.out.println(\"• \" + objeto);\n" +
            "        }\n" +
            "        \n" +
            "        // BUSCAR OBJETOS ESPECÍFICOS\n" +
            "        System.out.println(\"\\n=== BUSCAR OBJETOS ===");\n" +
            "        String objetoBuscado = \"Escudo de Madera\";\n" +
            "        boolean encontrado = false;\n" +
            "        \n" +
            "        for (Objeto objeto : inventario) {\n" +
            "            if (objeto.getNombre().equals(objetoBuscado)) {\n" +
            "                System.out.println(\"🔍 Encontrado: \" + objeto);\n" +
            "                encontrado = true;\n" +
            "                break;\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        if (!encontrado) {\n" +
            "            System.out.println(\"Objeto '\" + objetoBuscado + \"' no encontrado\");\n" +
            "        }\n" +
            "        \n" +
            "        // FILTRAR OBJETOS POR TIPO\n" +
            "        System.out.println(\"\\n=== OBJETOS CONSUMIBLES ===");\n" +
            "        ArrayList<Objeto> consumibles = new ArrayList<>();\n" +
            "        \n" +
            "        for (Objeto objeto : inventario) {\n" +
            "            if (\"Consumible\".equals(objeto.getTipo())) {\n" +
            "                consumibles.add(objeto);\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        System.out.println(\"Consumibles disponibles: \" + consumibles.size());\n" +
            "        for (Objeto consumible : consumibles) {\n" +
            "            System.out.println(\"🍎 \" + consumible);\n" +
            "        }\n" +
            "        \n" +
            "        // USAR (ELIMINAR) OBJETOS CONSUMIBLES\n" +
            "        System.out.println(\"\\n=== USANDO CONSUMIBLES ===");\n" +
            "        Iterator<Objeto> iterator = inventario.iterator();\n" +
            "        while (iterator.hasNext()) {\n" +
            "            Objeto objeto = iterator.next();\n" +
            "            if (\"Consumible\".equals(objeto.getTipo())) {\n" +
            "                System.out.println(\"Usando: \" + objeto.getNombre() + \" (+\" + objeto.getValor() + \" vida)\");\n" +
            "                iterator.remove(); // Eliminar de forma segura durante iteración\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        System.out.println(\"\\nInventario después de usar consumibles:\");\n" +
            "        for (Objeto objeto : inventario) {\n" +
            "            System.out.println(\"• \" + objeto);\n" +
            "        }\n" +
            "        \n" +
            "        // ORDENAR INVENTARIO POR VALOR\n" +
            "        System.out.println(\"\\n=== INVENTARIO ORDENADO POR VALOR ===");\n" +
            "        Collections.sort(inventario, (o1, o2) -> Integer.compare(o2.getValor(), o1.getValor()));\n" +
            "        \n" +
            "        for (int i = 0; i < inventario.size(); i++) {\n" +
            "            System.out.println((i + 1) + \". \" + inventario.get(i));\n" +
            "        }\n" +
            "        \n" +
            "        // OPERACIONES AVANZADAS\n" +
            "        System.out.println(\"\\n=== OPERACIONES AVANZADAS ===");\n" +
            "        \n" +
            "        // Valor total del inventario\n" +
            "        int valorTotal = 0;\n" +
            "        for (Objeto objeto : inventario) {\n" +
            "            valorTotal += objeto.getValor();\n" +
            "        }\n" +
            "        System.out.println(\"💰 Valor total del inventario: \" + valorTotal + \" puntos\");\n" +
            "        \n" +
            "        // Verificar si contiene objeto específico\n" +
            "        Objeto espada = new Objeto(\"Espada de Hierro\", 50, \"Arma\");\n" +
            "        boolean tieneEspada = inventario.contains(espada);\n" +
            "        System.out.println(\"\n⚔️ ¿Tiene espada?: \" + (tieneEspada ? \"Sí\" : \"No\"));\n" +
            "        \n" +
            "        // Mostrar log de acciones\n" +
            "        System.out.println(\"\\n=== LOG DE ACCIONES ===");\n" +
            "        for (String accion : logAcciones) {\n" +
            "            System.out.println(\"📄 \" + accion);\n" +
            "        }\n" +
            "        \n" +
            "        System.out.println(\"\\n¡ArrayList dominado! Inventario dinámico funcional.\");\n" +
            "    }\n" +
            "}",
            "**ArrayList vs Array:**\n\n" +
            "| **Array** | **ArrayList** |\n" +
            "|-----------|---------------|\n" +
            "| Tamaño fijo | Tamaño dinámico |\n" +
            "| Más rápido | Ligeramente más lento |\n" +
            "| Sintaxis simple | Métodos ricos |\n" +
            "| Memoria optimizada | Overhead de objetos |\n\n" +
            "**Métodos Principales de ArrayList:**\n" +
            "• **add(elemento)**: Agrega al final\n" +
            "• **add(indice, elemento)**: Agrega en posición\n" +
            "• **get(indice)**: Obtiene elemento\n" +
            "• **set(indice, elemento)**: Reemplaza elemento\n" +
            "• **remove(indice)**: Elimina por índice\n" +
            "• **remove(objeto)**: Elimina por objeto\n" +
            "• **size()**: Tamaño actual\n" +
            "• **isEmpty()**: Está vacío\n" +
            "• **contains(objeto)**: Contiene elemento\n" +
            "• **clear()**: Vacía completamente\n\n" +
            "**Generics (Tipos Genéricos):**\n" +
            "```java\n" +
            "ArrayList<String> nombres = new ArrayList<>();\n" +
            "ArrayList<Integer> numeros = new ArrayList<>();\n" +
            "ArrayList<MiClase> objetos = new ArrayList<>();\n" +
            "```\n" +
            "Los generics proporcionan type safety en tiempo de compilación.\n\n" +
            "**Iteración Segura:**\n" +
            "```java\n" +
            "Iterator<Objeto> iter = lista.iterator();\n" +
            "while (iter.hasNext()) {\n" +
            "    Objeto obj = iter.next();\n" +
            "    if (condicion) {\n" +
            "        iter.remove(); // Seguro durante iteración\n" +
            "    }\n" +
            "}\n" +
            "```\n\n" +
            "**Aplicaciones en Juegos:**\n" +
            "- Listas de enemigos activos\n" +
            "- Inventario de objetos\n" +
            "- Lista de proyectiles\n" +
            "- Queue de acciones del jugador\n" +
            "- Historial de movimientos\n" +
            "- Lista de power-ups temporales",
            1
        );
        contenidoDAO.insertarContenido(cap13);
        
        // CAPÍTULO 14: HashMap y Estructuras Clave-Valor
        ContenidoTutorial cap14 = new ContenidoTutorial(
            14,
            "Paso 14: HashMap - Mapas de Clave-Valor",
            "HashMap es una estructura de datos que asocia claves únicas con valores. Es como un diccionario donde puedes buscar rápidamente. En juegos, es perfecto para configuraciones, estadísticas de jugadores, mapeo de teclas, caches de recursos, etc.",
            "import java.util.HashMap;\n" +
            "import java.util.Map;\n" +
            "import java.util.Set;\n" +
            "\n" +
            "// Clase para estadísticas de jugador\n" +
            "class EstadisticasJugador {\n" +
            "    private String nombre;\n" +
            "    private int nivel;\n" +
            "    private int experiencia;\n" +
            "    private double tiempoJugado;\n" +
            "    \n" +
            "    public EstadisticasJugador(String nombre, int nivel, int experiencia, double tiempoJugado) {\n" +
            "        this.nombre = nombre;\n" +
            "        this.nivel = nivel;\n" +
            "        this.experiencia = experiencia;\n" +
            "        this.tiempoJugado = tiempoJugado;\n" +
            "    }\n" +
            "    \n" +
            "    // Getters y toString\n" +
            "    public String getNombre() { return nombre; }\n" +
            "    public int getNivel() { return nivel; }\n" +
            "    public int getExperiencia() { return experiencia; }\n" +
            "    public double getTiempoJugado() { return tiempoJugado; }\n" +
            "    \n" +
            "    @Override\n" +
            "    public String toString() {\n" +
            "        return String.format(\"%s (Nv.%d, %d XP, %.1fh)\", \n" +
            "                           nombre, nivel, experiencia, tiempoJugado);\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "public class SistemaConfiguracion {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"=== HASHMAP - SISTEMAS DE CONFIGURACIÓN ===");\n" +
            "        \n" +
            "        // CONFIGURACIONES DEL JUEGO\n" +
            "        HashMap<String, Object> configuracion = new HashMap<>();\n" +
            "        \n" +
            "        // Cargar configuraciones por defecto\n" +
            "        configuracion.put(\"volumenMusica\", 0.8);\n" +
            "        configuracion.put(\"volumenEfectos\", 0.9);\n" +
            "        configuracion.put(\"dificultad\", \"Medio\");\n" +
            "        configuracion.put(\"pantallaCompleta\", true);\n" +
            "        configuracion.put(\"idioma\", \"Español\");\n" +
            "        configuracion.put(\"fps\", 60);\n" +
            "        configuracion.put(\"sensibilidadMouse\", 1.5);\n" +
            "        \n" +
            "        // ESTADÍSTICAS DE JUGADORES\n" +
            "        HashMap<String, EstadisticasJugador> jugadores = new HashMap<>();\n" +
            "        \n" +
            "        jugadores.put(\"player1\", new EstadisticasJugador(\"ProGamer\", 25, 15420, 120.5));\n" +
            "        jugadores.put(\"player2\", new EstadisticasJugador(\"CasualPlayer\", 12, 3200, 45.2));\n" +
            "        jugadores.put(\"player3\", new EstadisticasJugador(\"SpeedRunner\", 50, 45000, 200.8));\n" +
            "        \n" +
            "        // MAPEO DE TECLAS\n" +
            "        HashMap<String, String> controles = new HashMap<>();\n" +
            "        controles.put(\"W\", \"Mover Arriba\");\n" +
            "        controles.put(\"S\", \"Mover Abajo\");\n" +
            "        controles.put(\"A\", \"Mover Izquierda\");\n" +
            "        controles.put(\"D\", \"Mover Derecha\");\n" +
            "        controles.put(\"SPACE\", \"Saltar\");\n" +
            "        controles.put(\"CLICK_IZQ\", \"Atacar\");\n" +
            "        controles.put(\"CLICK_DER\", \"Defender\");\n" +
            "        controles.put(\"ESC\", \"Pausa\");\n" +
            "        \n" +
            "        // MOSTRAR CONFIGURACIÓN\n" +
            "        System.out.println(\"\\n=== CONFIGURACIÓN ACTUAL ===");\n" +
            "        for (Map.Entry<String, Object> entrada : configuracion.entrySet()) {\n" +
            "            System.out.println(entrada.getKey() + \" = \" + entrada.getValue());\n" +
            "        }\n" +
            "        \n" +
            "        // MOSTRAR ESTADÍSTICAS DE JUGADORES\n" +
            "        System.out.println(\"\\n=== ESTADÍSTICAS DE JUGADORES ===");\n" +
            "        for (String playerId : jugadores.keySet()) {\n" +
            "            EstadisticasJugador stats = jugadores.get(playerId);\n" +
            "            System.out.println(playerId + \": \" + stats);\n" +
            "        }\n" +
            "        \n" +
            "        // MOSTRAR CONTROLES\n" +
            "        System.out.println(\"\\n=== CONTROLES DEL JUEGO ===");\n" +
            "        for (Map.Entry<String, String> control : controles.entrySet()) {\n" +
            "            System.out.println(\"🎮 \" + control.getKey() + \" → \" + control.getValue());\n" +
            "        }\n" +
            "        \n" +
            "        // BUSCAR Y MODIFICAR CONFIGURACIONES\n" +
            "        System.out.println(\"\\n=== MODIFICAR CONFIGURACIONES ===");\n" +
            "        \n" +
            "        // Cambiar dificultad\n" +
            "        String dificultadAnterior = (String) configuracion.get(\"dificultad\");\n" +
            "        configuracion.put(\"dificultad\", \"Difícil\");\n" +
            "        System.out.println(\"Dificultad cambiada: \" + dificultadAnterior + \" → Difícil\");\n" +
            "        \n" +
            "        // Verificar si existe una configuración\n" +
            "        if (configuracion.containsKey(\"modoOscuro\")) {\n" +
            "            System.out.println(\"Modo oscuro ya configurado\");\n" +
            "        } else {\n" +
            "            configuracion.put(\"modoOscuro\", false);\n" +
            "            System.out.println(\"Modo oscuro agregado (deshabilitado)\");\n" +
            "        }\n" +
            "        \n" +
            "        // SIMULACIÓN DE CACHÉ DE RECURSOS\n" +
            "        System.out.println(\"\\n=== CACHÉ DE RECURSOS ===");\n" +
            "        HashMap<String, String> cacheRecursos = new HashMap<>();\n" +
            "        \n" +
            "        // Simular carga de recursos\n" +
            "        String[] recursos = {\"player.png\", \"enemy.png\", \"background.jpg\", \"music.mp3\", \"sound.wav\"};\n" +
            "        \n" +
            "        for (String recurso : recursos) {\n" +
            "            // Simular carga (normalmente sería desde archivo)\n" +
            "            String contenido = \"[Contenido de \" + recurso + \" cargado]\";\n" +
            "            cacheRecursos.put(recurso, contenido);\n" +
            "            System.out.println(\"Recurso cargado: \" + recurso);\n" +
            "        }\n" +
            "        \n" +
            "        // Acceder a recursos desde caché\n" +
            "        String spriteJugador = cacheRecursos.get(\"player.png\");\n" +
            "        if (spriteJugador != null) {\n" +
            "            System.out.println(\"\n🎨 Sprite del jugador listo: \" + spriteJugador);\n" +
            "        }\n" +
            "        \n" +
            "        // Estadísticas del caché\n" +
            "        System.out.println(\"\\n📊 Recursos en caché: \" + cacheRecursos.size());\n" +
            "        System.out.println(\"Memoria optimizada con HashMap!\");\n" +
            "        \n" +
            "        System.out.println(\"\\n¡HashMap dominado! Sistemas de configuración y caché funcionales.\");\n" +
            "    }\n" +
            "}",
            "**HashMap - Conceptos Clave:**\n\n" +
            "• **Clave-Valor**: Cada elemento tiene una clave única asociada a un valor\n" +
            "• **Búsqueda rápida**: Acceso O(1) promedio - muy eficiente\n" +
            "• **Claves únicas**: No puede haber claves duplicadas\n" +
            "• **Valores duplicados**: Sí se permiten\n\n" +
            "**Métodos Principales:**\n" +
            "• **put(clave, valor)**: Agrega o actualiza\n" +
            "• **get(clave)**: Obtiene valor por clave\n" +
            "• **remove(clave)**: Elimina entrada\n" +
            "• **containsKey(clave)**: Verifica si existe la clave\n" +
            "• **containsValue(valor)**: Verifica si existe el valor\n" +
            "• **keySet()**: Conjunto de todas las claves\n" +
            "• **values()**: Colección de todos los valores\n" +
            "• **entrySet()**: Conjunto de pares clave-valor\n\n" +
            "**Iteración sobre HashMap:**\n" +
            "```java\n" +
            "// Solo claves\n" +
            "for (String clave : mapa.keySet()) { ... }\n" +
            "\n" +
            "// Solo valores\n" +
            "for (Valor valor : mapa.values()) { ... }\n" +
            "\n" +
            "// Clave y valor\n" +
            "for (Map.Entry<String, Valor> entrada : mapa.entrySet()) {\n" +
            "    String clave = entrada.getKey();\n" +
            "    Valor valor = entrada.getValue();\n" +
            "}\n" +
            "```\n\n" +
            "**Casos de Uso en Juegos:**\n" +
            "- **Configuraciones**: dificultad, volúmenes, controles\n" +
            "- **Estadísticas**: datos de jugadores, logros\n" +
            "- **Cachés**: recursos cargados, texturas\n" +
            "- **Mapeo de teclas**: asociar teclas con acciones\n" +
            "- **Inventarios complejos**: ID → Objeto\n" +
            "- **Localización**: idioma → textos\n\n" +
            "**Performance:**\n" +
            "- Búsqueda: O(1) promedio\n" +
            "- Inserción: O(1) promedio\n" +
            "- Eliminación: O(1) promedio\n" +
            "- Ideal para accesos frecuentes por clave",
            1
        );
        contenidoDAO.insertarContenido(cap14);
        
        // CAPÍTULO 15: Manejo de Excepciones
        ContenidoTutorial cap15 = new ContenidoTutorial(
            15,
            "Paso 15: Manejo de Excepciones - Programación Defensiva",
            "Las excepciones son errores que pueden ocurrir durante la ejecución del programa. En los juegos, pueden surgir por archivos faltantes, conexiones de red, memoria insuficiente, etc. Aprender a manejarlas correctamente hace que tu juego sea robusto y profesional.",
            "import java.io.FileNotFoundException;\n" +
            "import java.io.IOException;\n" +
            "import java.util.ArrayList;\n" +
            "import java.util.Scanner;\n" +
            "\n" +
            "// Excepción personalizada para el juego\n" +
            "class JuegoException extends Exception {\n" +
            "    private String codigoError;\n" +
            "    \n" +
            "    public JuegoException(String mensaje, String codigoError) {\n" +
            "        super(mensaje);\n" +
            "        this.codigoError = codigoError;\n" +
            "    }\n" +
            "    \n" +
            "    public String getCodigoError() {\n" +
            "        return codigoError;\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "// Excepción para recursos\n" +
            "class RecursoNoEncontradoException extends JuegoException {\n" +
            "    public RecursoNoEncontradoException(String nombreRecurso) {\n" +
            "        super(\"Recurso no encontrado: \" + nombreRecurso, \"RESOURCE_404\");\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "public class SistemaManejoExcepciones {\n" +
            "    \n" +
            "    // Simulador de carga de recursos\n" +
            "    public static String cargarRecurso(String nombreArchivo) throws RecursoNoEncontradoException {\n" +
            "        // Simular que algunos archivos no existen\n" +
            "        if (nombreArchivo.contains(\"missing\")) {\n" +
            "            throw new RecursoNoEncontradoException(nombreArchivo);\n" +
            "        }\n" +
            "        \n" +
            "        return \"[Contenido de \" + nombreArchivo + \" cargado exitosamente]\";\n" +
            "    }\n" +
            "    \n" +
            "    // Simulador de operaciones de juego riesgosas\n" +
            "    public static int calcularPuntuacion(int baseScore, int multiplier, int enemies) \n" +
            "            throws ArithmeticException, IllegalArgumentException {\n" +
            "        \n" +
            "        if (baseScore < 0 || multiplier < 0 || enemies < 0) {\n" +
            "            throw new IllegalArgumentException(\"Los valores no pueden ser negativos\");\n" +
            "        }\n" +
            "        \n" +
            "        if (multiplier == 0) {\n" +
            "            throw new ArithmeticException(\"El multiplicador no puede ser cero\");\n" +
            "        }\n" +
            "        \n" +
            "        return baseScore + (enemies * 100 / multiplier);\n" +
            "    }\n" +
            "    \n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"=== MANEJO DE EXCEPCIONES EN JUEGOS ===");\n" +
            "        \n" +
            "        // TRY-CATCH BÁSICO\n" +
            "        System.out.println(\"\\n=== TRY-CATCH BÁSICO ===");\n" +
            "        \n" +
            "        ArrayList<Integer> puntuaciones = new ArrayList<>();\n" +
            "        puntuaciones.add(100);\n" +
            "        puntuaciones.add(200);\n" +
            "        \n" +
            "        try {\n" +
            "            // Operación riesgosa - acceso a índice\n" +
            "            System.out.println(\"Puntuación 0: \" + puntuaciones.get(0));\n" +
            "            System.out.println(\"Puntuación 1: \" + puntuaciones.get(1));\n" +
            "            System.out.println(\"Puntuación 5: \" + puntuaciones.get(5)); // ¡ERROR!\n" +
            "            \n" +
            "        } catch (IndexOutOfBoundsException e) {\n" +
            "            System.err.println(\"⚠️ Error: Intentaste acceder a un índice que no existe\");\n" +
            "            System.err.println(\"  Detalles: \" + e.getMessage());\n" +
            "            System.out.println(\"  🔧 El juego continúa normalmente...\");\n" +
            "        }\n" +
            "        \n" +
            "        // MULTIPLE CATCH\n" +
            "        System.out.println(\"\\n=== MÚTIPLE CATCH ===");\n" +
            "        \n" +
            "        try {\n" +
            "            String entrada = \"abc\"; // Simular entrada inválida\n" +
            "            int numero = Integer.parseInt(entrada);\n" +
            "            \n" +
            "            int resultado = 100 / numero; // Posible división por cero\n" +
            "            System.out.println(\"Resultado: \" + resultado);\n" +
            "            \n" +
            "        } catch (NumberFormatException e) {\n" +
            "            System.err.println(\"⚠️ Error: La entrada no es un número válido\");\n" +
            "            System.out.println(\"  🔧 Usando valor por defecto: 1\");\n" +
            "            \n" +
            "        } catch (ArithmeticException e) {\n" +
            "            System.err.println(\"⚠️ Error: División por cero detectada\");\n" +
            "            System.out.println(\"  🔧 Evitando división por cero\");\n" +
            "            \n" +
            "        } catch (Exception e) {\n" +
            "            System.err.println(\"⚠️ Error general: \" + e.getMessage());\n" +
            "            System.out.println(\"  🔧 Manejando error desconocido\");\n" +
            "        }\n" +
            "        \n" +
            "        // TRY-CATCH-FINALLY\n" +
            "        System.out.println(\"\\n=== TRY-CATCH-FINALLY ===");\n" +
            "        \n" +
            "        Scanner scanner = null;\n" +
            "        try {\n" +
            "            System.out.println(\"Intentando conectar con servidor de puntuaciones...\");\n" +
            "            \n" +
            "            // Simular operación que puede fallar\n" +
            "            if (Math.random() < 0.5) {\n" +
            "                throw new IOException(\"Conexión perdida con el servidor\");\n" +
            "            }\n" +
            "            \n" +
            "            System.out.println(\"✅ Conexión exitosa\");\n" +
            "            System.out.println(\"Subiendo puntuación...\");\n" +
            "            \n" +
            "        } catch (IOException e) {\n" +
            "            System.err.println(\"⚠️ Error de conexión: \" + e.getMessage());\n" +
            "            System.out.println(\"  🔧 Guardando puntuación localmente...\");\n" +
            "            \n" +
            "        } finally {\n" +
            "            // Este bloque SIEMPRE se ejecuta\n" +
            "            System.out.println(\"📋 Cerrando recursos y limpiando memoria...\");\n" +
            "            if (scanner != null) {\n" +
            "                scanner.close();\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        // TRY-WITH-RESOURCES (Java 7+)\n" +
            "        System.out.println(\"\\n=== TRY-WITH-RESOURCES ===");\n" +
            "        \n" +
            "        try (Scanner input = new Scanner(System.in)) {\n" +
            "            System.out.println(\"📁 Recurso abierto automáticamente\");\n" +
            "            // El Scanner se cierra automáticamente al salir del try\n" +
            "            \n" +
            "        } catch (Exception e) {\n" +
            "            System.err.println(\"Error: \" + e.getMessage());\n" +
            "        }\n" +
            "        // No necesitamos finally - se cierra automáticamente\n" +
            "        \n" +
            "        // EXCEPCIONES PERSONALIZADAS\n" +
            "        System.out.println(\"\\n=== EXCEPCIONES PERSONALIZADAS ===");\n" +
            "        \n" +
            "        String[] recursosJuego = {\"player.png\", \"missing_texture.jpg\", \"background.jpg\"};\n" +
            "        \n" +
            "        for (String recurso : recursosJuego) {\n" +
            "            try {\n" +
            "                String contenido = cargarRecurso(recurso);\n" +
            "                System.out.println(\"✅ \" + recurso + \" cargado\");\n" +
            "                \n" +
            "            } catch (RecursoNoEncontradoException e) {\n" +
            "                System.err.println(\"⚠️ \" + e.getMessage());\n" +
            "                System.err.println(\"  Código de error: \" + e.getCodigoError());\n" +
            "                System.out.println(\"  🔧 Usando recurso por defecto\");\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        // TESTING DE CÁLCULOS SEGUROS\n" +
            "        System.out.println(\"\\n=== CÁLCULOS SEGUROS ===");\n" +
            "        \n" +
            "        int[][] testCases = {\n" +
            "            {100, 2, 10},  // Caso normal\n" +
            "            {-50, 3, 5},   // Base negativa\n" +
            "            {200, 0, 8},   // Multiplicador cero\n" +
            "            {150, 5, -3}   // Enemigos negativos\n" +
            "        };\n" +
            "        \n" +
            "        for (int i = 0; i < testCases.length; i++) {\n" +
            "            int base = testCases[i][0];\n" +
            "            int mult = testCases[i][1];\n" +
            "            int enemigos = testCases[i][2];\n" +
            "            \n" +
            "            try {\n" +
            "                int puntuacion = calcularPuntuacion(base, mult, enemigos);\n" +
            "                System.out.println(\"Caso \" + (i+1) + \": Puntuación = \" + puntuacion);\n" +
            "                \n" +
            "            } catch (IllegalArgumentException e) {\n" +
            "                System.err.println(\"Caso \" + (i+1) + \" - Error de argumentos: \" + e.getMessage());\n" +
            "                \n" +
            "            } catch (ArithmeticException e) {\n" +
            "                System.err.println(\"Caso \" + (i+1) + \" - Error aritmético: \" + e.getMessage());\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        // JERARQUÍA DE EXCEPCIONES\n" +
            "        System.out.println(\"\\n=== JERARQUÍA DE EXCEPCIONES ===");\n" +
            "        \n" +
            "        try {\n" +
            "            // Diferentes tipos de excepciones\n" +
            "            throw new JuegoException(\"Error genérico del juego\", \"GAME_001\");\n" +
            "            \n" +
            "        } catch (JuegoException e) {\n" +
            "            System.err.println(\"🎮 Error de juego capturado: \" + e.getMessage());\n" +
            "            System.err.println(\"  Código: \" + e.getCodigoError());\n" +
            "            \n" +
            "        } catch (Exception e) {\n" +
            "            // Este catch nunca se ejecutará porque JuegoException es más específico\n" +
            "            System.err.println(\"Error general: \" + e.getMessage());\n" +
            "        }\n" +
            "        \n" +
            "        // PROPAGACIÓN DE EXCEPCIONES\n" +
            "        System.out.println(\"\\n=== PROPAGACIÓN DE EXCEPCIONES ===");\n" +
            "        \n" +
            "        try {\n" +
            "            inicializarJuego();\n" +
            "            System.out.println(\"🎮 Juego inicializado exitosamente\");\n" +
            "            \n" +
            "        } catch (JuegoException e) {\n" +
            "            System.err.println(\"😱 Error crítico al inicializar: \" + e.getMessage());\n" +
            "            System.err.println(\"  Código: \" + e.getCodigoError());\n" +
            "            System.out.println(\"  🔧 Intentando modo seguro...\");\n" +
            "            \n" +
            "        } catch (Exception e) {\n" +
            "            System.err.println(\"😨 Error inesperado: \" + e.getMessage());\n" +
            "        }\n" +
            "        \n" +
            "        System.out.println(\"\\n¡Manejo de excepciones dominado! Juego robusto y seguro.\");\n" +
            "    }\n" +
            "    \n" +
            "    // Método que puede lanzar múltiples excepciones\n" +
            "    public static void inicializarJuego() throws JuegoException {\n" +
            "        System.out.println(\"Inicializando sistemas del juego...\");\n" +
            "        \n" +
            "        try {\n" +
            "            // Cargar recursos esenciales\n" +
            "            cargarRecurso(\"player.png\");\n" +
            "            cargarRecurso(\"background.jpg\");\n" +
            "            cargarRecurso(\"missing_sound.wav\"); // Este fallará\n" +
            "            \n" +
            "        } catch (RecursoNoEncontradoException e) {\n" +
            "            // Re-lanzar como excepción más general\n" +
            "            throw new JuegoException(\"Fallo al cargar recursos: \" + e.getMessage(), \"INIT_FAIL\");\n" +
            "        }\n" +
            "    }\n" +
            "}",
            "**Jerarquía de Excepciones en Java:**\n\n" +
            "```\n" +
            "Throwable\n" +
            "├── Error (errores críticos del sistema)\n" +
            "└── Exception\n" +
            "    ├── RuntimeException (no checked)\n" +
            "    │   ├── NullPointerException\n" +
            "    │   ├── IndexOutOfBoundsException\n" +
            "    │   └── IllegalArgumentException\n" +
            "    └── Checked Exceptions\n" +
            "        ├── IOException\n" +
            "        └── SQLException\n" +
            "```\n\n" +
            "**Tipos de Excepciones:**\n\n" +
            "• **Checked Exceptions**: Deben manejarse o declararse\n" +
            "  - IOException, SQLException, etc.\n" +
            "  - Errores predecibles que se pueden manejar\n\n" +
            "• **Unchecked Exceptions**: Opcionales de manejar\n" +
            "  - RuntimeException y sus subclases\n" +
            "  - Errores de programación generalmente\n\n" +
            "**Estructuras de Manejo:**\n\n" +
            "**Try-Catch Básico:**\n" +
            "```java\n" +
            "try {\n" +
            "    // código riesgoso\n" +
            "} catch (TipoException e) {\n" +
            "    // manejar excepción\n" +
            "}\n" +
            "```\n\n" +
            "**Try-Catch-Finally:**\n" +
            "```java\n" +
            "try {\n" +
            "    // código riesgoso\n" +
            "} catch (Exception e) {\n" +
            "    // manejar excepción\n" +
            "} finally {\n" +
            "    // siempre se ejecuta\n" +
            "}\n" +
            "```\n\n" +
            "**Try-With-Resources:**\n" +
            "```java\n" +
            "try (Scanner sc = new Scanner(file)) {\n" +
            "    // usar recurso\n" +
            "} // se cierra automáticamente\n" +
            "```\n\n" +
            "**Buenas Prácticas:**\n" +
            "- Manejar excepciones específicas primero\n" +
            "- No capturar excepciones y no hacer nada\n" +
            "- Loguear errores para debugging\n" +
            "- Proporcionar fallbacks cuando sea posible\n" +
            "- Crear excepciones personalizadas para tu dominio\n" +
            "- Usar finally para limpieza de recursos\n\n" +
            "**En Desarrollo de Juegos:**\n" +
            "- Manejar carga de recursos faltantes\n" +
            "- Errores de conexión de red\n" +
            "- Problemas de memoria\n" +
            "- Estados inválidos del juego\n" +
            "- Errores de entrada del usuario",
            1
        );
        contenidoDAO.insertarContenido(cap15);
    }
    
    /**
     * CAPÍTULOS 11-15: ESTRUCTURAS DE DATOS Y COLECCIONES
     * Placeholder para futuras implementaciones
     */
    private void insertarCapitulos11a15_EstructurasDatos() {
        // Ya implementado arriba
    }
    
    private void insertarCapitulos16a20_InterfacesGraficas() {
        // Implementación futura en FASE 3
    }
    
    private void insertarCapitulos21a25_DesarrolloJuego() {
        // Implementación futura en FASE 4
    }
    
    private void insertarCapitulos26a30_OptimizacionFinalizacion() {
        // Implementación futura en FASE 5
    }
    
    /**
     * Verifica si el contenido ya está inicializado
     */
    public boolean tieneContenido() {
        return contenidoDAO.obtenerNumeroCapitulos() > 0;
    }
}