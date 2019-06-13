package com.dev.lyhoangvinh.mvparchitecture.di.component

import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.MyApplication
import com.dev.lyhoangvinh.mvparchitecture.data.DatabaseManager
import com.dev.lyhoangvinh.mvparchitecture.data.SharedPrefs
import com.dev.lyhoangvinh.mvparchitecture.data.dao.*
import com.dev.lyhoangvinh.mvparchitecture.data.services.AvgleService
import com.dev.lyhoangvinh.mvparchitecture.data.services.ComicVineService
import com.dev.lyhoangvinh.mvparchitecture.di.module.AppModule
import com.dev.lyhoangvinh.mvparchitecture.di.module.DataModule
import com.dev.lyhoangvinh.mvparchitecture.di.module.NetworkModule
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ApplicationContext
import com.dev.lyhoangvinh.mvparchitecture.utils.ConnectionLiveData
import dagger.Component
import javax.inject.Singleton

/**
 * Created by lyhoangvinh on 05/01/18.
 */
@Singleton
@Component(modules = [AppModule::class, DataModule::class, NetworkModule::class])
interface AppComponent {

    @ApplicationContext
    fun context(): Context

    fun getDataBaseManager(): DatabaseManager

    fun getComicVineService(): ComicVineService

    fun getAvgleService(): AvgleService

    fun getSharedPrefs(): SharedPrefs

    fun getConnectionLiveData(): ConnectionLiveData

    fun comicsDao(): ComicsDao

    fun issuesDao(): IssuesDao

    fun categoriesDao(): CategoriesDao

    fun collectionDao(): CollectionDao

    fun videosDao(): VideosDao

    fun inject(app: MyApplication)
}