package com.dev.lyhoangvinh.mvparchitecture.ui.feature.comics

import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.BaseView

interface IssuesView : BaseView {

    fun connection(isConnected: Boolean)

    fun deleteSuccess()
}