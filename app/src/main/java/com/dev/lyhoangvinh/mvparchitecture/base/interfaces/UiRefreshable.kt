package com.dev.lyhoangvinh.mvparchitecture.base.interfaces

/**
 * Indicate refreshable ui objects, eg. activity, fragment...
 */

interface UiRefreshable : Refreshable {
    fun doneRefresh()
    fun refreshWithUi()
    fun refreshWithUi(delay: Long)
    fun setRefreshEnabled(enabled: Boolean)
}
