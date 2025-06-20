package com.example.satellitefinder.repo

import android.content.Context
import android.util.Log
import com.example.satellitefinder.database.GeneralSatelliteEntity
import com.example.satellitefinder.database.IssSatelliteEntity
import com.example.satellitefinder.database.SatelliteDatabase
import com.example.satellitefinder.database.StarlinkSatelliteEntity
import com.example.satellitefinder.database.WeatherSatelliteEntity
import com.example.satellitefinder.models.SatelliteModel
import kotlinx.coroutines.Dispatchers
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

class SatelliteRepositoryImpl(private val db: SatelliteDatabase, private val context: Context) :
    SatelliteRepository {

    override suspend fun fetchTLESatellites(satelliteType: String): List<SatelliteModel> =
        withContext(Dispatchers.IO) {
            when (satelliteType) {
                "weather" -> db.weatherDao().getAll().ifEmpty {
                    val fromNetwork = fetchFromNetwork("weather")
                    db.weatherDao().insertAll(fromNetwork.map { it.toEntity() })
                    Log.d("databaseseather", "fetchTLESatellites: jfkjdfak")
                    db.weatherDao().getAll()
                }.map { it.toModel() }

                "general" -> db.generalDao().getAll().ifEmpty {
                    val fromNetwork = fetchFromNetwork("general")
                    db.generalDao().insertAll(fromNetwork.map { it.toGeneralEntity() })
                    db.generalDao().getAll()
                }.map { it.toModel() }

                "starlink" -> db.starlinkDao().getAll().ifEmpty {
                    val fromNetwork = fetchFromNetwork("starlink")
                    db.starlinkDao().insertAll(fromNetwork.map { it.toStarlinkEntity() })
                    db.starlinkDao().getAll()
                }.map { it.toModel() }

                "iss" -> db.issDao().getAll().ifEmpty {

                    val fromNetwork = fetchFromNetwork("iss")
                    db.issDao().insertAll(fromNetwork.map { it.toIssEntity() })
                    db.issDao().getAll()
                }.map { it.toModel() }

                else -> emptyList()
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
            parseTLEText(body)
        }

    private fun parseTLEText(rawText: String): List<SatelliteModel> {
        val orekitDataDir = File(context.filesDir, "orekit-data")
        if (!orekitDataDir.exists() || orekitDataDir.list().isNullOrEmpty()) {
            copyOrekitDataFromAssets(context)
            Log.d("OrekitInit", "orekit-data copied from assets")
        }
        val manager = DataContext.getDefault().dataProvidersManager
        manager.addProvider(DirectoryCrawler(orekitDataDir))
        val lines = rawText.lines().filter { it.isNotBlank() }
        val result = mutableListOf<SatelliteModel>()
        for (i in lines.indices step 3) {
            if (i + 2 < lines.size) {
                val name = lines[i].trim()
                /* val tle = TLE(arrayOf(name, lines[i + 1], lines[i + 2]))
                 val satellite = SatelliteFactory.createSatellite(tle)
                 satellite.calculateSatelliteVectors(Date())
                 val pos = satellite.calculateSatelliteGroundTrack()*/


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
            }
        }
        return result
    }

    fun copyOrekitDataFromAssets(
        context: Context,
        assetPath: String = "orekit-data",
        outDir: File = File(context.filesDir, "orekit-data")
    ) {
        val assetManager = context.assets
        if (!outDir.exists()) outDir.mkdirs()

        val files = assetManager.list(assetPath) ?: return

        for (filename in files) {
            val fullAssetPath = "$assetPath/$filename"
            val outFile = File(outDir, filename)

            val subFiles = assetManager.list(fullAssetPath)
            if (subFiles.isNullOrEmpty()) {
                // It's a file
                assetManager.open(fullAssetPath).use { input ->
                    FileOutputStream(outFile).use { output ->
                        input.copyTo(output)
                    }
                }
            } else {
                // It's a directory
                copyOrekitDataFromAssets(context, fullAssetPath, outFile)
            }
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