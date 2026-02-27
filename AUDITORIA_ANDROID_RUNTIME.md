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

## Dependencias reportadas faltantes en jniLibs

- libconfuse.so: FALTA
- libc++_shared.so: FALTA
- libz.so.1: FALTA

## Parche libusb

- ANDROID_USB_FD en get_device_list: OK
- libusb_wrap_sys_device en libusb_open: OK
