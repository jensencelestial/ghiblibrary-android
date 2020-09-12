package com.jensencelestial.ghiblibrary.android.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jensencelestial.ghiblibrary.android.data.model.Film
import com.jensencelestial.ghiblibrary.android.data.model.Location
import com.jensencelestial.ghiblibrary.android.data.model.Person
import com.jensencelestial.ghiblibrary.android.data.model.Species

@Database(
    entities = [
        Film::class,
        Location::class,
        Person::class,
        Species::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao

    abstract fun locationDao(): LocationDao

    abstract fun peopleDao(): PersonDao

    abstract fun speciesDao(): SpeciesDao
}