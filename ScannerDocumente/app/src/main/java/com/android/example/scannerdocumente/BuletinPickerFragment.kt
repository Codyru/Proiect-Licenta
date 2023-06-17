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
    private lateinit var btnSerie: Button
    private lateinit var btnDataExpirare: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_buletin_picker, container, false)

        imgPhoto = view.findViewById(R.id.ivPicturePicker)
        btnPickPhoto = view.findViewById(R.id.btnImageSelect)
        btnValidate = view.findViewById(R.id.btnValidate)
        btnSerie = view.findViewById(R.id.btnSerie)
        btnDataExpirare = view.findViewById(R.id.btnDataExpirare)

        onClickOpenImage()
        onCliclValidateCNP()
        onClickValidateExpirationDate()
        onClickValidateSerie()

        return view
    }


    fun onCliclValidateCNP(){
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
    }

    fun onClickOpenImage(){
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
    }

    fun onClickValidateExpirationDate(){
        btnDataExpirare.setOnClickListener {
            val recognizeExpirationDate = TextRecognition(requireContext())
            recognizeExpirationDate.recognizeText(imageURI){result ->
                val expirationDateValidator = Validator()
                val dateConvertor = Converters()
                val lastTwoLines = recognizeExpirationDate.extractLastTwoLines(result)
                val lastLine = lastTwoLines.first
                Log.d("EXTRACTIE","Utlima line: $lastLine")
                Log.d("EXTRACTIE_TOTALA", "$result + '${lastLine.length}'")
//                val expirationDate = lastLine.substring(22,28)
//                Log.d("DATA_EXPIRARE", "$expirationDate")
//                val expirationDateCorrectFormat = dateConvertor.toCorrectDateFormat(expirationDate)
//                Log.d("DATA_EXPIRARE_FORMATATA", "$expirationDateCorrectFormat")
//                if(expirationDateValidator.validateExpirationDate(expirationDateCorrectFormat))
//                    Toast.makeText(requireContext(),"Data expirare in termen",Toast.LENGTH_LONG).show()
//                else
//                    Toast.makeText(requireContext(),"Data expirare trecuta",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun onClickValidateSerie(){
        btnSerie.setOnClickListener {
            val recognizeSerie = TextRecognition(requireContext())
            recognizeSerie.recognizeText(imageURI){result ->
                val serieValidator = Validator()
                val lastTwoLines = recognizeSerie.extractLastTwoLines(result)
                val lastLine = lastTwoLines.first
                val serie = lastLine.substring(0,2)
                if(serieValidator.validateSerie(serie))
                    Toast.makeText(requireContext(),"Serie valida", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(requireContext(),"Serie invalida", Toast.LENGTH_LONG).show()
            }
        }
    }

}