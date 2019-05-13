package com.dev.lyhoangvinh.mvparchitecture.database.typecoverter

import android.arch.persistence.room.TypeConverter
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Volume
import com.google.gson.Gson

class VolumeTypeConverter {

    @TypeConverter
    fun stringToImageAll(data: String?): Volume? {
        return Gson().fromJson(data, Volume::class.java)
    }

    @TypeConverter
    fun imageAllToString(data: Volume?): String? {
        return Gson().toJson(data)
    }
}