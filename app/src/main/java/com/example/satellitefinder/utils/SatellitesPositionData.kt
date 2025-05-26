package com.example.satellitefinder.utils

class SatellitesPositionData(satelliteData: SatellitesInformationData) :java.io.Serializable {

    private  val TAG = "SatellitePosition"

    private var mSatelliteDirection: String? = null
    private var mSatellitesInformationData: SatellitesInformationData? = satelliteData
    private var mSatelliteLongitude: Double? = null
    private var mSatelliteLatitude: Double? = null
    var mSatelliteName = ""


    fun getSatelliteDirection(): String? {
        return mSatelliteDirection
    }

    override fun toString(): String {
        return mSatelliteName
    }

    fun getSatLongitude(): Double? {
        return mSatelliteLongitude
    }

    fun getSatLatitude(): Double? {
        return mSatelliteLatitude
    }

    fun getSatellite(): SatellitesInformationData? {
        return mSatellitesInformationData
    }

    init {
        mSatelliteLongitude = satelliteData.getsatelliteLongitude()
        mSatelliteLatitude = satelliteData.satelliteLatitude
        mSatelliteDirection = satelliteData.satelliteDirection
    }

    fun getSatelliteAzimut(): Int {
        var azimuth = 0.0
        var beta = 0.0
        val currentLat = currentLocation?.let { Math.toRadians(it.getLatitude()) }
        val satelliteLong = Math.toRadians(mSatelliteLongitude!!)
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
        return Math.toDegrees(azimuth).toInt()
    }

    fun getSatelliteElevation(): Double? {
        var elev = 0.0
        val currentLat = currentLocation?.let { Math.toRadians(it.latitude) }
        val satelliteLong = Math.toRadians(mSatelliteLongitude!!)
       val currentLong = currentLocation?.let { Math.toRadians(it.getLongitude()) }?: kotlin.run {
            0.0
       }


        val deltaLon = satelliteLong - currentLong
        currentLat?.let { cLat ->
            elev = Math.atan(
                (Math.cos(deltaLon) * Math.cos(cLat) - 0.1512f) / Math.sqrt(
                    1 - Math.pow(
                        Math.cos(deltaLon), 2.0
                    ) * Math.pow(Math.cos(cLat), 2.0)
                )
            )
        } ?: kotlin.run {
            elev = 0.0
        }

        return Math.toDegrees(elev)
    }

    fun getLNBSkew(): Double {
        val currentLat: Double = currentLocation!!.getLatitude()
        val currentLong: Double = currentLocation!!.getLongitude()
        val longdiff = (currentLong - mSatelliteLongitude!!) / 57.29578
        val ltr = Math.toRadians(currentLat)
        val skew = Math.atan(Math.sin(longdiff) / Math.tan(ltr))
        return Math.toDegrees(skew)
    }

}