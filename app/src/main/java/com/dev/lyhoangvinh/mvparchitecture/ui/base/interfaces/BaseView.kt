package com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces

/**
 * Created by lyhoangvinh on 04/01/18.
 */
interface BaseView {

    fun showProgress()

    fun hideProgress()

    fun setProgress(show: Boolean)

    fun showMessage(message: String)
}