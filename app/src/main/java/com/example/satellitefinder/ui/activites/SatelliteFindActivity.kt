package com.example.satellitefinder.ui.activites

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.newLoadAndShowNativeAd
import com.example.satellitefinder.admobAds.showPriorityAdmobInterstitial
import com.example.satellitefinder.admobAds.showPriorityInterstitialAdWithCounter
import com.example.satellitefinder.databinding.ActivitySatelliteFindBinding
import com.example.satellitefinder.databinding.SatelliteInfoSheetBinding
import com.example.satellitefinder.ui.dialogs.InfoDialog
import com.example.satellitefinder.ui.dialogs.InfoSheet
import com.example.satellitefinder.ui.dialogs.RattingDialog
import com.example.satellitefinder.utils.MySharePrefrencesHelper
import com.example.satellitefinder.utils.SatellitesPositionData
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.currentLocation
import com.example.satellitefinder.utils.isRatting
import com.example.satellitefinder.utils.requestLocationPermissions
import com.example.satellitefinder.utils.satelliteAdCounter
import com.example.satellitefinder.utils.screenEventAnalytics
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import java.util.Locale

class SatelliteFindActivity : AppCompatActivity(), OnMapReadyCallback, SensorEventListener {
    private val binding:ActivitySatelliteFindBinding by lazy{
        ActivitySatelliteFindBinding.inflate(layoutInflater)
    }

    private var mComAziMuth = 0
    private var sensorManager: SensorManager? = null
    private var compassRotationV: Sensor? = null
    private  var compassAccelerometer: Sensor? = null
    private  var compassMagnetometer: Sensor? = null
    private var compassMat = FloatArray(9)
    private var orientation = FloatArray(9)
    private val compassLastAccelerometer = FloatArray(3)
    private val compassLastMagnetometer = FloatArray(3)
    private var haveSensorOne = false
    private  var haveSensorTwo:Boolean = false
    private var isCompassLastAccelerometerSet = false
    private var isCompassLastMagnetometerSet = false
    private var vibrator: Vibrator? = null
    private var mSatelliteAzimuth = 0
    private var mTextToSpeech: TextToSpeech? = null
    private var isGPSEnabled = false
    private var satellitePosition: SatellitesPositionData? = null

    private var isNetworkEnabled = false

    private var reviewManager: ReviewManager? = null
    private var reviewInfo: ReviewInfo? = null
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
    private lateinit var dialog:InfoDialog
    private var adCountInfo = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (canWeShowAds(RemoteConfig.interAll)){

            showPriorityInterstitialAdWithCounter(true,getString(R.string.interstialId),)
        }
        setContentView(binding.root)


        showNativeAd()

        screenEventAnalytics("SatelliteFindActivity")

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


        dialog = InfoDialog(this@SatelliteFindActivity)
        binding.apply {

          /*  btnSatelliteInfo.setOnClickListener{

                    satelliteInfo()

            }*/

            ivBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            btnSelectSatellite.setOnClickListener {
                 selectSatellite()

            }

            btnInfo.setOnClickListener {
                if (canWeShowAds(RemoteConfig.interAll) && adCountInfo >= 2) {
                    showPriorityAdmobInterstitial(true, getString(R.string.interstialId), closeListener = {
                        InfoSheet(this@SatelliteFindActivity).showSheet { sheetBinding ->
                            satelliteInfo(sheetBinding)
                        }
                    }, failListener = {
                        InfoSheet(this@SatelliteFindActivity).showSheet { sheetBinding ->
                            satelliteInfo(sheetBinding)
                        }
                    }, showListener = {
                        adCountInfo = 0
                    })
                } else {
                    adCountInfo++
                    InfoSheet(this@SatelliteFindActivity).showSheet { sheetBinding ->
                        satelliteInfo(sheetBinding)
                    }
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

        onBackPressedDispatcher.addCallback(this@SatelliteFindActivity, callback)

    }

    private fun selectSatellite() {
        if (location != null) {
            val intent = Intent(this@SatelliteFindActivity, SatellitesActivity::class.java)
            resultLauncher.launch(intent)
        } else {
            Toast.makeText(
                this@SatelliteFindActivity,
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

    @SuppressLint("SetTextI18n")
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            try{
                satellitePosition = data?.getSerializableExtra("satObject") as SatellitesPositionData?
                binding.satelliteTitle.text = satellitePosition!!.getSatellite().toString() + ""
                binding.textCompass.visibility = View.VISIBLE
                mSatelliteAzimuth = satellitePosition!!.getSatelliteAzimut()
                binding.btnInfo.visibility = View.VISIBLE
//                satelliteInfo()
            }
            catch (e:Exception){
                binding.btnInfo.visibility = View.GONE
                Log.e("TAG", ": $e" )
            }

        }
    }

    private fun startSensor() {
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if (sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null || sensorManager!!.getDefaultSensor(
                    Sensor.TYPE_ACCELEROMETER
                ) == null
            ) {
                Toast.makeText(this@SatelliteFindActivity, "No Sensor found", Toast.LENGTH_SHORT).show()
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
                sensorManager!!.registerListener(this, compassRotationV, SensorManager.SENSOR_DELAY_UI)
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent) {
        Log.e("TAG", "onSensorChanged: " )
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
            SensorManager.getRotationMatrix(compassMat, null, compassLastAccelerometer, compassLastMagnetometer)
            SensorManager.getOrientation(compassMat, orientation)
            mComAziMuth = ((Math.toDegrees(
                SensorManager.getOrientation(compassMat, orientation)[0]
                    .toDouble()
            ) + 360) % 360).toInt()
        }
        mComAziMuth = Math.round(mComAziMuth.toFloat())

        binding.rotateRelativeLayout.rotation = -mComAziMuth.toFloat()
        binding.sateImageView.rotation = mSatelliteAzimuth.toFloat()
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
        var i = mComAziMuth - mSatelliteAzimuth
        if (i < 0) {
            i *= -1
            binding.compassAzimuthTv.text = i.toString() + "° " + "Right"
        } else {
            binding.compassAzimuthTv.text = i.toString() + "° " + "Left"
        }
        if (mComAziMuth == mSatelliteAzimuth && mSatelliteAzimuth > 0) {
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
                            this@SatelliteFindActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@SatelliteFindActivity,
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

            try{
                sourceLatitude = "" + location.latitude
                sourceLongitude = "" + location.longitude
                currentLocation = location
                myLocation = LatLng(location.latitude, location.longitude)

            }catch (e:Exception){
                Log.e("TAG", "onLocationChanged: $e" )
            }

            //            addUserLocation(source_lat,source_lng);
        }

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
        override fun onProviderEnabled(s: String) {}
        override fun onProviderDisabled(s: String) {}
    }

    companion object {
        private const val MIN_DISTANCE_FOR_UPDATES: Long = 1000 // 10 meters

        private const val MIN_TIME_UPDATES = (1000 * 60 * 1 // 1 minute
                ).toLong()
    }

    private fun showNativeAd(){
        if (canWeShowAds(RemoteConfig.satelliteFindNative)) {
            newLoadAndShowNativeAd(
                binding.layoutNative,
                R.layout.native_ad_layout_small,
                getString(R.string.findSatelliteNativeId),
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
            if (!MySharePrefrencesHelper.getBoolean(this@SatelliteFindActivity, isRatting)){
                val ratingDailog = RattingDialog(this@SatelliteFindActivity){
                    finish()
                }
                ratingDailog.show()
            } else {
                finish()
            }

            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        satelliteAdCounter = 0
    }
}