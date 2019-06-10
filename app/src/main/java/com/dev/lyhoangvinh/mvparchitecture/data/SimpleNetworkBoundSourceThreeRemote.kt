package com.dev.lyhoangvinh.mvparchitecture.data


import android.util.Log
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.ErrorEntity
import com.dev.lyhoangvinh.mvparchitecture.data.response.ResponseBiZip
import com.dev.lyhoangvinh.mvparchitecture.data.response.ResponseThreeZip
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainResponseZipBiConsumer
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainResponseZipThreeConsumer
import com.dev.lyhoangvinh.mvparchitecture.utils.makeRequest
import io.reactivex.FlowableEmitter
import io.reactivex.Single
import retrofit2.Response
import java.util.function.BiConsumer

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 */
abstract class SimpleNetworkBoundSourceThreeRemote<T1, T2, T3>(
    emitter: FlowableEmitter<Resource<ResponseThreeZip<T1, T2, T3>>>,
    isRefresh: Boolean
) {

    init {
        emitter.onNext(Resource.loading(null))
        // since realm was create on Main Thread, so if we need to touch on realm database after calling
        // api, must make request on main thread by setting shouldUpdateUi params = true
        makeRequest(getRemote1(), getRemote2(), getRemote3(), true, object : PlainResponseZipThreeConsumer<T1, T2, T3> {
            override fun accept(dto: ResponseThreeZip<T1, T2, T3>) {
                Log.d(TAG, "SimpleNetworkBoundSource: call API success!")
                saveCallResult(dto, isRefresh)
                emitter.onNext(Resource.success(dto))
            }
        }, object : PlainConsumer<ErrorEntity> {
            override fun accept(t: ErrorEntity) {
                Log.d(TAG, "SimpleNetworkBoundSource: call API error: " + t.getMessage())
                emitter.onNext(Resource.error(t.getMessage(), null))
            }
        })
    }

    abstract fun getRemote1(): Single<Response<T1>>

    abstract fun getRemote2(): Single<Response<T2>>

    abstract fun getRemote3(): Single<Response<T3>>


    abstract fun saveCallResult(data: ResponseThreeZip<T1, T2, T3>, isRefresh: Boolean)

    companion object {
        val TAG = "resource"
    }
}