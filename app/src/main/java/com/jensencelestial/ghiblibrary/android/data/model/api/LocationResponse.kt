package com.jensencelestial.ghiblibrary.android.data.model.api

import com.squareup.moshi.Json

data class LocationResponse(
    val id: String,

    val name: String,

    val climate: String,

    val terrain: String,

    @Json(name = "surface_water")
    val surfaceWater: String,
)