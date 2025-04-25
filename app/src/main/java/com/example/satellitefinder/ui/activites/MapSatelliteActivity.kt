package com.example.satellitefinder.ui.activites

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.newLoadAndShowNativeAd
import com.example.satellitefinder.admobAds.showPriorityAdmobInterstitial
import com.example.satellitefinder.admobAds.showPriorityInterstitialAdWithCounter
import com.example.satellitefinder.databinding.ActivityMapSatelliteBinding
import com.example.satellitefinder.databinding.SatelliteInfoSheetBinding
import com.example.satellitefinder.ui.dialogs.InfoDialog
import com.example.satellitefinder.ui.dialogs.InfoSheet
import com.example.satellitefinder.ui.dialogs.MapTypesDialog
import com.example.satellitefinder.utils.FirebaseEvents
import com.example.satellitefinder.utils.LanguagesHelper
import com.example.satellitefinder.utils.SatellitesPositionData
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.currentLocation
import com.example.satellitefinder.utils.requestLocationPermissions
import com.example.satellitefinder.utils.satelliteAdCounter
import com.example.satellitefinder.utils.screenEventAnalytics
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
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

class MapSatelliteActivity : AppCompatActivity(), OnMapReadyCallback {
    private var isGPSEnabled = false
    private var satellitePosition: SatellitesPositionData? = null
    private var currentSatelliteLatitude: Double? = 0.0
    private var currentSatelliteLonigtude: Double? = 0.0
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
    lateinit var typesDialog: MapTypesDialog
    lateinit var infoDialog: InfoDialog

    private var reviewManager: ReviewManager? = null
    private var reviewInfo: ReviewInfo? = null

