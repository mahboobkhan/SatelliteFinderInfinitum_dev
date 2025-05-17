package com.example.satellitefinder.admobAds

import android.app.Activity
import android.content.Context
import com.example.adssdk.constants.AppUtils.mInterstitialAd
import com.example.adssdk.intertesialAds.InterstitialAdType
import com.example.adssdk.intertesialAds.InterstitialAdUtils
import com.example.satellitefinder.R
import com.google.android.gms.ads.interstitial.InterstitialAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    } else {
        failListener?.invoke()
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


    if (mInterstitialAd == null) {
        failListener?.invoke()
        loadAdmobInterstitial(adId = getString(R.string.interstialId))
        return
    }
    CoroutineScope(Dispatchers.Main).launch {
        InterstitialAdClass.getInstance().showLoadingDialog(this@showPriorityAdmobInterstitial)
        delay(2000L)
        if (!this@showPriorityAdmobInterstitial.isDestroyed && !this@showPriorityAdmobInterstitial.isFinishing) {
            InterstitialAdUtils(
                activity = this@showPriorityAdmobInterstitial,
                screenName = "FullScreenHowToUse"
            )
                .setReloadAd(true)
                .showInterstitialAd(
                    adsKey = getString(R.string.interstialId),
                    remoteConfig = true,
                    fullScreenAdShow = {
                        isInterstitialAdOnScreen = true
                        showListener?.invoke()
                        InterstitialAdClass.getInstance()
                            .dismissLoadingDialog(this@showPriorityAdmobInterstitial)
                    },
                    fullScreenAdDismissed = {
                        isInterstitialAdOnScreen = false
                        closeListener?.invoke()
                    },
                    fullScreenAdFailedToShow = {
                        failListener?.invoke()
                        InterstitialAdClass.getInstance()
                            .dismissLoadingDialog(this@showPriorityAdmobInterstitial)
                    },
                    fullScreenAdNotAvailable = {
                        closeListener?.invoke()
                        InterstitialAdClass.getInstance()
                            .dismissLoadingDialog(this@showPriorityAdmobInterstitial)
                    },
                    adType = InterstitialAdType.DEFAULT_AD,

                    )
        }

    }

    /*InterstitialAdClass.getInstance().showPriorityInterstitialAdNew(
        this,
        loadAgain,
        adIDLow,
        loadAd,
        closeListener,
        failListener,
        showListener
    )*/
}

fun Activity.loadAdmobInterstitial(
    adId: String,
    onAdLoaded: () -> Unit = {},
    onAdFailed: () -> Unit = {}
) {
//    InterstitialAdClass.getInstance().loadSimpleInterstitialAd(this, adId, onAdLoaded, onAdFailed)
    InterstitialAdUtils(this, "FullScreenHowToUse").loadInterstitialAd(
        adsKey = adId,
        remoteConfig = true,
        adLoaded = { _ -> onAdLoaded.invoke() },
        adFaileds = { _, _ -> onAdFailed.invoke() },
        adValidate = {
            onAdFailed.invoke()
        },
        adAlreadyLoaded = onAdLoaded,
        adType = InterstitialAdType.DEFAULT_AD
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
    /*if (isInternetConnected()) {
        InterstitialAdClass.getInstance()
            .loadAndShowSplashInterstitial(this, adIDLow, closeListener, failListener, showListener)
    } else {
        failListener?.invoke()
    }
*/



    CoroutineScope(Dispatchers.Main).launch {
        InterstitialAdClass.getInstance().showLoadingDialog(this@loadAndShowInterstitial)
        InterstitialAdUtils(this@loadAndShowInterstitial, "FullScreenHowToUse").loadInterstitialAd(
            adsKey = adIDLow,
            remoteConfig = true,
            adLoaded = { _ ->
                if (!this@loadAndShowInterstitial.isDestroyed && !this@loadAndShowInterstitial.isFinishing) {
                    InterstitialAdUtils(
                        activity = this@loadAndShowInterstitial,
                        screenName = "FullScreenHowToUse"
                    )
                        .setReloadAd(true)
                        .showInterstitialAd(
                            adsKey = getString(R.string.interstialId),
                            remoteConfig = true,
                            fullScreenAdShow = {
                                isInterstitialAdOnScreen = true
                                showListener?.invoke()
                                InterstitialAdClass.getInstance()
                                    .dismissLoadingDialog(this@loadAndShowInterstitial)
                            },
                            fullScreenAdDismissed = {
                                isInterstitialAdOnScreen = false
                                closeListener?.invoke()
                            },
                            fullScreenAdFailedToShow = {
                                failListener?.invoke()
                                InterstitialAdClass.getInstance()
                                    .dismissLoadingDialog(this@loadAndShowInterstitial)
                            },
                            fullScreenAdNotAvailable = {
                                closeListener?.invoke()
                                InterstitialAdClass.getInstance()
                                    .dismissLoadingDialog(this@loadAndShowInterstitial)
                            },
                            adType = InterstitialAdType.DEFAULT_AD,

                            )
                }
            },
            adFaileds = { _, _ -> failListener?.invoke() },
            adValidate = {
                failListener?.invoke()
            },
            adAlreadyLoaded = {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(1000)
                    if (!this@loadAndShowInterstitial.isDestroyed && !this@loadAndShowInterstitial.isFinishing) {
                        InterstitialAdUtils(
                            activity = this@loadAndShowInterstitial,
                            screenName = "FullScreenHowToUse"
                        )
                            .setReloadAd(true)
                            .showInterstitialAd(
                                adsKey = getString(R.string.interstialId),
                                remoteConfig = true,
                                fullScreenAdShow = {
                                    isInterstitialAdOnScreen = true
                                    showListener?.invoke()
                                    InterstitialAdClass.getInstance()
                                        .dismissLoadingDialog(this@loadAndShowInterstitial)
                                },
                                fullScreenAdDismissed = {
                                    isInterstitialAdOnScreen = false
                                    closeListener?.invoke()
                                },
                                fullScreenAdFailedToShow = {
                                    failListener?.invoke()
                                    InterstitialAdClass.getInstance()
                                        .dismissLoadingDialog(this@loadAndShowInterstitial)
                                },
                                fullScreenAdNotAvailable = {
                                    closeListener?.invoke()
                                    InterstitialAdClass.getInstance()
                                        .dismissLoadingDialog(this@loadAndShowInterstitial)
                                },
                                adType = InterstitialAdType.DEFAULT_AD,

                                )
                    }
                }

            },
            adType = InterstitialAdType.DEFAULT_AD
        )



    }
}

