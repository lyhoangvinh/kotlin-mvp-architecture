package com.dev.lyhoangvinh.mvparchitecture.ui

import android.os.Bundle
import android.widget.Toast
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.base.activity.BaseSwipeRecyclerViewActivity
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Comics
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseSwipeRecyclerViewActivity<MainAdapter, MainView, MainPresenter>(),
    MainView {

    override fun createAdapter(): MainAdapter = presenter.getMainAdapter()!!

    override fun getLayoutResource() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent()?.inject(this)
        super.onCreate(savedInstanceState)
        refreshWithUi(300L)
        btnInsert.setOnClickListener { presenter.insert() }
    }

    override fun getDataSuccess(list: List<Comics>) {
        var size = 0
        if (list != null) {
            size = list.size
        }
        Toast.makeText(this, "SIZE : " + size, Toast.LENGTH_LONG).show()
    }

}
