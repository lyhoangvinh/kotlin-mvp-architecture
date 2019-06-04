package com.dev.lyhoangvinh.mvparchitecture.ui.feature.comics

import android.arch.lifecycle.Observer
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BaseListPresenter
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.comic.Issues
import com.dev.lyhoangvinh.mvparchitecture.data.repo.IssuesRepo
import com.dev.lyhoangvinh.mvparchitecture.data.response.BaseResponseComic
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerActivity
import com.dev.lyhoangvinh.mvparchitecture.utils.ConnectionLiveData
import javax.inject.Inject

@PerActivity
class IssuesPresenter @Inject constructor(
    @ActivityContext context: Context,
    private val connectionLive: ConnectionLiveData,
    private val issuesRepo: IssuesRepo
) : BaseListPresenter<IssuesView>(context) {

    private var canLoadMore = false

    private var currentConnected = true

    override fun canLoadMore(): Boolean = canLoadMore

    override fun fetchData() {
        insert()
    }

    private var mainAdapter: IssuesAdapter? = null

    fun getMainAdapter(): IssuesAdapter? {
        if (mainAdapter == null)
            mainAdapter = IssuesAdapter(ArrayList())
        return mainAdapter
    }

    fun observe() {
        if (mainAdapter == null) {
            mainAdapter = IssuesAdapter(ArrayList())
        }
        issuesRepo.liveData().observe(getLifeCircleOwner(), Observer {
            mainAdapter?.updateNotes(it!!)
            getView()?.showMessage("SIZE : " + it!!.size)
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
        execute(issuesRepo.getRepoIssues(isRefreshed), object : PlainConsumer<BaseResponseComic<Issues>> {
            override fun accept(t: BaseResponseComic<Issues>) {
                isRefreshed = false
                canLoadMore = t.results.isNotEmpty()
            }
        })
    }

    private var issues: Issues? = null

    fun delete(i: Int) {
        issues = getMainAdapter()?.getData()!![i]
        execute({ issuesRepo.delete(issues!!) }, object : () -> Unit {
            override fun invoke() {
                getView()?.deleteSuccess()
            }
        })
    }

    fun undo() {
        execute { issuesRepo.insert(issues!!) }
    }
}