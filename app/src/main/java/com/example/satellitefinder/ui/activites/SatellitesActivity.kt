package com.example.satellitefinder.ui.activites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.showPriorityAdmobInterstitial
import com.example.satellitefinder.admobAds.showPriorityInterstitialAdWithCounter
import com.example.satellitefinder.databinding.ActivitySatellitesBinding
import com.example.satellitefinder.ui.adapters.SatellitesAdapter
import com.example.satellitefinder.utils.LanguagesHelper
import com.example.satellitefinder.utils.SatellitesInformationData
import com.example.satellitefinder.utils.SatellitesPositionData
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.isInternetConnected
import com.example.satellitefinder.utils.satelliteAdCounter
import com.example.satellitefinder.utils.screenEventAnalytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Collections

class SatellitesActivity : AppCompatActivity() {
    lateinit var adapter: SatellitesAdapter
    var isNativeAdEnable = true
    var isAutoAdsRemoved = false
    var mSatelliteList = ArrayList<SatellitesPositionData>()
    private val binding: ActivitySatellitesBinding by lazy {
        ActivitySatellitesBinding.inflate(layoutInflater)
    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        newBase?.let {
            LanguagesHelper.onAttach(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (canWeShowAds(RemoteConfig.interAll)) {

            showPriorityInterstitialAdWithCounter(true, getString(R.string.interstialId))
        }
        setContentView(binding.root)

        screenEventAnalytics("SatellitesActivity")

        binding.ivBack.setOnClickListener { onBackPressed() }
        binding.ivSearch.setOnClickListener {
            val intent = Intent(this, SearchSatelliteActivity::class.java)
            resultLauncher.launch(intent)
        }

        adapter =
            SatellitesAdapter(this@SatellitesActivity) {
                if (canWeShowAds(RemoteConfig.interAll) && satelliteAdCounter == 0) {
                    showPriorityAdmobInterstitial(true, getString(R.string.interstialId), closeListener = {
                        sendDataResult(it)
                    }, failListener = {
                        sendDataResult(it)
                    }, showListener = {
                        satelliteAdCounter = 1
                    })
                } else {
                    sendDataResult(it)
                }
            }

        if (canWeShowAds(RemoteConfig.selectSatelliteNative)) {
            isNativeAdEnable = true
            isAutoAdsRemoved = false
        } else {
            isNativeAdEnable = false
            isAutoAdsRemoved = true
        }

        CoroutineScope(Dispatchers.IO).launch {
            mSatelliteList.clear()
            mSatelliteList = loadSatellitesData()
        }.invokeOnCompletion {
            CoroutineScope(Dispatchers.Main).launch {
                adapter.setData(
                    isNativeAdEnable,
                    isAutoAdsRemoved,
                    mSatelliteList,
                    isInternetConnected(),
                    5,
                    this@SatellitesActivity
                )
                delay(3000)
                binding.loadingLayout.visibility = View.GONE
                binding.satellitesList.visibility = View.VISIBLE
                binding.satellitesList.adapter = adapter

            }
        }
    }

    private fun sendDataResult(satellitePositionData: SatellitesPositionData?) {
        val intent = intent
        intent.putExtra("satObject", satellitePositionData)
        setResult(RESULT_OK, intent)
        finish()
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                try {
                    val mSatelliteData =
                        data?.getSerializableExtra("satObject") as SatellitesPositionData?
                    val intent = intent
                    intent.putExtra("satObject", mSatelliteData)
                    setResult(RESULT_OK, intent)
                    finish()
                } catch (e: Exception) {
                    Log.e("TAG", ": $e")
                }
            }
        }

    private fun loadSatellitesData(): ArrayList<SatellitesPositionData> {
        val itemList: ArrayList<SatellitesPositionData> = ArrayList()
        val positionsList: ArrayList<SatellitesPositionData> = ArrayList()

        val satellitesList = SatellitesInformationData.getSatteliteArray(this)
        satellitesList.forEachIndexed { _, sat ->
            sat.let {
                val satPosition = SatellitesPositionData(it)
                satPosition.let { data ->
                    data.getSatelliteElevation()?.let { itt ->
                        if (itt.toInt() > 1) {
                            positionsList.add(satPosition)
                        }
                    }

                }
            }
        }

        Collections.sort(positionsList, compare)
        itemList.addAll(positionsList)
        return itemList
    }

    private val compare = Comparator { e1: SatellitesPositionData, e2: SatellitesPositionData ->
        e1.getSatellite().toString().compareTo(e2.getSatellite().toString())
    }


    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
    }

}