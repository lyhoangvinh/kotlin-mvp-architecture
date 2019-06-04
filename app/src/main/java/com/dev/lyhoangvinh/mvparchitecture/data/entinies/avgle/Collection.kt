package com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "COLLECTION")
data class Collection(
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @SerializedName("id")
    @Expose
    var idCLS: String? = "",

    @SerializedName("title")
    @Expose
    var title: String? = "",

    @SerializedName("keyword")
    @Expose
    var keyword: String? = "",

    @SerializedName("cover_url")
    @Expose
    var coverUrl: String? = "",
    @SerializedName("total_views")
    @Expose
    var totalViews: Int? = 0,
    @SerializedName("video_count")
    @Expose
    var videoCount: Int? = 0,
    @SerializedName("collection_url")
    @Expose
    var collectionUrl: String? = ""
)