package com.example.satellitefinder.ui.activites

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.satellitefinder.R
import com.example.satellitefinder.models.TLEEntry

import com.example.satellitefinder.ui.adapters.TLEAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.Date

class TLESatelliteActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tlesatellite)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fetchAllTLEs { tleList ->
            runOnUiThread {
                val adapter = TLEAdapter(tleList) { tle ->
//                    plotSatelliteOnMap(this, googleMap, tle)
                }
                findViewById<RecyclerView>(R.id.rvList).adapter = adapter
            }
        }
    }

    private fun fetchTLE(onResult: (String, String) -> Unit) {
        val url = "https://celestrak.org/NORAD/elements/stations.txt"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("TLE", "Failed to fetch", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val lines = response.body?.string()?.lines()
                lines?.let {
                    for (i in lines.indices step 3) {
                        if (lines[i].contains("ISS") || lines[i].contains("ZARYA")) {
                            val line1 = lines[i + 1].trim()
                            val line2 = lines[i + 2].trim()
                            onResult(line1, line2)
                            break
                        }
                    }
                }
            }
        })
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

    }

    fun parseTLEText(rawText: String): List<TLEEntry> {
        val lines = rawText.lines().filter { it.isNotBlank() }
        val tleList = mutableListOf<TLEEntry>()

        for (i in lines.indices step 3) {
            if (i + 2 < lines.size) {
                val name = lines[i].trim()
                val line1 = lines[i + 1].trim()
                val line2 = lines[i + 2].trim()
                tleList.add(TLEEntry(name, line1, line2))
            }
        }

        return tleList
    }

    fun fetchAllTLEs(onResult: (List<TLEEntry>) -> Unit) {
        val url = "https://celestrak.org/NORAD/elements/stations.txt"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("TLE", "Failed to fetch", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val tleList = body?.let { parseTLEText(it) }
                if (tleList != null) {
                    onResult(tleList)
                }
            }
        })
    }

   /* fun plotSatelliteOnMap(context: Context, googleMap: GoogleMap, tleEntry: TLEEntry) {
        try {
            val tle = TLE(arrayOf(tleEntry.name, tleEntry.line1, tleEntry.line2))
            val satellite = SatelliteFactory.createSatellite(tle)

            val now = Date()
            satellite.calculateSatelliteVectors(now)
            val pos = satellite.calculateSatelliteGroundTrack()

            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(pos.latitude, pos.longitude))
                    .title(tleEntry.name)
            )
            marker?.showInfoWindow()
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        pos.latitude,
                        pos.longitude
                    ), 4f
                )
            )

        } catch (e: Exception) {
            Log.e("Map", "Failed to show satellite", e)
        }
    }*/
}