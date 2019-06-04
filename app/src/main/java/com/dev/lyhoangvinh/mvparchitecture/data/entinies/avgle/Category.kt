package com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "CATEGORY")
data class Category(

    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @SerializedName("CHID")
    @Expose
    var CHID: String? = "",

    @SerializedName("name")
    @Expose
    var name: String? = "",

    @SerializedName("slug")
    @Expose
    var slug: String? = "",

    @SerializedName("total_videos")
    @Expose
    var totalVideos: Int? = 0,

    @SerializedName("category_url")
    @Expose
    var categoryUrl: String? = "",

    @SerializedName("cover_url")
    @Expose
    var coverUrl: String? = ""
)