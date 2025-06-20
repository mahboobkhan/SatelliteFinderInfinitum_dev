package com.example.satellitefinder.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "general_satellites")
data class GeneralSatelliteEntity(
    @PrimaryKey val name: String,
    val latitude: Double,
    val longitude: Double,
    val azimuth: Double,
    val elevation: Double
)
