package com.android.example.scannerdocumente

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Buletin")
data class Buletin(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val lastName: String,
    val firsName: String,
    val CNP: String,
    val serie: String,
    val number: Int,
    val expirationDate: String
)
