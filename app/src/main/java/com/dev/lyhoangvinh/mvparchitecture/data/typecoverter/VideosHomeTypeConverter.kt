package com.dev.lyhoangvinh.mvparchitecture.data.typecoverter

import android.arch.persistence.room.TypeConverter
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Video
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class VideosHomeTypeConverter {

    @TypeConverter
    fun stringToContentList(data: String?): List<Video> {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<Video>>() {}.type
        return Gson().fromJson<List<Video>>(data, listType)
    }

    @TypeConverter
    fun contentListToString(someObjects: List<Video>): String {
        return Gson().toJson(someObjects)
    }
}