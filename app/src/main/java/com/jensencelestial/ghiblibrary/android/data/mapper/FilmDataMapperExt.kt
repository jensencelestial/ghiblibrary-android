package com.jensencelestial.ghiblibrary.android.data.mapper

import com.jensencelestial.ghiblibrary.android.data.model.Film
import com.jensencelestial.ghiblibrary.android.data.model.api.FilmResponse

fun FilmResponse.toEntity(): Film {
    return Film(
        id = id,
        title = title,
        description = description,
        director = director,
        producer = producer,
        releaseDate = releaseDate,
        rtScore = rtScore
    )
}

fun List<FilmResponse>.toEntities(): List<Film> {
    return this.map {
        it.toEntity()
    }
}