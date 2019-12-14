package com.example.mypagination.paginglibs.db

import android.util.Log
import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.example.mypagination.Api
import com.example.mypagination.db.ApiResponseModel
import com.example.mypagination.db.InboxMsg
import com.example.mypagination.ui.MainActivity
import com.example.paging.PagingRequestHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class PersonsBoundaryCallback(
    private val webservice: Api,
    private val handleResponse: (List<InboxMsg>) -> Unit,
    private val ioExecutor: Executor,
    private var networkPageNum: Int
) : PagedList.BoundaryCallback<InboxMsg>() {


    var itemAtEndid: Int = 0
    val helper = PagingRequestHelper(ioExecutor)

    companion object {

        const val TAG = "PersonsBoundaryCallback"

    }

    @MainThread
    override fun onZeroItemsLoaded() {

        Log.e(TAG, "onZero: $networkPageNum")

        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            webservice.getMessages(
                "y7BgGWnH8J8w5/sC9zU19QT4YqPtWqNRt+jzoYrTBLEChUiF4lblSsExngMuqB1Ni+KwnmUI1LEkr88usxwstulzPJlAEAY/gap3YYQ2aqrE5v01sZs1iqkzkkHm5/ZE",
                2424,
                "0",
                0,
                "",
                0,
                1
            ).enqueue(createWebserviceCallback(it))
        }
    }

    /**
     * User reached to the end of the list.
     */
    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: InboxMsg) {

        if (itemAtEndid != itemAtEnd.msgId) {
            itemAtEndid = itemAtEnd.msgId

            Log.e(TAG, "onItem: $networkPageNum")

            helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
                webservice.getMessages(
                    "y7BgGWnH8J8w5/sC9zU19QT4YqPtWqNRt+jzoYrTBLEChUiF4lblSsExngMuqB1Ni+KwnmUI1LEkr88usxwstulzPJlAEAY/gap3YYQ2aqrE5v01sZs1iqkzkkHm5/ZE",
                    2424,
                    "0",
                    0,
                    itemAtEnd.timestamp.toString(),
                    0,
                    1
                ).enqueue(createWebserviceCallback(it))
            }
        }
    }

    private fun insertItemsIntoDb(
        response: Response<ApiResponseModel<List<InboxMsg>>>,
        it: PagingRequestHelper.Request.Callback
    ) {
        response.body()?.let { usersResponse ->
            usersResponse.data?.let { persons ->
                ioExecutor.execute {
                    handleResponse(persons)
                    it.recordSuccess()
                }
            }
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: InboxMsg) {
        // ignored, since we only ever append to what's in the DB
    }

    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback)
            : Callback<ApiResponseModel<List<InboxMsg>>> {
        return object : Callback<ApiResponseModel<List<InboxMsg>>> {
            override fun onFailure(
                call: Call<ApiResponseModel<List<InboxMsg>>>,
                t: Throwable
            ) {
                it.recordFailure(t)
            }

            override fun onResponse(
                call: Call<ApiResponseModel<List<InboxMsg>>>,
                response: Response<ApiResponseModel<List<InboxMsg>>>
            ) {
                insertItemsIntoDb(response, it)
            }
        }
    }
}