package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.search

import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.BaseView

interface SearchView : BaseView{
    fun connection(isConnected: Boolean)
    fun openDetail(url: String)
}