package com.android.example.scannerdocumente

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val pictureUri: Uri?,
    private val documentType: String?
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> {
                val fragment = BuletinPickerFragment()
                val bundle = Bundle()
                bundle.putParcelable("PICTURE_URI", pictureUri)
                bundle.putString("DOCUMENT_TYPE", documentType)
                fragment.arguments = bundle
                fragment
            }
            1 -> {
                val fragment = PasaportPickerFragment()
                val bundle = Bundle()
                bundle.putParcelable("PICTURE_URI", pictureUri)
                bundle.putString("DOCUMENT_TYPE", documentType)
                fragment.arguments = bundle
                fragment
            }
            2 -> {
                val fragment = DiplomaPickerFragment()
                val bundle = Bundle()
                bundle.putParcelable("PICTURE_URI", pictureUri)
                bundle.putString("DOCUMENT_TYPE", documentType)
                fragment.arguments = bundle
                fragment
            }
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }
}