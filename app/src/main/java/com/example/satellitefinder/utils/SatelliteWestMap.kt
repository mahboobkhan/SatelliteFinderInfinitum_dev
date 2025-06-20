package com.example.satellitefinder.utils

object SatelliteWestMap {
    private var cachedMap: Map<Double, SatelliteInfo>? = null
    private var cachedLat: Double? = null
    private var cachedLon: Double? = null

    data class SatelliteInfo(
        val name: String,
        val longitude: Double,
        val isActive: Boolean,
        var isSelected: Boolean = false
    )

    // Placeholder for user selection list
    private fun getUserSelectedLongitudes(): List<Double> {
        // TODO: Implement user selection retrieval
        return emptyList()
    }

    // Placeholder for visibility check
    private fun isSatelliteVisible(latitude: Double, longitude: Double, satLon: Double): Boolean {
        // A satellite is visible if its elevation is above the horizon (>= 0).
        return SatelliteCalculator.calculate(latitude, longitude, satLon).elevation >= 0.0
    }

    fun getMap(latitude: Double, longitude: Double): Map<Double, SatelliteInfo> {
        if (cachedMap != null && cachedLat == latitude && cachedLon == longitude) {
            return cachedMap!!
        }
        cachedLat = latitude
        cachedLon = longitude
        val map = mutableMapOf<Double, SatelliteInfo>()
        fun add(name: String, lon: Double, isActive: Boolean) {
            map[lon] = SatelliteInfo(name, lon, isActive)
        }
        add("NSS 9", -177.0, true)
        add("AMC 8", -139.0, true)
        add("AMC 7", -137.0, true)
        add("AMC 10", -135.0, true)
        add("Galaxy 15", -133.0, true)
        add("AMC 11", -131.0, false)
        add("Ciel 2/Galaxy 12", -129.0, true)
        add("Galaxy 13/Horizons 1", -127.0, false)
        add("AMC 21/Galaxy 14", -125.0, true)
        add("Galaxy 18", -123.0, false)
        add("EchoStar 9/Galaxy 23", -121.0, false)
        add("Anik F3/DirecTV 7S/EchoStar 14", -119.0, true)
        add("SatMex 5", -116.8, false)
        add("SatMex 6", -113.0, false)
        add("Anik F2", -111.1, false)
        add("DirecTV 5/EchoStar 10/11", -110.0, true)
        add("Anik F1/F1R", -107.3, true)
        add("AMC 15/18", -105.0, false)
        add("AMC 1/SES 3/Spaceway 1 & DirecTV 10/12", -103.0, false)
        add("DirecTV 4S/8/SES 1", -101.0, true)
        add("Galaxy 16/Spaceway 2 & DirecTV 11", -99.2, false)
        add("Galaxy 19", -97.0, true)
        add("Galaxy 3C/Spaceway 3", -95.0, false)
        add("Galaxy 25", -93.1, false)
        add("Galaxy 17/Nimiq 6", -91.0, true)
        add("Galaxy 28", -89.0, true)
        add("SES 2", -87.0, false)
        add("AMC 16", -85.0, false)
        add("Brasilsat B4", -84.0, true)
        add("AMC 9", -83.0, false)
        add("Nimiq 4", -82.0, false)
        add("Simón Bolívar", -78.0, false)
        add("EchoStar 1/8/QuetzSat 1", -77.0, true)
        add("Nimiq 5", -72.7, false)
        add("AMC 6", -72.0, true)
        add("Star One C2", -70.0, true)
        add("Star One C1", -65.0, true)
        add("Telstar 14R", -63.0, true)
        add("EchoStar 12/15/16", -61.5, false)
        add("Amazonas 1/2", -61.0, true)
        add("Intelsat 21", -58.0, false)
        add("Galaxy 11/Intelsat 805", -55.5, true)
        add("Intelsat 23", -53.0, false)
        add("Intelsat 23", -51.5, true)
        add("Intelsat 21", -51.3, false)
        add("Intelsat 1R", -50.0, true)
        add("Intelsat 14", -45.0, false)
        add("Intelsat 9/11", -43.1, true)
        add("NSS 806", -40.5, true)
        add("NSS 10/Telstar 11N", -37.5, true)
        add("Intelsat 903", -34.5, true)
        add("Intelsat 25", -31.5, false)
        add("Hispasat 1C/1D/1E", -30.0, true)
        add("Intelsat 907", -27.5, true)
        add("Intelsat 905", -24.5, true)
        add("SES 4", -22.0, false)
        add("NSS 7", -20.0, true)
        add("Intelsat 901", -18.0, true)
        add("Telstar 12", -15.0, true)
        add("Express A4", -14.0, false)
        add("Eutelsat 12 West A", -12.5, true)
        add("Express AM44", -11.0, false)
        add("Eutelsat 8 West A", -8.0, false)
        add("Eutelsat 7 West A/Nilesat 102/201", -7.0, true)
        add("Eutelsat 5 West A", -5.0, false)
        add("Amos 2/3", -4.0, true)
        add("Thor 5/6/Intelsat 10-02", -0.8, true)
        add("Eutelsat 3A/3C/Rascom QAF 1R", 3.0, false)
        add("Eutelsat 3A/3C/Rascom QAF 1R", 3.1, false)
        add("Eutelsat 3A", 3.3, true)
        add("Astra 4A/SES 5", 4.9, false)
        add("SES 5", 5.0, false)
        add("Eutelsat 7A", 7.0, true)
        add("Eutelsat 9A/Ka-Sat 9A", 9.0, false)
        add("Eutelsat 10A", 10.0, true)
        add("Eutelsat Hot Bird 13A/13B/13C", 13.0, true)
        add("Eutelsat 16A/25A", 16.0, true)
        add("Amos 5", 17.0, false)
        add("Astra 1KR/1L/1M/2C", 19.2, true)
        add("Arabsat 5C", 20.0, false)
        add("Eutelsat 21B", 21.5, false)
        add("Astra 1D", 23.1, false)
        add("Astra 1D/3B", 23.3, true)
        add("Eutelsat 25C", 25.5, false)
        add("Badr 4/5/6", 26.0, true)
        add("Astra 1N/2A/2F/Eutelsat 28A", 28.2, true)
        add("Arabsat 5A", 30.5, false)
        add("Astra 1G", 31.5, false)
        add("Eutelsat 33A/Intelsat 28", 33.0, true)
        add("Eutelsat 36A/36B", 36.0, true)
        add("Paksat 1R", 38.0, false)
        add("Hellas Sat 2", 39.0, true)
        add("Türksat 2A/3A", 42.0, true)
        add("Astra 2F", 43.5, false)
        add("Intelsat 12", 45.0, true)
        add("Intelsat 10", 47.5, false)
        add("Eutelsat 48C", 48.0, false)
        add("Yamal 202", 49.0, false)
        add("Intelsat 26", 50.0, true)
        add("NSS 5", 50.5, false)
        add("Eutelsat 70B", 51.0, false)
        add("ChinaSat 12", 51.5, false)
        add("Yahsat 1A", 52.5, false)
        add("Express AM22", 53.0, true)
        add("Astra 1F/G-Sat 8/Insat 3E/Yamal 402", 54.9, false)
        add("DirecTV 1R", 55.8, false)
        add("Bonum 1/DirecTV 1R", 55.9, true)
        add("NSS 12", 57.0, false)
        add("Intelsat 904", 60.0, true)
        add("Intelsat 902", 62.0, true)
        add("Intelsat 20", 63.0, false)
        add("Intelsat 906", 64.2, false)
        add("Intelsat 17", 66.0, true)
        add("Intelsat 20", 68.5, false)
        add("Eutelsat 70B", 70.5, false)
        add("Intelsat 22", 72.1, true)
        add("Insat 3C/4CR", 74.0, false)
        add("ABS 1", 75.0, true)
        add("Apstar 7", 76.5, false)
        add("Thaicom 5", 78.5, true)
        add("Express AM2/MD1", 80.0, false)
        add("ChinaSat 12", 81.5, false)
        add("G-Sat 10/12/Insat 4A", 83.0, true)
        add("Horizons 2/Intelsat 15", 85.0, false)
        add("KazSat 2", 86.5, true)
        add("ChinaSat 5A", 87.5, false)
        add("ST 2", 88.0, false)
        add("Yamal 201/300K", 90.0, true)
        add("Measat 3/3a", 91.5, false)
        add("ChinaSat 9", 92.2, false)
        add("Insat 3A/4B", 93.5, false)
        add("NSS 6", 95.0, true)
        add("Express AM33", 96.5, false)
        add("ChinaSat 11", 98.0, true)
        add("AsiaSat 5", 100.5, true)
        add("Express A2", 103.0, false)
        add("AsiaSat 3S/Intelsat 5", 105.5, true)
        add("Astra 1E/NSS 11/SES 7/Telkom 1", 108.2, false)
        add("BSAT 2C/3A/3C/JCSAT 110R/N-Sat 110", 110.0, false)
        add("ChinaSat 10", 110.5, true)
        add("JCSAT 4A/Koreasat 5/Palapa D", 113.0, false)
        add("ChinaSat 6B", 115.5, true)
        add("ABS 7/Koreasat 6", 116.0, false)
        add("Telkom 2", 118.0, true)
        add("AsiaSat 4", 122.2, true)
        add("JCSAT 4B", 124.0, false)
        add("ChinaSat 6A", 125.0, true)
        add("JCSAT 3A", 128.0, true)
        add("JCSAT 5A/Vinasat 1", 132.0, true)
        add("Apstar 6", 134.0, true)
        add("Telstar 18", 138.0, true)
        add("Express AM3", 140.0, true)
        add("Superbird C2", 144.0, true)
        add("JCSAT 1B", 150.0, true)
        add("Optus D2", 152.0, true)
        add("JCSAT 2A", 154.0, false)
        add("Optus C1/D3", 156.0, true)
        add("Intelsat 706", 157.0, false)
        add("Optus D1", 160.0, true)
        add("Superbird B2", 162.0, false)
        add("Optus B3", 164.0, true)
        add("Intelsat 19", 166.0, true)
        add("Intelsat 8/701", 169.0, false)
        add("Intelsat 19", 170.0, true)
        add("Eutelsat 172A", 172.0, true)
        add("Intelsat 19", 176.0, true)
        add("Intelsat 18", 180.0, true)

        // User selection list (placeholder)
        val userSelected = getUserSelectedLongitudes()
        val iterator = map.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val satLon = entry.key
            val satInfo = entry.value
            if (!isSatelliteVisible(latitude, longitude, satLon)) {
                iterator.remove()
            } else if (userSelected.isNotEmpty()) {
                satInfo.isSelected = userSelected.contains(satLon)
            }
        }
        cachedMap = map
        return map
    }
} 