#include <jni.h>
#include <string.h>
#include <stdint.h>
#include <android/log.h>

#define LOG_TAG "FirmwareUtilsNative"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" {

/**
 * Recalcula nativamente el checksum NVM de 16 bits para Intel GbE.
 * Suma las 64 palabras de 16 bits (omitiendo checksumWordIndex) y devuelve
 * el valor complementario para que la suma total sea 0xBABA.
 */
JNIEXPORT jint JNICALL
Java_com_diamon_curso_binary_GbeMacPatcher_calculateNvmChecksumNative(
        JNIEnv *env, jclass clazz, jbyteArray data, jint offset, jint checksumWordIndex) {
    if (data == nullptr || offset < 0 || checksumWordIndex < 0 || checksumWordIndex >= 64) {
        return -1;
    }

    jsize len = env->GetArrayLength(data);
    if (offset + 128 > len) {
        return -1;
    }

    jbyte *buf = env->GetByteArrayElements(data, nullptr);
    if (!buf) return -1;

    const uint8_t *p = reinterpret_cast<const uint8_t *>(buf + offset);
    uint32_t sum = 0;

    for (int i = 0; i < 64; i++) {
        if (i == checksumWordIndex) continue;
        uint16_t word = static_cast<uint16_t>(p[i * 2]) | (static_cast<uint16_t>(p[i * 2 + 1]) << 8);
        sum += word;
    }

    env->ReleaseByteArrayElements(data, buf, JNI_ABORT);

    uint16_t chk = static_cast<uint16_t>((0xBABA - (sum & 0xFFFF)) & 0xFFFF);
    return static_cast<jint>(chk);
}

/**
 * Búsqueda rápida de subpatrones binarios (memmem) para firmas MSDM, _SM_, $FPT.
 * Retorna el offset relativo dentro del buffer donde se encontró el patrón, o -1.
 */
JNIEXPORT jint JNICALL
Java_com_diamon_curso_binary_MsdmExtractor_findSignatureNative(
        JNIEnv *env, jclass clazz, jbyteArray data, jint startOffset, jbyteArray pattern) {
    if (data == nullptr || pattern == nullptr || startOffset < 0) {
        return -1;
    }

    jsize dataLen = env->GetArrayLength(data);
    jsize patLen = env->GetArrayLength(pattern);

    if (startOffset + patLen > dataLen || patLen <= 0) {
        return -1;
    }

    jbyte *dataBytes = env->GetByteArrayElements(data, nullptr);
    jbyte *patBytes = env->GetByteArrayElements(pattern, nullptr);

    if (!dataBytes || !patBytes) {
        if (dataBytes) env->ReleaseByteArrayElements(data, dataBytes, JNI_ABORT);
        if (patBytes) env->ReleaseByteArrayElements(pattern, patBytes, JNI_ABORT);
        return -1;
    }

    const void *haystack = dataBytes + startOffset;
    size_t haystackLen = static_cast<size_t>(dataLen - startOffset);
    const void *needle = patBytes;
    size_t needleLen = static_cast<size_t>(patLen);

    const void *match = memmem(haystack, haystackLen, needle, needleLen);

    int result = -1;
    if (match != nullptr) {
        result = static_cast<int>(reinterpret_cast<const char *>(match) - reinterpret_cast<const char *>(dataBytes));
    }

    env->ReleaseByteArrayElements(data, dataBytes, JNI_ABORT);
    env->ReleaseByteArrayElements(pattern, patBytes, JNI_ABORT);

    return result;
}

} // extern "C"
