package com.jensencelestial.ghiblibrary.android.data.mapper

import com.jensencelestial.ghiblibrary.android.data.model.Person
import com.jensencelestial.ghiblibrary.android.data.model.api.PersonResponse

fun PersonResponse.toEntity(): Person {
    return Person(
        id = id,
        name = name,
        gender = gender,
        age = age,
        eyeColor = eyeColor,
        hairColor = hairColor,
    )
}

fun List<PersonResponse>.toEntities(): List<Person> {
    return this.map {
        it.toEntity()
    }
}