package com.dev.lyhoangvinh.mvparchitecture.ui.feature.splash

import android.os.Bundle
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.activity.BasePresenterActivity
import com.dev.lyhoangvinh.mvparchitecture.utils.NavigatorHelper
import javax.inject.Inject

class SplashActivity : BasePresenterActivity<SplashView, SplashPresenter>(), SplashView {

    @Inject
    lateinit var navigatorHelper: NavigatorHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent()?.inject(this)
        super.onCreate(savedInstanceState)
        presenter.getData()
    }

    override fun getLayoutResource() = R.layout.activity_splash

    override fun swapDataSuccess() {
        navigatorHelper.navigateAvgleActivity(this)
    }
}