package com.example.satellitefinder.utils.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.roundToInt
import kotlin.math.sin

class GonioView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    private var lock: Boolean = false

    private val handlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 6f
        color = Color.parseColor("#01D89F") // Cyan fill
    }

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 6f
        color = Color.WHITE // White stroke
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#01D89F")
        strokeWidth = 8f
    }

    private val anglePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        textSize = 48f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val handleRadius = 30f
    private var handles = arrayOf(
        PointF(300f, 500f),  // Top point
        PointF(300f, 800f),  // Middle point
        PointF(600f, 800f)   // Bottom point (Right)
    )

    private var currentHandle: PointF? = null
    private var listener: (() -> Unit)? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw lines
        canvas.drawLine(handles[0].x, handles[0].y, handles[1].x, handles[1].y, linePaint)
        canvas.drawLine(handles[1].x, handles[1].y, handles[2].x, handles[2].y, linePaint)

        // Draw angle arc
        drawAngleArc(canvas)

        // Draw angle text


        // Draw handle points
        handles.forEach {
            canvas.drawCircle(it.x, it.y, handleRadius, handlePaint)
            canvas.drawCircle(it.x, it.y, handleRadius, strokePaint)
        }
    }

    private fun drawAngleArc(canvas: Canvas) {
        val a = handles[0]  // First point
        val b = handles[1]  // Vertex point
        val c = handles[2]  // Third point

        val ab = PointF(a.x - b.x, a.y - b.y)
        val cb = PointF(c.x - b.x, c.y - b.y)

        val angleAB = atan2(ab.y, ab.x).toDouble()
        val angleCB = atan2(cb.y, cb.x).toDouble()

        // Start angle for arc (converted to degrees)
        var startAngle = Math.toDegrees(angleCB).toFloat()
        var sweepAngle = Math.toDegrees(angleAB - angleCB).toFloat()

        // Normalize sweep angle
        if (sweepAngle < 0) sweepAngle += 360f
        var useReflex = false
        if (sweepAngle > 180f) {
            startAngle = Math.toDegrees(angleAB).toFloat()
            sweepAngle = 360f - sweepAngle
            useReflex = true
        }

        // Arc radius and bounding box
        val radius = 80f
        val rect = RectF(
            b.x - radius,
            b.y - radius,
            b.x + radius,
            b.y + radius
        )

        // Mid angle for text position
        val startDeg = if (useReflex) Math.toDegrees(angleAB) else Math.toDegrees(angleCB)
        val midAngleDeg = (startDeg + sweepAngle / 2.0) % 360
        val midAngleRad = Math.toRadians(midAngleDeg)

        val textRadius = 140f
        val textX = b.x + cos(midAngleRad).toFloat() * textRadius
        val textY = b.y + sin(midAngleRad).toFloat() * textRadius

        // Draw the arc
        canvas.drawArc(rect, startAngle, sweepAngle, false, anglePaint)

        // Draw the angle text centered at that point
        val angle = getCurrentAngle()
        val text = "$angle°"
        val textWidth = textPaint.measureText(text)
        val textHeight = textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent
        canvas.drawText(text, textX - textWidth / 2, textY + textHeight / 2, textPaint)
    }

    private fun getCurrentAngle(): Int {
        val a = handles[0]
        val b = handles[1]
        val c = handles[2]

        val ab = PointF(a.x - b.x, a.y - b.y)
        val cb = PointF(c.x - b.x, c.y - b.y)

        val dot = ab.x * cb.x + ab.y * cb.y
        val cross = ab.x * cb.y - ab.y * cb.x

        val angle = Math.toDegrees(atan2(cross.toDouble(), dot.toDouble()))
        return abs(angle).roundToInt()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (lock) return false // Ignore touch if locked
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentHandle = handles.find { point ->
                    hypot(point.x - event.x, point.y - event.y) <= handleRadius * 2
                }
                return currentHandle != null
            }

            MotionEvent.ACTION_MOVE -> {
                currentHandle?.apply {
                    x = event.x.coerceIn(0f, width.toFloat())
                    y = event.y.coerceIn(0f, height.toFloat())
                }
                invalidate()
                return true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                currentHandle = null
                listener?.invoke()
                return true
            }
        }
        return false
    }

    fun setOnAngleSelectedListener(callback: () -> Unit) {
        listener = callback
    }


    fun setLock(isLocked: Boolean) {
        lock = isLocked
    }
}