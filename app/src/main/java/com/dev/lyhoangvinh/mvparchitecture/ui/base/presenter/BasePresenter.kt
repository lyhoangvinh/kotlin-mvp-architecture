package com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.ErrorEntity
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.BaseView
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.Lifecycle
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.Refreshable
import com.dev.lyhoangvinh.mvparchitecture.utils.makeRequest
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.annotation.Resource

abstract class BasePresenter<V : BaseView> internal constructor(
    @ActivityContext var context: Context
) : Lifecycle, Refreshable {
    private var view: V? = null

    private var mCompositeDisposable: CompositeDisposable? = null

    protected fun <T> execute(
        showProgress: Boolean, request: Single<Response<T>>,
        responseConsumer: PlainConsumer<T>?
    ) {
        execute(request, showProgress, true, responseConsumer)
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

    private fun <T> execute(
        request: Single<Response<T>>, showProgress: Boolean,
        forceResponseWithoutCheckNullView: Boolean,
        responseConsumer: PlainConsumer<T>?
    ) {
        val shouldUpdateUI = showProgress || responseConsumer != null

        if (showProgress) {
            getView()?.showProgress()
        }

        val disposable = makeRequest(
            request,
            shouldUpdateUI, object : PlainConsumer<T> {
                override fun accept(t: T) {
                    if ((forceResponseWithoutCheckNullView || view != null) && responseConsumer != null) {
                        responseConsumer.accept(t)
                    }
                }
            },
            onComplete = Action {
                getView()?.hideProgress()
            }, errorConsumer = object : PlainConsumer<ErrorEntity> {
                override fun accept(t: ErrorEntity) {
                    getView()?.showToast(t.getMessage())
                }
            })

        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(disposable)
    }

    /**
     * add a request with [Resource] flowable created by
     * @param showProgress
     * @param resourceFlowable
     * @param response
     * @param <T>
    </T> */
    fun <T> execute(resourceFlowable: Flowable<Response<T>>, response: PlainConsumer<T>?) {
        val disposable = resourceFlowable.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe { resource ->
                if (resource.isSuccessful && getView() != null) {
                    Log.d("source", "addRequest: resource changed: " + resource.toString())
                    resource.body()?.let { response?.accept(it) }
                }
            }
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(disposable)
    }
}