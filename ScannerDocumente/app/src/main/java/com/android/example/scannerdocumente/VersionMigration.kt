package com.android.example.scannerdocumente

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class VersionMigration: Migration(1,2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `Buletin` (`id` INTEGER PRIMARY KEY, `lastName` TEXT NOT NULL, `firstName` TEXT NOT NULL, `CNP` TEXT NOT NULL, " +
                "`serie` TEXT NOT NULL, `number` INTEGER NOT NULL, `expirationDate` TEXT NOT NULL)")
        database.execSQL("CREATE TABLE IF NOT EXISTS `Pasaport` (`id` INTEGER PRIMARY KEY, `lastName` TEXT NOT NULL," +
                " `firstName` TEXT NOT NULL, `nationality` TEXT NOT NULL, `CNP` TEXT NOT NULL, `expirationDate` TEXT NOT NULL)")

    }
}