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
import com.example.satellitefinder.databinding.FragmentObADBinding
import com.example.satellitefinder.databinding.NativeAdLayoutFullBinding
import com.example.satellitefinder.ui.activites.OnBoardingScreen
import com.example.satellitefinder.utils.canWeShowAds

class ObADFragment : Fragment() {
    private lateinit var binding: FragmentObADBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentObADBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSkip.setOnClickListener {
            OnBoardingScreen.viewPager?.currentItem = 3
        }

        showNativeAd()
    }

    private fun showNativeAd() {
        activity?.apply {
            if (canWeShowAds(RemoteConfig.onBoardingNative)) {

                binding.nativeAdContainer.visibility = View.VISIBLE
                val bindAdNative = NativeAdLayoutFullBinding.inflate(layoutInflater)

                NativeAdUtils(requireActivity().application, "intro_full").loadNativeAd(
                    adsKey = getString(R.string.onBoardingNativeId),
                    remoteConfig = RemoteConfig.onBoardingNative,
                    nativeAdType = NativeAdType.EXIT_SCREEN_AD,
                    adContainer = binding.nativeAdContainer,
                    nativeAdView = bindAdNative.root,
                    adHeadline = bindAdNative.adHeadline,
                    adBody = bindAdNative.adBody,
                    adIcon = null,
                    mediaView = bindAdNative.adMedia,
                    adSponsor = null,
                    callToAction = bindAdNative.callToAction,
                    adLoaded = {

                    }, adFailed = { _, _ ->

                    }, adImpression = {

                    }, adClicked = {

                    }, adValidate = {
                        binding.nativeAdContainer.visibility = View.GONE
                    }
                )
            } else {
                binding.nativeAdContainer.visibility = View.GONE
            }
        }
    }
}