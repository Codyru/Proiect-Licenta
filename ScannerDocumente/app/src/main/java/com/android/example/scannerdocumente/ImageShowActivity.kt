package com.android.example.scannerdocumente

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ImageShowActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_show)


        val imageUri = Uri.parse(intent.getStringExtra("imageUri"))
        val bundle = intent.extras
        val pictureData = if(VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            bundle?.getParcelable("pictureData", Picture::class.java)
        }
        else{
            // Functia aceasta e folosita doar pentru API de nivel mai mic decat 33
            bundle?.getParcelable("pictureData")
        }
        val imageView = findViewById<ImageView>(R.id.ivSavedImage)
        imageView.setImageURI(imageUri)

        val saveButton = findViewById<Button>(R.id.btnSave)
        val deleteButton = findViewById<Button>(R.id.btnDelete)

        saveButton.setOnClickListener {

            val entityUri = pictureData?.uri
            val entityName = pictureData?.name
            val entityDate = pictureData?.date
            val entityType = pictureData?.type
            val imageData = ImageData(uri = entityUri, name = entityName, currentDate = entityDate, documentType = entityType)

            val db = AppDatabase.getInstance(applicationContext)
            val imageDataDao = db.imageDataDao()


            lifecycleScope.launch {

                val outputStream = contentResolver.openOutputStream(imageUri)

                outputStream?.close()

                imageDataDao.insert(imageData)
                Log.d("Insert", "Inserat cu succes")
                Toast.makeText(this@ImageShowActivity, "Image saved", Toast.LENGTH_SHORT).show()
                finish()
            }
            Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show()
            finish()
        }

        deleteButton.setOnClickListener {

            contentResolver.delete(imageUri, null, null)
            Toast.makeText(this, "Image deleted", Toast.LENGTH_SHORT).show()
            finish()
        }

    }



}