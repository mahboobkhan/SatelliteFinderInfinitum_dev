package com.example.satellitefinder.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.satellitefinder.ui.fragments.FragmentFour
import com.example.satellitefinder.ui.fragments.Fragment_One
import com.example.satellitefinder.ui.fragments.Fragment_Three
import com.example.satellitefinder.ui.fragments.Fragment_Two
import com.example.satellitefinder.ui.fragments.ObADFragment

class ViewPagerAdapter(fm: FragmentManager, canShowAds: Boolean) : FragmentPagerAdapter(fm) {

    private val fragmentList = if (canShowAds) {
        listOf(
            Fragment_One(),
            Fragment_Two(),
            ObADFragment(),
            Fragment_Three(),
            FragmentFour()
        )
    } else {
        listOf(
            Fragment_One(),
            Fragment_Two(),
            Fragment_Three(),
            FragmentFour()
        )
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}
