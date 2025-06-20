package com.example.satellitefinder.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.databinding.FragmentOneBinding
import com.example.satellitefinder.ui.activites.OnBoardingScreen
import com.example.satellitefinder.utils.FirebaseEvents
import com.example.satellitefinder.utils.canWeShowAds
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

class Fragment_One : Fragment() {
    private lateinit var binding: FragmentOneBinding

    private var inLineAdaptiveWithCustomHeightAdView: AdView? = null
    private val TAG = "FirstOnBoardingFragment"
    private var isInLineAdaptiveWithCustomHeightBannerLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOneBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNext.setOnClickListener {
            FirebaseEvents.logEvent("intro_screen_click_next_1", "intro_screen_click_next_1")
            OnBoardingScreen.viewPager?.currentItem = 1
        }

        showBannerAd()
    }


    private fun showBannerAd() {
        requireActivity().let {
            if (it.canWeShowAds(RemoteConfig.onBoarding1Banner)) {
                binding.bannerAd.visibility = View.VISIBLE
                binding.clShimmer.visibility = View.VISIBLE


                loadInLineAdaptiveWithCustomHeightBanner(
                    activity = requireActivity(),
                    bannerAdId = getString(R.string.bannerId),
                    height = 150,
                    adViewContainer = binding.bannerAd
                )

            } else {
                binding.bannerAd.visibility = View.GONE
            }
        }
    }

    private fun loadInLineAdaptiveWithCustomHeightBanner(
        activity: Activity,
        bannerAdId: String,
        height: Int,
        adViewContainer: FrameLayout,
    ) {

        if (inLineAdaptiveWithCustomHeightAdView?.parent != null) {
            (inLineAdaptiveWithCustomHeightAdView?.parent as FrameLayout).removeView(
                inLineAdaptiveWithCustomHeightAdView
            )
        }

        if (inLineAdaptiveWithCustomHeightAdView != null) {
            adViewContainer.removeAllViews()
            binding.clShimmer.visibility = View.GONE
            adViewContainer.addView(inLineAdaptiveWithCustomHeightAdView)
            return
        }

        if (isInLineAdaptiveWithCustomHeightBannerLoading) {
            return
        }

        isInLineAdaptiveWithCustomHeightBannerLoading = true

        val adSize =
            AdSize.getInlineAdaptiveBannerAdSize(
                if (adViewContainer.width <= 150) 320 else adViewContainer.width,
                height
            )


        inLineAdaptiveWithCustomHeightAdView = AdView(activity)
        inLineAdaptiveWithCustomHeightAdView?.adUnitId = bannerAdId
        inLineAdaptiveWithCustomHeightAdView?.setAdSize(adSize)

        // Replace ad container with new ad view.
        adViewContainer.removeAllViews()
        adViewContainer.addView(inLineAdaptiveWithCustomHeightAdView)

        // [START load_ad]
        // Start loading the ad in the background.
        val adRequest =
            AdRequest.Builder().build()
        inLineAdaptiveWithCustomHeightAdView?.loadAd(adRequest)

        inLineAdaptiveWithCustomHeightAdView?.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                isInLineAdaptiveWithCustomHeightBannerLoading = false
                binding.clShimmer.visibility = View.GONE

                Log.d(TAG, "Banner onAdLoaded:")

            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                binding.bannerAd.visibility = View.GONE
                binding.clShimmer.visibility = View.GONE
                inLineAdaptiveWithCustomHeightAdView = null
                isInLineAdaptiveWithCustomHeightBannerLoading = false
                Log.d(TAG, "Banner onAdFailedToLoad: ${p0.message}")
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Log.d(TAG, "Banner onAdImpression:")
                inLineAdaptiveWithCustomHeightAdView = null

            }

            override fun onAdClicked() {
                super.onAdClicked()
                Log.d(TAG, "Banner onAdClicked:")
            }
        }

    }
}