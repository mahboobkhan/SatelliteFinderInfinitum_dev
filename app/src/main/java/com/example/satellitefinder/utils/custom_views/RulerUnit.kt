package com.example.satellitefinder.utils.custom_views

import android.util.DisplayMetrics
import android.util.TypedValue

/**
 * Created by Anas Altair on 8/29/2018.
 */
enum class RulerUnit(val converter: Float, val unit: String) {
    MM(25.4f, "MM"),
    CM(2.54f, "CM"),
    IN(1f, "INCH");

    companion object {

        fun mmToPx(mm: Float, coefficient: Float, displayMetrics: DisplayMetrics): Float {
            return mm * TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_MM,
                coefficient,
                displayMetrics
            )
        }

        fun pxToIn(px: Float, coefficient: Float, displayMetrics: DisplayMetrics): Float {
            return px / TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_IN,
                coefficient,
                displayMetrics
            )
        }
        /*fun inToPx(inches: Float, coefficient: Float, metrics: DisplayMetrics): Float {
            return inches * 160f * coefficient * (metrics.densityDpi / 160f)
        }*/
        /*fun inToPx(inches: Float, coefficient: Float, metrics: DisplayMetrics): Float {
            return inches * 160f * coefficient * (metrics.densityDpi / 160f)
        }*/

        fun pxToIn(px: Float, displayMetrics: DisplayMetrics): Float {
            return px / displayMetrics.xdpi
        }

        fun inToPx(inches: Float, coefficient: Float, metrics: DisplayMetrics): Float {
            return inches * 160f * coefficient * (metrics.densityDpi / 160f)
        }

        fun inToPx(inches: Float, metrics: DisplayMetrics): Float {
            return inches * metrics.xdpi
        }

    }

    /**
     * @param value in IN
     */
//    fun getUnitString(value: Float) = "${(value * converter).format(1)} $unit"
    fun getUnitString(value: Float): String {
        return when (this) {
            IN -> "${value.format(1)} $unit"
            else -> "${(value * converter).format(1)} $unit"
        }
    }

    /**
     * @param value in IN
     *
     * @return value in #unit
     */
    fun convert(value: Float) = value * converter
}

private fun Float.format(value: Int) = java.lang.String.format("%.${value}f", this)