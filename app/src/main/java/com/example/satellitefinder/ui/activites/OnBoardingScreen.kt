package com.example.satellitefinder.ui.activites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.loadAndReturnAd
import com.example.satellitefinder.databinding.ActivityOnBoardingScreenBinding
import com.example.satellitefinder.ui.adapters.ViewPagerAdapter
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.isFirstTime

class OnBoardingScreen : AppCompatActivity() {
    private val binding: ActivityOnBoardingScreenBinding by lazy {
        ActivityOnBoardingScreenBinding.inflate(layoutInflater)
    }

    companion object {
        var viewPager: ViewPager? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isFirstTime = true

        setContentView(binding.root)

        if (canWeShowAds(RemoteConfig.permissionNative)) {
            loadAndReturnAd(
                this@OnBoardingScreen,
                getString(R.string.permissionNativeId)
            ) {
                PermissionActivity.permissionNativeAd = it
            }
        }

        setUpViewPager()
    }

    private fun setUpViewPager() {
        viewPager = binding.viewPager

        val adapter = ViewPagerAdapter(supportFragmentManager, canWeShowAds(RemoteConfig.onBoardingNative))
        viewPager?.adapter = adapter
    }
}