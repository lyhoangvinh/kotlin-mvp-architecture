package com.dev.lyhoangvinh.mvparchitecture

import android.arch.lifecycle.Observer
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.base.BasePresenter
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.database.DatabaseManager
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Comics
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerActivity
import lyhoangvinh.com.myutil.thread.BackgroundThreadExecutor
import javax.inject.Inject

@PerActivity
class MainPresenter @Inject constructor(@ActivityContext context: Context, databaseManager: DatabaseManager) :
    BasePresenter<MainView>(context, databaseManager) {

    fun insert() {
//        getView()?.showProgress()

        BackgroundThreadExecutor.getInstance().runOnBackground {
            val comics = Comics(publisher = "xxxx")
            databaseManager.comicsDao().insert(comics)
        }


        getLifeCircleOwner()?.let {
            databaseManager.comicsDao().liveData().observe(it, Observer {
                it?.let { it1 ->
                    getView()?.getDataSuccess(it1)
//                                getView()?.hideProgress()

                }
            })
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