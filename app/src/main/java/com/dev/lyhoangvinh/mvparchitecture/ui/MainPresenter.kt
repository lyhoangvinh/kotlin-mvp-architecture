package com.dev.lyhoangvinh.mvparchitecture.ui

import android.arch.lifecycle.Observer
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.BuildConfig
import com.dev.lyhoangvinh.mvparchitecture.base.api.ComicVineService
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.base.presenter.BaseListPresenter
import com.dev.lyhoangvinh.mvparchitecture.database.DatabaseManager
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Issues
import com.dev.lyhoangvinh.mvparchitecture.database.response.BaseResponse
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerActivity
import lyhoangvinh.com.myutil.thread.BackgroundThreadExecutor
import javax.inject.Inject

@PerActivity
class MainPresenter @Inject constructor(
    @ActivityContext context: Context,
    private val comicVineService: ComicVineService, private val databaseManager: DatabaseManager
) :
    BaseListPresenter<MainView>(context) {

    override fun canLoadMore(): Boolean = false

    override fun fetchData() {
        if (mainAdapter == null) {
            mainAdapter = MainAdapter(ArrayList())
        }
        databaseManager.issuesDao().liveData().observe(getLifeCircleOwner()!!, Observer {
            mainAdapter?.updateNotes(it!!)
            getView()?.size(it!!.size)
            getView()?.hideProgress()
        })
    }

    private var mainAdapter: MainAdapter? = null

    fun getMainAdapter(): MainAdapter? {
        if (mainAdapter == null)
            mainAdapter = MainAdapter(ArrayList())
        return mainAdapter
    }

    fun insert() {
        execute(true, comicVineService.getIssues2(
            100, 0, BuildConfig.API_KEY, "json",
            "cover_date: desc"
        ), responseConsumer = object :
            PlainConsumer<BaseResponse<Issues>> {
            override fun accept(t: BaseResponse<Issues>) {
                BackgroundThreadExecutor.getInstance().runOnBackground {
                    upsert(t.results)
                }
            }
        })
    }

    fun upsert(entities: List<Issues>) {
        databaseManager.issuesDao().insert2(entities)
        databaseManager.issuesDao().update2(entities)
    }
}