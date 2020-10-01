package com.jensencelestial.ghiblibrary.android.data.network

import com.jensencelestial.ghiblibrary.android.data.model.api.LocationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LocationService {
    @GET("locations")
    suspend fun getLocations(): Response<List<LocationResponse>>

    @GET("locations/{id}")
    suspend fun getLocation(@Path("id") locationId: String): Response<LocationResponse>
}