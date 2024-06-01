package com.example.satellitefinder.utils

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Status>


    enum class Status {
        Disconnected, Connected
    }
}