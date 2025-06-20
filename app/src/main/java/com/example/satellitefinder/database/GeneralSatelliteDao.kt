package com.example.satellitefinder.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GeneralSatelliteDao {
    @Query("SELECT * FROM general_satellites")
    fun getAll(): List<GeneralSatelliteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertAll(satellites: List<GeneralSatelliteEntity>)
}