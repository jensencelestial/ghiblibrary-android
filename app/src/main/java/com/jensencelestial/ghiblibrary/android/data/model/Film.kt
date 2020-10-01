package com.jensencelestial.ghiblibrary.android.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "film")
data class Film(
    @PrimaryKey
    val id: String,

    val title: String,

    val description: String,

    val director: String,

    val producer: String,

    @ColumnInfo(name = "release_date")
    val releaseDate: String,

    @ColumnInfo(name = "rt_score")
    val rtScore: String,
    
    var imageUrl: String? = null,
)