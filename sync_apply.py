import os
import shutil

fake_root_dir = r"c:\Users\Daniel\Desktop\Proyectos\Flash-EEPROM-Tool\fake_root"
assets_dir = r"c:\Users\Daniel\Desktop\Proyectos\Flash-EEPROM-Tool\app\src\main\assets"
jnilibs_dir = r"c:\Users\Daniel\Desktop\Proyectos\Flash-EEPROM-Tool\app\src\main\jniLibs\arm64-v8a"

def get_renamed_so(name):
    if ".so" not in name: return name
    if name.endswith(".so"): return name
    parts = name.split(".so.")
    if len(parts) == 2:
        vers_part = parts[1].replace(".", "_")
        return f"{parts[0]}_{vers_part}.so"
    return name

def get_executable_renamed(name):
    if name == "flashrom": return "libflashrom_bin.so"
    return f"lib{name}.so"

executables_list = [
    "flashrom", "pcilmr", "setpci", "update-pciids", "ftdi_eeprom", "libftdi1-config", "lspci"
]

print("Iniciando sincronización...")

# 1 & 2 & 3: Copy to assets and copy/rename to jniLibs
for root, dirs, files in os.walk(fake_root_dir):
    for filename in files:
        src_path = os.path.join(root, filename)
        rel_path = os.path.relpath(src_path, fake_root_dir)
        
        # --- Copia exacta a assets ---
        dest_assets_path = os.path.join(assets_dir, rel_path)
        os.makedirs(os.path.dirname(dest_assets_path), exist_ok=True)
        shutil.copy2(src_path, dest_assets_path)
        
        # --- Copias a jniLibs para ejecutables/librerias dinámicas ---
        # Si es un ejecutable (basado en la ruta usr/bin o usr/sbin)
        if "usr\\bin\\" in rel_path or "usr/bin/" in rel_path or "usr\\sbin\\" in rel_path or "usr/sbin/" in rel_path:
            renamed = get_executable_renamed(filename)
            dest_jni_path = os.path.join(jnilibs_dir, renamed)
            shutil.copy2(src_path, dest_jni_path)
            print(f"[{filename}] copiado a jniLibs como -> {renamed}")
        
        # Si es una librería dinámica (basado en usr/lib y nombre .so)
        elif ("usr\\lib\\" in rel_path or "usr/lib/" in rel_path) and ".so" in filename:
            renamed = get_renamed_so(filename)
            dest_jni_path = os.path.join(jnilibs_dir, renamed)
            shutil.copy2(src_path, dest_jni_path)
            print(f"[{filename}] copiado a jniLibs como -> {renamed}")

# 4: Delete un-renamed duplicates from jniLibs
for f in os.listdir(jnilibs_dir):
    path_f = os.path.join(jnilibs_dir, f)
    if os.path.isfile(path_f):
        # Librerías estilo libflashrom.so.1.0.0 (que no terminan exactamente en .so pero tienen .so.)
        if ".so." in f and not f.endswith(".so"):
            os.remove(path_f)
            print(f"Borrado duplicado sin renombrar (librería): {f}")
        # Los ejecutables sin sufijos correctos (archivos como 'flashrom', 'lspci')
        elif f in executables_list:
            os.remove(path_f)
            print(f"Borrado duplicado sin renombrar (ejecutable): {f}")

print("Sincronización completada!")
