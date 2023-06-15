package com.android.example.scannerdocumente

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3 // Return the number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        // Create and return the fragment for each tab
        return when (position) {
            0 -> BuletinPickerFragment()
            1 -> PasaportPickerFragment()
            2 -> DiplomaPickerFragment()
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }
}