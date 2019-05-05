package com.dev.lyhoangvinh.mvparchitecture.base.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.BaseView
import com.dev.lyhoangvinh.mvparchitecture.base.interfaces.UiRefreshable
import com.dev.lyhoangvinh.mvparchitecture.di.component.ActivityComponent
import com.dev.lyhoangvinh.mvparchitecture.di.component.DaggerActivityComponent
import com.dev.lyhoangvinh.mvparchitecture.di.getAppComponent
import com.dev.lyhoangvinh.mvparchitecture.di.module.ActivityModule
import org.greenrobot.eventbus.EventBus

/**
 * Created by lyhoangvinh on 04/01/18.
 */
abstract class BaseActivity : AppCompatActivity(), BaseView {

    private var dialog: Dialog? = null

    private var mActivityComponent: ActivityComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        if (shouldPostponeTransition()) {
            ActivityCompat.postponeEnterTransition(this)
        }
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this) && shouldRegisterEventBus())
            EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (shouldRegisterEventBus() && EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    // activity component, activity may or may not need this
    fun activityComponent(): ActivityComponent? {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .appComponent(getAppComponent(this))
                .build()
        }
        return mActivityComponent
    }

    /**
     * @return true if this activity should postpone transition (in case of destination view is in viewpager)
     */
    protected open fun shouldPostponeTransition(): Boolean {
        return false
    }

    protected open fun shouldRegisterEventBus(): Boolean {
        return false
    }

    abstract fun getLayoutResource(): Int

    override fun hideProgress() {
        if (this is UiRefreshable) {
            (this as UiRefreshable).doneRefresh()
        }
//        dialog?.let { if (it.isShowing) it.dismiss() }
        if (dialog != null && dialog!!.isShowing)
            dialog?.dismiss()
    }

    override fun showProgress() {
        hideProgress()
        if (dialog == null){
            dialog = showLoadingDialog()
        }
        dialog?.show()
    }

    fun showLoadingDialog(): Dialog {
        val progressDialog = Dialog(this)
        progressDialog.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setContentView(R.layout.progress_dialog)
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            return it
        }
    }
}