package com.jensencelestial.ghiblibrary.android.data.network

import com.jensencelestial.ghiblibrary.android.data.model.api.SpeciesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SpeciesService {
    @GET("species")
    suspend fun getSpecies(): Response<List<SpeciesResponse>>

    @GET("species/{id}")
    suspend fun getSpecies(@Path("id") speciesId: String): Response<SpeciesResponse>
}