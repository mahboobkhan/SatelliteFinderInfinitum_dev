package com.example.satellitefinder.ui.activites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adssdk.native_ad.NativeAdType
import com.example.adssdk.native_ad.NativeAdUtils
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.showNativeAd
import com.example.satellitefinder.databinding.ActivitySatelliteTypeSelectionBinding
import com.example.satellitefinder.databinding.NativeAdLayoutMainBinding
import com.example.satellitefinder.repo.SatelliteDataManager
import com.example.satellitefinder.ui.adapters.SatelliteAdapter
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.startActivityWithSlideTransition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext

class SatelliteTypeSelection : AppCompatActivity() {
    private val binding by lazy {
        ActivitySatelliteTypeSelectionBinding.inflate(layoutInflater)
    }

    private val satelliteList by lazy {
        arrayOf(
            getString(R.string.weather_satellites),
            getString(R.string.general_satellites),
            getString(R.string.starlink_satellites)
        )
    }

    val fromWhere by lazy {
        intent.getStringExtra("fromWhere")
    }
    
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        showNativeAd()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
        
        checkDataAvailability()
        setUpRecyclerView()
    }
    
    private fun checkDataAvailability() {
        scope.launch {
            try {
                val dataManager = GlobalContext.get().get<SatelliteDataManager>()
                
                // Check if any data is still loading
                val isLoading = listOf("weather", "general", "starlink").any { 
                    dataManager.isLoading(it) 
                }
                
                if (isLoading) {
                    binding.loadingContainer.visibility = View.VISIBLE
                    
                    // Set up callback to hide loading when data is ready
                    dataManager.onDataLoaded = { satelliteType ->
                        // Check if all data is loaded
                        val allLoaded = listOf("weather", "general", "starlink").all { 
                            dataManager.isDataLoaded(it) 
                        }
                        if (allLoaded) {
                            binding.loadingContainer.visibility = View.GONE
                        }
                    }
                } else {
                    binding.loadingContainer.visibility = View.GONE
                }
                
            } catch (e: Exception) {
                binding.loadingContainer.visibility = View.GONE
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvSatelliteList.adapter = SatelliteAdapter(satelliteList) {
            when (it) {
                getString(R.string.weather_satellites) -> {
                    if (fromWhere == "arView"){
                        startActivityWithSlideTransition(
                            ARViewActivity::class.java,
                            Bundle().apply {
                                putBoolean("satelliteFromInternet", true)
                                putString("satellite_type", "weather")
                            })
                    }else{
                        Intent(this, SatelliteTypeLIst::class.java).apply {
                            putExtra("satellite_type", "weather")
                            putExtra("fromWhere", fromWhere)
                            startActivity(this)
                        }
                    }

                }

                getString(R.string.general_satellites) -> {

                    if (fromWhere == "arView"){
                        startActivityWithSlideTransition(
                            ARViewActivity::class.java,
                            Bundle().apply {
                                putBoolean("satelliteFromInternet", true)
                                putString("satellite_type", "general")
                            })
                    }else{
                        Intent(this, SatelliteTypeLIst::class.java).apply {
                            putExtra("satellite_type", "general")
                            putExtra("fromWhere", fromWhere)
                            startActivity(this)
                        }
                    }
                }

                getString(R.string.starlink_satellites) -> {
                    if (fromWhere == "arView"){
                        startActivityWithSlideTransition(
                            ARViewActivity::class.java,
                            Bundle().apply {
                                putBoolean("satelliteFromInternet", true)
                                putString("satellite_type", "starlink")
                            })
                    }else{
                        Intent(this, SatelliteTypeLIst::class.java).apply {
                            putExtra("satellite_type", "starlink")
                            putExtra("fromWhere", fromWhere)
                            startActivity(this)
                        }
                    }
                }
            }
        }
        binding.rvSatelliteList.layoutManager = LinearLayoutManager(this)
    }

    private fun showNativeAd() {
        if (canWeShowAds(RemoteConfig.nativeSatelliteTypes)) {

            binding.layoutNative.visibility = View.VISIBLE
            val bindAdNative = NativeAdLayoutMainBinding.inflate(layoutInflater)

            NativeAdUtils(this@SatelliteTypeSelection.application, "satelliteType").loadNativeAd(
                adsKey = getString(R.string.findSatelliteNativeId),
                remoteConfig = RemoteConfig.nativeSatelliteTypes,
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
}