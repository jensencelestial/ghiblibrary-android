package com.jensencelestial.ghiblibrary.android.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jensencelestial.ghiblibrary.android.data.model.Film

@Database(
    entities = [
        Film::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}