# Auditoría de runtime Android

## Replicación de rutas desde data/

- data/usr flashrom: OK
- data/usr setpci: OK
- data/usr pcilmr: OK
- data/usr update-pciids: OK
- data/usr lspci: OK
- data/usr ftdi_eeprom: OK

## Assets empaquetados

- Raíz exacta en assets: OK (`app/src/main/assets/data/data/com.diamon.curso/files/usr`)

## Binarios en jniLibs para enlaces simbólicos

- flashrom -> libflashrom_bin.so: OK
- setpci -> libsetpci.so: OK
- pcilmr -> libpcilmr.so: OK
- update-pciids: Es script bash extraído directo desde assets (N/A en jniLibs)
- lspci -> liblspci.so: OK
- ftdi_eeprom -> libftdi_eeprom.so: OK

## Dependencias críticas en jniLibs

- libconfuse.so: OK
- libc++_shared.so: OK
- libz_1.so (soname runtime: libz.so.1): OK
- libcrypto_3.so (soname runtime: libcrypto.so.3): OK
- libssl_3.so (soname runtime: libssl.so.3): OK

## Parche libusb

- ANDROID_USB_FD en get_device_list: OK
- libusb_wrap_sys_device en libusb_open: OK

## Flujo USB en Java/UI

- Enumeración USB con `UsbManager.getDeviceList()`: OK
- Selector de dispositivo cuando hay múltiples USB conectados: OK
- Advertencia amigable si el USB no es reconocido como programador (no bloqueante): OK
- Menú **Agregar dispositivo/programador flashrom (lista)** con programadores soportados: OK
- Menú **Configurar programador manual (anterior)** (texto libre legacy): OK
- Menú **Modo Prueba (Dummy)** para pruebas locales de chip virtual: OK
- Visor Hexadecimal avanzado (`HexViewerActivity`) con soporte Intel HEX y origen de datos: OK
- Campo de comando manual para ejecutar parámetros flashrom y ver salida en consola: OK
- Rastreo dinámico de archivos leídos (ej. usa `lastReadFile` en lugar de `bios.bin` rígido): OK
- Comandos manuales sin USB conectado (ej. `--version`, `-L`, `--help`): OK
- Ejecución nativa sigue usando `flashrom -p <valor_usuario>`: OK

## Programadores soportados por esta build (según logs)

asm106x, atavia, buspirate_spi, ch341a_spi, ch347_spi, dediprog, developerbox_spi, digilent_spi, dirtyjtag_spi, drkaiser, dummy, ft2232_spi, gfxnvidia, internal, it8212, jlink_spi, linux_mtd, linux_spi, parade_lspcon, mediatek_i2c_spi, mstarddc_spi, nicintel, nicintel_eeprom, nicintel_spi, nv_sma_spi, ogp_spi, pickit2_spi, pony_spi, raiden_debug_spi, realtek_mst_i2c_spi, satasii, serprog, spidriver, stlinkv3_spi, usbblaster_spi.

## Programadores no soportados en esta plataforma

atahpt, atapromise, ni845x_spi, nic3com, nicnatsemi, nicrealtek, rayer_spi, satamv.
