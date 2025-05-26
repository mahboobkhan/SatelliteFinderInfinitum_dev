package com.example.satellitefinder.leveler.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.satellitefinder.R

class BubbleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var widthSize = 0
    private var heightSize = 0
    private var newX: Float = 0f

    private val backgroundDrawable =
        context?.let { ContextCompat.getDrawable(it, R.drawable.level_background) }
    private val bubbleDrawable =
        context?.let { ContextCompat.getDrawable(it, R.drawable.ic_level_bubble) }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthSize = w
        heightSize = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawVerticalCenterLines(canvas)
        drawBubble(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        /*val backgroundPaint = Paint().apply {
            color = Color.parseColor("#4B4B4B")
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val borderPaint = Paint().apply {
            color = Color.parseColor("#01D89F")
            style = Paint.Style.STROKE
            strokeWidth = 6f
            isAntiAlias = true
        }

        val radius = 40f
        val rect = RectF(0f, 0f, widthSize.toFloat(), heightSize.toFloat())

        // Fill background
        canvas.drawRoundRect(rect, radius, radius, backgroundPaint)
        // Green border with corners
        canvas.drawRoundRect(rect, radius, radius, borderPaint)*/

        backgroundDrawable?.setBounds(0, 0, widthSize, heightSize)
        backgroundDrawable?.draw(canvas)
    }

    private fun drawVerticalCenterLines(canvas: Canvas) {
        val linePaint = Paint().apply {
            color = Color.parseColor("#2D334B")
            strokeWidth = 5f
            isAntiAlias = true
        }

        val lineLength = heightSize
        val gap = 70f // space between lines

        val centerX = widthSize / 2f
        val top = 0f
        val bottom = (heightSize + lineLength) / 2f

        // Left vertical line
        canvas.drawLine(centerX - gap, top, centerX - gap, bottom, linePaint)
        // Right vertical line
        canvas.drawLine(centerX + gap, top, centerX + gap, bottom, linePaint)
    }

    private fun drawBubble(canvas: Canvas) {
        bubbleDrawable?.let { drawable ->
            val bubbleRadius = 100f // or calculate from drawable.intrinsicWidth / 2
            val halfWidth = widthSize * 0.5f
            val halfHeight = heightSize * 0.5f

            val angleClamped = newX.coerceIn(-90f, 90f)
            val maxAngle = 90f
            val normalized = angleClamped / maxAngle
            val bubbleX = normalized * (halfWidth - bubbleRadius)

            val left = (halfWidth + bubbleX - bubbleRadius).toInt()
            val top = (halfHeight - bubbleRadius).toInt()
            val right = (halfWidth + bubbleX + bubbleRadius).toInt()
            val bottom = (halfHeight + bubbleRadius).toInt()

            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        }
    }

    fun setValues(x: Float) {
        this.newX = x
        invalidate()
    }
}

