package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.detail

import android.arch.lifecycle.Observer
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BasePresenter
import com.dev.lyhoangvinh.mvparchitecture.utils.ConnectionLiveData
import javax.inject.Inject

@PerActivity
class DetailPresenter @Inject constructor(@ActivityContext context: Context, private val connectionLiveData: ConnectionLiveData) :
    BasePresenter<DetailView>(context) {

    private var isReload = true

    fun observeConnection(url: String) {
        connectionLiveData.observe(getLifeCircleOwner(), Observer {
            getView()?.connection(it?.isConnected!!, url, isReload)
            isReload = true
        })
    }

    fun onPause() {
        isReload = false
    }
}

