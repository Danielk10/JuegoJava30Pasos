# Flash SPI Tool v1.9.6 (versionCode 92) — Selector de Chip (-c) Interactivo, Soporte Multi-idioma (Chino Simplificado y Tradicional) y Optimización de Comandos

Esta versión (`v1.9.6`, código de versión `92`) introduce mejoras clave orientadas a la compatibilidad con múltiples definiciones de chips flash, la internacionalización completa de la interfaz con soporte nativo para Chino Simplificado y Tradicional, y la robustez en la ejecución de comandos en segundo plano.

---

## 🚀 Principales Novedades y Mejoras Técnicas

### 1. Selector Interactivo de Modelo de Chip (`-c`)
• **Gestión de Definiciones Múltiples:** Cuando `flashrom` detecta múltiples definiciones de chips coincidentes (por ejemplo, en series Winbond W25Q64), el sistema analiza el output nativo (`FlashromExecutor.parseOutputForAmbiguity`) y ofrece automáticamente un selector interactivo.  
• **Componente UI Accesible:** Nuevo widget dedicado en la interfaz principal para configurar, cambiar o restablecer a modo automático el parámetro `-c` con un solo toque.

### 2. Soporte Multilingüe Completo (Español, Inglés y Chino)
• **Selector de Idioma In-App:** Añadido menú y selector de idioma utilizando `AppCompatDelegate.setApplicationLocales()`.  
• **Localización Completa:** Traducciones nativas añadidas para **Chino Simplificado (`zh-CN`)** y **Chino Tradicional (`zh-TW`)**, además de Español e Inglés.

### 3. Optimización de Comandos en Segundo Plano
• Refactorización y corrección de la inyección de comandos y captura de salida para los botones GUI de Lectura, Escritura y Verificación de ROM.

---

## 🌐 Notas de Versión para Google Play y GitHub (Multilingüe)

### 🇪🇸 Español (`es-419` / `es-ES`)
- Soporte para selección de modelo de chip (-c) ante definiciones múltiples detectadas en Winbond W25Q64 y similares.
- Selector interactivo de chip (-c) integrado en la interfaz para configurar o borrar la variable en 1 clic.
- Soporte multilingüe completo: añadido selector de idioma con soporte nativo para Chino Simplificado (简体中文) y Tradicional (繁體中文).
- Optimización y corrección en la inyección de comandos en segundo plano para botones GUI de Lectura, Escritura y Verificación.

### 🇺🇸 English (`en-US`)
- Added chip model selector (-c) handling for multiple matching flash definitions (e.g. Winbond W25Q64 series).
- Interactive Chip (-c) UI component to easily specify, change, or clear chip definition with 1 tap.
- Full multi-language support: added in-app language switcher with Simplified Chinese (简体中文) and Traditional Chinese (繁體中文).
- Fixed and optimized background command execution for GUI Read, Write, and Verify buttons.

### 🇨🇳 简体中文 (`zh-CN`)
- 修复并支持多重芯片定义匹配（如 Winbond W25Q64 系列）时自动提示并指定 -c 芯片型号。
- UI 界面新增芯片型号 (-c) 快捷选择栏，支持一键设置、切换或清除芯片型号。
- 全面支持多语言界面：新增应用内语言切换器，完整支持简体中文与繁体中文本地化。
- 优化读取芯片、写入芯片和校验 ROM 等 GUI 按钮的后台指令自动追加逻辑。

### 🇹🇼 繁體中文 (`zh-TW`)
- 修復並支援多重晶片定義相符（如 Winbond W25Q64 系列）時自動提示並指定 -c 晶片型號。
- UI 介面新增晶片型號 (-c) 快捷選擇列，支援一鍵設定、切換或清除晶片型號。
- 全面支援多語言介面：新增應用程式內語言切換器，完整支援繁體中文與簡體中文在地化。
- 最佳化讀取晶片、寫入晶片和校驗 ROM 等 GUI 按鈕的背景指令自動附加邏輯。

---

## 📦 Artefactos de la Versión

• **APK Release:** `Flash-SPI-Tool-v1.9.6-release.apk` (20 MB)  
  **SHA-256:** `a8e23d54022307dab2068168f5860434c2545159ee2e7465bdffc528426a2737`  
• **AAB Release (Google Play Bundle):** `Flash-SPI-Tool-v1.9.6.aab` (19 MB)  
  **SHA-256:** `2b0c0724f093dae503d32112a9c7ed37ff11223b185d52d68d4ab5d2e29b8d3b`  
• **Firma Digital:** Certificado oficial RSA 2048-bit (`firma_flash_spi_tool.jks`)
