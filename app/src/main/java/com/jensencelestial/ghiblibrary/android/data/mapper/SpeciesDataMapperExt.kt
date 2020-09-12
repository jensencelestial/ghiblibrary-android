package com.jensencelestial.ghiblibrary.android.data.mapper

import com.jensencelestial.ghiblibrary.android.data.model.Species
import com.jensencelestial.ghiblibrary.android.data.model.api.SpeciesResponse

fun SpeciesResponse.toEntity(): Species {
    return Species(
        id = id,
        name = name,
        classification = classification,
        eyeColors = eyeColors,
        hairColors = hairColors,
    )
}

fun List<SpeciesResponse>.toEntities(): List<Species> {
    return this.map {
        it.toEntity()
    }
}