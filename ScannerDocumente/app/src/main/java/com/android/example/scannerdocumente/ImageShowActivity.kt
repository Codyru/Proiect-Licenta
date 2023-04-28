package com.android.example.scannerdocumente

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ImageShowActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_show)


        val imageUri = Uri.parse(intent.getStringExtra("imageUri"))
        val imageView = findViewById<ImageView>(R.id.ivSavedImage)
        imageView.setImageURI(imageUri)

        val saveButton = findViewById<Button>(R.id.btnSave)
        val deleteButton = findViewById<Button>(R.id.btnDelete)

        saveButton.setOnClickListener {
            // Save the image
            val outputStream = contentResolver.openOutputStream(imageUri)
            // Write the image data to the output stream
            outputStream?.close()
            Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show()
            finish()
        }

        deleteButton.setOnClickListener {
            // Delete the image
            contentResolver.delete(imageUri, null, null)
            Toast.makeText(this, "Image deleted", Toast.LENGTH_SHORT).show()
            finish()
        }

    }



}