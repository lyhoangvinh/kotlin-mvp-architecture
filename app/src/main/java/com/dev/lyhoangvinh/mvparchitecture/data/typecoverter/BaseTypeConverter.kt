package com.dev.lyhoangvinh.mvparchitecture.data.typecoverter

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

abstract class BaseTypeConverter<T> {

    @TypeConverter
    fun stringToContent(data: String?): T {
        val listType = object : TypeToken<T>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun contentToString(someObjects: T): String {
        return Gson().toJson(someObjects)
    }
}