package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle

import com.dev.lyhoangvinh.mvparchitecture.ui.base.activity.BaseSingleFragmentActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.category.CategoriesFragment

class AvgleActivity : BaseSingleFragmentActivity<CategoriesFragment>() {

    override fun createFragment() = CategoriesFragment()

}