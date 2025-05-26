package com.example.satellitefinder.ui.activites

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.adssdk.native_ad.NativeAdType
import com.example.adssdk.native_ad.NativeAdUtils
import com.example.adssdk.open_app_ad.OpenAppAdState
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.AdsConsentManager
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.loadAdmobInterstitial
import com.example.satellitefinder.admobAds.showPriorityAdmobInterstitial
import com.example.satellitefinder.databinding.ActivitySplashBinding
import com.example.satellitefinder.databinding.NativeAdLayoutMainBinding
import com.example.satellitefinder.utils.FirebaseEvents
import com.example.satellitefinder.utils.LanguagesHelper
import com.example.satellitefinder.utils.MyApplication.Companion.canRequestAdByConsent
import com.example.satellitefinder.utils.baseConfig
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.isAlreadyPurchased
import com.example.satellitefinder.utils.isFromLang
import com.example.satellitefinder.utils.isPFromSplash
import com.example.satellitefinder.utils.isSplash
import com.example.satellitefinder.utils.startActivityWithSlideTransition

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        newBase?.let {
            LanguagesHelper.onAttach(it)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {

        } else {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // KoinStarter.start(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        OpenAppAdState().disabled("splash")
        isSplash = true
        isPFromSplash = true

        FirebaseEvents.logEventActivity("splash_screen", "splash_screen")


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("TAG", "User accepted the notifications!")
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }


        binding.btnGetStarted.setOnClickListener {
            FirebaseEvents.logEvent("splash_screen_click_start", "splash_screen_click_start")
            if (canWeShowAds(RemoteConfig.interSplash)) {
                showPriorityAdmobInterstitial(
                    canReload = false, showListener = {
                        isInterstitialShowedOnSplash = true
                    }, closeListener = {
                        moveNext()
                    }, failListener = {
                        isInterstitialShowedOnSplash = false
                        moveNext()
                    })
            } else {
                moveNext()
            }
        }


    }

    private fun moveNext() {
        if (!baseConfig.isOnBoardingDone) {
            FirebaseEvents.logEvent("splash_screen_move_to_intro", "splash_screen_move_to_intro")
            val bundle = Bundle()
            bundle.putBoolean("isFirstTime", true)
            startActivityWithSlideTransition(OnBoardingScreen::class.java, bundle)
            finish()
        } else if (baseConfig.isOnPermissionDone) {

            if (isAlreadyPurchased()) {
                FirebaseEvents.logEvent("splash_screen_move_to_home", "splash_screen_move_to_home")
                startActivityWithSlideTransition(MainActivity::class.java)
                finish()
            } else {
                FirebaseEvents.logEvent("splash_screen_move_to_home", "splash_screen_move_to_home")
                val subscriptionIntent = Intent(this, SubscriptionActivity::class.java)
                subscriptionIntent.putExtra("fromSplash", true)
                startActivity(subscriptionIntent)
                finish()
            }
        } else {
            FirebaseEvents.logEvent(
                "splash_screen_move_to_permission",
                "splash_screen_move_to_permission"
            )
            val bundle = Bundle()
            bundle.putBoolean("isFirstTime", true)
            startActivityWithSlideTransition(PermissionActivity::class.java, bundle)
            finish()
        }
    }

    private fun loadAds() {
        showNativeAd()
        if (canWeShowAds(RemoteConfig.interSplash)) {
//            loadPriorityAdmobInterstitial(getString(R.string.splashInterstial))
            startHandler(7000)
            loadAdmobInterstitial(getString(R.string.splashInterstial), onAdLoaded = {
                binding.progress.visibility = View.GONE
                binding.btnGetStarted.visibility = View.VISIBLE
            })
        } else {
            binding.progress.visibility = View.GONE
            binding.btnGetStarted.visibility = View.VISIBLE
            binding.layoutNative.visibility = View.GONE
        }
    }

    private fun startHandler(delayTime: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            kotlin.run {
                binding.progress.visibility = View.GONE
                binding.btnGetStarted.visibility = View.VISIBLE
            }

        }, delayTime)
    }

    private fun showNativeAd() {
        if (canWeShowAds(RemoteConfig.nativeSplash)) {
            /*newLoadAndShowNativeAd(
                binding.layoutNative,
                R.layout.native_ad_layout_main,
                getString(R.string.splashNativeId),
                adLoading = {
                    binding.layoutNative.visibility = View.VISIBLE
                },
                failToLoad = {
                    binding.layoutNative.visibility = View.GONE
                })*/
            binding.layoutNative.visibility = View.VISIBLE
            val bindAdNative = NativeAdLayoutMainBinding.inflate(layoutInflater)

            NativeAdUtils(this@SplashActivity.application, "splash").loadNativeAd(
                adsKey = getString(R.string.splashNativeId),
                remoteConfig = RemoteConfig.nativeSplash,
                nativeAdType = NativeAdType.DEFAULT_AD,
                adContainer = binding.layoutNative,
                nativeAdView = bindAdNative.root,
                adHeadline = bindAdNative.adHeadline,
                adBody = null,
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

        /*if (canWeShowAds(RemoteConfig.onBoardingNative)) {
            //Pre load ob Native
            loadAndReturnAd(this, getString(R.string.onBoardingNativeId)) { adState ->
                obNativeAd1 = adState
            }

            //Pre load ob Native Full
            loadAndReturnAd(this, getString(R.string.obNativeFull)) { adState ->
                obNativeAdFull = adState
            }
        }*/
    }

    private fun requestConsentForm() {
        RemoteConfig.fetchRecord(this) {
            val adsConsentManager = AdsConsentManager(this)
            adsConsentManager.requestUMP(object : AdsConsentManager.UMPResultListener {
                override fun onCheckUMPSuccess(canRequestAds: Boolean) {
                    canRequestAdByConsent = canRequestAds
                    if (canRequestAds) {
                        runOnUiThread { loadAds() }
                    } else {
                        runOnUiThread {
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                }
            })
        }
    }


    /*private fun initializeRemoteConfig() {
        CoroutineScope(Dispatchers.IO).launch {

            remoteConfigViewModel.getRemoteConfigSplash(this@SplashActivity)
        }.invokeOnCompletion() {

            CoroutineScope(Dispatchers.Main).launch {
                remoteConfigViewModel.remoteConfig.observe(this@SplashActivity) { remoteConfig ->
                    remoteConfigViewModel.remoteConfigCounter.observe(
                        this@SplashActivity
                    ) { remoteConfig2 ->
                        if (remoteConfig?.InterstitialMain?.value == 1 || remoteConfig?.splashNative?.value == 1) {
                            remoteAdCounter = remoteConfig2.mainScreenCounter.value
                            loadAds()
                            if (remoteConfig?.openAppAdID?.value == 1 || remoteConfig?.openAppAdID?.value == 1) {
                                OpenApp(application as MyApplication)
                            }
                        } else {
                            binding.layoutNative.visibility = View.GONE
                            startHandler(5000)
                        }
                    }
                }
            }
        }
    }*/

    override fun onResume() {
        super.onResume()
        if (isFromLang) {
            if (canWeShowAds(RemoteConfig.nativeSplash)) {
                showNativeAd()
            }
            startHandler(5000)
        } else {
            requestConsentForm()

        }
    }

    companion object {
        var isInterstitialShowedOnSplash = false
    }
}