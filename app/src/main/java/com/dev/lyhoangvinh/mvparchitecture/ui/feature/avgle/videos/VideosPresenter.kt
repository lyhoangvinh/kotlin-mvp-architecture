package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.videos

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.data.repo.CollectionRepo
import com.dev.lyhoangvinh.mvparchitecture.data.repo.VideosRepo
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BaseListPresenter
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.category.CategoriesAdapter
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.collection.CollectionAdapter
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.collection.CollectionView
import com.dev.lyhoangvinh.mvparchitecture.utils.ConnectionLiveData
import javax.inject.Inject

@PerFragment
class VideosPresenter @Inject constructor(
    @ActivityContext context: Context, private val videosRepo: VideosRepo,
    private val connectionLiveData: ConnectionLiveData
) :
    BaseListPresenter<VideosView>(context) {

    private var chId = ""

    private var currentConnected = true

    private var adapter: VideosAdapter? = null

    override fun canLoadMore() = false

    override fun fetchData() {
        execute(chId)
        observe(chId, getLifeCircleOwner())
    }

    fun getAdapter(): VideosAdapter? {
        if (adapter == null)
            adapter = VideosAdapter()
        return adapter
    }

    fun setKeyword(chId: String) {
        this.chId = chId
        adapter?.setOnItemClickListener { getView()?.openDetail(it) }
    }

    private fun observe(chId: String, owner: LifecycleOwner) {
        videosRepo.liveData().observe(owner, Observer {
            adapter?.updateVideos(it!!)
            getView()?.showMessage("SIZE : " + it!!.size)
        })

        connectionLiveData.observe(owner, Observer {
            getView()?.connection(it!!.isConnected)
            if (currentConnected && !it!!.isConnected) {
                currentConnected = it.isConnected
            } else if (!currentConnected && it!!.isConnected) {
                isRefreshed = true
                execute(chId)
            }
        })
    }

    private fun execute(chId: String) {
        execute(videosRepo.getRepoVideos(chId))
    }

    fun deleteAll() {
        videosRepo.deleteAll()
    }
}