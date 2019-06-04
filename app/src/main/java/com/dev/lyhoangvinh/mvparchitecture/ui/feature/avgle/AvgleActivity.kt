package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle

import android.os.Bundle
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.activity.BaseSwipeRecyclerViewActivity
import com.dev.lyhoangvinh.mvparchitecture.utils.NavigatorHelper
import com.dev.lyhoangvinh.mvparchitecture.utils.setVisible
import kotlinx.android.synthetic.main.view_error_connection.*
import javax.inject.Inject

class AvgleActivity : BaseSwipeRecyclerViewActivity<CategoriesAdapter, AvgleView, AvglePresenter>(), AvgleView {

    override fun createAdapter() = presenter.getAdapter()!!

    override fun getLayoutResource() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent()?.inject(this)
        super.onCreate(savedInstanceState)
        refreshWithUi(300L)
    }

    override fun connection(isConnected: Boolean) {
        viewErrorConnection.setVisible(!isConnected)
    }

}