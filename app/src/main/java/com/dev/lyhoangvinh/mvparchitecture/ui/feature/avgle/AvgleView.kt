package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle

import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.BaseView

interface AvgleView : BaseView {
    fun connection(isConnected: Boolean)

    fun openDetail(url: String)
}