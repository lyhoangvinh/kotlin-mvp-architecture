package com.dev.lyhoangvinh.mvparchitecture.ui.main

import android.arch.lifecycle.Observer
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.BuildConfig
import com.dev.lyhoangvinh.mvparchitecture.base.api.ComicVineService
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.base.presenter.BaseListPresenter
import com.dev.lyhoangvinh.mvparchitecture.database.dao.IssuesDao
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Issues
import com.dev.lyhoangvinh.mvparchitecture.database.response.BaseResponse
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerActivity
import lyhoangvinh.com.myutil.thread.BackgroundThreadExecutor
import javax.inject.Inject

@PerActivity
class MainPresenter @Inject constructor(
    @ActivityContext context: Context,
    private val comicVineService: ComicVineService, private val issuesDao: IssuesDao
) :
    BaseListPresenter<MainView>(context) {

    private var offset = 0

    private var canLoadMore = false

    override fun canLoadMore(): Boolean = canLoadMore

    override fun fetchData() {
        insert(offset)
    }

    private var mainAdapter: MainAdapter? = null

    fun getMainAdapter(): MainAdapter? {
        if (mainAdapter == null)
            mainAdapter = MainAdapter(ArrayList())
        return mainAdapter
    }

    fun observe() {
        if (mainAdapter == null) {
            mainAdapter = MainAdapter(ArrayList())
        }
        issuesDao.liveData().observe(getLifeCircleOwner()!!, Observer {
            mainAdapter?.updateNotes(it!!)
            getView()?.size(it!!.size)
            getView()?.hideProgress()
        })
    }

    fun insert(page: Int) {
        execute(true, comicVineService.getIssues2(
            50, page, BuildConfig.API_KEY,
            "json",
            "cover_date: desc"
        ), object : PlainConsumer<BaseResponse<Issues>> {
            override fun accept(t: BaseResponse<Issues>) {
                if (t.results.isNotEmpty()) {
                    canLoadMore = true
                    BackgroundThreadExecutor.getInstance().runOnBackground {
                        offset = page + 1
                        upsert(t.results)
                    }
                } else {
                    canLoadMore = false
                }
            }
        })
    }

    fun upsert(entities: List<Issues>) {
        issuesDao.insertIgnore(entities)
        issuesDao.updateIgnore(entities)
    }
}