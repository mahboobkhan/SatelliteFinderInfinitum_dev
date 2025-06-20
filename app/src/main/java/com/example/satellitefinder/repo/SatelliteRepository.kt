package com.example.satellitefinder.repo

import com.example.satellitefinder.models.SatelliteModel

interface SatelliteRepository {
    suspend fun fetchTLESatellites(satelliteType : String): List<SatelliteModel>
}