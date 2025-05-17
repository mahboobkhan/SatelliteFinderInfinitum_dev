package com.example.satellitefinder.ui.dialogs

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adssdk.banner_ads.BannerAdUtils
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.showBannerAD
import com.example.satellitefinder.databinding.SatelliteInfoSheetBinding
import com.example.satellitefinder.utils.canWeShowAds
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class InfoSheet(private val activity: Activity) {
    fun showSheet(onSheetDisplay: ((SatelliteInfoSheetBinding) -> Unit)) {
        val sheetBinding = SatelliteInfoSheetBinding.inflate(LayoutInflater.from(activity))
        val dialog = BottomSheetDialog(activity)
        dialog.setContentView(sheetBinding.root)

        val bottomSheet =
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            it.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
            behavior.state = BottomSheetBehavior.STATE_EXPANDED // Optional: Start fully expanded
        }

        onSheetDisplay.invoke(sheetBinding)

        showBannerAd(sheetBinding)

        dialog.show()
    }

    private fun showBannerAd(binding: SatelliteInfoSheetBinding) {
        if (activity.canWeShowAds(RemoteConfig.infoSheetBanner)) {
            showBannerAD(binding.bannerAdContainer, activity, activity.getString(R.string.bannerId))
            binding.bannerAdContainer.visibility = View.VISIBLE
            BannerAdUtils(activity = activity, screenName = "info_sheet")
                .loadBanner(
                    adsKey = activity.getString(R.string.bannerId),
                    remoteConfig = RemoteConfig.infoSheetBanner,
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
                    }
                )
        } else {
            binding.bannerAdContainer.visibility = View.GONE
            binding.clShimmer.visibility = View.GONE
        }
    }
}