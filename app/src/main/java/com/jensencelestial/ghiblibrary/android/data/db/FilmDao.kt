package com.jensencelestial.ghiblibrary.android.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jensencelestial.ghiblibrary.android.data.model.Film

@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: Film)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilms(films: List<Film>)

    @Query("SELECT * FROM films")
    suspend fun getAllFilms(): List<Film>

    @Query("SELECT * FROM films WHERE id = :filmId")
    suspend fun getFilm(filmId: Int): Film

}