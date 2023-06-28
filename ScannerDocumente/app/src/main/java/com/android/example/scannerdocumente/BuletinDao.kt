package com.android.example.scannerdocumente

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface BuletinDao {
    @Insert
    suspend fun insert(buletin: Buletin)
    @Update
    suspend fun update(buletin: Buletin)
    @Delete
    suspend fun delete(buletin: Buletin)
}