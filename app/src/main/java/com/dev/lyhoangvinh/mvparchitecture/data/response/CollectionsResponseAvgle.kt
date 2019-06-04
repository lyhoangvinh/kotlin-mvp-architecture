package com.dev.lyhoangvinh.mvparchitecture.data.response

import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CollectionsResponseAvgle(

    @SerializedName("has_more")
    @Expose
    var hasMore: Boolean,

    @SerializedName("total_collections")
    @Expose
    var totalCollections: Int? = 0,

    @SerializedName("current_offset")
    @Expose
    var currentOffset: Int? = 0,

    @SerializedName("limit")
    @Expose
    var limit: Int? = 0,

    @SerializedName("collections")
    @Expose
    var collections: List<Collection>
)