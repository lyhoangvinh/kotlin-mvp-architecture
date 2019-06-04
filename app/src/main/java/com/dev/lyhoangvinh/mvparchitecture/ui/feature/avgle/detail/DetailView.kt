package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.detail

import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.BaseView

interface DetailView : BaseView {

    fun connection(isConnection: Boolean, url: String, isReload: Boolean)
}