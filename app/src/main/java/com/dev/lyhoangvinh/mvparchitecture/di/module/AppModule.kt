package com.dev.lyhoangvinh.mvparchitecture.di.module

import android.app.Application
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.utils.DateDeserializer
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ApplicationContext
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import lyhoangvinh.com.myutil.thread.BackgroundThreadExecutor
import lyhoangvinh.com.myutil.thread.UIThreadExecutor
import java.lang.reflect.Modifier
import java.util.*
import javax.inject.Singleton

@Module
class AppModule(private var application: Application) {

    @Singleton
    @Provides
    @ApplicationContext
    fun provideContext(): Context = application

    @Provides
    @Singleton
    fun provideGSon(): Gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
        .disableHtmlEscaping()
        .setPrettyPrinting()
        .registerTypeAdapter(Date::class.java, DateDeserializer())
        .create()

    @Provides
    @Singleton
    fun provideBackgroundThreadExecutor(): BackgroundThreadExecutor = BackgroundThreadExecutor.getInstance()

    @Provides
    @Singleton
    fun provideUIThreadExecutor(): UIThreadExecutor = UIThreadExecutor.getInstance()
}