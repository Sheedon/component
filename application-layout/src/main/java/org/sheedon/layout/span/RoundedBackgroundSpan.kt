package org.sheedon.layout.span

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan
import kotlin.math.roundToInt


/**
 * TextView中tag，圆角背景Span
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2022/12/13 12:52
 */
class RoundedBackgroundSpan(
    val cornerRadius: Float = 8F,
    val backgroundColor: Int = 0,
    val textColor: Int = 0,
) : ReplacementSpan() {


    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        return paint.measureText(text, start, end).roundToInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val rect = RectF(
            x,
            top.toFloat(),
            x + measureText(paint, text ?: "", start, end),
            bottom.toFloat()
        )
        paint.color = backgroundColor
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
        paint.color = textColor
        canvas.drawText(text ?: "", start, end, x, y.toFloat(), paint)
    }

    private fun measureText(paint: Paint, text: CharSequence, start: Int, end: Int): Float {
        return paint.measureText(text, start, end)
    }
}