package com.android.example.scannerdocumente

import androidx.room.*

@Dao
interface ImageDataDao {
    @Insert
    suspend fun insert(imageData: ImageData)

    @Update
    suspend fun update(imageData: ImageData)

    @Delete
    suspend fun delete(imageData: ImageData)

    @Query("SELECT * FROM images")
    suspend fun getAllImages(): List<ImageData>
}