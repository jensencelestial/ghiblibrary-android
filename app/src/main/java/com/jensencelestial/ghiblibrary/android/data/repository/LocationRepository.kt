package com.jensencelestial.ghiblibrary.android.data.repository

import com.jensencelestial.ghiblibrary.android.data.db.LocationDao
import com.jensencelestial.ghiblibrary.android.data.mapper.toEntities
import com.jensencelestial.ghiblibrary.android.data.mapper.toEntity
import com.jensencelestial.ghiblibrary.android.data.model.Location
import com.jensencelestial.ghiblibrary.android.data.model.api.LocationResponse
import com.jensencelestial.ghiblibrary.android.data.network.LocationService
import com.jensencelestial.ghiblibrary.android.data.repository.result.RepResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationDao: LocationDao,
    private val locationService: LocationService
) {

    suspend fun getLocations(): RepResult<List<Location>> {
        return try {
            val remoteLocationsResponse: Response<List<LocationResponse>> =
                locationService.getLocations()

            if (remoteLocationsResponse.isSuccessful) {
                var locationsToSave: List<Location> =
                    remoteLocationsResponse.body()?.toEntities() ?: mutableListOf()

                locationsToSave = attachImageUrls(*locationsToSave.toTypedArray())

                locationDao.insertLocations(locationsToSave)

                RepResult.Success(result = locationDao.getAllLocations())
            } else {
                RepResult.Error<Nothing>()
            }
        } catch (e: Exception) {
            Timber.e(e, "Get Locations from API failed.")

            when (e) {
                is SocketTimeoutException, is IOException -> {
                    // For timeout errors, get Locations from DB.
                    return try {
                        RepResult.Success(result = locationDao.getAllLocations())
                    } catch (dbe: Exception) {
                        Timber.e(e, "Get Locations from DB failed.")

                        RepResult.Error(exception = dbe)
                    }
                }
                else -> RepResult.Error(exception = e)
            }
        }
    }

    suspend fun getLocation(locationId: String): RepResult<Location> {
        return try {
            val remoteLocationResponse: Response<LocationResponse> =
                locationService.getLocation(locationId)

            if (remoteLocationResponse.isSuccessful) {
                var locationToSave: Location? = remoteLocationResponse.body()?.toEntity()

                if (locationToSave != null) {
                    locationToSave = attachImageUrls(locationToSave)[0]

                    locationDao.insertLocation(locationToSave)
                }

                RepResult.Success(result = locationDao.getLocation(locationId))
            } else {
                RepResult.Error<Nothing>()
            }
        } catch (e: Exception) {
            Timber.e(e, "Get Location from API failed.")

            when (e) {
                is SocketTimeoutException, is IOException -> {
                    // For timeout errors, get Location from DB.
                    return try {
                        RepResult.Success(result = locationDao.getLocation(locationId))
                    } catch (dbe: Exception) {
                        Timber.e(e, "Get Location from DB failed.")

                        RepResult.Error(exception = dbe)
                    }
                }
                else -> RepResult.Error(exception = e)
            }
        }
    }

    private suspend fun attachImageUrls(vararg locations: Location): List<Location> {
        withContext(Dispatchers.Default) {
            locations.forEach { location: Location ->
                location.imageUrl = when (location.id) {
                    "af3910a6-429f-4c74-9ad5-dfe1c4aa04f2" -> {
                        "https://vignette3.wikia.nocookie.net/studio-ghibli/images/b/b3/Ponyo.jpg/revision/latest?cb=20160526141617"
                    }
                    else -> ""
                }
            }
        }

        return locations.toList()
    }
}