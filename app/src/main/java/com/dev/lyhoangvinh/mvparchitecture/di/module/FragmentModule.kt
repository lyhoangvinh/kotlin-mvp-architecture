package com.dev.lyhoangvinh.mvparchitecture.di.module

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityContext
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ActivityFragmentManager
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ChildFragmentManager
import com.dev.lyhoangvinh.mvparchitecture.di.scopes.PerFragment
import com.dev.lyhoangvinh.mvparchitecture.ui.base.fragment.BaseFragment
import com.dev.lyhoangvinh.mvparchitecture.utils.NavigatorHelper
import dagger.Module
import dagger.Provides
import lyhoangvinh.com.myutil.navigation.ActivityNavigator
import lyhoangvinh.com.myutil.navigation.ChildFragmentNavigator
import lyhoangvinh.com.myutil.navigation.FragmentNavigator
import lyhoangvinh.com.myutil.navigation.Navigator

@Module
class FragmentModule(private val mFragment: BaseFragment) {

    @Provides
    @PerFragment
    internal fun provideFragment(): Fragment = mFragment

    @Provides
    @PerFragment
    @ChildFragmentManager
    internal fun provideChildFragmentManager(): FragmentManager = mFragment.childFragmentManager

    @Provides
    internal fun provideActivity(): FragmentActivity? = mFragment.activity

    @Provides
    @ActivityFragmentManager
    internal fun provideFragmentManager(): FragmentManager? = mFragment.activity?.supportFragmentManager

    @Provides
    @ActivityContext
    internal fun provideContext(): Context = mFragment.context!!

    @Provides
    @PerFragment
    internal fun provideFragmentNavigator(): FragmentNavigator = ChildFragmentNavigator(mFragment)

    @Provides
    @PerFragment
    internal fun provideNavigatorHelper(navigator: FragmentNavigator): NavigatorHelper = NavigatorHelper(navigator)

    @Provides
    internal fun provideNavigator(): Navigator = ActivityNavigator(mFragment.activity)

    @Provides
    internal fun provideLifeCycleOwner(): LifecycleOwner = mFragment
}
