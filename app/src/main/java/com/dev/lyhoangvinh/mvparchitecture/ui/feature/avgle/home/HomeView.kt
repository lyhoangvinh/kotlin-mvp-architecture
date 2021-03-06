package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home

import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Category
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Video
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.BaseView

interface HomeView : BaseView {
    fun swapCategoriesSuccess(categories: List<Category>)
    fun swapCollectionsBannerSuccess(collections: List<Collection>)
    fun swapCollectionsBottomSuccess(collections: List<Collection>)
    fun swapVideos(videos: List<Video>)
}