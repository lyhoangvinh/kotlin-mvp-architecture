package com.dev.lyhoangvinh.mvparchitecture.database.entinies

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.dev.lyhoangvinh.mvparchitecture.database.typecoverter.ImageTypeConverter
import com.google.gson.annotations.SerializedName

@Entity
data class Issues(

    @PrimaryKey
    @SerializedName("id")
    var id: Long,

    @SerializedName("date_added")
    var date_added: String? = null,

    @SerializedName("date_last_updated")
    var date_last_updated: String? = null,

    @SerializedName("image")
    var images: ImageAll

)