package com.example.satellitefinder.ui.activites

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adssdk.native_ad.NativeAdType
import com.example.adssdk.native_ad.NativeAdUtils
import com.example.adssdk.open_app_ad.OpenAppAdState
import com.example.adssdk.utils.toast
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.showPriorityAdmobInterstitial
import com.example.satellitefinder.admobAds.showPriorityInterstitialAdWithCounter
import com.example.satellitefinder.databinding.ActivityMainBinding
import com.example.satellitefinder.databinding.NativeAdLayoutMainBinding
import com.example.satellitefinder.repo.SatelliteViewModel
import com.example.satellitefinder.ui.adapters.NavigationAdapter
import com.example.satellitefinder.ui.adapters.NavigationItemModel
import com.example.satellitefinder.ui.dialogs.RattingDialog
import com.example.satellitefinder.utils.ClickListener
import com.example.satellitefinder.utils.FirebaseEvents
import com.example.satellitefinder.utils.LanguagesHelper
import com.example.satellitefinder.utils.RecyclerTouchListener
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.isAlreadyPurchased
import com.example.satellitefinder.utils.isInternetConnected
import com.example.satellitefinder.utils.isSplash
import com.example.satellitefinder.utils.privacyPolicy
import com.example.satellitefinder.utils.rateUs
import com.example.satellitefinder.utils.screenEventAnalytics
import com.example.satellitefinder.utils.sharesApp
import com.example.satellitefinder.utils.showToast
import com.example.satellitefinder.utils.startActivityWithSlideTransition
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var rateUsDialog: RattingDialog
    private lateinit var drawerItems: ArrayList<NavigationItemModel>
    private val viewModels: SatelliteViewModel by viewModel()
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        newBase?.let {
            LanguagesHelper.onAttach(it)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        isSplash = false
        OpenAppAdState().enabled("splash")
        FirebaseEvents.logEventActivity("home_screen", "home_screen")
        screenEventAnalytics("MainActivity")

        showNativeAd()
        rateUsDialog = RattingDialog(
            this@MainActivity,
        )

        onBackPressedDispatcher.addCallback(this@MainActivity, callback)
        viewModels.satelliteList.observe(this@MainActivity) {
            if (it.isNotEmpty()) {
                showInterstitialAndNavigate(RemoteConfig.interIssTracker) {
                    val item = it.firstOrNull()
                    Intent(this, NewSatelliteMeterActivity::class.java).apply {
                        putExtra("fromIssTracker", true)
                        putExtra("satelliteName", item?.name)
                        putExtra("satelliteLat", item?.latitude)
                        putExtra("satelliteLng", item?.longitude)
                        putExtra("satelliteAzimuth", item?.azimuth)
                        putExtra("satelliteElevation", item?.elevation)
                        startActivity(this)
                    }
                }
            } else {
                toast("No satellites found")
            }
        }

        binding.apply {
            btnFindSatellite.setOnClickListener {
                FirebaseEvents.logEvent(
                    "home_screen_click_find_satellite",
                    "home_screen_click_find_satellite"
                )
                showInterstitialAndNavigate(RemoteConfig.interFindSatellite) {
                    if (!isInternetConnected()) {
                        startActivityWithSlideTransition(SatelliteFindActivity::class.java)
                    } else {
                        startActivityWithSlideTransition(
                            SatelliteTypeSelection::class.java,
                            Bundle().apply { putString("fromWhere", "meter") })
                    }
                }
            }

            btnSatelliteMap.setOnClickListener {
                FirebaseEvents.logEvent(
                    "home_screen_click_satellite_map",
                    "home_screen_click_satellite_map"
                )
                if (!isInternetConnected()) {
                    startActivityWithSlideTransition(MapSatelliteActivity::class.java)
                } else {
                    showInterstitialAndNavigate(RemoteConfig.interSatelliteMap) {
                        startActivityWithSlideTransition(
                            SatelliteTypeSelection::class.java,
                            Bundle().apply { putString("fromWhere", "map") })
                    }
                }

            }
            btnCompass.setOnClickListener {
                FirebaseEvents.logEvent("home_screen_click_compass", "home_screen_click_compass")
                showInterstitialAndNavigate(RemoteConfig.interCompass) {
                    startActivityWithSlideTransition(CompassActivity::class.java)
                }
            }


            btnBubbleLevel.setOnClickListener {
                FirebaseEvents.logEvent(
                    "home_screen_click_bubble_level",
                    "home_screen_click_bubble_level"
                )
                startActivityWithSlideTransition(LevelActivity::class.java)
            }

            btnCurrent.setOnClickListener {
                FirebaseEvents.logEvent(
                    "home_screen_click_cur_location",
                    "home_screen_click_cur_location"
                )
                showInterstitialAndNavigate(RemoteConfig.interCurrentLocation) {
                    startActivityWithSlideTransition(CurrentLocationActivity::class.java)
                }
            }

            btnARView.setOnClickListener {
                FirebaseEvents.logEvent(
                    "home_screen_click_ar_view",
                    "home_screen_click_ar_view"
                )
                if (checkCameraPermission()) {
                    if (isInternetConnected()) {
                        startActivityWithSlideTransition(
                            SatelliteTypeSelection::class.java,
                            Bundle().apply { putString("fromWhere", "arView") })
                    } else {
                        showInterstitialAndNavigate(RemoteConfig.interCurrentLocation) {
                            startActivityWithSlideTransition(
                                ARViewActivity::class.java,
                                Bundle().apply {
                                    putBoolean("satelliteFromInternet", false)
                                })
                        }
                    }


                } else {
                    checkCameraPermission(onPermissionsGranted = {
                        if (isInternetConnected()) {
                            startActivityWithSlideTransition(
                                SatelliteTypeSelection::class.java,
                                Bundle().apply { putString("fromWhere", "arView") })
                        } else {
                            showInterstitialAndNavigate(RemoteConfig.interCurrentLocation) {
                                startActivityWithSlideTransition(
                                    ARViewActivity::class.java,
                                    Bundle().apply {
                                        putBoolean("fromEmptyList", true)
                                    })
                            }
                        }
                    }, onPermissionsDenied = {
                        showRationaleDialog()
                    }, onError = {
                        showToast(it)
                    })
                }

            }
            btnProtractor.setOnClickListener {
                FirebaseEvents.logEvent(
                    "home_screen_click_protractor",
                    "home_screen_click_protractor"
                )
                showInterstitialAndNavigate(RemoteConfig.interProtractor) {
                    startActivityWithSlideTransition(ProtractorActivity::class.java)
                }
            }
            btnInclinometer.setOnClickListener {
                FirebaseEvents.logEvent(
                    "home_screen_click_inclinometer",
                    "home_screen_click_inclinometer"
                )
                showInterstitialAndNavigate(RemoteConfig.interInclinometer) {
                    startActivityWithSlideTransition(InclinometerActivity::class.java)
                }
            }

            btnPendulumBob.setOnClickListener {
                FirebaseEvents.logEvent(
                    "home_screen_click_pendulum",
                    "home_screen_click_pendulum"
                )
                showInterstitialAndNavigate(RemoteConfig.interPendulum) {
                    startActivityWithSlideTransition(PendulumActivity::class.java)
                }
            }

            btnAngleMeter.setOnClickListener {
                FirebaseEvents.logEvent(
                    "home_screen_click_angle_meter",
                    "home_screen_click_angle_meter"
                )
                showInterstitialAndNavigate(RemoteConfig.interAngleMeter) {
                    startActivityWithSlideTransition(AngleMeterActivity::class.java)
                }
            }

            btnRulerLevel.setOnClickListener {
                FirebaseEvents.logEvent(
                    "home_screen_click_ruler",
                    "home_screen_click_ruler"
                )
                showInterstitialAndNavigate(RemoteConfig.interRulerLevel) {
                    startActivityWithSlideTransition(RulerLevelActivity::class.java)
                }
            }
            btnISSTracker.setOnClickListener {
                FirebaseEvents.logEvent(
                    "home_screen_click_iss_tracker",
                    "home_screen_click_iss_tracker"
                )
                if (isInternetConnected()) {
                    viewModels.loadSatellites("iss")
                } else {
                    showToast("No Internet Connection")
                }


            }

            btnSetting.setOnClickListener {
                FirebaseEvents.logEvent("home_screen_click_drawer", "home_screen_click_drawer")
                if (layoutDrawer.isDrawerOpen(GravityCompat.START)) {
                    layoutDrawer.closeDrawer(GravityCompat.START)
                } else {
                    layoutDrawer.openDrawer(GravityCompat.START)
                    //this.hideKeyboard(view = View(this))
                }
            }


            if (isAlreadyPurchased()) {
                btnPremium.visibility = View.GONE
                goVersion.visibility = View.GONE
            } else {
                btnPremium.setOnClickListener {
                    FirebaseEvents.logEvent(
                        "home_screen_click_premium",
                        "home_screen_click_premium"
                    )
                    startActivityWithSlideTransition(SubscriptionActivity::class.java)
                }

                goVersion.setOnClickListener {
                    FirebaseEvents.logEvent(
                        "home_screen_click_drawer_go_premium",
                        "home_screen_click_drawer_go_premium"
                    )
                    startActivityWithSlideTransition(SubscriptionActivity::class.java)
                }
            }


        }

        setUpDrawerLayout()
    }

    fun Context.checkCameraPermission(
        onPermissionsDenied: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null,
        onPermissionsGranted: (() -> Unit)? = null,
    ) {
        Dexter.withContext(this).withPermissions(Manifest.permission.CAMERA)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report?.areAllPermissionsGranted() == true) {
                        onPermissionsGranted?.invoke()
                    }

                    if (report?.isAnyPermissionPermanentlyDenied == true) {
                        onPermissionsDenied?.invoke()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).withErrorListener { error ->
                onError?.invoke("Error occurred! $error")
            }.onSameThread().check()
    }

    fun Context.checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Function to open app settings
    private fun openAppSettings() {
        try {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        } catch (e: Exception) {
            e.printStackTrace()

        }
    }


    private fun showRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage("You have denied the permission permanently. Please go to the app settings to allow the permission.")
            .setPositiveButton("Go to Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun showInterstitialAndNavigate(remoteConfig: Boolean, action: () -> Unit) {

        if (canWeShowAds(remoteConfig)) {

            if (!SplashActivity.isInterstitialShowedOnSplash) {
                if (canWeShowAds(RemoteConfig.interSplash)) {
                    showPriorityAdmobInterstitial(
                        canReload = false, showListener = {
                            SplashActivity.isInterstitialShowedOnSplash = true
                        }, closeListener = {
                            action.invoke()

                        }, failListener = {
                            SplashActivity.isInterstitialShowedOnSplash = false
                            action.invoke()
                        })
                } else {
                    showPriorityInterstitialAdWithCounter(closeListener = {
                        action.invoke()
                    }, failListener = {
                        action.invoke()
                    })
                }
            } else {
                showPriorityInterstitialAdWithCounter(closeListener = {
                    action.invoke()
                }, failListener = {
                    action.invoke()
                })
            }

        } else {
            action.invoke()
        }
    }

    private fun setUpDrawerLayout() {
        drawerItems = arrayListOf(
            NavigationItemModel(R.drawable.ic_languages, this.getString(R.string.languages)),
            NavigationItemModel(R.drawable.ic_rate_us, this.getString(R.string.rate_us)),
            NavigationItemModel(R.drawable.ic_share_, this.getString(R.string.share_app)),
            NavigationItemModel(R.drawable.ic_privacy, this.getString(R.string.privacy_policy))
        )
        binding.navigationRv.layoutManager = LinearLayoutManager(this)
        binding.navigationRv.setHasFixedSize(true)

        binding.navigationRv.addOnItemTouchListener(RecyclerTouchListener(this, object :
            ClickListener {
            override fun onClick(view: View, position: Int) {
                when (position) {
                    0 -> {
                        FirebaseEvents.logEvent(
                            "home_screen_click_language",
                            "home_screen_click_language"
                        )
                        if (canWeShowAds(RemoteConfig.interLanguage)) {
                            showPriorityInterstitialAdWithCounter(closeListener = {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        LanguagesActivity::class.java
                                    )
                                )

                            }, failListener = {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        LanguagesActivity::class.java
                                    )
                                )

                            })
                        } else {
                            startActivity(Intent(this@MainActivity, LanguagesActivity::class.java))
                        }
                    }

                    1 -> {
                        FirebaseEvents.logEvent(
                            "home_screen_click_rate_us",
                            "home_screen_click_rate_us"
                        )
                        rateUs()
                    }

                    2 -> {
                        FirebaseEvents.logEvent(
                            "home_screen_click_share_app",
                            "home_screen_click_share_app"
                        )
                        sharesApp("Install this free Satellite Finder App")
                    }

                    3 -> {
                        FirebaseEvents.logEvent(
                            "home_screen_click_privacy_policy",
                            "home_screen_click_privacy_policy"
                        )
                        privacyPolicy()
                    }
                }
                updateDrawerAdapter(position)
            }
        }))

        updateDrawerAdapter(0)

        binding.tvVersion.text = getVersionName()
    }

    private fun updateDrawerAdapter(highlightItemPos: Int) {
        val drawerAdapter = NavigationAdapter(drawerItems, highlightItemPos)
        binding.navigationRv.adapter = drawerAdapter
        drawerAdapter.notifyDataSetChanged()
    }

    private fun getVersionName(): String {
        var versionName = ""
        try {
            versionName = applicationContext.packageManager.getPackageInfo(
                applicationContext.packageName,
                0
            ).versionName.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "App Version $versionName"
    }

    private fun showNativeAd() {
        if (canWeShowAds(RemoteConfig.mainNative)) {
            binding.layoutNative.visibility = View.VISIBLE
            val bindAdNative = NativeAdLayoutMainBinding.inflate(layoutInflater)

            NativeAdUtils(this@MainActivity.application, "main").loadNativeAd(
                adsKey = getString(R.string.mainNativeId),
                remoteConfig = RemoteConfig.mainNative,
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

    }

    private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            startActivityWithSlideTransition(ExitActivity::class.java)
        }
    }
}