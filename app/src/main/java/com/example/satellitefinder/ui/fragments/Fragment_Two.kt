package com.example.satellitefinder.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.loadAndReturnAd
import com.example.satellitefinder.admobAds.newLoadAndShowNativeAd
import com.example.satellitefinder.admobAds.obNativeAd1
import com.example.satellitefinder.admobAds.obNativeAd2
import com.example.satellitefinder.admobAds.obNativeAd3
import com.example.satellitefinder.admobAds.showLoadedNativeAd
import com.example.satellitefinder.databinding.FragmentTwoBinding
import com.example.satellitefinder.ui.activites.OnBoardingScreen
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
            OnBoardingScreen.viewPager?.currentItem = 2
        }

        showNativeAd()
    }


    private fun showNativeAd() {
        activity?.apply {
            if (canWeShowAds(RemoteConfig.onBoardingNative)) {
                binding.layoutNative.visibility = View.VISIBLE
                obNativeAd2?.let { ad ->
                    showLoadedNativeAd(this, binding.layoutNative, R.layout.native_ad_layout_small, ad)
                } ?: run {
                    newLoadAndShowNativeAd(binding.layoutNative, R.layout.native_ad_layout_small, getString(R.string.onBoardingNativeId))
                }

                //Pre load 3rd
                loadAndReturnAd(this, getString(R.string.onBoardingNativeId)) { adState ->
                    obNativeAd3 = adState
                }
            } else {
                binding.layoutNative.visibility = View.GONE
            }
        }
    }
}