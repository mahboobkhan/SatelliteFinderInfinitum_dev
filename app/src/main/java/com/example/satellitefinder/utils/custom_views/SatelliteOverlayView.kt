package com.example.satellitefinder.utils.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

// Data class to hold all necessary info for drawing a satellite on the overlay
data class DrawableSatellite(val name: String, val azimuth: Double, val elevation: Double)

class SatelliteOverlayView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var satellites = listOf<DrawableSatellite>()

    // Device's current orientation (provided by the activity)
    var deviceAzimuth: Double = 0.0 // in radians
    var devicePitch: Double = 0.0 // in radians

    // Camera field of view (provided by the activity)
    var cameraXAngle: Double = Math.toRadians(60.0)
    var cameraYAngle: Double = Math.toRadians(45.0)

    var hasLeftSatellite: Boolean = false
    var hasRightSatellite: Boolean = false

    private val scalingFactor: Float = 2f
    private val paint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 30f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }
    private val backgroundPaint = Paint().apply {
        color = Color.parseColor("#2F2F2F")
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    fun setSatellites(sats: List<DrawableSatellite>) {
        this.satellites = sats
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        hasLeftSatellite = false
        hasRightSatellite = false

        var anyOnScreen = false
        var anyLeft = false
        var anyRight = false


        var prevX: Float? = null
        var prevY: Float? = null

        for (sat in satellites) {
            // Satellite's position is already calculated, just convert to radians for math
            val satAz = Math.toRadians(sat.azimuth)
            val satEl = Math.toRadians(sat.elevation)

            // Calculate the difference between the device's orientation and the satellite's position
            var deltaAz = satAz - deviceAzimuth
            // Normalize azimuth difference to the range -PI to PI
            deltaAz = (deltaAz + Math.PI * 3) % (Math.PI * 2) - Math.PI
            val deltaEl = satEl - devicePitch

            // Map the angular difference to screen coordinates
            val x = (width / 2.0 + (deltaAz / cameraXAngle) * width * scalingFactor).toFloat()
            val y = (height / 2.0 - (deltaEl / cameraYAngle) * height * scalingFactor).toFloat()

            // Check if satellite is on screen
            if (x >= 0 && x <= width) {
                anyOnScreen = true
            } else if (x < 0) {
                anyLeft = true
            } else if (x > width) {
                anyRight = true
            }

            // Draw line from previous satellite
            if (prevX != null && prevY != null) {
                paint.strokeWidth = 4f
                canvas.drawLine(prevX, prevY, x, y, paint)
            }
            // Draw satellite dot
            paint.style = Paint.Style.FILL
            paint.color = Color.GREEN
            canvas.drawCircle(x, y, 15f, paint)

            // Draw satellite name with a rounded background
            val text = sat.name
            val textOffset = 50f // Increased offset for the box
            val cornerRadius = 20f
            val horizontalPadding = 24f
            val verticalPadding = 12f

            // Measure text for background
            val textWidth = textPaint.measureText(text)
            val fontMetrics = textPaint.fontMetrics

            // Define background rect
            val textBaseline = y + textOffset
            val rectLeft = (x - 130) - (textWidth / 2) - horizontalPadding
            val rectRight = (x - 130) + (textWidth / 2) + horizontalPadding
            val rectTop = textBaseline + fontMetrics.ascent - verticalPadding
            val rectBottom = textBaseline + fontMetrics.descent + verticalPadding

            // Draw the background
            canvas.drawRoundRect(
                rectLeft,
                rectTop,
                rectRight,
                rectBottom,
                cornerRadius,
                cornerRadius,
                backgroundPaint
            )

            // Draw the text
            canvas.drawText(text, x - 130, textBaseline, textPaint)

            // Save current as previous
            prevX = x
            prevY = y
        }

        // Set the flags based on satellite positions
        hasLeftSatellite = !anyOnScreen && anyLeft
        hasRightSatellite = !anyOnScreen && anyRight
    }
}