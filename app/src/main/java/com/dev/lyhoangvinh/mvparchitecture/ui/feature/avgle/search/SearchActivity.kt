package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionInflater
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.activity.BaseSwipeRecyclerViewActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.videos.VideosAdapter
import com.dev.lyhoangvinh.mvparchitecture.utils.NavigatorHelper
import com.dev.lyhoangvinh.mvparchitecture.utils.hideKeyPost
import com.dev.lyhoangvinh.mvparchitecture.utils.setVisible
import com.dev.lyhoangvinh.mvparchitecture.utils.textChanges
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.view_error_connection.*
import kotlinx.android.synthetic.main.view_recyclerview.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchActivity : BaseSwipeRecyclerViewActivity<VideosAdapter, SearchView, SearchPresenter>(), SearchView {

    @Inject
    lateinit var navigatorHelper: NavigatorHelper

    override fun getLayoutResource() = R.layout.activity_search


    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent()?.inject(this)
        super.onCreate(savedInstanceState)
        window.enterTransition =
            TransitionInflater.from(this).inflateTransition(R.transition.changebounds_with_arcmotion)
        presenter.observe()
        imvBack.setOnClickListener { onBackPressed() }
        edtSearch.textChanges { presenter.setKeyword(it) }
        rcv.hideKeyPost(this)
    }

    override fun createAdapter() = presenter.getAdapter()!!

    override fun connection(isConnected: Boolean) {
        viewErrorConnection.setVisible(!isConnected)
    }

    override fun openDetail(url: String) {
        navigatorHelper.navigateDetailActivity(url)
    }

}