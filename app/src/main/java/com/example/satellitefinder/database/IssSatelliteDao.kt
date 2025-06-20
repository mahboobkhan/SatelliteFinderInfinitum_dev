package com.example.satellitefinder.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IssSatelliteDao {
    @Query("SELECT * FROM iss_satellites")
     fun getAll(): List<IssSatelliteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll(satellites: List<IssSatelliteEntity>)

    @Query("DELETE FROM iss_satellites")
    fun deleteAll()
}