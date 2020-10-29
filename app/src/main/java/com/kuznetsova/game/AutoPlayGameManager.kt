package com.kuznetsova.game

import android.widget.Toast
import kotlinx.coroutines.*
import kotlin.math.cos
import kotlin.math.sin

class AutoPlayGameManager(
    gameFieldView: GameFieldView,
    racket: DrawableObject.Racket,
    ball: DrawableObject.Ball
): BasaGameManager(gameFieldView, racket, ball) {


    override val ballSpeed: Float = gameFieldView.resources.getDimension(R.dimen.auto_play_ball_speed)


   override suspend fun updateObject(timePassed: Long) {

        val racketCenterX: Float = when {
            ball.centerX <= racket.width/2 -> racket.width/2
            ball.centerX >= gameFieldView.width - racket.width/2 -> gameFieldView.width - racket.width/2
            else -> ball.centerX
        }
        racket.centerX = racketCenterX

        super.updateObject(timePassed)
    }


}