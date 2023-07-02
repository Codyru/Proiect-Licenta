package com.android.example.scannerdocumente

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextRecognition(private val context: Context) {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    internal var errorString = "OCR processing failed"
        private set

    fun recognizeText(imageUri: Uri, onResult: (String) -> Unit) {
        val image = InputImage.fromFilePath(context, imageUri)
        recognizer.process(image).addOnSuccessListener { visionText ->
            val resultText = visionText.text
            onResult(resultText)
        }
        recognizer.process(image).addOnFailureListener { exception ->
            Log.e("OCR", "$errorString + ': ${exception.message}' ")
            onResult(errorString)
        }
    }

    fun extractLastTwoLines(text: String): Pair<String, String> {
        val lines = text.split("\n")

        if (lines.size >= 2) {
            val lastLine = lines[lines.size - 1].trim()
            val secondLastLine = lines[lines.size - 2].trim()

            return Pair(lastLine, secondLastLine)
        }

        return Pair("", "")
    }

    fun extractCNP(cnpLine: String): String{
        var ocrResult = ""
        if (cnpLine.contains("CNP"))
            ocrResult = cnpLine


        val pattern = Regex("\\b\\d{13}\\b")


        val matchResult = pattern.find(ocrResult)


        val number = matchResult?.value


        return if (number != null) {
            number.toString()
        } else {
            ""
        }
    }
}