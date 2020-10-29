package com.kuznetsova.game


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity() : AppCompatActivity()/*, SensorEventListener*/ {

   /* private lateinit var sensorManager: SensorManager
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val mappedRotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private lateinit var manager: Manager
*/

    val APP_PREFERENCES = "mysettings"
    var mSettings: SharedPreferences? = null
    var currentTheme: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        currentTheme = mSettings?.getString("Theme","")
        if(currentTheme.equals("") || currentTheme.equals(ThemeEnum.LIGHT.toString())){
            setTheme(R.style.AppTheme)
        }else{
            if(currentTheme.equals(ThemeEnum.DARK.toString())){
                setTheme(R.style.DarkTheme)
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



/*
        val gameFieldView: GameFieldView = GameFieldView(this)
        setContentView(gameFieldView)

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
                ContextCompat.getColor(this, R.color.racketColor),
                screenCenterX,
                racketCenterY
            )
            val ball: DrawableObject.Ball = DrawableObject.Ball(
                resources.getDimension(R.dimen.ball_radius),
                ContextCompat.getColor(this, R.color.ballColor),
                screenCenterX,
                screenCenterY
            )
            gameFieldView.drawableObjects.add(racket)

            gameFieldView.drawableObjects.add(ball)

            gameFieldView.invalidate()

            manager = Manager(gameFieldView, racket, ball)
            manager.start()
        }


        val tmp: SensorManager? = getSystemService() // nullpointer
        if (tmp != null) {
            sensorManager = tmp
        } else {
            return
        }*/


    }



/*    override fun onResume() {
        super.onResume()

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

        val (xAxis, yAxis) = when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> SensorManager.AXIS_X to SensorManager.AXIS_Y
            Surface.ROTATION_90 -> SensorManager.AXIS_Y to SensorManager.AXIS_MINUS_X
            Surface.ROTATION_180 -> SensorManager.AXIS_MINUS_X to SensorManager.AXIS_MINUS_Y
            else -> SensorManager.AXIS_MINUS_Y to SensorManager.AXIS_X
        }

        SensorManager.remapCoordinateSystem(rotationMatrix, xAxis, yAxis, mappedRotationMatrix)

        SensorManager.getOrientation(mappedRotationMatrix, orientationAngles)

        if (::manager.isInitialized) {
            manager.angelY = Math.toDegrees(orientationAngles[2].toDouble()).toFloat()
        }
    }*/
}