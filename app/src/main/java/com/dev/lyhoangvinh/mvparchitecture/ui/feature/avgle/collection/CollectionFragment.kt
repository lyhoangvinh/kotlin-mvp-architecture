package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.collection

import android.content.Context
import android.os.Bundle
import android.view.View
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
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

    override fun getLayoutResource() = R.layout.fragment_all

    override fun createAdapter() = presenter.getAdapter()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent()?.inject(this)
    }

    override fun initialize(view: View, ctx: Context?) {
        super.initialize(view, ctx)
        refreshWithUi(300L)
        presenter.observe()
        tvText.text = getString(R.string.collection)
    }

    override fun connection(isConnected: Boolean) {
        viewErrorConnection.setVisible(!isConnected)
    }

    override fun openDetail(collection: Collection) {
        navigatorHelper.navigateVideosFragment(collection)
    }

    override fun onBackPressed(): Boolean {
        fragmentManager?.popBackStack()
        return true
    }
}