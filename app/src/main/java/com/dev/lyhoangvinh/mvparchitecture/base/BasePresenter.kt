package com.dev.lyhoangvinh.mvparchitecture.base

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.os.Bundle
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.BaseView
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.Lifecycle
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.Refreshable
import com.dev.lyhoangvinh.mvparchitecture.database.DatabaseManager
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

abstract class BasePresenter<V : BaseView> internal constructor(
    @ActivityContext var context: Context,
    val databaseManager: DatabaseManager
) : Lifecycle, Refreshable {
    private var view: V? = null

    private var mCompositeDisposable: CompositeDisposable? = null

    private fun <T> addRequestSingle(
        request: Single<T>, showProgress: Boolean,
        forceResponseWithoutCheckNullView: Boolean,
        responseConsumer: PlainConsumer<T>?
    ) {
        val shouldUpdateUI = showProgress || responseConsumer != null

        if (showProgress) {
            getView()?.showProgress()
        }

        var single = request.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
        if (shouldUpdateUI) {
            single = single.observeOn(AndroidSchedulers.mainThread())
        }

        val disposable = single.subscribe({ t ->
            if (responseConsumer != null && (forceResponseWithoutCheckNullView && getView() != null)) {
                responseConsumer.accept(t)
            }
            if (showProgress) {
                getView()?.hideProgress()
            }
        }, {
            if (showProgress) {
                getView()?.hideProgress()
            }
        })

        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(disposable)
    }

    protected fun <T> addRequestSingle(
        showProgress: Boolean, request: Single<T>,
        responseConsumer: PlainConsumer<T>?
    ) {
        addRequestSingle(request, showProgress, showProgress, responseConsumer)
    }


    /**
     * @return [LifecycleOwner] associate with this presenter (host activities, fragments)
     */
    protected fun getLifeCircleOwner(): LifecycleOwner? {
        return view as LifecycleOwner?
    }

    fun getView(): V? = view

    fun bindView(view: V) {
        this.view = view
    }

    override fun onDestroy() {
        mCompositeDisposable?.dispose()
        this.view = null
    }

    override fun onCreate() {

    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onSaveInstanceState(outState: Bundle) {
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    }

    override fun refresh() {

    }
}