#include <jni.h>
#include <string>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/wait.h>
#include <android/log.h>

#define LOG_TAG "FlashromJNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jint JNICALL
Java_com_diamon_curso_MainActivity_runNativeFlashrom(JNIEnv *env, jobject thiz, jint fd, jstring binPath, jstring args) {
    const char *nativeBinPath = env->GetStringUTFChars(binPath, nullptr);
    const char *nativeArgs = env->GetStringUTFChars(args, nullptr);

    // 1. Establecer el File Descriptor en el entorno para nuestra libusb parcheada
    std::string fd_str = std::to_string(fd);
    setenv("ANDROID_USB_FD", fd_str.c_str(), 1);
    LOGI("ANDROID_USB_FD set to %s", fd_str.c_str());

    // 2. Ejecutar el comando de flashrom almacenado en el data del app
    // Como args contendrá parametros separados por espacio, usar system()
    // es más sencillo que armar un vector de C-strings para execve
    std::string command = std::string(nativeBinPath) + " " + std::string(nativeArgs) + " 2>&1";
    LOGI("Ejecutando: %s", command.c_str());

    // NOTA: libusb_wrap_sys_device es interceptado automáticamente
    // por nuestro binario precompilado que usa la libusb parcheada.
    int result = system(command.c_str());

    int exitCode = WEXITSTATUS(result);
    LOGI("Comando finalizó con código: %d", exitCode);

    env->ReleaseStringUTFChars(binPath, nativeBinPath);
    env->ReleaseStringUTFChars(args, nativeArgs);

    return exitCode;
}

/**
 * Crea un par pseudo-terminal (PTY) y devuelve [masterFdStr, slavePath].
 * El slave path (e.g. "/dev/pts/3") se pasa a flashrom como:
 *   -p serprog:dev=/dev/pts/3:115200
 * El master FD se usa en Java para puentear bytes con usb-serial-for-android.
 *
 * Retorna null si el sistema no soporta devpts.
 */
extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_diamon_curso_PtyBridge_createPty(JNIEnv *env, jclass clazz) {
    // Abrir el master PTY
    int masterFd = posix_openpt(O_RDWR | O_NOCTTY);
    if (masterFd < 0) {
        LOGE("posix_openpt falló: errno=%d", errno);
        return nullptr;
    }
    if (grantpt(masterFd) != 0) {
        LOGE("grantpt falló: errno=%d", errno);
        close(masterFd);
        return nullptr;
    }
    if (unlockpt(masterFd) != 0) {
        LOGE("unlockpt falló: errno=%d", errno);
        close(masterFd);
        return nullptr;
    }
    const char *slavePath = ptsname(masterFd);
    if (slavePath == nullptr) {
        LOGE("ptsname falló: errno=%d", errno);
        close(masterFd);
        return nullptr;
    }
    LOGI("PTY creado: master_fd=%d slave=%s", masterFd, slavePath);

    // Construir array de retorno: [masterFdAsString, slavePath]
    jclass stringClass = env->FindClass("java/lang/String");
    jobjectArray result = env->NewObjectArray(2, stringClass, nullptr);
    env->SetObjectArrayElement(result, 0, env->NewStringUTF(std::to_string(masterFd).c_str()));
    env->SetObjectArrayElement(result, 1, env->NewStringUTF(slavePath));
    return result;
}

/**
 * Cierra un file descriptor nativo. Usado para liberar el master del PTY
 * cuando la operación de flashrom termina.
 */
extern "C" JNIEXPORT void JNICALL
Java_com_diamon_curso_PtyBridge_closeFd(JNIEnv *env, jclass clazz, jint fd) {
    if (fd >= 0) {
        LOGI("Cerrando FD nativo: %d", fd);
        close((int) fd);
    }
}