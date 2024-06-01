package com.example.satellitefinder.ui.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.satellitefinder.databinding.LayoutMapViewBinding
import com.example.satellitefinder.databinding.LayoutSatelliteInfoBinding
import com.example.satellitefinder.utils.SatellitesPositionData
import com.example.satellitefinder.utils.currentLocation
import com.example.satellitefinder.utils.getWindowWidth
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class InfoDialog(context: Context,) : Dialog(context){
    lateinit var binding: LayoutSatelliteInfoBinding
    @SuppressLint("SetTextI18n")
    fun showDialog(
         satelliteInfo:SatellitesPositionData
    ) {
        binding = LayoutSatelliteInfoBinding.inflate(LayoutInflater.from(context))
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(context.getWindowWidth(0.90f), ViewGroup.LayoutParams.WRAP_CONTENT)
        context.let {
            val dialog = MaterialAlertDialogBuilder(it)

            dialog.setView(binding.root)
            dialog.setCancelable(true)
            val alert = dialog.create()
            alert.show()



            binding.apply {
                btnClose.setOnClickListener {
                    alert.dismiss()
                }
                satTitle.text = "Satellite :  ${satelliteInfo.getSatellite()}"
                satAzimut.text = "Azimuth : ${satelliteInfo.getSatelliteAzimut()}°"
                satellitePositionTv.text = "Position : ${ satelliteInfo.getSatLongitude()}°"
                satElevation.text = "Elevation : ${satelliteInfo.getSatelliteElevation()?.let { it1 -> Math.round(it1) }}°"
                satelliteLNBskewTv.text = "LNB Skew : ${Math.round(satelliteInfo.getLNBSkew())}°"
                satelliteLatitudeTv.text = "Latitude : ${Math.round(currentLocation!!.latitude)}"
                satelliteLongitudeTv.text = "Longitude : ${Math.round(currentLocation!!.longitude)}"
                satelliteAltitudeTv.text = "Altitude : ${Math.round(currentLocation!!.altitude)}"
                satelliteAccuracyTv.text  = "Accuracy : ${Math.round(currentLocation!!.accuracy)}"

            }

        }

    }
}
