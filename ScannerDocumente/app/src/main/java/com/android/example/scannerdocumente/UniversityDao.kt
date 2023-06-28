package com.android.example.scannerdocumente

import androidx.room.Dao
import androidx.room.Query

@Dao
interface UniversityDao {
    @Query("SELECT EXISTS(SELECT 1 FROM University WHERE name LIKE :searchQuery LIMIT 1)")
    fun searchUniversityName(searchQuery: String): Boolean
}