package com.example.mypagination.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.mypagination.Api
import com.example.mypagination.db.AppDatabase
import com.example.mypagination.db.Person
import com.example.mypagination.paginglibs.db.DbPersonRepoImpl
import java.util.concurrent.Executors

class MyViewModel() : ViewModel() {

    private val DISK_IO = Executors.newSingleThreadExecutor()

    lateinit var persons: LiveData<PagedList<Person>>

    fun init(db: AppDatabase) {
        val repo = DbPersonRepoImpl(db, Api.create(), DISK_IO, 0)
        persons = repo.getPersons(MainActivity.PAGE_SIZE).pagedList
    }
}