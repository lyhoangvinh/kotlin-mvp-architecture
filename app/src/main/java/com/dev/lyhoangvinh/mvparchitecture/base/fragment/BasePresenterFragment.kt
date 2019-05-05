package com.dev.lyhoangvinh.mvparchitecture.base.fragment

import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.View
import com.dev.lyhoangvinh.mvparchitecture.base.presenter.BasePresenter
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.BaseView
import javax.inject.Inject

abstract class BasePresenterFragment<V : BaseView, P : BasePresenter<V>> : BaseFragment() {

    @Inject
    protected lateinit var presenter: P

    @CallSuper
    override fun initialize(view: View, ctx: Context?) {
        presenter.bindView(getViewLayer())
        presenter.onCreate()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        presenter.onRestoreInstanceState(savedInstanceState!!)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    private fun getViewLayer(): V {
        return this as V
    }
}
