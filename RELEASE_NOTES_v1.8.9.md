# Flash SPI Tool v1.8.9 (versionCode 85)

Esta versión (v1.8.9, código de versión 85) corrige el cierre inesperado al intentar conectar en dispositivos o estados donde el servicio de USB Host no está disponible en Android (como en restricciones de hardware, modo seguro o políticas de fabricante), garantizando una detección segura y el registro claro de avisos en la aplicación.

---

## 🚀 Principales Novedades y Mejoras Técnicas

### 1. Manejo Seguro de UsbManager (Fix NPE en UsbController)
• **Prevención de excepciones fatales**: Se incorporó la comprobación ante valor nulo de `UsbManager` en `searchAndRequestProgrammer`, `requestUsbPermission` y `connectToDevice`.
• **Detección y diagnóstico no destructivo**: Si el dispositivo no cuenta con el servicio USB Host disponible, la aplicación informa de manera limpia en el log interactivo sin cerrarse inesperadamente.
• **Método de comprobación de compatibilidad**: Se implementó `isUsbHostSupported()` en `UsbController` para consultar el estado del hardware USB en cualquier momento.

### 2. Soporte Bilingüe Completo sin Cadenas Hardcodeadas
• **Internacionalización (i18n)**: Todos los mensajes de aviso y diagnóstico ante ausencia del servicio USB Host utilizan recursos del sistema (`str_err_usb_host_not_supported`) tanto en español (`res/values-es/strings.xml`) como en inglés (`res/values/strings.xml`).

### 3. Validación Exhaustiva en Hardware Emulado
• Verificado y contrastado al 100% mediante el banco de pruebas local del emulador de memorias SPI GD25Q80 (1 MB) en todos los protocolos (`serprog`, `buspirate_spi`, `spidriver` y `ch341a_spi`).
• Pruebas unitarias de JUnit ejecutadas y aprobadas.

---

## 🌐 Notas de Versión / Release Notes

### 🇪🇸 Español (`es-419` / `es-ES`)
• Corrección de cierre inesperado al conectar en dispositivos sin servicio USB Host.
• Detección segura y aviso en registro si el subsistema USB no está disponible.
• Validación completa en banco de pruebas de hardware SPI y protocolos serie.
• Mejoras de estabilidad y robustez en la gestión de periféricos USB.

### 🇺🇸 English (`en-US`)
• Fixed unexpected crash on connect on devices without USB Host service.
• Safe detection and log notification if the USB subsystem is unavailable.
• Full validation on SPI hardware testbench and serial protocols.
• Stability and robustness improvements in USB peripheral management.

---

## 📦 Artefactos de la Versión
• `app-release.aab`: Android App Bundle oficial firmado para Google Play Store.
• `app-release.apk`: Paquete APK universal compilado y firmado para instalación directa en dispositivos físicos.
