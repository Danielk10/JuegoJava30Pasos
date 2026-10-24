# Flash SPI Tool v1.9.7 (versionCode 93) — Refactorización de Localización Nativa y Selector Interactivo de Chip (-c)

Esta versión (`v1.9.7`, código de versión `93`) refactoriza el sistema de localización de idiomas alineándolo con el estándar nativo de recursos de Android (`values-zh`, `values-zh-rCN`, `values-zh-rTW`), eliminando `locales_config.xml` para garantizar la detección automática de idioma en dispositivos configurados en Chino, junto con el selector interactivo de modelo de chip (`-c`).

---

## 🚀 Principales Novedades y Mejoras Técnicas

### 1. Refactorización de Localización de Idiomas
• **Alineación con Estándar Nativo:** Eliminación de `locales_config.xml` y limpieza de dependencias de `AppCompatDelegate` en el manifiesto para garantizar que el sistema operativo resuelva correctamente los recursos en Chino (`zh`, `zh-CN`, `zh-TW`) y Español de forma automática, idéntico al comportamiento en aplicaciones de producción estables.

### 2. Selector Interactivo de Modelo de Chip (`-c`)
• **Gestión de Definiciones Múltiples:** Detección automática y selector interactivo ante múltiples definiciones coincidentes en `flashrom` (ej. Winbond W25Q64).  
• **Componente UI Accesible:** Widget dedicado para definir o restablecer a modo automático el parámetro `-c` en un toque.

---

## 🌐 Notas de Versión para Google Play y GitHub (Multilingüe)

### 🇪🇸 Español (`es-419` / `es-ES`)
- Optimización y refactorización del sistema de localización de idiomas alineado con el estándar de recursos nativos (values-zh / zh-rCN / zh-rTW).
- Soporte para selección de modelo de chip (-c) ante definiciones múltiples detectadas en Winbond W25Q64 y similares.
- Selector interactivo de chip (-c) integrado en la interfaz para configurar o borrar la variable en 1 clic.
- Soporte multilingüe completo con selector de idioma en la app (Español, Inglés, Chino Simplificado y Tradicional).

### 🇺🇸 English (`en-US`)
- Optimized and refactored localization system aligned with native resource standards (values-zh / zh-rCN / zh-rTW).
- Added chip model selector (-c) handling for multiple matching flash definitions (e.g. Winbond W25Q64 series).
- Interactive Chip (-c) UI component to easily specify, change, or clear chip definition with 1 tap.
- Full multi-language support with in-app language switcher (Spanish, English, Simplified and Traditional Chinese).

### 🇨🇳 简体中文 (`zh-CN`)
- 优化并重构本地化资源系统，完全对齐原生 Android 语言解析标准 (values-zh / zh-rCN / zh-rTW)。
- 修复并支持多重芯片定义匹配（如 Winbond W25Q64 系列）时自动提示并指定 -c 芯片型号。
- UI 界面新增芯片型号 (-c) 快捷选择栏，支持一键设置、切换或清除芯片型号。
- 全面支持多语言界面与应用内语言切换器（西班牙语、英语、简体中文、繁体中文）。

### 🇹🇼 繁體中文 (`zh-TW`)
- 最佳化並重構在地化資源系統，完全對齊原生 Android 語言解析標準 (values-zh / zh-rCN / zh-rTW)。
- 修復並支援多重晶片定義相符（如 Winbond W25Q64 系列）時自動提示並指定 -c 晶片型號。
- UI 介面新增晶片型號 (-c) 快捷選擇列，支援一鍵設定、切換或清除晶片型號。
- 全面支援多語言介面與應用程式內語言切換器（西班牙文、英文、簡體中文、繁體中文）。

---

## 📦 Artefactos de la Versión

• **APK Release:** `Flash-SPI-Tool-v1.9.7-release.apk` (20 MB)  
  **SHA-256:** `22decc91f1769c22b0d0a586df71c79ad1f1d2d3c0bd4ce551c5d1f6c2c36916`  
• **AAB Release (Google Play Bundle):** `Flash-SPI-Tool-v1.9.7.aab` (19 MB)  
  **SHA-256:** `c567d2007c3dc58101778a741fa3e2dec6aac952238b13ffeacb2adffc882802`  
• **Firma Digital:** Certificado oficial RSA 2048-bit (`firma_flash_spi_tool.jks`)
