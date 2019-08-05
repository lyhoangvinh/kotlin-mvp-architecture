package com.dev.lyhoangvinh.mvparchitecture.ui.feature.splash

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.activity.BasePresenterActivity
import com.dev.lyhoangvinh.mvparchitecture.utils.NavigatorHelper
import com.dev.lyhoangvinh.mvparchitecture.utils.progressbutton.attachTextChangeAnimator
import com.dev.lyhoangvinh.mvparchitecture.utils.progressbutton.bindProgressButton
import com.dev.lyhoangvinh.mvparchitecture.utils.progressbutton.showDrawable
import com.dev.lyhoangvinh.mvparchitecture.utils.progressbutton.showProgress
import com.dev.lyhoangvinh.mvparchitecture.utils.removeStatusBar
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

class SplashActivity : BasePresenterActivity<SplashView, SplashPresenter>(), SplashView {

    @Inject
    lateinit var navigatorHelper: NavigatorHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent()?.inject(this)
        super.onCreate(savedInstanceState)
        removeStatusBar()
        presenter.getData()
        tvLoading.attachTextChangeAnimator()
        bindProgressButton(tvLoading)
        tvLoading.showProgress {
            buttonTextRes = R.string.loading
            progressColor = Color.WHITE
        }

    }

    override fun getLayoutResource() = R.layout.activity_splash

    override fun swapDataSuccess() {
        val animatedDrawable = ContextCompat.getDrawable(this, R.drawable.animated_check)!!
        val drawableSize = resources.getDimensionPixelSize(R.dimen.doneSize)
        animatedDrawable.setBounds(0, 0, drawableSize, drawableSize)
        tvLoading.isEnabled = true
        tvLoading.showDrawable(animatedDrawable) {
            buttonTextRes = R.string.success
        }
        Handler().postDelayed({ navigatorHelper.navigateAvgleActivity(this) }, 300L)
    }
}