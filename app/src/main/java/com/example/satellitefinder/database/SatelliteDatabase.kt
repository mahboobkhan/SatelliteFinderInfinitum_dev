package com.example.satellitefinder.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WeatherSatelliteEntity::class, GeneralSatelliteEntity::class, StarlinkSatelliteEntity::class, IssSatelliteEntity::class],
    version = 1
)
abstract class SatelliteDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherSatelliteDao
    abstract fun generalDao(): GeneralSatelliteDao
    abstract fun starlinkDao(): StarlinkSatelliteDao
    abstract fun issDao(): IssSatelliteDao
}