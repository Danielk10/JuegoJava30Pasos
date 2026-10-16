# ⚡ Propuesta de Arquitectura: Flash SPI Tool PRO & Monetización Híbrida Ética

> **Aplicación:** Flash SPI Tool (`com.diamon.curso`)  
> **Estado:** Documento de Especificación y Diseño Arquitectónico PRO  
> **Fecha:** 16 de Octubre de 2026  
> **Basado en el éxito probado de:** `PIC-k150-Programing` y `C-PIC Compiler`

---

## 📋 1. Resumen Ejecutivo y Visión del Proyecto

**Flash SPI Tool** se ha consolidado como una herramienta de referencia mundial en Android con calificaciones impecables de **5 estrellas**, gracias a su capacidad única de ejecutar `flashrom` nativo sobre arquitecturas ARM64, soportando programadores directos por hardware (**CH341A, FT2232, Dediprog, ST-Link**) y programadores seriales mediante puente PTY/Socket (**Serprog/Arduino, Bus Pirate, SPIDriver**).

Para capitalizar esta excelente recepción sin comprometer el cariño y la confianza de la comunidad técnica:
1. **Regla de Oro Ética:** **TODO lo que la app hace hoy seguirá siendo 100% GRATUITO E ILIMITADO.** Ningún técnico quedará bloqueado para leer, escribir, verificar, borrar o detectar chips.
2. **Monetización Híbrida Ética (Pase Pro 12 Horas + Compra In-App):**
   - **🎬 Pase Pro 12 Horas Gratuito:** Al ver un video publicitario bonificado (Rewarded Video de AdMob), el usuario desbloquea todas las herramientas avanzadas y elimina la publicidad por **12 horas continuas**. Cualquier técnico en cualquier país, incluso sin tarjeta de crédito, puede trabajar al 100%.
   - **👑 Compra In-App Permanente ($4.99 USD):** Mediante Google Play Billing 9.1.0 (`remove_ads_pro`), para quienes desean apoyar al desarrollador y tener la suite Pro y cero publicidad para siempre.
3. **Nuevas Funcionalidades Pro de Alto Valor:** Herramientas quirúrgicas para técnicos de placas madre (motherboards), reparadores de laptops, talleres automotrices (ECU) y entusiastas de BIOS/firmware.

---

## 🛡️ 2. Garantía Ética: Matriz Comparativa (Gratis vs. Pro)

