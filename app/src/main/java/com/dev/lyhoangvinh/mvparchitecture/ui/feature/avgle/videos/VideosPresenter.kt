package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.videos

import android.arch.lifecycle.Observer
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.data.repo.VideosRepo
import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.VideosResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BaseListPresenter
import com.dev.lyhoangvinh.mvparchitecture.utils.ConnectionLiveData
import javax.inject.Inject

@PerFragment
class VideosPresenter @Inject constructor(
    @ActivityContext context: Context, private val videosRepo: VideosRepo,
    private val connectionLiveData: ConnectionLiveData
) :
    BaseListPresenter<VideosView>(context) {

    private var chId = ""

    private var currentPage: Int = 0

    private var currentConnected = true

    private var canLoadMore = false

    private var adapter: VideosAdapter? = null

    override fun canLoadMore() = canLoadMore

    override fun fetchData() {
        if (isRefreshed) {
            currentPage = 0
        } else {
            currentPage += 1
        }
        execute(chId, currentPage)
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

    fun observe() {
        videosRepo.liveDataVideoAll().observe(getLifeCircleOwner(), Observer {
            adapter?.updateVideos(it!!)
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

    private fun execute(chId: String, page: Int) {
        execute(videosRepo.getRepoVideos(isRefreshed, chId, page),
            object : PlainConsumer<BaseResponseAvgle<VideosResponseAvgle>> {
                override fun accept(t: BaseResponseAvgle<VideosResponseAvgle>) {
                    canLoadMore = t.response.hasMore
                    isRefreshed = false
                }
            })
    }

    fun deleteAll() {
        videosRepo.deleteAll()
    }
}