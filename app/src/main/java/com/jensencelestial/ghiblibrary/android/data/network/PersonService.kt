package com.jensencelestial.ghiblibrary.android.data.network

import com.jensencelestial.ghiblibrary.android.data.model.api.PersonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PersonService {
    @GET("people")
    suspend fun getPeople(): Response<List<PersonResponse>>

    @GET("people/{id}")
    suspend fun getPerson(@Path("id") personId: String): Response<PersonResponse>
}