package com.dev.lyhoangvinh.mvparchitecture.ui.main

import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.BaseView
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Comics

interface MainView : BaseView{
    fun size(size : Int)
}