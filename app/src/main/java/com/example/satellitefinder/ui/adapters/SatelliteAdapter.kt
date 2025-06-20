package com.example.satellitefinder.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.satellitefinder.databinding.ItemSatelliteTypeBinding

class SatelliteAdapter(
    private val items: Array<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<SatelliteAdapter.SatelliteViewHolder>() {

    inner class SatelliteViewHolder(val binding: ItemSatelliteTypeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SatelliteViewHolder {
        val binding = ItemSatelliteTypeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SatelliteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SatelliteViewHolder, position: Int) {
        holder.binding.tvSatelliteName.text = items[position]
        holder.binding.clMain.setOnClickListener {
            onItemClick.invoke(items[position])
        }
    }

    override fun getItemCount() = items.size
}