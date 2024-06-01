package com.example.satellitefinder.firebaseRemoteConfigurations


import android.content.Context
import androidx.annotation.Keep
import com.example.satellitefinder.R

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class RemoteClient {
    private lateinit var remoteConfig: FirebaseRemoteConfig

    fun init(context: Context): FirebaseRemoteConfig {
        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSetting = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(10)
            .build()
        remoteConfig.setConfigSettingsAsync(configSetting)
        remoteConfig.setDefaultsAsync(
            mapOf(context.getString(R.string.remote) to Gson().toJson(RemoteConfig()))
        )
        return remoteConfig
    }
}

@Keep
data class RemoteConfig(
    // @SerializedName("admob_app_open_id")
    @SerializedName("openAppAdID")
    val openAppAdID: RemoteDefaultVal = RemoteDefaultVal(0),


    @SerializedName("splashNative")
    val splashNative: RemoteDefaultVal = RemoteDefaultVal(0),


    @SerializedName("InterstitialMain")
    val InterstitialMain: RemoteDefaultVal = RemoteDefaultVal(0),

    @SerializedName("mainNative")
    val mainNative: RemoteDefaultVal = RemoteDefaultVal(0),


    @SerializedName("compassNative")
    val compassNative: RemoteDefaultVal = RemoteDefaultVal(0),


    @SerializedName("currentLocationNative")
    val currentLocationNative: RemoteDefaultVal = RemoteDefaultVal(0),


    @SerializedName("mapSatelliteNative")
    val mapSatelliteNative: RemoteDefaultVal = RemoteDefaultVal(0),

    @SerializedName("moreNative")
    val moreNative: RemoteDefaultVal = RemoteDefaultVal(0),

    @SerializedName("permissionNative")
    val permissionNative: RemoteDefaultVal = RemoteDefaultVal(0),

    @SerializedName("onBoardingNative")
    val onBoardingNative: RemoteDefaultVal = RemoteDefaultVal(0),

    @SerializedName("exitNative")
    val exitNative: RemoteDefaultVal = RemoteDefaultVal(0),

    @SerializedName("languagesNative")
    val languagesNative: RemoteDefaultVal = RemoteDefaultVal(0),

    @SerializedName("satelliteFindNative")
    val satelliteFindNative : RemoteDefaultVal = RemoteDefaultVal(0),

    @SerializedName("selectSatelliteNative")
    val selectSatelliteNative : RemoteDefaultVal = RemoteDefaultVal(0),


    @SerializedName("searchSatelliteNative")
    val searchSatelliteNative : RemoteDefaultVal = RemoteDefaultVal(0)

)

@Keep
data class RemoteDefaultVal(
    @SerializedName("value")
    val value: Int = 0,
)