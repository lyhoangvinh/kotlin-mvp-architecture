package com.dev.lyhoangvinh.mvparchitecture.ui.base.activity

import android.os.Bundle
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.BaseView
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BasePresenter
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
abstract class BasePresenterActivity<V : BaseView, P : BasePresenter<V>> : BaseActivity() {

    @Inject
    protected lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter.bindView(getViewLayer())
        presenter.onCreate()
        super.onCreate(savedInstanceState)
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
