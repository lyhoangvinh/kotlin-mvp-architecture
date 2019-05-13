package com.dev.lyhoangvinh.mvparchitecture.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.dev.lyhoangvinh.mvparchitecture.database.dao.ComicsDao
import com.dev.lyhoangvinh.mvparchitecture.database.dao.IssuesDao
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Comics
import com.dev.lyhoangvinh.mvparchitecture.database.entinies.Issues
import com.dev.lyhoangvinh.mvparchitecture.database.typecoverter.ImageTypeConverter
import com.dev.lyhoangvinh.mvparchitecture.database.typecoverter.VolumeTypeConverter


@Database(entities = [Comics::class, Issues::class], version = 1, exportSchema = false)
@TypeConverters(ImageTypeConverter::class, VolumeTypeConverter::class)
abstract class DatabaseManager : RoomDatabase() {

    abstract fun comicsDao(): ComicsDao

    abstract fun issuesDao(): IssuesDao
}
