package com.dev.lyhoangvinh.mvparchitecture.data.services

import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.CategoriesResponse
import com.dev.lyhoangvinh.mvparchitecture.data.response.CollectionsResponseAvgle
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AvgleService {

    @GET("categories")
    fun getCategories(): Single<Response<BaseResponseAvgle<CategoriesResponse>>>

    @GET("collections/{page}")
    fun getAllVideo(@Path("page") page: Int): Single<Response<BaseResponseAvgle<CollectionsResponseAvgle>>>

    @GET("collections/{keyword}")
    fun getVideoCollections(@Path("keyword") keyword: String): Single<Response<BaseResponseAvgle<CollectionsResponseAvgle>>>
}