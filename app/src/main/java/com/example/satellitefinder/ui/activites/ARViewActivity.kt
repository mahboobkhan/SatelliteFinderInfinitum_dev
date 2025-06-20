package com.example.satellitefinder.ui.activites

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.satellitefinder.databinding.ActivityArviewBinding
import com.example.satellitefinder.models.SatelliteModel
import com.example.satellitefinder.repo.SatelliteViewModel
import com.example.satellitefinder.utils.SatelliteCalculator
import com.example.satellitefinder.utils.SatelliteWestMap
import com.example.satellitefinder.utils.custom_views.DrawableSatellite
import com.example.satellitefinder.utils.custom_views.OrientationManager
import com.example.satellitefinder.utils.custom_views.SatelliteOverlayView
import com.example.satellitefinder.utils.showToast
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.ext.android.viewModel

class ARViewActivity : AppCompatActivity() {

    private val binding: ActivityArviewBinding by lazy {
        ActivityArviewBinding.inflate(layoutInflater)
    }

    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var orientationManager: OrientationManager
    private var cameraXAngle: Double = Math.toRadians(60.0) // fallback default
    private var cameraYAngle: Double = Math.toRadians(45.0) // fallback default
    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0

    private val satelliteFromInternet by lazy {
        intent.getBooleanExtra("satelliteFromInternet", false)
    }

    private val satelliteType by lazy {
        intent.getStringExtra("satellite_type")
    }

    private var listOfSatellites: List<SatelliteModel> = listOf()

    private lateinit var satelliteOverlayView: SatelliteOverlayView
    private val viewModels: SatelliteViewModel by viewModel()

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
        initializeCameraProvider()
        setupLocation()
        satelliteOverlayView = binding.satelliteOverlayView
        // Orientation sensor
        orientationManager = OrientationManager(this)
        getCameraAngles() // get FOV
        observeViewModel()

    }

    private fun setupLocation() {
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
                    binding.apply {
                        tvLat.text = "Lat: ${location.latitude}"
                        tvLong.text = "Lon: ${location.longitude}"
                    }
                    userLatitude = location.latitude
                    userLongitude = location.longitude
                    updateAndSetSatellites()
                }
            }
        }
    }

    private fun updateAndSetSatellites() {
        if (userLatitude == 0.0 && userLongitude == 0.0) return

        val drawableList: List<DrawableSatellite>
        if (!satelliteFromInternet) {
            binding.progressBar.visibility = View.GONE
            binding.pointer.visibility = View.VISIBLE
            val satelliteMap = SatelliteWestMap.getMap(userLatitude, userLongitude)
            drawableList = satelliteMap.values.map { satInfo ->
                val position =
                    SatelliteCalculator.calculate(userLatitude, userLongitude, satInfo.longitude)
                DrawableSatellite(satInfo.name, position.azimuth, position.elevation)
            }
        } else {
            if (listOfSatellites.isEmpty()) {
                binding.progressBar.visibility = View.VISIBLE
                binding.pointer.visibility = View.GONE
                satelliteType?.let {
                    viewModels.loadSatellites(it)
                }
                return
            } else {
                drawableList = listOfSatellites
                    .mapNotNull { satModel ->
                        // Calculate the position and check if it's above the horizon
                        val position = SatelliteCalculator.calculate(
                            userLatitude,
                            userLongitude,
                            satModel.longitude
                        )
                        if (position.elevation >= 0) {
                            DrawableSatellite(satModel.name, position.azimuth, position.elevation)
                        } else {
                            null
                        }
                    }.distinctBy { it.azimuth.toInt() }
                    .sortedByDescending { it.azimuth }
            }
        }

        satelliteOverlayView.setSatellites(drawableList)

    }

    private fun observeViewModel() {
        viewModels.satelliteList.observe(this) {
            if (it.isNotEmpty()) {
                binding.progressBar.visibility = View.GONE
                binding.pointer.visibility = View.VISIBLE
                listOfSatellites = it
                updateAndSetSatellites()
            } else {
                showToast("No satellites found")
                finish()
            }
        }
    }

    private fun initializeCameraProvider() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            startCamera()
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

    private fun getCameraAngles() {
        // Try to get camera FOV from Camera2 API, fallback to defaults
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            for (cameraId in cameraManager.cameraIdList) {
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                    val focalLengths =
                        characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)
                    val sensorSize =
                        characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE)
                    if (sensorSize != null && focalLengths != null && focalLengths.isNotEmpty()) {
                        val fl = focalLengths[0]
                        cameraXAngle =
                            2 * Math.atan2((sensorSize.width / 2).toDouble(), fl.toDouble())
                        cameraYAngle =
                            2 * Math.atan2((sensorSize.height / 2).toDouble(), fl.toDouble())
                        break
                    }
                }
            }
        } catch (e: Exception) {
            // fallback to defaults
        }
    }

    override fun onResume() {
        super.onResume()
        // Pass display rotation to orientationManager
        try {
            val rotation = windowManager.defaultDisplay.rotation
            orientationManager.setDisplayRotation(rotation)
            orientationManager.start()
            startOrientationListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        orientationManager.stop()
    }

    private fun startOrientationListener() {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                // Use orientationManager's azimuth and pitch (in degrees)
                val azimuth = Math.toRadians(orientationManager.azimuth)
                val pitch = Math.toRadians(orientationManager.pitch)
                // Pass FOV and orientation to overlay
                satelliteOverlayView.deviceAzimuth = azimuth
                satelliteOverlayView.devicePitch = -pitch
                satelliteOverlayView.cameraXAngle = cameraXAngle
                satelliteOverlayView.cameraYAngle = cameraYAngle
                satelliteOverlayView.invalidate()
                // Optionally update UI

                binding.tvAzimuth.text = "Azimuth: %.2f°".format(orientationManager.azimuth)
                binding.tvElevation.text = "Elevation: %.2f°".format(-orientationManager.pitch)
                // Show "face the sky" message if pitch is too low (facing ground)
                if (orientationManager.pitch > 60 || orientationManager.pitch < -120) {
                    binding.tvFaceSky.visibility = View.VISIBLE
                } else {
                    binding.tvFaceSky.visibility = View.GONE
                }
                handler.post {
                    binding.ivArrowLeft.visibility =
                        if (satelliteOverlayView.hasLeftSatellite) View.VISIBLE else View.GONE
                    binding.ivArrowRight.visibility =
                        if (satelliteOverlayView.hasRightSatellite) View.VISIBLE else View.GONE
                }

                handler.postDelayed(this, 100) // update every 100ms
            }
        })
    }
}