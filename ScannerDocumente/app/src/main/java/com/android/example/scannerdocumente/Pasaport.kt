package com.android.example.scannerdocumente

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Pasaport")
data class Pasaport(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val lastName: String,
    val firstName: String,
    val nationality: String,
    val CNP: String,
    val expirationDate: String,
)
