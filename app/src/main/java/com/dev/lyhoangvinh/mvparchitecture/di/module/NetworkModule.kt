package com.dev.lyhoangvinh.mvparchitecture.di.module

import android.app.Application
import com.dev.lyhoangvinh.mvparchitecture.ui.base.api.ComicVineService
import com.dev.lyhoangvinh.mvparchitecture.utils.makeOkHttpClientBuilder
import com.dev.lyhoangvinh.mvparchitecture.utils.makeService
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.OkHttpNoAuth
import com.dev.lyhoangvinh.mvparchitecture.utils.ConnectionLiveData
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class NetworkModule(private var context: Application) {

    @Provides
    @OkHttpNoAuth
    @Singleton
    internal fun provideOkHttpClientNoAuth(): OkHttpClient {
        return makeOkHttpClientBuilder(context).build()
    }

    @Provides
    @Singleton
    internal fun provideGithubService(gson: Gson, @OkHttpNoAuth okHttpClient: OkHttpClient): ComicVineService {
        return makeService(ComicVineService::class.java, gson, okHttpClient)
    }

    @Singleton
    @Provides
    internal fun providesConnectionLiveData(): ConnectionLiveData {
        return ConnectionLiveData(context)
    }
}
