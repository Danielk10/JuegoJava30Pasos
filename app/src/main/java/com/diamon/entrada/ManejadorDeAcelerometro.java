package com.diamon.entrada;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ManejadorDeAcelerometro implements SensorEventListener {

    private float x;

    private float y;

    private float z;

    public ManejadorDeAcelerometro(Context contexto) {

        SensorManager manejadorSensor =
                (SensorManager) contexto.getSystemService(Context.SENSOR_SERVICE);

        if (manejadorSensor.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {

            Sensor sensor = manejadorSensor.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);

            manejadorSensor.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int precicion) {}

    @Override
    public void onSensorChanged(SensorEvent eventoDeSensor) {

        x = eventoDeSensor.values[0];

        y = eventoDeSensor.values[1];

        z = eventoDeSensor.values[2];
    }

    public float getAcelerometroEnX() {
        return x;
    }

    public float getAcelerometroEnY() {
        return y;
    }

    public float getAcelerometroEnZ() {
        return z;
    }
}
