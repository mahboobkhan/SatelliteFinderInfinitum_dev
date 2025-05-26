package com.example.satellitefinder.utils.custom_views
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat.setLayerType
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class ProtractorView @JvmOverloads constructor(
context: Context,
attrs: AttributeSet? = null
) : View(context, attrs) {

    private val backgroundColor = Color.parseColor("#0A0F2C")
    private val greenColor = Color.parseColor("#00FFB2")
    private val whiteColor = Color.WHITE
    private val grayColor = Color.LTGRAY

    private val center = PointF()
    private var radius = 0f

    var angleA = 12.2f
    var angleB = 90f

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        strokeWidth = 6f
        style = Paint.Style.STROKE
    }

    private val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = grayColor
        strokeWidth = 4f
    }

    private val textPaintRightGreen = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = greenColor
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }

    private val textPaintRightWhite = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = whiteColor
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }

    private val textPaintLeft = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = grayColor
        textSize = 28f
        textAlign = Paint.Align.CENTER
    }

    private val largeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = whiteColor
        textSize = 64f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
    }

    private val linePaintA = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = greenColor
        strokeWidth = 5f
        setShadowLayer(10f, 0f, 0f, greenColor)
    }

    private val linePaintB = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = whiteColor
        strokeWidth = 5f
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        setBackgroundColor(backgroundColor)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /*center.set(width / 2f, height.toFloat() - 40f)
        radius = min(width, height) / 2f - 60f*/
        val padding = 60f
        radius = height  - 200f  // Use height for a safe top arc fit
        center.set(width / 2f, height.toFloat() - padding)

        val oval = RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius)
        canvas.drawArc(oval, 180f, 180f, false, arcPaint)

        for (i in 0..180) {
            val angleRad = Math.toRadians(i.toDouble())
            val cosVal = cos(angleRad).toFloat()
            val sinVal = sin(angleRad).toFloat()
            val tickLen = if (i % 10 == 0) 50f else if (i % 5 == 0) 30f else 20f
            val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = grayColor
                strokeWidth = if (i % 10 == 0) 4f else 2f
            }
            val startX = center.x + (radius - tickLen) * cosVal
            val startY = center.y - (radius - tickLen) * sinVal
            val endX = center.x + radius * cosVal
            val endY = center.y - radius * sinVal

            canvas.drawLine(startX, startY, endX, endY, tickPaint)

            if (i % 10 == 0) {
                val labelRadius = radius - 80f
                val labelLeftRadius = radius - 130f


                // Right side labels
                val textXRight = center.x + labelRadius * cosVal
                val textYRight = center.y - labelRadius * sinVal + 12f
                val paintRight = if ((i / 10) % 2 == 0) textPaintRightGreen else textPaintRightWhite
                canvas.drawText("$i", textXRight, textYRight, paintRight)

                // Left side mirror labels
                val mirrorAngle = 180 - i
                val mirrorRad = Math.toRadians(mirrorAngle.toDouble())
                val mirrorCos = cos(mirrorRad).toFloat()
                val mirrorSin = sin(mirrorRad).toFloat()

                val textXLeft = center.x + labelLeftRadius * mirrorCos
                val textYLeft = center.y - labelLeftRadius * mirrorSin + 12f
                canvas.drawText("$i", textXLeft, textYLeft, textPaintLeft)
            }
        }

        // Draw angle lines
        drawLineAtAngle(canvas, angleA, linePaintA)
        drawLineAtAngle(canvas, angleB, linePaintB)

        // Show angle between lines
        val betweenAngle = getAngleBetween(angleA, angleB)
        canvas.drawText("${"%.1f".format(betweenAngle)}°", center.x, center.y  - 30, largeTextPaint)

        // Show individual angles at top corners
        canvas.drawText("${"%.1f".format(angleA)}°",width - 80f, 80f, textPaintRightGreen)               // left top
        canvas.drawText("${"%.1f".format(angleB)}°",  80f, 80f, textPaintRightWhite) // right top
    }

    private fun drawLineAtAngle(canvas: Canvas, angleDeg: Float, paint: Paint) {
        val rad = Math.toRadians(angleDeg.toDouble())
        /*
        val endX = center.x + radius * cos(rad).toFloat()
        val endY = center.y - radius * sin(rad).toFloat()*/

        val lineLength = max(width, height).toFloat()  // or use a fixed large length
        val endX = center.x + lineLength * cos(rad).toFloat()
        val endY = center.y - lineLength * sin(rad).toFloat()
        canvas.drawLine(center.x, center.y, endX, endY, paint)


    }

    private fun getAngleBetween(a: Float, b: Float): Float {
        val diff = abs(a - b) % 360
        return if (diff > 180) 360 - diff else diff
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val dx = event.x - center.x
        val dy = center.y - event.y
        val angle = (atan2(dy, dx) * 180 / Math.PI).toFloat().let {
            if (it < 0) it + 360 else it
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val distA = abs(angle - angleA)
                val distB = abs(angle - angleB)
                if (distA < distB) {
                    angleA = angle
                } else {
                    angleB = angle
                }
                invalidate()
            }
        }
        return true
    }
}