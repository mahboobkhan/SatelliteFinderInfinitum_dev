package com.example.satellitefinder.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.satellitefinder.ui.fragments.Fragment_One
import com.example.satellitefinder.ui.fragments.Fragment_Three
import com.example.satellitefinder.ui.fragments.Fragment_Two

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragmentList = listOf(
        Fragment_One(),
        Fragment_Two(),
        Fragment_Three()
    )

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}
