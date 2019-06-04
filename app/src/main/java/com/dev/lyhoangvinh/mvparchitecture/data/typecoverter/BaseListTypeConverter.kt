package com.dev.lyhoangvinh.mvparchitecture.data.typecoverter

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

abstract class BaseListTypeConverter<T> {

    @TypeConverter
    fun stringToContentList(data: String?): List<T> {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<T>>() {}.type
        return Gson().fromJson<List<T>>(data, listType)
    }

    @TypeConverter
    fun contentListToString(someObjects: List<T>): String {
        return Gson().toJson(someObjects)
    }
}