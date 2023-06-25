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
import com.bumptech.glide.Glide
import org.w3c.dom.Text


class DiplomaPickerFragment : Fragment() {

    private var pictureUri: Uri? = null
    private lateinit var btnValidateDiploma: Button
    private lateinit var ivDiploma: ImageView
    private var documentType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_diploma_picker, container, false)

        btnValidateDiploma = view.findViewById(R.id.btnValidateDiploma)
        ivDiploma = view.findViewById(R.id.ivDiploma)

        arguments?.let {
            pictureUri = it.getParcelable("PICTURE_URI")
            documentType = it.getString("DOCUMENT_TYPE")
        }

        btnValidateDiploma.setOnClickListener {
            extractDiplomaText()
        }

        loadFromPictureFromRV()

        return view
    }

    fun loadFromPictureFromRV(){
        if(pictureUri != null && documentType == "Diploma") {
            Glide.with(requireContext())
                .load(pictureUri)
                .into(ivDiploma)
        }
    }

    fun extractDiplomaText(){
        var recognizeDiploma = TextRecognition(requireContext())
        pictureUri?.let { recognizeDiploma.recognizeText(it) { result ->
            if (result.isNullOrBlank() || result.trim() == recognizeDiploma.errorString || result.length < 10){
                Log.d("EROARE_RECUNOASTERE", "Nu am putut recunoaste textul.")
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), R.string.unrecognized_text, Toast.LENGTH_LONG).show()
                }
            }
            else{
                Log.d("RESULT", "$result")
                Toast.makeText(requireContext(), "Rezultat recunoscut", Toast.LENGTH_LONG).show()
            }
        } }
    }

}