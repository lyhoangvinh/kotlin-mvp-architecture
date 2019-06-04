package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.category

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.data.repo.CategoriesRepo
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BaseListPresenter
import com.dev.lyhoangvinh.mvparchitecture.utils.ConnectionLiveData
import javax.inject.Inject

@PerFragment
class CategoriesPresenter @Inject constructor(
    @ActivityContext context: Context, private val categoriesRepo: CategoriesRepo,
    private val connectionLiveData: ConnectionLiveData
) :
    BaseListPresenter<CategoriesView>(context) {

    private var currentConnected = true

    private var adapter: CategoriesAdapter? = null

    override fun canLoadMore() = false

    override fun fetchData() {
        execute()
        observe(getLifeCircleOwner())
        adapter?.setOnClickItemListener { getView()?.openDetail(it) }
    }

    fun getAdapter(): CategoriesAdapter? {
        if (adapter == null)
            adapter = CategoriesAdapter()
        return adapter
    }

    private fun observe(owner: LifecycleOwner) {
        categoriesRepo.liveData().observe(owner, Observer {
            adapter?.updateCategories(it!!)
            getView()?.showMessage("SIZE : " + it!!.size)
        })

        connectionLiveData.observe(owner, Observer {
            getView()?.connection(it!!.isConnected)
            if (currentConnected && !it!!.isConnected) {
                currentConnected = it.isConnected
            } else if (!currentConnected && it!!.isConnected) {
                isRefreshed = true
                execute()
            }
        })
    }

    private fun execute() {
        execute(categoriesRepo.getRepoCategories())
    }
}