package com.jensencelestial.ghiblibrary.android.data.network

import com.jensencelestial.ghiblibrary.android.data.model.api.FilmResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FilmService {
    @GET("films")
    suspend fun getFilms(): Response<List<FilmResponse>>

    @GET("films/{id}")
    suspend fun getFilm(@Path("id") filmId: String): Response<FilmResponse>
}