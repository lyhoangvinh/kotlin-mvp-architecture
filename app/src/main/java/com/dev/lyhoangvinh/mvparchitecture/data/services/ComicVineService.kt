package com.dev.lyhoangvinh.mvparchitecture.data.services

import com.dev.lyhoangvinh.mvparchitecture.data.entinies.comic.Issues
import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseComic
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
            Single<BaseResponseComic<Issues>>

    @GET("/issues")
    fun getIssues2(
        @Query("limit") limit: Int, @Query("offset") offset: Int
        , @Query("api_key") api_key: String, @Query("format") format: String
        , @Query("sort") sort: String
    ):
            Single<Response<BaseResponseComic<Issues>>>
}