package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home

import android.arch.lifecycle.Observer
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.data.repo.HomeRepo
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BasePresenter
import javax.inject.Inject

@PerFragment
class HomePresenter @Inject constructor(
    @ActivityContext ctx: Context, private val homeRepo: HomeRepo
) : BasePresenter<HomeView>(ctx) {

    fun getData() {

        homeRepo.liveCategories().observe(getLifeCircleOwner(), Observer {
            getView()?.swapCategoriesSuccess(it!!)
        })
        homeRepo.liveCollection().observe(getLifeCircleOwner(), Observer {
            getView()?.swapCollectionsSuccess(it!!)
        })

        homeRepo.liveVideosHome().observe(getLifeCircleOwner(), Observer {
            getView()?.swapVideos(it!!)
        })
    }
}