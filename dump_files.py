import os

fake_root_dir = r"c:\Users\Daniel\Desktop\Proyectos\Flash-EEPROM-Tool\fake_root"
out_file = r"c:\Users\Daniel\Desktop\Proyectos\Flash-EEPROM-Tool\fake_root_files.txt"

with open(out_file, "w") as f:
    for root, dirs, files in os.walk(fake_root_dir):
        for file in files:
            path = os.path.relpath(os.path.join(root, file), fake_root_dir)
            f.write(path + "\n")
