package com.example.mypagination.paginglibs.db

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.mypagination.Api
import com.example.mypagination.Listing
import com.example.mypagination.db.AppDatabase
import com.example.mypagination.db.Person
import com.example.mypagination.paginglibs.DbPersonRepo
import com.example.mypagination.ui.MainActivity
import java.util.concurrent.Executor

class DbPersonRepoImpl(
    val db: AppDatabase,
    private val redditApi: Api,
    private val ioExecutor: Executor,
    private var networkPageSize: Int = 0
) : DbPersonRepo {


    private fun getLastIndex() {
        db.runInTransaction {
            networkPageSize = db.personsDao().getNextIndex()
        }
    }

    private fun insertResultIntoDb(persons: List<Person>) {
        persons.let {
            db.runInTransaction {
                val start = db.personsDao().getNextIndex()
                val items = persons.mapIndexed { index, child ->
                    child.indexInResponse = start + index
                    child
                }
                db.personsDao().insertAll(items)
            }
        }
    }

    @MainThread
    override fun getPersons(pageSize: Int): Listing<Person> {
        val boundaryCallback = PersonsBoundaryCallback(
            webservice = redditApi,
            handleResponse = this::insertResultIntoDb,
            ioExecutor = ioExecutor,
            networkPageNum = networkPageSize
        )

        val livePagedList = db.personsDao().getPersonDataSource().toLiveData(
            pageSize = pageSize,
            boundaryCallback = boundaryCallback
        )

        return Listing(
            pagedList = livePagedList
        )
    }
}

