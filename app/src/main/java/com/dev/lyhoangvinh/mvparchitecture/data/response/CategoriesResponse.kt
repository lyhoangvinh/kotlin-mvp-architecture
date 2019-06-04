package com.dev.lyhoangvinh.mvparchitecture.data.response

import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Category
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CategoriesResponse(

    @SerializedName("categories")
    @Expose
    var categories: List<Category>? = ArrayList()

)