package com.example.mypagination.db

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.net.HttpURLConnection

class ApiResponseModel<T> {

    @SerializedName("code")
    @Expose
    var code: Int? = 0
    @SerializedName("data")
    @Expose
    var data: T? = null
    @SerializedName("msg")
    @Expose
    var message: String? = null

    fun isSuccessful(): Boolean {
        return code == HttpURLConnection.HTTP_OK
    }
}