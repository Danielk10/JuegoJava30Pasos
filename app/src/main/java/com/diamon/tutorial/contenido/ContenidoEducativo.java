package com.diamon.tutorial.contenido;

import android.content.Context;
import com.diamon.tutorial.database.ContenidoDAO;
import com.diamon.tutorial.models.ContenidoTutorial;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona todo el contenido educativo del tutorial
 * "Juego Java 30 Pasos"
 * 
 * Esta clase contiene el contenido completo y estructurado de los 30 capítulos
 * del tutorial, desde conceptos básicos hasta el desarrollo completo del juego.
 * 
 * @author Daniel Diamon
 * @version 1.0
 */
public class ContenidoEducativo {
    
    private Context context;
    private ContenidoDAO contenidoDAO;
    
    public ContenidoEducativo(Context context) {
        this.context = context;
        this.contenidoDAO = new ContenidoDAO(context);
    }
    
    /**
     * Inicializa todo el contenido educativo en la base de datos
     */
    public void inicializarContenidoCompleto() {
        // Limpiar contenido existente
        contenidoDAO.limpiarTodoElContenido();
        
        // Insertar todo el contenido educativo
        insertarCapitulos1a5_Fundamentos();
        insertarCapitulos6a10_POO();
        insertarCapitulos11a15_EstructurasDatos();
        insertarCapitulos16a20_InterfacesGraficas();
        insertarCapitulos21a25_DesarrolloJuego();
        insertarCapitulos26a30_OptimizacionFinalizacion();
    }
    
