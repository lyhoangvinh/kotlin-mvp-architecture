package com.dev.lyhoangvinh.mvparchitecture.data.services

import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.CategoriesResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface AvgleService {

    @GET("categories")
    fun getCategories(): Single<Response<BaseResponseAvgle<CategoriesResponse>>>
}