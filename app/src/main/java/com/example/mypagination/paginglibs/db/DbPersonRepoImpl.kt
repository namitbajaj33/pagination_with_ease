package com.example.mypagination.paginglibs.db

import androidx.annotation.MainThread
import androidx.paging.toLiveData
import com.example.mypagination.Api
import com.example.mypagination.Listing
import com.example.mypagination.db.AppDatabase
import com.example.mypagination.db.InboxMsg
import com.example.mypagination.paginglibs.DbPersonRepo
import java.util.concurrent.Executor

class DbPersonRepoImpl(
    val db: AppDatabase,
    private val redditApi: Api,
    private val ioExecutor: Executor,
    private var networkPageSize: Int = 0
) : DbPersonRepo {


    private fun insertResultIntoDb(persons: List<InboxMsg>) {
        persons.let {
            db.runInTransaction {
                val items = persons.mapIndexed { _, child ->
                    child.projectId = 2424
                    child.spaceId = ""
                    child
                }
                db.personsDao().insertAll(items)
            }
        }
    }

    @MainThread
    override fun getPersons(pageSize: Int): Listing<InboxMsg> {
        val boundaryCallback = PersonsBoundaryCallback(
            webservice = redditApi,
            handleResponse = this::insertResultIntoDb,
            ioExecutor = ioExecutor,
            networkPageNum = networkPageSize
        )

        val livePagedList = db.personsDao()
            .getAll().toLiveData(
                pageSize = pageSize,
                boundaryCallback = boundaryCallback
            )

        return Listing(
            pagedList = livePagedList
        )
    }
}

