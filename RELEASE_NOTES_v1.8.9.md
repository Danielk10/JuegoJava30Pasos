# Notas de Lanzamiento - Flash-EEPROM-Tool v1.8.9 (versionCode 84)

## 🌐 Notas para Google Play Console (Bilingüe)

### Español (`es-419` / `es-ES`)
- Corrección de cierre inesperado al conectar en dispositivos sin servicio USB Host.
- Detección segura y aviso claro si el subsistema USB no está disponible.
- Validaciones completas en emulador de hardware SPI y protocolos serie.
- Mejoras continuas de estabilidad y rendimiento en el motor flashrom.

### English (`en-US`)
- Fixed unexpected crash on connect on devices without USB Host service.
- Safe detection and clear notification if the USB subsystem is unavailable.
- Full validation on SPI hardware emulator and serial protocols.
- Continuous stability and performance improvements in flashrom engine.

---

## 🛠️ Detalle Técnico de Cambios

1. **Protección contra NullPointerException en UsbController (Issue #96)**:
   - En dispositivos donde `Context.USB_SERVICE` devuelve `null` (por ejemplo, entornos restringidos, ciertas tablets o teléfonos con restricciones de hardware o políticas de fabricante), la invocación a `usbManager.getDeviceList()` provocaba un crash `NullPointerException`.
   - Se incorporó la comprobación `if (usbManager == null)` en `searchAndRequestProgrammer`, `requestUsbPermission` y `connectToDevice`.
   - Se añadió el método público `isUsbHostSupported()` en `UsbController`.
   - Ante la falta del servicio USB Host, la app ya no crashea y registra el mensaje informativo localizado en el log de la aplicación.

2. **Sin Cadenas Hardcodeadas (i18n)**:
   - Se crearon los recursos bilingües `str_err_usb_host_not_supported` en `res/values/strings.xml` y `res/values-es/strings.xml`.

3. **Validación Exhaustiva**:
   - Pruebas unitarias de JUnit aprobadas exitosamente.
   - Banco de pruebas local en hardware emulado (`emulador_flashrom`) verificado al 100% en todos los modos (`serprog`, `buspirate_spi`, `spidriver` y `ch341a_spi`).
