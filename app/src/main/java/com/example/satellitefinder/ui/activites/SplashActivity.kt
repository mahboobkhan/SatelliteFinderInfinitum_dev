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
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.OpenApp

import com.example.satellitefinder.admobAds.loadPriorityAdmobInterstitial
import com.example.satellitefinder.databinding.ActivitySplashBinding
import com.example.satellitefinder.firebaseRemoteConfigurations.RemoteViewModel
import com.example.satellitefinder.utils.LanguagesHelper
import com.example.satellitefinder.utils.MyApplication
import com.example.satellitefinder.utils.baseConfig
import com.example.satellitefinder.utils.isFromLang
import com.example.satellitefinder.utils.isInternetConnected
import com.example.satellitefinder.utils.isPFromSplash
import com.example.satellitefinder.utils.isSplash
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import newLoadAndShowNativeAd
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.atomic.AtomicBoolean

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    val remoteConfigViewModel: RemoteViewModel by viewModel()

    private lateinit var consentInformation: ConsentInformation
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)
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
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        isSplash = true
        isPFromSplash = true



        if (isFromLang) {
            if (remoteConfigViewModel.getRemoteConfig(this@SplashActivity)?.splashNative?.value == 1){
                showNativeAd()
            }
            startHandler(7000)
        } else {
            requestConsentForm()

        }
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
            if (!baseConfig.isOnBoardingDone) {
                startActivity(
                    Intent(this, OnBoardingScreen::class.java).putExtra(
                        "isFirstTime",
                        true
                    )
                )
                finish()
            } else if (baseConfig.isOnPermissionDone) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(
                    Intent(this@SplashActivity, PermissionActivity::class.java).putExtra(
                        "isFirstTime",
                        true
                    )
                )
                finish()
            }
        }


    }

    private fun loadAds() {
        if (isInternetConnected()) {
            showNativeAd()
            if (baseConfig.isOnBoardingDone){
                loadPriorityAdmobInterstitial(getString(R.string.splashInterstial))
            }

            startHandler(9000)

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
        newLoadAndShowNativeAd(
            binding.layoutNative,
            R.layout.native_ad_layout_main,
            getString(R.string.splashNativeId),
            adLoading = {
                binding.layoutNative.visibility = View.VISIBLE
            },
            failToLoad = {
                binding.layoutNative.visibility = View.GONE
            })

    }


    private fun requestConsentForm() {
        // Set tag for under age of consent. false means users are not under age
        // of consent.
        val params = ConsentRequestParameters
            .Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    this@SplashActivity,
                    ConsentForm.OnConsentFormDismissedListener { loadAndShowError ->
                        // Consent gathering failed.
                        Log.e(
                            "TAG", String.format(
                                "%s: %s",
                                loadAndShowError?.errorCode,
                                loadAndShowError?.message
                            )
                        )

                        // Consent has been gathered.
                        if (consentInformation.canRequestAds()) {
                            initializeMobileAdsSdk()
                        }
                    }
                )
            },
            { requestConsentError ->
                // Consent gathering failed.
                Log.w(
                    "TAG", String.format(
                        "%s: %s",
                        requestConsentError.errorCode,
                        requestConsentError.message
                    )
                )
            })

        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk()
        }

    }


    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }

        // Initialize the Google Mobile Ads SDK.
        MobileAds.initialize(this)

        initializeRemoteConfig()

    }

    private fun initializeRemoteConfig() {
        CoroutineScope(Dispatchers.IO).launch {

            remoteConfigViewModel.getRemoteConfigSplash(this@SplashActivity)
        }.invokeOnCompletion() {

            CoroutineScope(Dispatchers.Main).launch {
                remoteConfigViewModel.remoteConfig.observe(this@SplashActivity, fun(remoteConfig) {
                    if (remoteConfig?.InterstitialMain?.value == 1 || remoteConfigViewModel.getRemoteConfig(this@SplashActivity)?.splashNative?.value == 1) {
                        loadAds()
                        if (remoteConfigViewModel.getRemoteConfig(this@SplashActivity)?.openAppAdID?.value ==1){
                            OpenApp(application as MyApplication)
                        }
                    }else{
                        binding.layoutNative.visibility = View.GONE
                        startHandler(5000)
                    }

                })
            }
        }
    }
}