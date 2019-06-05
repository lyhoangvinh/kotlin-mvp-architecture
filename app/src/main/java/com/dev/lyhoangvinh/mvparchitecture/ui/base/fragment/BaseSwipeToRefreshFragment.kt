package com.dev.lyhoangvinh.mvparchitecture.ui.base.fragment

import android.content.Context
import android.support.annotation.CallSuper
import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.View
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.BaseView
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.UiRefreshable
import com.dev.lyhoangvinh.mvparchitecture.ui.base.presenter.BasePresenter
import kotlinx.android.synthetic.main.swipe_recycler_view.*
import lyhoangvinh.com.myutil.view.AppBarStateChangeListener

abstract class BaseSwipeToRefreshFragment<V : BaseView, P : BasePresenter<V>> : BasePresenterFragment<V, P>(),
    SwipeRefreshLayout.OnRefreshListener, UiRefreshable {

    protected var isRefresh = false

    private var shouldRefreshUi = true

    override fun initialize(view: View, ctx: Context?) {
        super.initialize(view, ctx)
        initSwipeToRefresh()
    }

    private fun initSwipeToRefresh() {
        srl!!.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
        srl!!.setOnRefreshListener(this)
    }

    override fun onStop() {
        super.onStop()
        doneRefresh()
    }

    override fun setRefreshEnabled(enabled: Boolean) {
        srl!!.isEnabled = enabled
    }

    override fun showProgress() {
        refreshWithUi()
    }

    @CallSuper
    override fun doneRefresh() {
        isRefresh = false
        shouldRefreshUi = false
        if (srl != null) {
            srl!!.isRefreshing = false
        }
    }

    @CallSuper
    override fun onRefresh() {
        if (!isRefresh) {
            isRefresh = true
            refresh()
        }
    }


    override fun refreshWithUi(delay: Long) {
        if (srl != null) {
            srl!!.postDelayed({
                refreshWithUi()
                onRefresh()
            }, delay)
        }
    }

    override fun refreshWithUi() {
        shouldRefreshUi = true
        android.os.Handler().postDelayed({
            if (shouldRefreshUi && srl != null) {
                srl!!.isRefreshing = true
            }
        }, 100L)
    }

    /**
     * adopt with [AppBarLayout] for smoother scrolling
     * when user is interacting with appbar, we should disable swipe to refresh layout
     *
     * @param appBar appbar inside fragment's layout
     */
    fun setupWithAppBarScrollingState(appBar: AppBarLayout) {
        appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: AppBarStateChangeListener.State) {
                if (state == AppBarStateChangeListener.State.EXPANDED) {
                    if (srl != null) {
                        Log.d("refresh_layout", "enabled")
                        srl!!.isEnabled = true
                    }
                } else {
                    if (srl != null && !isRefresh) {
                        Log.d("refresh_layout", "disabled")
                        srl!!.isEnabled = false
                    }
                }
            }
        })
    }

    /**
     * adopt with [ViewPager] for smoother scrolling
     * when user is interacting with viewpager, should disable swipe to refresh layout
     *
     * @param pager view pager inside fragment's layout
     */
    fun setupWithViewPagerScrollingState(pager: ViewPager) {
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            internal var mPreviousState = ViewPager.SCROLL_STATE_IDLE

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {
                // All of this is to inhibit any scrollable container from consuming our touch events as the user is changing pages
                if (mPreviousState == ViewPager.SCROLL_STATE_IDLE) {
                    if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                        srl!!.isEnabled = false
                    }
                } else {
                    if (state == ViewPager.SCROLL_STATE_IDLE || state == ViewPager.SCROLL_STATE_SETTLING) {
                        srl!!.isEnabled = true
                    }
                }
                mPreviousState = state
            }
        })
    }
}
