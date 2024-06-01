package com.example.satellitefinder.ui.adapters

import androidx.recyclerview.widget.RecyclerView

abstract class BaseItem {
    abstract fun itemType(): Int
    abstract fun bindItem(holder: RecyclerView.ViewHolder?, position: Int)
}