package com.example.satellitefinder.admobAds

import com.google.gson.annotations.SerializedName

data class RemoteConfigModel(
    @SerializedName("app_open") var appOpen: Boolean = true,
    @SerializedName("inter_splash") var interSplash: Boolean = true,
    @SerializedName("inter_language") var interLanguage: Boolean = true,
    @SerializedName("inter_permission") var interPermission: Boolean = true,
    @SerializedName("inter_compass") var interCompass: Boolean = true,
    @SerializedName("inter_current_location") var interCurrentLocation: Boolean = true,
    @SerializedName("inter_satellite_map") var interSatelliteMap: Boolean = true,
    @SerializedName("inter_find_satellite") var interFindSatellite: Boolean = true,
    @SerializedName("inter_satellites") var interSatellites: Boolean = true,
    @SerializedName("inter_satellites_click") var interSatellitesClick: Boolean = false,
    @SerializedName("native_splash") var nativeSplash: Boolean = true,
    @SerializedName("languages_native") var languagesNative: Boolean = true,
    @SerializedName("on_boarding_native") var onBoardingNative: Boolean = true,
    @SerializedName("main_native") var mainNative: Boolean = true,
    @SerializedName("native_exit") var nativeExit: Boolean = true,
    @SerializedName("satellite_find_native") var satelliteFindNative: Boolean = true,
    @SerializedName("compass_native") var compassNative: Boolean = true,
    @SerializedName("current_location_native") var currentLocationNative: Boolean = true,
    @SerializedName("select_satellite_native") var selectSatelliteNative: Boolean = false,
    @SerializedName("search_satellite_native") var searchSatelliteNative: Boolean = true,
    @SerializedName("map_satellite_native") var mapSatelliteNative: Boolean = true,
    @SerializedName("more_native") var moreNative: Boolean = true,
    @SerializedName("permission_native") var permissionNative: Boolean = true,
    @SerializedName("level_banner") var levelBanner: Boolean = true,
    @SerializedName("info_sheet_banner") var infoSheetBanner: Boolean = true,
)