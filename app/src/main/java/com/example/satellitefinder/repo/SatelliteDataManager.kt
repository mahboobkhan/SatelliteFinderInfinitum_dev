package com.example.satellitefinder.repo

import android.content.Context
import android.util.Log
import com.example.satellitefinder.database.SatelliteDatabase
import com.example.satellitefinder.models.SatelliteModel
import com.example.satellitefinder.utils.isInternetConnected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.context.GlobalContext

class SatelliteDataManager(
    private val context: Context,
    private val database: SatelliteDatabase
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val satelliteTypes = listOf("weather", "general", "starlink", "iss")

    // Track loading state for each satellite type
    private val loadingStates = mutableMapOf<String, Boolean>()

    // Callback for when data is loaded
    var onDataLoaded: ((String) -> Unit)? = null

    // Callback for when data loading fails
    var onDataLoadError: ((String, String) -> Unit)? = null

    // Static TLE data for immediate access (not saved to database)
    private val staticTleData = mapOf(
        "weather" to """NOAA 15                 
1 25338U 98030A   25179.18231934  .00000158  00000+0  82777-4 0  9994
2 25338  98.5341 203.8650 0009204 309.4291  50.6074 14.26988837411059
DMSP 5D-3 F16 (USA 172) 
1 28054U 03048A   25179.18801111 -.00000016  00000+0  15298-4 0  9995
2 28054  99.0120 195.1910 0008316  72.0590   7.4354 14.14358611119423
NOAA 18                 
1 28654U 05018A   25179.16651588  .00000065  00000+0  57696-4 0  9990
2 28654  98.8424 258.4339 0015307  78.3746 281.9143 14.13615259 36371
METEOSAT-9 (MSG-2)      
1 28912U 05049B   25178.79613794  .00000114  00000+0  00000+0 0  9993
2 28912   8.5522  57.5579 0002133   1.1093 189.8361  1.00276859  3453
EWS-G1 (GOES 13)        
1 29155U 06018A   25178.60536166 -.00000265  00000+0  00000+0 0  9995
2 29155   3.6100  80.7798 0060421 267.0622 290.1667  0.98797882 39743
DMSP 5D-3 F17 (USA 191) 
1 29522U 06050A   25179.17016284 -.00000008  00000+0  18526-4 0  9998
2 29522  98.7382 188.0021 0009958 353.9151   6.1900 14.14838092962358
FENGYUN 3A              
1 32958U 08026A   25179.13507862  .00000061  00000+0  49449-4 0  9999
2 32958  98.6484 122.4308 0009810  70.4174 289.8061 14.19450269885285
NOAA 19                 
1 33591U 09005A   25179.17597538  .00000154  00000+0  10625-3 0  9990
2 33591  98.9966 243.9844 0012859 314.3569  45.6549 14.13382343844731
GOES 14                 
1 35491U 09033A   25178.61959669 -.00000075  00000+0  00000+0 0  9992
2 35491   0.7635  87.8462 0002313  68.8754 234.0812  1.00273529  3032
DMSP 5D-3 F18 (USA 210) 
1 35951U 09057A   25178.89375438  .00000196  00000+0  12473-3 0  9999
2 35951  98.8599 156.3413 0011124 172.0487 188.0864 14.14642902809492
EWS-G2 (GOES 15)        
1 36411U 10008A   25178.95283656  .00000032  00000+0  00000+0 0  9994
2 36411   0.3625  85.3546 0002697 113.6767 121.5442  1.00271296 56085""",
        
        "general" to """CALSPHERE 1             
1 00900U 64063C   25178.96411517  .00000469  00000+0  47249-3 0  9992
2 00900  90.2158  63.6397 0023470 288.6373  82.4695 13.76096604 22700
CALSPHERE 2             
1 00902U 64063E   25178.97288610  .00000057  00000+0  73070-4 0  9996
2 00902  90.2310  67.4950 0018695 169.6224 201.9098 13.52865113808378
LCS 1                   
1 01361U 65034C   25178.73941466  .00000016  00000+0  10142-2 0  9998
2 01361  32.1438 298.6161 0012924 306.7661  53.1650  9.89308688174780
TEMPSAT 1               
1 01512U 65065E   25178.86919399  .00000032  00000+0  45172-4 0  9995
2 01512  89.9755 212.9148 0070301 162.0931  11.9262 13.33566655912820
CALSPHERE 4A            
1 01520U 65065H   25178.88849609  .00000084  00000+0  14378-3 0  9992
2 01520  89.9225 125.6267 0070861  25.1066  35.0215 13.36202101915398
OPS 5712 (P/L 160)      
1 02826U 67053A   25178.95362068  .00004232  00000+0  70379-3 0  9990
2 02826  69.9178 181.8097 0004303 223.0293 137.0515 14.70781380 14023
LES-5                   
1 02866U 67066E   25179.16514581 -.00000094  00000+0  00000+0 0  9994
2 02866   1.8178 109.4605 0052094 193.3925 251.2009  1.09425862127158
SURCAL 159              
1 02872U 67053F   25178.87941459  .00000046  00000+0  68449-4 0  9994
2 02872  69.9747  43.7006 0008344 231.0148 129.0217 13.99480181960631
OPS 5712 (P/L 153)      
1 02874U 67053H   25178.93477467  .00000001  00000+0  39107-4 0  9996
2 02874  69.9724 154.5719 0009850 289.4615  70.5427 13.96753214957310
SURCAL 150B             
1 02909U 67053J   25178.87772422  .00047077  00000+0  17770-2 0  9991
2 02909  69.9220  76.8370 0011353 199.4582 160.6160 15.26724599 27382
OPS 3811 (DSP 2)        
1 05204U 71039A   25179.23354812 -.00000047  00000+0  00000+0 0  9993
2 05204   0.6909 289.9258 0020387 355.2866 296.5819  0.98160443 21801
RIGIDSPHERE 2 (LCS 4)   
1 05398U 71067E   25178.96165069  .00000488  00000+0  15481-3 0  9992
2 05398  87.6183  77.1643 0059410 329.9251  29.8540 14.37217328822994
OSCAR 7 (AO-7)          
1 07530U 74089B   25178.92074625 -.00000043  00000+0  22495-4 0  9994
2 07530 101.9927 183.3801 0012658  87.3237 339.0794 12.53689854316121""",
        
        "starlink" to """STARLINK-1008           
1 44714U 19074B   25178.93281733  .00000617  00000+0  60275-4 0  9997
2 44714  53.0542 250.2638 0000890 125.1771 234.9301 15.06400804310350
STARLINK-1010           
1 44716U 19074D   25178.85785822  .00067543  00000+0  11547-2 0  9995
2 44716  53.0530 235.6232 0008023 321.4997  38.5438 15.50648312310539
STARLINK-1011           
1 44717U 19074E   25178.67297506 -.00000797  00000+0 -34626-4 0  9998
2 44717  53.0553 271.4329 0000947  83.2636 276.8460 15.06391660310039
STARLINK-1012           
1 44718U 19074F   25179.24245168  .00000411  00000+0  46496-4 0  9999
2 44718  53.0593 248.8788 0001621  87.2044 272.9130 15.06393196310563
STARLINK-1013           
1 44719U 19074G   25179.25002315  .00092795  00000+0  55291-3 0  9994
2 44719  53.0538 226.9068 0009442 329.0698 314.8785 15.77149197310876
STARLINK-1014           
1 44720U 19074H   25179.25002315  .00350563  58099-4  10074-2 0  9991
2 44720  53.0269 175.8200 0006087  53.2020 164.1262 15.92602938311596
STARLINK-1015           
1 44721U 19074J   25178.57413405  .00093716  00000+0  92067-3 0  9993
2 44721  53.0545 233.2350 0009461 322.7713  37.2644 15.65242930310552
STARLINK-1017           
1 44723U 19074L   25178.93723305  .00015067  00000+0  10084-2 0  9999
2 44723  53.0532 250.1786 0000963   6.7810 353.3192 15.07124404310355
STARLINK-1019           
1 44724U 19074M   25178.50893453  .00000872  00000+0  77413-4 0  9994
2 44724  53.0552 252.1685 0001470  72.9959 287.1191 15.06405745310285
STARLINK-1020           
1 44725U 19074N   25179.24428995  .00000823  00000+0  74109-4 0  9997
2 44725  53.0547 268.8692 0000851 114.9304 245.1773 15.06399926310288""",
        
        "iss" to """ISS (ZARYA)             
1 25544U 98067A   25179.15776912  .00006109  00000+0  11381-3 0  9994
2 25544  51.6349 252.7052 0002263 302.4301  57.6469 15.50279857516861"""
    )

    init {
        // Initialize loading states
        satelliteTypes.forEach { type ->
            loadingStates[type] = false
        }
    }

    fun preloadAllSatelliteData() {
        Log.d("SatelliteDataManager", "Starting preload of all satellite data")

        // Check internet connectivity first
        if (!isInternetConnected(context)) {
            Log.w("SatelliteDataManager", "No internet connection available for preloading")
            satelliteTypes.forEach { type ->
                onDataLoadError?.invoke(type, "No internet connection")
            }
            return
        }

        satelliteTypes.forEach { satelliteType ->
            if (!isDataLoaded(satelliteType)) {
                scope.launch {
                    loadSatelliteData(satelliteType)
                }
            }
        }
    }

     suspend fun loadSatelliteData(satelliteType: String) {
        if (loadingStates[satelliteType] == true) {
            Log.d("SatelliteDataManager", "Already loading $satelliteType data")
            return
        }

        loadingStates[satelliteType] = true
        Log.d("SatelliteDataManager", "Loading $satelliteType satellite data")

        try {
            // Check internet connectivity before network request
            if (!isInternetConnected(context)) {
                Log.w("SatelliteDataManager", "No internet connection for $satelliteType")
                loadingStates[satelliteType] = false
                onDataLoadError?.invoke(satelliteType, "No internet connection")
                return
            }

            val satellites = fetchFromNetwork(satelliteType)

            withContext(Dispatchers.IO) {
                when (satelliteType) {
                    "weather" -> {
                        database.weatherDao().deleteAll()
                        database.weatherDao().insertAll(satellites.map { it.toWeatherEntity() })
                    }
                    "general" -> {
                        database.generalDao().deleteAll()
                        database.generalDao().insertAll(satellites.map { it.toGeneralEntity() })
                    }
                    "starlink" -> {
                        database.starlinkDao().deleteAll()
                        database.starlinkDao().insertAll(satellites.map { it.toStarlinkEntity() })
                    }
                    "iss" -> {
                        database.issDao().deleteAll()
                        database.issDao().insertAll(satellites.map { it.toIssEntity() })
                    }
                }
            }

            loadingStates[satelliteType] = false
            Log.d("SatelliteDataManager", "Successfully loaded $satelliteType data: ${satellites.size} satellites")
            onDataLoaded?.invoke(satelliteType)

        } catch (e: Exception) {
            loadingStates[satelliteType] = false
            Log.e("SatelliteDataManager", "Error loading $satelliteType data", e)
            onDataLoadError?.invoke(satelliteType, e.message ?: "Unknown error")
        }
    }

    // Get static data for immediate display (not saved to database)
    fun getStaticData(satelliteType: String): List<SatelliteModel> {
        return try {
            val staticTle = staticTleData[satelliteType] ?: return emptyList()
            val satellites = parseTLEText(staticTle)
            
            Log.d("SatelliteDataManager", "Returned static data for $satelliteType: ${satellites.size} satellites")
            satellites
        } catch (e: Exception) {
            Log.e("SatelliteDataManager", "Error parsing static data for $satelliteType", e)
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

            val client = okhttp3.OkHttpClient()
            val request = okhttp3.Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val body = response.body?.string()

            if (!response.isSuccessful || body == null) {
                return@withContext emptyList()
            }

            Log.d("SatelliteDataManager", "Fetched $satelliteType data from network")
            val allSatellites = parseTLEText(body)
            
            // Limit to 500 satellites for performance
            val limitedSatellites = if (allSatellites.size > 500) {
                Log.d("SatelliteDataManager", "Limiting $satelliteType satellites from ${allSatellites.size} to 500")
                allSatellites.take(500)
            } else {
                allSatellites
            }
            
            limitedSatellites
        }

    private fun parseTLEText(rawText: String): List<SatelliteModel> {
        val orekitDataDir = java.io.File(context.filesDir, "orekit-data")
        if (!orekitDataDir.exists() || orekitDataDir.list().isNullOrEmpty()) {
            copyOrekitDataFromAssets(context)
            Log.d("OrekitInit", "orekit-data copied from assets")
        }

        val manager = org.orekit.data.DataContext.getDefault().dataProvidersManager
        manager.addProvider(org.orekit.data.DirectoryCrawler(orekitDataDir))

        val lines = rawText.lines().filter { it.isNotBlank() }
        val result = mutableListOf<SatelliteModel>()

        for (i in lines.indices step 3) {
            if (i + 2 < lines.size) {
                val name = lines[i].trim()

                val tle = org.orekit.propagation.analytical.tle.TLE(
                    lines[i + 1],
                    lines[i + 2]
                )
                val propagator = org.orekit.propagation.analytical.tle.TLEPropagator.selectExtrapolator(tle)
                val currentDate = org.orekit.time.AbsoluteDate(java.util.Date(), org.orekit.time.TimeScalesFactory.getUTC())
                val pvCoordinates = propagator.getPVCoordinates(
                    currentDate,
                    org.orekit.frames.FramesFactory.getITRF(org.orekit.utils.IERSConventions.IERS_2010, false)
                )
                val position = pvCoordinates.position

                val r = Math.sqrt(position.x * position.x + position.y * position.y + position.z * position.z)
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

    private fun copyOrekitDataFromAssets(
        context: Context,
        assetPath: String = "orekit-data",
        outDir: java.io.File = java.io.File(context.filesDir, "orekit-data")
    ) {
        val assetManager = context.assets
        if (!outDir.exists()) outDir.mkdirs()

        val files = assetManager.list(assetPath) ?: return

        for (filename in files) {
            val fullAssetPath = "$assetPath/$filename"
            val outFile = java.io.File(outDir, filename)

            val subFiles = assetManager.list(fullAssetPath)
            if (subFiles.isNullOrEmpty()) {
                // It's a file
                assetManager.open(fullAssetPath).use { input ->
                    java.io.FileOutputStream(outFile).use { output ->
                        input.copyTo(output)
                    }
                }
            } else {
                // It's a directory
                copyOrekitDataFromAssets(context, fullAssetPath, outFile)
            }
        }
    }

    fun isDataLoaded(satelliteType: String): Boolean {
        return when (satelliteType) {
            "weather" -> database.weatherDao().getAll().isNotEmpty()
            "general" -> database.generalDao().getAll().isNotEmpty()
            "starlink" -> database.starlinkDao().getAll().isNotEmpty()
            "iss" -> database.issDao().getAll().isNotEmpty()
            else -> false
        }
    }

    fun isLoading(satelliteType: String): Boolean {
        return loadingStates[satelliteType] ?: false
    }

    // Extension functions for entity conversion
    private fun SatelliteModel.toWeatherEntity() = com.example.satellitefinder.database.WeatherSatelliteEntity(
        name, latitude, longitude, azimuth, elevation
    )

    private fun SatelliteModel.toGeneralEntity() = com.example.satellitefinder.database.GeneralSatelliteEntity(
        name, latitude, longitude, azimuth, elevation
    )

    private fun SatelliteModel.toStarlinkEntity() = com.example.satellitefinder.database.StarlinkSatelliteEntity(
        name, latitude, longitude, azimuth, elevation
    )

    private fun SatelliteModel.toIssEntity() = com.example.satellitefinder.database.IssSatelliteEntity(
        name, latitude, longitude, azimuth, elevation
    )
}