# Descripción para Google Play

## Descripción corta (80 caracteres max)

Programador USB de EEPROM/Flash desde Android con soporte profesional flashrom

## Descripción larga

🔧 **Flash EEPROM Tool** es la herramienta profesional para Android que te permite programar, respaldar y verificar chips de memoria flash y EEPROM directamente desde tu dispositivo móvil.

Convierte tu smartphone o tablet Android en una estación de trabajo portátil para reparación, desarrollo y mantenimiento de firmware. Ideal para técnicos electrónicos, desarrolladores embedded, entusiastas de hardware y profesionales de reparación.

### ⚡ Características principales

✅ **Detección automática de programadores USB**
   - Conexión OTG plug-and-play
   - Selector inteligente cuando múltiples dispositivos están conectados
   - Soporte nativo para más de 30 programadores

✅ **Operaciones completas de memoria**
   - 📖 Lectura y respaldo automático (backup)
   - ✏️ Escritura de firmware con verificación
   - 🔍 Verificación contra archivo de referencia
   - 🆔 Identificación automática de chip

✅ **Interfaz técnica profesional**
   - Consola de salida en tiempo real
   - Modo comando manual para diagnóstico avanzado
   - Importar/exportar archivos binarios vía SAF
   - Selector de programador con presets y modo manual

✅ **Motor flashrom nativo ARM64**
   - Compilado nativamente para máximo rendimiento
   - Integración completa con libusb parcheado para Android
   - Soporte de buses SPI e I2C

### 🔌 Programadores soportados

**Interfaces SPI:** ch341a_spi, ch347_spi, ft2232_spi, buspirate_spi, serprog, dediprog, digilent_spi, dirtyjtag_spi, jlink_spi, linux_spi, pickit2_spi, raiden_debug_spi, spidriver, stlinkv3_spi, usbblaster_spi

**Interfaces I2C:** mediatek_i2c_spi, mstarddc_spi, realtek_mst_i2c_spi

**Hardware especializado:** asm106x, atavia, developerbox_spi, drkaiser, gfxnvidia, internal, it8212, linux_mtd, nicintel, nicintel_eeprom, nicintel_spi, nv_sma_spi, ogp_spi, parade_lspcon, pony_spi, satasii

**Modo testing:** dummy (simulación sin hardware)

### 📋 Requisitos

- Android 6.0 o superior (API 23-36)
- Dispositivo ARM64 (arm64-v8a)
- Cable USB OTG
- Programador compatible con flashrom

### 🎯 Casos de uso

- Respaldo y restauración de BIOS
- Programación de chips SPI Flash (W25Q, MX25L, AT25, etc.)
- Lectura/escritura de EEPROM I2C
- Desarrollo y testing de firmware
- Reparación de equipos electrónicos
- Modificación de firmware en placas base
- Recuperación de dispositivos brick

### 🔐 Privacidad y seguridad

- Sin permisos invasivos
- Sin recopilación de datos personales
- Procesamiento local de todos los archivos
- Código fuente disponible en GitHub
- Licencia GNU GPL v3.0

### 📚 Documentación completa

Visita el repositorio oficial para:
- Guías de compilación
- Lista completa de dependencias
- Documentación técnica detallada
- Ejemplos de uso avanzado

### ⚠️ Advertencia técnica

Esta aplicación está diseñada para usuarios con conocimientos técnicos en electrónica y firmware. El mal uso puede resultar en daño permanente al hardware. Siempre realiza respaldos antes de escribir.

---

**Desarrollador:** Daniel Diamon  
**Licencia:** GNU GPL v3.0  
**Soporte:** danielpdiamon@gmail.com
