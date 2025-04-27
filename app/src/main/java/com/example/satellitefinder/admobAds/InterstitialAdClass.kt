package com.example.satellitefinder.admobAds

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.satellitefinder.R
import com.example.satellitefinder.utils.isInternetConnected
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

open class InterstitialAdClass {
    private val logTag = "interstitialAdFlow"

    private var someOpPerformed = false

    companion object {
        @Volatile
        private var instance: InterstitialAdClass? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: InterstitialAdClass().also { instance = it }
        }
    }

    /**
     *pre load interstitial ad by priority and show later when needed for splash and other
     * */

    fun loadAndShowSplashInterstitial(
        activity: Activity,
        adIDLow: String,
        closeListener: (() -> Unit)? = null,
        failListener: (() -> Unit)? = null,
        showListener: (() -> Unit)? = null
    ) {
        showLoadingDialog(activity)

        InterstitialAd.load(
            activity,
            adIDLow,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {

                override fun onAdFailedToLoad(ad: LoadAdError) {
                    interstitialAdPriority = null
                    dismissLoadingDialog(activity)
                    failListener?.invoke()
                    showInterstitialAdLog("High Ad failed to load because $ad")
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAdPriority = ad
                    interstitialAdPriority?.let {
                        showInterstitialAdLog("priority Ad is not null , Calling show and setting listener ...")
                        it.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                closeListener?.invoke()
                                showInterstitialAdLog("priority Ad closed by user")
                                isInterstitialAdOnScreen = false
                                interstitialAdPriority = null
                                loadAgainPriorityInterstitialAd(
                                    activity,
                                    activity.getString(R.string.interstialId)
                                )
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                someOpPerformed = true
                                interstitialAdPriority = null
                                failListener?.invoke()
                                showInterstitialAdLog("priority Ad failed to show")
                                dismissLoadingDialog(activity)

                            }

                            override fun onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent()
                                isInterstitialAdOnScreen = true
                                showListener?.invoke()
                                showInterstitialAdLog("priority Ad successfully showed")
                                interstitialAdPriority = null
                                someOpPerformed = true
                                Handler().postDelayed({
                                    dismissLoadingDialog(activity)
                                }, 1000)
                            }
                        }

                        someOpPerformed = false

                        Handler().postDelayed({
                            if (ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(
                                    Lifecycle.State.RESUMED
                                )
                            ) {
                                it.show(activity)
                            } else {
                                dismissLoadingDialog(activity)
                            }

                        }, 1000)

                    } ?: run {

                    }

                }
            })
    }

    fun loadPriorityInterstitialAds(
        context: Context,
        adIDLow: String
    ) {
        interstitialAdPriority?.let { return }
        showInterstitialAdLog("Loading High Ad 2...")
        loadInterstitialAdPriority(context, adIDLow) {}
    }

    private fun loadInterstitialAdPriority(
        context: Context, adId: String, adLoadedCallback: () -> Unit
    ) {
        context.let {
            interstitialAdPriority?.let { return }
            InterstitialAd.load(
                it,
                adId,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {

                    override fun onAdFailedToLoad(ad: LoadAdError) {
                        adLoadedCallback.invoke()
                        showInterstitialAdLog("High Ad failed to load because $ad")
                    }

                    override fun onAdLoaded(ad: InterstitialAd) {
                        interstitialAdPriority = ad
                        showInterstitialAdLog("High Ad successfully loaded")
                    }
                })
        }
    }

    fun showPriorityInterstitialAdNew(
        activity: Activity,
        loadAgain: Boolean = false,
        adIDLow: String? = null,
        loadAd: ((InterstitialAd) -> Unit)? = null,
        closeListener: (() -> Unit)? = null,
        failListener: (() -> Unit)? = null,
        showListener: (() -> Unit)? = null
    ) {
        showInterstitialAdLog("Showing priority Ad ...")
        if (activity.isInternetConnected()) {
            interstitialAdPriority?.let {
                showLoadingDialog(activity)
                showInterstitialAdLog("priority Ad is not null , Calling show and setting listener ...")
                it.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        showInterstitialAdLog("priority Ad closed by user")
                        isInterstitialAdOnScreen = false
                        interstitialAdPriority = null
                        closeListener?.invoke()
                        if (loadAgain) {
                            loadAgainPriorityInterstitialAd(activity, adIDLow)
                        }
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        someOpPerformed = true
                        interstitialAdPriority = null
                        showInterstitialAdLog("priority Ad failed to show")
                        failListener?.invoke()
                        dismissLoadingDialog(activity)
                        if (loadAgain) {
                            loadAgainPriorityInterstitialAd(activity, adIDLow)
                        }
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        isInterstitialAdOnScreen = true
                        showInterstitialAdLog("priority Ad successfully showed")
                        interstitialAdPriority = null
                        someOpPerformed = true
                        showListener?.invoke()
                        Handler().postDelayed({
                            dismissLoadingDialog(activity)
                        }, 500)
                    }
                }

                someOpPerformed = false

                Handler().postDelayed({
                    if (ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        it.show(activity)
                    } else {
                        dismissLoadingDialog(activity)
                    }

                }, 500)

            } ?: run {
                if (loadAgain) {
                    loadAgainPriorityInterstitialAd(activity, adIDLow)
                }
            }
        }
    }

    private fun loadAgainPriorityInterstitialAd(
        activity: Activity,
        adIDLow: String?
    ) {
        if (adIDLow != null) {
            loadPriorityInterstitialAds(activity, adIDLow)
        }
    }


    /***************************************************************************
     * Normal Ad Flow
     * ***************************************************************************/

    private fun showInterstitialAdLog(message: String) {
        Log.d(logTag, message)
    }

    private var loadingDialog: Dialog? = null
    private fun showLoadingDialog(activity: Activity) {
        loadingDialog = Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        loadingDialog?.setContentView(R.layout.ad_loading_dialog)
        loadingDialog?.setCancelable(false)
        if (loadingDialog?.isShowing == false) {
            loadingDialog?.show()
        }
    }

    private fun dismissLoadingDialog(activity: Activity) {
        if (loadingDialog != null && loadingDialog?.isShowing == true) {
            if (!activity.isDestroyed) {
                loadingDialog?.dismiss()
                loadingDialog = null
            }
        }
    }

}