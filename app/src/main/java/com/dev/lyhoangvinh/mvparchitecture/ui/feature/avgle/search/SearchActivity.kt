package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.search

import android.os.Bundle
import android.text.TextUtils
import android.transition.TransitionInflater
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.activity.BaseSwipeRecyclerViewActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.videos.VideosAdapter
import com.dev.lyhoangvinh.mvparchitecture.utils.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.view_error_connection.*
import kotlinx.android.synthetic.main.view_recyclerview.*
import javax.inject.Inject

class SearchActivity : BaseSwipeRecyclerViewActivity<VideosAdapter, SearchView, SearchPresenter>(), SearchView {

    @Inject
    lateinit var navigatorHelper: NavigatorHelper

    override fun getLayoutResource() = R.layout.activity_search


    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent()?.inject(this)
        super.onCreate(savedInstanceState)
        setStatusBarColor(R.color.colorWhite)
        window.enterTransition =
            TransitionInflater.from(this).inflateTransition(R.transition.changebounds_with_arcmotion)
        presenter.observe()
        imvBack.setOnClickListener { onBackPressed() }
        edtSearch.textChanges {
            if (!TextUtils.isEmpty(it)) {
                presenter.setKeyword(it)
            }
        }
        rcv.hideKeyPost(this)
        refreshWithUi(300L)
    }

    override fun createAdapter() = presenter.getAdapter()!!

    override fun connection(isConnected: Boolean) {
        viewErrorConnection.setVisible(!isConnected)
    }

    override fun openDetail(url: String) {
        navigatorHelper.navigateDetailActivity(url)
    }

}