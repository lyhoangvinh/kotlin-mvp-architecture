package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.collection

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.data.repo.CollectionRepo
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BaseListPresenter
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.category.CategoriesAdapter
import com.dev.lyhoangvinh.mvparchitecture.utils.ConnectionLiveData
import javax.inject.Inject

@PerFragment
class CollectionPresenter @Inject constructor(
    @ActivityContext context: Context, private val collectionRepo: CollectionRepo,
    private val connectionLiveData: ConnectionLiveData
) :
    BaseListPresenter<CollectionView>(context) {

    private var keyword = ""

    private var currentConnected = true

    private var adapter: CollectionAdapter? = null

    override fun canLoadMore() = false

    override fun fetchData() {
        execute(keyword)
        observe(keyword, getLifeCircleOwner())
    }

    fun getAdapter(): CollectionAdapter? {
        if (adapter == null)
            adapter = CollectionAdapter()
        return adapter
    }

    fun setKeyword(keyword: String) {
        this.keyword = keyword
        adapter?.setOnItemClickListener { getView()?.openDetail(it) }
    }

    private fun observe(keyword: String, owner: LifecycleOwner) {
        collectionRepo.liveData().observe(owner, Observer {
            adapter?.updateCollection(it!!)
            getView()?.showMessage("SIZE : " + it!!.size)
        })

        connectionLiveData.observe(owner, Observer {
            getView()?.connection(it!!.isConnected)
            if (currentConnected && !it!!.isConnected) {
                currentConnected = it.isConnected
            } else if (!currentConnected && it!!.isConnected) {
                isRefreshed = true
                execute(keyword)
            }
        })
    }

    private fun execute(keyword: String) {
        execute(collectionRepo.getRepoCollections(keyword))
    }
}