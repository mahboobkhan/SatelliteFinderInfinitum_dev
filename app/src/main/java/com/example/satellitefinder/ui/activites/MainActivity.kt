package com.example.satellitefinder.ui.activites

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.loadAndReturnAd
import com.example.satellitefinder.admobAds.newLoadAndShowNativeAd
import com.example.satellitefinder.databinding.ActivityMainBinding
import com.example.satellitefinder.ui.adapters.NavigationAdapter
import com.example.satellitefinder.ui.adapters.NavigationItemModel
import com.example.satellitefinder.ui.dialogs.RattingDialog
import com.example.satellitefinder.utils.ClickListener
import com.example.satellitefinder.utils.LanguagesHelper
import com.example.satellitefinder.utils.RecyclerTouchListener
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.exitNativeAd
import com.example.satellitefinder.utils.isSplash
import com.example.satellitefinder.utils.privacyPolicy
import com.example.satellitefinder.utils.rateUs
import com.example.satellitefinder.utils.screenEventAnalytics
import com.example.satellitefinder.utils.sharesApp
import com.example.satellitefinder.utils.startActivityWithSlideTransition

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var rateUsDialog: RattingDialog
    private lateinit var drawerItems: ArrayList<NavigationItemModel>

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

        screenEventAnalytics("MainActivity")

        showNativeAd()
        rateUsDialog = RattingDialog(
            this@MainActivity,
        )

        if (canWeShowAds(RemoteConfig.nativeExit)){
            loadAndReturnAd(this@MainActivity, getString(R.string.exitNativeId)) {
                exitNativeAd = it
            }
        }

        onBackPressedDispatcher.addCallback(this@MainActivity, callback)

        binding.apply {
            btnFindSatellite.setOnClickListener {
                startActivityWithSlideTransition(SatelliteFindActivity::class.java)
            }

            btnSatelliteMap.setOnClickListener {
                startActivityWithSlideTransition(MapSatelliteActivity::class.java)
            }
            btnCompass.setOnClickListener {
                startActivityWithSlideTransition(CompassActivity::class.java)
            }

            btnBubbleLevel.setOnClickListener {
                startActivityWithSlideTransition(LevelActivity::class.java)
            }

            btnCurrent.setOnClickListener {
                startActivityWithSlideTransition(CurrentLocationActivity::class.java)
            }

            btnSetting.setOnClickListener {
                if (layoutDrawer.isDrawerOpen(GravityCompat.START)) {
                    layoutDrawer.closeDrawer(GravityCompat.START)
                } else {
                    layoutDrawer.openDrawer(GravityCompat.START)
                    //this.hideKeyboard(view = View(this))
                }
            }

            goVersion.setOnClickListener {
                startActivityWithSlideTransition(SubscriptionActivity::class.java)
            }
        }

        setUpDrawerLayout()
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
                when(position) {
                    0 -> {
                        startActivity(Intent(this@MainActivity, LanguagesActivity::class.java))
                    }
                    1 -> {
                        rateUs()
                    }
                    2 -> {
                        sharesApp("Install this free Satellite Finder App")
                    }
                    3 -> {
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
        if (canWeShowAds(RemoteConfig.mainNative)){

            newLoadAndShowNativeAd(
                binding.layoutNative,
                R.layout.native_ad_layout_main,
                getString(R.string.mainNativeId),
                adLoading = {
                    binding.layoutNative.visibility = View.VISIBLE
                },
                failToLoad = {binding.layoutNative.visibility = View.GONE})
        }else{
            binding.layoutNative.visibility =  View.GONE
        }

    }

    private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            startActivityWithSlideTransition(ExitActivity::class.java)
        }
    }
}