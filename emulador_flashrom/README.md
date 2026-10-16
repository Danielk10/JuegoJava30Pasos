# Emulador de Flashrom (GD25Q80) — Flash SPI Tool v1.9.0

Entorno de simulación de hardware y silicio para desarrollo, pruebas unitarias y validación local de **Flash SPI Tool v1.9.0** sin necesidad de conectar programadores físicos o chips reales.

El emulador implementa un modelo de silicio en C++ para el chip **GigaDevice GD25Q80** (1 Megabyte / 1024 KB) junto con la emulación de protocolo para los cuatro tipos de programadores compatibles con la aplicación.

---

## 🛠️ Programadores Virtuales Soportados

1. **CH341A (Modo USB Directo)**:
   - Simula un programador USB conectándose a través de un par de sockets UNIX.
   - Utiliza inyección de descriptor mediante la variable de entorno `ANDROID_USB_FD` y la librería `libusb` modificada.
   - Procesa flujos `UIO_STREAM` (pines CS) y `SPI_STREAM` (datos MOSI/MISO) con encuadre LSB de 4 bytes para evitar desalineación.

2. **Serprog (RP2040 / Arduino / STM32)**:
   - Crea un pseudo-terminal maestro/esclavo PTY virtual (`./serprog_pty`).
   - Implementa el protocolo binario `serprog` completo (SYNCNOP, Version, CmdMap, BufSize, BusType, SPI Operation).
   - Compatible con velocidades estándar y altas velocidades de comunicación (1.000.000 y 2.000.000 baudios) con respuesta de sincronización de beacon `0xAA 0x55`.

3. **Bus Pirate (v3 / v4)**:
   - Expone un PTY virtual en `./buspirate_pty`.
   - Simula la secuencia de inicialización de texto, entrada a modo binario `BBIO` y transición a modo SPI (`0x01`), respondiendo con la firma `Bus Pirate v3a...`.

4. **SPIDriver**:
   - Expone un PTY virtual en `./spidriver_pty`.
   - Simula el protocolo híbrido binario/ASCII a 460.800 baudios con comando de estado `?`, comprobación de eco `e <byte>`, Chip Select `s`/`u` y bloques de transferencia SPI de 1 a 64 bytes.

---

## 🚀 Compilación y Ejecución

### Compilar el binario:
```bash
make clean && make
```
Esto genera el ejecutable `emulador_flashrom`.

### Comandos de prueba rápida:

```bash
# Ver ayuda de opciones disponibles
./emulador_flashrom --help

# Modo CH341A autónomo con flashrom:
./emulador_flashrom --ch341a ./flashrom_local.sh -p ch341a_spi -r backup.bin

# Modo Serprog en segundo plano:
./emulador_flashrom --serprog &
./flashrom_local.sh -p serprog:dev=./serprog_pty:115200 -r backup.bin
kill %1

# Modo Bus Pirate en segundo plano:
./emulador_flashrom --buspirate &
./flashrom_local.sh -p buspirate_spi:dev=./buspirate_pty -r backup.bin
kill %1

# Modo SPIDriver en segundo plano:
./emulador_flashrom --spidriver &
./flashrom_local.sh -p spidriver:dev=./spidriver_pty -r backup.bin
kill %1
```

---

## 🔐 Validación de Volcados e Integridad (MD5 / SHA-256 & Hex Diff)

Los archivos `.bin` generados durante las pruebas con el emulador pueden auditarse de inmediato:
- **Verificación Criptográfica:** Mediante `md5sum backup.bin` y `sha256sum backup.bin` en terminal o con la opción **"🔒 Verificar Integridad MD5 / SHA-256"** de la app.
- **Comparador Hex Diff con Hashes MD5:** Comparación side-by-side de archivos en **"Comparar Archivos Hex (Diff)"**, mostrando sumas MD5, discrepancias byte a byte y mapa de calor visual por sectores de 4 KB.
- **Suite Pro:** Los volcados son compatibles con las herramientas de la suite Pro (Bóveda Antibrick, Editor Hexadecimal en Vivo con parcheo in-place, inyector MAC Intel GbE `0xBABA`, extractor Windows MSDM y registros SPI), disponibles con el Pase Libre de 12 horas o compra permanente con **"Sin anuncios de por vida"**.

---

## 🔄 Flujo de Desarrollo

- **Repositorio (`Flash-EEPROM-Tool/emulador_flashrom/`)**: Fuente de verdad con control de versiones. Todas las modificaciones deben registrarse aquí.
- **Directorio Local de Ejecución (`~/emulador_flashrom/`)**: Utilizado para compilar binarios locales y ejecutar pruebas de integración con `flashrom_local.sh`.
- Para documentación exhaustiva sobre arquitectura, parcheo de `libusb` y simulación JEDEC, consulta [`GUIA_USO_EMULADOR.md`](../GUIA_USO_EMULADOR.md).
