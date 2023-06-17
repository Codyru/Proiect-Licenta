package com.android.example.scannerdocumente

import android.net.Uri
import androidx.room.TypeConverter
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
        val date = SimpleDateFormat("yyMMdd", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return outputDateFormat.format(date)
    }

}