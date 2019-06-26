package com.dev.lyhoangvinh.mvparchitecture.ui.feature.splash

import android.arch.lifecycle.Observer
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.data.repo.HomeRepo
import com.dev.lyhoangvinh.mvparchitecture.data.response.*
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BasePresenter
import com.dev.lyhoangvinh.mvparchitecture.utils.ConnectionLiveData
import java.util.logging.Handler
import javax.inject.Inject

@PerActivity
class SplashPresenter @Inject constructor(
    @ActivityContext context: Context, private val homeRepo: HomeRepo,
    private val connectionLiveData: ConnectionLiveData
) :
    BasePresenter<SplashView>(context) {
    fun getData() {
        connectionLiveData.observe(getLifeCircleOwner(), Observer {
            if (it!!.isConnected) {
                execute(false,
                    homeRepo.getRepoHome(), object :
                        PlainConsumer<ResponseFourZip<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>>> {
                        override fun accept(t: ResponseFourZip<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>>) {
                            getView()?.swapDataSuccess()
                        }
                    })
            } else {
                android.os.Handler().postDelayed({ getView()?.swapDataSuccess() }, 500L)
            }
        })
    }
}