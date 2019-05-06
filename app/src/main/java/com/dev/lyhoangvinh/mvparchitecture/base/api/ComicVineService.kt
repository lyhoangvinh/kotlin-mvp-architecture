package com.dev.lyhoangvinh.mvparchitecture.base.api

import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Issues
import com.dev.lyhoangvinh.mvparchitecture.database.response.BaseResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ComicVineService {

    @GET("/issues")
    fun getIssues(
        @Query("limit") limit: Int, @Query("offset") offset: Int
        , @Query("api_key") api_key: String, @Query("format") format: String
        , @Query("sort") sort: String
    ):
            Single<BaseResponse<Issues>>

    @GET("/issues")
    fun getIssues2(
        @Query("limit") limit: Int, @Query("offset") offset: Int
        , @Query("api_key") api_key: String, @Query("format") format: String
        , @Query("sort") sort: String
    ):
            Single<Response<BaseResponse<Issues>>>
}