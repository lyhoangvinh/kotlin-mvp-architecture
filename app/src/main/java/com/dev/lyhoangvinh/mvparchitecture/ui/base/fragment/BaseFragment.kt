package com.dev.lyhoangvinh.mvparchitecture.ui.base.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
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
import com.dev.lyhoangvinh.mvparchitecture.ui.base.interfaces.UiRefreshable
import com.dev.lyhoangvinh.mvparchitecture.utils.hideKeyboard
import com.dev.lyhoangvinh.mvparchitecture.utils.showToastMessage
import org.greenrobot.eventbus.EventBus

/**
 * Created by lyhoangvinh on 04/01/18.
 */

@Suppress("UNCHECKED_CAST")
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
        if (this is UiRefreshable) {
            (this as UiRefreshable).doneRefresh()
        }
        dialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun showProgress() {
        hideProgress()
        dialog = showLoadingDialog(activity!!)
    }

    private fun showLoadingDialog(ctx: Context): Dialog {
        Dialog(ctx).let {
            it.show()
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setContentView(R.layout.progress_dialog)
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            return it
        }
    }

    override fun setProgress(show: Boolean) {
        if (show) {
            showProgress()
        } else {
            hideProgress()
        }
    }

    fun finishFragment() {
        hideKeyboard()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.finishAfterTransition()
        } else {
            activity?.finish()
        }
    }

    override fun showMessage(message: String) {
        showToastMessage(message)
    }

    /**
     * @return true if fragment should handle back press, false if not (activity will handle back press event)
     */
    fun onBackPressed(): Boolean {
        return false
    }
}