package com.example.satellitefinder.admobAds

import android.annotation.SuppressLint
import android.app.Activity
import com.example.satellitefinder.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings


object RemoteConfig {
    var appOpen: Boolean = true

    var interAll: Boolean = true
    var interCompass: Boolean = true
    var interCurrentLocation: Boolean = true
    var interLanguage: Boolean = true
    var interSatelliteMap: Boolean = true
    var interFindSatellite: Boolean = true
    var interSatellites: Boolean = false
    var interSatellitesClick: Boolean = false
    var interSplash: Boolean = true

    var nativeSplash: Boolean = true
    var mainNative: Boolean = true
    var nativeExit: Boolean = true
    var nativeOnboarding: Boolean = true
    var satelliteFindNative: Boolean = true
    var compassNative: Boolean = true
    var currentLocationNative: Boolean = true
    var selectSatelliteNative: Boolean = false
    var searchSatelliteNative: Boolean = true
    var mapSatelliteNative: Boolean = true
    var moreNative: Boolean = true
    var onBoardingNative: Boolean = true
    var permissionNative: Boolean = true
    var languagesNative: Boolean = true
    var banner: Boolean = true
    var adCounter:Long = 3



    //configurations
    @SuppressLint("StaticFieldLeak")
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    private var interval: Long = 0
    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = interval
    }


    fun getRemoteConfig(): FirebaseRemoteConfig {
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_defaults)
        return remoteConfig
    }


    fun fetchRecord(application: Activity, callBack: (Boolean) -> Unit) {
        val remoteConfig = getRemoteConfig()
        remoteConfig.fetchAndActivate().addOnCompleteListener(application) { task ->

            appOpen = remoteConfig["appOpenID"].asBoolean()
            interSplash = remoteConfig["splashInterstitialID"].asBoolean()
            interAll = remoteConfig["interstitialID"].asBoolean()
            interCompass = remoteConfig["interCompass"].asBoolean()
            interCurrentLocation = remoteConfig["interCurrentLocation"].asBoolean()
            interLanguage = remoteConfig["interLanguage"].asBoolean()
            interSatelliteMap = remoteConfig["interSatelliteMap"].asBoolean()
            interFindSatellite = remoteConfig["interFindSatellite"].asBoolean()
            interSatellites = remoteConfig["interSatellites"].asBoolean()
            interSatellitesClick = remoteConfig["interSatellitesClick"].asBoolean()

            nativeSplash = remoteConfig["splashNativeId"].asBoolean()
            mainNative = remoteConfig["mainNativeId"].asBoolean()
            selectSatelliteNative = remoteConfig["selectSatelliteNativeId"].asBoolean()
            searchSatelliteNative = remoteConfig["searchSatelliteNativeId"].asBoolean()
            satelliteFindNative = remoteConfig["satelliteFindNativeId"].asBoolean()
            compassNative = remoteConfig["compassNativeId"].asBoolean()
            currentLocationNative = remoteConfig["currentLocationNativeId"].asBoolean()
            nativeExit = remoteConfig["exitNativeId"].asBoolean()
            nativeOnboarding = remoteConfig["onBoardingNativeId"].asBoolean()
            mapSatelliteNative = remoteConfig["mapSatelliteNativeId"].asBoolean()
            moreNative = remoteConfig["moreNativeId"].asBoolean()
            onBoardingNative = remoteConfig["onBoardingNativeId"].asBoolean()
            permissionNative = remoteConfig["permissionNative"].asBoolean()
            languagesNative = remoteConfig["languagesNativeId"].asBoolean()
            banner = remoteConfig["bannerId"].asBoolean()
            adCounter = remoteConfig["adCounter"].asLong()

            callBack.invoke(task.isSuccessful)
        }
    }

}