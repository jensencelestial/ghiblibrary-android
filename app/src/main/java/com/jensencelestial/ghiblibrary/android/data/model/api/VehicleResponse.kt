package com.jensencelestial.ghiblibrary.android.data.model.api

import com.squareup.moshi.Json

data class VehicleResponse(
    val id: String,

    val name: String,

    val description: String,

    @Json(name = "vehicle_class")
    val vehicleClass: String,

    val length: String? = null
)