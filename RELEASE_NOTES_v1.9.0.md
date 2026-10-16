# Flash SPI Tool v1.9.0 (versionCode 86) — Suite PRO & Fair Monetization

Esta versión mayor (v1.9.0, código de versión 86) introduce la arquitectura **Flash SPI Tool PRO** con monetización híbrida ética inspirada en `PIC-k150-Programing`, suite de integridad criptográfica, edición in-place en el visor hexadecimal, herramientas de firmware (MAC Intel GbE, ACPI MSDM, DMI, Intel ME), bóveda de seguridad antibrick pre-flash, controles de registros SPI y Firebase Crashlytics con soporte NDK.

---

## 🚀 Principales Novedades y Mejoras Técnicas

### 1. Monetización Híbrida Ética (Garantía 100% Gratuita Básica)
• **Operaciones de Silicio 100% Gratuitas**: Leer, escribir, borrar, verificar y detectar chips SPI continúa siendo completamente libre e ilimitado.
• **Pase Libre de 12 Horas**: Desbloqueo temporal completo de todas las herramientas avanzadas y supresión total de publicidad tras ver un anuncio recompensado de AdMob.
• **Triple Respaldo del Pase**: Persistencia en SharedPreferences y archivo firmado local con SHA-256 (`.flash_spi_pro_pass.dat`).
• **Compra In-App Permanente**: Google Play Billing 9.1.0 (`remove_ads_pro`) con acknowledge permanente y retrocompatibilidad con `donacion_pizza`.

### 2. Suite de Integridad y Checksums
• **Streaming Multialgoritmo (NIO)**: Cálculo de MD5, SHA-1, SHA-256 y CRC32 para volcados de 16MB a 64MB sin desbordamiento de memoria (Zero OOM).
• **Cotejo con Portapapeles**: Detección automática y comparación en tiempo real contra hashes copiados.
• **Sidecars GNU**: Exportación de archivos `.sha256` y `.md5` compatibles con `sha256sum -c`.

### 3. Editor Hexadecimal en Vivo y Modificación Quirúrgica
• **Edición In-Place con Historial**: Modificación directa de celdas con diálogo de desgloses (Hex, Dec, Bin, ASCII, Endian), resaltado ámbar (`#FFB300`) y pilas Undo/Redo (`PatchTransaction`).
• **Inyector MAC Intel GbE**: Asistente con recálculo automático del Checksum NVM `0xBABA`.
• **Extractor Windows OEM MSDM**: Detección y extracción de clave digital de 25 caracteres desde tablas ACPI.
• **Editor DMI/SMBIOS**: Extracción y clonación de número de serie y UUID GUID de 16 bytes.
• **Analizador Intel ME / CSME**: Decodificación de cabecera `$FPT`, versión de firmware y diagnóstico de integridad.

### 4. Bóveda Antibrick y Controles de Silicio SPI
• **Bóveda de Seguridad Pre-Flash**: Snapshot automático del chip antes de cualquier escritura destructiva (`-w`).
• **Rollback de Emergencia**: Restauración con 1 clic del último volcado funcional.
• **Desbloqueo de Registros SR1/SR2/SR3**: Desactivación con 1 clic de bits de protección contra escritura (`--wp-disable`).
• **Quad Enable (QE)**: Verificación y activación del bit QE en SR2.
• **Turbo Baudrate Serprog**: Modos de alta velocidad a 1,000,000 y 2,000,000 baudios.

### 5. Comparador Hex Diff con Heatmap Zero-OOM
• **Cinta de Calor (DiffHeatmapView)**: Mapeo de sectores de 4KB con navegación instantánea por diferencias.

### 6. Firebase Crashlytics & Credential Security
• **Crashlytics Java + NDK**: Diagnóstico y captura automática de fallos nativos C/C++ y Java.
• **Seguridad Git**: Exclusión estricta de credenciales en `.gitignore`.

---

## 🌐 Notas de Versión para Google Play (Bilingüe Obligatorio)

### 🇪🇸 Español (`es-419` / `es-ES`)
- Nueva suite Flash SPI Tool Pro con herramientas avanzadas para técnicos.
- Pase Pro gratis de 12h mediante video bonificado y compra in-app permanente.
- Bóveda de seguridad antibrick con respaldo automático y rollback en 1 clic.
- Editor hexadecimal en vivo con historial deshacer/rehacer y resaltado de cambios.
- Inyector MAC GbE con checksum 0xBABA y extractor de claves OEM Windows MSDM.
- Desbloqueo de registros SPI (SR1/2/3) y Quad Enable.
- Firebase Crashlytics integrado.

### 🇺🇸 English (`en-US`)
- New Flash SPI Tool Pro suite with advanced firmware repair utilities.
- 12-Hour Free Pro Pass via rewarded video and lifetime in-app purchase.
- Antibrick safety vault with automatic pre-flash backup and 1-click rollback.
- Live in-place hex editor with undo/redo history and modified byte highlighting.
- Intel GbE MAC injector with 0xBABA checksum and Windows OEM MSDM key extractor.
- SPI status registers unlock (SR1/2/3) and Quad Enable toggle.
- Firebase Crashlytics integrated.

---

## 📦 Artefactos de la Versión
• **APK Release:** `/tmp/flashrom/outputs/apk/release/app-release.apk` (18 MB)  
  *SHA-256:* `206b6762d0ab672404bf108064f0c2f239cb5e369a7ec5b0d9f8b95986d2caed`  
• **AAB Release (Google Play Bundle):** `/tmp/flashrom/outputs/bundle/release/app-release.aab` (17 MB)  
  *SHA-256:* `e36a9922ec53eb9ef32d9ab3b7676a3564b6f16f812a5db1339641b172f7bdea`  
• **APK Debug:** `/tmp/flashrom/outputs/apk/debug/app-debug.apk` (23 MB)  
• **Firma Digital:** Certificado oficial RSA 2048-bit (Esquema v2 verificado con `apksigner`)
