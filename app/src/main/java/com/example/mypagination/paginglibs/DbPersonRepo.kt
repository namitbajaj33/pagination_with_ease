package com.example.mypagination.paginglibs

import androidx.paging.PagedList
import com.example.mypagination.Listing
import com.example.mypagination.db.Person

interface DbPersonRepo {

    fun getPersons(pageSize: Int): Listing<Person>

}