| Funcionalidad / Herramienta | Versión Gratuita (Para Siempre) | Suite PRO (Pase 12h o Permanente) |
| :--- | :---: | :---: |
| **Conexión USB de Programadores (CH341A, FT2232, Serprog, etc.)** | ✅ 100% Gratis | ✅ Incluido |
| **Detección y Reconocimiento de Chip (Probe / JEDEC ID)** | ✅ 100% Gratis | ✅ Incluido |
| **Lectura Completa de Flash ROM (`-r bios.bin`)** | ✅ 100% Gratis | ✅ Incluido |
| **Escritura Completa de Flash ROM (`-w bios.bin`)** | ✅ 100% Gratis | ✅ Incluido |
| **Borrado Masivo del Chip (`--erase`)** | ✅ 100% Gratis | ✅ Incluido |
| **Verificación Completa Byte a Byte (`-v bios.bin`)** | ✅ 100% Gratis | ✅ Incluido |
| **Consola de Comandos Personalizados (`flashrom CLI`)** | ✅ 100% Gratis | ✅ Incluido |
| **Diagramas de Pines y Pinouts de Hardware (SOIC8, CH341A, etc.)** | ✅ 100% Gratis | ✅ Incluido |
| **Exportación de Firmware Serprog Arduino (.ino / .hex)** | ✅ 100% Gratis | ✅ Incluido |
| **Importación y Exportación de Dumps (.bin, .hex, .rom)** | ✅ 100% Gratis | ✅ Incluido |
| **Visor Hexadecimal Estándar (Lectura de memoria mmap)** | ✅ 100% Gratis | ✅ Incluido |
| **Comparador Hex Diff Básico** | ✅ 100% Gratis | ✅ Incluido |
| ───────────────────────────────────────────────────────────── | ────────────── | ────────────── |
| 🛡️ **Bóveda de Seguridad (Backup Pre-Flash Automático Antibrick)** | ❌ Pro | 💎 **DESBLOQUEADO** |
| 🔒 **Verificador Criptográfico de Integridad (MD5, SHA-256, CRC32)**| ❌ Pro | 💎 **DESBLOQUEADO** |
| 📋 **Comprobación Automática contra Hash del Portapapeles** | ❌ Pro | 💎 **DESBLOQUEADO** |
| 📄 **Exportación de Archivos Sidecar (.md5 y .sha256)** | ❌ Pro | 💎 **DESBLOQUEADO** |
| ✏️ **Editor Hexadecimal en Vivo con Parcheo de Bytes In-Place** | ❌ Pro | 💎 **DESBLOQUEADO** |
| 🌐 **Inyector de MAC Intel GbE LAN con Checksum NVM (0xBABA)** | ❌ Pro | 💎 **DESBLOQUEADO** |
| 🆔 **Clonador y Editor DMI/SMBIOS (UUID de placa y Número de Serie)**| ❌ Pro | 💎 **DESBLOQUEADO** |
| 🔑 **Extractor de Clave de Producto Windows OEM (MSDM / ACPI)** | ❌ Pro | 💎 **DESBLOQUEADO** |
| 🩺 **Analizador de Salud y Versión de Intel ME / CSME ($FPT)** | ❌ Pro | 💎 **DESBLOQUEADO** |
| 🔓 **Desbloqueo de Registros de Estado (BP0..BP3, SRP0) en 1 Clic** | ❌ Pro | 💎 **DESBLOQUEADO** |
| 🚀 **Activador y Corrector de Quad Enable (Bit QE en SR2)** | ❌ Pro | 💎 **DESBLOQUEADO** |
| ⚡ **Flasheo Delta Selectivo de Sectores 4KB (Ahorro del 90% tiempo)**| ❌ Pro | 💎 **DESBLOQUEADO** |
| 🗺️ **Mapa de Calor (Heatmap) de Sectores Modificados en HexDiff** | ❌ Pro | 💎 **DESBLOQUEADO** |
| 🏎️ **Turbo Baudrate Serprog a 2,000,000 Baudios y Clock Tuning** | ❌ Pro | 💎 **DESBLOQUEADO** |
| 🚫 **Cero Publicidad (Supresión Absoluta de Banners e Interstitials)**| ❌ (Con Ads) | 💎 **100% SIN ANUNCIOS** |

---

## 🏛️ 3. Arquitectura del Sistema de Monetización Híbrido

Inspirado exactamente en el módulo de monetización exitoso de **PIC-k150-Programing**:

```
                              ┌────────────────────────┐
                              │  Flash SPI Tool UI     │
                              │  (Herramienta Pro)     │
                              └───────────┬────────────┘
                                          │
                              ┌───────────▼────────────┐
                              │ ¿Usuario es Pro Activo?│
                              │ isProActive(billing)   │
                              └─────┬────────────┬─────┘
                                    │ SÍ         │ NO
                        ┌───────────▼──┐   ┌─────▼──────────────────┐
                        │ Abre Función │   │ ProAccessDialog.java   │
                        │ Directamente │   │ Modal con 2 Opciones   │
                        └──────────────┘   └─────┬────────────┬─────┘
                                                 │            │
                         Opción 1: Pase 12 Horas │            │ Opción 2: Compra Permanente
                                                 ▼            ▼
                                    ┌─────────────────┐  ┌──────────────────┐
                                    │ Rewarded Video  │  │ Google Play      │
                                    │ AdMob           │  │ Billing 9.1.0    │
                                    │ (12 Horas Pass) │  │ (remove_ads_pro) │
                                    └────────┬────────┘  └────────┬─────────┘
                                             │                    │
                                             └──────────┬─────────┘
                                                        ▼
                                           ┌────────────────────────┐
                                           │  ACTIVA ESTADO PRO     │
                                           │  • Oculta Banners      │
                                           │  • Suprime Intersticial│
                                           │  • Habilita Menú Pro   │
                                           └────────────────────────┘
```

