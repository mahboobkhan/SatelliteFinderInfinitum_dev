package com.example.satellitefinder.ui.activites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.adssdk.native_ad.NativeAdType
import com.example.adssdk.native_ad.NativeAdUtils
import com.example.adssdk.utils.toast
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.databinding.ActivityExitBinding
import com.example.satellitefinder.databinding.NativeAdLayoutMainBinding
import com.example.satellitefinder.repo.SatelliteViewModel
import com.example.satellitefinder.ui.adapters.ExitViewPagerAdapter
import com.example.satellitefinder.utils.FirebaseEvents
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.isInternetConnected
import com.example.satellitefinder.utils.screenEventAnalytics
import com.example.satellitefinder.utils.startActivityWithSlideTransition
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExitBinding

    val arrayOfFeature by lazy {
        arrayOf(
            "angleMeter",
            "satellite",
            "issTracker"
        )
    }

    private val viewModels: SatelliteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseEvents.logEventActivity("exit_screen", "exit_screen")

        binding.viewPager.adapter = ExitViewPagerAdapter(this@ExitActivity, arrayOfFeature) {
            when (it) {
                "angleMeter" -> {
                    startActivityWithSlideTransition(AngleMeterActivity::class.java)
                }

                "satellite" -> {
                    if (isInternetConnected()){
                        startActivityWithSlideTransition(
                            SatelliteTypeSelection::class.java,
                            Bundle().apply { putString("fromWhere", "meter") })
                    }else{
                        startActivityWithSlideTransition(SatelliteFindActivity::class.java)
                    }

                }

                "issTracker" -> {
                    if (isInternetConnected()){
                        viewModels.loadSatellites("iss")
                    }else{
                        toast("No internet connection")
                    }
                }
            }
        }
        binding.dotsIndicator.attachTo(binding.viewPager)

        binding.viewPager.currentItem = 1

        initEvents()
        showNativeAd()
        onBackPressedDispatcher.addCallback(this@ExitActivity, callback)

        viewModels.satelliteList.observe(this@ExitActivity) {
            if (it.isNotEmpty()) {
                val item = it.firstOrNull()
                Intent(this, NewSatelliteMeterActivity::class.java).apply {
                    putExtra("fromIssTracker", true)
                    putExtra("satelliteName", item?.name)
                    putExtra("satelliteLat", item?.latitude)
                    putExtra("satelliteLng", item?.longitude)
                    putExtra("satelliteAzimuth", item?.azimuth)
                    putExtra("satelliteElevation", item?.elevation)
                    startActivity(this)
                }

            }else{
                    toast("No satellites found")
            }
        }
    }

    private fun initEvents() {
        binding.apply {
            btnCancel.setOnClickListener {
                FirebaseEvents.logEvent("exit_screen_click_close", "exit_screen_click_close")
                onBackPressedDispatcher.onBackPressed()
            }

            btnStay.setOnClickListener {
                FirebaseEvents.logEvent("exit_screen_click_stay", "exit_screen_click_stay")
                onBackPressedDispatcher.onBackPressed()
            }

            btnLeave.setOnClickListener {
                FirebaseEvents.logEvent("exit_screen_click_leave", "exit_screen_click_leave")
                finishAffinity()
            }
        }
    }

    private fun showNativeAd() {
        if (canWeShowAds(RemoteConfig.nativeExit)) {
//            binding.layoutNative.visibility = View.VISIBLE
            screenEventAnalytics("Exit Dialog")

            /*if (exitNativeAd != null) {
                showLoadedNativeAd(this, binding.layoutNative, R.layout.native_ad_layout_main, exitNativeAd!!)
            } else {
                binding.layoutNative.visibility = View.GONE
            }*/
            binding.layoutNative.visibility = View.VISIBLE
            val bindAdNative = NativeAdLayoutMainBinding.inflate(layoutInflater)

            NativeAdUtils(this@ExitActivity.application, "exit").loadNativeAd(
                adsKey = getString(R.string.exitNativeId),
                remoteConfig = RemoteConfig.nativeExit,
                nativeAdType = NativeAdType.DEFAULT_AD,
                adContainer = binding.layoutNative,
                nativeAdView = bindAdNative.root,
                adHeadline = bindAdNative.adHeadline,
                adBody = null,
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

    private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}