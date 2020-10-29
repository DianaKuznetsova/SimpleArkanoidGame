package com.kuznetsova.game

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.core.content.ContentProviderCompat.requireContext

class GameFieldView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defaultStyle: Int = 0) :
    View(context, attrs, defaultStyle) {

    private val mPaint: Paint = Paint()
    val drawableObjects: MutableList<DrawableObject> = mutableListOf()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) {
            return
        }

        mPaint.style = Paint.Style.FILL
        mPaint.color = getThemeColor(context.theme, R.attr.gameFieldColor)
        canvas.drawPaint(mPaint)

        for (drawableObject in drawableObjects) {
            mPaint.color = drawableObject.color
            when (drawableObject) {
                is DrawableObject.Racket -> {
                    val rectX: Float = drawableObject.centerX - drawableObject.width / 2
                    val rectY: Float = drawableObject.centerY - drawableObject.height / 2
                    canvas.drawRect(
                        rectX,
                        rectY,
                        rectX + drawableObject.width,
                        rectY + drawableObject.height,
                        mPaint
                    )
                }
                is DrawableObject.Ball -> {
                    canvas.drawCircle(
                        drawableObject.centerX,
                        drawableObject.centerY,
                        drawableObject.radius,
                        mPaint
                    )
                }

            }
        }


    }

    fun getThemeColor(theme: Resources.Theme, @AttrRes attributeId: Int): Int {
        val value = TypedValue()
        theme.resolveAttribute(attributeId, value, true)
        return value.data
    }

}