package com.jensencelestial.ghiblibrary.android.data.model.api

import com.squareup.moshi.Json

data class FilmResponse(
    val id: String,

    val title: String,

    val description: String,

    val director: String,

    val producer: String,

    @Json(name = "release_date")
    val releaseDate: String,

    @Json(name = "rt_score")
    val rtScore: String
)