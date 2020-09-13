package com.jensencelestial.ghiblibrary.android.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle")
data class Vehicle(
    @PrimaryKey
    val id: String,

    val name: String,

    val description: String,

    @ColumnInfo(name = "vehicle_class")
    val vehicleClass: String,

    val length: String? = null,
    
    var imageUrl: String? = null,
)