package com.dev.lyhoangvinh.mvparchitecture.database.entinies

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class ImageAll(
    @SerializedName("medium_url")
    var medium_url: String? = ""
)