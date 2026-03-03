package com.diamon.curso;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * PtyBridge: Puente entre el pseudo-terminal (PTY) que usa flashrom (-p
 * serprog:dev=/dev/pts/N)
 * y el dispositivo USB-Serial real (Arduino con firmware serprog) via
 * usb-serial-for-android.
 *
 * Arquitectura:
 * flashrom <-> /dev/pts/N (slave PTY)
 * ↕ (ParcelFileDescriptor del master PTY)
 * PtyBridge (2 hilos de forwarding)
 * ↕
 * UsbSerialPort (usb-serial-for-android)
 * ↕
 * [Arduino con firmware serprog]
 */
public class PtyBridge {

    private static final String TAG = "PtyBridge";
    private static final int USB_TIMEOUT_MS = 50;
    private static final int BUFFER_SIZE = 4096;

    // Cargado desde native-lib.cpp (mismo .so que el resto de la app)
    static {
        System.loadLibrary("curso");
    }

    // JNI: retorna [masterFdAsString, slavePath] o null si falla
    public static native String[] createPty();

    // JNI: cierra el master FD nativo
    public static native void closeFd(int fd);

    // -------- Estado --------
    private int masterFd = -1;
    private String slavePath = null;
    private UsbSerialPort usbPort = null;
    private UsbDeviceConnection usbConnection = null;
    private ParcelFileDescriptor masterPfd = null;
    private volatile boolean running = false;
    private Thread threadMasterToUsb = null;
    private Thread threadUsbToMaster = null;
    private int baudRate = 115200;

