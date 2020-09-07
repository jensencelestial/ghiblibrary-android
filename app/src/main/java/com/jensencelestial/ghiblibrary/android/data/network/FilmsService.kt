package com.jensencelestial.ghiblibrary.android.data.network

import com.jensencelestial.ghiblibrary.android.data.model.api.FilmResponse
import retrofit2.Response
import retrofit2.http.GET

interface FilmsService {
    @GET("films")
    suspend fun getFilms(): Response<List<FilmResponse>>
}