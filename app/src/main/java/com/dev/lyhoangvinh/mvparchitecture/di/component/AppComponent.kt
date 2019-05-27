package com.dev.lyhoangvinh.mvparchitecture.di.component

import android.content.Context
import com.dev.lyhoangvinh.mvparchitecture.MyApplication
import com.dev.lyhoangvinh.mvparchitecture.ui.base.api.ComicVineService
import com.dev.lyhoangvinh.mvparchitecture.database.DatabaseManager
import com.dev.lyhoangvinh.mvparchitecture.database.SharedPrefs
import com.dev.lyhoangvinh.mvparchitecture.database.dao.ComicsDao
import com.dev.lyhoangvinh.mvparchitecture.database.dao.IssuesDao
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

    fun getSharedPrefs(): SharedPrefs

    fun getConnectionLiveData(): ConnectionLiveData

    fun comicsDao(): ComicsDao

    fun issuesDao(): IssuesDao

    fun inject(app: MyApplication)
}