package com.jensencelestial.ghiblibrary.android.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jensencelestial.ghiblibrary.android.data.model.Location

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: Location)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocations(people: List<Location>)

    @Query("SELECT * FROM location")
    suspend fun getAllLocations(): List<Location>

    @Query("SELECT * FROM location WHERE id = :locationId")
    suspend fun getLocation(locationId: String): Location
}