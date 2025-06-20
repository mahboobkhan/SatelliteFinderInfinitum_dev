package com.example.satellitefinder.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherSatelliteDao {
    @Query("SELECT * FROM weather_satellites")
    fun getAll(): List<WeatherSatelliteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(satellites: List<WeatherSatelliteEntity>)
}