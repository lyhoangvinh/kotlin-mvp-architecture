package com.dev.lyhoangvinh.mvparchitecture.data.services

import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.CategoriesResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AvgleService {

    @GET("categories")
    fun getCategories(): Single<Response<BaseResponseAvgle<CategoriesResponse>>>

    @GET("categories/{page}")
    fun getAllVideo(@Path("page") page: Int)

    @GET("categories/{keyword}")
    fun getVideoCollections(@Path("keyword") keyword: String)
}