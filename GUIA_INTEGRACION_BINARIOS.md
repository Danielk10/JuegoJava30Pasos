# Mini guía: integración manual de 9 binarios/archivos actualizados

> Esta guía está pensada para que **tú reemplaces manualmente** los binarios nuevos sin borrar nada en este PR, evitando errores de compilación durante CI/local.

## 1) Regla principal

- Mantén en el repositorio los binarios actuales para compilar.
- Integra tus 9 archivos nuevos **después** de este PR, reemplazando contenido archivo por archivo.
- Si un archivo nuevo trae nombre distinto, **renómbralo al nombre exacto que ya usa el proyecto**.

## 2) Dónde colocar cada tipo de archivo

### A) Runtime raíz (carpeta `data/` del repo)

Coloca/reemplaza en:

- `data/data/com.diamon.curso/files/usr/lib/`
- `data/data/com.diamon.curso/files/usr/share/bash-completion/completions/`
- `data/data/com.diamon.curso/files/usr/share/man/man8/`

### B) Assets empaquetados de Android

Cada archivo que reemplaces en `data/...` también debe reflejarse en:

- `app/src/main/assets/data/data/com.diamon.curso/files/usr/lib/`
- `app/src/main/assets/data/data/com.diamon.curso/files/usr/share/bash-completion/completions/`
- `app/src/main/assets/data/data/com.diamon.curso/files/usr/share/man/man8/`

## 3) Lista sugerida según tus capturas

De acuerdo con tus imágenes, los cambios incluyen (al menos):

- `libflashrom.a`
- `libflashrom.so.1.0.0`
- `libftdi1.a`
- `libftdi1.so.2.6.0`
- `libjaylink.a`
- `flashrom.bash`
- `flashrom.8`

Completa la lista exacta de 9 en tu entorno y reemplaza en ambas ubicaciones (runtime + assets).

## 4) Duplicados con nombres incompatibles

Si te llega una variante con nombre no compatible para Android/jni, no borres nada aquí:

1. Conserva el archivo existente usado por la app.
2. Renombra tu nuevo binario al nombre esperado por el proyecto (mismo nombre que el viejo).
3. Reemplaza contenido en sitio.

## 5) Verificación rápida recomendada

Después de tu reemplazo manual:

```bash
git status --short
./gradlew assembleDebug
```

Si compila y solo aparecen tus archivos objetivo, integración correcta.
