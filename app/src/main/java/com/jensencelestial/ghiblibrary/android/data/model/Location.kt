package com.jensencelestial.ghiblibrary.android.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class Location(
    @PrimaryKey
    val id: String,

    val name: String,

    val climate: String,

    val terrain: String,

    @ColumnInfo(name = "surface_water")
    val surfaceWater: String,

    var imageUrl: String? = null,
)