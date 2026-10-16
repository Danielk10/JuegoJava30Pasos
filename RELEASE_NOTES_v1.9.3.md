# Flash SPI Tool v1.9.3 (versionCode 89) — Direct Storage Export & Emulator Validation

Esta versión (v1.9.3, código de versión 89) introduce la capacidad de exportar directamente volcados modificados al almacenamiento público del teléfono desde el Visor Hexadecimal, exportación automática de archivos sidecar de integridad (`.sha256` y `.md5`) a la carpeta de descargas, incorporación de las guías técnicas del parche PTY y USB, y validación completa de la suite de emulación de silicio local.

---

## 🚀 Principales Novedades y Mejoras Técnicas

### 1. Exportación Directa al Almacenamiento del Teléfono (SAF)
• **Exportar desde el Diálogo de Guardar:** Al tocar `Guardar (X modificaciones)` en el Visor Hexadecimal, se presenta la opción `Exportar al Teléfono`, abriendo el selector de carpetas de Android (*Storage Access Framework*) para guardar en `Descargas`, `Documentos`, `Tarjeta SD` o `Google Drive`.  
• **Opción Dedicada en el Menú:** Nuevo ítem `📥 Exportar al Teléfono` accesible en cualquier momento desde el menú de opciones del visor.  
• **Exportación Automática de Sidecars:** Al generar sumas de comprobación en la herramienta de Integridad, los archivos sidecar `.sha256` y `.md5` se copian automáticamente a `Descargas/FlashromApp` para facilitar su auditoría en PC con `sha256sum -c`.

### 2. Guías Técnicas de Arquitectura Integradas
• **Guía Técnica Parche PTY USB (`GUIA_TECNICA_PARCHE_PTY_USB.md`):** Documentación completa del bug de Line Discipline en terminales Linux, solución con **Dummy Slave FD**, prevención de overrun en CH340 y arquitectura del motor en C++ multihilo (`usb_bridge.cpp`).  
• **Guía Técnica Parche Libusb (`GUIA_TECNICA_PARCHE_LIBUSB_ANDROID.md`):** Explicación del bypass de SELinux en Android sin root, inyección `ANDROID_USB_FD`, lecturas atómicas con `ioctl(USBDEVFS_CONTROL)` y preservación de FDs en Android 10+ vía JNI `fork()`/`execv()`.  
• **Especificación de Rendimiento (`PROPUESTAS_OPTIMIZACION_USB_VELOCIDAD.md`):** Estado 100% implementado del transporte dual (Socket TCP `127.0.0.1` vs PTY) y memoria virtual `mmap`.

### 3. Validación de Pruebas Unitarias y Suite de Emulación Local
• **Tests Unitarios:** Verificación de suite completa de pruebas unitarias (`./gradlew test`) con éxito.  
• **Validación de 4 Modos en Emulador Local (`emulador_flashrom`):** Sondeo y detección exitosa de memoria GigaDevice GD25Q80(B) bajo emuladores nativos de Serprog (PTY), Bus Pirate (PTY), SPIDriver (PTY a 460.800 bps) y CH341A (USB Socket).

---

## 🌐 Notas de Versión para Google Play (Bilingüe Obligatorio)

### 🇪🇸 Español (`es-419` / `es-ES`)
- Exportación directa de archivos ROM modificados al almacenamiento del teléfono.
- Exportación automática de sidecars .sha256 y .md5 a Descargas/FlashromApp.
- Pase Pro 12h con persistencia en Firebase Firestore y sincronización en la nube.
- Suite de pruebas de emulador local validada (Serprog, Bus Pirate, SPIDriver, CH341A).
- Mejoras de estabilidad y optimizaciones para Android 15/16.

### 🇺🇸 English (`en-US`)
- Direct export of modified ROM files to phone storage from Hex Viewer.
- Automatic export of .sha256 and .md5 sidecars to Downloads/FlashromApp.
- 12h Pro pass with Firebase Firestore cloud persistence and sync.
- Validated local emulator test suite (Serprog, Bus Pirate, SPIDriver, CH341A).
- Performance improvements and full compatibility with Android 15/16.

---

## 📦 Artefactos de la Versión

• **APK Release:** `Flash-SPI-Tool-v1.9.3-release.apk` (20 MB)  
  **SHA-256:** `bff086885026eac15ddafedb74e1c27776f3e6f8b5ea97eac84221cc518c0f87`  
• **AAB Release (Google Play Bundle):** `Flash-SPI-Tool-v1.9.3.aab` (19 MB)  
  **SHA-256:** `ee7bc6c62f57b8b28e01c2b7a4e57383348c3fdf8d786c9f201bc933972996ae`  
• **Firma Digital:** Certificado oficial RSA 2048-bit (`firma_flash_spi_tool.jks`)
