package com.dev.lyhoangvinh.mvparchitecture.di.module

import  android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.dev.lyhoangvinh.mvparchitecture.ui.base.activity.BaseActivity
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
    fun provideContext(): Context = activity

    @Provides
    fun provideActivity(): FragmentActivity = activity

    @Provides
    @ActivityFragmentManager
    fun provideFragmentManager(): FragmentManager = activity.supportFragmentManager

    @Provides
    fun provideNavigator(): Navigator = ActivityNavigator(activity)

    @Provides
    fun provideNavigatorHelper(navigator: Navigator): NavigatorHelper = NavigatorHelper(navigator)

    @Provides
    fun provideLifeCycleOwner(): LifecycleOwner = activity
}