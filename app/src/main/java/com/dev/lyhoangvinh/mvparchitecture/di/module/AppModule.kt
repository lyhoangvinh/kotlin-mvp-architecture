package com.dev.lyhoangvinh.mvparchitecture.di.module

import android.app.Application
import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.database.SharedPrefs
import com.dev.lyhoangvinh.mvparchitecture.utils.DateDeserializer
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ApplicationContext
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import java.lang.reflect.Modifier
import java.util.*
import javax.inject.Singleton

@Module
class AppModule(private var application: Application) {

    @Singleton
    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .create()
    }


    @Singleton
    @Provides
    internal fun providesSharePeres(): SharedPrefs {
        return SharedPrefs.getInstance(application)
    }
}