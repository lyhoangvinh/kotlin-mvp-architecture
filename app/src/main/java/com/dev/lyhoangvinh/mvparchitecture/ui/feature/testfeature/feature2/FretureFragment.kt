package com.dev.lyhoangvinh.mvparchitecture.ui.feature.testfeature.feature2

import android.content.Context
import android.view.View
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.fragment.BaseFragment

class FretureFragment : BaseFragment() {

    override fun initialize(view: View, ctx: Context?) {
        showMessage("XXX")
    }

    override fun getLayoutResource() = R.layout.view_no_data

}