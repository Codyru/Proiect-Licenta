package com.android.example.scannerdocumente

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ImageData::class, Buletin::class, Pasaport::class, University::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDataDao(): ImageDataDao
    abstract fun BuletinDao(): BuletinDao
    abstract fun PasaportDao(): PasaportDao
    abstract fun UniversityDao(): UniversityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Insert prepopulated data here
                        db.execSQL("INSERT INTO `University` (`name`, `location`, `foundationDate`, `type`) VALUES ('UNIVERSITATEA OVIDIUS DIN CONSTANTA', 'CONSTANTA', '1990', 'DE STAT')")
                        db.execSQL("INSERT INTO `University` (`name`, `location`, `foundationDate`, `type`) VALUES ('UNIVERSITATEA DE VEST DIN TIMISOARA', 'TIMISOARA', '1944', 'DE STAT')")
                        db.execSQL("INSERT INTO `University` (`name`, `location`, `foundationDate`, `type`) VALUES ('UNIVERSITATEA LUCIAN BLAGA DIN SIBIU', 'SIBIU', '1990', 'DE STAT')")
                        db.execSQL("INSERT INTO `University` (`name`, `location`, `foundationDate`, `type`) VALUES ('UNIVERSITATEA ROMANO-AMERICANA', 'BUCURESTI', '1991', 'PRIVATA')")
                    }
                })
                    .addMigrations(VersionMigration())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