    private var adCountInfo = 0
    private val binding: ActivityMapSatelliteBinding by lazy {
        ActivityMapSatelliteBinding.inflate(layoutInflater)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        newBase?.let {
            LanguagesHelper.onAttach(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (canWeShowAds(RemoteConfig.interSatelliteMap)) {
            showPriorityInterstitialAdWithCounter(true, getString(R.string.interstialId))
        }
        setContentView(binding.root)
        FirebaseEvents.logEventActivity("satellite_map_screen", "satellite_map_screen")
        reviewManager = ReviewManagerFactory.create(this)

        val request: Task<ReviewInfo> = reviewManager!!.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We can get the ReviewInfo object
                reviewInfo = task.result
            } else {
                // There was some problem, continue regardless of the result.
            }
        }

        typesDialog = MapTypesDialog(this@MapSatelliteActivity)
        infoDialog = InfoDialog(this@MapSatelliteActivity)

        showNativeAd()

        screenEventAnalytics("MapSatelliteActivity")

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


        binding.btnSelectSatellite.setOnClickListener {
            FirebaseEvents.logEvent("satellite_map_screen_click_search", "satellite_map_screen_click_search")
            selectSatellite()
        }

        binding.btnCurrentLocation.setOnClickListener {
            FirebaseEvents.logEvent("satellite_map_screen_click_cur_location", "satellite_map_screen_click_cur_location")
            if (canWeShowAds(RemoteConfig.interSatelliteMap)) {
                //              adCount = 0
                // loadAndShowSplashInterstitial(getString(R.string.interstialId))
                showPriorityInterstitialAdWithCounter(true, getString(R.string.interstialId),
                    showListener = { currentLocation() },
                    closeListener = { currentLocation() },
                    failListener = { currentLocation() })

                currentLocation()

            } else {
//                adCount +=1
                currentLocation()

            }


        }

        binding.ivBack.setOnClickListener {
            FirebaseEvents.logEvent("satellite_map_screen_click_back", "satellite_map_screen_click_back")
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnInfo.setOnClickListener {
            FirebaseEvents.logEvent("satellite_map_screen_click_info", "satellite_map_screen_click_info")
            if (adCountInfo >= 2) {
                showPriorityAdmobInterstitial(true, getString(R.string.interstialId), closeListener = {
                    InfoSheet(this@MapSatelliteActivity).showSheet { sheetBinding ->
                        satelliteInfo(sheetBinding)
                    }
                }, failListener = {
                    InfoSheet(this@MapSatelliteActivity).showSheet { sheetBinding ->
                        satelliteInfo(sheetBinding)
                    }
                }, showListener = {
                    adCountInfo = 0
                })
            } else {
                adCountInfo++
                InfoSheet(this@MapSatelliteActivity).showSheet { sheetBinding ->
                    satelliteInfo(sheetBinding)
                }
            }
        }

        binding.btnMapTypes.setOnClickListener {
            FirebaseEvents.logEvent("satellite_map_screen_click_map_type", "satellite_map_screen_click_map_type")
            typesDialog.showDialog {
                when (it) {
                    "hybrid" -> {
                        if (canWeShowAds(RemoteConfig.interSatelliteMap)) {
                            // adCount = 0
                            showPriorityInterstitialAdWithCounter(true,
                                getString(R.string.interstialId),
                                showListener = { googleMap1?.mapType = GoogleMap.MAP_TYPE_HYBRID },
                                closeListener = { googleMap1?.mapType = GoogleMap.MAP_TYPE_HYBRID },
                                failListener = { googleMap1?.mapType = GoogleMap.MAP_TYPE_HYBRID }
                            )
                        } else {
                            googleMap1?.mapType = GoogleMap.MAP_TYPE_HYBRID
                        }

                    }

                    "normal" -> {
                        if (canWeShowAds(RemoteConfig.interSatelliteMap)) {
                            showPriorityInterstitialAdWithCounter(true,
                                getString(R.string.interstialId),
                                showListener = { googleMap1?.mapType = GoogleMap.MAP_TYPE_NORMAL },
                                closeListener = { googleMap1?.mapType = GoogleMap.MAP_TYPE_NORMAL },
                                failListener = { googleMap1?.mapType = GoogleMap.MAP_TYPE_NORMAL }
                            )
                        } else {
                            googleMap1?.mapType = GoogleMap.MAP_TYPE_NORMAL
                        }
                    }

                    "satellite" -> {
                        if (canWeShowAds(RemoteConfig.interSatelliteMap)) {
                            showPriorityInterstitialAdWithCounter(
                                true,
                                getString(R.string.interstialId),
                                showListener = {
                                    googleMap1?.mapType = GoogleMap.MAP_TYPE_SATELLITE
                                },
                                closeListener = {
                                    googleMap1?.mapType = GoogleMap.MAP_TYPE_SATELLITE
                                },
                                failListener = {
                                    googleMap1?.mapType = GoogleMap.MAP_TYPE_SATELLITE
                                }
                            )

                        } else {
                            googleMap1?.mapType = GoogleMap.MAP_TYPE_SATELLITE
                        }

                    }

                    "terrain" -> {
                        if (canWeShowAds(RemoteConfig.interSatelliteMap)) {
                            showPriorityInterstitialAdWithCounter(true,
                                getString(R.string.interstialId),
                                showListener = { googleMap1?.mapType = GoogleMap.MAP_TYPE_TERRAIN },
                                closeListener = {
                                    googleMap1?.mapType = GoogleMap.MAP_TYPE_TERRAIN
                                },
                                failListener = { googleMap1?.mapType = GoogleMap.MAP_TYPE_TERRAIN }
                            )


                        } else {
                            googleMap1?.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        }

                    }

                }
            }
        }

        onBackPressedDispatcher.addCallback(this@MapSatelliteActivity, callback)
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

    private fun currentLocation() {
        currentLocation?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            googleMap1?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            googleMap1?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        }
    }


    private fun selectSatellite() {
        if (mLocation != null) {
            val intent = Intent(this@MapSatelliteActivity, SatellitesActivity::class.java)
            resultLauncher.launch(intent)
        } else {
            Toast.makeText(
                this@MapSatelliteActivity,
                "Please check location is enabled or not!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun satelliteInfo(sheetBinding: SatelliteInfoSheetBinding) {
        if (currentLocation != null && satellitePosition != null) {
            sheetBinding.apply {
                satAzimut.text = satellitePosition?.getSatelliteAzimut().toString()
                satellitePositionTv.text = satellitePosition?.getSatLongitude().toString()
                satElevation.text = satellitePosition?.getSatelliteElevation()?.let { it1 -> Math.round(it1) }.toString()
                satelliteLNBskewTv.text = satellitePosition?.getLNBSkew()?.let { Math.round(it) }.toString()
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
                            currentLocation = mLocation
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
                                currentLocation = mLocation
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
                currentLocation = location
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

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data?.getSerializableExtra("satObject") != null) {
                    satellitePosition =
                        data.getSerializableExtra("satObject") as SatellitesPositionData?
                    binding.satelliteTitle.text = satellitePosition?.getSatellite().toString() + ""
                    currentSatelliteLatitude = satellitePosition!!.getSatLatitude()
                    currentSatelliteLonigtude = satellitePosition!!.getSatLongitude()
//                    satelliteInfo()
                    binding.btnInfo.visibility = View.VISIBLE
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
                                    LatLng(currentSatelliteLatitude!!, currentSatelliteLonigtude!!)
                                ).width(10f).color(
                                    Color.BLACK
                                )
                        )?.endCap =
                            CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.ic_sat_on_map))

                    }
                }
                else {
                    binding.btnInfo.visibility = View.GONE
                }
            } else {
                binding.btnInfo.visibility = View.GONE
            }
        }

    private fun showNativeAd() {
        if (canWeShowAds(RemoteConfig.mapSatelliteNative)) {
            newLoadAndShowNativeAd(
                binding.layoutNative,
                R.layout.native_ad_layout_small,
                getString(R.string.mapScreenNativeId),
                adLoading = {
                    binding.layoutNative.visibility = View.VISIBLE
                },
                failToLoad = {
                    binding.layoutNative.visibility = View.GONE
                })

        }
    }

    private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        satelliteAdCounter = 0
    }
}