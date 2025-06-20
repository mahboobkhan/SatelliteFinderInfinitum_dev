package com.example.satellitefinder.utils

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

object SatelliteCalculator {

    data class Position(val azimuth: Double, val elevation: Double)

    /**
     * Calculates the Azimuth and Elevation of a satellite from a given location.
     *
     * @param userLat Latitude of the user's location in degrees.
     * @param userLon Longitude of the user's location in degrees.
     * @param satLon Longitude of the satellite in degrees.
     * @return A Position object containing the satellite's Azimuth and Elevation in degrees.
     */
    fun calculate(userLat: Double, userLon: Double, satLon: Double): Position {
        val d10 = (userLon - satLon) * 0.017453292519943295
        val d11 = userLat * 0.017453292519943295

        val atan2Result = atan2(sin(d11), tan(d10)) * 57.29577951308232
        val azimuthAngle = (270.0 - atan2Result) / 360.0
        val azimuth = (azimuthAngle - floor(azimuthAngle)) * 360.0

        val cos10 = cos(d10)
        val cos11 = cos(d11)

        val term1 = cos11 * 6.6107 * cos10
        val term2 = 1.0 - (cos11 * cos11 * cos10 * cos10)
        val elevation = atan2(term1 - 1.0, sqrt(term2) * 6.6107) * 57.29577951308232

        return Position(azimuth, elevation)
    }
} 