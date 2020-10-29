package com.kuznetsova.game

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GameManager(
    private val listener: GameManager.OnGameEventListener, gameFieldView: GameFieldView,
    racket: DrawableObject.Racket,
    ball: DrawableObject.Ball) : BasaGameManager(gameFieldView, racket, ball){


    interface OnGameEventListener {
        fun onGameLost()
        fun onLevelChange()
    }


    override var ballSpeed: Float = gameFieldView.resources.getDimension(R.dimen.start_ball_speed)
    private var ballHitCount: Int = 0


    /**
     * нормализованный угол от 0 до 1 ракетки
     * угол поворота устройства
     */
    var angelY: Float = 0f
        set(value) {
            val constrainedValue = when {
                value < MIN_ANGLE -> MIN_ANGLE
                value > MAX_ANGLE -> MAX_ANGLE
                else -> value
            }
            field = (constrainedValue - MIN_ANGLE) / ANGLES_RANGE
        }


    override suspend fun updateObject(timePassed: Long) {

        val racketCenterX: Float = racket.width / 2 + (gameFieldView.width - racket.width) * angelY
        racket.centerX = racketCenterX
        super.updateObject(timePassed)

    }

    override fun onBallHit() {
        ballHitCount++
        if(ballHitCount% BALL_HIT_TO_SPEED_CHANGE==0){
            ballSpeed = ballSpeed*1.5f
            listener.onLevelChange()

        }
    }

    override fun onGameLost() {
        GlobalScope.launch(Dispatchers.Main){listener.onGameLost()}
    }


    companion object {
        const val BALL_HIT_TO_SPEED_CHANGE = 5
        const val MIN_ANGLE = -20f
        const val MAX_ANGLE = 20f
        const val ANGLES_RANGE: Float = MAX_ANGLE - MIN_ANGLE

    }
}

