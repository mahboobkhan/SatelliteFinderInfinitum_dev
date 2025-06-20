package com.example.satellitefinder.utils

import android.content.Context
import com.example.satellitefinder.repo.SatelliteDataManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext

object SatelliteDataUtils {
    
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    /**
     * Check if satellite data is available for a specific type
     */
    fun isDataAvailable(satelliteType: String): Boolean {
        return try {
            val dataManager = GlobalContext.get().get<SatelliteDataManager>()
            dataManager.isDataLoaded(satelliteType)
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Check if satellite data is currently loading for a specific type
     */
    fun isLoading(satelliteType: String): Boolean {
        return try {
            val dataManager = GlobalContext.get().get<SatelliteDataManager>()
            dataManager.isLoading(satelliteType)
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Load static data immediately for a specific satellite type
     */
    fun loadStaticData(satelliteType: String): List<com.example.satellitefinder.models.SatelliteModel> {
        return try {
            val dataManager = GlobalContext.get().get<SatelliteDataManager>()
            dataManager.getStaticData(satelliteType)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * Check if static data is available for a specific satellite type
     */
    fun hasStaticData(satelliteType: String): Boolean {
        return satelliteType in listOf("weather", "general", "starlink", "iss")
    }
    
    /**
     * Get immediate data for a satellite type (cached or static)
     */
    fun getImmediateData(satelliteType: String): List<com.example.satellitefinder.models.SatelliteModel> {
        return if (isDataAvailable(satelliteType)) {
            // Return cached data from database
            emptyList() // This will be handled by the repository
        } else if (hasStaticData(satelliteType)) {
            // Return static data
            loadStaticData(satelliteType)
        } else {
            emptyList()
        }
    }
    
    /**
     * Force refresh satellite data for a specific type
     */
    fun refreshData(satelliteType: String, context: Context, onComplete: (() -> Unit)? = null) {
        scope.launch {
            try {
                // Check internet connectivity before refresh
                if (!isInternetConnected(context)) {
                    onComplete?.invoke()
                    return@launch
                }
                
                val dataManager = GlobalContext.get().get<SatelliteDataManager>()
                // This will trigger a fresh network request
                dataManager.preloadAllSatelliteData()
                onComplete?.invoke()
            } catch (e: Exception) {
                onComplete?.invoke()
            }
        }
    }
    
    /**
     * Get data status for all satellite types
     */
    fun getDataStatus(): Map<String, Boolean> {
        return try {
            val dataManager = GlobalContext.get().get<SatelliteDataManager>()
            mapOf(
                "weather" to dataManager.isDataLoaded("weather"),
                "general" to dataManager.isDataLoaded("general"),
                "starlink" to dataManager.isDataLoaded("starlink"),
                "iss" to dataManager.isDataLoaded("iss")
            )
        } catch (e: Exception) {
            emptyMap()
        }
    }
    
    /**
     * Check if all satellite data is loaded
     */
    fun isAllDataLoaded(): Boolean {
        return try {
            val dataManager = GlobalContext.get().get<SatelliteDataManager>()
            listOf("weather", "general", "starlink", "iss").all { 
                dataManager.isDataLoaded(it) 
            }
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Check if any satellite data is currently loading
     */
    fun isAnyDataLoading(): Boolean {
        return try {
            val dataManager = GlobalContext.get().get<SatelliteDataManager>()
            listOf("weather", "general", "starlink", "iss").any { 
                dataManager.isLoading(it) 
            }
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Get loading status for all satellite types
     */
    fun getLoadingStatus(): Map<String, Boolean> {
        return try {
            val dataManager = GlobalContext.get().get<SatelliteDataManager>()
            mapOf(
                "weather" to dataManager.isLoading("weather"),
                "general" to dataManager.isLoading("general"),
                "starlink" to dataManager.isLoading("starlink"),
                "iss" to dataManager.isLoading("iss")
            )
        } catch (e: Exception) {
            emptyMap()
        }
    }
} 