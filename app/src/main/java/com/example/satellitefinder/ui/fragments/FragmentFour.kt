package com.example.satellitefinder.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.adssdk.native_ad.NativeAdType
import com.example.adssdk.native_ad.NativeAdUtils
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.showPriorityAdmobInterstitial
import com.example.satellitefinder.databinding.FragmentFourBinding
import com.example.satellitefinder.databinding.NativeAdLayoutSmallBinding
import com.example.satellitefinder.ui.activites.PermissionActivity
import com.example.satellitefinder.ui.activites.SplashActivity
import com.example.satellitefinder.utils.FirebaseEvents
import com.example.satellitefinder.utils.baseConfig
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.startActivityWithSlideTransition


class FragmentFour : Fragment() {
    private lateinit var binding: FragmentFourBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFourBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNext.setOnClickListener {
            FirebaseEvents.logEvent("intro_screen_click_start", "intro_screen_click_start")
            context?.baseConfig?.isOnBoardingDone = true

            if (!SplashActivity.isInterstitialShowedOnSplash) {
                if (requireActivity().canWeShowAds(RemoteConfig.interSplash)) {
                    requireActivity().showPriorityAdmobInterstitial(
                        canReload = false, showListener = {
                            SplashActivity.isInterstitialShowedOnSplash = true
                        }, closeListener = {
                            activity?.startActivityWithSlideTransition(PermissionActivity::class.java)

                        }, failListener = {
                            SplashActivity.isInterstitialShowedOnSplash = false
                            activity?.startActivityWithSlideTransition(PermissionActivity::class.java)
                        })
                } else {
                    activity?.startActivityWithSlideTransition(PermissionActivity::class.java)
                }
            } else {
                activity?.startActivityWithSlideTransition(PermissionActivity::class.java)
            }
        }

        showNativeAd()
    }

    private fun showNativeAd() {
        activity?.apply {
            if (canWeShowAds(RemoteConfig.onBoardingNative)) {
                /*binding.layoutNative.visibility = View.VISIBLE
                obNativeAd4?.let { ad ->
                    showLoadedNativeAd(this, binding.layoutNative, R.layout.native_ad_layout_small, ad)
                } ?: run {
                    newLoadAndShowNativeAd(binding.layoutNative, R.layout.native_ad_layout_small, getString(R.string.onBoardingNativeId))
                }*/
                binding.layoutNative.visibility = View.VISIBLE
                val bindAdNative = NativeAdLayoutSmallBinding.inflate(layoutInflater)

                NativeAdUtils(requireActivity().application, "intro_4").loadNativeAd(
                    adsKey = getString(R.string.onBoardingNativeId),
                    remoteConfig = RemoteConfig.onBoardingNative,
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
}