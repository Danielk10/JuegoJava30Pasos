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
- update-pciids -> libupdate-pciids.so: OK
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
- Configuración manual de `-p` desde menú (`Configurar programador flashrom`): OK
- Ejecución nativa sigue usando `flashrom -p <valor_usuario>`: OK
