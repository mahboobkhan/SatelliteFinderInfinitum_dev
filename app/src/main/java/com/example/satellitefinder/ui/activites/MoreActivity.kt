package com.example.satellitefinder.ui.activites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.newLoadAndShowNativeAd
import com.example.satellitefinder.databinding.ActivityMoreBinding
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.privacyPolicy
import com.example.satellitefinder.utils.rateUs
import com.example.satellitefinder.utils.screenEventAnalytics
import com.example.satellitefinder.utils.sharesApp

class MoreActivity : AppCompatActivity() {
    val binding : ActivityMoreBinding by lazy {
        ActivityMoreBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        showNativeAd()

        screenEventAnalytics("MoreActivity")

        binding.apply{
            ivBack.setOnClickListener {
                finish()
            }
            btnRateUs.setOnClickListener {
                rateUs()
            }
            btnShare.setOnClickListener {
                sharesApp("Install this free Satellite Finder App")
            }
            btnPrivacy.setOnClickListener {
                privacyPolicy()
            }
            btnLanguages.setOnClickListener{
                startActivity(Intent(this@MoreActivity,LanguagesActivity::class.java))
                finish()
            }
            btnPremium.setOnClickListener {
                startActivity(Intent(this@MoreActivity,SubscriptionActivity::class.java))
                finish()
            }
        }
    }

    private fun showNativeAd(){
        if (canWeShowAds(RemoteConfig.moreNative)){
        newLoadAndShowNativeAd(
            binding.layoutNative,
            R.layout.native_ad_layout_small,
            getString(R.string.moreScreenNativeId),
            adLoading = {
                binding.layoutNative.visibility = View.VISIBLE
            },
            failToLoad = {
                binding.layoutNative.visibility = View.GONE
            })

    }
    }
}