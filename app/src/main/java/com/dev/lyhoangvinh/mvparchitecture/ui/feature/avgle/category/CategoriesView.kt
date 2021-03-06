package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.category

import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Category
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.BaseView

interface CategoriesView : BaseView {
    fun connection(isConnected: Boolean)

    fun openCollection(category: Category)
}