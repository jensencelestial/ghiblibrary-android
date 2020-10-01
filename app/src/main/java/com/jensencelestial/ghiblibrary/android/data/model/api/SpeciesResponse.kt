package com.jensencelestial.ghiblibrary.android.data.model.api

import com.squareup.moshi.Json

data class SpeciesResponse(
    val id: String,

    val name: String,

    val classification: String,

    @Json(name = "eye_colors")
    val eyeColors: String,

    @Json(name = "hair_colors")
    val hairColors: String
)