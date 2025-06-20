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
import com.example.satellitefinder.databinding.FragmentTwoBinding
import com.example.satellitefinder.databinding.NativeAdLayoutSmallBinding
import com.example.satellitefinder.ui.activites.OnBoardingScreen
import com.example.satellitefinder.utils.FirebaseEvents
import com.example.satellitefinder.utils.canWeShowAds

class Fragment_Two : Fragment() {
    private lateinit var binding: FragmentTwoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvNext.setOnClickListener {
            FirebaseEvents.logEvent("intro_screen_click_next_2", "intro_screen_click_next_2")
            OnBoardingScreen.viewPager?.currentItem = 2
        }

        showNativeAd()
    }


    private fun showNativeAd() {
        activity?.apply {
            if (canWeShowAds(RemoteConfig.onBoardingNative)) {

                binding.layoutNative.visibility = View.VISIBLE
                val bindAdNative = NativeAdLayoutSmallBinding.inflate(layoutInflater)

                NativeAdUtils(requireActivity().application, "intro_2").loadNativeAd(
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