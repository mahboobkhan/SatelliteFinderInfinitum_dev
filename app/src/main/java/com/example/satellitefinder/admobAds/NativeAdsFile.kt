package com.example.satellitefinder.admobAds

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.LayoutRes
import com.example.satellitefinder.BuildConfig
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.example.satellitefinder.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val nativeAdFlow = "nativeAdFlow"
fun showNativeLog(msg: String) {
    Log.d("showNativeLog", "showNativeLog: $msg")
//    showLogMessage(nativeAdFlow, msg)
}



fun populateUnifiedNativeAdView(
    nativeAd: NativeAd,
    adView: NativeAdView,
    scaleType: ImageView.ScaleType?
) {
    adView.mediaView = adView.findViewById(R.id.adMedia)
    adView.headlineView = adView.findViewById(R.id.adHeadline)
    adView.callToActionView = adView.findViewById(R.id.callToAction)
    adView.iconView = adView.findViewById(R.id.adIcon)
    nativeAd.mediaContent?.let {
        adView.mediaView?.apply {
            setMediaContent(it)
            scaleType?.let { sType ->
                setImageScaleType(ImageView.ScaleType.FIT_XY)
            }
        }
    }

    nativeAd.headline?.let {
        (adView.headlineView as TextView).text = it
    } ?: changeTextToEmpty(adView.headlineView as TextView)

    nativeAd.icon?.let {
        (adView.iconView as ImageView?)?.apply {
            setImageDrawable(it.drawable)
        }
    } ?: hideView(adView.iconView)

    nativeAd.callToAction?.let {
        (adView.callToActionView as Button).text = it
    } ?: hideView(adView.callToActionView)
    val starRatingView = adView.findViewById<RatingBar>(R.id.ratingBar)
    nativeAd.starRating?.let {
        when {
            it > 0 -> starRatingView.rating = it.toFloat()
            else -> hideView(starRatingView)
        }
    } ?: myAddRating(starRatingView)


    adView.setNativeAd(nativeAd)


    val vc = nativeAd.mediaContent?.videoController

    vc?.apply {
        if (hasVideoContent()) {

            videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                override fun onVideoEnd() {
                    showNativeLog("native ad video ended 1")
                    super.onVideoEnd()
                }
            }
        } else {
            showNativeLog("native ad not contains video 1")
        }
    }


}

fun myAddRating(starRatingView: RatingBar) {
    when {
        BuildConfig.DEBUG -> starRatingView.rating = 3.5F
        else -> hideView(starRatingView)
    }
}

fun changeTextToEmpty(textView: TextView) {
    textView.text = ""
}

fun hideView(view: View?) {
    view?.visibility = View.GONE
}


