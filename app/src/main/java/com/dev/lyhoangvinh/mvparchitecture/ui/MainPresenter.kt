package com.dev.lyhoangvinh.mvparchitecture.ui

import android.arch.lifecycle.Observer
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.base.presenter.BaseListPresenter
import com.dev.lyhoangvinh.mvparchitecture.database.DatabaseManager
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Comics
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerActivity
import lyhoangvinh.com.myutil.thread.BackgroundThreadExecutor
import javax.inject.Inject

@PerActivity
class MainPresenter @Inject constructor(@ActivityContext context: Context, private val databaseManager: DatabaseManager) :
    BaseListPresenter<MainView>(context) {

    override fun canLoadMore(): Boolean = false

    override fun fetchData() {
        if (mainAdapter == null) {
            mainAdapter = MainAdapter(ArrayList())
        }
        databaseManager.comicsDao().liveData().observe(getLifeCircleOwner()!!, Observer {
            getView()?.getDataSuccess(it!!)
            mainAdapter?.updateNotes(it!!)
        })
    }

    private var mainAdapter: MainAdapter? = null

    fun getMainAdapter(): MainAdapter? {
        if (mainAdapter == null)
            mainAdapter = MainAdapter(ArrayList())
        return mainAdapter
    }

    fun insert() {
//        getView()?.showProgress()

        BackgroundThreadExecutor.getInstance().runOnBackground {
            val comics = Comics(publisher = "xxxx")
            databaseManager.comicsDao().insert(comics)
        }



        getView()?.getDataSuccess(emptyList())


    }

    fun getBook() {
        getView()?.getDataSuccess(emptyList())
        addRequestSingle(true, databaseManager.comicsDao().getAll(), responseConsumer = object :
            PlainConsumer<List<Comics>> {
            override fun accept(t: List<Comics>) {
                getView()?.getDataSuccess(t)
            }
        })
    }
}