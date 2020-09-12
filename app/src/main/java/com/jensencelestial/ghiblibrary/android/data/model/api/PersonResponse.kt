package com.jensencelestial.ghiblibrary.android.data.model.api

import com.squareup.moshi.Json

data class PersonResponse(
    val id: String,

    val name: String,

    val gender: String,

    val age: String,

    @Json(name = "eye_color")
    val eyeColor: String,

    @Json(name = "hair_color")
    val hairColor: String
)