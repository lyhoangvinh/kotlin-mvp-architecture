package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.search

import android.arch.lifecycle.Observer
import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.dev.lyhoangvinh.mvparchitecture.data.repo.VideosRepo
import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.data.response.ResponseBiZip
import com.dev.lyhoangvinh.mvparchitecture.data.response.VideosResponseAvgle
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BaseListPresenter
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.videos.VideosAdapter
import com.dev.lyhoangvinh.mvparchitecture.utils.ConnectionLiveData
import javax.inject.Inject

@PerActivity
class SearchPresenter @Inject constructor(
    @ActivityContext context: Context, private val videosRepo: VideosRepo,
    private val connectionLiveData: ConnectionLiveData
) :
    BaseListPresenter<SearchView>(context) {

    private var canLoadMore = false

    private var chId = ""

    private var currentConnected = true

    private var currentPage: Int = 0

    private var adapter: VideosAdapter? = null

    override fun fetchData() {
        if (isRefreshed) {
            currentPage = 0
        } else {
            currentPage += 1
        }
        execute(chId, currentPage)
    }

    override fun canLoadMore() = canLoadMore

    private fun execute(chId: String, page: Int) {
        execute(videosRepo.search(isRefreshed, chId, page),
            object :
                PlainConsumer<ResponseBiZip<BaseResponseAvgle<VideosResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>>> {
                override fun accept(t: ResponseBiZip<BaseResponseAvgle<VideosResponseAvgle>, BaseResponseAvgle<VideosResponseAvgle>>) {
                    canLoadMore = true
                    isRefreshed = false
                }
            })
    }

    fun observe() {
        adapter?.setOnItemClickListener { getView()?.openDetail(it) }
        videosRepo.liveDataSearch().observe(getLifeCircleOwner(), Observer {
            adapter?.updateVideos(it!!)
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


    fun getAdapter(): VideosAdapter? {
        if (adapter == null)
            adapter = VideosAdapter()
        return adapter
    }


    fun setKeyword(chId: String) {
        this.chId = chId
        isRefreshed = true
        fetchData()
    }
}