package com.kuznetsova.game

import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.*
import kotlin.math.cos
import kotlin.math.sin

abstract class BasaGameManager(
    val gameFieldView: GameFieldView,
    val racket: DrawableObject.Racket,
    val ball: DrawableObject.Ball
) {

    abstract val ballSpeed: Float

    private var gameLoop: Job? = null
    private var ballAngle: Double = 45 + Math.random() * 90


    open fun start() {

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
        try {
            gameLoop?.cancel()
            gameLoop = null
        } catch (e: Exception) {
            Log.e("BaseGameManager", "pause: ", e)
        }
    }

    open suspend fun updateObject(timePassed: Long) {


        val d = ballSpeed * timePassed / 10e6

        val ballAngleRadians = Math.toRadians(ballAngle)
        val dy: Float = (d * sin(ballAngleRadians)).toFloat()
        val dx: Float = (d * cos(ballAngleRadians)).toFloat()

        //левая
        if (ball.centerX + dx <= ball.radius) {
            if (ballAngle >= 90 && ballAngle <= 270)
                ballAngle = 180 - ballAngle
        } //правая
        if (ball.centerX + dx >= gameFieldView.width - ball.radius) {
            if (ballAngle >= 0 && ballAngle <= 90 || ballAngle >= 270 && ballAngle <= 360)
                ballAngle = 180 - ballAngle

        }
        //верх
        if (ball.centerY + dy <= ball.radius) {
            if (ballAngle >= 180 && ballAngle <= 360)
                ballAngle = 360 - ballAngle

        }
        //низ
        if (ball.centerY + dy >= racket.centerY - racket.height / 2 - ball.radius) {
            if (ballAngle >= 0 && ballAngle <= 180 && (racket.centerX - racket.width / 2 <= ball.centerX - ball.radius && racket.centerX + racket.width / 2 >= ball.centerX + ball.radius)) {
                ballAngle = 360 - ballAngle
                onBallHit()
            } else if (ball.centerY - ball.radius >= gameFieldView.height) {
               /* withContext(Dispatchers.Main) {
                    Toast.makeText(gameFieldView.context, "You lose", Toast.LENGTH_LONG).show()

                }*/
                onGameLost()
                pause()
            }
        }


        ball.centerX = ball.centerX + dx
        ball.centerY = ball.centerY + dy



        if (ballAngle < 0) {
            ballAngle += 360
        } else if (ballAngle > 360) {
            ballAngle -= 360
        }

         withContext(Dispatchers.Main) {
            gameFieldView.invalidate()
        }
    }

    open fun onBallHit(){

    }

    open fun onGameLost(){

    }


}