package com.android.example.scannerdocumente

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils.substring
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


class BuletinPickerFragment : Fragment() {

    private lateinit var imgPhoto: ImageView
    private lateinit var btnPickPhoto: Button
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>
    private lateinit var imageURI: Uri
    private lateinit var btnValidate: Button
    private lateinit var btnSerie: Button
    private lateinit var btnDataExpirare: Button
    private var pictureUri: Uri? = null
    private var documentType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_buletin_picker, container, false)

        imgPhoto = view.findViewById(R.id.ivPicturePicker)
        btnPickPhoto = view.findViewById(R.id.btnImageSelect)
        btnValidate = view.findViewById(R.id.btnValidate)
        btnSerie = view.findViewById(R.id.btnSerie)
        btnDataExpirare = view.findViewById(R.id.btnDataExpirare)

        arguments?.let {
            pictureUri = it.getParcelable("PICTURE_URI")
            documentType = it.getString("DOCUMENT_TYPE")
        }

        loadFromPictureFromRV()

        onClickOpenImage()
        onClickValidateCNP()
        onClickValidateExpirationDate()
        onClickValidateSerie()

        return view
    }


    fun onClickValidateCNP(){

        btnValidate.setOnClickListener {
            if (imgPhoto.drawable == null){
                Toast.makeText(requireContext(), R.string.empty_iv_message, Toast.LENGTH_SHORT).show()
            }
            else{
                val textRecognitionForPicker = TextRecognition(requireContext())
                textRecognitionForPicker.recognizeText(imageURI) { result ->
                    if (result.isNullOrBlank() || result.trim() == textRecognitionForPicker.errorString || result.length < 10){
                        Log.d("EROARE_RECUNOASTERE", "Nu am putut recunoaste textul.")
                        activity?.runOnUiThread {
                            Toast.makeText(requireContext(), R.string.unrecognized_text, Toast.LENGTH_LONG).show()
                        }
                    }
                    else{
                        Log.d("RESULT", "$result")
                        val cnpValidator = Validator()

                        val cnp = textRecognitionForPicker.extractCNP(result)
                        Log.d("CNP", "$cnp")
                        if(cnpValidator.validateCNP(cnp)){
                            Log.d("CNP_VALIDARE", "Este valid")
                            Toast.makeText(requireContext(), "CNP valid",  Toast.LENGTH_LONG).show()
                        }
                        else{
                            Log.d("CNP_INVALIDARE", "Este invalid")
                            Toast.makeText(requireContext(), "CNP invalid",  Toast.LENGTH_LONG).show()
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
            if (imgPhoto.drawable == null){
                Toast.makeText(requireContext(), R.string.empty_iv_message, Toast.LENGTH_SHORT).show()
            }
            else{
                val recognizeExpirationDate = TextRecognition(requireContext())
                recognizeExpirationDate.recognizeText(imageURI){result ->
                    if (result.isNullOrBlank() || result.trim() == recognizeExpirationDate.errorString || result.length < 10){
                        Toast.makeText(requireContext(), R.string.unrecognized_text, Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val expirationDateValidator = Validator()
                        val dateConvertor = Converters()
                        val lastTwoLines = recognizeExpirationDate.extractLastTwoLines(result)
                        val lastLine = lastTwoLines.first
                        Log.d("LAST_LINE", "$lastLine")
                        val lastLineNoSpace = lastLine.replace(" ", "")
                        val expirationDate = lastLineNoSpace.substring(21,27)
                        Log.d("EXPIRATION_DATE", "$expirationDate")
                        val expirationDateCorrectFormat = dateConvertor.toCorrectDateFormat(expirationDate)
                        Log.d("CONVERTED_EXPIRATION", "$expirationDateCorrectFormat")
                        if(expirationDateValidator.validateExpirationDate(expirationDateCorrectFormat))
                            Toast.makeText(requireContext(),"Data expirare in termen",Toast.LENGTH_LONG).show()
                        else
                            Toast.makeText(requireContext(),"Data expirare trecuta",Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }

    fun onClickValidateSerie(){
        btnSerie.setOnClickListener {
            if (imgPhoto.drawable == null){
                Toast.makeText(requireContext(), R.string.empty_iv_message, Toast.LENGTH_SHORT).show()
            }
            else{
                val recognizeSerie = TextRecognition(requireContext())
                recognizeSerie.recognizeText(imageURI){result ->
                    if (result.isNullOrBlank() || result.trim() == recognizeSerie.errorString || result.length < 10)
                    {
                        Toast.makeText(requireContext(), R.string.unrecognized_text, Toast.LENGTH_SHORT).show()
                    }
                    else{
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
    }

    fun loadFromPictureFromRV(){
        if(pictureUri != null && documentType == "Buletin") {
            Glide.with(requireContext())
                .load(pictureUri)
                .into(imgPhoto)
            imageURI = pictureUri!!
        }

    }

}