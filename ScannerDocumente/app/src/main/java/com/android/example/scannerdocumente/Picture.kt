package com.android.example.scannerdocumente

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
@Parcelize
data class Picture(val uri: Uri, val name: String, val date: String, val type: String?) : Parcelable
