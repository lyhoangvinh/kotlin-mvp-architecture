package com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle

sealed class MergedData
data class CategoryData(val categoryItems: List<Category>) : MergedData()
data class CollectionBannerData(val collectionBannerItems: List<Collection>) : MergedData()
data class CollectionBottomData(val collectionBottomItems: List<Collection>) : MergedData()
data class VideoData(val videoItems: List<Video>) : MergedData()
