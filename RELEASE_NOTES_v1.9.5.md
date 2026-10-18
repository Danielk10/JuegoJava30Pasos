# Flash SPI Tool v1.9.5 (versionCode 91) — Play Billing Resilience & Dependency Modernization

Esta versión (`v1.9.5`, código de versión `91`) refuerza la estabilidad de Google Play Billing frente a fallos `NullPointerException` en `ProxyBillingActivity` (tanto en dispositivos reales con desconexión temporal del servicio de Play Store como en entornos automatizados de Firebase Test Lab / Google Play Pre-launch Report) y actualiza las bibliotecas y plugins principales a sus últimas versiones estables.

---

## 🚀 Principales Novedades y Mejoras Técnicas

### 1. Blindaje Defensivo en Facturación (`BillingManager.java`)
• **Prevención de `NullPointerException` en `ProxyBillingActivity`:** Se validan estrictamente el ciclo de vida de la `Activity` (`!isFinishing() && !isDestroyed()`) y la disponibilidad activa de `billingClient.isReady()` (con reconexión automática si el servicio se desconectó) en ambas sobrecargas de `launchPurchaseFlow`.  
• **Detección de Entornos Automatizados (Firebase Test Lab):** Se identifica automáticamente la propiedad del sistema `firebase.test.lab` para omitir la invocación de `ProxyBillingActivity` en granjas de bots sin cuenta de Google Play activa.  
• **Protección Anti-Doble Toque (Debounce) y Captura de `RuntimeException`:** Se incorpora una ventana de protección de `2000 ms` reiniciada en `onPurchasesUpdated()` y bloques `try-catch (RuntimeException)` alrededor de `billingClient.launchBillingFlow(...)`.

### 2. Actualización del Ecosistema de Dependencias (`gradle/libs.versions.toml`)
• **Android Gradle Plugin (AGP):** Actualizado a `9.4.1`.  
• **Firebase BoM & Crashlytics:** Actualizados a `firebase-bom: 35.0.2` y `firebase-crashlytics: 21.0.0`.  
• **Google Mobile Ads (AdMob):** Actualizado a `play-services-ads: 25.5.0`.  
• **AndroidX UI:** Actualizados `appcompat: 1.8.0` y `constraintlayout: 2.2.2`.

### 3. Pruebas Unitarias Ampliadas (`BillingManagerTest.java`)
• Nuevas pruebas unitarias para validar el comportamiento defensivo cuando la actividad está finalizando, cuando `BillingClient` no está listo y cuando ocurre una excepción de tiempo de ejecución durante el inicio del flujo de compra.

---

## 🌐 Notas de Versión para Google Play (Bilingüe Obligatorio)

### 🇪🇸 Español (`es-419` / `es-ES`)
- Corrección de fallo en ProxyBillingActivity al iniciar compras con Play Store desconectado o en pruebas automatizadas.
- Protección anti-doble toque y reconexión automática en Google Play Billing.
- Actualización de dependencias principales (AGP 9.4.1, Firebase BoM 35.0.2, Crashlytics 21.0.0 y AdMob 25.5.0).
- Mejoras generales de estabilidad y rendimiento (API 24 - 37).

### 🇺🇸 English (`en-US`)
- Fixed ProxyBillingActivity crash when launching purchases with Play Store disconnected or in automated test environments.
- Added anti-double-tap debounce and automatic reconnection in Google Play Billing.
- Updated core dependencies (AGP 9.4.1, Firebase BoM 35.0.2, Crashlytics 21.0.0, and AdMob 25.5.0).
- General stability and performance improvements (API 24 - 37).

---

## 📦 Artefactos de la Versión

• **APK Release:** `Flash-SPI-Tool-v1.9.5-release.apk` (20 MB)  
  **SHA-256:** `6bac1f44f6ec1cf5bf1e2bd587540cde0609b764eb4ee4d6be84ab66ac4a135d`  
• **AAB Release (Google Play Bundle):** `Flash-SPI-Tool-v1.9.5.aab` (19 MB)  
  **SHA-256:** `8498033f1a451e9d549bdc03317588e8112d3f9224edc0c48edcbec3ef0d92ea`  
• **Firma Digital:** Certificado oficial RSA 2048-bit (`firma_flash_spi_tool.jks`)
