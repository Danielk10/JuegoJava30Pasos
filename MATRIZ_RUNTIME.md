
## Estado documental actual

- README actualizado con programadores soportados/no soportados según logs de build.
- Auditoría UI actualizada con selector por lista, modo manual legacy y comando manual en UI principal.
- Guía de integración manual de 9 binarios disponible en `GUIA_INTEGRACION_BINARIOS.md` (sin borrado de binarios en PR).

---

# Matriz de Runtime (data → assets/jniLibs → files/usr)

Esta matriz documenta **qué archivo base existe en `data/.../usr`**, si viene en assets, si se resuelve por `jniLibs`, y la estrategia final en runtime (`/data/user/0/com.diamon.curso/files/usr`).

| Ruta runtime (usr/...) | En data | En assets | Mapeo jniLibs | Existe en jniLibs | Estrategia runtime |
|---|---|---|---|---|---|
| bin/ftdi_eeprom | SI | NO | libftdi_eeprom.so | SI | Symlink a jniLibs (fallback: copia) |
| bin/libftdi1-config | SI | NO | liblibftdi1-config.so | SI | Symlink a jniLibs (fallback: copia) |
| bin/lspci | SI | NO | liblspci.so | SI | Symlink a jniLibs (fallback: copia) |
| include/libflashrom.h | SI | SI | - | NO | Copiado desde assets al primer arranque |
| include/libftdi1/ftdi.h | SI | SI | - | NO | Copiado desde assets al primer arranque |
| include/libftdi1/ftdi.hpp | SI | SI | - | NO | Copiado desde assets al primer arranque |
| include/libjaylink/libjaylink.h | SI | SI | - | NO | Copiado desde assets al primer arranque |
| include/libjaylink/version.h | SI | SI | - | NO | Copiado desde assets al primer arranque |
| include/libusb-1.0/libusb.h | SI | SI | - | NO | Copiado desde assets al primer arranque |
| include/pci/config.h | SI | SI | - | NO | Copiado desde assets al primer arranque |
| include/pci/header.h | SI | SI | - | NO | Copiado desde assets al primer arranque |
| include/pci/pci.h | SI | SI | - | NO | Copiado desde assets al primer arranque |
| include/pci/types.h | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/cmake/libftdi1/LibFTDI1Config.cmake | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/cmake/libftdi1/LibFTDI1ConfigVersion.cmake | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/cmake/libftdi1/UseLibFTDI1.cmake | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/libflashrom.a | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/libflashrom.so | SI | NO | libflashrom.so | SI | Symlink a jniLibs (fallback: copia) |
| lib/libflashrom.so.1 | SI | NO | libflashrom.so.1 | SI | Symlink a jniLibs (fallback: copia) |
| lib/libflashrom.so.1.0.0 | SI | NO | libflashrom.so.1.0.0 | SI | Symlink a jniLibs (fallback: copia) |
| lib/libftdi1.a | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/libftdi1.so | SI | NO | libftdi1.so | SI | Symlink a jniLibs (fallback: copia) |
| lib/libftdi1.so.2 | SI | NO | libftdi1.so.2 | SI | Symlink a jniLibs (fallback: copia) |
| lib/libftdi1.so.2.6.0 | SI | NO | libftdi1.so.2.6.0 | SI | Symlink a jniLibs (fallback: copia) |
| lib/libftdipp1.a | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/libftdipp1.so | SI | NO | libftdipp1.so | SI | Symlink a jniLibs (fallback: copia) |
| lib/libftdipp1.so.2.6.0 | SI | NO | libftdipp1.so.2.6.0 | SI | Symlink a jniLibs (fallback: copia) |
| lib/libftdipp1.so.3 | SI | NO | libftdipp1.so.3 | SI | Symlink a jniLibs (fallback: copia) |
| lib/libjaylink.a | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/libjaylink.la | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/libjaylink.so | SI | NO | libjaylink.so | SI | Symlink a jniLibs (fallback: copia) |
| lib/libpci.so | SI | NO | libpci.so | SI | Symlink a jniLibs (fallback: copia) |
| lib/libpci.so.3 | SI | NO | libpci.so.3 | SI | Symlink a jniLibs (fallback: copia) |
| lib/libpci.so.3.14.0 | SI | NO | libpci.so.3.14.0 | SI | Symlink a jniLibs (fallback: copia) |
| lib/libusb-1.0.a | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/libusb-1.0.la | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/libusb-1.0.so | SI | NO | libusb-1.0.so | SI | Symlink a jniLibs (fallback: copia) |
| lib/pkgconfig/flashrom.pc | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/pkgconfig/libftdi1.pc | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/pkgconfig/libftdipp1.pc | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/pkgconfig/libjaylink.pc | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/pkgconfig/libpci.pc | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/pkgconfig/libusb-1.0.pc | SI | SI | - | NO | Copiado desde assets al primer arranque |
| lib/python3.12/site-packages/_pyftdi1.so | SI | NO | libpyftdi1.so | SI | Symlink a jniLibs (fallback: copia) |
| lib/python3.12/site-packages/ftdi1.py | SI | SI | - | NO | Copiado desde assets al primer arranque |
| man/man5/pci.ids.5 | SI | SI | - | NO | Copiado desde assets al primer arranque |
| man/man7/pcilib.7 | SI | SI | - | NO | Copiado desde assets al primer arranque |
| man/man8/lspci.8 | SI | SI | - | NO | Copiado desde assets al primer arranque |
| man/man8/pcilmr.8 | SI | SI | - | NO | Copiado desde assets al primer arranque |
| man/man8/setpci.8 | SI | SI | - | NO | Copiado desde assets al primer arranque |
| man/man8/update-pciids.8 | SI | SI | - | NO | Copiado desde assets al primer arranque |
| sbin/flashrom | SI | NO | libflashrom_bin.so | SI | Symlink a jniLibs (fallback: copia) |
| sbin/pcilmr | SI | NO | libpcilmr.so | SI | Symlink a jniLibs (fallback: copia) |
| sbin/setpci | SI | NO | libsetpci.so | SI | Symlink a jniLibs (fallback: copia) |
| sbin/update-pciids | SI | NO | libupdate-pciids.so | SI | Symlink a jniLibs (fallback: copia) |
| share/bash-completion/completions/flashrom.bash | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/.buildinfo | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/1200px-DIP_socket_as_SOIC_clip.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/166px-Serprogduino_v2.jpeg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/250px-Arduino_5V_lpc_spi_shield.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/250px-Arduino_lpcspi_shield_render.png | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/300px-AtmegaXXu2-flasher.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/300px-Avr_rs232_programmer.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/300px-HydraFW_Default_PinAssignment.png | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/300px-Serprog_0001.jpeg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/300px-Teensy31_lpcspi_flasher.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/300px-glasgow-in-case.png | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/300px-glasgow-pcba.png | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/ARM-USB-TINY_pinout.png | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Amd_am29f010_tsop32.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Bios_savior.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Buspirate_v3_back.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Buspirate_v3_front.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Dip32_chip.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Dip32_chip_back.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Dip32_in_socket.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Dip8_chip.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Dip8_chip_back.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Dip8_in_socket.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Dip_tool.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Dlp_usb1232h_bottom.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Dlp_usb1232h_side.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Dlp_usb1232h_spi_programmer.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Dlp_usb1232h_spi_programmer_breadboard_1.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Dlp_usb1232h_spi_programmer_breadboard_2.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Dual_plcc32_soldered.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Empty_dip32_socket.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Empty_dip8_socket.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Empty_plcc32_socket.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Flash-BGA.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Ft2232spi_programer.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Lycom-pe115-flashrom-buspirate-2.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Openmoko_0001.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Openmoko_0002.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Openmoko_0003.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/P1v1_arduino328.png | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/P1v2_arduino1280.png | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/P2v1_resdivider.png | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/P2v2_oc_cs.png | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/P2v3_buffer4050.png | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/P3v1_dil8_so8_spi.png | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/P3v2_so16_spi.png | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Plcc32_chip.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Plcc32_chip_back.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Plcc32_in_socket.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Plcc_tool.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Pomona_5250_soic8.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Pushpin_roms_2.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Soic8_chip.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Soic8_socket_back.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Soic8_socket_front_closed.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Soic8_socket_half_opened.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Soic8_socket_open.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Soic8_socket_with_chip.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Soic8_socket_with_chip_inserted.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Soldered_plcc32.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Soldered_tsop40.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Soldered_tsop48.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Spi-socket-dscn2913-1024x768.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Sst_39vf040_tsop32.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Top_hat_flash.jpeg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Via_epia_m700_bios.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/Via_epia_m700_programer.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/black_board_with_IC_socket.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/blue_board_additional_IC_socket_with_wiring.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/blue_board_without_IC_socket.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/flashrom_logo.png | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/green_3_3V_orange_5V.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/pinout_of_EEPROM.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_images/yellow_insulating_tape_pink_new_connections.jpg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/about_flashrom/code_of_conduct.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/about_flashrom/hall_of_fame.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/about_flashrom/index.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/about_flashrom/license.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/about_flashrom/privacy_policy.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/about_flashrom/team.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/classic_cli_manpage.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/contact.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/contrib_howtos/board_enable/board_testing_howto.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/contrib_howtos/board_enable/index.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/contrib_howtos/board_enable/overview.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/contrib_howtos/board_enable/reverse_engineering.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/contrib_howtos/how_to_add_new_chip.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/contrib_howtos/how_to_add_unit_test.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/contrib_howtos/how_to_mark_chip_tested.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/contrib_howtos/index.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/contrib_howtos/laptops_and_ec.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/dev_guide/building_from_source.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/dev_guide/development_guide.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/dev_guide/index.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/dev_guide/release_process.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/documentation_license.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/how_to_add_docs.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/how_to_support_flashrom.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/index.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/intro.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/release_notes/devel.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/release_notes/index.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/release_notes/v_1_3.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/release_notes/v_1_4.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/release_notes/v_1_5.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/release_notes/v_1_6.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/supported_hw/index.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/supported_hw/supported_boards.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/supported_hw/supported_chipsets.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/supported_hw/supported_flashchips.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/supported_hw/supported_prog/buspirate.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/supported_hw/supported_prog/ch341ab.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/supported_hw/supported_prog/dummyflasher.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/supported_hw/supported_prog/ft2232_spi.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/supported_hw/supported_prog/index.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/supported_hw/supported_prog/serprog/arduino_flasher.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/supported_hw/supported_prog/serprog/arduino_flasher_3.3v.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/supported_hw/supported_prog/serprog/index.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/supported_hw/supported_prog/serprog/overview.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/supported_hw/supported_prog/serprog/serprog-protocol.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/supported_hw/supported_prog/serprog/teensy_3_1.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/user_docs/chromebooks.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/user_docs/example_partial_wp.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/user_docs/fw_updates_vs_spi_wp.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/user_docs/in_system.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/user_docs/index.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/user_docs/management_engine.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/user_docs/misc_intel.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/user_docs/misc_notes.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/user_docs/msi_jspi1.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/user_docs/overview.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_sources/user_docs/raspberry_pi.rst.txt | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_static/alabaster.css | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_static/base-stemmer.js | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_static/basic.css | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_static/custom.css | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_static/doctools.js | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_static/documentation_options.js | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_static/english-stemmer.js | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_static/file.png | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_static/flashrom_icon_color-32x32.ico | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_static/github-banner.svg | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_static/language_data.js | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_static/minus.png | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_static/plus.png | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_static/pygments.css | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_static/searchtools.js | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/_static/sphinx_highlight.js | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/about_flashrom/code_of_conduct.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/about_flashrom/hall_of_fame.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/about_flashrom/index.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/about_flashrom/license.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/about_flashrom/privacy_policy.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/about_flashrom/team.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/classic_cli_manpage.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/contact.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/contrib_howtos/board_enable/board_testing_howto.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/contrib_howtos/board_enable/index.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/contrib_howtos/board_enable/overview.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/contrib_howtos/board_enable/reverse_engineering.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/contrib_howtos/how_to_add_new_chip.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/contrib_howtos/how_to_add_unit_test.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/contrib_howtos/how_to_mark_chip_tested.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/contrib_howtos/index.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/contrib_howtos/laptops_and_ec.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/dev_guide/building_from_source.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/dev_guide/development_guide.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/dev_guide/index.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/dev_guide/release_process.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/documentation_license.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/genindex.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/how_to_add_docs.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/how_to_support_flashrom.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/index.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/intro.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/objects.inv | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/release_notes/devel.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/release_notes/index.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/release_notes/v_1_3.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/release_notes/v_1_4.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/release_notes/v_1_5.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/release_notes/v_1_6.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/search.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/searchindex.js | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/supported_hw/index.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/supported_hw/supported_boards.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/supported_hw/supported_chipsets.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/supported_hw/supported_flashchips.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/supported_hw/supported_prog/buspirate.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/supported_hw/supported_prog/ch341ab.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/supported_hw/supported_prog/dummyflasher.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/supported_hw/supported_prog/ft2232_spi.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/supported_hw/supported_prog/index.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/supported_hw/supported_prog/serprog/arduino_flasher.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/supported_hw/supported_prog/serprog/arduino_flasher_3.3v.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/supported_hw/supported_prog/serprog/index.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/supported_hw/supported_prog/serprog/overview.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/supported_hw/supported_prog/serprog/serprog-protocol.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/supported_hw/supported_prog/serprog/teensy_3_1.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/user_docs/chromebooks.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/user_docs/example_partial_wp.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/user_docs/fw_updates_vs_spi_wp.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/user_docs/in_system.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/user_docs/index.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/user_docs/management_engine.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/user_docs/misc_intel.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/user_docs/misc_notes.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/user_docs/msi_jspi1.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/user_docs/overview.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/flashrom/html/user_docs/raspberry_pi.html | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/doc/libftdi1/example.conf | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/libftdi/examples/cbus.py | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/libftdi/examples/complete.py | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/libftdi/examples/simple.py | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/man/man8/flashrom.8 | SI | SI | - | NO | Copiado desde assets al primer arranque |
| share/pci.ids.gz | SI | SI | - | NO | Copiado desde assets al primer arranque; si Android lo lista como `pci.ids`, se normaliza a `pci.ids.gz` en runtime |
| lib/libz.so.1 | NO (no viene en data base) | SI | libz_1.so | SI | Enlace runtime a soname `libz.so.1` desde binario Android-compat (`libz_1.so`) |
| lib/libconfuse.so | NO (no viene en data base) | SI | libconfuse.so | SI | Enlace runtime directo desde jniLibs |
| lib/libc++_shared.so | NO (no viene en data base) | SI | libc++_shared.so | SI | Enlace runtime directo desde jniLibs |
