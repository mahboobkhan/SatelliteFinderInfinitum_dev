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

    fun loadSatellites(satelliteType: String) {
        viewModelScope.launch {
            try {
                val satellites = repository.fetchTLESatellites(satelliteType)
                _satelliteList.value = satellites
            } catch (e: Exception) {
                _satelliteList.value = emptyList()
            }
        }
    }
}
