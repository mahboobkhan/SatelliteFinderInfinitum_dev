package com.example.satellitefinder.ui.activites

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adssdk.native_ad.NativeAdType
import com.example.adssdk.native_ad.NativeAdUtils
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.databinding.ActivityInclinometerBinding
import com.example.satellitefinder.databinding.NativeAdLayoutSmallBinding
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.showToast

class InclinometerActivity : AppCompatActivity() {

    private val binding: ActivityInclinometerBinding by lazy {
        ActivityInclinometerBinding.inflate(layoutInflater)
    }

    lateinit var _sm: SensorManager

    private var gravity = FloatArray(3)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.ivBack.setOnClickListener {
            finish()
        }


        binding.btnResetDeviceSensor.setOnClickListener {
            showToast("Reset Successfully")
        }

        _sm = getSystemService(SENSOR_SERVICE) as SensorManager
        setAccelerometerListener()

        loadNativeAd()
    }

    private fun setAccelerometerListener() {
        if (_sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            _sm.registerListener(object : SensorEventListener {
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                }

                override fun onSensorChanged(event: SensorEvent?) {
                    if (event == null) return

                    val alpha = 0.8f
                    gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]
                    gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]
                    gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]

                    val pitch =
                        Math.atan2(gravity[0].toDouble(), gravity[2].toDouble()) * 180 / Math.PI
                    val roll =
                        Math.atan2(gravity[1].toDouble(), gravity[2].toDouble()) * 180 / Math.PI

                    // Display pitch and roll
                    binding.tvMainAngle.text = "%.1f°".format(event.values[0])
                    binding.tvPitchAngle.text = "%.1f°".format(pitch)
                    binding.tvRollAngle.text = "%.1f°".format(roll)


                    // Send roll to bubble (adjust as needed)
                    binding.mainLevelView.setValues(pitch.toFloat())
                    binding.bubbleView2D.setValues(roll.toFloat(), pitch.toFloat())

                }
            }, _sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun loadNativeAd() {
        if (canWeShowAds(RemoteConfig.inclinometerNative)) {
            binding.layoutNative.visibility = View.VISIBLE
            val bindAdNative = NativeAdLayoutSmallBinding.inflate(layoutInflater)

            NativeAdUtils(this@InclinometerActivity.application, "inclinometer").loadNativeAd(
                adsKey = getString(R.string.mapScreenNativeId),
                remoteConfig = RemoteConfig.inclinometerNative,
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