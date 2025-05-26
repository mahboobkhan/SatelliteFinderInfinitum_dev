package com.example.satellitefinder.leveler.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.satellitefinder.R

class VerticalBubbleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var widthSize = 0
    private var heightSize = 0
    private var newY: Float = 0f // Vertical movement

    private val backgroundDrawable = context?.let { ContextCompat.getDrawable(it, R.drawable.level_background) }
    private val bubbleDrawable = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_level_bubble) }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthSize = w
        heightSize = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawHorizontalCenterLines(canvas)
        drawBubble(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        backgroundDrawable?.setBounds(0, 0, widthSize, heightSize)
        backgroundDrawable?.draw(canvas)
    }

    private fun drawHorizontalCenterLines(canvas: Canvas) {
        val linePaint = Paint().apply {
            color = Color.parseColor("#2D334B")
            strokeWidth = 5f
            isAntiAlias = true
        }

        val lineLength = widthSize
        val gap = 70f // space between lines

        val centerY = heightSize / 2f
        val left = 0f
        val right = widthSize.toFloat()

        // Top horizontal line
        canvas.drawLine(left, centerY - gap, right, centerY - gap, linePaint)
        // Bottom horizontal line
        canvas.drawLine(left, centerY + gap, right, centerY + gap, linePaint)
    }

    private fun drawBubble(canvas: Canvas) {
        bubbleDrawable?.let { drawable ->
            val bubbleRadius = 100f
            val halfWidth = widthSize * 0.5f
            val halfHeight = heightSize * 0.5f

            val angleClamped = newY.coerceIn(-90f, 90f)
            val maxAngle = 90f
            val normalized = angleClamped / maxAngle
            val bubbleY = normalized * (halfHeight - bubbleRadius)

            val left = (halfWidth - bubbleRadius).toInt()
            val top = (halfHeight + bubbleY - bubbleRadius).toInt()
            val right = (halfWidth + bubbleRadius).toInt()
            val bottom = (halfHeight + bubbleY + bubbleRadius).toInt()

            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        }
    }

    fun setValues(y: Float) {
        this.newY = y
        invalidate()
    }
}