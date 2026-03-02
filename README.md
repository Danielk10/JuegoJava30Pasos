# Flash EEPROM Tool

[![Android](https://img.shields.io/badge/Android-6.0%20(API%2023)%20a%20Android%2016%20(API%2036)-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![ABI](https://img.shields.io/badge/ABI-arm64--v8a-0091EA?logo=arm&logoColor=white)](https://developer.android.com/ndk/guides/abis)
[![NDK](https://img.shields.io/badge/NDK-r29-4CAF50?logo=android&logoColor=white)](https://developer.android.com/ndk)
[![Flashrom](https://img.shields.io/badge/flashrom-integrado-orange)](https://github.com/flashrom/flashrom)
[![Licencia](https://img.shields.io/badge/Licencia-GPLv3-blue)](./LICENSE.txt)

Aplicación Android para lectura/escritura/verificación de memorias **SPI e I2C** usando **flashrom** y librerías nativas compiladas para **ARM64**.

> Nombre visible de la app: **Flash EEPROM Tool**.

> Rango de soporte Android: **API 23 a API 36** (Android 6.0 a Android 16).

---

## 1) Objetivo del proyecto

Este proyecto empaqueta una cadena nativa completa (flashrom + dependencias) dentro de una app Android y la conecta con la capa Java/Kotlin para:

- Detectar programadores USB de forma práctica (no limitado a un único modelo).
- Solicitar permisos USB de Android correctamente.
- Reutilizar un **File Descriptor (FD)** autorizado por Android dentro de una `libusb` parcheada.
- Ejecutar binarios nativos en rutas de runtime equivalentes a las rutas de compilación.

---

## 2) Arquitectura general

### Capa Java (Android)

- Enumera dispositivos USB con `UsbManager.getDeviceList()`.
- Si hay múltiples dispositivos, muestra selector de dispositivo en UI (VID:PID + fabricante/producto).
- Abre el dispositivo elegido con `UsbManager.openDevice(...)`.
- Obtiene `fd = connection.getFileDescriptor()`.
- Inyecta `ANDROID_USB_FD` al proceso nativo (`ProcessBuilder`), para que `libusb` parcheada lo use.

### Capa nativa / runtime

- Binarios y `.so` en `app/src/main/jniLibs/arm64-v8a/`.
- Runtime operativo en `files/usr` del sandbox Android.
- Enlaces simbólicos (o copia fallback) para replicar rutas exactas esperadas por binarios y sonames.

---

## 3) Rutas exactas (clave para que funcione)

### Ruta base de compilación/documentación

```text
data/data/com.diamon.curso/files/usr
```

### En el proyecto (assets empaquetados)

```text
app/src/main/assets/data/data/com.diamon.curso/files/usr
```

### En tiempo de ejecución (teléfono)

```text
/data/user/0/com.diamon.curso/files/usr
```

El código copia assets de datos (share/include/pkgconfig, etc.) al runtime y crea enlaces para binarios/librerías desde `nativeLibraryDir` a `files/usr/*`, manteniendo compatibilidad de rutas y resolución de dependencias.

---

## 4) Binarios y dependencias nativas

### Binarios principales (en jniLibs, formato Android)

- `libflashrom_bin.so`  → runtime `usr/sbin/flashrom`
- `libsetpci.so`        → runtime `usr/sbin/setpci`
- `libpcilmr.so`        → runtime `usr/sbin/pcilmr`
- `libupdate-pciids.so` → runtime `usr/sbin/update-pciids`
- `liblspci.so`         → runtime `usr/bin/lspci`
- `libftdi_eeprom.so`   → runtime `usr/bin/ftdi_eeprom`

### Librerías base enlazadas

- `libflashrom.so(.1*)`
- `libpci.so(.3*)`
- `libftdi1.so(.2*)`
- `libftdipp1.so(.3*)`
- `libusb-1.0.so`
- `libjaylink.so`
- `libcrypto.so.3`
- `libssl.so.3`

### Resolución de nombres para Android (copia garantizada)

Android solo garantiza la copia directa de nombres terminados en `.so`. Por eso el proyecto mantiene binarios renombrados en `jniLibs` y crea enlaces runtime con el soname esperado:

- `libcrypto.so.3` runtime -> `libcrypto_3.so` en `jniLibs`
- `libssl.so.3` runtime -> `libssl_3.so` en `jniLibs`
- `libz.so.1` runtime -> `libz_1.so` en `jniLibs`

Las variantes con versión original (`*.so.N`) se pueden conservar como respaldo, pero el flujo principal usa los nombres Android-compatibles.

---

## 5) Parche de `libusb` para Android (fuente)

Archivo: `patch_libusb.py` (Python 3).

El parche inserta dos comportamientos clave en `core.c` de libusb:

1. **`libusb_get_device_list`**
   - Si existe `ANDROID_USB_FD`, crea una lista mínima y permite continuar flujo sin escaneo clásico.

2. **`libusb_open`**
   - Si existe `ANDROID_USB_FD`, llama:
   - `libusb_wrap_sys_device(ctx, (intptr_t)fd, dev_handle)`

Con esto, flashrom/libusb usan el descriptor otorgado por Android en Java, evitando problemas del modelo de permisos USB de Android.

---

## 6) Detección de dispositivos y soporte

La app está orientada a trabajar con el ecosistema soportado por flashrom (programadores/chips según build y drivers disponibles), incluyendo flujos SPI/I2C cuando el hardware/driver de flashrom lo soporte.

La detección USB en Java usa la API nativa de Android (`UsbManager`) y permite elegir dispositivo cuando hay más de uno conectado. Además, desde el menú se puede configurar el valor de programador `-p` con dos modos: lista de soportados y modo manual legacy (texto libre). También existe una caja de comando manual en la UI principal para ejecutar parámetros directos de flashrom y ver resultados en el log.

### Programadores soportados por esta build (logs Meson/flashrom)

- asm106x
- atavia
- buspirate_spi
- ch341a_spi
- ch347_spi
- dediprog
- developerbox_spi
- digilent_spi
- dirtyjtag_spi
- drkaiser
- dummy
- ft2232_spi
- gfxnvidia
- internal
- it8212
- jlink_spi
- linux_mtd
- linux_spi
- parade_lspcon
- mediatek_i2c_spi
- mstarddc_spi
- nicintel
- nicintel_eeprom
- nicintel_spi
- nv_sma_spi
- ogp_spi
- pickit2_spi
- pony_spi
- raiden_debug_spi
- realtek_mst_i2c_spi
- satasii
- serprog
- spidriver
- stlinkv3_spi
- usbblaster_spi

### Programadores no soportados en la plataforma reportada

- atahpt
- atapromise
- ni845x_spi
- nic3com
- nicnatsemi
- nicrealtek
- rayer_spi
- satamv

> Nota: la app no bloquea programadores manuales. Si un comando falla, comparte el log para depuración.

Flujo recomendado:

1. Java enumera dispositivos USB con `UsbManager`.
2. Si hay varios, el usuario selecciona cuál usar.
3. Android concede permiso y se obtiene FD.
4. Se exporta `ANDROID_USB_FD` al proceso nativo.
5. flashrom opera con libusb parcheada.

---

## 7) Compilación y empaquetado (referencia)

### Scripts/archivos clave

- `setup-sdk.sh`: prepara SDK/NDK y `local.properties`.
- `linesCorrectos.txt`: comandos de compilación de dependencias nativas y empaquetado.
- `REPORTE_DEPENDENCIAS_BINARIOS.md`: DT_NEEDED por binario/librería.
- `AUDITORIA_ANDROID_RUNTIME.md`: checklist de runtime/rutas/dependencias.

### Build de app

```bash
bash ./setup-sdk.sh
./gradlew assembleDebug
```

---

## 8) Uso básico de la app

1. Conecta el programador USB OTG.
2. Pulsa **Detectar y Conectar Automáticamente** — la app reconoce automáticamente CH341A, FT2232, ST-LINK, Dediprog, J-Link, Bus Pirate y otros por VID:PID. Si hay múltiples dispositivos, elige en el selector.
3. Usa los botones principales:
   - **Identificar Chip** (`flashrom -p <programador>`)
   - **Leer Backup** (`-r bios.bin`)
   - **Verificar ROM** (`-v bios.bin`)
   - **Flashear ROM** (`-w bios.bin`)
   - **Borrar Chip** (`--erase`) — con diálogo de confirmación
   - **Borrar ROM** — elimina archivos temporales (bios.bin, read_test.bin, bios_test.bin)
4. **Barra de progreso en tiempo real**: durante operaciones de lectura/escritura/verificación/borrado, una barra inline muestra el porcentaje parseado del stdout de flashrom.
5. **Consola de comandos**: ejecuta cualquier comando flashrom directamente (ej. `--version`, `-L`, `--help`, `-p ch341a_spi -r bios.bin --layout layout.txt`, offsets parciales, etc.).
6. Importa/exporta archivos ROM desde/hacia almacenamiento usando SAF.

El panel de log muestra salida nativa real con prefijo `[native]`, comandos solicitados y estados de entorno/rutas.

---

## 9) Dependencias y licencias

## Proyecto

- **Flash EEPROM Tool**
- Licencia: **GNU GPL v3.0**
- Archivo: [`LICENSE.txt`](./LICENSE.txt)

## Dependencias principales

- **flashrom** — GPL-2.0+  
  https://github.com/flashrom/flashrom
- **libusb** — LGPL-2.1+  
  https://github.com/libusb/libusb
- **pciutils** — GPL-2.0+  
  https://github.com/pciutils/pciutils
- **libftdi** — LGPL-2.1+  
  https://developer.intra2net.com/git/libftdi
- **libjaylink** — GPL-2.0+  
  https://gitlab.zapb.de/libjaylink/libjaylink
- **OpenSSL (`libcrypto`/`libssl`)** — Apache License 2.0  
  https://www.openssl.org/
- **usb-serial-for-android** — MIT+  
  https://github.com/mik3y/usb-serial-for-android

> Revisa siempre obligaciones de distribución de binarios y código fuente según cada licencia.

---

## 10) Notas operativas importantes

- La app y los binarios están preparados para **ARM64 (`arm64-v8a`)**.
- Java usa `UsbManager` nativo para enumeración/selección/permisos/FD, y la operación del bus USB la realiza la capa nativa (flashrom/libusb parcheada).
- La selección de backend de flashrom (`-p`) se configura en la app y puede adaptarse a distintos programadores soportados por flashrom.
- Si agregas nuevos binarios/librerías en `jniLibs`, respeta sonames y rutas runtime esperadas para mantener compatibilidad.

---

## 11) Características de Usuario

### Barra de Progreso en Tiempo Real
- Barra delgada (4dp) integrada en el header del terminal — no consume espacio extra.
- Parsea el porcentaje del stdout de flashrom (ej: `Reading flash... 50%`) y muestra progreso visual.
- Cambia de color automáticamente: amarillo (en progreso), verde (completado), rojo (error).
- Se oculta automáticamente 3 segundos después de finalizar la operación.

### Borrado de Chip
- Botón **Borrar Chip** (`--erase`) con diálogo de confirmación de seguridad.
- Funciona tanto en modo real (hardware USB) como en modo dummy (emulación).

### Modo Dummy y Diagnóstico
La app incluye soporte nativo y un flujo dedicado para el programador `dummy` de flashrom.
- Permite probar la interfaz y comandos sin hardware real conectado.
- Cuenta con un menú **Modo Prueba (Dummy)** para generar chips virtuales (SST25VF040, MX25L6436, etc.) en un archivo temporal.
- Los botones principales adaptan su comando automáticamente si el programador dummy está seleccionado.

### Visor Hexadecimal Integrado
Cuenta con un visor hexadecimal profesional (`HexViewerActivity`) capaz de:
- Mostrar volcados de datos leídos del chip.
- Decodificar e inspeccionar archivos Intel HEX (analizando direcciones reales, soportando Extended Address Types 02/04).
- Mostrar siempre el *Origen* de los datos (ej: `Leído del chip (ch341a_spi)` o `Importado: firmware.hex`).
- Protección contra OOM para archivos grandes (límite de 32 MB para visualización).

### Comparador HEX (Diff)
Herramienta de comparación binaria (`HexDiffActivity`) accesible desde el menú:
- Compara dos archivos byte a byte con vista hexadecimal.
- Resalta diferencias en rojo con notación `A→B` (valor original → valor nuevo).
- Muestra estadísticas: cantidad y porcentaje de bytes diferentes.
- Pre-carga automáticamente `bios.bin` como archivo A si existe.

### Pinouts de Hardware
Guía de referencia rápida accesible desde el menú con diagramas ASCII:
- **CH341A Mini Programmer** — pinout del header SPI y configuración de jumpers.
- **Clip SOIC8/DIP8** — pinout del chip flash y conexión al CH341A.
- **Interfaz SPI** — diagrama de conexión Master-Slave.
- **Interfaz I2C** — diagrama de bus con pull-ups y pinout de EEPROM.

### Consola de Comandos Avanzada
- Ejecuta cualquier comando flashrom desde la interfaz (ej: `-p ch341a_spi -r bios.bin --layout layout.txt`).
- Soporta operaciones parciales con offsets nativos de flashrom.
- Funciona sin USB para comandos informativos (`--version`, `-L`, `--help`).
- Inyecta automáticamente `ANDROID_USB_FD` y `LD_LIBRARY_PATH` cuando hay USB conectado.

### Detección Automática de Programadores USB
La app reconoce automáticamente más de 25 modelos de programadores por VID:PID:
- CH341A, CH347, FT2232/FT232H/FT4232H, Bus Pirate, ST-LINK (v2/v2.1/v3), J-Link, Pickit2, USB-Blaster, Dediprog, Digilent, DirtyJTAG, Serprog (Arduino/CH340/CP2102).
- Selecciona automáticamente el parámetro `-p` correcto sin intervención del usuario.

### Importación y Exportación Inteligente
- **Cargar ROM**: usa el Android Storage Access Framework (SAF) para importar sin restricciones (`*/*`), detectando automáticamente el formato (binario crudo vs Intel HEX) y validando tamaños (hasta 128 MB). Incluye conversión Intel HEX → binario automática con validación de checksum.
- **Guardar ROM**: solo permite respaldar volcados legítimos (datos comprobados mediante chip read), asegurando que los usuarios no exporten accidentalmente el mismo archivo que importaron.
