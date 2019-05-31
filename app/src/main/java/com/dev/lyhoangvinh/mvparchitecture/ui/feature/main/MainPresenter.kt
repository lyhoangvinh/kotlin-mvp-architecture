package com.dev.lyhoangvinh.mvparchitecture.ui.feature.main

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Handler
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BaseListPresenter
import com.dev.lyhoangvinh.mvparchitecture.database.dao.IssuesDao
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Issues
import com.dev.lyhoangvinh.mvparchitecture.database.repo.IssuesRepo
import com.dev.lyhoangvinh.mvparchitecture.database.response.BaseResponse
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerActivity
import com.dev.lyhoangvinh.mvparchitecture.utils.ConnectionLiveData
import javax.inject.Inject

@PerActivity
class MainPresenter @Inject constructor(
    @ActivityContext context: Context,
    private val connectionLive: ConnectionLiveData,
    private val issuesDao: IssuesDao,
    private val issuesRepo: IssuesRepo
) : BaseListPresenter<MainView>(context) {

    private var canLoadMore = false

    private var currentConnected = true

    override fun canLoadMore(): Boolean = canLoadMore

    override fun fetchData() {
        insert()
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
        issuesDao.liveData().observe(getLifeCircleOwner(), Observer {
            mainAdapter?.updateNotes(it!!)
            getView()?.size(it!!.size)
            getView()?.hideProgress()
        })

        connectionLive.observe(getLifeCircleOwner(), Observer {
            getView()?.connection(it!!.isConnected)
            if (currentConnected && !it!!.isConnected) {
                currentConnected = it.isConnected
            } else if (!currentConnected && it!!.isConnected) {
                isRefreshed = true
                insert()
            }
        })
    }

    private fun insert() {
        execute(issuesRepo.getRepoIssues(isRefreshed), object : PlainConsumer<BaseResponse<Issues>> {
            override fun accept(t: BaseResponse<Issues>) {
                isRefreshed = false
                canLoadMore = t.results.isNotEmpty()
            }
        })
    }

    private var issues: Issues? = null

    fun delete(i: Int) {
        issues = getMainAdapter()?.getData()!![i]
        execute({ issuesDao.delete(issues!!) }, object : () -> Unit {
            override fun invoke() {
                getView()?.deleteSuccess()
            }
        })
    }

    fun undo() {
        execute { issuesDao.insert(issues!!) }
    }
}