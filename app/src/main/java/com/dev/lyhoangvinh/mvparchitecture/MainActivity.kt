package com.dev.lyhoangvinh.mvparchitecture

import android.os.Bundle
import android.widget.Toast
import com.dev.lyhoangvinh.mvparchitecture.base.activity.BasePresenterActivity
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Comics
import com.dev.lyhoangvinh.mvparchitecture.utils.NavigatorHelper
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BasePresenterActivity<MainView, MainPresenter>(), MainView {

    @Inject
    lateinit var navigatorHelper: NavigatorHelper

    override fun getLayoutResource() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent()?.inject(this)
        initialize()
        tvHL.setOnClickListener { presenter.insert() }
    }

    override fun getDataSuccess(list: List<Comics>) {
        var size = 0
        if (list != null) {
            size = list.size
        }
        Toast.makeText(this, "SIZE : " + size, Toast.LENGTH_LONG).show()
    }

}
