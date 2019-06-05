package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.videos

import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.BaseView

interface VideosView: BaseView {
    fun connection(isConnected: Boolean)
    fun openDetail(url: String)
}