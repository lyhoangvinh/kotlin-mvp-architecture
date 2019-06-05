package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.category


import android.content.Context
import android.os.Bundle
import android.view.View
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.fragment.BaseSwipeRecyclerViewFragment
import com.dev.lyhoangvinh.mvparchitecture.utils.NavigatorHelper
import com.dev.lyhoangvinh.mvparchitecture.utils.setVisible
import kotlinx.android.synthetic.main.view_error_connection.*
import javax.inject.Inject

class CategoriesFragment : BaseSwipeRecyclerViewFragment<CategoriesAdapter, CategoriesView, CategoriesPresenter>(),
    CategoriesView {

    @Inject
    lateinit var navigatorHelper: NavigatorHelper

    override fun getLayoutResource() = R.layout.activity_main

    override fun createAdapter() = presenter.getAdapter()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent()?.inject(this)
    }

    override fun initialize(view: View, ctx: Context?) {
        super.initialize(view, ctx)
        refreshWithUi(300L)
    }

    override fun connection(isConnected: Boolean) {
        viewErrorConnection.setVisible(!isConnected)
    }

    override fun openCollection(keyword: String) {
        navigatorHelper.navigateCollectionFragment(keyword)
    }
}