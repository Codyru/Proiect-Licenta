package com.android.example.scannerdocumente

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils.replace
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
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_pasaport_picker.*


class PasaportPickerFragment : Fragment() {

    private lateinit var btnNationalitateValidate: Button
    private lateinit var btnImagePick: Button
    private lateinit var ivPic: ImageView
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>
    private lateinit var imageURI: Uri
    private lateinit var btnCNP: Button
    private lateinit var btnExpirationDate: Button
    private var pictureUri: Uri? = null
    private var documentType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_pasaport_picker, container, false)

        btnNationalitateValidate = view.findViewById(R.id.btnNationalitateValidate)
        btnImagePick = view.findViewById(R.id.btnPicSelect)
        ivPic = view.findViewById(R.id.ivPicPicker)
        btnCNP = view.findViewById(R.id.btnCNPValidate)
        btnExpirationDate = view.findViewById(R.id.btnExpirationDate)

        arguments?.let {
            pictureUri = it.getParcelable("PICTURE_URI")
            documentType = it.getString("DOCUMENT_TYPE")
        }

        loadFromPictureFromRV()

        pickImage()
        validateNationality()
        validateExpirationDate()
        validateCNP()

        return view

    }

    fun pickImage(){
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
    }

    fun validateNationality(){
        btnNationalitateValidate.setOnClickListener {
            if(ivPic.drawable == null){
                Toast.makeText(requireContext(), R.string.empty_iv_message, Toast.LENGTH_SHORT).show()
            }
            else{
                val textRecognition = TextRecognition(requireContext())
                textRecognition.recognizeText(imageURI) { result ->
                    if (result.isNullOrBlank() || result.trim() == textRecognition.errorString || result.length < 10)
                    {
                        Toast.makeText(requireContext(), R.string.unrecognized_text, Toast.LENGTH_SHORT).show()
                    }
                    else{
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
            }

        }
    }

    fun validateExpirationDate(){
        btnExpirationDate.setOnClickListener {
            if(ivPic.drawable == null){
                Toast.makeText(requireContext(), R.string.empty_iv_message, Toast.LENGTH_SHORT).show()
            }
            else{
                val recognizeExpirationDate = TextRecognition(requireContext())
                recognizeExpirationDate.recognizeText(imageURI) { result ->
                    if (result.isNullOrBlank() || result.trim() == recognizeExpirationDate.errorString || result.length < 10)
                    {
                        Toast.makeText(requireContext(), R.string.unrecognized_text, Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val lastTwoLines = recognizeExpirationDate.extractLastTwoLines(result)
                        val lastLine = lastTwoLines.first
                        var expirationDate = lastLine.substring(21,27)
                        val dateConvertor = Converters()
                        expirationDate = dateConvertor.toCorrectDateFormat(expirationDate)
                        val expirationValidator = Validator()
                        if (expirationValidator.validateExpirationDate(expirationDate)){
                            Toast.makeText(requireContext(), "Data expirare in termen", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(requireContext(), "Data expirare trecuta", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
    }

    fun validateCNP(){
        btnCNP.setOnClickListener {
            if (ivPic.drawable == null){
                Toast.makeText(requireContext(), R.string.empty_iv_message, Toast.LENGTH_SHORT).show()
            }
            else{
                val recognizeCNP = TextRecognition(requireContext())
                recognizeCNP.recognizeText(imageURI){ result ->
                    if (result.isNullOrBlank() || result.trim() == recognizeCNP.errorString || result.length < 10)
                    {
                        Toast.makeText(requireContext(), R.string.unrecognized_text, Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val lastTwoLines = recognizeCNP.extractLastTwoLines(result)
                        val lastLine = lastTwoLines.first
                        var CNP = lastLine.substring(28,43)
                        CNP = CNP.replace("\\s".toRegex(), "")
                        Log.d("VERIFICA_PSEUDO_CNP", "$CNP")
                        val validatorCNP = Validator()
                        if (validatorCNP.validateCNP(CNP))
                            Toast.makeText(requireContext(), "CNP valid", Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(requireContext(), "CNP invalid", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
    }

    fun loadFromPictureFromRV(){
        if(pictureUri != null && documentType == "Pasaport") {
            Glide.with(requireContext())
                .load(pictureUri)
                .into(ivPic)
            imageURI = pictureUri!!
        }
    }

}