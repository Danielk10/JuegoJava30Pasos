# Flash SPI Tool v1.9.2 (versionCode 88) — Firebase Firestore Cloud Sync & Enhanced Pro Persistence

Esta versión (v1.9.2, código de versión 88) añade persistencia en la nube para el Pase Pro Temporal de 12 Horas mediante **Firebase Firestore** (idéntica a `PIC-k150-Programing`), sincronización automática al reinstalar o limpiar datos, enlace prioritario para el producto permanente de Google Play, y actualización en caliente de la interfaz y anuncios.

---

## 🚀 Principales Novedades y Mejoras Técnicas

### 1. Pase Pro de 12 Horas con Persistencia Multinivel en Firebase Firestore
• **Sincronización Cloud con Firebase Firestore:** El pase de 12 horas obtenido tras ver un anuncio recompensado se respalda automáticamente en la colección `pro_passes` en Firestore bajo el `deviceId` de hardware.
• **Restauración Automática Anti-Desinstalación:** Si el usuario desinstala la aplicación, borra datos o cambia de versión, la app consulta Firestore en el arranque y restaura el tiempo restante sin necesidad de volver a ver publicidad.
• **Arquitectura de Respaldo Cuádruple:** SharedPreferences (0 ms) + Archivo firmado SHA-256 (`.flash_spi_pro_pass.dat`) + Firebase Firestore + Google Cloud Auto-Backup.

### 2. Integración y Priorización de Facturación Google Play
• **Priorización de Producto Pro:** Selección inmediata del producto `donacion_pizza` ("Flash SPI Tool Pro") desde Google Play Console como producto principal.
• **Reconocimiento Permanente de por Vida:** Reconocimiento no consumible con `acknowledgePurchase`, desbloqueando permanentemente la suite Pro sin anuncios.

### 3. Actualización Dinámica de la Interfaz
• **Refresco Inmediato:** Actualización automática del menú de opciones y ocultación inmediata de anuncios banner al confirmarse el pase temporal o la compra permanente.

---

## 🌐 Notas de Versión para Google Play (Bilingüe Obligatorio)

### 🇪🇸 Español (`es-419` / `es-ES`)
- Pase Pro 12 horas con sincronización en la nube y persistencia en Firebase Firestore.
- Restauración automática del pase Pro al reinstalar o limpiar datos.
- Priorización optimizada del producto Pro permanente de Google Play.
- Actualización en caliente de la interfaz y eliminación inmediata de anuncios.
- Mejoras de estabilidad general y compatibilidad con Android 15/16.

### 🇺🇸 English (`en-US`)
- 12-hour Pro pass with cloud sync and persistence via Firebase Firestore.
- Automatic Pro pass restoration upon app reinstall or clearing data.
- Streamlined priority for Google Play permanent Pro in-app purchase.
- Live UI state refresh and instant ad removal on pass activation.
- Overall stability and compatibility improvements for Android 15/16.

---

## 📦 Artefactos de la Versión
• **APK Release:** `Flash-SPI-Tool-v1.9.2-release.apk` (20 MB)  
  *SHA-256:* `92edb194f70ad87e9f28eaa9b38844b29f0b66bf2ba0eb7f6dd0e8f37afd3aa0`  
• **AAB Release (Google Play Bundle):** `Flash-SPI-Tool-v1.9.2.aab` (19 MB)  
  *SHA-256:* `b623d07ce70d9c54c805bfeed208dce0ca6996f5b965633a5c941760fe8e0847`  
• **Firma Digital:** Certificado oficial RSA 2048-bit (`firma_flash_spi_tool.jks`)
