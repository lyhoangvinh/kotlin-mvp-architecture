package com.dev.lyhoangvinh.mvparchitecture.database.typecoverter

import android.arch.persistence.room.TypeConverter
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.ImageAll
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

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