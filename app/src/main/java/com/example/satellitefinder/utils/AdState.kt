package com.example.satellitefinder.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.android.gms.ads.nativead.NativeAd

sealed class AdState {
    object Loading : AdState()
    data class Loaded(val nativeAd: NativeAd) : AdState()
    object Failed : AdState()
}


// Function to check internet connectivity
fun isInternetConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
    return networkInfo?.isConnected == true
}
