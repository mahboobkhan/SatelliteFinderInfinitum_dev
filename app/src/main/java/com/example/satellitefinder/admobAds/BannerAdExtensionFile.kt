package com.example.satellitefinder.admobAds

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.view.WindowMetrics
import android.widget.FrameLayout
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

fun showBannerAD(
    frameLayout : FrameLayout,
    context: Context,
    bannerAdId: String,
    adCallBack: (() -> Unit)? = null
) {
    val admobView = AdView(context)
    admobView.adUnitId = bannerAdId
    getBannerAdSize(context)?.let { admobView.setAdSize(it) }
    val adRequest = AdRequest.Builder().build()
    admobView.loadAd(adRequest)
    setBannerAdListener(admobView, frameLayout) {
        adCallBack?.invoke()
    }
}

private fun setBannerAdListener(admobView: AdView, myView: FrameLayout, adCallBack: () -> Unit) {
    admobView.adListener = object : AdListener() {
        override fun onAdLoaded() {
            adCallBack.invoke()
            myView.removeAllViews()
            myView.addView(admobView)
        }
    }
}

private fun getBannerAdSize(context: Context): AdSize? {
    // Get the ad size with screen width.
    val displayMetrics = context.applicationContext.resources.displayMetrics
    val adWidthPixels =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
            windowMetrics.bounds.width()
        } else {
            displayMetrics.widthPixels
        }
    val density = displayMetrics.density
    val adWidth = (adWidthPixels / density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context.applicationContext, adWidth)
}

fun showCollapsibleBanner(
    myView: FrameLayout,
    context: Context,
    bannerId: String,
    adCallBack: (() -> Unit)? = null
) {
    // Determine the ad size before creating the AdView

    val adSize: AdSize = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        getBannerAdSize(context) ?: AdSize.BANNER
    } else {
        AdSize.BANNER
    }


    // Create the AdView and set the adUnitId and adSize immediately
    val admobView = AdView(context).apply {
        this.adUnitId = bannerId
        this.setAdSize(adSize)
    }

    val extras = Bundle().apply {
        putString("collapsible", "bottom")
    }

    val adRequest = AdRequest.Builder()
        .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
        .build()

    // Add the AdView to the FrameLayout
    myView.addView(admobView)

    // Load the ad
    admobView.loadAd(adRequest)

    // Set the ad listener
    setBannerAdListener(admobView, myView) {
        adCallBack?.invoke()
    }
}
