package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.search

import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BaseListPresenter
import javax.inject.Inject

@PerActivity
class SearchPresenter @Inject constructor(@ActivityContext context: Context) : BaseListPresenter<SearchView>(context) {

    override fun fetchData() {

    }

    override fun canLoadMore() = false

}