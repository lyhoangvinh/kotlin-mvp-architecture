package com.dev.lyhoangvinh.mvparchitecture.di.module

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.dev.lyhoangvinh.mvparchitecture.base.activity.BaseActivity
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityFragmentManager
import com.dev.lyhoangvinh.mvparchitecture.utils.NavigatorHelper
import dagger.Module
import dagger.Provides
import lyhoangvinh.com.myutil.navigation.ActivityNavigator
import lyhoangvinh.com.myutil.navigation.Navigator

@Module
class ActivityModule(private var activity: BaseActivity) {

    @Provides
    @ActivityContext
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): FragmentActivity {
        return activity
    }

    @Provides
    @ActivityFragmentManager
    fun provideFragmentManager(): FragmentManager {
        return activity.supportFragmentManager
    }

    @Provides
    fun provideNavigator(): Navigator {
        return ActivityNavigator(activity)
    }

    @Provides
    fun provideNavigatorHelper(navigator: Navigator): NavigatorHelper {
        return NavigatorHelper(navigator)
    }

    @Provides
    fun provideLifeCycleOwner(): LifecycleOwner {
        return activity
    }
}