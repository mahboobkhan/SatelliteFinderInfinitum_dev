package com.example.satellitefinder.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_satellites")
data class WeatherSatelliteEntity(
    @PrimaryKey val name: String,
    val latitude: Double,
    val longitude: Double,
    val azimuth: Double,
    val elevation: Double
)