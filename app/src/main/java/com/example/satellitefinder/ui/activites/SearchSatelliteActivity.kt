package com.example.satellitefinder.ui.activites

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.adssdk.native_ad.NativeAdType
import com.example.adssdk.native_ad.NativeAdUtils
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.databinding.ActivitySearchSatelliteBinding
import com.example.satellitefinder.databinding.NativeAdLayoutSmallBinding
import com.example.satellitefinder.ui.adapters.SearchSatelliteAdapter
import com.example.satellitefinder.utils.FirebaseEvents
import com.example.satellitefinder.utils.LanguagesHelper
import com.example.satellitefinder.utils.SatellitesInformationData
import com.example.satellitefinder.utils.SatellitesPositionData
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.screenEventAnalytics
import java.util.Collections

class SearchSatelliteActivity : AppCompatActivity() {
    lateinit var adapter: SearchSatelliteAdapter
    lateinit var mSearchView: SearchView

    private val binding: ActivitySearchSatelliteBinding by lazy {
        ActivitySearchSatelliteBinding.inflate(layoutInflater)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        newBase?.let {
            LanguagesHelper.onAttach(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        FirebaseEvents.logEventActivity("search_sate_screen", "search_sate_screen")
        supportActionBar?.show()
        supportActionBar?.title = "Tap to search Satellite"
        if (canWeShowAds(RemoteConfig.searchSatelliteNative)) {
//            newLoadAndShowNativeAd(binding.layoutNative,R.layout.native_ad_layout_small,getString(R.string.searchSatelliteNativeId))

            binding.layoutNative.visibility = View.VISIBLE
            val bindAdNative = NativeAdLayoutSmallBinding.inflate(layoutInflater)

            NativeAdUtils(
                this@SearchSatelliteActivity.application,
                "search_satellite"
            ).loadNativeAd(
                adsKey = getString(R.string.searchSatelliteNativeId),
                remoteConfig = RemoteConfig.searchSatelliteNative,
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

        screenEventAnalytics("SearchSatelliteActivity")

        adapter = SearchSatelliteAdapter(object : SearchSatelliteAdapter.ActionListener {
            override fun sendData(satellitePositionData: SatellitesPositionData?) {
                val intent = intent
                intent.putExtra("satObject", satellitePositionData)
                setResult(RESULT_OK, intent)
                finish()
            }
        })
        adapter.setData(loadSatelliteData())
        binding.searchList.adapter = adapter
    }

    private fun loadSatelliteData(): ArrayList<SatellitesPositionData> {
        val itemList: ArrayList<SatellitesPositionData> = ArrayList()
        val positionsList: ArrayList<SatellitesPositionData> = ArrayList()

        val satellitesList = SatellitesInformationData.getSatteliteArray(this)
        satellitesList.forEachIndexed { _, sat ->
            sat.let {
                val satPosition = SatellitesPositionData(it)
                satPosition.getSatelliteElevation()?.let { itt ->
                    if (itt.toInt() > 1) {
                        positionsList.add(satPosition)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu.findItem(R.id.search_item)
        mSearchView = menuItem.actionView as SearchView
        mSearchView.queryHint = "Search"
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
    }
}