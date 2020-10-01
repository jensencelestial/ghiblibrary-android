package com.jensencelestial.ghiblibrary.android.data.repository

import com.jensencelestial.ghiblibrary.android.data.db.SpeciesDao
import com.jensencelestial.ghiblibrary.android.data.mapper.toEntities
import com.jensencelestial.ghiblibrary.android.data.mapper.toEntity
import com.jensencelestial.ghiblibrary.android.data.model.Species
import com.jensencelestial.ghiblibrary.android.data.model.api.SpeciesResponse
import com.jensencelestial.ghiblibrary.android.data.network.SpeciesService
import com.jensencelestial.ghiblibrary.android.data.repository.result.RepResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class SpeciesRepository @Inject constructor(
    private val speciesDao: SpeciesDao,
    private val speciesService: SpeciesService
) {

    suspend fun getSpecies(): RepResult<List<Species>> {
        return try {
            val remoteSpeciesResponse: Response<List<SpeciesResponse>> = speciesService.getSpecies()

            if (remoteSpeciesResponse.isSuccessful) {
                var speciesToSave: List<Species> =
                    remoteSpeciesResponse.body()?.toEntities() ?: mutableListOf()

                speciesToSave = attachImageUrls(*speciesToSave.toTypedArray())

                speciesDao.insertSpecies(speciesToSave)

                RepResult.Success(result = speciesDao.getAllSpecies())
            } else {
                RepResult.Error<Nothing>()
            }
        } catch (e: Exception) {
            Timber.e(e, "Get Species from API failed.")

            when (e) {
                is SocketTimeoutException, is IOException -> {
                    // For timeout errors, get Species from DB.
                    return try {
                        RepResult.Success(result = speciesDao.getAllSpecies())
                    } catch (dbe: Exception) {
                        Timber.e(e, "Get Species from DB failed.")

                        RepResult.Error(exception = dbe)
                    }
                }
                else -> RepResult.Error(exception = e)
            }
        }
    }

    suspend fun getSpecies(speciesId: String): RepResult<Species> {
        return try {
            val remoteSpeciesResponse: Response<SpeciesResponse> =
                speciesService.getSpecies(speciesId)

            if (remoteSpeciesResponse.isSuccessful) {
                var speciesToSave: Species? = remoteSpeciesResponse.body()?.toEntity()

                if (speciesToSave != null) {
                    speciesToSave = attachImageUrls(speciesToSave)[0]

                    speciesDao.insertSpecies(speciesToSave)
                }

                RepResult.Success(result = speciesDao.getSpecies(speciesId))
            } else {
                RepResult.Error<Nothing>()
            }
        } catch (e: Exception) {
            Timber.e(e, "Get Species from API failed.")

            when (e) {
                is SocketTimeoutException, is IOException -> {
                    // For timeout errors, get Species from DB.
                    return try {
                        RepResult.Success(result = speciesDao.getSpecies(speciesId))
                    } catch (dbe: Exception) {
                        Timber.e(e, "Get Species from DB failed.")

                        RepResult.Error(exception = dbe)
                    }
                }
                else -> RepResult.Error(exception = e)
            }
        }
    }

    private suspend fun attachImageUrls(vararg species: Species): List<Species> {
        withContext(Dispatchers.Default) {
            species.forEach { sp: Species ->
                sp.imageUrl = when (sp.id) {
                    "af3910a6-429f-4c74-9ad5-dfe1c4aa04f2" -> {
                        "https://vignette3.wikia.nocookie.net/studio-ghibli/images/b/b3/Ponyo.jpg/revision/latest?cb=20160526141617"
                    }
                    "6bc92fdd-b0f4-4286-ad71-1f99fb4a0d1e" -> {
                        "http://vignette2.wikia.nocookie.net/studio-ghibli/images/8/85/Skogsande.png/revision/latest?cb=20130628172610"
                    }
                    "b5a92d0e-5fb4-43d4-ba60-c012135958e4" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/9/98/Kodama.jpg/revision/latest?cb=20130323094805"
                    }
                    "f25fa661-3073-414d-968a-ab062e3065f7" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/5/53/Nightwalker_of_Mononoke.jpg/revision/latest?cb=20171121125158"
                    }
                    "603428ba-8a86-4b0b-a9f1-65df6abef3d3" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/0/0a/Lily.png/revision/latest/scale-to-width-down/350?cb=20110716004647"
                    }
                    "74b7f547-1577-4430-806c-c358c8b6bcf5" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/5/53/Totoro.jpg/revision/latest/scale-to-width-down/350?cb=20171224152530"
                    }
                    else -> ""
                }
            }
        }

        return species.toList()
    }
}