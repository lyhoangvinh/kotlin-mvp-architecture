package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home

import android.arch.lifecycle.Observer
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.CategoryData
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.CollectionBannerData
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.CollectionBottomData
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.VideoData
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
        homeRepo.fetchData().observe(getLifeCircleOwner(), Observer {
            when (it) {
                is CategoryData -> getView()?.swapCategoriesSuccess(it.categoryItems)
                is CollectionBannerData -> getView()?.swapCollectionsBannerSuccess(it.collectionBannerItems)
                is CollectionBottomData -> getView()?.swapCollectionsBottomSuccess(it.collectionBottomItems)
                is VideoData -> getView()?.swapVideos(it.videoItems)
            }
        })
    }
}