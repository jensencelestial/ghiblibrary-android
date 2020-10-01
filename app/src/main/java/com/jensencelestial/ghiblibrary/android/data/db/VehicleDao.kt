package com.jensencelestial.ghiblibrary.android.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jensencelestial.ghiblibrary.android.data.model.Vehicle

@Dao
interface VehicleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: Vehicle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicles(vehicles: List<Vehicle>)

    @Query("SELECT * FROM vehicle")
    suspend fun getAllVehicles(): List<Vehicle>

    @Query("SELECT * FROM vehicle WHERE id = :vehicleId")
    suspend fun getVehicle(vehicleId: String): Vehicle

}