import os
import shutil

fake_root_dir = r"c:\Users\Daniel\Desktop\Proyectos\Flash-EEPROM-Tool\fake_root"
assets_dir = r"c:\Users\Daniel\Desktop\Proyectos\Flash-EEPROM-Tool\app\src\main\assets"
jnilibs_dir = r"c:\Users\Daniel\Desktop\Proyectos\Flash-EEPROM-Tool\app\src\main\jniLibs\arm64-v8a"

new_files = []
replaced_files = []
binaries = []

if not os.path.exists(fake_root_dir):
    print("fake_root no existe")
    exit()

def get_all_files(directory):
    file_list = []
    for root, dirs, files in os.walk(directory):
        for file in files:
            file_list.append(os.path.relpath(os.path.join(root, file), directory))
    return file_list

fake_files = get_all_files(fake_root_dir)
asset_files = get_all_files(assets_dir)

for f in fake_files:
    asset_path = os.path.join(assets_dir, f)
    is_executable = False
    # heuristica simple: si esta en bin o si no tiene extension pero esta marcado ejecutable en linux (no tan facil en windows)
    # pero podemos checar las carpetas bin o lib
    
    if os.path.exists(asset_path):
        replaced_files.append(f)
    else:
        new_files.append(f)
        
    if "usr\\bin\\" in f or "usr/bin/" in f:
        binaries.append(f)

print(f"Total fake files: {len(fake_files)}")
print(f"New to assets: {len(new_files)}")
print(f"Replace in assets: {len(replaced_files)}")
print(f"Executables in bin: {binaries}")
