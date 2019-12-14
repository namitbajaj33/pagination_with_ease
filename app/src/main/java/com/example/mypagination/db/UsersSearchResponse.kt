package com.example.mypagination.db

import com.google.gson.annotations.SerializedName

class UsersSearchResponse {

    @SerializedName("total_count")
    var totalCount: Int? = null
    @SerializedName("incomplete_results")
    var incompleteResults: Boolean? = null
    @SerializedName("items")
    var persons: List<Person>? = null
}