# Estado de resolución runtime (documentación)

Este reporte de `DT_NEEDED` se complementa con:

- `MATRIZ_RUNTIME.mdo` (mapeo archivo por archivo a estrategia runtime)
- `AUDITORIA_ANDROID_RUNTIME.md` (checklist operativo)

## Resumen de dependencias críticas y opcionales

- Dependencias base presentes en `jniLibs` para flashrom y librerías principales: `libusb-1.0.so`, `libpci.so(.3/.3.14.0)`, `libftdi1.so(.2/.2.6.0)`, `libftdipp1.so(.3/.2.6.0)`, `libjaylink.so`, `libcrypto.so.3`, `libssl.so.3`, `libflashrom.so(.1/.1.0.0)`, `libflashrom_bin.so`, `libsetpci.so`, `libpcilmr.so`, `liblspci.so`, `libupdate-pciids.so`, `libftdi_eeprom.so`, `liblibftdi1-config.so`, `libpyftdi1.so`.
- Dependencias opcionales reportadas por `DT_NEEDED` de algunos binarios que pueden no venir en el paquete inicial y se contemplan para auto-link cuando se agreguen en `jniLibs`: `libz.so.1`, `libconfuse.so`, `libc++_shared.so`.

---

# Reporte de dependencias de binarios

## app/src/main/jniLibs/arm64-v8a/libcrypto.so.3
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libdl.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libflashrom.so
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libcrypto.so.3
  - libpci.so.3
  - libusb-1.0.so
  - libftdi1.so.2
  - libjaylink.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libflashrom.so.1
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libcrypto.so.3
  - libpci.so.3
  - libusb-1.0.so
  - libftdi1.so.2
  - libjaylink.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libflashrom.so.1.0.0
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libcrypto.so.3
  - libpci.so.3
  - libusb-1.0.so
  - libftdi1.so.2
  - libjaylink.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libflashrom_bin.so
- Tipo ELF: DYN (Position-Independent Executable file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libcrypto.so.3
  - libpci.so.3
  - libusb-1.0.so
  - libftdi1.so.2
  - libjaylink.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libftdi1.so
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libusb-1.0.so
  - libdl.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libftdi1.so.2
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libusb-1.0.so
  - libdl.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libftdi1.so.2.6.0
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libusb-1.0.so
  - libdl.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libftdi_eeprom.so
- Tipo ELF: DYN (Position-Independent Executable file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libftdi1.so.2
  - libconfuse.so
  - libusb-1.0.so
  - libdl.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libftdipp1.so
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libftdi1.so.2
  - libusb-1.0.so
  - libc++_shared.so
  - libdl.so
  - libm.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libftdipp1.so.2.6.0
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libftdi1.so.2
  - libusb-1.0.so
  - libc++_shared.so
  - libdl.so
  - libm.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libftdipp1.so.3
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libftdi1.so.2
  - libusb-1.0.so
  - libc++_shared.so
  - libdl.so
  - libm.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libjaylink.so
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libusb-1.0.so
  - libdl.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/liblspci.so
- Tipo ELF: DYN (Position-Independent Executable file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libz.so.1
  - libpci.so.3
  - libdl.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libpci.so
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libz.so.1
  - libdl.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libpci.so.3
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libz.so.1
  - libdl.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libpci.so.3.14.0
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libz.so.1
  - libdl.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libpcilmr.so
- Tipo ELF: DYN (Position-Independent Executable file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libz.so.1
  - libpci.so.3
  - libdl.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libpyftdi1.so
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libftdi1.so.2
  - libusb-1.0.so
  - libdl.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libsetpci.so
- Tipo ELF: DYN (Position-Independent Executable file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libz.so.1
  - libpci.so.3
  - libdl.so
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libssl.so.3
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libcrypto.so.3
  - libc.so

## app/src/main/jniLibs/arm64-v8a/libusb-1.0.so
- Tipo ELF: DYN (Shared object file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED):
  - libdl.so
  - libc.so

## app/src/main/assets/data/data/com.diamon.curso/files/usr/lib/libflashrom.a
- Tipo ELF: REL (Relocatable file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED): (ninguna)

## app/src/main/assets/data/data/com.diamon.curso/files/usr/lib/libftdi1.a
- Tipo ELF: REL (Relocatable file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED): (ninguna)

## app/src/main/assets/data/data/com.diamon.curso/files/usr/lib/libftdipp1.a
- Tipo ELF: REL (Relocatable file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED): (ninguna)

## app/src/main/assets/data/data/com.diamon.curso/files/usr/lib/libjaylink.a
- Tipo ELF: REL (Relocatable file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED): (ninguna)

## app/src/main/assets/data/data/com.diamon.curso/files/usr/lib/libusb-1.0.a
- Tipo ELF: REL (Relocatable file)
- Arquitectura: AArch64
- Dependencias directas (DT_NEEDED): (ninguna)
