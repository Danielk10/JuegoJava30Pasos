# Flash EEPROM Tool

[![Android](https://img.shields.io/badge/Android-6.0%20(API%2023)%20a%20Android%2016%20(API%2036)-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![ABI](https://img.shields.io/badge/ABI-arm64--v8a-0091EA?logo=arm&logoColor=white)](https://developer.android.com/ndk/guides/abis)
[![NDK](https://img.shields.io/badge/NDK-r29-4CAF50?logo=android&logoColor=white)](https://developer.android.com/ndk)
[![Flashrom](https://img.shields.io/badge/flashrom-integrado-orange)](https://github.com/flashrom/flashrom)
[![Version](https://img.shields.io/badge/Versión-1.6.4%20(v56)-blue)](./app/build.gradle)
[![Licencia](https://img.shields.io/badge/Licencia-GPLv3-blue)](./LICENSE.txt)

Aplicación Android profesional para lectura/escritura/verificación de memorias **SPI e I2C** usando **flashrom** nativo. Diseñada para técnicos e ingenieros, capaz de manejar archivos de firmware de cualquier tamaño (512 MB, 1 GB+) con herramientas de inspección hexadecimal de alto rendimiento.

> Nombre visible de la app: **Flash EEPROM Tool**.
> Soporte de Archivos: **Sin límite de tamaño** para binarios (.bin, .rom, .img).

---

## 🚀 Novedades de la Versión 1.6.4 (Profesional)

- **Soporte de Archivos Gigantes (512MB+):** Refactorización completa del sistema de importación/exportación. Ahora usa *streaming* directo a disco, permitiendo cargar y grabar archivos masivos de BIOS/Firmware (como los de consolas o servidores) sin riesgo de cierres por falta de memoria RAM.
- **Visor Hexadecimal con Acceso Aleatorio:** Implementación de `RandomAccessFile`. El visor ahora es instantáneo: puedes abrir un archivo de 1 GB y desplazarte por él sin esperas ni consumo excesivo de RAM.
- **Comparador HEX (Diff) Masivo:** Nuevo motor de comparación en segundo plano que analiza archivos gigantes buscando diferencias sin bloquear la interfaz de usuario.

---

## 1) Objetivo del proyecto

Este proyecto empaqueta una cadena nativa completa (flashrom + dependencias) dentro de una app Android y la conecta con la capa Java/Kotlin para:

- Detectar programadores USB de forma práctica (no limitado a un único modelo).
- Solicitar permisos USB de Android correctamente.
- Reutilizar un **File Descriptor (FD)** autorizado por Android dentro de una `libusb` parcheada.
- Ejecutar binarios nativos en rutas de runtime equivalentes a las rutas de compilación.

---

## 2) Arquitectura de Manejo de Datos

La app está optimizada para dispositivos móviles con memoria limitada, utilizando técnicas de servidores:

- **Streaming I/O:** Los archivos importados o leídos del chip se transmiten bloque a bloque (64KB) directamente al almacenamiento interno.
- **Lazy Loading (Lectura Diferida):** El visor hexadecimal y el comparador solo leen los 16 bytes necesarios para la fila visible en pantalla directamente desde el disco.
- **Procesamiento Nativo:** El motor `flashrom` opera en C puro, gestionando su propio buffer de memoria fuera del alcance del recolector de basura de Java.

---

## 3) Arquitectura USB

La app tiene **dos caminos** de comunicación USB según el tipo de programador:

```
                  ┌─────────────────┐
                  │  USB_AUTO_MAP   │
                  │  (18 VID:PID)   │
                  └───────┬─────────┘
                          │
              ┌───────────┴───────────┐
              │                       │
     Serial (PTY)              USB directo (libusb)
              │                       │
    ┌─────────┴─────────┐   ┌────────┴────────┐
    │  PtyBridge.java   │   │  ANDROID_USB_FD  │
    │  DTR/RTS + Beacon │   │  → patch_libusb  │
    │  USB↔PTY threads  │   │  → flashrom      │
    └─────────┬─────────┘   └────────┬────────┘
         /dev/pts/N              libusb nativo
              │                       │
          flashrom               flashrom
```

---

## 4) Binarios y dependencias nativas

### Binarios principales (en jniLibs, formato Android)

- `libflashrom_bin.so`  → runtime `usr/sbin/flashrom`
- `liblspci.so`         → runtime `usr/bin/lspci`
- `libftdi_eeprom.so`   → runtime `usr/bin/ftdi_eeprom`

### Librerías base enlazadas (Runtime usr/lib)

- `libflashrom.so`, `libpci.so`, `libftdi1.so`, `libusb-1.0.so`, `libjaylink.so`, `libcrypto.so.3`, `libssl.so.3`, `libz.so.1`.

---

## 5) Detección de dispositivos y auto-detección

La app detecta automáticamente el programador USB conectado usando un mapa de **18 VID:PID**:

| Familia | VID:PID | Programador | Interfaz |
|---------|---------|-------------|----------|
| CH341A | `1a86:5512`, `1a86:5523` | `ch341a_spi` | libusb |
| CH347 | `1a86:55db` | `ch347_spi` | libusb |
| FT2232/FT232H | `0403:6010`–`6015` | `ft2232_spi` | libusb |
| Bus Pirate | `0403:6001` | `buspirate_spi` | **PTY** |
| ST-Link | `0483:3748`–`3754` | `stlinkv3_spi` | libusb |
| Serprog | `2341:0043/0001`, `1a86:7523`, `10c4:ea60` | `serprog` | **PTY** |

---

## 6) Características Principales

### Visor Hexadecimal Profesional
- **Instantáneo:** Abre archivos de 512 MB o más en milisegundos.
- **Bajo Consumo:** No carga el archivo en RAM; usa `RandomAccessFile`.
- **Intel HEX:** Decodifica y mapea direcciones reales para archivos .hex de hasta 16 MB.

### Comparador HEX (Diff) Profesional
- **Escaneo Masivo:** Compara archivos gigantes buscando diferencias en segundo plano.
- **Visualización Clara:** Resalta cambios con notación `A→B` y estadísticas de porcentaje de diferencia.

### Pinouts de Hardware Integrados
Guía visual completa con diagramas para:
- CH341A (SPI Header & Jumpers).
- Clips SOIC8 / DIP8.
- Arduino UNO (serprog) → Conexión detallada.
- Bus Pirate, SPIDriver e interfaces genéricas SPI/I2C.

### Barra de Progreso Inteligente
- Parsea el *stdout* nativo en tiempo real.
- Indica porcentaje exacto de Lectura, Escritura y Verificación.

---

## 7) Dependencias y licencias

Este proyecto es Software Libre bajo la licencia **GNU GPL v3.0**.

### Componentes de Terceros:
- **flashrom** (GPL-2.0+), **libusb** (LGPL-2.1+), **pciutils** (GPL-2.0+), **libftdi** (LGPL-2.1+), **libjaylink** (GPL-2.0+), **OpenSSL** (Apache 2.0), **usb-serial-for-android** (MIT).

---

## 8) Notas para Desarrolladores

- **Arquitectura:** ARM64 (`arm64-v8a`) únicamente.
- **Compilación Nativa:** Las librerías deben estar parcheadas para aceptar `ANDROID_USB_FD` si usan libusb.
- **Entorno:** Los binarios esperan un *fake root* en `files/usr` para localizar `pci.ids` y librerías dinámicas.
