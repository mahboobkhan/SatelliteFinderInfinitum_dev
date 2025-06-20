package com.example.satellitefinder.ui.activites

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adssdk.native_ad.NativeAdType
import com.example.adssdk.native_ad.NativeAdUtils
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.databinding.ActivityNewSatelliteMapBinding
import com.example.satellitefinder.databinding.NativeAdLayoutSmallBinding
import com.example.satellitefinder.ui.activites.MapSatelliteActivity.Companion.MIN_DISTANCE_FOR_UPDATES
import com.example.satellitefinder.ui.activites.MapSatelliteActivity.Companion.MIN_TIME_UPDATES
import com.example.satellitefinder.utils.FirebaseEvents
import com.example.satellitefinder.utils.SatellitesPositionData
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.requestLocationPermissions
import com.example.satellitefinder.utils.showGpsDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CustomCap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class NewSatelliteMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private val binding by lazy {
        ActivityNewSatelliteMapBinding.inflate(layoutInflater)
    }
    private val satelliteLatitude by lazy {
        intent.getDoubleExtra("satelliteLat", 0.0)
    }
    private val satelliteLongitude by lazy {
        intent.getDoubleExtra("satelliteLng", 0.0)
    }
    private var isGPSEnabled = false
    private var satellitePosition: SatellitesPositionData? = null

    private var isNetworkEnabled = false
    private var isAbleToGetLocation = false
    private var mLocation: Location? = null
    private var mLatitude = 0.0
    private var mLongitude = 0.0
    private var sourceLatitude = ""
    private var sourceLongitude = ""
    var myCurrentLocation: LatLng? = null
    private val TAG = "MapActivity"
    private var googleMap1: GoogleMap? = null
    var supportMapFragment: SupportMapFragment? = null
    private val M_REQUEST_PERMISSION = 10


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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
        binding.ivBack.setOnClickListener {
            FirebaseEvents.logEvent(
                "satellite_map_screen_click_back",
                "satellite_map_screen_click_back"
            )
            finish()
        }

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
                drawMarker()

                /*googleMap1?.addMarker(MarkerOptions().position(it).title("It's Me!"))
                googleMap1?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(it, 13.75f),
                    5000,
                    null
                )*/
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap1 = googleMap
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

    private fun drawMarker() {
        val markerOptions = MarkerOptions().position(
            LatLng(
                myCurrentLocation!!.latitude,
                myCurrentLocation!!.longitude
            )
        ).title("Your Position")
        if (googleMap1 != null) {
            googleMap1?.clear()
            googleMap1?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        myCurrentLocation!!.latitude,
                        myCurrentLocation!!.longitude
                    ), 17f
                )
            )
            //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            googleMap1?.addMarker(markerOptions)
            googleMap1?.addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(
                            myCurrentLocation!!.latitude,
                            myCurrentLocation!!.longitude
                        ),
                        LatLng(satelliteLatitude, satelliteLongitude)
                    ).width(10f).color(
                        Color.BLACK
                    )
            )?.endCap =
                CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.ic_sat_on_map))

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

    private fun showNativeAd() {
        if (canWeShowAds(RemoteConfig.mapSatelliteNative)) {


            binding.layoutNative.visibility = View.VISIBLE
            val bindAdNative = NativeAdLayoutSmallBinding.inflate(layoutInflater)

            NativeAdUtils(this@NewSatelliteMapActivity.application, "map_satellite").loadNativeAd(
                adsKey = getString(R.string.mapScreenNativeId),
                remoteConfig = RemoteConfig.mapSatelliteNative,
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

    private fun checkGpsStatus() {
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGpsDialog()
        } else {
            getMyLocation()
        }
    }


}