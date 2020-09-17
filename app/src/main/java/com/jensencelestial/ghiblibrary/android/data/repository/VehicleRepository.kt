package com.jensencelestial.ghiblibrary.android.data.repository

import com.jensencelestial.ghiblibrary.android.data.db.VehicleDao
import com.jensencelestial.ghiblibrary.android.data.mapper.toEntities
import com.jensencelestial.ghiblibrary.android.data.mapper.toEntity
import com.jensencelestial.ghiblibrary.android.data.model.Vehicle
import com.jensencelestial.ghiblibrary.android.data.model.api.VehicleResponse
import com.jensencelestial.ghiblibrary.android.data.network.VehicleService
import com.jensencelestial.ghiblibrary.android.data.repository.result.RepResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class VehicleRepository @Inject constructor(
    private val vehicleDao: VehicleDao,
    private val vehicleService: VehicleService
) {

    suspend fun getVehicles(): RepResult<List<Vehicle>> {
        return try {
            val remoteVehiclesResponse: Response<List<VehicleResponse>> =
                vehicleService.getVehicles()

            if (remoteVehiclesResponse.isSuccessful) {
                var vehiclesToSave: List<Vehicle> =
                    remoteVehiclesResponse.body()?.toEntities() ?: mutableListOf()

                vehiclesToSave = attachImageUrls(*vehiclesToSave.toTypedArray())

                vehicleDao.insertVehicles(vehiclesToSave)

                RepResult.Success(result = vehicleDao.getAllVehicles())
            } else {
                RepResult.Error<Nothing>()
            }
        } catch (e: Exception) {
            Timber.e(e, "Get Vehicles from API failed.")

            when (e) {
                is SocketTimeoutException, is IOException -> {
                    // For timeout errors, get Vehicles from DB.
                    return try {
                        RepResult.Success(result = vehicleDao.getAllVehicles())
                    } catch (dbe: Exception) {
                        Timber.e(e, "Get Vehicles from DB failed.")

                        RepResult.Error(exception = dbe)
                    }
                }
                else -> RepResult.Error(exception = e)
            }
        }
    }

    suspend fun getVehicle(vehicleId: String): RepResult<Vehicle> {
        return try {
            val remoteVehicleResponse: Response<VehicleResponse> =
                vehicleService.getVehicle(vehicleId)

            if (remoteVehicleResponse.isSuccessful) {
                var vehicleToSave: Vehicle? = remoteVehicleResponse.body()?.toEntity()

                if (vehicleToSave != null) {
                    vehicleToSave = attachImageUrls(vehicleToSave)[0]

                    vehicleDao.insertVehicle(vehicleToSave)
                }

                RepResult.Success(result = vehicleDao.getVehicle(vehicleId))
            } else {
                RepResult.Error<Nothing>()
            }
        } catch (e: Exception) {
            Timber.e(e, "Get Vehicle from API failed.")

            when (e) {
                is SocketTimeoutException, is IOException -> {
                    // For timeout errors, get Vehicle from DB.
                    return try {
                        RepResult.Success(result = vehicleDao.getVehicle(vehicleId))
                    } catch (dbe: Exception) {
                        Timber.e(e, "Get Vehicle from DB failed.")

                        RepResult.Error(exception = dbe)
                    }
                }
                else -> RepResult.Error(exception = e)
            }
        }
    }

    private suspend fun attachImageUrls(vararg vehicles: Vehicle): List<Vehicle> {
        withContext(Dispatchers.Default) {
            vehicles.forEach { vehicle: Vehicle ->
                vehicle.imageUrl = when (vehicle.id) {
                    "4e09b023-f650-4747-9ab9-eacf14540cfb" -> {
                        "https://vignette4.wikia.nocookie.net/studio-ghibli/images/e/e5/Goliath.png/revision/latest?cb=20140118142031"
                    }
                    "d8f893b5-1dd9-41a1-9918-0099c1aa2de8" -> {
                        "https://vignette2.wikia.nocookie.net/studio-ghibli/images/0/0f/Porcos_r%C3%B6da_flygplan.jpg/revision/latest?cb=20131019112228"
                    }
                    "923d70c9-8f15-4972-ad53-0128b261d628" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/0/02/Ponyo_and_Sosuke_in_boat.jpg/revision/latest?cb=20130119193102"
                    }
                    else -> ""
                }
            }
        }

        return vehicles.toList()
    }
}