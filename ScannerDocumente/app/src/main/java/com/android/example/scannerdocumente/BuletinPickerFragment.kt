package com.android.example.scannerdocumente

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts


class BuletinPickerFragment : Fragment() {

    private lateinit var imgPhoto: ImageView
    private lateinit var btnPickPhoto: Button
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>
    private lateinit var imageURI: Uri
    private lateinit var btnValidate: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_buletin_picker, container, false)

        imgPhoto = view.findViewById(R.id.ivPicturePicker)
        btnPickPhoto = view.findViewById(R.id.btnImageSelect)
        btnValidate = view.findViewById(R.id.btnValidate)

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                if (uri != null) {
                    imgPhoto.setImageURI(uri)
                    imageURI = uri
                }
            }
        )

        btnPickPhoto.setOnClickListener {
            activityResultLauncher.launch("image/*")
        }

        btnValidate.setOnClickListener {
            val textRecognitionForPicker = TextRecognition(requireContext())
            textRecognitionForPicker.recognizeText(imageURI) { result ->
                val cnpValidator = Validator()
                val lines = result.split("\n")
                for (line in lines) {
                    val words = line.split(" ")
                    for (word in words) {
                        if (cnpValidator.validateCNP(word)) {
                            Log.d("CNP_VALIDARE", "Este valid")
                            Toast.makeText(requireContext(), "CNP valid",  Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }
        }

        return view
    }


}