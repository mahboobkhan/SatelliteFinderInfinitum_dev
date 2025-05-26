package com.example.satellitefinder.ui.activites

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adssdk.banner_ads.BannerAdUtils
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.databinding.ActivityRulerLevelBinding
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.custom_views.RulerUnit

class RulerLevelActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRulerLevelBinding.inflate(layoutInflater) }

    var unit = "cm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        setUpClickListeners()
        showBannerAd()
    }

    private fun setUpClickListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.ivUnit.setOnClickListener {
            when (unit) {
                "cm" -> {
                    unit = "in"
                    binding.ivUnit.setImageResource(R.drawable.ic_ruler_cm)
                    binding.rulerView.setUnit(RulerUnit.IN)
                }

                "in" -> {
                    unit = "cm"
                    binding.ivUnit.setImageResource(R.drawable.ic_ruler_inch)
                    binding.rulerView.setUnit(RulerUnit.CM)
                }
            }
        }
    }

    private fun showBannerAd() {
        if (canWeShowAds(RemoteConfig.rulerLevelBanner)) {
            binding.bannerAdContainer.visibility = View.VISIBLE
            BannerAdUtils(activity = this, screenName = "level")
                .loadBanner(
                    adsKey = getString(R.string.bannerId),
                    remoteConfig = RemoteConfig.rulerLevelBanner,
                    adsView = binding.bannerAdContainer,
                    shimmerLayout = binding.clShimmer,
                    onAdClicked = {},
                    onAdFailedToLoads = { _, _ -> },
                    onAdImpression = {},
                    onAdLoaded = { _, _ -> },
                    onAdOpened = {},
                    onAdValidate = {
                        binding.bannerAdContainer.visibility = View.GONE
                        binding.clShimmer.visibility = View.GONE
                    })
        } else {
            binding.bannerAdContainer.visibility = View.GONE
            binding.clShimmer.visibility = View.GONE
        }
    }
}