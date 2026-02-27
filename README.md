# Flash EEPROM Tool (Android ARM64)

[![Android](https://img.shields.io/badge/Android-12%2B-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![ABI](https://img.shields.io/badge/ABI-arm64--v8a-0091EA?logo=arm&logoColor=white)](https://developer.android.com/ndk/guides/abis)
[![NDK](https://img.shields.io/badge/NDK-r29-4CAF50?logo=android&logoColor=white)](https://developer.android.com/ndk)
[![Flashrom](https://img.shields.io/badge/flashrom-integrado-orange)](https://github.com/flashrom/flashrom)
[![Licencia](https://img.shields.io/badge/Licencia-GPLv3-blue)](./LICENSE.txt)

Aplicación Android para lectura/escritura/verificación de memorias SPI usando **flashrom** y librerías nativas compiladas para **ARM64**.

> Nombre visible de la app: **Flash EEPROM Tool**.

---

## 1) Objetivo del proyecto

Este proyecto empaqueta una cadena nativa completa (flashrom + dependencias) dentro de una app Android y la conecta con la capa Java/Kotlin para:

- Detectar programadores USB de forma práctica.
- Solicitar permisos USB de Android correctamente.
- Reutilizar un **File Descriptor (FD)** autorizado por Android dentro de una `libusb` parcheada.
- Ejecutar binarios nativos en rutas de runtime equivalentes a las rutas de compilación.

---

## 2) Arquitectura general

### Capa Java (Android)

- Descubre dispositivos con `usb-serial-for-android` (`UsbSerialProber`).
- Abre el dispositivo con `UsbManager.openDevice(...)`.
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

### Dependencias opcionales que quedan auto-listas

Si agregas estas librerías a `app/src/main/jniLibs/arm64-v8a/`, el runtime las enlaza automáticamente en el siguiente arranque:

- `libz.so.1`
- `libconfuse.so`
- `libc++_shared.so`

Si no están, se informa en log como faltantes.

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

La app está orientada a trabajar con el ecosistema soportado por flashrom (programadores/chips según build y drivers disponibles), y usa `usb-serial-for-android` para facilitar descubrimiento/permisos USB.

Flujo recomendado:

1. Prober Java encuentra dispositivo USB.
2. Android concede permiso y se obtiene FD.
3. Se exporta `ANDROID_USB_FD` al proceso nativo.
4. flashrom opera con libusb parcheada.

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
2. Pulsa **Conectar Programador** y concede permiso.
3. Usa:
   - **Probar Conexión** (`flashrom -p ch341a_spi`)
   - **Leer EEPROM** (`-r bios.bin`)
   - **Verificar BIOS** (`-v bios.bin`)
   - **Escribir EEPROM** (`-w bios.bin`)
4. Importa/exporta `bios.bin` desde/hacia almacenamiento usando SAF.

El panel de log muestra salida nativa real con prefijo `[native]` y estados de entorno/rutas.

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
- **usb-serial-for-android** — MIT  
  https://github.com/mik3y/usb-serial-for-android

> Revisa siempre obligaciones de distribución de binarios y código fuente según cada licencia.

---

## 10) Notas operativas importantes

- La app y los binarios están preparados para **ARM64 (`arm64-v8a`)**.
- Para evitar conflictos, Java se usa para prober/permisos/FD, y la operación del bus USB la realiza la capa nativa (flashrom/libusb parcheada).
- Si agregas nuevos binarios/librerías en `jniLibs`, respeta sonames y rutas runtime esperadas para mantener compatibilidad.

---

## 11) Estado de diagnóstico

- Verifica dependencias con: `REPORTE_DEPENDENCIAS_BINARIOS.md`.
- Verifica despliegue runtime con: `AUDITORIA_ANDROID_RUNTIME.md`.
- Verifica coherencia de compilación histórica con: `linesCorrectos.txt`.

