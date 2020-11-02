package com.kuznetsova.game

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Surface
import androidx.core.content.getSystemService

class DeviceRotationManager(
    private val activity: Activity,
    private val listener: DeviceRotationListener
) : SensorEventListener {
    private var sensorManager: SensorManager? = activity.getSystemService()
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val mappedRotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)
    private val kalmanFilter: KalmanFilter = KalmanFilter(1.0, 1.0, 2.0, 10.0)
    private var isStarted: Boolean = false

    init {
        kalmanFilter.setState(0.0, 0.1)
    }

    interface DeviceRotationListener {
        fun onRotationChange(angle: Float)
    }

    fun start() {
        if(isStarted) return
        sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager?.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_UI,
                SensorManager.SENSOR_DELAY_UI
            )
        }

        sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensorManager?.registerListener(
                this,
                magneticField,
                SensorManager.SENSOR_DELAY_UI,
                SensorManager.SENSOR_DELAY_UI
            )

        }
        isStarted = true
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }
        updateOrientationAngles()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    private fun updateOrientationAngles() {
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )

        val (xAxis, yAxis) = when (activity.windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> SensorManager.AXIS_X to SensorManager.AXIS_Y
            Surface.ROTATION_90 -> SensorManager.AXIS_Y to SensorManager.AXIS_MINUS_X
            Surface.ROTATION_180 -> SensorManager.AXIS_MINUS_X to SensorManager.AXIS_MINUS_Y
            else -> SensorManager.AXIS_MINUS_Y to SensorManager.AXIS_X
        }

        SensorManager.remapCoordinateSystem(rotationMatrix, xAxis, yAxis, mappedRotationMatrix)

        SensorManager.getOrientation(mappedRotationMatrix, orientationAngles)

        val rawAngle = Math.toDegrees(orientationAngles[2].toDouble())
        kalmanFilter.correct(rawAngle)
        listener.onRotationChange(kalmanFilter.state.toFloat())
    }

    fun pause() {
        sensorManager?.unregisterListener(this)
        isStarted = false
    }
}