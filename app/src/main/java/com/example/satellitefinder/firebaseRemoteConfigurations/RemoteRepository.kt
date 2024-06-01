package com.example.satellitefinder.firebaseRemoteConfigurations

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class RemoteRepository(private val remoteData: FirebaseRemoteConfig) {
    fun getFirebaseRemoteConfig(): FirebaseRemoteConfig = remoteData
}