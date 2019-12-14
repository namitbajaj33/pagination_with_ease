package com.example.mypagination.db

import androidx.paging.DataSource
import androidx.room.*

@Dao
interface PersonDao {

    @Query("SELECT * FROM person")
    fun getAll(): List<Person>

    @Query("SELECT COUNT(*) from person")
    fun countPersons(): Int

    @Query("SELECT * FROM person")
    fun getPersonDataSource(): DataSource.Factory<Int, Person>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(persons: List<Person>)

    @Delete
    fun delete(person: Person)

    @Query("SELECT MAX(indexInResponse) + 1 FROM person")
    fun getNextIndex(): Int
}