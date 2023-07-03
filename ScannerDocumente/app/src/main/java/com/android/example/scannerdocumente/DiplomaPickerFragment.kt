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
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text


class DiplomaPickerFragment : Fragment() {

    private var pictureUri: Uri? = null
    private lateinit var btnValidateDiploma: Button
    private lateinit var ivDiploma: ImageView
    private var documentType: String? = null
    private lateinit var btnSelectImage: Button
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_diploma_picker, container, false)

        btnValidateDiploma = view.findViewById(R.id.btnValidateDiploma)
        ivDiploma = view.findViewById(R.id.ivDiploma)
        btnSelectImage = view.findViewById(R.id.btnSelectImage)

        arguments?.let {
            pictureUri = it.getParcelable("PICTURE_URI")
            documentType = it.getString("DOCUMENT_TYPE")
        }

        btnValidateDiploma.setOnClickListener {
            extractDiplomaText()
        }

        pickImage()

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
                val lines = result.split("\n")
                var university = ""
                for(line in lines)
                {
                    if(line.contains("UNIVERSITATEA"))
                    {   university = line
                        val convertor = Converters()
                        Log.d("UNIVERSITATE_BEFORE", "$university")
                        university = convertor.convertRomanianDiacritics(university)
                        Log.d("UNIVERSITATE_AFTER", "$university")
                        val universityDao = AppDatabase.getInstance(requireContext()).UniversityDao()

                        val scope = CoroutineScope(Dispatchers.Main)

                        scope.launch {
                            val isUniversityExists = withContext(Dispatchers.IO) {

                                universityDao.searchUniversityName(university)
                            }

                            if (isUniversityExists) {
                                Toast.makeText(requireContext(), "Diploma valida", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(), "Diploma invalida", Toast.LENGTH_SHORT).show()
                            }
                        }

                        break
                    }
                }
            }
        } }
    }

    fun pickImage(){
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                if (uri != null) {
                    ivDiploma.setImageURI(uri)
                    pictureUri = uri
                }
            }
        )

        btnSelectImage.setOnClickListener {
            activityResultLauncher.launch("image/*")
        }
    }

}