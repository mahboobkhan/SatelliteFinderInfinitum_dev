package com.example.satellitefinder.ui.activites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adssdk.native_ad.NativeAdType
import com.example.adssdk.native_ad.NativeAdUtils
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.showPriorityInterstitialAdWithCounter
import com.example.satellitefinder.databinding.ActivityLanguagesBinding
import com.example.satellitefinder.databinding.NativeAdLayoutSmallBinding
import com.example.satellitefinder.ui.adapters.LanguagesAdapter
import com.example.satellitefinder.utils.FirebaseEvents
import com.example.satellitefinder.utils.LanguagesHelper
import com.example.satellitefinder.utils.LanguagesModel
import com.example.satellitefinder.utils.MySharePrefrencesHelper
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.isFromLang
import com.example.satellitefinder.utils.screenEventAnalytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LanguagesActivity : AppCompatActivity() {

    private var adapter: LanguagesAdapter? = null
    private var myLangugesList = mutableListOf<LanguagesModel>()
    private var selectedLanguageCode: String = "en"
    val binding: ActivityLanguagesBinding by lazy {
        ActivityLanguagesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseEvents.logEventActivity("language_screen", "language_screen")
        setContentView(binding.root)

        showNativeAd()

        screenEventAnalytics("LanguagesActivity")

        LanguagesHelper.languagesList.forEach { model ->
            myLangugesList.add(model)
        }

        adapter = LanguagesAdapter(myLangugesList) {
            selectedLanguageCode = it
        }
        binding.languageRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.languageRv.adapter = adapter


        binding.backArrow.setOnClickListener {
            FirebaseEvents.logEvent("language_screen_click_back", "language_screen_click_back")
            finish()
        }



        binding.btnDone.setOnClickListener {
            FirebaseEvents.logEvent("language_screen_click_done", "language_screen_click_done")
            CoroutineScope(Dispatchers.Main).launch {
                delay(10)
                if (selectedLanguageCode != MySharePrefrencesHelper.getKey(
                        this@LanguagesActivity,
                        "langCode",
                        "en"
                    )
                ) {
                    MySharePrefrencesHelper.putKey(
                        this@LanguagesActivity,
                        "langCode",
                        selectedLanguageCode
                    )
                    val restartIntent = Intent(this@LanguagesActivity, SplashActivity::class.java)
                    restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    isFromLang = true
                    startActivity(restartIntent)
                    finishAffinity()
                } else {
                    finish()
                }
            }
        }
    }

    private fun showNativeAd() {
        if (canWeShowAds(RemoteConfig.languagesNative)) {
            /*newLoadAndShowNativeAd(
                binding.layoutNative,
                R.layout.native_ad_layout_small,
                getString(R.string.languagesNativeId),
                adLoading = {
                    binding.layoutNative.visibility = View.VISIBLE
                },
                failToLoad = {
                    binding.layoutNative.visibility = View.GONE
                })*/

            binding.layoutNative.visibility = View.VISIBLE
            val bindAdNative = NativeAdLayoutSmallBinding.inflate(layoutInflater)

            NativeAdUtils(this@LanguagesActivity.application, "language").loadNativeAd(
                adsKey = getString(R.string.languagesNativeId),
                remoteConfig = RemoteConfig.languagesNative,
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