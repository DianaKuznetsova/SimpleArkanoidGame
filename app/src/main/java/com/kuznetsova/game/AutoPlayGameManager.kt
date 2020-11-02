package com.kuznetsova.game

class AutoPlayGameManager(
    gameFieldView: GameFieldView
) : BaseGameManager(gameFieldView) {


    override val ballSpeed: Float = gameFieldView.resources.getDimension(R.dimen.auto_play_ball_speed)


    override suspend fun updateObject(timePassed: Long) {

        val racketCenterX: Float = when {
            ball.centerX <= racket.width / 2 -> racket.width / 2
            ball.centerX >= gameFieldView.width - racket.width / 2 -> gameFieldView.width - racket.width / 2
            else -> ball.centerX
        }
        racket.centerX = racketCenterX

        super.updateObject(timePassed)
    }


}