package com.android.example.scannerdocumente

import android.net.Uri
import androidx.room.TypeConverter
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*

class Converters {
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        return uriString?.let { Uri.parse(it) }
    }

    fun toCorrectDateFormat(date: String): String{
        val inputDateFormat = SimpleDateFormat("yyMMdd", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val parseDate = inputDateFormat.parse(date)
        return outputDateFormat.format(parseDate)
    }

    fun convertRomanianDiacritics(input: String): String {
        val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
        val regex = "\\p{Mn}".toRegex()
        val converted = regex.replace(normalized, "")
        return converted
    }

}