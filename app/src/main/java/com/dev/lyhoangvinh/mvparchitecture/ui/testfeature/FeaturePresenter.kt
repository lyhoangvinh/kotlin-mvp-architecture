package com.dev.lyhoangvinh.mvparchitecture.ui.testfeature

import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.base.presenter.BasePresenter
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class FeaturePresenter @Inject constructor(@ActivityContext context: Context) : BasePresenter<FeatureView>(context) {


}