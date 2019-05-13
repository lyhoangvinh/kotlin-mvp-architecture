package com.dev.lyhoangvinh.mvparchitecture.ui.base.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.lyhoangvinh.mvparchitecture.R
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.BaseView
import com.dev.lyhoangvinh.mvparchitecture.di.component.DaggerFragmentComponent
import com.dev.lyhoangvinh.mvparchitecture.di.component.FragmentComponent
import com.dev.lyhoangvinh.mvparchitecture.utils.getAppComponent
import com.dev.lyhoangvinh.mvparchitecture.di.module.FragmentModule
import org.greenrobot.eventbus.EventBus

/**
 * Created by lyhoangvinh on 04/01/18.
 */
abstract class BaseFragment : Fragment(), BaseView {

    private var mFragmentComponent: FragmentComponent? = null

    private var dialog: Dialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResource(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize(view, activity)
        if (shouldRegisterEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (shouldRegisterEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    fun fragmentComponent(): FragmentComponent? {
        if (mFragmentComponent == null) {
            mFragmentComponent = DaggerFragmentComponent.builder()
                .fragmentModule(FragmentModule(this))
                .appComponent(getAppComponent(this))
                .build()
        }
        return mFragmentComponent
    }

    protected fun shouldRegisterEventBus(): Boolean {
        return false
    }

    protected abstract fun initialize(view: View, ctx: Context?)

    abstract fun getLayoutResource(): Int

    override fun hideProgress() {
        dialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun showProgress() {
        hideProgress()
        dialog = showLoadingDialog()
    }

    fun showLoadingDialog(): Dialog {
        val progressDialog = Dialog(activity)
        progressDialog.let {
            it.show()
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setContentView(R.layout.progress_dialog)
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            return it
        }
    }
}