package com.dev.lyhoangvinh.mvparchitecture.database.repo

import com.dev.lyhoangvinh.mvparchitecture.database.Resource
import com.dev.lyhoangvinh.mvparchitecture.database.SimpleNetworkBoundSource
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.Response

abstract class BaseRepo {

    /**
     * For single data
     * @param remote
     * @param onSave
     * @param <T>
     * @return
    </T> */
    protected fun <T> createResource(
        remote: Single<Response<T>>?,
        onSave: PlainConsumer<T>?
    ): Flowable<Resource<T>> {
        return Flowable.create({
            object : SimpleNetworkBoundSource<T>(it, true) {
                override fun getRemote(): Single<Response<T>> = remote!!
                override fun saveCallResult(data: T, isRefresh: Boolean) {
                    onSave?.accept(data)
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
    protected fun <T> createResource(
        isRefresh: Boolean, remote: Single<Response<T>>?,
        onSave: OnSaveResultListener<T>?
    ): Flowable<Resource<T>> {
        return Flowable.create({
            object : SimpleNetworkBoundSource<T>(it, isRefresh) {
                override fun getRemote(): Single<Response<T>> = remote!!
                override fun saveCallResult(data: T, isRefresh: Boolean) {
                    onSave?.onSave(data, isRefresh)
                }
            }
        }, BackpressureStrategy.BUFFER)
    }

    protected interface OnSaveResultListener<T> {
        fun onSave(data: T, isRefresh: Boolean)
    }
}