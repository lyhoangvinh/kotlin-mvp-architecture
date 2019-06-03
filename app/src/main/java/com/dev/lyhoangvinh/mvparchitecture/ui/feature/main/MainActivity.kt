package com.dev.lyhoangvinh.mvparchitecture.ui.feature.main

import android.os.Bundle
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.Toast
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.activity.BaseSwipeRecyclerViewActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.ui.widget.SwipeToDeleteCallback
import com.dev.lyhoangvinh.mvparchitecture.utils.setVisible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_error_connection.*
import lyhoangvinh.com.myutil.androidutils.AlertUtils

class MainActivity : BaseSwipeRecyclerViewActivity<MainAdapter, MainView, MainPresenter>(),
    MainView {

    override fun getLayoutResource() = R.layout.activity_main

    override fun createAdapter(): MainAdapter = presenter.getMainAdapter()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent()?.inject(this)
        super.onCreate(savedInstanceState)
        refreshWithUi(300L)
        presenter.observe()
        ItemTouchHelper(SwipeToDeleteCallback(this@MainActivity, object : PlainConsumer<Int> {
            override fun accept(t: Int) {
                presenter.delete(t)
            }
        })).attachToRecyclerView(recyclerView)
    }

    override fun connection(isConnected: Boolean) {
        viewErrorConnection.setVisible(!isConnected)
    }

    override fun deleteSuccess() {
        AlertUtils.showSnackBarShortMessage(root, "Issues is deleted", "Undo") { presenter.undo() }
    }

    override fun size(size: Int) {
        Toast.makeText(this, "SIZE : " + size, Toast.LENGTH_LONG).show()
    }
}
