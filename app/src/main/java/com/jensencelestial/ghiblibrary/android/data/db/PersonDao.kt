package com.jensencelestial.ghiblibrary.android.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jensencelestial.ghiblibrary.android.data.model.Person

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: Person)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeople(people: List<Person>)

    @Query("SELECT * FROM person")
    suspend fun getAllPeople(): List<Person>

    @Query("SELECT * FROM person WHERE id = :personId")
    suspend fun getPerson(personId: String): Person
}