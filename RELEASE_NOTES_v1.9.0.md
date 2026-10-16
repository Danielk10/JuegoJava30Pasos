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
- Suite Flash SPI Tool Pro con herramientas avanzadas para técnicos y reparadores.
- Pase Pro Libre de 12 horas mediante video bonificado y compra permanente in-app.
- Bóveda de seguridad antibrick con respaldo automático pre-flash y rollback en 1 clic.
- Verificador criptográfico de integridad (MD5, SHA-256, CRC32) y cotejo con portapapeles.
- Editor hexadecimal en vivo con historial deshacer/rehacer y resaltado de cambios.
- Inyector de dirección MAC Intel GbE con corrección automática de checksum 0xBABA.
- Extractor de licencia digital OEM Windows desde tablas ACPI MSDM.
- Desbloqueo de registros de estado SPI (SR1/SR2/SR3) y activación de Quad Enable (QE).
- Modo turbo Serprog a 1M y 2M baudios para lecturas y grabaciones ultrarrápidas.
- Mapa de calor de sectores de 4KB en el comparador Hex Diff con soporte mmap.
- Integración de Firebase Crashlytics para máxima estabilidad.

### 🇺🇸 English (`en-US`)
- Flash SPI Tool Pro suite with advanced utilities for technicians and repairers.
- 12-Hour Free Pro Pass via rewarded video and lifetime in-app purchase.
- Antibrick safety vault with automatic pre-flash backup and 1-click emergency rollback.
- Cryptographic integrity verifier (MD5, SHA-256, CRC32) with clipboard hash match.
- Live in-place hex editor with undo/redo history and modified byte highlighting.
- Intel GbE MAC address injector with automatic 0xBABA NVM checksum recalculation.
- Windows OEM digital license product key extractor from ACPI MSDM tables.
- SPI status registers unlock (SR1/SR2/SR3) and Quad Enable (QE bit) toggle.
- Turbo Serprog baudrate presets at 1M and 2M baud for high-speed flashing.
- 4KB sector heatmap ribbon in Hex Diff viewer with zero-OOM mmap support.
- Firebase Crashlytics integration for highest crash-free stability.

---

## 📦 Artefactos de la Versión
• **APK Debug generado:** `/tmp/flashrom/outputs/apk/debug/app-debug.apk`
