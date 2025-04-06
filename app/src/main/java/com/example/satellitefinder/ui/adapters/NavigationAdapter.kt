package com.example.satellitefinder.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.satellitefinder.R
import com.example.satellitefinder.databinding.NavigationItemBinding

data class NavigationItemModel(var icon: Int, var title: String)

class NavigationAdapter(private var items: ArrayList<NavigationItemModel>, private var currentPos: Int) :
    RecyclerView.Adapter<NavigationAdapter.NavigationItemViewHolder>() {

    class NavigationItemViewHolder(val binding: NavigationItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationItemViewHolder {

        return NavigationItemViewHolder(NavigationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: NavigationItemViewHolder, position: Int) {
        // To highlight the selected Item, show different background color
        if (position == currentPos) {
            holder.binding.root.setBackgroundResource(R.drawable.menu_selected_bg)
        } else {
            holder.binding.root.setBackgroundResource(0)
        }

        holder.binding.navigationTitle.text = items[position].title

        holder.binding.navigationIcon.setImageResource(items[position].icon)

    }
}