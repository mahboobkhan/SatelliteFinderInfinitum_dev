package com.example.satellitefinder.ui.activites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.adssdk.native_ad.NativeAdType
import com.example.adssdk.native_ad.NativeAdUtils
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.loadAdmobInterstitial
import com.example.satellitefinder.admobAds.loadAndShowInterstitial
import com.example.satellitefinder.admobAds.showPriorityAdmobInterstitial
import com.example.satellitefinder.databinding.ActivityPermissionBinding
import com.example.satellitefinder.databinding.NativeAdLayoutSmallBinding
import com.example.satellitefinder.utils.AdState
import com.example.satellitefinder.utils.FirebaseEvents
import com.example.satellitefinder.utils.baseConfig
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.isAlreadyPurchased
import com.example.satellitefinder.utils.requestLocationPermissions
import com.example.satellitefinder.utils.startActivityWithSlideTransition

class PermissionActivity : AppCompatActivity() {

    val binding: ActivityPermissionBinding by lazy {
        ActivityPermissionBinding.inflate(layoutInflater)
    }


    companion object {
//        var permissionNativeAd: AdState? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        FirebaseEvents.logEventActivity("permission_screen", "permission_screen")
        showNativeAd()
        if (canWeShowAds(RemoteConfig.interPermission)){
            loadAdmobInterstitial(getString(R.string.interstialId))
        }

        binding.btnAllow.setOnClickListener {
            baseConfig.isOnPermissionDone = true

            requestLocationPermissions {
                if (it) {
                    if (canWeShowAds(RemoteConfig.interPermission)) {
                        showPriorityAdmobInterstitial(
                            adIDLow = getString(R.string.interstialId),
                            closeListener = {
                                moveNext()
                            },
                            failListener = {
                                moveNext()
                            })
                    } else {
                        moveNext()
                    }
                }
            }
        }

        binding.btnNotNow.setOnClickListener {
            if (canWeShowAds(RemoteConfig.interPermission)) {
                showPriorityAdmobInterstitial(
                    adIDLow =getString(R.string.splashInterstial), closeListener = {
                    moveNext()
                }, failListener = {
                    moveNext()
                })
            } else {
                moveNext()
            }
        }
    }

    private fun moveNext() {
        if (isAlreadyPurchased()) {
            FirebaseEvents.logEvent(
                "permission_screen_move_to_home",
                "permission_screen_move_to_home"
            )
            startActivityWithSlideTransition(MainActivity::class.java)
            finish()
        } else {
            FirebaseEvents.logEvent(
                "permission_screen_move_to_home",
                "permission_screen_move_to_home"
            )
            val subscriptionIntent = Intent(this, SubscriptionActivity::class.java)
            subscriptionIntent.putExtra("fromSplash", true)
            startActivity(subscriptionIntent)
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
//        permissionNativeAd = null
    }

    private fun showNativeAd() {
        if (canWeShowAds(RemoteConfig.permissionNative)) {
            /*if (permissionNativeAd == null) {
                newLoadAndShowNativeAd(
                    binding.layoutNative,
                    R.layout.native_ad_layout_small,
                    getString(R.string.permissionNativeId),
                    adLoading = {
                        binding.layoutNative.visibility = View.VISIBLE
                    },
                    failToLoad = {
                        binding.layoutNative.visibility = View.GONE
                    })

            } else {
                showLoadedNativeAd(
                    this@PermissionActivity, binding.layoutNative, R.layout.native_ad_layout_small,
                    permissionNativeAd!!
                )

            }*/
            binding.layoutNative.visibility = View.VISIBLE
            val bindAdNative = NativeAdLayoutSmallBinding.inflate(layoutInflater)

            NativeAdUtils(this@PermissionActivity.application, "permission").loadNativeAd(
                adsKey = getString(R.string.permissionNativeId),
                remoteConfig = RemoteConfig.permissionNative,
                nativeAdType = NativeAdType.DEFAULT_AD,
                adContainer = binding.layoutNative,
                nativeAdView = bindAdNative.root,
                adHeadline = bindAdNative.adHeadline,
                adBody = bindAdNative.adBody,
                adIcon = bindAdNative.adIcon,
                mediaView = bindAdNative.adMedia,
                adSponsor = null,
                callToAction = bindAdNative.callToAction,
                adLoaded = {

                }, adFailed = { _, _ ->

                }, adImpression = {

                }, adClicked = {

                }, adValidate = {
                    binding.layoutNative.visibility = View.GONE
                }
            )
        } else {
            binding.layoutNative.visibility = View.GONE
        }

    }
}