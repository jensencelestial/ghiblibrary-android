package com.jensencelestial.ghiblibrary.android.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "species")
data class Species(
    @PrimaryKey
    val id: String,

    val name: String,

    val classification: String,

    @ColumnInfo(name = "eye_colors")
    val eyeColors: String,

    @ColumnInfo(name = "hair_colors")
    val hairColors: String,

    var imageUrl: String? = null,
)