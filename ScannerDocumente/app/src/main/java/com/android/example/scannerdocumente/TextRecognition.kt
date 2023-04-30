package com.android.example.scannerdocumente

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextRecognition(private val context: Context) {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun recognizeText(imageUri: Uri, onResult: (String) -> Unit) {
        val image = InputImage.fromFilePath(context, imageUri)
        recognizer.process(image).addOnSuccessListener { visionText ->
            val resultText = visionText.text
            onResult(resultText)
        }
    }
}