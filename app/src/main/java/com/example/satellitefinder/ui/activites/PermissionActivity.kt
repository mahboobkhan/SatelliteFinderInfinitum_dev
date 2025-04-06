package com.example.satellitefinder.ui.activites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.loadAndShowInterstitial
import com.example.satellitefinder.admobAds.newLoadAndShowNativeAd
import com.example.satellitefinder.admobAds.showLoadedNativeAd
import com.example.satellitefinder.databinding.ActivityPermissionBinding
import com.example.satellitefinder.utils.AdState
import com.example.satellitefinder.utils.baseConfig
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.requestLocationPermissions

class PermissionActivity : AppCompatActivity() {

    val binding:ActivityPermissionBinding by lazy{
        ActivityPermissionBinding.inflate(layoutInflater)
    }


    companion object{
        var permissionNativeAd: AdState? =null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        showNativeAd()

        binding.btnAllow.setOnClickListener {
            baseConfig.isOnPermissionDone = true

            requestLocationPermissions {
                if (it) {
                    if (canWeShowAds(RemoteConfig.interSplash)) {
                        loadAndShowInterstitial(getString(R.string.splashInterstial), closeListener = {
                            moveNext()
                        }, failListener = {
                            moveNext()
                        })
                    } else {
                        moveNext()
                    }
                }
            }
        }

        binding.btnNotNow.setOnClickListener {
            if (canWeShowAds(RemoteConfig.interSplash)) {
                loadAndShowInterstitial(getString(R.string.splashInterstial), closeListener = {
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
        startActivity(Intent(this@PermissionActivity,MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        permissionNativeAd = null
    }
    private fun showNativeAd(){
        if (canWeShowAds(RemoteConfig.permissionNative)) {
            if (permissionNativeAd == null) {
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

            }
        }

    }
}