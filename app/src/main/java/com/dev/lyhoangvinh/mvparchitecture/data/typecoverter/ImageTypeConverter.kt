package com.dev.lyhoangvinh.mvparchitecture.data.typecoverter

import android.arch.persistence.room.TypeConverter
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.comic.ImageAll
import com.google.gson.Gson

class ImageTypeConverter {

    @TypeConverter
    fun stringToImageAll(data: String?): ImageAll? {
        return Gson().fromJson(data, ImageAll::class.java)
    }

    @TypeConverter
    fun imageAllToString(data: ImageAll?): String? {
        return Gson().toJson(data)
    }
}