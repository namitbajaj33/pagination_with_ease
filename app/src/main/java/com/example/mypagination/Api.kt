package com.example.mypagination

import android.util.Log
import com.example.mypagination.db.ApiResponseModel
import com.example.mypagination.db.InboxMsg
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("message/collaborationmsglist")
    fun getMessages(
        @Field("securityToken") token: String, @Field("projectId") pId: Int,
        @Field("spaceId") sId: String, @Field("istickerId") stickerId: Int,
        @Field("timestamp") timestamp: String,
        @Field("drawingId") drawingId: Int,
        @Field("isReverse") isReverse: Int
    ): Call<ApiResponseModel<List<InboxMsg>>>


    companion object {
        private const val BASE_URL = "https://api.archchat.com/API/web/index.php/api/v1/"
        fun create(): Api = create(HttpUrl.parse(BASE_URL)!!)
        fun create(httpUrl: HttpUrl): Api {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.d("API", it)
            })
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
        }
    }
}