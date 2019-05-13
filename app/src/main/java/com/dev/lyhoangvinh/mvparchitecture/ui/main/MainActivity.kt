package com.dev.lyhoangvinh.mvparchitecture.ui.main

import android.os.Bundle
import android.widget.Toast
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.base.activity.BaseSwipeRecyclerViewActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseSwipeRecyclerViewActivity<MainAdapter, MainView, MainPresenter>(),
    MainView {
    override fun size(size: Int) {
        Toast.makeText(this, "SIZE : " + size, Toast.LENGTH_LONG).show()
    }

    override fun createAdapter(): MainAdapter = presenter.getMainAdapter()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent()?.inject(this)
        super.onCreate(savedInstanceState)
        refreshWithUi(300L)
        presenter.observe()
    }
}
