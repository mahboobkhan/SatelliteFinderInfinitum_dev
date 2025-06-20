package com.example.satellitefinder.ui.activites

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adssdk.native_ad.NativeAdType
import com.example.adssdk.native_ad.NativeAdUtils
import com.example.adssdk.utils.toast
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.databinding.ActivitySatelliteTypeListBinding
import com.example.satellitefinder.databinding.NativeAdLayoutSmallBinding
import com.example.satellitefinder.models.SatelliteModel
import com.example.satellitefinder.repo.SatelliteViewModel
import com.example.satellitefinder.ui.adapters.SatelliteListAdapter
import com.example.satellitefinder.utils.canWeShowAds
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.ext.android.viewModel


class SatelliteTypeLIst : AppCompatActivity() {

    private val binding by lazy { ActivitySatelliteTypeListBinding.inflate(layoutInflater) }
    private val viewModels: SatelliteViewModel by viewModel()
    private var fullSatelliteList: List<SatelliteModel> = emptyList()

    val satelliteType by lazy {
        intent.getStringExtra("satellite_type") ?: "general"
    }

    val fromWhere by lazy {
        intent.getStringExtra("fromWhere")
    }
    val adapter by lazy {
        SatelliteListAdapter(onClick = {
            when (fromWhere) {
                "map" -> {
                    Intent(this, NewSatelliteMapActivity::class.java).apply {
                        putExtra("satelliteLat", it.latitude)
                        putExtra("satelliteLng", it.longitude)
                        startActivity(this)
                    }
                }

                "meter" -> {
                    Intent(this, NewSatelliteMeterActivity::class.java).apply {
                        putExtra("satelliteName", it.name)
                        putExtra("satelliteLat", it.latitude)
                        putExtra("satelliteLng", it.longitude)
                        putExtra("satelliteAzimuth", it.azimuth)
                        putExtra("satelliteElevation", it.elevation)
                        startActivity(this)
                    }
                }
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        showNativeAd()
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.rvSatelliteList.layoutManager = LinearLayoutManager(this)
        binding.rvSatelliteList.adapter = adapter

        setUpSearch()


        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    adapter.currentLocation = location
                    adapter.notifyDataSetChanged()
                    viewModels.satelliteList.observe(this) {
                        if (it.isNotEmpty()){
                            binding.progressBar.visibility = View.GONE
                            fullSatelliteList = it
                            adapter.submitList(it)
                        }else{
                            binding.progressBar.visibility = View.GONE
                            binding.noSatelliteFound.visibility = View.VISIBLE
                            toast("No satellites found")
                        }

                    }
                }
            }
        }



        viewModels.loadSatellites(satelliteType)
    }

    private fun setUpSearch() {
        binding.ivSearch.setOnClickListener {
            binding.ivSearch.visibility = View.GONE
            binding.edSearch.visibility = View.VISIBLE
            binding.ivClose.visibility = View.VISIBLE
            openKeyboard(binding.edSearch)

        }

        binding.ivClose.setOnClickListener {
            binding.edSearch.text?.clear()
            binding.ivSearch.visibility = View.VISIBLE
            binding.edSearch.visibility = View.GONE
            binding.ivClose.visibility = View.GONE
            adapter.submitList(fullSatelliteList)
        }

        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredList = fullSatelliteList.filter { satellite ->
                    satellite.name.contains(s.toString(), ignoreCase = true)
                }
                adapter.submitList(filteredList)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun showNativeAd() {
        if (canWeShowAds(RemoteConfig.satelliteFindNative)) {


            binding.layoutNative.visibility = View.VISIBLE
            val bindAdNative = NativeAdLayoutSmallBinding.inflate(layoutInflater)

            NativeAdUtils(this@SatelliteTypeLIst.application, "map_satellite").loadNativeAd(
                adsKey = getString(R.string.mapScreenNativeId),
                remoteConfig = RemoteConfig.satelliteFindNative,
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

    private fun openKeyboard(editText: EditText) {
        try {
            editText.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}