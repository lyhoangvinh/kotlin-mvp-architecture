package com.dev.lyhoangvinh.mvparchitecture

import android.app.Activity
import android.app.Application
import com.dev.lyhoangvinh.mvparchitecture.di.component.AppComponent
import com.dev.lyhoangvinh.mvparchitecture.di.component.DaggerAppComponent
import com.dev.lyhoangvinh.mvparchitecture.di.module.AppModule
import com.dev.lyhoangvinh.mvparchitecture.di.module.DataModule

class MyApplication : Application() {

    private var appComponent: AppComponent? = null

    companion object {
        private lateinit var instance: MyApplication

        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    // component
    fun setupAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .dataModule(DataModule(this))
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
}