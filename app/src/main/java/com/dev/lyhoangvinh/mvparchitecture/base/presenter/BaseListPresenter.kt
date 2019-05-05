package com.dev.lyhoangvinh.mvparchitecture.base.presenter

import android.content.Context
import android.support.annotation.CallSuper
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.BaseView
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.LoadMoreable
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.Refreshable

/**
 * Base presenter with paging
 */

abstract class BaseListPresenter<V : BaseView>(context: Context) :
    BasePresenter<V>(context), Refreshable, LoadMoreable {

    var isRefreshed = false

    /**
     * refresh all paging date and re-fetch data
     */
    @CallSuper
    override fun refresh() {
        isRefreshed = true
        fetchData()
    }

    /**
     * load next page
     */
    @CallSuper
    override fun loadMore() {
        if (canLoadMore()) {
            fetchData()
        }
    }

    /**
     * Fetch data from server
     */
    protected abstract fun fetchData()


}
