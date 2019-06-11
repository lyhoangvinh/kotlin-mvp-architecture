package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle

import com.dev.lyhoangvinh.mvparchitecture.ui.base.activity.BaseSingleFragmentActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.category.CategoriesFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home.HomeFragment

class AvgleActivity : BaseSingleFragmentActivity<HomeFragment>() {

    override fun createFragment() = HomeFragment()

}