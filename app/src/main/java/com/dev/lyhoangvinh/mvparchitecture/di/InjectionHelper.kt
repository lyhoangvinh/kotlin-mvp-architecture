package com.dev.lyhoangvinh.mvparchitecture.di

import android.app.Activity
import android.support.v4.app.Fragment
import com.dev.lyhoangvinh.mvparchitecture.MyApplication
import com.dev.lyhoangvinh.mvparchitecture.di.component.AppComponent


fun getAppComponent(fragment: Fragment): AppComponent? {
    return getAppComponent(fragment.activity)
}

fun getAppComponent(activity: Activity?): AppComponent? {
    return activity?.let { MyApplication.getInstance().get(it).getAppComponent() }
}