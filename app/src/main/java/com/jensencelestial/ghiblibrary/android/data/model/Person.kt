package com.jensencelestial.ghiblibrary.android.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person")
data class Person(
    @PrimaryKey
    val id: String,

    val name: String,

    val gender: String,

    val age: String,

    @ColumnInfo(name = "eye_color")
    val eyeColor: String,

    @ColumnInfo(name = "hair_color")
    val hairColor: String,

    var imageUrl: String? = null,
)