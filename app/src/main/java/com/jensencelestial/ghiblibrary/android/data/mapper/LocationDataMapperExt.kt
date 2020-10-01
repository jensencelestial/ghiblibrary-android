package com.jensencelestial.ghiblibrary.android.data.mapper

import com.jensencelestial.ghiblibrary.android.data.model.Location
import com.jensencelestial.ghiblibrary.android.data.model.api.LocationResponse

fun LocationResponse.toEntity(): Location {
    return Location(
        id = id,
        name = name,
        climate = climate,
        terrain = terrain,
        surfaceWater = surfaceWater,
    )
}

fun List<LocationResponse>.toEntities(): List<Location> {
    return this.map {
        it.toEntity()
    }
}