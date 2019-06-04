package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.collection

import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BaseListPresenter
import javax.inject.Inject

@PerFragment
class CollectionPresenter @Inject constructor(@ActivityContext context: Context) :
    BaseListPresenter<CollectionView>(context) {


    override fun canLoadMore() = false

    override fun fetchData() {

    }


}