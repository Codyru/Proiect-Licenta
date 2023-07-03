package com.android.example.scannerdocumente


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class Picture(val uri: String?, val name: String, val date: String, val type: String?) : Parcelable
