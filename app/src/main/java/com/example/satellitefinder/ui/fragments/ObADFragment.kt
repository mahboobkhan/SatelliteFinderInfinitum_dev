package com.example.satellitefinder.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.newLoadAndShowNativeAd
import com.example.satellitefinder.admobAds.obNativeAdFull
import com.example.satellitefinder.admobAds.showLoadedNativeAd
import com.example.satellitefinder.databinding.FragmentObADBinding
import com.example.satellitefinder.ui.activites.OnBoardingScreen
import com.example.satellitefinder.utils.canWeShowAds

class ObADFragment : Fragment() {
    private lateinit var binding: FragmentObADBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                obNativeAdFull?.let {
                    showLoadedNativeAd(this, binding.nativeAdContainer,
                        R.layout.native_ad_layout_full, it)
                } ?: run {
                    newLoadAndShowNativeAd(
                        binding.nativeAdContainer,
                        R.layout.native_ad_layout_full,
                        getString(R.string.obNativeFull)
                    )
                }
            } else {
                binding.nativeAdContainer.visibility = View.GONE
            }
        }
    }
}