    /**
     * CAPÍTULOS 1-5: FUNDAMENTOS DE JAVA
     */
    private void insertarCapitulos1a5_Fundamentos() {
        
        // CAPÍTULO 1: Introducción a Java y Primer Programa
        ContenidoTutorial cap1 = new ContenidoTutorial(
            1,
            "Paso 1: Tu Primer Programa en Java",
            "Bienvenido al mundo de la programación en Java. En este primer paso, aprenderás a crear tu primer programa y entenderás la estructura básica de Java. Este es el fundamento sobre el cual construiremos nuestro juego completo.",
            "// Mi primer programa en Java\n" +
            "public class HolaMundo {\n" +
            "    public static void main(String[] args) {\n" +
            "        System.out.println(\"¡Hola, mundo del desarrollo de juegos!\");\n" +
            "        System.out.println(\"Bienvenido al Tutorial Java 30 Pasos\");\n" +
            "    }\n" +
            "}",
            "**Explicación detallada:**\n\n" +
            "• **public class HolaMundo**: Declara una clase pública llamada 'HolaMundo'\n" +
            "• **public static void main**: Es el punto de entrada del programa\n" +
            "• **String[] args**: Parámetros que el programa puede recibir\n" +
            "• **System.out.println()**: Imprime texto en la consola\n\n" +
            "**Conceptos clave:**\n" +
            "- Java es un lenguaje orientado a objetos\n" +
            "- Todo código debe estar dentro de una clase\n" +
            "- El método main es obligatorio para ejecutar el programa\n" +
            "- Java distingue entre mayúsculas y minúsculas",
            1
        );
        contenidoDAO.insertarContenido(cap1);
        
        // CAPÍTULO 2: Variables y Tipos de Datos
        ContenidoTutorial cap2 = new ContenidoTutorial(
            2,
            "Paso 2: Variables y Tipos de Datos",
            "Las variables son espacios en memoria donde almacenamos información. Java tiene diferentes tipos de datos para diferentes tipos de información. Aprenderás a usar variables que serán fundamentales para desarrollar nuestro juego.",
            "// Tipos de datos primitivos\n" +
            "public class VariablesJuego {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Números enteros\n" +
            "        int puntuacion = 100;\n" +
            "        int vidas = 3;\n" +
            "        int nivel = 1;\n" +
            "        \n" +
            "        // Números decimales\n" +
            "        double velocidad = 5.5;\n" +
            "        double posicionX = 10.0;\n" +
            "        double posicionY = 20.0;\n" +
            "        \n" +
            "        // Valores verdadero/falso\n" +
            "        boolean juegoActivo = true;\n" +
            "        boolean jugadorVivo = true;\n" +
            "        boolean juegoTerminado = false;\n" +
            "        \n" +
            "        // Caracteres\n" +
            "        char inicial = 'J';\n" +
            "        char grado = 'A';\n" +
            "        \n" +
            "        // Texto (String)\n" +
            "        String nombreJugador = \"Desarrollador\";\n" +
            "        String mensajeBienvenida = \"¡Comienza el juego!\";\n" +
            "        \n" +
            "        // Mostrar los valores\n" +
            "        System.out.println(\"Jugador: \" + nombreJugador);\n" +
            "        System.out.println(\"Puntuación: \" + puntuacion);\n" +
            "        System.out.println(\"Vidas: \" + vidas);\n" +
            "        System.out.println(\"Posición: (\" + posicionX + \", \" + posicionY + \")\");\n" +
            "    }\n" +
            "}",
            "**Tipos de datos primitivos:**\n\n" +
            "• **int**: Números enteros (-2,147,483,648 a 2,147,483,647)\n" +
            "• **double**: Números decimales (hasta 15 dígitos de precisión)\n" +
            "• **boolean**: true o false únicamente\n" +
            "• **char**: Un solo carácter (entre comillas simples)\n\n" +
            "**Tipos por referencia:**\n" +
            "• **String**: Cadenas de texto (entre comillas dobles)\n\n" +
            "**Reglas importantes:**\n" +
            "- Los nombres de variables deben empezar con letra o _\n" +
            "- No pueden contener espacios\n" +
            "- Usar camelCase: nombreVariable\n" +
            "- Ser descriptivos: 'puntuacion' mejor que 'p'",
            1
        );
        contenidoDAO.insertarContenido(cap2);
        
        // CAPÍTULO 3: Operadores y Expresiones
        ContenidoTutorial cap3 = new ContenidoTutorial(
            3,
            "Paso 3: Operadores y Expresiones",
            "Los operadores nos permiten realizar cálculos, comparaciones y operaciones lógicas. Son esenciales para crear la lógica de nuestro juego, como calcular puntuaciones, verificar colisiones y controlar el flujo del juego.",
            "public class OperadoresJuego {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Variables del juego\n" +
            "        int puntuacionBase = 100;\n" +
            "        int multiplicador = 3;\n" +
            "        int vidas = 5;\n" +
            "        int enemigosDestruidos = 10;\n" +
            "        \n" +
            "        // OPERADORES ARITMÉTICOS\n" +
            "        int puntuacionTotal = puntuacionBase + (enemigosDestruidos * multiplicador);\n" +
            "        int vidasRestantes = vidas - 1;\n" +
            "        int bonificacion = puntuacionTotal / 2;\n" +
            "        int resto = puntuacionTotal % 100; // Módulo\n" +
            "        \n" +
            "        // OPERADORES DE COMPARACIÓN\n" +
            "        boolean puedeJugar = vidas > 0;\n" +
            "        boolean juegoTerminado = vidas <= 0;\n" +
            "        boolean nuevoRecord = puntuacionTotal >= 1000;\n" +
            "        boolean puntuacionBaja = puntuacionTotal < 500;\n" +
            "        boolean puntuacionExacta = puntuacionTotal == 1000;\n" +
            "        boolean puntuacionDiferente = puntuacionTotal != 0;\n" +
            "        \n" +
            "        // OPERADORES LÓGICOS\n" +
            "        boolean condicionVictoria = (enemigosDestruidos >= 10) && (vidas > 0);\n" +
            "        boolean gameOver = (vidas <= 0) || (puntuacionTotal < 0);\n" +
            "        boolean continuar = !juegoTerminado; // NOT lógico\n" +
            "        \n" +
            "        // OPERADORES DE INCREMENTO/DECREMENTO\n" +
            "        vidas++; // Incrementa en 1\n" +
            "        enemigosDestruidos--; // Decrementa en 1\n" +
            "        \n" +
            "        // Mostrar resultados\n" +
            "        System.out.println(\"Puntuación total: \" + puntuacionTotal);\n" +
            "        System.out.println(\"¿Puede jugar?: \" + puedeJugar);\n" +
            "        System.out.println(\"¿Nuevo récord?: \" + nuevoRecord);\n" +
            "        System.out.println(\"¿Condición de victoria?: \" + condicionVictoria);\n" +
            "    }\n" +
            "}",
            "**Operadores Aritméticos:**\n" +
            "• **+** Suma\n" +
            "• **-** Resta\n" +
            "• **\*** Multiplicación\n" +
            "• **/** División\n" +
            "• **%** Módulo (resto de división)\n\n" +
            "**Operadores de Comparación:**\n" +
            "• **>** Mayor que\n" +
            "• **<** Menor que\n" +
            "• **>=** Mayor o igual\n" +
            "• **<=** Menor o igual\n" +
            "• **==** Igual a\n" +
            "• **!=** Diferente de\n\n" +
            "**Operadores Lógicos:**\n" +
            "• **&&** AND lógico (ambas condiciones verdaderas)\n" +
            "• **||** OR lógico (al menos una condición verdadera)\n" +
            "• **!** NOT lógico (invierte el valor booleano)\n\n" +
            "**Precedencia:** Los operadores se evalúan en orden específico. Usa paréntesis para controlar el orden.",
            1
        );
        contenidoDAO.insertarContenido(cap3);
        
        // CAPÍTULO 4: Estructuras de Control - Condicionales
        ContenidoTutorial cap4 = new ContenidoTutorial(
            4,
            "Paso 4: Estructuras de Control - Condicionales",
            "Las estructuras condicionales permiten que nuestro programa tome decisiones. En un juego, necesitamos verificar constantemente condiciones: ¿el jugador tocó un enemigo? ¿se acabaron las vidas? ¿completó el nivel?",
            "public class CondicionalesJuego {\n" +
            "    public static void main(String[] args) {\n" +
            "        // Variables del estado del juego\n" +
            "        int vidas = 3;\n" +
            "        int puntuacion = 1250;\n" +
            "        boolean tieneLlave = true;\n" +
            "        String nivelActual = \"facil\";\n" +
            "        \n" +
            "        // CONDICIONAL IF-ELSE SIMPLE\n" +
            "        if (vidas > 0) {\n" +
            "            System.out.println(\"El jugador puede continuar\");\n" +
            "        } else {\n" +
            "            System.out.println(\"Game Over - Reiniciar juego\");\n" +
            "        }\n" +
            "        \n" +
            "        // CONDICIONAL IF-ELSE IF-ELSE\n" +
            "        if (puntuacion >= 2000) {\n" +
            "            System.out.println(\"¡Puntuación excelente! Rango S\");\n" +
            "        } else if (puntuacion >= 1500) {\n" +
            "            System.out.println(\"¡Muy buena puntuación! Rango A\");\n" +
            "        } else if (puntuacion >= 1000) {\n" +
            "            System.out.println(\"Buena puntuación. Rango B\");\n" +
            "        } else if (puntuacion >= 500) {\n" +
            "            System.out.println(\"Puntuación regular. Rango C\");\n" +
            "        } else {\n" +
            "            System.out.println(\"Puntuación baja. Rango D\");\n" +
            "        }\n" +
            "        \n" +
            "        // CONDICIONALES ANIDADAS\n" +
            "        if (vidas > 0) {\n" +
            "            if (tieneLlave) {\n" +
            "                System.out.println(\"Puede abrir la puerta secreta\");\n" +
            "            } else {\n" +
            "                System.out.println(\"Necesita encontrar la llave\");\n" +
            "            }\n" +
            "        }\n" +
            "        \n" +
            "        // SWITCH STATEMENT\n" +
            "        switch (nivelActual) {\n" +
            "            case \"facil\":\n" +
            "                System.out.println(\"Nivel Fácil: Enemigos lentos\");\n" +
            "                break;\n" +
            "            case \"medio\":\n" +
            "                System.out.println(\"Nivel Medio: Enemigos normales\");\n" +
            "                break;\n" +
            "            case \"dificil\":\n" +
            "                System.out.println(\"Nivel Difícil: Enemigos rápidos\");\n" +
            "                break;\n" +
            "            case \"extremo\":\n" +
            "                System.out.println(\"Nivel Extremo: ¡Supervivencia!\");\n" +
            "                break;\n" +
            "            default:\n" +
            "                System.out.println(\"Nivel desconocido\");\n" +
            "                break;\n" +
            "        }\n" +
            "        \n" +
            "        // OPERADOR TERNARIO (condicional compacto)\n" +
            "        String estado = (vidas > 0) ? \"Vivo\" : \"Muerto\";\n" +
            "        System.out.println(\"Estado del jugador: \" + estado);\n" +
            "        \n" +
            "        // CONDICIONES COMPLEJAS\n" +
            "        if ((vidas > 0) && (puntuacion >= 1000) && tieneLlave) {\n" +
            "            System.out.println(\"¡Acceso al nivel bonus desbloqueado!\");\n" +
            "        }\n" +
            "    }\n" +
            "}",
            "**Estructura IF-ELSE:**\n" +
            "```\n" +
            "if (condicion) {\n" +
            "    // código si es verdadero\n" +
            "} else {\n" +
            "    // código si es falso\n" +
            "}\n" +
            "```\n\n" +
            "**Estructura IF-ELSE IF:**\n" +
            "Permite evaluar múltiples condiciones en secuencia.\n\n" +
            "**Estructura SWITCH:**\n" +
            "Útil cuando tenemos muchas opciones basadas en un valor específico.\n" +
            "¡No olvides el 'break'!\n\n" +
            "**Operador Ternario:**\n" +
            "```\n" +
            "variable = (condicion) ? valorSiVerdadero : valorSiFalso;\n" +
            "```\n\n" +
            "**Buenas prácticas:**\n" +
            "- Usar llaves {} siempre, incluso para una línea\n" +
            "- Condiciones claras y legibles\n" +
            "- Evitar anidamiento excesivo",
            1
        );
        contenidoDAO.insertarContenido(cap4);
        
        // CAPÍTULO 5: Estructuras de Control - Bucles
        ContenidoTutorial cap5 = new ContenidoTutorial(
            5,
            "Paso 5: Estructuras de Control - Bucles",
            "Los bucles nos permiten repetir código múltiples veces. En los juegos, los usamos para el game loop principal, para procesar múltiples enemigos, para animaciones, y muchas otras tareas repetitivas.",
            "public class BuclesJuego {\n" +
            "    public static void main(String[] args) {\n" +
            "        // BUCLE FOR - Para contar repeticiones específicas\n" +
            "        System.out.println(\"=== BUCLE FOR ===");\n" +
            "        System.out.println(\"Creando enemigos:\");\n" +
            "        for (int i = 1; i <= 5; i++) {\n" +
            "            System.out.println(\"Enemigo #\" + i + \" creado en posición \" + (i * 10));\n" +
            "        }\n" +
            "        \n" +
            "        // BUCLE WHILE - Mientras se cumpla una condición\n" +
            "        System.out.println(\"\\n=== BUCLE WHILE ===");\n" +
            "        int vidas = 3;\n" +
            "        int ronda = 1;\n" +
            "        while (vidas > 0 && ronda <= 5) {\n" +
            "            System.out.println(\"Ronda \" + ronda + \": Vidas restantes = \" + vidas);\n" +
            "            \n" +
            "            // Simular daño aleatorio\n" +
            "            if (ronda % 2 == 0) { // En rondas pares, recibe daño\n" +
            "                vidas--;\n" +
            "                System.out.println(\"  ¡Recibiste daño! Vidas: \" + vidas);\n" +
            "            }\n" +
            "            ronda++;\n" +
            "        }\n" +
            "        \n" +
            "        if (vidas <= 0) {\n" +
            "            System.out.println(\"Game Over\");\n" +
            "        } else {\n" +
            "            System.out.println(\"¡Sobreviviste todas las rondas!\");\n" +
            "        }\n" +
            "        \n" +
            "        // BUCLE DO-WHILE - Se ejecuta al menos una vez\n" +
            "        System.out.println(\"\\n=== BUCLE DO-WHILE ===");\n" +
            "        int intentos = 0;\n" +
            "        boolean acierto = false;\n" +
            "        \n" +
            "        do {\n" +
            "            intentos++;\n" +
            "            System.out.println(\"Intento #\" + intentos + \" de abrir cofre...\");\n" +
            "            \n" +
            "            // Simular 30% de probabilidad de éxito\n" +
            "            if (intentos >= 3) {\n" +
            "                acierto = true;\n" +
            "                System.out.println(\"¡Cofre abierto!\");\n" +
            "            }\n" +
            "        } while (!acierto && intentos < 5);\n" +
            "        \n" +
            "        // BUCLES ANIDADOS - Crear un mapa\n" +
            "        System.out.println(\"\\n=== BUCLES ANIDADOS ===");\n" +
            "        System.out.println(\"Generando mapa 5x5:\");\n" +
            "        for (int fila = 0; fila < 5; fila++) {\n" +
            "            for (int columna = 0; columna < 5; columna++) {\n" +
            "                if (fila == 0 || fila == 4 || columna == 0 || columna == 4) {\n" +
            "                    System.out.print(\"# \"); // Pared\n" +
            "                } else {\n" +
            "                    System.out.print(\". \"); // Espacio libre\n" +
            "                }\n" +
            "            }\n" +
            "            System.out.println(); // Nueva línea\n" +
            "        }\n" +
            "        \n" +
            "        // CONTROL DE BUCLES - break y continue\n" +
            "        System.out.println(\"\\n=== CONTROL DE BUCLES ===");\n" +
            "        System.out.println(\"Buscando power-up especial:\");\n" +
            "        for (int objeto = 1; objeto <= 10; objeto++) {\n" +
            "            if (objeto % 3 == 0) {\n" +
            "                System.out.println(\"  Objeto \" + objeto + \": Bomba - ¡Evitado!\");\n" +
            "                continue; // Salta a la siguiente iteración\n" +
            "            }\n" +
            "            \n" +
            "            if (objeto == 7) {\n" +
            "                System.out.println(\"  Objeto \" + objeto + \": ¡POWER-UP ENCONTRADO!\");\n" +
            "                break; // Sale del bucle\n" +
            "            }\n" +
            "            \n" +
            "            System.out.println(\"  Objeto \" + objeto + \": Moneda común\");\n" +
            "        }\n" +
            "        \n" +
            "        System.out.println(\"\\n¡Ejemplo de bucles completado!\");\n" +
            "    }\n" +
            "}",
            "**Bucle FOR:**\n" +
            "```\n" +
            "for (inicialización; condición; actualización) {\n" +
            "    // código a repetir\n" +
            "}\n" +
            "```\n" +
            "Ideal cuando sabes cuántas veces repetir.\n\n" +
            "**Bucle WHILE:**\n" +
            "```\n" +
            "while (condición) {\n" +
            "    // código a repetir\n" +
            "}\n" +
            "```\n" +
            "Evalúa la condición antes de ejecutar.\n\n" +
            "**Bucle DO-WHILE:**\n" +
            "```\n" +
            "do {\n" +
            "    // código a repetir\n" +
            "} while (condición);\n" +
            "```\n" +
            "Se ejecuta al menos una vez.\n\n" +
            "**Control de bucles:**\n" +
            "• **break**: Sale completamente del bucle\n" +
            "• **continue**: Salta a la siguiente iteración\n\n" +
            "**Aplicaciones en juegos:**\n" +
            "- Game loop principal\n" +
            "- Procesamiento de arrays de enemigos\n" +
            "- Generación de mapas\n" +
            "- Animaciones frame por frame",
            1
        );
        contenidoDAO.insertarContenido(cap5);
    }
    
    /**
     * CAPÍTULOS 6-10: PROGRAMACIÓN ORIENTADA A OBJETOS
     */
    private void insertarCapitulos6a10_POO() {
        // El contenido de POO se implementará en la siguiente iteración
        // Placeholder para mantener la estructura
        ContenidoTutorial cap6 = new ContenidoTutorial(
            6,
            "Paso 6: Clases y Objetos Básicos",
            "La Programación Orientada a Objetos (POO) es el paradigma fundamental de Java. Aprenderemos a crear clases que representen elementos de nuestro juego como jugadores, enemigos y objetos.",
            "// Próximamente: Contenido completo de POO",
            "Este capítulo está siendo desarrollado con contenido completo sobre clases y objetos.",
            1
        );
        contenidoDAO.insertarContenido(cap6);
    }
    
    /**
     * CAPÍTULOS 11-15: ESTRUCTURAS DE DATOS Y COLECCIONES
     * Placeholder para futuras implementaciones
     */
    private void insertarCapitulos11a15_EstructurasDatos() {
        // Implementación futura
    }
    
    private void insertarCapitulos16a20_InterfacesGraficas() {
        // Implementación futura
    }
    
    private void insertarCapitulos21a25_DesarrolloJuego() {
        // Implementación futura
    }
    
    private void insertarCapitulos26a30_OptimizacionFinalizacion() {
        // Implementación futura
    }
    
    /**
     * Verifica si el contenido ya está inicializado
     */
    public boolean tieneContenido() {
        return contenidoDAO.obtenerNumeroCapitulos() > 0;
    }
}