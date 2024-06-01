package com.example.satellitefinder.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.satellitefinder.databinding.LayoutMapViewBinding
import com.example.satellitefinder.utils.getWindowWidth
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class MapTypesDialog(context: Context) : Dialog(context){
    lateinit var binding: LayoutMapViewBinding
    fun showDialog(
        dismissCallBack: ((type:String) -> Unit)
    ) {
        binding = LayoutMapViewBinding.inflate(LayoutInflater.from(context))
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(context.getWindowWidth(0.90f), ViewGroup.LayoutParams.WRAP_CONTENT)
        context.let {
            val dialog = MaterialAlertDialogBuilder(it)

            dialog.setView(binding.root)
            dialog.setCancelable(true)
            val alert = dialog.create()
            alert.show()



           binding.btnHybridView.setOnClickListener {
               alert.dismiss()
               dismissCallBack.invoke("hybrid")
           }
            binding.btnNormalView.setOnClickListener {
               alert.dismiss()
               dismissCallBack.invoke("normal")
           }
            binding.btnSatelliteView.setOnClickListener {
               alert.dismiss()
               dismissCallBack.invoke("satellite")
           }
            binding.btnTerranView.setOnClickListener {
               alert.dismiss()
               dismissCallBack.invoke("terrain")
           }



        }

    }
}
