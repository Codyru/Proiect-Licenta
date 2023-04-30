package com.android.example.scannerdocumente

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class FilePickerActivity : AppCompatActivity() {

    private lateinit var btnPickPhoto: Button
    private lateinit var imgPhoto: ImageView
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>
    private lateinit var btnValidate: Button
    private lateinit var imageURI: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_picker)

        btnPickPhoto = findViewById(R.id.btnImageSelect)
        imgPhoto = findViewById(R.id.ivPicturePicker)
        btnValidate = findViewById(R.id.btnValidate)


        btnValidate.setOnClickListener {
            val textRecognitionForPicker = TextRecognition(this@FilePickerActivity)
            textRecognitionForPicker.recognizeText(imageURI) { result ->
                val cnpValidator = Validator()
                val lines = result.split("\n")
                for (line in lines) {
                    val words = line.split(" ")
                    for (word in words) {
                        if (cnpValidator.validateCNP(word)) {
                            Log.d("CNP_VALIDARE", "Este valid")
                            Toast.makeText(this@FilePickerActivity, "CNP valid",  Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }
        }

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    imgPhoto.setImageURI(uri)
                    imageURI = uri
                }
            }

        btnPickPhoto.setOnClickListener {
            activityResultLauncher.launch("image/*")
        }



    }

}