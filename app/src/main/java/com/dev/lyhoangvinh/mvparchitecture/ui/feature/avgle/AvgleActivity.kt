package com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle

import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.activity.BaseSingleFragmentActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.category.CategoriesFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.feature.avgle.home.HomeFragment

class AvgleActivity : BaseSingleFragmentActivity<HomeFragment>() {
    override fun createFragment(): HomeFragment {
        window.enterTransition.duration = resources.getInteger(R.integer.anim_duration_medium).toLong()
        return HomeFragment()
    }

}