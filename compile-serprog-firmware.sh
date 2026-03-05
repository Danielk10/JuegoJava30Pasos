#!/usr/bin/env bash
set -euo pipefail

SKETCH_SRC="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/serprog_arduino_uno_ch340g.ino"
WORK_DIR="$(mktemp -d)"
trap 'rm -rf "${WORK_DIR}"' EXIT

SKETCH_DIR="${WORK_DIR}/serprog_sketch"
mkdir -p "${SKETCH_DIR}"
cp "${SKETCH_SRC}" "${SKETCH_DIR}/serprog_sketch.ino"

echo "Compilando firmware serprog (Arduino UNO)..."
arduino --verify --board arduino:avr:uno "${SKETCH_DIR}/serprog_sketch.ino"

echo "OK: compilación completada"
