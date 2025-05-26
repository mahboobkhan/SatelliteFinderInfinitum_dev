package com.example.satellitefinder.leveler.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.satellitefinder.R
import kotlin.math.hypot
import kotlin.math.min

class TwoDBubbleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var widthSize = 0
    private var heightSize = 0
    private var newX: Float = 0f
    private var newY: Float = 0f

    private val backgroundDrawable = context?.let {
        ContextCompat.getDrawable(it, R.drawable.level_2d_background)
    }
    private val bubbleDrawable = context?.let {
        ContextCompat.getDrawable(it, R.drawable.ic_level_bubble)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthSize = w
        heightSize = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawBubble(canvas)
        drawCircleGuide(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        backgroundDrawable?.setBounds(0, 0, widthSize, heightSize)
        backgroundDrawable?.draw(canvas)
    }

    private fun drawBubble(canvas: Canvas) {
        bubbleDrawable?.let { drawable ->
            val halfWidth = widthSize / 2f
            val halfHeight = heightSize / 2f
            val bubbleRadius = min(widthSize, heightSize) * 0.08f  // 8% of the smaller size

            // Convert tilt to normalized values
            val clampedX = newX.coerceIn(-90f, 90f)
            val clampedY = newY.coerceIn(-90f, 90f)
            val maxAngle = 90f

            val normX = clampedX / maxAngle // -1 to 1
            val normY = clampedY / maxAngle

            // Define max bubble offset inside circle
            val maxOffset = (min(widthSize, heightSize) / 2f) - bubbleRadius

            var bubbleX = normX * maxOffset
            var bubbleY = normY * maxOffset

            // Clamp to circle: bubble must not exceed radius
            val distance = hypot(bubbleX, bubbleY)
            if (distance > maxOffset) {
                val scale = maxOffset / distance
                bubbleX *= scale
                bubbleY *= scale
            }

            // Compute position relative to center
            val left = (halfWidth + bubbleX - bubbleRadius).toInt()
            val top = (halfHeight + bubbleY - bubbleRadius).toInt()
            val right = (halfWidth + bubbleX + bubbleRadius).toInt()
            val bottom = (halfHeight + bubbleY + bubbleRadius).toInt()

            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        }
    }

    private fun drawCircleGuide(canvas: Canvas) {
        val centerX = widthSize / 2f
        val centerY = heightSize / 2f
        val radius = 100f // small center circle

        // Paint for circle
        val circlePaint = Paint().apply {
            color = Color.parseColor("#2D334B")
            style = Paint.Style.STROKE
            strokeWidth = 5f
            isAntiAlias = true
        }

        // Draw small center circle
        canvas.drawCircle(centerX, centerY, radius, circlePaint)

        // Paint for lines
        val linePaint = Paint().apply {
            color = Color.parseColor("#2D334B")
            strokeWidth = 4f
            isAntiAlias = true
        }

        // Draw vertical full-height line
        canvas.drawLine(centerX, 0f, centerX, heightSize.toFloat(), linePaint)

        // Draw horizontal full-width line
        canvas.drawLine(0f, centerY, widthSize.toFloat(), centerY, linePaint)
    }


    fun setValues(x: Float, y: Float) {
        this.newX = x
        this.newY = y
        invalidate()
    }
}