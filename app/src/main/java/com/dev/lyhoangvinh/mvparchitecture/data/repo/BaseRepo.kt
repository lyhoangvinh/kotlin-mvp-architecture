package com.dev.lyhoangvinh.mvparchitecture.data.repo

import android.util.Log
import com.dev.lyhoangvinh.mvparchitecture.data.Resource
import com.dev.lyhoangvinh.mvparchitecture.data.SimpleNetworkBoundSource
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


abstract class BaseRepo {
    /**
     * For single data
     * @param remote
     * @param onSave
     * @param <T>
     * @return
    </T> */
    fun <T> createResource(
        remote: Single<Response<T>>,
        onSave: PlainConsumer<T>
    ): Flowable<Resource<T>> {
        return Flowable.create({
            object : SimpleNetworkBoundSource<T>(it, true) {
                override fun getRemote(): Single<Response<T>> = remote
                override fun saveCallResult(data: T, isRefresh: Boolean) {
                    onSave.accept(data)
                }
            }
        }, BackpressureStrategy.BUFFER)
    }

    /**
     * For a list of data
     * @param isRefresh
     * @param remote
     * @param onSave
     * @param <T>
     * @return
    </T> */
    fun <T> createResource(
        isRefresh: Boolean,
        remote: Single<Response<T>>,
        onSave: OnSaveResultListener<T>
    ): Flowable<Resource<T>> {
        return Flowable.create({
            object : SimpleNetworkBoundSource<T>(it, isRefresh) {
                override fun getRemote(): Single<Response<T>> = remote
                override fun saveCallResult(data: T, isRefresh: Boolean) {
                    onSave.onSave(data, isRefresh)
                }
            }
        }, BackpressureStrategy.BUFFER)
    }

    interface OnSaveResultListener<T> {
        fun onSave(data: T, isRefresh: Boolean)
    }

    fun execute(action: () -> Unit, onComplete: () -> Unit) {
        Completable.fromAction {
            action.invoke()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    onComplete.invoke()
                    Log.d(BaseRepo::class.java.simpleName, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    Log.d(BaseRepo::class.java.simpleName, "onSubscribe")
                }

                override fun onError(e: Throwable) {
                    Log.d(BaseRepo::class.java.simpleName, "onError")
                }
            })
    }
}


