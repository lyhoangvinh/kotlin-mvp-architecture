package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.videos

import android.content.Context
import android.os.Bundle
import android.view.View
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Category
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Collection
import com.dev.lyhoangvinh.mvparchitecture.ui.base.fragment.BaseSwipeRecyclerViewFragment
import com.dev.lyhoangvinh.mvparchitecture.utils.NavigatorHelper
import com.dev.lyhoangvinh.mvparchitecture.utils.setStatusBarGradient
import com.dev.lyhoangvinh.mvparchitecture.utils.setVisible
import kotlinx.android.synthetic.main.toolbar_back.*
import kotlinx.android.synthetic.main.view_error_connection.*
import javax.inject.Inject

class VideosFragment : BaseSwipeRecyclerViewFragment<VideosFooterAdapter, VideosView, VideosPresenter>(),
    VideosView {

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
        setStatusBarGradient()
        if (arguments != null) {
            if (arguments!!.getParcelable<Category>(Constants.EXTRA_DATA) is Category) {
                val category: Category = arguments!!.getParcelable(Constants.EXTRA_DATA)!!
                presenter.setKeyword(category.CHID!!)
                tvTitleToolBar.text = category.name
            } else if (arguments!!.getParcelable<Collection>(Constants.EXTRA_DATA) is Collection) {
                val collection: Collection = arguments!!.getParcelable(Constants.EXTRA_DATA)!!
                presenter.setKeyword(collection.keyword!!)
                tvTitleToolBar.text = collection.title
            }
        } else {
            presenter.setKeyword("")
            tvTitleToolBar.text = getString(R.string.all)
        }
        refreshWithUi(300L)
        imvBack.setOnClickListener { onBackPressed() }
        presenter.observe()
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