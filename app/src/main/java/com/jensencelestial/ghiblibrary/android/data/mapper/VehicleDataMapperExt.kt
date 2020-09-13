package com.jensencelestial.ghiblibrary.android.data.mapper

import com.jensencelestial.ghiblibrary.android.data.model.Vehicle
import com.jensencelestial.ghiblibrary.android.data.model.api.VehicleResponse

fun VehicleResponse.toEntity(): Vehicle {
    return Vehicle(
        id = id,
        name = name,
        description = description,
        vehicleClass = vehicleClass,
        length = length,
    )
}

fun List<VehicleResponse>.toEntities(): List<Vehicle> {
    return this.map {
        it.toEntity()
    }
}