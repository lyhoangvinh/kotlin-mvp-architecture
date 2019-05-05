package com.dev.lyhoangvinh.mvparchitecture

import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.BaseView
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Comics

interface MainView : BaseView {
    fun getDataSuccess(list: List<Comics>)
}