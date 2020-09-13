package com.jensencelestial.ghiblibrary.android.data.network

import com.jensencelestial.ghiblibrary.android.data.model.api.VehicleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface VehicleService {
    @GET("vehicles")
    suspend fun getVehicles(): Response<List<VehicleResponse>>

    @GET("vehicles/{id}")
    suspend fun getVehicle(@Path("id") vehicleId: String): Response<VehicleResponse>
}