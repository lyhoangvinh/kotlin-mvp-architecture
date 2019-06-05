package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.collection

import android.content.Context
import android.os.Bundle
import android.view.View
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Category
import com.dev.lyhoangvinh.mvparchitecture.ui.base.fragment.BaseSwipeRecyclerViewFragment
import com.dev.lyhoangvinh.mvparchitecture.utils.NavigatorHelper
import com.dev.lyhoangvinh.mvparchitecture.utils.setVisible
import kotlinx.android.synthetic.main.toolbar_default.*
import kotlinx.android.synthetic.main.view_error_connection.*
import javax.inject.Inject

class CollectionFragment : BaseSwipeRecyclerViewFragment<CollectionAdapter, CollectionView, CollectionPresenter>(),
    CollectionView {

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
        if (arguments != null) {
            val category: Category = arguments!!.getParcelable(Constants.EXTRA_DATA)!!
            presenter.setKeyword(category.slug!!)
            tvText.text = category.name
        }
        refreshWithUi(300L)
    }

    override fun connection(isConnected: Boolean) {
        viewErrorConnection.setVisible(!isConnected)
    }

    override fun openDetail(url: String) {
        navigatorHelper.navigateDetailActivity(url)
    }

    override fun onBackPressed(): Boolean {
        fragmentManager?.popBackStack()
        return true
    }
}