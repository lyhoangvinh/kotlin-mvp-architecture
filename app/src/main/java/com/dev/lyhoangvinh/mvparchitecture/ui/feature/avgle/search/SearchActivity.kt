package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.search

import com.dev.lyhoangvinh.mvparchitecture.ui.base.activity.BaseSwipeRecyclerViewActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.videos.VideosAdapter

class SearchActivity : BaseSwipeRecyclerViewActivity<VideosAdapter, SearchView, SearchPresenter>(), SearchView {
    override fun createAdapter() = VideosAdapter()

}