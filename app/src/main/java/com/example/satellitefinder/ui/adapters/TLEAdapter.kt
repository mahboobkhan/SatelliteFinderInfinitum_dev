package com.example.satellitefinder.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.satellitefinder.R
import com.example.satellitefinder.models.TLEEntry

class TLEAdapter(
    private val tleList: List<TLEEntry>,
    private val onClick: (TLEEntry) -> Unit
) : RecyclerView.Adapter<TLEAdapter.TLEViewHolder>() {

    inner class TLEViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.tleName)
        init {
            itemView.setOnClickListener {
                onClick(tleList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TLEViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tle, parent, false)
        return TLEViewHolder(view)
    }

    override fun onBindViewHolder(holder: TLEViewHolder, position: Int) {
        holder.nameText.text = tleList[position].name
    }

    override fun getItemCount() = tleList.size
}