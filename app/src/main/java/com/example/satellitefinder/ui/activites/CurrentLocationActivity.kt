package com.example.satellitefinder.ui.activites

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.adssdk.native_ad.NativeAdType
import com.example.adssdk.native_ad.NativeAdUtils
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.showPriorityInterstitialAdWithCounter
import com.example.satellitefinder.databinding.ActivityCurrentLocationBinding
import com.example.satellitefinder.databinding.NativeAdLayoutSmallBinding
import com.example.satellitefinder.utils.FirebaseEvents
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.requestLocationPermissions
import com.example.satellitefinder.utils.screenEventAnalytics
import com.example.satellitefinder.utils.showGpsDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CurrentLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private var isGPSEnabled = false
    private var isNetworkEnabled = false
    private var isAbleToGetLocation = false
    private var mLocation: Location? = null
    private var mLatitude = 0.0
    private var mLongitude = 0.0
    private var sourceLatitude = ""
    private var sourceLongitude = ""
    var myCurrentLocation: LatLng? = null
    private val TAG = "CurrentLocationActivity"
    private var googleMap1: GoogleMap? = null
    var supportMapFragment: SupportMapFragment? = null
    private val M_REQUEST_PERMISSION = 10

    val binding: ActivityCurrentLocationBinding by lazy {
        ActivityCurrentLocationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseEvents.logEventActivity("cur_location_screen", "cur_location_screen")
        setContentView(binding.root)
        screenEventAnalytics(TAG)

        showNativeAd()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            checkGpsStatus()
            mapIntialization()
        } else {
            requestLocationPermissions()
        }

        binding.btnBack.setOnClickListener {
            FirebaseEvents.logEvent(
                "cur_location_screen_click_back",
                "cur_location_screen_click_back"
            )
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this@CurrentLocationActivity, callback)
    }


    private fun mapIntialization() {
        if (googleMap1 == null) {
            val fm = supportFragmentManager
            supportMapFragment = fm.findFragmentById(R.id.provider_map) as SupportMapFragment?
            supportMapFragment?.getMapAsync(this)
        }
        if (googleMap1 != null) {
            setupMap()
        }
    }

    private fun setupMap() {
        googleMap1?.let {
            it.uiSettings.isCompassEnabled = false
            it.isBuildingsEnabled = true
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }

            it.setOnCameraMoveStartedListener { }
            myCurrentLocation?.let {
                googleMap1?.addMarker(MarkerOptions().position(it).title("It's Me!"))
                googleMap1?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(it, 13.75f),
                    5000,
                    null
                )
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap1 = googleMap
        setupMap()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            M_REQUEST_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapIntialization()
                    getMyLocation()
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // requestPermission();
                    }
                }
            }

            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun currentLocation() {
        com.example.satellitefinder.utils.currentLocation?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            googleMap1?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            googleMap1?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        }
    }


    private fun getMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            // Getting GPS status
            isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)

            // Getting network status
            isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPSEnabled && !isNetworkEnabled) {
                // No network provider is enabled
            } else {
                isAbleToGetLocation = true
                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_UPDATES,
                        MIN_DISTANCE_FOR_UPDATES.toFloat(), mLocationListener
                    )
                    Log.d("Network", "Network")
                    locationManager.let {
                        mLocation = it.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        mLocation?.let {
                            mLatitude = it.latitude
                            mLongitude = it.longitude
                            sourceLatitude = "" + it.latitude
                            sourceLongitude = "" + it.longitude
                            com.example.satellitefinder.utils.currentLocation = mLocation
                            myCurrentLocation = LatLng(it.latitude, it.longitude)
                        }
                    }
                }
            }
            // If GPS enabled, get latitude/longitude using GPS Services
            if (isGPSEnabled) {
                if (mLocation == null) {
                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            M_REQUEST_PERMISSION
                        )
                    } else {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_UPDATES,
                            MIN_DISTANCE_FOR_UPDATES.toFloat(), mLocationListener
                        )
                        Log.d("GPS Enabled", "GPS Enabled")
                        locationManager.let {
                            mLocation = it.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            mLocation?.let {
                                mLatitude = it.latitude
                                mLongitude = it.longitude
                                sourceLatitude = "" + it.latitude
                                sourceLongitude = "" + it.longitude
                                com.example.satellitefinder.utils.currentLocation = mLocation
                                myCurrentLocation = LatLng(it.latitude, it.longitude)
                            }
                        }
                    }
                }
            }
            Log.e("mLocationListener ", "  CAL   ")
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestLocationPermissions()
            }
        }
    }

    private val mLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            try {
                sourceLatitude = "" + location.latitude
                sourceLongitude = "" + location.longitude
                com.example.satellitefinder.utils.currentLocation = location
                myCurrentLocation = LatLng(location.latitude, location.longitude)
                Log.e("mLocationListener ", "  source_lat   $sourceLatitude")
                Log.e("mLocationListener ", "  source_lng   $sourceLongitude")
            } catch (e: Exception) {
                Log.e(TAG, "onLocationChanged: $e")
            }
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
        override fun onProviderEnabled(s: String) {}
        override fun onProviderDisabled(s: String) {}
    }

    private fun checkGpsStatus() {
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGpsDialog()
        } else {
            getMyLocation()
        }
    }

    companion object {
        private const val MIN_DISTANCE_FOR_UPDATES: Long = 1000 // 10 meters

        private const val MIN_TIME_UPDATES = (1000 * 60 * 1 // 1 minute
                ).toLong()
    }


    private fun showNativeAd() {
        if (canWeShowAds(RemoteConfig.currentLocationNative)) {
            /* newLoadAndShowNativeAd(
                 binding.layoutNative,
                 R.layout.native_ad_layout_small,
                 getString(R.string.currentLocationNativeId),
                 adLoading = {
                     binding.layoutNative.visibility = View.VISIBLE
                 },
                 failToLoad = {
                     binding.layoutNative.visibility = View.GONE
                 })*/

            binding.layoutNative.visibility = View.VISIBLE
            val bindAdNative = NativeAdLayoutSmallBinding.inflate(layoutInflater)

            NativeAdUtils(
                this@CurrentLocationActivity.application,
                "current_location"
            ).loadNativeAd(
                adsKey = getString(R.string.currentLocationNativeId),
                remoteConfig = RemoteConfig.currentLocationNative,
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

    private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()

            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}