import os

assets_usr_dir = r"c:\Users\Daniel\Desktop\Proyectos\Flash-EEPROM-Tool\app\src\main\assets\data\data\com.diamon.curso\files\usr"
fake_root_usr_dir = r"c:\Users\Daniel\Desktop\Proyectos\Flash-EEPROM-Tool\fake_root\data\data\com.diamon.curso\files\usr"

assets_files = []
# recolectar todo lo que hay en assets/usr
for root, dirs, files in os.walk(assets_usr_dir):
    for f in files:
        rel = os.path.relpath(os.path.join(root, f), assets_usr_dir)
        assets_files.append(rel)

print(f"Total archivos en assets/usr actualmente: {len(assets_files)}")

# buscar huerfanos (los que esten en assets pero NO en fake_root) 
# y que tampoco pasaron a ser binarios movidos a jniLibs (aunque los binarios igual no deberian estar en assets)
deleted_count = 0
for rel in assets_files:
    # la ruta equivalente en fake_root:
    fake_path = os.path.join(fake_root_usr_dir, rel)
    assets_path = os.path.join(assets_usr_dir, rel)
    
    if not os.path.exists(fake_path):
        os.remove(assets_path)
        print(f"Borrando residual (no existe en fake_root): {rel}")
        deleted_count += 1
    # IMPORTANTE: Si es un ejecutable (bin o sbin), ya se movió a jniLibs, 
    # por lo que de existir en assets ES UN RESIDUAL INCORECTO y genera peso muerto. 
    # Pero el script anterior ya los movio... O los copió a assets? Vamos a checar:
    # script previo hizo: shutil.copy2 a assets, y LUEGO shutil.copy2 a jniLibs si era ejecutable.
    # Por lo tanto, los binarios ESTÁN duplicados en assets ahorita (porque fake_root los tiene y los copio ambos).
    # Debemos borrar de assets los archivos base que detectemos que son binarios o librerias nativas.
    else:
        # Existe en fake_root, pero es un binario nativo/libreria? No hace falta en assets! 
        # (Aunque el app quizas extraiga librerias, android NativeLibs usa jniLibs)
        # Segun la MACRO-MATRIZ, "usr/bin/lspci", "usr/sbin/flashrom" "usr/lib/libflashrom.so" en assets=NO.
        if rel.startswith("bin\\") or rel.startswith("bin/") or rel.startswith("sbin\\") or rel.startswith("sbin/"):
            os.remove(assets_path)
            print(f"Borrando binario de assets (solo deben estar en jniLibs): {rel}")
            deleted_count += 1
        # si es libreria so
        elif (rel.startswith("lib\\") or rel.startswith("lib/")) and ".so" in rel:
            os.remove(assets_path)
            print(f"Borrando libreria nativa .so de assets (solo en jniLibs): {rel}")
            deleted_count += 1

print(f"Limpieza finalizada. Archivos borrados: {deleted_count}")
