package com.example.sensorsdemo.utils

import com.example.sensorsdemo.R

class SensorImage {
    fun getSensorImage(type: Int): Int {
        return when(type){
            1 -> R.drawable.accelerometer_sensor
            3 -> R.drawable.orientation_sensor
            4 -> R.drawable.gyroscope_sensor
            else -> {R.color.white}
        }
    }
}