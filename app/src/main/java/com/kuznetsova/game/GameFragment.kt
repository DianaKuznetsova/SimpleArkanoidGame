package com.kuznetsova.game

import android.content.res.Resources
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GameFragment : Fragment(), SensorEventListener, GameManager.OnGameEventListener {

    private lateinit var sensorManager: SensorManager
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val mappedRotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private val kalmanFilter: KalmanFilter = KalmanFilter(1.0, 1.0, 2.0, 10.0)

    private lateinit var gameManager: GameManager
    private lateinit var levelTV: TextView
    private var level: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tmp: SensorManager? = requireContext().getSystemService() // nullpointer
        if (tmp != null) {
            sensorManager = tmp
        } else {
            return
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return return inflater.inflate(R.layout.game_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        levelTV = requireView().findViewById<TextView>(R.id.level)
        levelTV.text = getString(R.string.start_level, level)
        val gameFieldView: GameFieldView = view.findViewById(R.id.gameFieldView)
        gameFieldView.post {

            val screenCenterX: Float = gameFieldView.width / 2f
            val racketCenterY: Float =
                gameFieldView.height - resources.getDimension(R.dimen.racket_bottom_margin) - resources.getDimension(
                    R.dimen.racket_height
                ) / 2f
            val screenCenterY: Float = gameFieldView.height / 2f

            val racket: DrawableObject.Racket = DrawableObject.Racket(
                resources.getDimension(R.dimen.racket_width),
                resources.getDimension(R.dimen.racket_height),
                getThemeColor(requireContext().theme, R.attr.racketColor),
                screenCenterX,
                racketCenterY
            )
            val ball: DrawableObject.Ball = DrawableObject.Ball(
                resources.getDimension(R.dimen.ball_radius),
                getThemeColor(requireContext().theme, R.attr.ballColor),
                screenCenterX,
                screenCenterY
            )
            gameFieldView.drawableObjects.add(racket)

            gameFieldView.drawableObjects.add(ball)

            gameFieldView.invalidate()

            gameManager = GameManager(this, gameFieldView, racket, ball)
            gameManager.start()
        }

        kalmanFilter.setState(0.0, 0.1)

    }

    private fun getThemeColor(theme: Resources.Theme, @AttrRes attributeId: Int): Int {
        val value = TypedValue()
        theme.resolveAttribute(attributeId, value, true)
        return value.data
    }

    override fun onResume() {
        super.onResume()

        if (this::gameManager.isInitialized) {
            gameManager.start()
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_UI,
                SensorManager.SENSOR_DELAY_UI
            )
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensorManager.registerListener(
                this,
                magneticField,
                SensorManager.SENSOR_DELAY_UI,
                SensorManager.SENSOR_DELAY_UI
            )

        }
    }


    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        gameManager.pause()

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

        val (xAxis, yAxis) = when (requireActivity().windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> SensorManager.AXIS_X to SensorManager.AXIS_Y
            Surface.ROTATION_90 -> SensorManager.AXIS_Y to SensorManager.AXIS_MINUS_X
            Surface.ROTATION_180 -> SensorManager.AXIS_MINUS_X to SensorManager.AXIS_MINUS_Y
            else -> SensorManager.AXIS_MINUS_Y to SensorManager.AXIS_X
        }

        SensorManager.remapCoordinateSystem(rotationMatrix, xAxis, yAxis, mappedRotationMatrix)

        SensorManager.getOrientation(mappedRotationMatrix, orientationAngles)

        val rawAngle = Math.toDegrees(orientationAngles[2].toDouble())
        kalmanFilter.correct(rawAngle)
        if (::gameManager.isInitialized) {
            gameManager.angelY = kalmanFilter.state.toFloat()
        }
    }


    override fun onGameLost() {
        findNavController().navigate(R.id.action_gameFragment_to_gameOverFragment)

    }

    override fun onLevelChange() {
        level++
        GlobalScope.launch(Dispatchers.Main) {
            levelTV.text = getString(R.string.start_level, level)
        }
    }
}