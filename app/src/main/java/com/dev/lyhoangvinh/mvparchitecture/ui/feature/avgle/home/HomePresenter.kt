package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home

import android.arch.lifecycle.Observer
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.data.repo.HomeRepo
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BasePresenter
import com.dev.lyhoangvinh.mvparchitecture.utils.ConnectionLiveData
import javax.inject.Inject

@PerFragment
class HomePresenter @Inject constructor(
    @ActivityContext ctx: Context, private val homeRepo: HomeRepo,
    private val connectionLiveData: ConnectionLiveData
) :
    BasePresenter<HomeView>(ctx) {

    private var currentConnection = true

    fun getData() {

        connectionLiveData.observe(getLifeCircleOwner(), Observer {
            if (it!!.isConnected && !currentConnection) {
                execute(homeRepo.getRepoHome())
                currentConnection = true
            } else {
                currentConnection = false
            }
        })

        homeRepo.liveCategories().observe(getLifeCircleOwner(), Observer {
            getView()?.swapCategoriesSuccess(it!!)
        })
        homeRepo.liveCollection().observe(getLifeCircleOwner(), Observer {
            getView()?.swapCollectionsSuccess(it!!)
        })

        homeRepo.liveVideos().observe(getLifeCircleOwner(), Observer {
            getView()?.swapVideos(it!!)
        })
    }
}