package com.dev.lyhoangvinh.mvparchitecture.di

import android.app.Activity
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.lyhoangvinh.mvparchitecture.MyApplication
import com.dev.lyhoangvinh.mvparchitecture.di.component.AppComponent


fun getAppComponent(fragment: Fragment): AppComponent? {
    return getAppComponent(fragment.activity)
}

fun getAppComponent(activity: Activity?): AppComponent? {
    return activity?.let { MyApplication.getInstance().get(it).getAppComponent() }
}

fun ViewGroup.inflate(@LayoutRes layout: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layout, this, attachToRoot)
}