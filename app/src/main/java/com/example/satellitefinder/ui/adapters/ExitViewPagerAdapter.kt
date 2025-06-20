package com.example.satellitefinder.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.satellitefinder.R
import com.example.satellitefinder.databinding.ItemExitViewPagerBinding

class ExitViewPagerAdapter(
    private val context: Context,
    private val items: Array<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ExitViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemExitViewPagerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExitViewPagerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]

        with(holder.binding) {
            when (currentItem) {
                "angleMeter" -> {
                    main.setBackgroundResource(R.drawable.bg_exit_angle_meter)
                    btnTry.setTextColor(Color.parseColor("#C26395"))
                    imageView2.setImageResource(R.drawable.ic_angle_meter)
                    textView9.text = context.getString(R.string.angle_meter)
                    textView10.text = context.getString(R.string.angle_meter_desc)

                }

                "satellite" -> {
                    main.setBackgroundResource(R.drawable.bg_exit_satellite)
                    btnTry.setTextColor(Color.parseColor("#3495F1"))
                    imageView2.setImageResource(R.drawable.ic_exit_logo)
                    textView9.text = context.getString(R.string.satellite)
                    textView10.text = context.getString(R.string.satellite_desc)
                }

                "issTracker" -> {
                    main.setBackgroundResource(R.drawable.ic_satellite_tracker)
                    btnTry.setTextColor(Color.parseColor("#1CCBAB"))
                    imageView2.setImageResource(R.drawable.ic_iss_tracker)
                    textView9.text = context.getString(R.string.iss_tracker)
                    textView10.text = context.getString(R.string.iss_tracker_desc)

                }
            }
            btnTry.setOnClickListener {
                onItemClick.invoke(currentItem)
            }
        }


    }

    override fun getItemCount() = items.size
}