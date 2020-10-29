package com.kuznetsova.game

sealed class DrawableObject(val color: Int, var centerX: Float,var centerY: Float) {




    class Racket(val width: Float, val height: Float, color: Int, centerX: Float, centerY: Float) : DrawableObject(color, centerX,centerY){

    }
    class Ball(val radius: Float, color: Int, centerX: Float, centerY: Float) : DrawableObject(color, centerX,
        centerY
    ){

    }
}