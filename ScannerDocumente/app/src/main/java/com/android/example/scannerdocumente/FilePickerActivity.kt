package com.android.example.scannerdocumente

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class FilePickerActivity : AppCompatActivity() {

    private lateinit var btnPickPhoto: Button
    private lateinit var imgPhoto: ImageView
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_picker)

        btnPickPhoto = findViewById(R.id.btnImageSelect)
        imgPhoto = findViewById(R.id.ivPicturePicker)

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    imgPhoto.setImageURI(uri)
                }
            }

        btnPickPhoto.setOnClickListener {
            activityResultLauncher.launch("image/*")
        }

    }

}