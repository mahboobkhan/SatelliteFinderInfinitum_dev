package com.example.satellitefinder.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast

internal class DeviceRotationManager(
    private val context: Context,
    private val onRotationChangedListener: OnRotationChangedListener
) : SensorEventListener {
    fun onStart() {
        registerSensorsListener()
    }

    fun onStop() {
        unregisterSensorsListener()
    }

    private var mSensorManager: SensorManager? = null
        private get() {
            if (field == null) field =
                context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            return field
        }
    private var rotationVectorSensor: Sensor? = null
        private get() {
            if (field == null) field = mSensorManager!!.getDefaultSensor(Sensor.TYPE_GRAVITY)
            return field
        }

    private fun registerSensorsListener() {
        var registered = false
        val sensorManager = mSensorManager
        if (sensorManager != null) {
            val rotationVectorSensor = rotationVectorSensor
            if (rotationVectorSensor != null) {
                sensorManager.registerListener(
                    this,
                    rotationVectorSensor,
                    SensorManager.SENSOR_DELAY_FASTEST
                )
                registered = true
            }
        }
        if (!registered) {
            Toast.makeText(context, "Sensor unavailable", Toast.LENGTH_SHORT).show()
        }
    }

    private fun unregisterSensorsListener() {
        val sensorManager = mSensorManager
        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_GRAVITY) return
        val x = event.values[0] / 10.0f
        val y = -event.values[1] / 10.0f
        onRotationChangedListener.onRotationChanged(x, y)
    }

    internal interface OnRotationChangedListener {
        fun onRotationChanged(x: Float, y: Float)
    }
}