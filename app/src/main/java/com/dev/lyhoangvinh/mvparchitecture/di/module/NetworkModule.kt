package com.dev.lyhoangvinh.mvparchitecture.di.module

import android.app.Application
import com.dev.lyhoangvinh.mvparchitecture.Constants
import com.dev.lyhoangvinh.mvparchitecture.data.services.AvgleService
import com.dev.lyhoangvinh.mvparchitecture.data.services.ComicVineService
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.ApplicationContext
import com.dev.lyhoangvinh.mvparchitecture.di.qualifier.OkHttpNoAuth
import com.dev.lyhoangvinh.mvparchitecture.utils.ConnectionLiveData
import com.dev.lyhoangvinh.mvparchitecture.utils.makeOkHttpClientBuilder
import com.dev.lyhoangvinh.mvparchitecture.utils.makeService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class NetworkModule(@ApplicationContext private var context: Application) {

    @Provides
    @OkHttpNoAuth
    @Singleton
    internal fun provideOkHttpClientNoAuth(): OkHttpClient = makeOkHttpClientBuilder(context).build()

    @Provides
    @Singleton
    internal fun provideComicVineService(gson: Gson, @OkHttpNoAuth okHttpClient: OkHttpClient): ComicVineService =
        makeService(ComicVineService::class.java, gson, okHttpClient, Constants.COMIC_ENDPOINT)

    @Provides
    @Singleton
    internal fun provideAvgleService(gson: Gson, @OkHttpNoAuth okHttpClient: OkHttpClient): AvgleService =
        makeService(AvgleService::class.java, gson, okHttpClient, Constants.AVGLE_ENDPOINT)

    @Singleton
    @Provides
    internal fun providesConnectionLiveData(): ConnectionLiveData = ConnectionLiveData(context)
}
