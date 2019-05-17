package com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.dev.lyhoangvinh.mvparchitecture.database.Resource
import com.dev.lyhoangvinh.mvparchitecture.database.Status
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

abstract class BasePresenter<V : BaseView> internal constructor(
    @ActivityContext var context: Context
) : Lifecycle, Refreshable {
    private var view: V? = null

    private var mCompositeDisposable: CompositeDisposable? = null

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
                    getView()?.showMessage(t.getMessage())
                }
            })

        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(disposable)
    }


    protected fun <T> execute(
        showProgress: Boolean, request: Single<Response<T>>,
        responseConsumer: PlainConsumer<T>?
    ) {
        execute(request, showProgress, true, responseConsumer)
    }


    /**
     * add a request with {@link Resource} flowable created by
     * {@link com.dev.lyhoangvinh.mvparchitecture.database.repo.BaseRepo}
     * @param showProgress
     * @param resourceFlowable
     * @param response
     * @param <T>
     */

    fun <T> execute(showProgress: Boolean, resourceFlowable: Flowable<Resource<T>>, response: PlainConsumer<T>?) {
        val disposable = resourceFlowable.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe { resource ->
                if (resource != null && getView() != null) {
                    Log.d("source", "addRequest: resource changed: $resource")
                    if (resource.data != null && response != null) {
                        response.accept(resource.data)
                    }
                    if (showProgress) {
                        getView()?.setProgress(resource.status === Status.LOADING)
                    }
                    if (resource.message != null) {
                        getView()?.showMessage(resource.message)
                    }
                }
            }
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(disposable)
    }

    fun <T> execute(resourceFlowable: Flowable<Resource<T>>, response: PlainConsumer<T>?) {
        execute(true, resourceFlowable, response)
    }

    fun <T> execute(resourceFlowable: Flowable<Resource<T>>) {
        execute(true, resourceFlowable, null)
    }
}