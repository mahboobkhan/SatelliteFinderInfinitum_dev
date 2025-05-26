package com.example.satellitefinder.ui.activites

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adssdk.banner_ads.BannerAdUtils
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.databinding.ActivityPendulumBinding
import com.example.satellitefinder.utils.canWeShowAds
import com.example.satellitefinder.utils.showToast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class PendulumActivity : AppCompatActivity(), SensorEventListener {
    private val binding by lazy {
        ActivityPendulumBinding.inflate(layoutInflater)
    }

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null


    private var torchEnable = false
    private var cameraEnable = false
    private var backgroundEnable = true
    private var isUsingBackCamera = true
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        setUpPendulumBobView()
        setupClickListeners()
        initializeCameraProvider()
        showBannerAd()
    }

    private fun setUpPendulumBobView() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]
            binding.tvPendulumAngle.text = "%.1f°".format(x)
            binding.pendulumBobView.updateAngle(x)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun setupClickListeners() = with(binding) {

        ivBack.setOnClickListener { finish() }

        ifvBackground.setOnClickListener {
            if (!backgroundEnable) {
                // Enable background, disable camera
                backgroundEnable = true
                cameraEnable = false
                torchEnable = false

                ifvBackground.setImageResource(R.drawable.ic_background_enable)
                ifvCamera.setImageResource(R.drawable.ic_camera_disable)
                ifvTorch.setImageResource(R.drawable.ic_torch_disable)

                cameraPreview.visibility = View.GONE
                stopCamera()
            }
            // Do nothing if background is already enabled
        }

        ifvCamera.setOnClickListener {
            if (checkCameraPermission()) {
                if (cameraEnable) {
                    val newCameraSelector = if (isUsingBackCamera) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    }
                    isUsingBackCamera = !isUsingBackCamera

                    startCamera(newCameraSelector)
                } else {
                    cameraEnable = true
                    backgroundEnable = false

                    ifvCamera.setImageResource(R.drawable.ic_camera_enable)
                    ifvBackground.setImageResource(R.drawable.ic_background_disable)

                    cameraPreview.visibility = View.VISIBLE
                    startCamera()
                }
            } else {
                checkCameraPermission(onPermissionsGranted = {
                    if (cameraEnable) {
                        showToast("Camera already enabled — switch logic can go here.")
                        // Optional: Implement front/back switch
                    } else {
                        cameraEnable = true
                        backgroundEnable = false

                        ifvCamera.setImageResource(R.drawable.ic_camera_enable)
                        ifvBackground.setImageResource(R.drawable.ic_background_disable)

                        cameraPreview.visibility = View.VISIBLE
                        startCamera()
                    }
                }, onPermissionsDenied = {
                    showRationaleDialog()
                }, onError = {
                    showToast(it)
                })
            }
        }

        ifvTorch.setOnClickListener {
            if (cameraEnable) {
                torchEnable = !torchEnable
                camera?.cameraControl?.enableTorch(torchEnable)
                ifvTorch.setImageResource(
                    if (torchEnable) R.drawable.ic_torch_enable else R.drawable.ic_torch_disable
                )
            } else {
                showToast("Please Enable Camera First")
            }
        }
    }


    private fun initializeCameraProvider() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun startCamera(cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA) {
        val cameraProvider = cameraProvider ?: return
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
        }

        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview)
        } catch (e: Exception) {
            showToast("Failed to start camera: ${e.localizedMessage}")
        }
    }

    private fun stopCamera() {
        cameraProvider?.unbindAll()
    }

    fun Context.checkCameraPermission(
        onPermissionsDenied: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null,
        onPermissionsGranted: (() -> Unit)? = null,
    ) {
        Dexter.withContext(this).withPermissions(Manifest.permission.CAMERA)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report?.areAllPermissionsGranted() == true) {
                        onPermissionsGranted?.invoke()
                    }

                    if (report?.isAnyPermissionPermanentlyDenied == true) {
                        onPermissionsDenied?.invoke()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).withErrorListener { error ->
                onError?.invoke("Error occurred! $error")
            }.onSameThread().check()
    }

    fun Context.checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Function to open app settings
    private fun openAppSettings() {
        try {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        } catch (e: Exception) {
            e.printStackTrace()

        }
    }


    private fun showRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage("You have denied the permission permanently. Please go to the app settings to allow the permission.")
            .setPositiveButton("Go to Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun showBannerAd() {
        if (canWeShowAds(RemoteConfig.pendulumBanner)) {
            binding.bannerAdContainer.visibility = View.VISIBLE
            BannerAdUtils(activity = this, screenName = "level")
                .loadBanner(
                    adsKey = getString(R.string.bannerId),
                    remoteConfig = RemoteConfig.pendulumBanner,
                    adsView = binding.bannerAdContainer,
                    shimmerLayout = binding.clShimmer,
                    onAdClicked = {},
                    onAdFailedToLoads = { _, _ -> },
                    onAdImpression = {},
                    onAdLoaded = { _, _ -> },
                    onAdOpened = {},
                    onAdValidate = {
                        binding.bannerAdContainer.visibility = View.GONE
                        binding.clShimmer.visibility = View.GONE
                    })
        } else {
            binding.bannerAdContainer.visibility = View.GONE
            binding.clShimmer.visibility = View.GONE
        }
    }
}