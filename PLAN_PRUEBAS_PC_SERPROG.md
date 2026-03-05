# Plan de pruebas en PC para validar serprog (Android + Arduino UNO CH340G)

Este plan permite aislar fallos entre:
- firmware Arduino (`serprog_arduino_uno_ch340g.ino`),
- puente Android PTY↔USB (`PtyBridge`),
- y comportamiento de `flashrom`.

---

## 1) Objetivo

Validar extremo a extremo que:
1. El Arduino responde correctamente al protocolo serprog base.
2. `flashrom` sincroniza (`SYNCNOP`) sin duplicaciones/corrupción.
3. Si **no hay chip SPI conectado**, se obtiene el mensaje esperado: `No EEPROM/flash device found`.
4. Con chip conectado, la detección/lectura funciona con logs trazables.

---

## 2) Requisitos (PC)

- Linux con `adb`, `flashrom`, `python3`, `screen` o `minicom`.
- Android SDK platform-tools instalado.
- Dispositivo Android físico con tu app instalada (debug/release).
- Arduino UNO CH340G conectado por USB al Android (OTG) o según tu topología de pruebas.
- Firmware cargado en el Arduino:
  - `serprog_arduino_uno_ch340g.ino`

Opcional:
- Analizador lógico USB/UART si quieres diagnóstico de bajo nivel.

---

## 3) Preparación

### 3.1 Compilar firmware en PC

```bash
./compile-serprog-firmware.sh
```

### 3.2 Flashear firmware al Arduino (ejemplo)

> Ajusta puerto y programador según tu entorno.

```bash
arduino --upload --board arduino:avr:uno --port /dev/ttyUSB0 ./serprog_arduino_uno_ch340g.ino
```

### 3.3 Verificar que Android ve el dispositivo

```bash
adb devices
adb shell dumpsys usb
```

---

## 4) Prueba A: Smoke local en PC (sin Android)

Objetivo: confirmar que el firmware responde en serie como esperas.

1. Abre monitor serie a 115200 8N1 sin control de flujo.
2. Espera beacon de arranque `AA 55`.
3. Envía comandos básicos y valida respuesta:
   - `0x00 -> 06`
   - `0x01 -> 06 01 00`
   - `0x10 -> 15 06`
   - `0x02 -> 06 + 32 bytes`

Ejemplo con Python (adaptar puerto):

```python
import serial, time
ser = serial.Serial('/dev/ttyUSB0', 115200, timeout=1)
time.sleep(2.5)
print('beacon', ser.read(2).hex())
for payload, n in [(b'\x00',1),(b'\x01',3),(b'\x10',2),(b'\x02',33)]:
    ser.write(payload)
    print(payload.hex(), ser.read(n).hex())
ser.close()
```

Criterio de éxito:
- Respuestas exactas y estables en repeticiones consecutivas.

---

## 5) Prueba B: Android + app + logs PTY/USB

Objetivo: confirmar que el bridge no introduce duplicados al inicio.

1. En la app, selecciona programador `serprog`.
2. Activa logs detallados del bridge en UI.
3. Ejecuta operación de prueba (`Probe` / lectura corta).
4. Captura logs Android:

```bash
adb logcat -v time | grep -E "PtyBridge|flashrom|USB→PTY|PTY→USB|SYNC"
```

Criterios de éxito:
- Secuencia inicial coherente:
  - `[PTY→USB] ... 10` seguido por `USB→PTY ... 15 06`
- No ráfagas duplicadas inesperadas de ACK/NAK.

---

## 6) Prueba C: flashrom en Android SIN chip SPI conectado

Objetivo: validar mensaje esperado de no detección.

Desde la app (o comando equivalente), lanzar `flashrom -V -p serprog:dev=...`.

Criterio de éxito:
- Inicialización serprog completa (sync, iface, cmdmap, bustype)
- Resultado final:
  - `No EEPROM/flash device found.`

Esto **no es fallo** del bridge ni del firmware si no hay chip.

---

## 7) Prueba D: flashrom en Android CON chip SPI conectado

1. Conecta chip compatible y cableado SPI correcto (MISO/MOSI/SCK/CS/GND/VCC).
2. Ejecuta:
   - Detección: `flashrom -V -p serprog:dev=...`
   - Lectura: `flashrom -V -p serprog:dev=... -r dump.bin`
3. Repite 3 veces para descartar intermitencia.

Criterio de éxito:
- Chip identificado consistentemente.
- Lectura completa sin errores de sync/timeouts.

---

## 8) Matriz de diagnóstico rápido

- **Falla en Prueba A**: problema en firmware/cableado UART.
- **A pasa, B falla**: problema en bridge Android PTY↔USB o timing CH340.
- **B pasa, C da “No EEPROM/flash device found”**: esperado si no hay chip.
- **B/C pasan, D falla**: problema en cableado SPI, nivel lógico o chip objetivo.

---

## 9) Checklist de evidencia para cerrar incidencia

Guardar:
- Logcat completo de una corrida buena y una mala.
- Comando exacto de flashrom usado en cada corrida.
- Hex de primeras transacciones PTY→USB y USB→PTY.
- Versión de firmware Arduino (hash/fecha).
- Foto o esquema de cableado SPI.

---

## 10) Comandos recomendados de captura

```bash
# Logcat filtrado del bridge
adb logcat -c
adb logcat -v threadtime | tee /tmp/serprog_android.log

# Guardar salida flashrom en archivo (si lo ejecutas desde shell Android)
adb shell 'flashrom -V -p serprog:dev=/dev/pts/XX:115200 2>&1' | tee /tmp/flashrom_run.log
```

---

## 11) Notas prácticas

- CH340G es sensible a timing tras auto-reset; espera beacon antes de iniciar forwarding.
- Evita abrir/cerrar puerto serie repetidamente durante una misma prueba.
- Si hay duplicación de bytes al inicio, comparar siempre PTY→USB vs USB→PTY para ver dónde nace.
