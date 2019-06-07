package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home

import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.data.repo.HomeRepo
import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.CategoriesResponse
import com.dev.lyhoangvinh.mvparchitecture.data.response.CollectionsResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.ResponseBiZip
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainResponseZipBiConsumer
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BasePresenter
import javax.inject.Inject

@PerFragment
class HomePresenter @Inject constructor(@ActivityContext ctx: Context, private val homeRepo: HomeRepo) :
    BasePresenter<HomeView>(ctx) {

    fun getData() {
        execute(
            homeRepo.getRepoHome(),
            object :
                PlainConsumer<ResponseBiZip<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>>> {
                override fun accept(t: ResponseBiZip<BaseResponseAvgle<CategoriesResponse>, BaseResponseAvgle<CollectionsResponseAvgle>>) {
                    getView()?.swapDataSuccess(t.t1?.response?.categories!!, t.t2?.response?.collections!!)
                }
            })
    }
}