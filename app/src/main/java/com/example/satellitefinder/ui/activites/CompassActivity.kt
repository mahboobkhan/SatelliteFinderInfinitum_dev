package com.example.satellitefinder.ui.activites

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.admobAds.newLoadAndShowNativeAd
import com.example.satellitefinder.admobAds.showPriorityInterstitialAdWithCounter
import com.example.satellitefinder.databinding.ActivityCompassBinding
import com.example.satellitefinder.utils.FirebaseEvents
import com.example.satellitefinder.utils.LanguagesHelper
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.screenEventAnalytics

class CompassActivity : AppCompatActivity(), SensorEventListener {

    var mCompassAzimuth = 0
    private var sensorManager: SensorManager? = null
    var mat = FloatArray(9)
    var mCompassOrientation = FloatArray(9)
    private val myLastAccelerometer = FloatArray(3)
    private val myLastMagnetometer = FloatArray(3)
    private var isHaveSensorOne = false
    private var isHaveSensorTwo = false
    private var myLastAccelerometerSet = false
    private var myLastMagnetometerSet = false
    private var myRotationV: Sensor? = null
    private var myAccelerometer: Sensor? = null
    private var myMagnetometer: Sensor? = null

    val binding: ActivityCompassBinding by lazy {
        ActivityCompassBinding.inflate(layoutInflater)
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
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        checkDeviceSensorAvailable()

        FirebaseEvents.logEventActivity("compass_screen", "compass_screen")

        showNativeAd()

        screenEventAnalytics("CompassActivity")
        binding.ivBack.setOnClickListener {
            FirebaseEvents.logEvent("compass_screen_click_back", "compass_screen_click_back")
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this@CompassActivity, callback)
    }

    override fun onStop() {
        super.onStop()
        stop()
    }

    override fun onPause() {
        super.onPause()
        checkDeviceSensorAvailable()
    }

    private fun checkDeviceSensorAvailable() {
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if (sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null || sensorManager!!.getDefaultSensor(
                    Sensor.TYPE_ACCELEROMETER
                ) == null
            ) {
                /*noSensorAlert()*/
            } else {
                myAccelerometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
                myMagnetometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
                isHaveSensorOne = sensorManager!!.registerListener(
                    this,
                    myAccelerometer,
                    SensorManager.SENSOR_DELAY_UI
                )
                isHaveSensorTwo = sensorManager!!.registerListener(
                    this,
                    myMagnetometer,
                    SensorManager.SENSOR_DELAY_UI
                )
            }
        } else {
            myRotationV = sensorManager!!.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            isHaveSensorOne =
                sensorManager!!.registerListener(this, myRotationV, SensorManager.SENSOR_DELAY_UI)
        }
    }


    private fun stop() {
        if (isHaveSensorOne && isHaveSensorTwo) {
            sensorManager!!.unregisterListener(this, myAccelerometer)
            sensorManager!!.unregisterListener(this, myMagnetometer)
        } else if (isHaveSensorOne) {
            sensorManager!!.unregisterListener(this, myRotationV)
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(mat, event.values)
            mCompassAzimuth = ((Math.toDegrees(
                SensorManager.getOrientation(mat, mCompassOrientation)[0].toDouble()
            ) + 360) % 360).toInt()
        }
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, myLastAccelerometer, 0, event.values.size)
            myLastAccelerometerSet = true
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, myLastMagnetometer, 0, event.values.size)
            myLastMagnetometerSet = true
        }
        if (myLastAccelerometerSet && myLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mat, null, myLastAccelerometer, myLastMagnetometer)
            SensorManager.getOrientation(mat, mCompassOrientation)
            mCompassAzimuth = ((Math.toDegrees(
                SensorManager.getOrientation(mat, mCompassOrientation)[0].toDouble()
            ) + 360) % 360).toInt()
        }
        mCompassAzimuth = Math.round(mCompassAzimuth.toFloat())
        binding.mainCompass.rotation = -mCompassAzimuth.toFloat()
        var where = "NO"
        if (mCompassAzimuth >= 350 || mCompassAzimuth <= 10) where = getString(R.string.north)
        if (mCompassAzimuth >= 350 || mCompassAzimuth <= 280) where = getString(R.string.northwest)
        if (mCompassAzimuth >= 280 || mCompassAzimuth <= 260) where = getString(R.string.west)
        if (mCompassAzimuth >= 260 || mCompassAzimuth <= 190) where = getString(R.string.southwest)
        if (mCompassAzimuth >= 190 || mCompassAzimuth <= 170) where = getString(R.string.south)
        if (mCompassAzimuth >= 170 || mCompassAzimuth <= 100) where = getString(R.string.southeast)
        if (mCompassAzimuth >= 100 || mCompassAzimuth <= 80) where = getString(R.string.east)
        if (mCompassAzimuth >= 80 || mCompassAzimuth <= 10) where = getString(R.string.north_east)
        binding.tvCompassText.text = "$mCompassAzimuth°$where"
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    private fun showNativeAd() {
        if (canWeShowAds(RemoteConfig.compassNative)) {
            newLoadAndShowNativeAd(
                binding.layoutNative,
                R.layout.native_ad_layout_small,
                getString(R.string.compassScreenNativeId),
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
}