package com.android.example.scannerdocumente

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "University")
data class University(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val location: String,
    val foundationDate: String,
    val type: String
)
