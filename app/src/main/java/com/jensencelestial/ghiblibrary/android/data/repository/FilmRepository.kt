package com.jensencelestial.ghiblibrary.android.data.repository

import com.jensencelestial.ghiblibrary.android.data.db.FilmDao
import com.jensencelestial.ghiblibrary.android.data.mapper.toEntities
import com.jensencelestial.ghiblibrary.android.data.mapper.toEntity
import com.jensencelestial.ghiblibrary.android.data.model.Film
import com.jensencelestial.ghiblibrary.android.data.model.api.FilmResponse
import com.jensencelestial.ghiblibrary.android.data.network.FilmService
import com.jensencelestial.ghiblibrary.android.data.repository.result.RepResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class FilmRepository @Inject constructor(
    private val filmDao: FilmDao,
    private val filmService: FilmService
) {

    suspend fun getFilms(): RepResult<List<Film>> {
        return try {
            val remoteFilmsResponse: Response<List<FilmResponse>> = filmService.getFilms()

            if (remoteFilmsResponse.isSuccessful) {
                var filmsToSave: List<Film> =
                    remoteFilmsResponse.body()?.toEntities() ?: mutableListOf()

                filmsToSave = attachImageUrls(*filmsToSave.toTypedArray())

                filmDao.insertFilms(filmsToSave)

                RepResult.Success(result = filmDao.getAllFilms())
            } else {
                RepResult.Error<Nothing>()
            }
        } catch (e: Exception) {
            Timber.e(e, "Get Films from API failed.")

            when (e) {
                is SocketTimeoutException, is IOException -> {
                    // For timeout errors, get Films from DB.
                    return try {
                        RepResult.Success(result = filmDao.getAllFilms())
                    } catch (dbe: Exception) {
                        Timber.e(e, "Get Films from DB failed.")

                        RepResult.Error(exception = dbe)
                    }
                }
                else -> RepResult.Error(exception = e)
            }
        }
    }

    suspend fun getFilm(filmId: String): RepResult<Film> {
        return try {
            val remoteFilmResponse: Response<FilmResponse> = filmService.getFilm(filmId)

            if (remoteFilmResponse.isSuccessful) {
                var filmToSave: Film? = remoteFilmResponse.body()?.toEntity()

                if (filmToSave != null) {
                    filmToSave = attachImageUrls(filmToSave)[0]

                    filmDao.insertFilm(filmToSave)
                }

                RepResult.Success(result = filmDao.getFilm(filmId))
            } else {
                RepResult.Error<Nothing>()
            }
        } catch (e: Exception) {
            Timber.e(e, "Get Films from API failed.")

            when (e) {
                is SocketTimeoutException, is IOException -> {
                    // For timeout errors, get Film from DB.
                    return try {
                        RepResult.Success(result = filmDao.getFilm(filmId))
                    } catch (dbe: Exception) {
                        Timber.e(e, "Get Films from DB failed.")

                        RepResult.Error(exception = dbe)
                    }
                }
                else -> RepResult.Error(exception = e)
            }
        }
    }

    private suspend fun attachImageUrls(vararg films: Film): List<Film> {
        withContext(Dispatchers.Default) {
            films.forEach { film: Film ->
                film.imageUrl = when (film.id) {
                    "2baf70d1-42bb-4437-b551-e5fed5a87abe" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BNTg0NmI1ZGQtZTUxNC00NTgxLThjMDUtZmRlYmEzM2MwOWYwXkEyXkFqcGdeQXVyMzM4MjM0Nzg@._V1_UY268_CR2,0,182,268_AL_.jpg"
                    }
                    "12cfb892-aac0-4c5b-94af-521852e46d6a" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BZjEwNDVhZjMtYzA1MS00ZWUxLThjOGUtZTliNGZiNGYyMjA3XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_UY268_CR4,0,182,268_AL_.jpg"
                    }
                    "58611129-2dbc-4a81-a72f-77ddfc1b1b49" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BNTdiOTQ0YmUtOGE3YS00NDg5LWI3YTEtNDAxZmE0MzRmZWM5L2ltYWdlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_UY268_CR6,0,182,268_AL_.jpg"
                    }
                    "ea660b10-85c4-4ae3-8a5f-41cea3648e3e" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BOTc0ODM1Njk1NF5BMl5BanBnXkFtZTcwMDI5OTEyNw@@._V1_UY268_CR3,0,182,268_AL_.jpg"
                    }
                    "4e236f34-b981-41c3-8c65-f8c9000b94e7" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BMTY5NjI2MjQxMl5BMl5BanBnXkFtZTgwMDA2MzM2NzE@._V1_UY268_CR0,0,182,268_AL_.jpg"
                    }
                    "ebbb6b7c-945c-41ee-a792-de0e43191bd8" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BZDIyOTBiZjktYTE0NS00ZGE2LWEzM2YtMzM0MWI2YzIzMGM2L2ltYWdlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_UY268_CR3,0,182,268_AL_.jpg"
                    }
                    "1b67aa9a-2e4a-45af-ac98-64d6ad15b16c" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BNDM3MDc3OTk4MF5BMl5BanBnXkFtZTcwMzQ2ODIyNw@@._V1_UY268_CR3,0,182,268_AL_.jpg"
                    }
                    "ff24da26-a969-4f0e-ba1e-a122ead6c6e3" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BNmQ3N2U5NGMtNjU0MS00YTQzLWE1ZDctZDU5M2M5NTNjOGRmXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_UY268_CR3,0,182,268_AL_.jpg"
                    }
                    "0440483e-ca0e-4120-8c50-4c8cd9b965d6" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BMTVlNWM4NTAtNDQxYi00YWU5LWIwM2MtZmVjYWFmODZiODE5XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_UY268_CR4,0,182,268_AL_.jpg"
                    }
                    "45204234-adfd-45cb-a505-a8e7a676b114" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BOThkZjMyMGYtMDNjNy00NjcwLTk1NmEtZmQwYTliMmM4YjBhXkEyXkFqcGdeQXVyMzM4NjcxOTc@._V1_UY268_CR3,0,182,268_AL_.jpg"
                    }
                    "dc2e6bd1-8156-4886-adff-b39e6043af0c" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BOGJjNzZmMmUtMjljNC00ZjU5LWJiODQtZmEzZTU0MjBlNzgxL2ltYWdlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_UX182_CR0,0,182,268_AL_.jpg"
                    }
                    "90b72513-afd4-4570-84de-a56c312fdf81" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BMTQ5ODMyNTgzOV5BMl5BanBnXkFtZTcwNDM4ODAyNw@@._V1_UY268_CR4,0,182,268_AL_.jpg"
                    }
                    "cd3d059c-09f4-4ff3-8d63-bc765a5184fa" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BZTRhY2QwM2UtNWRlNy00ZWQwLTg3MjktZThmNjQ3NTdjN2IxXkEyXkFqcGdeQXVyMzg2MzE2OTE@._V1_UY268_CR5,0,182,268_AL_.jpg"
                    }
                    "112c1e67-726f-40b1-ac17-6974127bb9b9" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BMTExODA0NzkxOTZeQTJeQWpwZ15BbWU3MDM0NDEyNzM@._V1_UX182_CR0,0,182,268_AL_.jpg"
                    }
                    "758bf02e-3122-46e0-884e-67cf83df1786" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BMjA5NzkxNTg2MF5BMl5BanBnXkFtZTcwMTA3MjU1Mg@@._V1_UX182_CR0,0,182,268_AL_.jpg"
                    }
                    "2de9426b-914a-4a06-a3a0-5e6d9d3886f6" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BMTAxNjk3OTYyODReQTJeQWpwZ15BbWU3MDgyODY2OTY@._V1_UX182_CR0,0,182,268_AL_.jpg"
                    }
                    "45db04e4-304a-4933-9823-33f389e8d74d" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BYWFiYjE0NTctZThiZC00NTYxLTllOWQtYmMyYzY4NWZiZDYyXkEyXkFqcGdeQXVyMTIyNzY1NzM@._V1_UY268_CR4,0,182,268_AL_.jpg"
                    }
                    "67405111-37a5-438f-81cc-4666af60c800" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BZGZkNWQ1ZDQtMjYzNy00NmYxLWEwMDEtNjY2Y2U2ZWEyOGQ5L2ltYWdlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_UY268_CR4,0,182,268_AL_.jpg"
                    }
                    "578ae244-7750-4d9f-867b-f3cd3d6fecf4" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BMTcwODI0MzEwOF5BMl5BanBnXkFtZTgwNjkyNTEwMTE@._V1_UY268_CR4,0,182,268_AL_.jpg"
                    }
                    "5fdfb320-2a02-49a7-94ff-5ca418cae602" -> {
                        "https://images-na.ssl-images-amazon.com/images/M/MV5BYTkyMTNmY2EtOTZmYi00YWU4LTgxN2UtZWU0NTI0OGFkMWRjXkEyXkFqcGdeQXVyMzg2MzE2OTE@._V1_UY268_CR2,0,182,268_AL_.jpg"
                    }
                    else -> ""
                }
            }
        }

        return films.toList()
    }
}