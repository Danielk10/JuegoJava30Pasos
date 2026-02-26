
import os

def run_patch():
    file_path = 'core.c'
    if not os.path.exists(file_path):
        print("Error: No se encontro core.c")
        return

    with open(file_path, 'r') as f:
        lines = f.readlines()

    # 1. Insertar Headers
    for i, line in enumerate(lines):
        if '#include <stdio.h>' in line:
            lines.insert(i + 1, '#include <stdlib.h>\n#include <stdint.h>\n')
            break

    # 2. Parchear libusb_get_device_list (aprox linea 841)
    for i, line in enumerate(lines):
        if 'ssize_t API_EXPORTED libusb_get_device_list' in line:
            for j in range(i, i + 20):
                if 'ssize_t i, len' in lines[j]:
                    p = [
                        '\n\tchar *f1 = getenv("ANDROID_USB_FD");\n',
                        '\tif (f1) {\n',
                        '\t\tret = calloc(2, sizeof(void*));\n',
                        '\t\tstruct libusb_device *d = usbi_alloc_device(usbi_get_context(ctx), 0);\n',
                        '\t\tret[0] = d; ret[1] = NULL; *list = ret;\n',
                        '\t\tif (discdevs) discovered_devs_free(discdevs);\n',
                        '\t\treturn 1;\n',
                        '\t}\n'
                    ]
                    lines[j+1:j+1] = p
                    break
            break

    # 3. Parchear libusb_open (aprox linea 1449)
    for i, line in enumerate(lines):
        if 'int API_EXPORTED libusb_open(' in line:
            for j in range(i, i + 20):
                if 'int r;' in lines[j]:
                    p = [
                        '\n\tchar *f2 = getenv("ANDROID_USB_FD");\n',
                        '\tif (f2) {\n',
                        '\t\tint fd = atoi(f2);\n',
                        '\t\treturn libusb_wrap_sys_device(ctx, (intptr_t)fd, dev_handle);\n',
                        '\t}\n'
                    ]
                    lines[j+1:j+1] = p
                    break
            break

    with open(file_path, 'w') as f:
        f.writelines(lines)
    print("--- Parche aplicado con exito ---")

if __name__ == "__main__":
    run_patch()
