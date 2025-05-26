package com.example.satellitefinder.utils.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.max
import kotlin.math.min

class OneDimensionRulerView : View {

    companion object {
        const val UpperSection = 1
        const val LowerSection = 2
    }

    private val colorPaintMask = Paint(Paint.ANTI_ALIAS_FLAG)
    private val grayPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    private var upperY: Float = 0f
    private var lowerY: Float = 1f

    private val minDistance = dpTOpx(10f)

    var markCmWidth = dpTOpx(20f)
    var markHalfCmWidth = dpTOpx(15f)
    var markMmWidth = dpTOpx(10f)

    private var currentSection = 0
    private var pointerY = 0f

    var coefficient = 1f
        set(value) {
            field = value
            invalidate()
        }

    private val backgroundPaint = Paint()
    private val textSizePx = dpTOpx(12f)

    private var unit: RulerUnit = RulerUnit.CM

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        grayPaint.color = Color.WHITE
        grayPaint.strokeWidth = dpTOpx(1f)

        textPaint.color = Color.WHITE
        textPaint.textSize = textSizePx
        textPaint.textAlign = Paint.Align.LEFT

        colorPaintMask.color = Color.BLACK
        colorPaintMask.alpha = 120  // semi-transparent
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        upperY = h * .3f
        lowerY = h * .7f

        // Vertical gradient background
        backgroundPaint.shader = LinearGradient(
            0f, 0f, 0f, h.toFloat(),
            Color.parseColor("#1468E6"),
            Color.parseColor("#188CEB"),
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw background gradient
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        drawRulerInInches(canvas)
        drawRulerInCm(canvas)

        // Transparent mask above and below selected area
        canvas.drawRect(0f, 0f, width.toFloat(), upperY, colorPaintMask)
        canvas.drawRect(0f, lowerY, width.toFloat(), height.toFloat(), colorPaintMask)

        drawDistanceValue(canvas)
    }

    private fun drawRulerInInches(canvas: Canvas) {
        val inchPx = RulerUnit.inToPx(1f, resources.displayMetrics)
        val tickInterval = inchPx / 10f  // 0.1 inch

        var i = 0
        while (true) {
            val y = i * tickInterval
            if (y > height) break

            val tickNumber = i % 10

            val lineLength = when {
                tickNumber == 0 -> markCmWidth           // full inch
                tickNumber == 5 -> markHalfCmWidth       // 0.5 inch
                tickNumber == 2 || tickNumber == 8 -> dpTOpx(12f) // 0.2 / 0.8
                else -> markMmWidth                      // all others
            }

            canvas.drawLine(0f, y, lineLength, y, grayPaint)

            // Only label full inch
            if (tickNumber == 0) {
                val inchValue = i / 10
                canvas.drawText(
                    "$inchValue in",
                    lineLength + dpTOpx(2f),
                    y + textSizePx / 2,
                    textPaint
                )
            }

            i++
        }
    }

    private fun drawRulerInCm(canvas: Canvas) {
        val oneMmInPx = RulerUnit.mmToPx(1f, coefficient, resources.displayMetrics)

        for (i in 1..1000) {
            val y = oneMmInPx * i
            if (y > height) break

            val markWidth = when {
                i % 10 == 0 -> markCmWidth
                i % 5 == 0 -> markHalfCmWidth
                else -> markMmWidth
            }

            canvas.drawLine(width.toFloat(), y, width - markWidth, y, grayPaint)

            if (i % 10 == 0) {
                val cmValue = i / 10
                canvas.drawText(
                    "$cmValue cm",
                    width - markWidth - dpTOpx(30f),
                    y + textSizePx / 2,
                    textPaint
                )
            }
        }
    }

    private fun drawDistanceValue(canvas: Canvas) {
        val distancePx = Math.abs(lowerY - upperY)
        val distanceInInches = RulerUnit.pxToIn(
            distancePx,
            coefficient,
            resources.displayMetrics
        )
        val displayValue = unit.getUnitString(distanceInInches)
        val centerY = (upperY + lowerY) / 2

        val distanceTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = dpTOpx(16f)
            color = Color.WHITE
        }



        canvas.drawText(
            displayValue,
            width * 0.5f,
            centerY + textSizePx / 2,
            distanceTextPaint
        )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val centerPoint = (lowerY + upperY) / 2
                currentSection = when {
                    event.y < centerPoint -> UpperSection
                    event.y > centerPoint -> LowerSection
                    else -> 0
                }
                pointerY = event.y
                return currentSection != 0
            }

            MotionEvent.ACTION_MOVE -> {
                val dy = event.y - pointerY
                when (currentSection) {
                    UpperSection -> {
                        upperY += dy
                        upperY = max(0f, min(lowerY - minDistance, upperY))
                    }

                    LowerSection -> {
                        lowerY += dy
                        lowerY = max(upperY + minDistance, min(height.toFloat(), lowerY))
                    }
                }
                pointerY = event.y
                invalidate()
                return true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> return false
        }
        return false
    }

    fun setUnit(newUnit: RulerUnit) {
        unit = newUnit
        invalidate()
    }

    private fun dpTOpx(dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
}
