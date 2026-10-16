# Flash SPI Tool v1.9.4 (versionCode 90) — Full Compatibility (API 24 - 37) & Direct Storage Export

Esta versión (v1.9.4, código de versión 90) consolida la compatibilidad absoluta desde Android 7.0 (Nougat, API 24) hasta Android 17 (Developer Preview, API 37) con soporte para páginas de memoria ELF de 16 KB en ARM64, exportación directa al almacenamiento del teléfono vía Storage Access Framework (SAF), exportación automática de sidecars de integridad a la carpeta Descargas y guías técnicas de arquitectura PTY y Libusb.

---

## 🚀 Principales Novedades y Mejoras Técnicas

### 1. Compatibilidad Total Retroactiva (API 24 - API 37)
• **Fallback para Android 7.0 / 7.1 (API 24/25):** Corrección en el visor hexadecimal sustituyendo llamadas exclusivas de Java NIO (`Files.readAllBytes`, API 26+) por streams nativos `FileInputStream` + `ByteArrayOutputStream` en dispositivos con Android 7.x.  
• **Soporte de Páginas de 16 KB en Android 15/16/17:** Binarios nativos ARM64 (`libcurso.so`, `libflashrom_bin.so`) compilados con alineación de segmento ELF a `0x4000` (16 KB) mediante NDK r30.  
• **Target SDK 37:** Preparado para la próxima generación de dispositivos Android manteniendo compatibilidad completa hacia atrás.

### 2. Exportación Directa al Almacenamiento del Teléfono (SAF)
• **Exportar desde el Diálogo de Guardar:** Al tocar `Guardar (X modificaciones)` en el Visor Hexadecimal, se presenta la opción `Exportar al Teléfono`, abriendo el selector del sistema (*Storage Access Framework*) para guardar en `Descargas`, `Documentos`, `Tarjeta SD` o `Google Drive`.  
• **Opción Dedicada en el Menú:** Nuevo ítem `📥 Exportar al Teléfono` accesible en cualquier momento desde el menú de opciones del visor.  
• **Exportación Automática de Sidecars:** Al generar sumas de comprobación en la herramienta de Integridad, los archivos sidecar `.sha256` y `.md5` se copian automáticamente a `Descargas/FlashromApp` para auditoría externa en PC con `sha256sum -c`.

### 3. Guías Técnicas de Arquitectura Integradas
• **Guía Técnica Parche PTY USB (`GUIA_TECNICA_PARCHE_PTY_USB.md`):** Solución al bug de Line Discipline en terminales Linux, mecanismo **Dummy Slave FD**, prevención de overrun en CH340 y motor multihilo C++ (`usb_bridge.cpp`).  
• **Guía Técnica Parche Libusb (`GUIA_TECNICA_PARCHE_LIBUSB_ANDROID.md`):** Bypass de SELinux en Android sin root, inyección `ANDROID_USB_FD`, lecturas atómicas con `ioctl(USBDEVFS_CONTROL)` y preservación de FDs en Android 10+ vía JNI `fork()`/`execv()`.  
• **Especificación de Rendimiento (`PROPUESTAS_OPTIMIZACION_USB_VELOCIDAD.md`):** Transporte dual (Socket TCP `127.0.0.1` vs PTY) y optimizaciones de memoria virtual `mmap`.

### 4. Validación de Suite de Emulación Local
• **Tests Unitarios:** Suite completa de pruebas unitarias verificada exitosamente.  
• **Validación de 4 Modos en Emulador Local (`emulador_flashrom`):** Sondeo y detección verificada de memoria GigaDevice GD25Q80(B) bajo emuladores nativos de Serprog (PTY), Bus Pirate (PTY), SPIDriver (PTY a 460.800 bps) y CH341A (USB Socket).

---

## 🌐 Notas de Versión para Google Play (Bilingüe Obligatorio)

### 🇪🇸 Español (`es-419` / `es-ES`)
- Compatibilidad ampliada desde Android 7.0 hasta Android 17 (API 24-37).
- Exportación directa de archivos ROM modificados al almacenamiento del teléfono.
- Exportación automática de sidecars .sha256 y .md5 a Descargas/FlashromApp.
- Suite de pruebas de emulador local validada (Serprog, Bus Pirate, SPIDriver, CH341A).
- Soporte para páginas de memoria ELF de 16 KB y mejoras de estabilidad.

### 🇺🇸 English (`en-US`)
- Broadened compatibility from Android 7.0 up to Android 17 (API 24-37).
- Direct export of modified ROM files to phone storage from Hex Viewer.
- Automatic export of .sha256 and .md5 sidecars to Downloads/FlashromApp.
- Validated local emulator test suite (Serprog, Bus Pirate, SPIDriver, CH341A).
- 16 KB ELF memory page alignment and stability improvements.

---

## 📦 Artefactos de la Versión

• **APK Release:** `Flash-SPI-Tool-v1.9.4-release.apk` (20 MB)  
  **SHA-256:** `849efd99399b7ae60d397f55ba5011bc0c4e4128d0e864cc03e88dab45bccbc2`  
• **AAB Release (Google Play Bundle):** `Flash-SPI-Tool-v1.9.4.aab` (19 MB)  
  **SHA-256:** `4e5feac9a315a53d0ad7dd8c9c5b6e0d724da9bffccd89d86bef46bb13c7f605`  
• **Firma Digital:** Certificado oficial RSA 2048-bit (`firma_flash_spi_tool.jks`)
