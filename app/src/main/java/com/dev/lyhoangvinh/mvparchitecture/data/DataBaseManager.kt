package com.dev.lyhoangvinh.mvparchitecture.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.dev.lyhoangvinh.mvparchitecture.data.dao.CategoriesDao
import com.dev.lyhoangvinh.mvparchitecture.data.dao.ComicsDao
import com.dev.lyhoangvinh.mvparchitecture.data.dao.IssuesDao
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.avgle.Category
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.comic.Comics
import com.dev.lyhoangvinh.mvparchitecture.data.entinies.comic.Issues
import com.dev.lyhoangvinh.mvparchitecture.data.typecoverter.ImageTypeConverter
import com.dev.lyhoangvinh.mvparchitecture.data.typecoverter.VolumeTypeConverter


@Database(entities = [Comics::class, Issues::class, Category::class], version = 1, exportSchema = false)
@TypeConverters(ImageTypeConverter::class, VolumeTypeConverter::class)
abstract class DatabaseManager : RoomDatabase() {

    abstract fun comicsDao(): ComicsDao

    abstract fun issuesDao(): IssuesDao

    abstract fun categoriesDao(): CategoriesDao
}
