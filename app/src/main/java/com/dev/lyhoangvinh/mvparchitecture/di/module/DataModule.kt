package com.dev.lyhoangvinh.mvparchitecture.di.module

import android.app.Application
import android.arch.persistence.room.Room
import com.dev.lyhoangvinh.mvparchitecture.data.DatabaseManager
import com.dev.lyhoangvinh.mvparchitecture.data.SharedPrefs
import com.dev.lyhoangvinh.mvparchitecture.data.dao.CategoriesDao
import com.dev.lyhoangvinh.mvparchitecture.data.dao.ComicsDao
import com.dev.lyhoangvinh.mvparchitecture.data.dao.IssuesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule(private var context: Application) {

    @Singleton
    @Provides
    fun providesRoomDatabase(): DatabaseManager {
        return Room.databaseBuilder(context, DatabaseManager::class.java, "my-database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideComicsDao(databaseManager: DatabaseManager): ComicsDao {
        return databaseManager.comicsDao()
    }

    @Provides
    @Singleton
    fun provideIssuesDao(databaseManager: DatabaseManager): IssuesDao {
        return databaseManager.issuesDao()
    }

    @Provides
    @Singleton
    fun provideCategoriesDao(databaseManager: DatabaseManager): CategoriesDao {
        return databaseManager.categoriesDao()
    }

    @Singleton
    @Provides
    internal fun providesSharePeres(): SharedPrefs {
        return SharedPrefs.getInstance(context)
    }
}