# Flash SPI Tool v1.9.1 (versionCode 87) — Suite PRO & Fair Monetization

Esta versión (v1.9.1, código de versión 87) consolida la suite **Flash SPI Tool PRO** con monetización híbrida ética inspirada en `PIC-k150-Programing`, cotejo MD5 side-by-side en el comparador Hex Diff, detección estricta de diferencias en volcados de tamaños dispares (sin falsos idénticos por relleno `0xFF`), acceso directo a verificación de integridad MD5/SHA-256 desde el menú principal, descripciones limpias sin paréntesis y "Sin anuncios de por vida".

---

## 🚀 Principales Novedades y Mejoras Técnicas

### 1. Comparador Hex Diff con Detección Estricta y Hashes MD5
• **Detección Estricta en Tamaños Dispares**: Detección rigurosa byte a byte ante archivos de distinto tamaño (ej. 16MB vs 1MB con relleno `0xFF`), eliminando falsos positivos de archivos idénticos y reflejando las diferencias explícitas (`XX→--` y `--→XX`).
• **Cotejo Side-by-Side de Hashes MD5**: Integración de sumas de verificación MD5 y tamaños exactos en bytes calculados y presentados en paralelo para ambos archivos seleccionados.
• **Mapa de Calor Zero-OOM**: Visualización sectorial de 4KB con navegación instantánea hacia bloques con discrepancias.

### 2. Suite Flash SPI Tool PRO
• **Sin anuncios de por vida**: Formato limpio, directo y transparente idéntico a `PIC-k150-Programing`.
• **Acceso Directo a Integridad**: Nueva opción "Verificar Integridad MD5 / SHA-256" directamente en el menú principal con botones rápidos para copiar MD5 o SHA-256 y generar sidecars GNU (`.sha256` / `.md5`).
• **Descripciones Profesionales sin Paréntesis**: Títulos y descripciones técnicas optimizadas para máxima claridad visual.
• **Bóveda Antibrick Pre-Flash**: Respaldo automático de seguridad antes de cualquier escritura y botón de Rollback de Emergencia en 1 clic.
• **Herramientas Avanzadas de Firmware**: Inyector MAC Intel GbE con checksum `0xBABA`, extractor OEM Windows MSDM, modificador SMBIOS/DMI y analizador Intel ME.
• **Controles de Registros SPI**: Desbloqueo de bits de protección (SR1, SR2, SR3 con `--wp-disable`) y configuración Quad Enable (bit QE).

### 3. Depuración del Menú y Estabilidad Serie
• **Retiro Definitivo de Pizza**: Eliminado el elemento del menú, conservando la retrocompatibilidad transparente con compras previas.
• **Serprog a 115.200 Baudios Fijo y Oficial**: Comunicación serie transparente fijada a 115.200 baudios, eliminando selectores inestables y garantizando compatibilidad 100% con Arduino Uno/Nano (`frser-arduino`), RP2040 y microcontroladores estándar.
• **Sincronización Total de Cadenas**: 100% de simetría bilingüe (341 claves en español e inglés), sin cadenas hardcodeadas ni textos huérfanos.

---

## 🌐 Notas de Versión para Google Play (Bilingüe Obligatorio)

### 🇪🇸 Español (`es-419` / `es-ES`)
- Nueva suite Flash SPI Tool Pro sin anuncios de por vida y pase gratis de 12h.
- Comparador Hex Diff: detección estricta entre tamaños distintos y cotejo MD5.
- Acceso directo a Verificar Integridad MD5 / SHA-256 desde el menú principal.
- Bóveda de seguridad antibrick con respaldo pre-flash y rollback en 1 clic.
- Editor hexadecimal en vivo con historial deshacer/rehacer y parcheo in-place.
- Serprog optimizado a 115.200 baudios para máxima compatibilidad con Arduino.

### 🇺🇸 English (`en-US`)
- New Flash SPI Tool Pro suite: ad-free for life and 12-hour free pass.
- Hex Diff tool: strict diff detection on mismatched sizes and MD5 comparison.
- Direct access to Verify Integrity MD5 / SHA-256 from the main menu.
- Antibrick safety vault with automatic pre-flash backup and 1-click rollback.
- Live in-place hex editor with undo/redo history and byte highlighting.
- Serprog streamlined at 115,200 baud for 100% official Arduino compatibility.

---

## 📦 Artefactos de la Versión
• **APK Release:** `Flash-SPI-Tool-v1.9.1-release.apk` (18 MB)  
  *SHA-256:* `9f26796b190297804e16926afdf29a41a78eba7363034d5f04f59437cfbb43e1`  
• **AAB Release (Google Play Bundle):** `Flash-SPI-Tool-v1.9.1.aab` (17 MB)  
  *SHA-256:* `a5f77e6b8a0d8a0d9666212c7acc148cd8b8637c6d150c0020bc394018b42649`  
• **APK Debug:** `Flash-SPI-Tool-v1.9.1-debug.apk` (21 MB)  
  *SHA-256:* `d05865e92cb5939d4571c836551c2091e3549bf90a7625ab48062909346f1e30`  
• **Firma Digital:** Certificado oficial RSA 2048-bit (Esquema v2 verificado con `apksigner`)
