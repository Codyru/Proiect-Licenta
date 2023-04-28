package com.android.example.scannerdocumente

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnItemReselectedListener {
            when(it.itemId){
                R.id.person-> startActivity(Intent(this,CameraActivity::class.java))

            }
            true
        }
    }
    }
}