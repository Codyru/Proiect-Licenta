package com.android.example.scannerdocumente

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uri: String?,
    val name: String?,
    val currentDate: String?,
    val documentType: String?
)
