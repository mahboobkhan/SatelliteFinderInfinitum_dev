package com.example.satellitefinder.utils.custom_views

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Display
import android.view.Surface

class OrientationManager(private val context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val rotationMatrix = FloatArray(9)
    private val remappedRotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)
    private val rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    var azimuth = 0.0
    var pitch = 0.0
    var roll = 0.0

    private var displayRotation: Int = Surface.ROTATION_0

    fun setDisplayRotation(rotation: Int) {
        displayRotation = rotation
    }

    fun start() {
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_UI)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            // Remap coordinate system based on display rotation
            val (axisX, axisY) = when (displayRotation) {
                Surface.ROTATION_0 -> Pair(SensorManager.AXIS_X, SensorManager.AXIS_Z)
                Surface.ROTATION_90 -> Pair(SensorManager.AXIS_Z, SensorManager.AXIS_MINUS_X)
                Surface.ROTATION_180 -> Pair(SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Z)
                Surface.ROTATION_270 -> Pair(SensorManager.AXIS_MINUS_Z, SensorManager.AXIS_X)
                else -> Pair(SensorManager.AXIS_X, SensorManager.AXIS_Z)
            }
            SensorManager.remapCoordinateSystem(rotationMatrix, axisX, axisY, remappedRotationMatrix)
            SensorManager.getOrientation(remappedRotationMatrix, orientationAngles)
            azimuth = Math.toDegrees(orientationAngles[0].toDouble())
            pitch = Math.toDegrees(orientationAngles[1].toDouble())
            roll = Math.toDegrees(orientationAngles[2].toDouble())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}