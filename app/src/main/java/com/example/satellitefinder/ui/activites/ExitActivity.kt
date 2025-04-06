package com.example.satellitefinder.ui.activites

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.showLoadedNativeAd
import com.example.satellitefinder.databinding.ActivityExitBinding
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.exitNativeAd
import com.example.satellitefinder.utils.screenEventAnalytics
import com.example.satellitefinder.utils.startActivityWithSlideTransition

class ExitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initEvents()
        showNativeAd()
        onBackPressedDispatcher.addCallback(this@ExitActivity, callback)
    }

    private fun initEvents() {
        binding.apply {
            btnCancel.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            btnStay.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            btnTry.setOnClickListener {
                startActivityWithSlideTransition(SatelliteFindActivity::class.java)
                finish()
            }

            btnLeave.setOnClickListener {
                finishAffinity()
            }
        }
    }

    private fun showNativeAd() {
        if (canWeShowAds(RemoteConfig.nativeExit)) {
            binding.layoutNative.visibility = View.VISIBLE
            screenEventAnalytics("Exit Dialog")

            if (exitNativeAd != null) {
                showLoadedNativeAd(this, binding.layoutNative, R.layout.native_ad_layout_main, exitNativeAd!!)
            } else {
                binding.layoutNative.visibility = View.GONE
            }
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