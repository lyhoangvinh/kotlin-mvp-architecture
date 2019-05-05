package com.dev.lyhoangvinh.mvparchitecture.base.activity

import android.os.Bundle
import android.support.annotation.CallSuper
import com.dev.lyhoangvinh.mvparchitecture.base.BasePresenter
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.BaseView
import javax.inject.Inject

abstract class BasePresenterActivity<V : BaseView, P : BasePresenter<V>> : BaseActivity() {

    @Inject
    protected lateinit var presenter: P

    @CallSuper
    protected fun initialize() {
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        presenter.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    private fun getViewLayer(): V {
        return this as V
    }
}
