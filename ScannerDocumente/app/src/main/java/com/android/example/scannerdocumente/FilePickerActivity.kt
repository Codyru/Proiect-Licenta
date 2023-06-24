package com.android.example.scannerdocumente


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FilePickerActivity : AppCompatActivity() {


private val tabTitles = listOf("Buletin", "Pasaport", "Diploma")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_picker)

        val documentType = intent.getStringExtra("PICTURE_DOCUMENT_TYPE")
        val pictureUriString = intent.getStringExtra("PICTURE_URI")
        val convertor = Converters()
        val pictureURI = convertor.toUri(pictureUriString)
        Log.d("FILE_PICKER_ACTIVITY", "$pictureURI")
        val desiredFragmentIndex = determineFragmentIndex(documentType)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, pictureURI, documentType)
        viewPager.adapter = adapter

        // Connect the TabLayout and ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Tab $position"
        }.attach()

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

    }

    private fun determineFragmentIndex(documentType: String?): Int {

        return when (documentType) {
            "Buletin" -> 0
            "Pasaport" -> 1
            "Diploma" -> 2
            else -> 0
        }
    }
}