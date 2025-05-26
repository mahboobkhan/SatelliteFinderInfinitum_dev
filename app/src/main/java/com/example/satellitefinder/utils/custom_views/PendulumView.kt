package com.example.satellitefinder.utils.custom_views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.content.res.AppCompatResources
import com.example.satellitefinder.R

class PendulumView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var angle: Float = 0f
    private var bobBitmap: Bitmap? = null
    private val paint = Paint()

    private var currentAngle: Float = 0f
    private var animator: ValueAnimator? = null

    init {
        bobBitmap = BitmapFactory.decodeResource(resources, R.drawable.pendulum_center)
    }

    fun updateAngle(newAngle: Float) {
        val targetAngle = newAngle * 5f

        animator?.cancel() // Stop previous animation if running

        animator = ValueAnimator.ofFloat(currentAngle, targetAngle).apply {
            duration = 200L // Adjust for smoothness
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                currentAngle = it.animatedValue as Float
                invalidate()
            }
            start()
        }

        val drawableId = if (targetAngle in -2f..2f) {
            R.drawable.pendulum_center
        } else {
            R.drawable.pendulum_tilted
        }
        bobBitmap = BitmapFactory.decodeResource(resources, drawableId)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        bobBitmap?.let { bitmap ->
            val centerX = width / 2f
            val topY = 100f // top pivot Y position

            val rodLength = bitmap.height.toFloat() // assume rod fills image height
            val bobHeight = 100f // estimate of bob's size in image

            // Save canvas and apply rotation
            canvas.save()
            canvas.translate(centerX, topY)
            canvas.rotate(currentAngle)

            // Draw the rotating pendulum image
            canvas.drawBitmap(bitmap, -bitmap.width / 2f, 0f, paint)
            canvas.restore()

            // Calculate fixed point at the end of the pendulum
            val pendulumEndY = topY + rodLength

            // Load and draw the fixed indicator just below the bob
            val indicatorBitmap =
                getBitmapFromVectorDrawable(context, R.drawable.pendulum_bottom_center)
            val centerBottomX = (width - indicatorBitmap.width) / 2f
            val fixedY = pendulumEndY - 200f // small gap

            canvas.drawBitmap(indicatorBitmap, centerBottomX, fixedY, paint)
        }
    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = AppCompatResources.getDrawable(context, drawableId)!!
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}