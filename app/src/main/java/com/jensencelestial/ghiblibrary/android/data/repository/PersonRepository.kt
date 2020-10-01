package com.jensencelestial.ghiblibrary.android.data.repository

import com.jensencelestial.ghiblibrary.android.data.db.PersonDao
import com.jensencelestial.ghiblibrary.android.data.mapper.toEntities
import com.jensencelestial.ghiblibrary.android.data.mapper.toEntity
import com.jensencelestial.ghiblibrary.android.data.model.Person
import com.jensencelestial.ghiblibrary.android.data.model.api.PersonResponse
import com.jensencelestial.ghiblibrary.android.data.network.PersonService
import com.jensencelestial.ghiblibrary.android.data.repository.result.RepResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class PersonRepository @Inject constructor(
    private val peopleDao: PersonDao,
    private val personService: PersonService
) {

    suspend fun getPeople(): RepResult<List<Person>> {
        return try {
            val remotePersonResponse: Response<List<PersonResponse>> = personService.getPeople()

            if (remotePersonResponse.isSuccessful) {
                var peopleToSave: List<Person> =
                    remotePersonResponse.body()?.toEntities() ?: mutableListOf()

                peopleToSave = attachImageUrls(*peopleToSave.toTypedArray())

                peopleDao.insertPeople(peopleToSave)

                RepResult.Success(result = peopleDao.getAllPeople())
            } else {
                RepResult.Error<Nothing>()
            }
        } catch (e: Exception) {
            Timber.e(e, "Get People from API failed.")

            when (e) {
                is SocketTimeoutException, is IOException -> {
                    // For timeout errors, get People from DB.
                    return try {
                        RepResult.Success(result = peopleDao.getAllPeople())
                    } catch (dbe: Exception) {
                        Timber.e(e, "Get People from DB failed.")

                        RepResult.Error(exception = dbe)
                    }
                }
                else -> RepResult.Error(exception = e)
            }
        }
    }

    suspend fun getPerson(personId: String): RepResult<Person> {
        return try {
            val remotePersonResponse: Response<PersonResponse> = personService.getPerson(personId)

            if (remotePersonResponse.isSuccessful) {
                var personToSave: Person? = remotePersonResponse.body()?.toEntity()

                if (personToSave != null) {
                    personToSave = attachImageUrls(personToSave)[0]

                    peopleDao.insertPerson(personToSave)
                }

                RepResult.Success(result = peopleDao.getPerson(personId))
            } else {
                RepResult.Error<Nothing>()
            }
        } catch (e: Exception) {
            Timber.e(e, "Get Person from API failed.")

            when (e) {
                is SocketTimeoutException, is IOException -> {
                    // For timeout errors, get Person from DB.
                    return try {
                        RepResult.Success(result = peopleDao.getPerson(personId))
                    } catch (dbe: Exception) {
                        Timber.e(e, "Get Person from DB failed.")

                        RepResult.Error(exception = dbe)
                    }
                }
                else -> RepResult.Error(exception = e)
            }
        }
    }

    private suspend fun attachImageUrls(vararg people: Person): List<Person> {
        withContext(Dispatchers.Default) {
            people.forEach { people: Person ->
                people.imageUrl = when (people.id) {
                    "ba924631-068e-4436-b6de-f3283fa848f0" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/4/49/Ashitaka.jpg/revision/latest?cb=20171215144249"
                    }
                    "ebe40383-aad2-4208-90ab-698f00c581ab" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/8/8b/Sanny.jpg/revision/latest?cb=20140101182911"
                    }
                    "34277bec-7401-43fa-a00a-5aee64b45b08" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/9/9f/Eboshi.png/revision/latest?cb=20110704192003"
                    }
                    "91939012-90b9-46e5-a649-96b898073c82" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/7/72/Jigo.jpg/revision/latest?cb=20161218114329"
                    }
                    "20e3bd33-b35d-41e6-83a4-57ca7f028d38" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/5/5c/Kohroku.jpg/revision/latest?cb=20140222170843"
                    }
                    "8bccdc78-545b-49f4-a4c8-756163a38c91" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/7/7d/Gonza.jpg/revision/latest?cb=20130623073032"
                    }
                    "116bfe1b-3ba8-4fa0-8f72-88537a493cb9" -> {
                        "https://vignette.wikia.nocookie.net/ghibli/images/8/81/Hii.jpg/revision/latest?cb=20101207205331&path-prefix=de"
                    }
                    "030555b3-4c92-4fce-93fb-e70c3ae3df8b" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/e/ef/Yakul.jpg/revision/latest?cb=20170312103123"
                    }
                    "ca568e87-4ce2-4afa-a6c5-51f4ae80a60b" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/b/b7/Forest_Spirit.jpg/revision/latest?cb=20131222092309"
                    }
                    "e9356bb5-4d4a-4c93-aadc-c83e514bffe3" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/5/5e/Moro.png/revision/latest?cb=20110702021519"
                    }
                    "7151abc6-1a9e-4e6a-9711-ddb50ea572ec" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/3/31/Kiki%27s_delivery_service_jiji.png/revision/latest?cb=20140419012421"
                    }
                    "986faac6-67e3-4fb8-a9ee-bad077c2e7fe" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/1/10/This_one%27s_for_you_mei.gif/revision/latest?cb=20160401023649"
                    }
                    "d5df3c04-f355-4038-833c-83bd3502b6b9" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/c/c9/Mei.jpeg/revision/latest?cb=20170121144048"
                    }
                    "3031caa8-eb1a-41c6-ab93-dd091b541e11" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/e/e4/07.jpg/revision/latest?cb=20131027200928"
                    }
                    "87b68b97-3774-495b-bf80-495a5f3e672d" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/2/28/Mrs.Kusakabe.png/revision/latest?cb=20110705210532"
                    }
                    "08ffbce4-7f94-476a-95bc-76d3c3969c19" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/d/d9/Granny.png/revision/latest?cb=20130124201437"
                    }
                    "0f8ef701-b4c7-4f15-bd15-368c7fe38d0a" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/c/c3/Kanta.jpg/revision/latest?cb=20130614154117"
                    }
                    "d39deecb-2bd0-4770-8b45-485f26e1381f" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/5/5f/Bluetotoro.png/revision/latest?cb=20150918181559"
                    }
                    "591524bc-04fe-4e60-8d61-2425e42ffb2a" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/d/db/Bl%C3%A5_Totoro.jpg/revision/latest?cb=20130401154121"
                    }
                    "c491755a-407d-4d6e-b58a-240ec78b5061" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/d/db/Bl%C3%A5_Totoro.jpg/revision/latest?cb=20130401154121"
                    }
                    "f467e18e-3694-409f-bdb3-be891ade1106" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/3/30/Catbus.jpg/revision/latest?cb=20130127095152"
                    }
                    "89026b3a-abc4-4053-ab1a-c6d2eea68faa" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/5/5a/Niya.jpg/revision/latest?cb=20131010121045"
                    }
                    "6b3facea-ea33-47b1-96ce-3fc737b119b8" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/6/6f/Muta.jpg/revision/latest?cb=20151128100340"
                    }
                    "3042818d-a8bb-4cba-8180-c19249822d57" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/3/31/Kattkungen.jpg/revision/latest?cb=20130304183749"
                    }
                    "58d1973f-f247-47d7-9358-e56cb0d2b5a6" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/e/eb/New_queen%2C_Yuki.JPG/revision/latest?cb=20170813030008"
                    }
                    "a3d8e70f-46a0-4e5a-b850-db01620d6b92" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/3/3d/Haru_%28dam%29.jpg/revision/latest?cb=20130219155133"
                    }
                    "fc196c4f-0201-4ed2-9add-c6403f7c4d32" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/7/73/Baron.jpg/revision/latest?cb=20150124102412"
                    }
                    "466bc926-2024-4653-ac63-fe52f2dc8c7b" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/c/c4/Natori.jpg/revision/latest?cb=20150905140608"
                    }
                    "40c005ce-3725-4f15-8409-3e1b1b14b583" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/d/d5/Muska.jpg/revision/latest?cb=20130424141851"
                    }
                    "6523068d-f5a9-4150-bf5b-76abe6fb42c3" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/f/fe/Porco.jpg/revision/latest?cb=20161129072722"
                    }
                    "a10f64f3-e0b6-4a94-bf30-87ad8bc51607" -> {
                        "https://vignette.wikia.nocookie.net/studio-ghibli/images/4/46/Sosuke.png/revision/latest?cb=20110730154019"
                    }
                    else -> ""
                }
            }
        }

        return people.toList()
    }
}