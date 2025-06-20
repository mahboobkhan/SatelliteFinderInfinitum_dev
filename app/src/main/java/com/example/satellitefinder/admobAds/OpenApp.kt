package com.example.satellitefinder.admobAds

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.satellitefinder.R
import com.example.satellitefinder.ui.activites.SplashActivity
import com.example.satellitefinder.utils.MyApplication
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import org.jetbrains.annotations.NotNull

class OpenApp(private val globalClass: MyApplication) : Application.ActivityLifecycleCallbacks,
    LifecycleEventObserver {

    private val log = "AppOpenManager"

    private var adVisible = false
     var appOpenAd: AppOpenAd? = null

    private var currentActivity: Activity? = null
    private var isShowingAd = false
    private var myApplication: MyApplication = globalClass
    private var fullScreenContentCallback: FullScreenContentCallback? = null


    init {
        this.myApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    /**
     * Request an ad
     */
    fun fetchAd() {
        // Have unused ad, no need to fetch another.
        if (isAdAvailable()) {
            return
        }

        /*
          Called when an app open ad has failed to load.

          @param loadAdError the error.
         */
        // Handle the error.
        val loadCallback: AppOpenAdLoadCallback = object : AppOpenAdLoadCallback() {
            /**
             * Called when an app open ad has loaded.
             *
             * @param ad the loaded app open ad.
             */

            override fun onAdLoaded(ad: AppOpenAd) {
                appOpenAd = ad
                Log.d(log, "loaded")
            }

            /**
             * Called when an app open ad has failed to load.
             *
             * @param loadAdError the error.
             */
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                // Handle the error.
                Log.d(log, "error")
            }
        }
        val request: AdRequest = getAdRequest()
        /*AppOpenAd.load(
            myApplication, globalClass.getString(R.string.appOpenId), request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback
        )*/
    }

     fun showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.

        //AppOpen
        if (!isShowingAd && isAdAvailable()) {
            Log.d(log, "Will show ad.")
            fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Set the reference to null so isAdAvailable() returns false.
                    appOpenAd = null
                    isShowingAd = false
                    adVisible = false
                    fetchAd()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {

                }

                override fun onAdShowedFullScreenContent() {
                    isShowingAd = true
                }
            }

            adVisible = true

            appOpenAd?.fullScreenContentCallback = fullScreenContentCallback
            currentActivity?.let { appOpenAd?.show(it) }

        } else {
            Log.d(log, "Can not show ad.")
            fetchAd()
        }
    }

    /**
     * Creates and returns ad request.
     */
    @NotNull
    private fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
     fun isAdAvailable(): Boolean {
        return appOpenAd != null
    }


    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
    }

    override fun onActivityStarted(p0: Activity) {
        currentActivity = p0
    }

    override fun onActivityResumed(p0: Activity) {
        currentActivity = p0
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }

    override fun onStateChanged(p0: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_START) {
            MyApplication.isForegrounded = true
            currentActivity?.let {
                if (it !is SplashActivity && !isInterstitialAdOnScreen) {
                    showAdIfAvailable()
                }
            }
        }
        if (event == Lifecycle.Event.ON_STOP) {
            MyApplication.isForegrounded = false
        }
    }
}