### 3.1 Google Play Billing 9.1.0 (`BillingManager.java`)
- **Product ID Principal:** `remove_ads_pro` (no consumible).
- **Product ID Alternativo:** `remove_ads` (retrocompatibilidad garantizada).
- **Donación Opcional:** Se conserva `donacion_pizza` para quienes deseen seguir aportando consumibles.
- **Consultas Silenciosas:** Todas las comprobaciones de red se realizan en segundo plano sin interrumpir ni escribir mensajes de error en la consola de flasheo.
- **Acknowledge Automático:** Confirmación de compra (`acknowledgePurchase`) para evitar reembolsos a los 3 días.

### 3.2 Pase Pro de 12 Horas (`ProPassManager.java`)
- Otorga 12 horas continuas (`12 * 3600 * 1000 ms`) tras ver un video bonificado de AdMob.
- **Triple Respaldo:**
  1. `SharedPreferences` (`pro_pass_prefs.xml`).
  2. Respaldo local firmado con hash SHA-256 (`.flash_spi_pro_pass.dat`) resistente a limpieza de caché.
  3. Formateo en vivo en el menú superior: `⭐ Pro 11h 45m`.

### 3.3 Gestión Publicitaria (`MostrarPublicidad.java`)
- Control mediante interfaz `AdsDisabledChecker`:
  - Si el usuario tiene Pase Pro activo o compra permanente, el banner adaptativo se oculta con `View.GONE` y los interstitials se omiten inmediatamente con `return false`.
- Unidad Rewarded Video AdMob: `ca-app-pub-5141499161332805/7291519778`.

---

## 🔬 4. Las Nuevas Funciones PRO en Detalle

### 🔷 PILAR 1: Suite de Integridad y Checksums Criptográficos

El flasheo de BIOS corruptas descargadas de foros o volcados con cables flojos es la causa #1 de equipos que no vuelven a encender.

1. **Motor de Hash Multialgoritmo en Streaming:**
   - Cálculo instantáneo mediante buffers directos NIO (`ByteBuffer.allocateDirect(262144)`) sin riesgo de memoria fuera de límites (`OutOfMemoryError`) en volcados de 16MB, 32MB o 64MB:
     * **MD5** (128 bits): Estándar en archivos de soporte de fabricantes antiguos.
     * **SHA-1** (160 bits): Frecuente en manifiestos corporativos.
     * **SHA-256** (256 bits): Estándar moderno de seguridad criptográfica.
     * **CRC32** (32 bits): Verificación rápida para regiones GbE y tablas de configuración.
2. **Badge de Coincidencia con el Portapapeles (Clipboard Match):**
   - Al abrir el diálogo de integridad, la app lee automáticamente el portapapeles. Si detecta una cadena hash válida, la compara en tiempo real:
     * 🟢 **MATCH CERTIFIED:** *"El archivo coincide exactamente con el hash oficial del fabricante. Es 100% seguro para flashear."*
     * 🔴 **INTEGRITY WARNING:** *"El hash NO coincide con el portapapeles. Riesgo de archivo corrupto o modelo incompatible."*
3. **Exportación de Archivos Sidecar GNU (`.md5` y `.sha256`):**
   - Opción para generar automáticamente los archivos de verificación junto al volcado `bios_dump.bin.sha256` y `bios_dump.bin.md5`, compatibles con `sha256sum -c` en Linux y PowerShell en Windows.

---

### 🔷 PILAR 2: Editor Hexadecimal en Vivo y Parcheo Quirúrgico de BIOS

Hasta hoy, el visor hexadecimal de la app era de solo lectura. La suite PRO lo convierte en una estación de modificación quirúrgica:

