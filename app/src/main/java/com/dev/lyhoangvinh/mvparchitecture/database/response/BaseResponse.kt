package com.dev.lyhoangvinh.mvparchitecture.database.response

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("error")
    var error: String? = null,

    @SerializedName("incomplete_results")
    var incompleteResults: Boolean? = null,

    @SerializedName("limit")
    var limit: Int? = null,

    @SerializedName("offset")
    var offset: Int? = null,

    @SerializedName("number_of_page_results")
    var number_of_page_results: Int? = null,

    @SerializedName("number_of_total_results")
    var number_of_total_results: Int? = null,

    @SerializedName("status_code")
    var status_code: Int? = null,

    @SerializedName("results")
    var results: List<T>
)