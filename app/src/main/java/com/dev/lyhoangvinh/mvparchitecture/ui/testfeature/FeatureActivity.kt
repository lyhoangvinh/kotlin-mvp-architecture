package com.dev.lyhoangvinh.mvparchitecture.ui.testfeature

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.base.activity.BasePresenterActivity
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.Item
import com.dev.lyhoangvinh.mvparchitecture.database.item.ImageItem
import com.dev.lyhoangvinh.mvparchitecture.database.item.TextItem
import com.dev.lyhoangvinh.mvparchitecture.ui.testfeature.adapter.FeatureAdapter
import kotlinx.android.synthetic.main.activity_feature.*
import java.util.*
import kotlin.collections.ArrayList

class FeatureActivity : BasePresenterActivity<FeatureView, FeaturePresenter>(), FeatureView {

    override fun getLayoutResource() = R.layout.activity_feature

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent()?.inject(this)
        super.onCreate(savedInstanceState)

        rcvFeature.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = FeatureAdapter()
        rcvFeature.adapter = adapter
        adapter.setItemList(hasItem())
    }

    private fun hasItem(): ArrayList<Item> {
        return ArrayList(
            Arrays.asList(
                TextItem(0, "asdas"),
                ImageItem(0, "assdjhakshd")
            )
        )
    }
}