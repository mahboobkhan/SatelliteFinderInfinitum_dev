package com.example.satellitefinder.admobAds

import android.app.Activity
import android.content.Context
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.example.satellitefinder.admobAds.populateUnifiedNativeAdView
import com.example.satellitefinder.admobAds.showNativeLog
import com.example.satellitefinder.utils.AdState
import com.example.satellitefinder.utils.isInternetConnected
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

var obNativeAd1: AdState? = null
var obNativeAd2: AdState? = null
var obNativeAdFull: AdState? = null
var obNativeAd3: AdState? = null
var obNativeAd4: AdState? = null

fun loadAndReturnAd(
    context: Context, nativeIdLow: String, adResult: (AdState) -> Unit
) {
    if (!isInternetConnected(context)) {
        adResult(AdState.Failed)
        return
    }

    if (nativeIdLow != null) {
        loadNativeAD(context, nativeIdLow) { ad ->
            when {
                ad != null -> adResult(AdState.Loaded(ad))
                else -> adResult(AdState.Failed)
            }
        }
    }
}

fun loadNativeAD(context: Context, nativeId: String, adResult: (NativeAd?) -> Unit) {
    val builder = AdLoader.Builder(context, nativeId)
    builder.forNativeAd { nativeAd ->
        adResult.invoke(nativeAd)
    }

    val videoOptions = VideoOptions.Builder().setStartMuted(true).build()

    val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()

    builder.withNativeAdOptions(adOptions)

    val adLoader = builder.withAdListener(object : AdListener() {
        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            val error =
                "${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}"
            showNativeLog("failed to load native ad: $error")
            adResult.invoke(null)
        }
    }).build()

    adLoader.loadAd(AdRequest.Builder().build())
}
/*

fun Activity.loadAndShowNativeAd(
    adFrame: FrameLayout,
    @LayoutRes layoutRes: Int,
    nativeId: String,
    failToLoad: (() -> Unit)? = null,
    adLoading: (() -> Unit)? = null
) {
    loadAndReturnAd(this, nativeId) { adLoadState ->
        when (adLoadState) {
            is AdState.Loading -> {
                adLoading?.invoke()
            }

            is AdState.Loaded -> {
                CoroutineScope(Dispatchers.Main).launch {
                    val activityDestroyed: Boolean = isDestroyed
                    if (activityDestroyed || isFinishing || isChangingConfigurations) {
                        adLoadState.nativeAd.destroy()
                        return@launch
                    }

                    val adView = layoutInflater.inflate(layoutRes, null, false) as NativeAdView
                    populateUnifiedNativeAdView(adLoadState.nativeAd, adView, null)
                    adFrame.removeAllViews()
                    adFrame.addView(adView)
                }
            }

            is AdState.Failed -> {
                failToLoad?.invoke()
            }
        }
    }
}
*/


private val currentAdStateMap: MutableMap<String, AdState> = mutableMapOf()

fun Activity.newLoadAndShowNativeAd(
    adFrame: FrameLayout,
    @LayoutRes layoutRes: Int,
    nativeId: String,
    adLoaded: (() -> Unit) ?= null,
    adLoading: (() -> Unit)? = null,
    failToLoad: (() -> Unit)? = null
) {
    val currentAdState = currentAdStateMap[nativeId]

    when (currentAdState) {
        is AdState.Loaded -> {
            showNativeAd(adFrame, layoutRes, currentAdState.nativeAd)
        }

        else -> {
            loadAndReturnAd(this, nativeId) { adLoadState ->
                when (adLoadState) {
                    is AdState.Loading -> {
                        adLoading?.invoke()
                    }

                    is AdState.Loaded -> {
                        currentAdStateMap[nativeId] = adLoadState
                        showNativeAd(adFrame, layoutRes, adLoadState.nativeAd)
                    }

                    is AdState.Failed -> {
                        failToLoad?.invoke()
                    }
                }
            }
        }
    }
}

fun Activity.showNativeAd(
    adFrame: FrameLayout,
    @LayoutRes layoutRes: Int,
    nativeAd: NativeAd,
    scaleType: ImageView.ScaleType? = ImageView.ScaleType.FIT_CENTER
) {
    CoroutineScope(Dispatchers.Main).launch {
        val activityDestroyed: Boolean = isDestroyed
        if (activityDestroyed || isFinishing || isChangingConfigurations) {
            nativeAd.destroy()
            return@launch
        }

        val adView = layoutInflater.inflate(layoutRes, null, false) as NativeAdView
        populateUnifiedNativeAdView(nativeAd, adView, scaleType)
        adFrame.removeAllViews()
        adFrame.addView(adView)
    }
}


fun showLoadedNativeAd(
    context: Activity,
    nativeAdHolder: FrameLayout,
    @LayoutRes adLayout: Int,
    adLoadState: AdState,
    scaleType: ImageView.ScaleType? = ImageView.ScaleType.FIT_CENTER
) {
    when (adLoadState) {
        is AdState.Loaded -> {
            val adView = context.layoutInflater.inflate(adLayout, null, false) as NativeAdView
            populateUnifiedNativeAdView(adLoadState.nativeAd, adView, scaleType)
            nativeAdHolder.removeAllViews()
            nativeAdHolder.addView(adView)
        }

        is AdState.Loading -> {}
        is AdState.Failed -> {}
    }
}

