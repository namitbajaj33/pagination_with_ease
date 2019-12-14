package com.example.mypagination.paginglibs.db

import android.util.Log
import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.example.mypagination.Api
import com.example.mypagination.ui.MainActivity
import com.example.mypagination.db.Person
import com.example.mypagination.db.UsersSearchResponse
import com.example.paging.PagingRequestHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class PersonsBoundaryCallback(
    private val webservice: Api,
    private val handleResponse: (List<Person>) -> Unit,
    private val ioExecutor: Executor,
    private var networkPageNum: Int
) : PagedList.BoundaryCallback<Person>() {

    val helper = PagingRequestHelper(ioExecutor)

    companion object {

        const val TAG = "PersonsBoundaryCallback"

    }

    @MainThread
    override fun onZeroItemsLoaded() {

        Log.e(TAG, "onZero: $networkPageNum")

        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            webservice.searchUsers(
                MainActivity.GOOGLE,
                networkPageNum,
                MainActivity.PAGE_SIZE
            ).enqueue(createWebserviceCallback(it))
        }
    }

    /**
     * User reached to the end of the list.
     */
    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: Person) {

        if ((itemAtEnd.indexInResponse + 1) % MainActivity.PAGE_SIZE == 0) {
            networkPageNum += 1

            Log.e(TAG, "onItem: $networkPageNum")

            helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
                webservice.searchUsers(
                    MainActivity.GOOGLE,
                    networkPageNum,
                    MainActivity.PAGE_SIZE
                ).enqueue(createWebserviceCallback(it))
            }
        }
    }

    private fun insertItemsIntoDb(
        response: Response<UsersSearchResponse>,
        it: PagingRequestHelper.Request.Callback
    ) {
        response.body()?.let { usersResponse ->
            usersResponse.persons?.let { persons ->
                ioExecutor.execute {
                    handleResponse(persons)
                    it.recordSuccess()
                }
            }
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: Person) {
        // ignored, since we only ever append to what's in the DB
    }

    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback)
            : Callback<UsersSearchResponse> {
        return object : Callback<UsersSearchResponse> {
            override fun onFailure(
                call: Call<UsersSearchResponse>,
                t: Throwable
            ) {
                it.recordFailure(t)
            }

            override fun onResponse(
                call: Call<UsersSearchResponse>,
                response: Response<UsersSearchResponse>
            ) {
                insertItemsIntoDb(response, it)
            }
        }
    }
}