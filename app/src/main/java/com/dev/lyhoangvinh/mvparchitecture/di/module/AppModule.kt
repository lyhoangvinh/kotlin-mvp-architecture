package com.dev.lyhoangvinh.mvparchitecture.di.module

import android.app.Application
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private var application: Application) {

    @Singleton
    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return application
    }
}