package com.example.satellitefinder.ui.adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.app.Activity
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import com.example.satellitefinder.databinding.SatelliteItemBinding
import com.example.satellitefinder.ui.activites.SearchSatelliteActivity
import com.example.satellitefinder.utils.SatellitesPositionData
import kotlin.collections.ArrayList

class SearchSatelliteAdapter(private val actionListner: ActionListener
) : RecyclerView.Adapter<SearchSatelliteAdapter.ViewHolder>(), Filterable {

    private val itemsList: ArrayList<SatellitesPositionData> = ArrayList()
    private var satelliteDataSource: ArrayList<SatellitesPositionData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SatelliteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.satTitle.text = itemsList[position].getSatellite().toString()
        holder.binding.satElevation.text = "Elevation:" + Math.round(itemsList[position].getSatelliteElevation()!!) + "°"
        holder.binding.satAzimut.text = "Azimuth:" + itemsList[position].getSatelliteAzimut() + "°"
        holder.binding.satAngel.text = "(" + itemsList[position].getSatelliteDirection() + ")"

        holder.itemView.setOnClickListener { v: View? ->
            if (holder.itemView.context is SearchSatelliteActivity) {
                val selectSatActivity = holder.itemView.context as SearchSatelliteActivity
                val intent = selectSatActivity.intent
                intent.putExtra("satObject", itemsList[position])
                selectSatActivity.setResult(Activity.RESULT_OK, intent)
                (holder.itemView.context as SearchSatelliteActivity).finish()
            }
            actionListner.sendData(itemsList[position])
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

     override fun getFilter(): Filter {
        return filter12
    }

    fun setData(loadData: java.util.ArrayList<SatellitesPositionData>) {
        satelliteDataSource.addAll(loadData)
        itemsList.addAll(loadData)
    }

    private var filter12: Filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filterList = ArrayList<SatellitesPositionData>()
            if (charSequence.toString().isEmpty()) {
                filterList.addAll(satelliteDataSource)
            } else {
                for (satelliteSource in satelliteDataSource) {
                    if (satelliteSource.getSatellite().toString().toLowerCase()
                            .contains(charSequence.toString().toLowerCase())
                    ) {
                        filterList.add(satelliteSource)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filterList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, filterResults: FilterResults) {
            itemsList.clear()
            itemsList.addAll((filterResults.values as Collection<SatellitesPositionData>))
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(val binding: SatelliteItemBinding) : RecyclerView.ViewHolder(
        binding.root)

    interface ActionListener {
        fun sendData(satellitePositionData: SatellitesPositionData?)
    }

}