    /**
     * Abre el puente:
     * 1. Crea el par PTY vía JNI (obtiene master FD y slave path).
     * 2. Abre el UsbSerialPort del dispositivo Arduino.
     * 3. Inicia los dos hilos de forwarding bidireccional.
     *
     * @param device     Dispositivo USB detectado
     * @param manager    UsbManager de la app
     * @param connection Conexión ya abierta (openDevice) del dispositivo
     * @param baud       Velocidad serie del firmware serprog (típico: 115200)
     * @return true si todo se abrió correctamente, false en caso de error
     */
    public boolean open(UsbDevice device, UsbManager manager, UsbDeviceConnection connection, int baud) {
        this.baudRate = baud;
        this.usbConnection = connection;

        // 1. Crear PTY
        String[] ptyResult = createPty();
        if (ptyResult == null || ptyResult.length < 2) {
            Log.e(TAG, "createPty() falló: posix_openpt no disponible en este kernel.");
            return false;
        }
        try {
            masterFd = Integer.parseInt(ptyResult[0]);
            slavePath = ptyResult[1];
        } catch (NumberFormatException e) {
            Log.e(TAG, "FD inválido retornado por createPty(): " + ptyResult[0]);
            return false;
        }
        Log.i(TAG, "PTY creado: masterFd=" + masterFd + " slavePath=" + slavePath);

        // 2. Abrir puerto USB-Serial con usb-serial-for-android
        List<UsbSerialDriver> drivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        UsbSerialDriver targetDriver = null;
        for (UsbSerialDriver d : drivers) {
            if (d.getDevice().getDeviceId() == device.getDeviceId()) {
                targetDriver = d;
                break;
            }
        }
        if (targetDriver == null || targetDriver.getPorts().isEmpty()) {
            Log.e(TAG, "No se encontró driver USB-Serial para el dispositivo: " + device.getDeviceName());
            cleanupPty();
            return false;
        }

        usbPort = targetDriver.getPorts().get(0);
        try {
            usbPort.open(connection);
            usbPort.setParameters(baudRate, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            // DTR/RTS en false para NO disparar Auto-Reset del Arduino
            // (DTR toggle = RESET en Arduino Uno con CH340/ATmega16U2)
            usbPort.setDTR(false);
            usbPort.setRTS(false);
        } catch (IOException e) {
            Log.e(TAG, "Error abriendo UsbSerialPort: " + e.getMessage());
            cleanupPty();
            return false;
        }

        // 3. Crear ParcelFileDescriptor desde el master FD (sin adoptarlo: dup primero)
        // adoptFd() cierra el FD cuando el PFD se cierra; nosotros lo cerramos en
        // closeFd()
        try {
            masterPfd = ParcelFileDescriptor.fromFd(masterFd);
        } catch (IOException e) {
            Log.e(TAG, "Error creando PFD desde master FD: " + e.getMessage());
            cleanupPort();
            cleanupPty();
            return false;
        }

        // 4. Iniciar hilos de forwarding
        running = true;
        startForwardingThreads();
        Log.i(TAG, "PtyBridge activo: flashrom usará " + slavePath + " a " + baudRate + " bps");
        return true;
    }

    /**
     * Devuelve el path del slave PTY para pasárselo a flashrom:
     * "-p serprog:dev=" + getSlavePath() + ":" + getBaudRate()
     */
    public String getSlavePath() {
        return slavePath;
    }

    public int getBaudRate() {
        return baudRate;
    }

    /**
     * Detiene los hilos y libera todos los recursos (PTY + puerto USB-serial).
     * Seguro llamarlo desde onDestroy() o cuando flashrom termina.
     */
    public void close() {
        running = false;

        // Interrumpir hilos
        if (threadMasterToUsb != null) {
            threadMasterToUsb.interrupt();
            threadMasterToUsb = null;
        }
        if (threadUsbToMaster != null) {
            threadUsbToMaster.interrupt();
            threadUsbToMaster = null;
        }

        cleanupPort();
        cleanupPfd();
        cleanupPty();
        Log.i(TAG, "PtyBridge cerrado.");
    }

    /** True si el puente está activo y listo. */
    public boolean isOpen() {
        return running && slavePath != null;
    }

    /**
     * Descarta toda la basura acumulada en el buffer USB y el PTY master.
     * Llamar ANTES de iniciar flashrom para que lo primero que lea sea
     * el ACK real del Arduino y no restos del bootloader o ruido del CH340.
     */
    public void purge() {
        // Drenar buffer USB (respuestas viejas / basura del bootloader)
        if (usbPort != null) {
            byte[] drain = new byte[BUFFER_SIZE];
            try {
                int total = 0;
                int n;
                while ((n = usbPort.read(drain, 10)) > 0) {
                    total += n;
                }
                if (total > 0) {
                    Log.i(TAG, "Purge USB: descartados " + total + " bytes de basura");
                }
            } catch (IOException ignored) {
            }
        }
        // Drenar buffer PTY master (bytes que ya pasaron del USB al PTY)
        if (masterPfd != null) {
            try {
                FileInputStream masterIn = new FileInputStream(masterPfd.getFileDescriptor());
                int avail = masterIn.available();
                if (avail > 0) {
                    byte[] skip = new byte[avail];
                    masterIn.read(skip);
                    Log.i(TAG, "Purge PTY: descartados " + avail + " bytes del master");
                }
            } catch (IOException ignored) {
            }
        }
    }

    // -------- Privados --------

    private void startForwardingThreads() {
        // Hilo A: PTY master → USB (lo que flashrom escribe al puerto serie virtual)
        threadMasterToUsb = new Thread(() -> {
            try (FileInputStream masterIn = new FileInputStream(masterPfd.getFileDescriptor())) {
                byte[] buf = new byte[BUFFER_SIZE];
                while (running && !Thread.currentThread().isInterrupted()) {
                    int n = masterIn.read(buf);
                    if (n > 0 && usbPort != null) {
                        try {
                            usbPort.write(buf, n, USB_TIMEOUT_MS);
                        } catch (IOException e) {
                            if (running)
                                Log.w(TAG, "Error escribiendo a USB: " + e.getMessage());
                        }
                    }
                }
            } catch (IOException e) {
                if (running)
                    Log.w(TAG, "Hilo master→USB terminado: " + e.getMessage());
            }
        }, "PtyBridge-master-to-usb");
        threadMasterToUsb.setDaemon(true);
        threadMasterToUsb.start();

        // Hilo B: USB → PTY master (respuestas del Arduino que flashrom lee)
        threadUsbToMaster = new Thread(() -> {
            try (FileOutputStream masterOut = new FileOutputStream(masterPfd.getFileDescriptor())) {
                byte[] buf = new byte[BUFFER_SIZE];
                while (running && !Thread.currentThread().isInterrupted()) {
                    try {
                        if (usbPort != null) {
                            int n = usbPort.read(buf, USB_TIMEOUT_MS);
                            if (n > 0) {
                                masterOut.write(buf, 0, n);
                                masterOut.flush();
                            }
                        }
                    } catch (IOException e) {
                        if (running)
                            Log.w(TAG, "Error leyendo USB: " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                if (running)
                    Log.w(TAG, "Hilo USB→master terminado: " + e.getMessage());
            }
        }, "PtyBridge-usb-to-master");
        threadUsbToMaster.setDaemon(true);
        threadUsbToMaster.start();
    }

    private void cleanupPort() {
        if (usbPort != null) {
            try {
                usbPort.close();
            } catch (IOException ignored) {
            }
            usbPort = null;
        }
        if (usbConnection != null) {
            usbConnection.close();
            usbConnection = null;
        }
    }

    private void cleanupPfd() {
        if (masterPfd != null) {
            try {
                masterPfd.close();
            } catch (IOException ignored) {
            }
            masterPfd = null;
        }
    }

    private void cleanupPty() {
        if (masterFd >= 0) {
            closeFd(masterFd);
            masterFd = -1;
        }
        slavePath = null;
    }
}
