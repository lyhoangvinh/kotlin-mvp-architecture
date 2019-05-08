package com.dev.lyhoangvinh.mvparchitecture.di.component

import android.arch.lifecycle.LifecycleOwner
import android.support.v4.app.FragmentManager
import com.dev.lyhoangvinh.mvparchitecture.di.module.ActivityModule
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityFragmentManager
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.main.MainActivity
import com.dev.lyhoangvinh.mvparchitecture.ui.testfeature.FeatureActivity
import dagger.Component

@PerActivity
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {

    @ActivityFragmentManager
    fun defaultFragmentManager(): FragmentManager

    fun lifeCycleOwner(): LifecycleOwner

    fun inject(activity: MainActivity)

    fun inject(activity: FeatureActivity)
}