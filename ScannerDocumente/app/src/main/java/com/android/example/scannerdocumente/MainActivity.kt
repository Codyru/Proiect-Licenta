package com.android.example.scannerdocumente

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentContainer: FrameLayout
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentContainer = findViewById(R.id.fragmentContainer)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.camera-> startActivity(Intent(this,CameraActivity::class.java))
                R.id.chooseFile -> startActivity(Intent(this, FilePickerActivity::class.java))
                R.id.viewDocuments -> {
                    replaceFragment(PictureListFragment())
                    true
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
