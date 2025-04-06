package com.example.satellitefinder.admobAds

import android.app.Activity
import android.content.Context
import com.example.satellitefinder.utils.isInternetConnected
import com.google.android.gms.ads.interstitial.InterstitialAd

var interstitialAdPriority: InterstitialAd? = null
var isInterstitialAdOnScreen = false
var interstitialAdCounter = 0
var interstitialAdCounterConfig = 0

fun counterCheck(): Boolean {
    interstitialAdCounter++
    return (interstitialAdCounter % RemoteConfig.adCounter).toInt() == 0
}

fun counterCheckConfig(counterValue: Int): Boolean {
    interstitialAdCounterConfig++
    return interstitialAdCounterConfig % counterValue == 0
}

fun Activity.showPriorityInterstitialAdWithCounter(
    loadAgain: Boolean = false,
    adIDLow: String? = null,
    loadAd: ((InterstitialAd) -> Unit)? = null,
    closeListener: (() -> Unit)? = null,
    failListener: (() -> Unit)? = null,
    showListener: (() -> Unit)? = null
) {
    if (counterCheck()) {
        showPriorityAdmobInterstitial(
            loadAgain,
            adIDLow,
            loadAd,
            closeListener,
            failListener,
            showListener,
        )
    }
}

fun Activity.showPriorityAdmobInterstitial(
    loadAgain: Boolean = false,
    adIDLow: String? = null,
    loadAd: ((InterstitialAd) -> Unit)? = null,
    closeListener: (() -> Unit)? = null,
    failListener: (() -> Unit)? = null,
    showListener: (() -> Unit)? = null
) {
    InterstitialAdClass.getInstance().showPriorityInterstitialAdNew(
        this,
        loadAgain,
        adIDLow,
        loadAd,
        closeListener,
        failListener,
        showListener
    )
}

fun Context.loadPriorityAdmobInterstitial(adIDLow: String) {
    InterstitialAdClass.getInstance().loadPriorityInterstitialAds(this, adIDLow)
}

fun Activity.loadAndShowInterstitial(
    adIDLow: String,
    closeListener: (() -> Unit)? = null,
    failListener: (() -> Unit)? = null,
    showListener: (() -> Unit)? = null
) {
    if (isInternetConnected()) {
        InterstitialAdClass.getInstance()
            .loadAndShowSplashInterstitial(this, adIDLow, closeListener, failListener, showListener)
    } else {
        failListener?.invoke()
    }
}

