package com.example.satellitefinder.ui.adapters


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.newLoadAndShowNativeAd
import com.example.satellitefinder.databinding.ListAdItemBinding
import com.example.satellitefinder.databinding.SatelliteItemBinding
import com.example.satellitefinder.ui.activites.SatellitesActivity
import com.example.satellitefinder.utils.SatellitesPositionData
import kotlin.math.roundToLong

class SatellitesAdapter(val context: Context, val onItemClick: (SatellitesPositionData) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var entitiesList: MutableList<BaseItem> = ArrayList()
    var adsHashMap = HashMap<Int, Any>()
    var activity : Activity? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder{
        return when (viewType) {
            0 -> {
                val inflate =
                    SatelliteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return SatelliteViewHolder(inflate)
            }
            else -> {
                NativeViewHolder(ListAdItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = entitiesList[position]) {
            is NativeItem -> {
                if (adsHashMap[position] != null) {
                    item.nativeAd = adsHashMap[position] as Any
                }
                item.bindItem(holder, position)
            }
            is SatelliteItemView -> {

                item.bindItem(holder, position)
            }
        }
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (entitiesList.isEmpty())
            return 0
        else {
            entitiesList.size
        }
    }

    inner class NativeViewHolder(val binding: ListAdItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            activity?.newLoadAndShowNativeAd(
                binding.adfram, R.layout.list_ad, context.getString(R.string.selectSatelliteNativeId), null)
        }
    }

    class SatelliteItemView(val satellite:SatellitesPositionData) : BaseItem() {
        override fun itemType(): Int {
            return 0
        }

        override fun bindItem(holder: RecyclerView.ViewHolder?, position: Int) {
            holder as SatelliteViewHolder

            holder.bindData(satellite,position)
        }
    }

    private class NativeItem(var nativeAd: Any?) : BaseItem() {
        override fun itemType(): Int {
            return 2
        }

        override fun bindItem(holder: RecyclerView.ViewHolder?, position: Int) {
            (holder as NativeViewHolder)
            holder.bind()
        }
    }

    fun setData(
        isNativeAdEnable: Boolean,
        isAutoAdsRemoved: Boolean,
        itemList: ArrayList<SatellitesPositionData>,
        isNetworkAvilable: Boolean,
        countAfter: Int,
        activity: Activity
    ) {
        this.activity = activity
        entitiesList.clear()

        itemList.forEachIndexed { index, entity ->

            if (isNativeAdEnable && isNetworkAvilable && !isAutoAdsRemoved) {
                if (index == 2) {
                    entitiesList.add(NativeItem(null))

                } else if (index > countAfter && ((index - 2) % countAfter) == 0)
                    entitiesList.add(NativeItem(null))

                entitiesList.add(SatelliteItemView(entity))
                notifyDataSetChanged()

            } else
                entitiesList.add(SatelliteItemView(entity))
            notifyDataSetChanged()

        }
    }
    override fun getItemViewType(position: Int): Int {
        return when {
            entitiesList[position].itemType() == 0 -> {
                0
            }
            entitiesList[position].itemType() == 1 -> {
                1
            }
            else -> 2
        }
    }

    inner class SatelliteViewHolder(val binding: SatelliteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindData(item: SatellitesPositionData?, position: Int) {
            item?.let { data ->
                binding.satTitle.text = data.getSatellite().toString()
                binding.satElevation.text =
                    "Elevation: ${(data.getSatelliteElevation())?.roundToLong()}°"
                binding.satAzimut.text = "Azimuth:${data.getSatelliteAzimut()}°"
                binding.satAngel.text = "(${data.getSatelliteDirection()})"

                binding.root.setOnClickListener {
                    onItemClick.invoke(data)
                }
            }
        }
    }
}