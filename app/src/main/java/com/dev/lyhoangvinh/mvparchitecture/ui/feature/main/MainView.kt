package com.dev.lyhoangvinh.mvparchitecture.ui.feature.main

import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.BaseView

interface MainView : BaseView {
    fun size(size: Int)

    fun connection(isConnected: Boolean)

    fun deleteSuccess()
}