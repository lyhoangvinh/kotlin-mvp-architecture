package com.dev.lyhoangvinh.mvparchitecture.data.services

import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.CategoriesResponse
import com.dev.lyhoangvinh.mvparchitecture.data.response.CollectionsResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.VideosResponseAvgle
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AvgleService {

    @GET("categories")
    fun getCategories(): Single<Response<BaseResponseAvgle<CategoriesResponse>>>

    @GET("collections/{page}")
    fun getCollections(@Path("page") page: Int, @Query("limit") limit: Int): Single<Response<BaseResponseAvgle<CollectionsResponseAvgle>>>

    @GET("collections/{keyword}")
    fun getVideoCollections(@Path("keyword") keyword: String): Single<Response<BaseResponseAvgle<CollectionsResponseAvgle>>>

    @GET("videos/{page}")
    fun getVideosFromKeyword(@Path("page") page: Int, @Query("c") chId: String): Single<Response<BaseResponseAvgle<VideosResponseAvgle>>>

    @GET("videos/{page}")
    fun getAllVideos(@Path("page") page: Int): Single<Response<BaseResponseAvgle<VideosResponseAvgle>>>

    @GET("search/{query}/{page}")
    fun searchVideos(@Path("query") query: String, @Path("page") page: Int): Single<Response<BaseResponseAvgle<VideosResponseAvgle>>>

    @GET("jav/{query}/{page}")
    fun searchJav(@Path("query") query: String, @Path("page") page: Int): Single<Response<BaseResponseAvgle<VideosResponseAvgle>>>
}