package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.collection

import android.arch.lifecycle.Observer
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.data.repo.CollectionRepo
import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.CollectionsResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.VideosResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BaseListPresenter
import com.dev.lyhoangvinh.mvparchitecture.utils.ConnectionLiveData
import javax.inject.Inject

@PerFragment
class CollectionPresenter @Inject constructor(
    @ActivityContext context: Context, private val collectionRepo: CollectionRepo,
    private val connectionLiveData: ConnectionLiveData
) :
    BaseListPresenter<CollectionView>(context) {

    private var currentConnected = true

    private var currentPage: Int = 0

    private var canLoadMore = false

    private var adapter: CollectionAdapter? = null

    override fun canLoadMore() = canLoadMore

    override fun fetchData() {
        if (isRefreshed) {
            currentPage = 0
        } else {
            currentPage += 1
        }
        execute(currentPage)
    }

    fun getAdapter(): CollectionAdapter? {
        if (adapter == null)
            adapter = CollectionAdapter()
        return adapter
    }

    fun observe() {
        adapter?.setOnItemClickListener {getView()?.openDetail(it)}
        collectionRepo.liveData().observe(getLifeCircleOwner(), Observer {
            adapter?.updateCollection(it!!)
            getView()?.hideProgress()
        })

        connectionLiveData.observe(getLifeCircleOwner(), Observer {
            getView()?.connection(it!!.isConnected)
            if (currentConnected && !it!!.isConnected) {
                currentConnected = it.isConnected
            } else if (!currentConnected && it!!.isConnected) {
                isRefreshed = true
                fetchData()
            }
        })
    }

    private fun execute(page: Int) {
        execute(collectionRepo.getRepoCollections(isRefreshed, page),
            object : PlainConsumer<BaseResponseAvgle<CollectionsResponseAvgle>>{
                override fun accept(t: BaseResponseAvgle<CollectionsResponseAvgle>) {
                    canLoadMore = t.response.hasMore
                    isRefreshed = false
                }
            }
        )
    }
}