package com.dev.lyhoangvinh.mvparchitecture.ui.feature.splash

import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.data.repo.HomeRepo
import com.dev.lyhoangvinh.mvparchitecture.data.response.*
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BasePresenter
import javax.inject.Inject

@PerActivity
class SplashPresenter @Inject constructor(@ActivityContext context: Context, private val homeRepo: HomeRepo) :
    BasePresenter<SplashView>(context) {
    fun getData() {
        execute(
            homeRepo.getRepoHome(),
            object :
                PlainConsumer<ResponseThreeZip<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>>> {
                override fun accept(t: ResponseThreeZip<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>>) {
                    getView()?.swapDataSuccess()
                }
            })
    }
}