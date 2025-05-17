package com.example.satellitefinder.admobAds

import android.annotation.SuppressLint
import android.app.Activity
import com.example.satellitefinder.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import java.lang.Exception


object RemoteConfig {
    var appOpen: Boolean = true
    var interCompass: Boolean = true
    var interCurrentLocation: Boolean = true
    var interLanguage: Boolean = true
    var interSatelliteMap: Boolean = true
    var interFindSatellite: Boolean = true
    var interSatellites: Boolean = false
    var interSatellitesClick: Boolean = false
    var interSplash: Boolean = true
    var interPermission: Boolean = true

    var nativeSplash: Boolean = true
    var mainNative: Boolean = true
    var nativeExit: Boolean = true
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
    var levelBanner: Boolean = true
    var infoSheetBanner: Boolean = true
    var adCounter: Long = 3


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


            adCounter = remoteConfig["adCounter"].asLong()

            // ads json
            try {
                val remoteAdsJson = remoteConfig["ads_json_from_v43"].asString()
                val data = Gson().fromJson(remoteAdsJson, RemoteConfigModel::class.java)

                appOpen = data.appOpen
                // interstitial
                interSplash = data.interSplash
                interLanguage = data.interLanguage
                interPermission = data.interPermission
                interCompass = data.interCompass
                interCurrentLocation = data.interCurrentLocation
                interSatelliteMap = data.interSatelliteMap
                interFindSatellite = data.interFindSatellite
                interSatellites = data.interSatellites
                interSatellitesClick = data.interSatellitesClick
                // native ads
                nativeSplash = data.nativeSplash
                languagesNative = data.languagesNative
                onBoardingNative = data.onBoardingNative
                mainNative = data.mainNative
                nativeExit = data.nativeExit
                satelliteFindNative = data.satelliteFindNative
                compassNative = data.compassNative
                currentLocationNative = data.currentLocationNative
                selectSatelliteNative = data.selectSatelliteNative
                searchSatelliteNative = data.searchSatelliteNative
                mapSatelliteNative = data.mapSatelliteNative
                moreNative = data.moreNative
                permissionNative = data.permissionNative
                // banners
                levelBanner = data.levelBanner
                infoSheetBanner = data.infoSheetBanner

            }catch (e : Exception){
                e.printStackTrace()
            }

            callBack.invoke(task.isSuccessful)
        }
    }

}