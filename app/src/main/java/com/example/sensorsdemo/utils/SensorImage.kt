package com.example.sensorsdemo.utils

import android.hardware.Sensor
import com.example.sensorsdemo.R

class SensorImage {
    fun getSensorImage(type: Int): Int {
        return when(type){
            Sensor.TYPE_ACCELEROMETER -> R.drawable.accelerometer_sensor
            Sensor.TYPE_ORIENTATION -> R.drawable.orientation_sensor
            Sensor.TYPE_GYROSCOPE -> R.drawable.gyroscope_sensor
            else -> {R.color.white}
        }
    }
}