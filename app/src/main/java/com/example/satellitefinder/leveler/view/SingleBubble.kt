package com.example.satellitefinder.leveler.view

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class SingleBubble(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var widthSize = 0
    private var heightSize = 0
    private var newX: Float = 0f

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
        val backgroundPaint = Paint().apply {
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
        canvas.drawRoundRect(rect, radius, radius, borderPaint)
    }

    private fun drawVerticalCenterLines(canvas: Canvas) {
        val linePaint = Paint().apply {
            color = Color.LTGRAY
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
        val bubblePaint = Paint().apply {
            color = Color.parseColor("#01D89F")
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val bubbleRadius = 60f
        val halfWidth = widthSize * 0.5f
        val halfHeight = heightSize * 0.5f

        // Clamp angle input to ±90° and map to full width range
        val angleClamped = newX.coerceIn(-90f, 90f)

        // Map angle -90 to 90 => position from left to right
        val maxAngle = 90f
        val normalized = angleClamped / maxAngle // range: -1 to 1
        val bubbleX = normalized * (halfWidth - bubbleRadius) // stay inside bounds

        canvas.save()
        canvas.translate(halfWidth, halfHeight) // move to center
        canvas.drawCircle(bubbleX, 0f, bubbleRadius, bubblePaint)
        canvas.restore()
    }

    fun setValues(x: Float) {
        this.newX = x
        invalidate()
    }
}
