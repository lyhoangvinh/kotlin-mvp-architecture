package com.dev.lyhoangvinh.mvparchitecture

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import com.dev.lyhoangvinh.mvparchitecture.di.component.AppComponent
import com.dev.lyhoangvinh.mvparchitecture.di.component.DaggerAppComponent
import com.dev.lyhoangvinh.mvparchitecture.di.module.AppModule
import com.dev.lyhoangvinh.mvparchitecture.di.module.DataModule
import com.dev.lyhoangvinh.mvparchitecture.di.module.NetworkModule

class MyApplication : Application() {

    private var appComponent: AppComponent? = null

    private var mDeviceWidth = 0

    companion object {
        private lateinit var instance: MyApplication

        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        val displayMetrics = DisplayMetrics()
        val windowManager = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay?.getMetrics(displayMetrics)
        mDeviceWidth = displayMetrics.widthPixels
    }

    // component
    fun setupAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .dataModule(DataModule(this))
            .networkModule(NetworkModule(this))
            .build()
        appComponent?.inject(this)
    }

    fun getAppComponent(): AppComponent? {
        if (appComponent == null) {
            setupAppComponent()
        }
        return appComponent
    }

    fun get(activity: Activity): MyApplication {
        return activity.application as MyApplication
    }

    fun getDeviceWidth(): Int {
        return mDeviceWidth
    }
}