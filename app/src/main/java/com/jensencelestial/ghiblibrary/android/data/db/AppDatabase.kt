package com.jensencelestial.ghiblibrary.android.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jensencelestial.ghiblibrary.android.data.model.*

@Database(
    entities = [
        Film::class,
        Location::class,
        Person::class,
        Species::class,
        Vehicle::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao

    abstract fun locationDao(): LocationDao

    abstract fun peopleDao(): PersonDao

    abstract fun speciesDao(): SpeciesDao

    abstract fun vehicleDao(): VehicleDao
}