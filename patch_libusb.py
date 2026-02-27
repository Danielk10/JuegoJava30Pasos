import os


def _insert_once(lines, anchor_predicate, snippet_lines):
    joined = ''.join(lines)
    snippet_text = ''.join(snippet_lines)
    if snippet_text in joined:
        return lines, False

    for i, line in enumerate(lines):
        if anchor_predicate(line):
            lines[i + 1:i + 1] = snippet_lines
            return lines, True
    return lines, False


def run_patch():
    file_path = 'core.c'
    if not os.path.exists(file_path):
        print('Error: No se encontro core.c')
        return

    with open(file_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()

    changed = False

    # 1) Insertar headers (idempotente).
    header_snippet = ['#include <stdlib.h>\n', '#include <stdint.h>\n']
    if '#include <stdlib.h>\n' not in lines:
        lines, did = _insert_once(lines, lambda l: '#include <stdio.h>' in l, header_snippet)
        changed = changed or did

    # 2) Parchear libusb_get_device_list
    get_list_snippet = [
        '\n\tchar *f1 = getenv("ANDROID_USB_FD");\n',
        '\tif (f1) {\n',
        '\t\tret = calloc(2, sizeof(void*));\n',
        '\t\tstruct libusb_device *d = usbi_alloc_device(usbi_get_context(ctx), 0);\n',
        '\t\tret[0] = d; ret[1] = NULL; *list = ret;\n',
        '\t\tif (discdevs) discovered_devs_free(discdevs);\n',
        '\t\treturn 1;\n',
        '\t}\n'
    ]
    if 'ANDROID_USB_FD' not in ''.join(lines):
        for i, line in enumerate(lines):
            if 'ssize_t API_EXPORTED libusb_get_device_list' in line:
                for j in range(i, min(i + 80, len(lines))):
                    if 'ssize_t i, len' in lines[j]:
                        lines[j + 1:j + 1] = get_list_snippet
                        changed = True
                        break
                break

    # 3) Parchear libusb_open
    open_snippet = [
        '\n\tchar *f2 = getenv("ANDROID_USB_FD");\n',
        '\tif (f2) {\n',
        '\t\tint fd = atoi(f2);\n',
        '\t\treturn libusb_wrap_sys_device(ctx, (intptr_t)fd, dev_handle);\n',
        '\t}\n'
    ]
    if 'libusb_wrap_sys_device(ctx, (intptr_t)fd, dev_handle)' not in ''.join(lines):
        for i, line in enumerate(lines):
            if 'int API_EXPORTED libusb_open(' in line:
                for j in range(i, min(i + 80, len(lines))):
                    if 'int r;' in lines[j]:
                        lines[j + 1:j + 1] = open_snippet
                        changed = True
                        break
                break

    if changed:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.writelines(lines)
        print('--- Parche aplicado con exito ---')
    else:
        print('--- Sin cambios: core.c ya contiene el parche o no se encontraron anclas ---')


if __name__ == '__main__':
    run_patch()
