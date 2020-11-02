package com.kuznetsova.game


import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import kotlinx.coroutines.*
import kotlin.math.cos
import kotlin.math.sin

abstract class BaseGameManager(
    protected val gameFieldView: GameFieldView
) {

    abstract val ballSpeed: Float

    private var gameLoop: Job? = null
    private var ballAngle: Double = 45 + Math.random() * 90
    protected lateinit var racket: DrawableObject.Racket
    protected lateinit var ball: DrawableObject.Ball

    private var isInitialized = false
    private var isStartRequested = false

    init {
        gameFieldView.post {
            initManager()
            isInitialized= true
            if(isStartRequested){
                start()
            }
        }
    }
    open fun initManager(){
        val screenCenterX: Float = gameFieldView.width / 2f
        val racketCenterY: Float =
            gameFieldView.height - gameFieldView.resources.getDimension(R.dimen.racket_bottom_margin) - gameFieldView.resources.getDimension(
                R.dimen.racket_height
            ) / 2f
        val screenCenterY: Float = gameFieldView.height / 2f

        racket = DrawableObject.Racket(
            gameFieldView.resources.getDimension(R.dimen.racket_width),
            gameFieldView.resources.getDimension(R.dimen.racket_height),
            getThemeColor(gameFieldView.context.theme, R.attr.racketColor),
            screenCenterX,
            racketCenterY
        )
        ball= DrawableObject.Ball(
            gameFieldView.resources.getDimension(R.dimen.ball_radius),
            getThemeColor(gameFieldView.context.theme, R.attr.ballColor),
            screenCenterX,
            screenCenterY
        )

        gameFieldView.drawableObjects.add(racket)
        gameFieldView.drawableObjects.add(ball)
        gameFieldView.invalidate()
    }

    open fun start() {
        isStartRequested=true
        if(!isInitialized) return
        if (gameLoop != null) return
        gameLoop = GlobalScope.launch {
            var latIterationTime = System.nanoTime()

            while (isActive) {
                val now = System.nanoTime()
                val timePassed = now - latIterationTime
                latIterationTime = now
                updateObject(timePassed)
            }
        }
    }

    open fun pause() = runBlocking {
        gameLoop?.cancel()
        gameLoop = null
    }

    open suspend fun updateObject(timePassed: Long) {


        val d = ballSpeed * timePassed / 10e6

        val ballAngleRadians = Math.toRadians(ballAngle)
        val dy: Float = (d * sin(ballAngleRadians)).toFloat()
        val dx: Float = (d * cos(ballAngleRadians)).toFloat()

        ball.centerX = ball.centerX + dx
        ball.centerY = ball.centerY + dy
        //боковые грани
        if (ball.centerX <= ball.radius && ballAngle >= 90 && ballAngle <= 270 ||
            (ball.centerX >= gameFieldView.width - ball.radius &&
                    (ballAngle >= 0 && ballAngle <= 90 || ballAngle >= 270 && ballAngle <= 360))) {
            ballAngle = 180 - ballAngle
        }
        //верх
        if (ball.centerY <= ball.radius && ballAngle >= 180 && ballAngle <= 360) {
                ballAngle = 360 - ballAngle

        }
        //низ
        if (ball.centerY >= racket.centerY - racket.height / 2 - ball.radius) {
            if (ballAngle >= 0 && ballAngle <= 180 && (racket.centerX - racket.width / 2 <= ball.centerX - ball.radius && racket.centerX + racket.width / 2 >= ball.centerX + ball.radius)) {
                ballAngle = 360 - ballAngle
                onBallHit()
            } else if (ball.centerY - ball.radius >= gameFieldView.height) {
                onGameLost()
                pause()
            }
        }

        if (ballAngle < 0) {
            ballAngle += 360
        } else if (ballAngle > 360) {
            ballAngle -= 360
        }

        withContext(Dispatchers.Main) {
            gameFieldView.invalidate()
        }
    }

    open fun onBallHit() {

    }

    open fun onGameLost() {

    }
    private fun getThemeColor(theme: Resources.Theme, @AttrRes attributeId: Int): Int {
        val value = TypedValue()
        theme.resolveAttribute(attributeId, value, true)
        return  value.data
    }


}