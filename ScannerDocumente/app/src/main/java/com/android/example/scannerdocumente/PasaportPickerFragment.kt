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
import kotlinx.android.synthetic.main.fragment_pasaport_picker.*


class PasaportPickerFragment : Fragment() {

    private lateinit var btnNationalitateValidate: Button
    private lateinit var btnImagePick: Button
    private lateinit var ivPic: ImageView
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>
    private lateinit var imageURI: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pasaport_picker, container, false)

        btnNationalitateValidate = view.findViewById(R.id.btnNationalitateValidate)
        btnImagePick = view.findViewById(R.id.btnPicSelect)
        ivPic = view.findViewById(R.id.ivPicPicker)

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                if (uri != null) {
                    ivPic.setImageURI(uri)
                    imageURI = uri
                }
            }
        )

        btnImagePick.setOnClickListener {
            activityResultLauncher.launch("image/*")
        }

        btnNationalitateValidate.setOnClickListener {
            val textRecognition = TextRecognition(requireContext())
            textRecognition.recognizeText(imageURI) { result ->

                val lastTwoLines = textRecognition.extractLastTwoLines(result)
                val lastLine = lastTwoLines.first
                Log.d("ULTIMA_LINIE", "$lastLine")
                val secondLastLine = lastTwoLines.second
                Log.d("PENULTIMA_LINIE", "$secondLastLine")
                val nationalite = secondLastLine.substring(2,5)
                Log.d("nationalitate", "$nationalite")
                val nationalitateValidator = Validator()

                if(nationalitateValidator.checkNationalitate(nationalite)) {
                    Log.d("NATIONALITATE_VALIDARE", "Nationalitate valida")
                    Toast.makeText(requireContext(), "Nationalitate valida", Toast.LENGTH_LONG).show()
                }
                else{
                    Log.d("NATIONALITATE_VALIDARE", "Nationalitate invalida")
                    Toast.makeText(requireContext(), "Nationalitate invalida", Toast.LENGTH_LONG).show()
                }
            }
        }
        return view

    }



}