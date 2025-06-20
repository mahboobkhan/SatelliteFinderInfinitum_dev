package com.example.satellitefinder.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StarlinkSatelliteDao {
    @Query("SELECT * FROM starlink_satellites")
    fun getAll(): List<StarlinkSatelliteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(satellites: List<StarlinkSatelliteEntity>)
    @Query("DELETE FROM starlink_satellites")
    fun deleteAll()
}