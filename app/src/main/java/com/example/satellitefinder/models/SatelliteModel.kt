package com.example.satellitefinder.models

import androidx.annotation.Keep
import java.io.Serializable
@Keep
data class SatelliteModel(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val azimuth: Double,
    val elevation: Double
) : Serializable