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
import com.example.satellitefinder.admobAds.obNativeAd4
import com.example.satellitefinder.admobAds.showLoadedNativeAd
import com.example.satellitefinder.databinding.FragmentFourBinding
import com.example.satellitefinder.databinding.FragmentOneBinding
import com.example.satellitefinder.ui.activites.OnBoardingScreen
import com.example.satellitefinder.ui.activites.PermissionActivity
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
            activity?.startActivityWithSlideTransition(PermissionActivity::class.java)
        }

        showNativeAd()
    }

    private fun showNativeAd() {
        activity?.apply {
            if (canWeShowAds(RemoteConfig.onBoardingNative)) {
                binding.layoutNative.visibility = View.VISIBLE
                obNativeAd4?.let { ad ->
                    showLoadedNativeAd(this, binding.layoutNative, R.layout.native_ad_layout_small, ad)
                } ?: run {
                    newLoadAndShowNativeAd(binding.layoutNative, R.layout.native_ad_layout_small, getString(R.string.onBoardingNativeId))
                }
            } else {
                binding.layoutNative.visibility = View.GONE
            }
        }
    }
}