1. **Edición In-Place de Bytes con Historial:**
   - Al pulsar sobre cualquier celda, se abre el diálogo editor de byte:
     * Dirección Hex y Decimal (`0x00001000 [4096]`).
     * Valor actual en Hex, Binario y carácter ASCII.
     * Desglose Little-Endian / Big-Endian para palabras de 16 y 32 bits.
     * Pila de Deshacer / Rehacer (`Undo / Redo`).
   - Las celdas modificadas se resaltan en color ámbar (`#FFB300`).
2. **Inyector de MAC Intel GbE LAN con Corrección de Checksum `0xBABA`:**
   - **El Problema:** Al clonar o cambiar una BIOS en motherboards con tarjeta de red Intel integrada, la dirección MAC se encuentra en los primeros 6 bytes de la región GbE (offset `0x1000` o `0x2000`). Intel exige por hardware que la suma de palabras de 16 bits de esa región sume exactamente `0xBABA`. Si se cambia la MAC manualmente sin recalcular ese checksum, Windows arroja **"Código 10: El dispositivo no puede iniciar"**.
   - **La Solución Pro:** Un asistente interactivo donde el técnico ingresa la MAC original de la etiqueta del equipo; la app la inyecta y **recalcula automáticamente la palabra complementaria en offset 0x06-0x07**, dejando la red operativa al 100%.
3. **Editor DMI / SMBIOS (UUID y Número de Serie):**
   - Localiza automáticamente las estructuras DMI en el volcado para clonar el número de serie de la laptop y el UUID de 16 bytes (GUID RFC 4122) preservando garantías y licencias corporativas.
4. **Extractor de Licencia Digital Windows OEM (MSDM / ACPI):**
   - Escanea la firma de la tabla ACPI `MSDM` (`0x4D 0x53 0x44 0x4D`).
   - Extrae en pantalla limpia la clave original de 25 caracteres de Windows (`XXXXX-XXXXX-XXXXX-XXXXX-XXXXX`) con botón para copiar al portapapeles con un toque.
5. **Acción Directa «Guardar y Grabar»:**
   - Permite aplicar los parches y disparar inmediatamente el flasheo al chip SPI conectado (`flashrom -w`) en un solo flujo sin salir del visor.

---

### 🔷 PILAR 3: Controles de Hardware SPI y Registros de Estado

1. **Editor Visual de Registros de Estado (SR1, SR2, SR3):**
   - Muchas memorias SPI fallan al borrar (`ERASE FAILED`) porque tienen activados los bits de protección contra escritura por hardware o software (`BP0`, `BP1`, `BP2`, `BP3`, `SEC`, `TB`, `SRP0`).
   - La suite Pro ofrece un panel gráfico con el estado de cada bit y un botón de **«🔓 Desbloquear Chip en 1 Clic»** que ejecuta `--wp-disable` y borra los bits de protección automáticamente.
2. **Corrector de Quad Enable (Bit QE):**
   - Motherboards modernas que arrancan en modo Quad-SPI (x4 líneas de datos) no dan video si el bit `QE` del registro de estado SR2 se borra durante el flasheo. La app permite activar o verificar el bit `QE` con un clic.
3. **Flasheo Delta Selectivo de Sectores de 4KB (Ahorro del 90% de tiempo):**
   - Flashear y verificar un chip de 16MB o 32MB con CH341A toma entre 6 y 18 minutos.
   - En reparaciones de BIOS, usualmente solo cambia una región de 64KB (DMI, password o ME).
   - El flasheo selectivo compara el archivo nuevo contra el chip y **solo borra y graba los sectores de 4KB que difieren**, reduciendo el tiempo de flasheo de **15 minutos a menos de 10 segundos**.
4. **Turbo Baudrate Serprog a 2,000,000 de Baudios:**
   - La app actualmente opera Serprog a 115,200 baudios (~11 KB/s).
   - La suite Pro habilita preajustes de alta velocidad a **1,000,000** y **2,000,000 de baudios** (para Arduino UNO Optiboot, CH340G, CP2102 y RP2040), multiplicando la velocidad de lectura y escritura por más de **15x**.

---

### 🔷 PILAR 4: Inteligencia de Firmware y Bóveda de Seguridad

