package com.android.example.scannerdocumente

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface PasaportDao {
    @Insert
    suspend fun insert(pasaport: Pasaport)
    @Update
    suspend fun update(pasaport: Pasaport)
    @Delete
    suspend fun delete(pasaport: Pasaport)
}