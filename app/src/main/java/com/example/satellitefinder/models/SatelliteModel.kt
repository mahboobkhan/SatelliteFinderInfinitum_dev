package com.example.satellitefinder.models

import java.io.Serializable

data class SatelliteModel(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val azimuth: Double,
    val elevation: Double
) : Serializable