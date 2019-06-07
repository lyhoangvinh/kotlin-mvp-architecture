package com.dev.lyhoangvinh.mvparchitecture.data.repo

import android.util.Log
import com.dev.lyhoangvinh.mvparchitecture.data.Resource
import com.dev.lyhoangvinh.mvparchitecture.data.SimpleNetworkBoundSource
import com.dev.lyhoangvinh.mvparchitecture.data.SimpleNetworkBoundSourceBiRemote
import com.dev.lyhoangvinh.mvparchitecture.data.response.ResponseBiZip
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainResponseZipBiConsumer
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

    /**
     * For 2 single data
     * @param remote
     * @param onSave
     * @param <T>
     * @return
    </T> */
    fun <T1, T2> createResource(
        remote1: Single<Response<T1>>,
        remote2: Single<Response<T2>>,
        onSave: PlainResponseZipBiConsumer<T1, T2>
    ): Flowable<Resource<ResponseBiZip<T1, T2>>> {
        return Flowable.create({
            object : SimpleNetworkBoundSourceBiRemote<T1, T2>(it, true) {
                override fun getRemote1(): Single<Response<T1>> = remote1
                override fun getRemote2(): Single<Response<T2>> = remote2
                override fun saveCallResult(data: ResponseBiZip<T1, T2>, isRefresh: Boolean) {
                    onSave.accept(data)
                }
            }
        }, BackpressureStrategy.BUFFER)
    }


    /**
     * Excute room
    </T> */

    fun execute(action: () -> Unit) {
        Completable.fromAction {
            action.invoke()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}


