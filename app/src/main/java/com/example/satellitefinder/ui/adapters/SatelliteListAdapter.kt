package com.example.satellitefinder.ui.adapters

import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.satellitefinder.databinding.ItemSatelliteListBinding
import com.example.satellitefinder.models.SatelliteModel

class SatelliteListAdapter(val onClick: (SatelliteModel) -> Unit) :
    ListAdapter<SatelliteModel, SatelliteListAdapter.SatelliteViewHolder>(DiffCallback()) {
    var currentLocation: Location? = null


    class SatelliteViewHolder(val binding: ItemSatelliteListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SatelliteViewHolder {
        val binding =
            ItemSatelliteListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SatelliteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SatelliteViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            satTitle.text = item.name

            Log.d(
                "Weather",
                "Name: ${item.name} Latitude: ${item.latitude} Longitude: ${item.longitude}"
            )
            satAzimut.text = "Azimuth: %.2f°".format(getSatelliteAzimut(item.longitude))
            satElevation.text = "Elevation: %.2f°".format(getSatelliteElevation(item.longitude))
            root.setOnClickListener {
                onClick(
                    SatelliteModel(
                        item.name,
                        item.latitude,
                        item.longitude,
                        getSatelliteAzimut(item.longitude),
                        getSatelliteElevation(item.longitude) ?: 0.0,
                    )
                )
            }
        }
    }

    fun getSatelliteAzimut(mSatelliteLongitude: Double): Double {
        var azimuth = 0.0
        var beta = 0.0
        val currentLat = currentLocation?.let { Math.toRadians(it.getLatitude()) }
        val satelliteLong = Math.toRadians(mSatelliteLongitude)
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
        return Math.toDegrees(azimuth)
    }

    fun getSatelliteElevation(mSatelliteLongitude: Double): Double? {
        var elev = 0.0
        val currentLat = currentLocation?.let { Math.toRadians(it.latitude) }
        val satelliteLong = Math.toRadians(mSatelliteLongitude!!)
        val currentLong = currentLocation?.let { Math.toRadians(it.getLongitude()) } ?: kotlin.run {
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

    class DiffCallback : DiffUtil.ItemCallback<SatelliteModel>() {
        override fun areItemsTheSame(old: SatelliteModel, new: SatelliteModel) =
            old.name == new.name

        override fun areContentsTheSame(old: SatelliteModel, new: SatelliteModel) = old == new
    }
}