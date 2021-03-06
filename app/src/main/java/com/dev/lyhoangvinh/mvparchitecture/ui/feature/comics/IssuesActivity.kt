package com.dev.lyhoangvinh.mvparchitecture.ui.feature.comics

import android.os.Bundle
import android.support.v7.widget.helper.ItemTouchHelper
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.activity.BaseSwipeRecyclerViewActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.PlainConsumer
import com.dev.lyhoangvinh.mvparchitecture.ui.widget.SwipeToDeleteCallback
import com.dev.lyhoangvinh.mvparchitecture.utils.setVisible
import kotlinx.android.synthetic.main.fragment_all.*
import kotlinx.android.synthetic.main.toolbar_back.*
import kotlinx.android.synthetic.main.view_error_connection.*
import kotlinx.android.synthetic.main.view_recyclerview.*
import lyhoangvinh.com.myutil.androidutils.AlertUtils

class IssuesActivity : BaseSwipeRecyclerViewActivity<IssuesAdapter, IssuesView, IssuesPresenter>(),
    IssuesView {

    override fun getLayoutResource() = R.layout.fragment_all

    override fun createAdapter(): IssuesAdapter = presenter.getMainAdapter()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent()?.inject(this)
        super.onCreate(savedInstanceState)
        refreshWithUi(300L)
        presenter.observe()
        ItemTouchHelper(SwipeToDeleteCallback(this@IssuesActivity, object : PlainConsumer<Int> {
            override fun accept(t: Int) {
                presenter.delete(t)
            }
        })).attachToRecyclerView(rcv)
        imvBack.setOnClickListener { onBackPressed() }
    }

    override fun connection(isConnected: Boolean) {
        viewErrorConnection.setVisible(!isConnected)
    }

    override fun deleteSuccess() {
        AlertUtils.showSnackBarShortMessage(root, "Issues is deleted", "Undo") { presenter.undo() }
    }
}
