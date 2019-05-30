package com.dev.lyhoangvinh.mvparchitecture.ui.feature.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.activity.BaseSwipeRecyclerViewActivity
import kotlinx.android.synthetic.main.view_error_connection.*

class MainActivity : BaseSwipeRecyclerViewActivity<MainAdapter, MainView, MainPresenter>(),
    MainView {

    override fun getLayoutResource() = R.layout.activity_main

    override fun createAdapter(): MainAdapter = presenter.getMainAdapter()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent()?.inject(this)
        super.onCreate(savedInstanceState)
        refreshWithUi(300L)
        presenter.observe()
    }

    override fun connection(isConnected: Boolean) {
        viewErrorConnection.visibility = if (isConnected) View.GONE else View.VISIBLE
    }

    override fun size(size: Int) {
        Toast.makeText(this, "SIZE : " + size, Toast.LENGTH_LONG).show()
    }
}
