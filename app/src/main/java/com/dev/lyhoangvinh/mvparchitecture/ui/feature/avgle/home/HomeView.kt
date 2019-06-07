package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home

import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Category
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.BaseView

interface HomeView : BaseView {
    fun swapDataSuccess(categories: List<Category>, collections: List<Collection>)
}