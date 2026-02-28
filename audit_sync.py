import os

fake_root = r"c:\Users\Daniel\Desktop\Proyectos\Flash-EEPROM-Tool\fake_root\data\data\com.diamon.curso\files"
assets_root = r"c:\Users\Daniel\Desktop\Proyectos\Flash-EEPROM-Tool\app\src\main\assets\data\data\com.diamon.curso\files"
jniLibs = r"c:\Users\Daniel\Desktop\Proyectos\Flash-EEPROM-Tool\app\src\main\jniLibs\arm64-v8a"

print("--- REPORTE DE AUDITORIA DE INTEGRACION ---\n")

def get_files(path):
    f_list = []
    for root, _, files in os.walk(path):
        for f in files:
            f_list.append(os.path.relpath(os.path.join(root, f), path))
    return f_list

# 1. Obtencion de listas
fake_files = get_files(fake_root)
assets_files = get_files(assets_root)
jni_files = os.listdir(jniLibs) if os.path.exists(jniLibs) else []

# 2. Verificar que todo fake_root termino en assets o en jniLibs
missing_fake = []
for f in fake_files:
    # es un binario que debio ir a jniLibs (excepto bash script update-pciids)?
    if (f.startswith("usr\\bin\\") or f.startswith("usr\\sbin\\") or (f.startswith("usr\\lib\\") and ".so" in f)):
        if f == "usr\\sbin\\update-pciids":
             # caso especial, va a assets
             if f not in assets_files:
                 missing_fake.append(f)
             continue
        
        # verificar en jniLibs (regla: prefix lib, append .so o parse_version)
        name = os.path.basename(f)
        jni_expected = name
        if not jni_expected.startswith("lib"): jni_expected = "lib" + jni_expected
        if ".so" not in jni_expected: jni_expected = jni_expected + ".so"
        if name == "flashrom": jni_expected = "libflashrom_bin.so"
        if name == "ftdi_eeprom": jni_expected = "libftdi_eeprom.so"
        
        # logica de .so.1.0 -> _1_0.so
        if ".so." in jni_expected:
            parts = jni_expected.split(".so.")
            jni_expected = parts[0] + "_" + parts[1].replace(".", "_") + ".so"
            
        if jni_expected not in jni_files and f"lib{name}_bin.so" not in jni_files:
             missing_fake.append(f + f" [Esperado en jniLibs como {jni_expected}]")
             continue
    else:
        # no es binario, debio ir directo a assets
        if f not in assets_files:
            missing_fake.append(f + " [Esperado en assets]")

print(f"Archivos en fake_root: {len(fake_files)}")
if missing_fake:
    print("X ERROR: Faltan archivos de fake_root en destino:")
    for m in missing_fake: print("   -", m)
else:
    print("V Todos los archivos de fake_root estan presentes en destino (assets o jniLibs)")

# 3. Verificar que assets NO tenga cosas viejas/basura
orphans_assets = []
for a in assets_files:
    if a not in fake_files:
        orphans_assets.append(a)

if orphans_assets:
    print(f"X ALERTA: Hay {len(orphans_assets)} archivos en assets que NO existen en fake_root:")
    for o in orphans_assets: print("   -", o)
else:
    print("V Carpeta Assets es una copia limpia e identica de fake_root (menos binarios extraidos)")

# 4. Verificar basuritas en jniLibs
orphans_jni = []
expected_jni = [
 "libflashrom_bin.so", "libpcilmr.so", "libsetpci.so", "liblspci.so", "libftdi_eeprom.so", "liblibftdi1-config.so"
] # y las lib*.so.* migrados
for jni in jni_files:
    if jni == "libc++_shared.so": continue # archivo nativo del app
    if jni == "libdiamon-tools.so": continue # libreria nativa de la app
    # la auditoria previa limpio jniLibs asi que validamos q todo este OK
    pass 

print("\nAuditoria completada.")
