package com.example.satellitefinder.ui.activites

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
import com.example.satellitefinder.admobAds.loadAdmobInterstitial
import com.example.satellitefinder.admobAds.showPriorityAdmobInterstitial
import com.example.satellitefinder.databinding.ActivityNewSatelliteMeterBinding
import com.example.satellitefinder.databinding.NativeAdLayoutSmallBinding
import com.example.satellitefinder.databinding.SatelliteInfoSheetBinding
import com.example.satellitefinder.ui.activites.MapSatelliteActivity.Companion.MIN_DISTANCE_FOR_UPDATES
import com.example.satellitefinder.ui.activites.MapSatelliteActivity.Companion.MIN_TIME_UPDATES
import com.example.satellitefinder.ui.dialogs.InfoDialog
import com.example.satellitefinder.ui.dialogs.InfoSheet
import com.example.satellitefinder.utils.FirebaseEvents
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.currentLocation
import com.example.satellitefinder.utils.requestLocationPermissions
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class NewSatelliteMeterActivity : AppCompatActivity(), OnMapReadyCallback, SensorEventListener {
    private val binding by lazy {
        ActivityNewSatelliteMeterBinding.inflate(layoutInflater)
    }

    private val satelliteName by lazy {
        intent.getStringExtra("satelliteName")
    }
    private val satelliteAzimuth by lazy {
        intent.getDoubleExtra("satelliteAzimuth", 0.0).toInt()
    }
    private val satelliteElevation by lazy {
        intent.getDoubleExtra("satelliteElevation", 0.0).toInt()
    }

    private val satelliteLatitude by lazy {
        intent.getDoubleExtra("satelliteLat", 0.0)
    }
    private val satelliteLongitude by lazy {
        intent.getDoubleExtra("satelliteLng", 0.0)
    }
    private val fromIssTracker by lazy {
        intent.getBooleanExtra("fromIssTracker", false)
    }

    private var mComAziMuth = 0
    private var sensorManager: SensorManager? = null
    private var compassRotationV: Sensor? = null
    private var compassAccelerometer: Sensor? = null
    private var compassMagnetometer: Sensor? = null
    private var compassMat = FloatArray(9)
    private var orientation = FloatArray(9)
    private val compassLastAccelerometer = FloatArray(3)
    private val compassLastMagnetometer = FloatArray(3)
    private var haveSensorOne = false
    private var haveSensorTwo: Boolean = false
    private var isCompassLastAccelerometerSet = false
    private var isCompassLastMagnetometerSet = false
    private var vibrator: Vibrator? = null

    private var issSatelliteAzimuth = 0
    private var mTextToSpeech: TextToSpeech? = null
    private var isGPSEnabled = false
    private var isNetworkEnabled = false


    private var isCanGetLocation = false
    private var location: Location? = null
    private var latitude = 0.0
    private var longitude = 0.0
    private var sourceLatitude = ""
    private var sourceLongitude = ""
    var myLocation: LatLng? = null
    private var mMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null
    private val M_REQUEST_PERMISSION = 10
    private lateinit var dialog: InfoDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this@NewSatelliteMeterActivity,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!fromIssTracker) {
                        if (canWeShowAds(RemoteConfig.interBackMenus)) {
                            showPriorityAdmobInterstitial(canReload = false, closeListener = {
                                finish()

                            }, showListener = {

                            }, failListener = {
                                finish()

                            })
                        } else {
                            finish()
                        }
                    } else {
                        finish()
                    }
                }
            })
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
            getMyCurrentLocation()
            mapInitialization()
        } else {
            requestLocationPermissions()
        }


        dialog = InfoDialog(this@NewSatelliteMeterActivity)
        binding.apply {
            satelliteTitle.text = satelliteName
            /*  btnSatelliteInfo.setOnClickListener{

                      satelliteInfo()

              }*/

            ivBack.setOnClickListener {
                FirebaseEvents.logEvent(
                    "sate_finder_screen_click_back",
                    "sate_finder_screen_click_back"
                )


                onBackPressedDispatcher.onBackPressed()
            }

            btnSelectSatellite.setOnClickListener {
                FirebaseEvents.logEvent(
                    "sate_finder_screen_click_select_sate",
                    "sate_finder_screen_click_select_sate"
                )

                finish()
            }

            btnInfo.setOnClickListener {
                FirebaseEvents.logEvent(
                    "sate_finder_screen_click_info",
                    "sate_finder_screen_click_info"
                )


                InfoSheet(this@NewSatelliteMeterActivity).showSheet { sheetBinding ->
                    satelliteInfo(sheetBinding)

                }
            }
        }
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        //getCurrentLocation();
        mTextToSpeech = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                mTextToSpeech!!.language = Locale.UK
            }
        }

        binding.apply {
            compassAzimuthTv.visibility = View.GONE
            leftToRightTextTv.visibility = View.GONE
            textCompass.visibility = View.GONE
            moveAntenaTv.visibility = View.GONE
        }

        startSensor()
        stopSensor()
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator


    }

    @SuppressLint("SetTextI18n")
    private fun satelliteInfo(sheetBinding: SatelliteInfoSheetBinding) {
        if (currentLocation != null) {
            sheetBinding.apply {
                satAzimut.text = "${ if (fromIssTracker) issSatelliteAzimuth else satelliteAzimuth} °"
                satellitePositionTv.text = satelliteLongitude.toString()
                satElevation.text = "$satelliteElevation °"
                satelliteLNBskewTv.text = satelliteLatitude.toString()
            }
        }
    }

    private fun startSensor() {
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if (sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null || sensorManager!!.getDefaultSensor(
                    Sensor.TYPE_ACCELEROMETER
                ) == null
            ) {
                Toast.makeText(
                    this@NewSatelliteMeterActivity,
                    "No Sensor found",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                compassAccelerometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
                compassMagnetometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
                haveSensorOne = sensorManager!!.registerListener(
                    this,
                    compassAccelerometer,
                    SensorManager.SENSOR_DELAY_UI
                )
                haveSensorTwo = sensorManager!!.registerListener(
                    this,
                    compassMagnetometer,
                    SensorManager.SENSOR_DELAY_UI
                )
            }
        } else {
            compassRotationV = sensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            haveSensorOne =
                sensorManager!!.registerListener(
                    this,
                    compassRotationV,
                    SensorManager.SENSOR_DELAY_UI
                )
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent) {
        Log.e("TAG", "onSensorChanged: ")
        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(compassMat, event.values)
            mComAziMuth = ((Math.toDegrees(
                SensorManager.getOrientation(compassMat, orientation)[0]
                    .toDouble()
            ) + 360) % 360).toInt()
        }
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, compassLastAccelerometer, 0, event.values.size)
            isCompassLastAccelerometerSet = true
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, compassLastMagnetometer, 0, event.values.size)
            isCompassLastMagnetometerSet = true
        }
        if (isCompassLastAccelerometerSet && isCompassLastMagnetometerSet) {
            SensorManager.getRotationMatrix(
                compassMat,
                null,
                compassLastAccelerometer,
                compassLastMagnetometer
            )
            SensorManager.getOrientation(compassMat, orientation)
            mComAziMuth = ((Math.toDegrees(
                SensorManager.getOrientation(compassMat, orientation)[0]
                    .toDouble()
            ) + 360) % 360).toInt()
        }
        mComAziMuth = Math.round(mComAziMuth.toFloat())

        binding.rotateRelativeLayout.rotation = -mComAziMuth.toFloat()
        binding.sateImageView.rotation = if (fromIssTracker) issSatelliteAzimuth.toFloat() else satelliteAzimuth.toFloat()
        var where = "No"
        if (mComAziMuth >= 350 || mComAziMuth <= 10) where = getString(R.string.north)
        if (mComAziMuth >= 350 || mComAziMuth <= 280) where = getString(R.string.northwest)
        if (mComAziMuth >= 280 || mComAziMuth <= 260) where = getString(R.string.west)
        if (mComAziMuth >= 260 || mComAziMuth <= 190) where = getString(R.string.southwest)
        if (mComAziMuth >= 190 || mComAziMuth <= 170) where = getString(R.string.south)
        if (mComAziMuth >= 170 || mComAziMuth <= 100) where = getString(R.string.southeast)
        if (mComAziMuth >= 100 || mComAziMuth <= 80) where = getString(R.string.east)
        if (mComAziMuth >= 80 || mComAziMuth <= 10) where = getString(R.string.north_east)
        binding.texCompAzimuth.text = "$mComAziMuth°$where"
        Log.e("TAG", "onSensorChanged: " + "$mComAziMuth°$where")


        //pppppp = comAziMuth -satelliteAzimuth;
        var i = mComAziMuth - if (fromIssTracker) issSatelliteAzimuth else satelliteAzimuth
        if (i < 0) {
            i *= -1
            binding.compassAzimuthTv.text = i.toString() + "° " + "Right"
        } else {
            binding.compassAzimuthTv.text = i.toString() + "° " + "Left"
        }
        val azimuth = if (fromIssTracker) issSatelliteAzimuth else satelliteAzimuth
        if (mComAziMuth == azimuth && azimuth > 0) {
            vibrate()
            binding.satelliteAzimuthIv.setImageResource(R.drawable.ic_satellite_match)
            binding.compassAzimuthTv.text = "Perfect"
            binding.texCompAzimuth.background = getDrawable(R.drawable.ic_sat_antena_matched)
            val speakingtext = "match"
            mTextToSpeech!!.speak(speakingtext, TextToSpeech.QUEUE_FLUSH, null)
        } else {
            binding.texCompAzimuth.background = getDrawable(R.drawable.ic_sat_antena)
            binding.compassAzimuthTv.setTextColor(Color.BLUE)
        }
    }

    private fun vibrate() {
        if (vibrator != null) {
            vibrator!!.vibrate(200)
        }
    }

    private fun stopSensor() {
        if (haveSensorOne && haveSensorTwo) {
            sensorManager!!.unregisterListener(this, compassAccelerometer)
            sensorManager!!.unregisterListener(this, compassMagnetometer)
        } else if (haveSensorOne) {
            sensorManager!!.unregisterListener(this, compassRotationV)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onResume() {
        super.onResume()
        startSensor()
    }

    override fun onPause() {
        super.onPause()
        stopSensor()
    }

    private fun mapInitialization() {
        if (mMap == null) {
            val fm = supportFragmentManager
            mapFragment = fm.findFragmentById(R.id.provider_map) as SupportMapFragment?
            mapFragment!!.getMapAsync(this)
        }
        if (mMap != null) {
            setupMap()
        }
    }

    private fun setupMap() {
        if (mMap != null) {
            mMap!!.uiSettings.isCompassEnabled = false
            mMap!!.isBuildingsEnabled = true
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

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setupMap()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            M_REQUEST_PERMISSION -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapInitialization()
                getMyCurrentLocation()
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun getMyCurrentLocation() {
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
            isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)

            isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                isCanGetLocation = true
                if (isNetworkEnabled) {
                    val requestPermissionsCode = 50
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_UPDATES,
                        MIN_DISTANCE_FOR_UPDATES.toFloat(), mLocationListener
                    )
                    Log.d("Network", "Network")
                    if (locationManager != null) {
                        location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null) {
                            latitude = location!!.latitude
                            longitude = location!!.longitude
                            sourceLatitude = "" + location!!.latitude
                            sourceLongitude = "" + location!!.longitude
                            currentLocation = location
                            if (fromIssTracker) {
                                issSatelliteAzimuth = getSatelliteAzimut(satelliteLongitude).toInt()
                            }
                            myLocation = LatLng(location!!.latitude, location!!.longitude)


                        }
                    }
                }
            }
            // If GPS enabled, get latitude/longitude using GPS Services
            if (isGPSEnabled) {
                if (location == null) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this@NewSatelliteMeterActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@NewSatelliteMeterActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            M_REQUEST_PERMISSION
                        )
                    } else {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_UPDATES,
                            MIN_DISTANCE_FOR_UPDATES.toFloat(), mLocationListener
                        )
                        Log.e("GPS Enabled", "GPS Enabled")
                        if (locationManager != null) {
                            location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (location != null) {
                                latitude = location!!.latitude
                                longitude = location!!.longitude
                                sourceLatitude = "" + location!!.latitude
                                sourceLongitude = "" + location!!.longitude
                                currentLocation = location
                                if (fromIssTracker) {
                                    issSatelliteAzimuth = getSatelliteAzimut(satelliteLongitude).toInt()
                                }
                                myLocation = LatLng(location!!.latitude, location!!.longitude)


                            }
                        }
                    }
                }
            }
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

                if (fromIssTracker) {
                    issSatelliteAzimuth = getSatelliteAzimut(satelliteLongitude).toInt()
                }
                myLocation = LatLng(location.latitude, location.longitude)

            } catch (e: Exception) {
                Log.e("TAG", "onLocationChanged: $e")
            }

            //            addUserLocation(source_lat,source_lng);
        }

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {
        }

        override fun onProviderEnabled(s: String) {}
        override fun onProviderDisabled(s: String) {}
    }

    fun getSatelliteAzimut(mSatelliteLongitude: Double): Double {
        var azimuth = 0.0
        var beta = 0.0
        val currentLat = currentLocation?.let { Math.toRadians(it.getLatitude()) }
        val satelliteLong = Math.toRadians(mSatelliteLongitude)
        val currentLong = currentLocation?.let { Math.toRadians(it.getLongitude()) }
        beta = Math.tan(satelliteLong - currentLong!!) / Math.sin(currentLat!!)

        azimuth = if (Math.abs(beta) < Math.PI) {
            Math.PI - Math.atan(beta)
        } else {
            Math.PI - Math.atan(beta)
        }
        if (currentLocation!!.getLatitude() < 0.0) {
            azimuth -= Math.PI
        }
        if (azimuth < 0.0) {
            azimuth += 2 * Math.PI
        }
        return Math.toDegrees(azimuth)
    }

    private fun showNativeAd() {

        if (!fromIssTracker) {
            if (canWeShowAds(RemoteConfig.interBackMenus)) {
                loadAdmobInterstitial(getString(R.string.interstialId))
            }
        }


        if (canWeShowAds(RemoteConfig.satelliteFindNative)) {
            binding.layoutNative.visibility = View.VISIBLE
            val bindAdNative = NativeAdLayoutSmallBinding.inflate(layoutInflater)

            NativeAdUtils(
                this@NewSatelliteMeterActivity.application,
                "satellite_find"
            ).loadNativeAd(
                adsKey = getString(R.string.findSatelliteNativeId),
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

}