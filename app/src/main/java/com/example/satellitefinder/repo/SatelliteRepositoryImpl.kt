package com.example.satellitefinder.repo

import android.content.Context
import android.util.Log
import com.example.satellitefinder.database.GeneralSatelliteEntity
import com.example.satellitefinder.database.IssSatelliteEntity
import com.example.satellitefinder.database.SatelliteDatabase
import com.example.satellitefinder.database.StarlinkSatelliteEntity
import com.example.satellitefinder.database.WeatherSatelliteEntity
import com.example.satellitefinder.models.SatelliteModel
import com.example.satellitefinder.utils.isInternetConnected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.orekit.data.DataContext
import org.orekit.data.DirectoryCrawler
import org.orekit.frames.FramesFactory
import org.orekit.propagation.analytical.tle.TLEPropagator
import org.orekit.time.AbsoluteDate
import org.orekit.time.TimeScalesFactory
import org.orekit.utils.IERSConventions
import java.io.File
import java.io.FileOutputStream
import java.util.Date

class SatelliteRepositoryImpl(
    private val db: SatelliteDatabase,
    private val context: Context,
    private val dataManager: SatelliteDataManager
) : SatelliteRepository {

    override suspend fun fetchTLESatellites(satelliteType: String): List<SatelliteModel> =
        withContext(Dispatchers.IO) {
            // First try to get data from database
            val cachedData = when (satelliteType) {
                "weather" -> db.weatherDao().getAll().map { it.toModel() }
                "general" -> db.generalDao().getAll().map { it.toModel() }
                "starlink" -> db.starlinkDao().getAll().map { it.toModel() }
                "iss" -> db.issDao().getAll().map { it.toModel() }
                else -> emptyList()
            }

            // If we have cached data, return it immediately
            if (cachedData.isNotEmpty()) {
                Log.d(
                    "SatelliteRepository",
                    "Returning cached data for $satelliteType: ${cachedData.size} satellites"
                )
                return@withContext cachedData
            }

            // If no cached data, load static data immediately
            Log.d("SatelliteRepository", "No cached data for $satelliteType, loading static data")
            val staticData = dataManager.getStaticData(satelliteType)

            if (staticData.isNotEmpty()) {
                Log.d(
                    "SatelliteRepository",
                    "Returning static data for $satelliteType: ${staticData.size} satellites"
                )

                // Trigger background network request to fetch fresh data for next time
                if (isInternetConnected(context) && !dataManager.isLoading(satelliteType)) {
                    Log.d("SatelliteRepository", "Starting background network fetch for $satelliteType")
                    CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                        try {
                            dataManager.loadSatelliteData(satelliteType)
                           /* val freshData = fetchFromNetwork(satelliteType)
                            if (freshData.isNotEmpty()) {
                                when (satelliteType) {
                                    "weather" -> {
                                        db.weatherDao().deleteAll()
                                        db.weatherDao().insertAll(freshData.map { it.toEntity() })
                                    }

                                    "general" -> {
                                        db.generalDao().deleteAll()
                                        db.generalDao()
                                            .insertAll(freshData.map { it.toGeneralEntity() })
                                    }

                                    "starlink" -> {
                                        db.starlinkDao().deleteAll()
                                        db.starlinkDao()
                                            .insertAll(freshData.map { it.toStarlinkEntity() })
                                    }

                                    "iss" -> {
                                        db.issDao().deleteAll()
                                        db.issDao().insertAll(freshData.map { it.toIssEntity() })
                                    }
                                }*/

//                            }
                        } catch (e: Exception) {
                            Log.e(
                                "SatelliteRepository",
                                "Background fetch failed for $satelliteType",
                                e
                            )
                        }
                    }
                }

                return@withContext staticData
            }

            // Check internet connectivity before attempting network request
            if (!isInternetConnected(context)) {
                Log.w("SatelliteRepository", "No internet connection for $satelliteType")
                return@withContext emptyList()
            }

            // If no cached data and data manager is not loading, trigger a load
            if (!dataManager.isLoading(satelliteType)) {
                Log.d(
                    "SatelliteRepository",
                    "No cached data for $satelliteType, fetching from network"
                )
                val fromNetwork = fetchFromNetwork(satelliteType)

                when (satelliteType) {
                    "weather" -> {
                        db.weatherDao().insertAll(fromNetwork.map { it.toEntity() })
                        db.weatherDao().getAll().map { it.toModel() }
                    }

                    "general" -> {
                        db.generalDao().insertAll(fromNetwork.map { it.toGeneralEntity() })
                        db.generalDao().getAll().map { it.toModel() }
                    }

                    "starlink" -> {
                        db.starlinkDao().insertAll(fromNetwork.map { it.toStarlinkEntity() })
                        db.starlinkDao().getAll().map { it.toModel() }
                    }

                    "iss" -> {
                        db.issDao().insertAll(fromNetwork.map { it.toIssEntity() })
                        db.issDao().getAll().map { it.toModel() }
                    }

                    else -> emptyList()
                }
            } else {
                Log.d(
                    "SatelliteRepository",
                    "Data is being loaded for $satelliteType, returning empty list"
                )
                emptyList()
            }
        }

    private suspend fun fetchFromNetwork(satelliteType: String): List<SatelliteModel> =
        withContext(Dispatchers.IO) {
            val url = when (satelliteType) {
                "weather" -> "https://celestrak.org/NORAD/elements/gp.php?GROUP=weather&FORMAT=tle"
                "general" -> "https://celestrak.org/NORAD/elements/gp.php?GROUP=active&FORMAT=tle"
                "starlink" -> "https://celestrak.org/NORAD/elements/gp.php?GROUP=starlink&FORMAT=tle"
                "iss" -> "https://celestrak.org/NORAD/elements/gp.php?CATNR=25544&FORMAT=TLE"
                else -> "https://celestrak.org/NORAD/elements/gp.php?GROUP=active&FORMAT=tle"
            }
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute() // <-- synchronous call
            val body = response.body?.string()

            if (!response.isSuccessful || body == null) {
                return@withContext emptyList()
            }

            // Replace this with your actual parsing logic
            Log.d("satelliteBody", "fetchFromNetwork: $body")
            val allSatellites = parseTLEText(body)

            // Limit to 500 satellites for performance
            val limitedSatellites = if (allSatellites.size > 500) {
                Log.d(
                    "SatelliteDataManager",
                    "Limiting $satelliteType satellites from ${allSatellites.size} to 500"
                )
                allSatellites.take(500)
            } else {
                allSatellites
            }

            limitedSatellites
        }

    private fun parseTLEText(rawText: String): List<SatelliteModel> {
        try {
            val orekitDataDir = File(context.filesDir, "orekit-data")
            if (!orekitDataDir.exists() || orekitDataDir.list().isNullOrEmpty()) {
                copyOrekitDataFromAssets(context)
                Log.d("OrekitInit", "orekit-data copied from assets")
            }

            // Ensure Orekit data context is properly initialized
            val dataContext = DataContext.getDefault()
            val manager = dataContext.dataProvidersManager

            // Clear existing providers to avoid conflicts
            manager.clearProviders()

            // Add the directory crawler
            manager.addProvider(DirectoryCrawler(orekitDataDir))

            // Force initialization of time scales
            try {
                TimeScalesFactory.getUTC()
                Log.d("OrekitInit", "Time scales initialized successfully")
            } catch (e: Exception) {
                Log.e("OrekitInit", "Failed to initialize time scales", e)
                throw e
            }

            val lines = rawText.lines().filter { it.isNotBlank() }
            val result = mutableListOf<SatelliteModel>()

            for (i in lines.indices step 3) {
                if (i + 2 < lines.size) {
                    try {
                        val name = lines[i].trim()

                        val tle = org.orekit.propagation.analytical.tle.TLE(
                            lines[i + 1],
                            lines[i + 2]
                        )
                        val propagator = TLEPropagator.selectExtrapolator(tle)
                        val currentDate = AbsoluteDate(Date(), TimeScalesFactory.getUTC())
                        val pvCoordinates = propagator.getPVCoordinates(
                            currentDate,
                            FramesFactory.getITRF(IERSConventions.IERS_2010, false)
                        )
                        val position = pvCoordinates.position
                        Log.d("satelliteTrackerNew", "position ${position} ")

                        val r =
                            Math.sqrt(position.x * position.x + position.y * position.y + position.z * position.z)
                        val lat = Math.toDegrees(Math.asin(position.z / r))
                        val lon = Math.toDegrees(Math.atan2(position.y, position.x))

                        result.add(
                            SatelliteModel(
                                name = name,
                                latitude = lat,
                                longitude = lon,
                                azimuth = 0.0,
                                elevation = 0.0
                            )
                        )
                    } catch (e: Exception) {
                        Log.w(
                            "SatelliteRepository",
                            "Failed to parse satellite at line $i: ${e.message}"
                        )
                        // Continue with next satellite instead of failing completely
                        continue
                    }
                }
            }

            Log.d(
                "SatelliteRepository",
                "Successfully parsed ${result.size} satellites from TLE data"
            )
            return result

        } catch (e: Exception) {
            Log.e("SatelliteRepository", "Error parsing TLE text", e)
            throw e
        }
    }

    fun copyOrekitDataFromAssets(
        context: Context,
        assetPath: String = "orekit-data",
        outDir: File = File(context.filesDir, "orekit-data")
    ) {
        try {
            val assetManager = context.assets
            if (!outDir.exists()) {
                val created = outDir.mkdirs()
                if (!created) {
                    Log.e("OrekitInit", "Failed to create directory: ${outDir.absolutePath}")
                    return
                }
            }

            val files = assetManager.list(assetPath) ?: return

            for (filename in files) {
                val fullAssetPath = "$assetPath/$filename"
                val outFile = File(outDir, filename)

                val subFiles = assetManager.list(fullAssetPath)
                if (subFiles.isNullOrEmpty()) {
                    // It's a file
                    try {
                        assetManager.open(fullAssetPath).use { input ->
                            FileOutputStream(outFile).use { output ->
                                input.copyTo(output)
                            }
                        }
                        Log.d("OrekitInit", "Copied file: $filename")
                    } catch (e: Exception) {
                        Log.e("OrekitInit", "Failed to copy file: $filename", e)
                    }
                } else {
                    // It's a directory
                    copyOrekitDataFromAssets(context, fullAssetPath, outFile)
                }
            }

            // Verify critical files are copied
            val criticalFiles = listOf("tai-utc.dat", "itrf-versions.conf")
            for (file in criticalFiles) {
                val filePath = File(outDir, file)
                if (filePath.exists()) {
                    Log.d("OrekitInit", "Critical file exists: $file")
                } else {
                    Log.w("OrekitInit", "Critical file missing: $file")
                }
            }

        } catch (e: Exception) {
            Log.e("OrekitInit", "Error copying Orekit data from assets", e)
        }
    }

    private fun SatelliteModel.toEntity(): WeatherSatelliteEntity = WeatherSatelliteEntity(
        name, latitude, longitude, azimuth, elevation
    )

    private fun WeatherSatelliteEntity.toModel(): SatelliteModel = SatelliteModel(
        name, latitude, longitude, azimuth, elevation
    )

    private fun SatelliteModel.toGeneralEntity() = GeneralSatelliteEntity(
        name, latitude, longitude, azimuth, elevation
    )

    private fun GeneralSatelliteEntity.toModel() = SatelliteModel(
        name, latitude, longitude, azimuth, elevation
    )

    private fun SatelliteModel.toStarlinkEntity() = StarlinkSatelliteEntity(
        name, latitude, longitude, azimuth, elevation
    )

    private fun StarlinkSatelliteEntity.toModel() = SatelliteModel(
        name, latitude, longitude, azimuth, elevation
    )

    private fun SatelliteModel.toIssEntity() = IssSatelliteEntity(
        name, latitude, longitude, azimuth, elevation
    )

    private fun IssSatelliteEntity.toModel() = SatelliteModel(
        name, latitude, longitude, azimuth, elevation
    )
}