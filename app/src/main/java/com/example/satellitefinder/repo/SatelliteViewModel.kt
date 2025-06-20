package com.example.satellitefinder.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satellitefinder.models.SatelliteModel
import kotlinx.coroutines.launch

class SatelliteViewModel(private val repository: SatelliteRepository) : ViewModel() {

    private val _satelliteList = MutableLiveData<List<SatelliteModel>>()
    val satelliteList: LiveData<List<SatelliteModel>> = _satelliteList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    /*fun loadSatellites(satelliteType: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                Log.d("SatelliteViewModel", "Loading satellites for type: $satelliteType")

                // First try to get data without showing loading (for cached/static data)
                val satellites = repository.fetchTLESatellites(satelliteType)
                
                if (satellites.isNotEmpty()) {
                    _satelliteList.value = satellites
                    Log.d("SatelliteViewModel", "Loaded ${satellites.size} satellites for $satelliteType (cached/static)")
                    return@launch
                }
                
                // If no data available, show loading and try network fetch
                _isLoading.value = true
                _errorMessage.value = null
                
                // Try network fetch
                val networkSatellites = repository.fetchTLESatellites(satelliteType)
                
                if (networkSatellites.isNotEmpty()) {
                    _satelliteList.value = networkSatellites
                    Log.d("SatelliteViewModel", "Loaded ${networkSatellites.size} satellites for $satelliteType (network)")
                } else {
                    _errorMessage.value = "No satellites found for $satelliteType"
                    Log.w("SatelliteViewModel", "No satellites found for $satelliteType")
                }
                
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load satellites: ${e.message}"
                Log.e("SatelliteViewModel", "Error loading satellites for $satelliteType", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
*/
    fun loadSatellites(satelliteType: String) {
        viewModelScope.launch {
            try {
                Log.d("SatelliteViewModel", "Loading satellites for type: $satelliteType")

                // Show loading state initially
                _isLoading.value = true
                _errorMessage.value = null

                // Single repository call that handles: cached → static → network
                val satellites = repository.fetchTLESatellites(satelliteType)

                if (satellites.isNotEmpty()) {
                    _satelliteList.value = satellites
                    Log.d("SatelliteViewModel", "Loaded ${satellites.size} satellites for $satelliteType")
                } else {
                    _errorMessage.value = "No satellites found for $satelliteType"
                    Log.w("SatelliteViewModel", "No satellites found for $satelliteType")
                }

            } catch (e: Exception) {
                _errorMessage.value = "Failed to load satellites: ${e.message}"
                Log.e("SatelliteViewModel", "Error loading satellites for $satelliteType", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