1. **Analizador de Salud de Intel ME / CSME ($FPT):**
   - El 80% de laptops que se apagan exactamente a los 30 minutos o cuyos ventiladores giran al 100% tienen la región Intel Management Engine dañada.
   - La app parsea la cabecera `$FPT`, mostrando la versión exacta de ME (ej. `11.8.92.4222`), SKU (Consumer vs Corporate) y el estado de los checksums de partición.
2. **Detector de Cápsula UEFI y Desencapsulador (Capsule Header Stripper):**
   - Alerta al usuario si el archivo descargado de la web del fabricante contiene una cabecera de actualización de cápsula (`_FVH`, cabeceras de 2KB o 4KB) que **arruinaría la placa** si se graba directamente. Ofrece recortar la cabecera para generar el archivo binario puro listo para el chip.
3. **Bóveda de Seguridad Antibrick (Pre-Flash Snapshot):**
   - Antes de ejecutar cualquier comando de escritura destructivo (`-w`), la app realiza una lectura previa automática ultrarrápida del chip actual y la guarda en una bóveda segura interna (`vault/backup_<chip>_<timestamp>.bin`).
   - Si la grabación fallase o el usuario cargó un archivo equivocado, cuenta con el botón de **«🚨 Rollback de Emergencia en 1 Clic»** para restaurar el chip a su estado previo sin dolor.

---

## 📂 5. Estructura de Clases Propuesta para la Implementación

```text
app/src/main/java/com/diamon/curso/
├── billing/
│   └── BillingManager.java          # Refactorizado: soporte dual remove_ads_pro / remove_ads + acknowledge
├── managers/
│   ├── ProPassManager.java          # Pase de 12 horas con triple respaldo (Prefs + Archivo firmado)
│   ├── ProAccessDialog.java         # Modal Pro estilizado con opción de Video y Compra
│   └── SafetyVaultManager.java      # Bóveda de seguridad antibrick y backups automáticos
├── binary/
│   ├── IntegrityManager.java        # Motor de hash streaming (MD5, SHA-1, SHA-256, CRC32)
│   ├── PatchTransaction.java        # Gestor transaccional de edición de celdas y Undo/Redo
│   ├── GbeMacPatcher.java           # Parser e inyector MAC Intel GbE con corrección de checksum 0xBABA
│   ├── DmiPatcher.java              # Extractor/inyector de UUID y serial DMI/SMBIOS
│   └── IntelMeParser.java           # Lector de cabecera $FPT y salud de Intel ME
├── diff/
│   ├── DiffHeatmapView.java         # Vista de cinta de calor con mapa de sectores 4KB
│   └── MappedDiffDataSource.java    # Comparador mmap zero-OOM para chips de 32MB y 64MB
├── ads/
│   ├── Publicidad.java              # Interfaz con AdsDisabledChecker y soporte Rewarded Video
│   └── MostrarPublicidad.java       # Manejador con supresión de anuncios y carga de videos AdMob
└── ui/activities/
    ├── MainActivity.java            # Menú Pro reactivo, botón en Toolbar y protección de acciones
    ├── HexViewerActivity.java       # Modo editor interactivo in-place y búsqueda de patrones
    └── HexDiffActivity.java         # Navegación rápida por sectores y soporte de 32MB/64MB
```

---

## 🚀 6. Conclusión y Próximos Pasos

Esta arquitectura eleva a **Flash SPI Tool** al nivel de una **suite móvil profesional de ingeniería inversa y reparación de placas**, manteniendo intacta la gratuidad y el aprecio de la comunidad técnica global.

* **Fase 1:** Incorporación de `ProPassManager.java`, actualización de `BillingManager.java` y `ProAccessDialog.java`.
* **Fase 2:** Implementación de la Suite de Integridad y Checksums (MD5, SHA-256, CRC32).
* **Fase 3:** Modo Edición en el Visor Hexadecimal, parcheador de MAC Intel GbE y Bóveda Antibrick.
* **Fase 4:** Controles visuales de Registros de Estado SPI (SR1/SR2/SR3) y turbo baudrate.
