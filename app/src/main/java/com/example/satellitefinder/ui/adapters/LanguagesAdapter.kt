package com.example.satellitefinder.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.satellitefinder.R
import com.example.satellitefinder.databinding.LanguageRowBinding
import com.example.satellitefinder.utils.LanguagesModel
import com.example.satellitefinder.utils.MySharePrefrencesHelper

import java.util.*

class LanguagesAdapter(private var langList: MutableList<LanguagesModel>, private var onLanguageClick: (String) -> Unit) : RecyclerView.Adapter<LanguagesAdapter.MyViewHolder>() {
    var isSelected = -1
    var selectedLangName: String = Locale.getDefault().displayName
    var languageCode: String = Locale.getDefault().language


    inner class MyViewHolder(private val binding: LanguageRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: LanguagesModel) {
            binding.name.text = model.name

            if (isSelected != -1) {
                if (adapterPosition == isSelected) {
                    binding.radioBtn.setImageResource(R.drawable.ic_check)
                } else {
                    binding.radioBtn.setImageResource(R.drawable.ic_uncheck)
                }
            } else {
                if (MySharePrefrencesHelper.getKey(binding.root.context,
                        "langCode", "en") == model.code) {
                    binding.radioBtn.setImageResource(R.drawable.ic_check)
                } else {
                    binding.radioBtn.setImageResource(R.drawable.ic_uncheck)
                }
            }

            itemView.setOnClickListener {

                onLanguageClick.invoke(model.code)
                isSelected = adapterPosition
                selectedLangName = model.name
                languageCode = model.code
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LanguageRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(langList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return langList.size
    }

}